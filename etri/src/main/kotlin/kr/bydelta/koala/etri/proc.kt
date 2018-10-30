@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.etri

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpPost
import kr.bydelta.koala.*
import kr.bydelta.koala.data.*
import kr.bydelta.koala.proc.*

/**
 * API 통신 과정에서 발생하는 문제를 담습니다.
 * @param statusCode HTTP 통신의 상태코드입니다.
 * @param msg 오류 메시지입니다.
 */
class APIException(val statusCode: Int, val msg: String) : Exception(msg)

/**
 * ETRI Open API와 통신을 하는 부분입니다.
 */
interface CanCommunicateETRIApi {
    /**
     * ETRI Open API의 Access Token 값입니다.
     */
    val apiKey: String

    /**
     * ETRI Open API와 통신합니다.
     *
     * @throws APIException ETRI 서버가 분석 결과값이 아닌 오류를 반환한 경우
     * @return 요청의 결과로 전달된 [ResultContent].
     */
    @Throws(APIException::class)
    fun request(apiKey: String, type: String, text: String): ResultContent {
        val klaxon = Klaxon()
        val requestBody = JsonObject(mapOf(
                "access_key" to apiKey,
                "argument" to JsonObject(mapOf(
                        "text" to text,
                        "analysis_code" to type
                ))
        )).toJsonString()

        val (_, resp, res) = "http://aiopen.etri.re.kr:8000/WiseNLU".httpPost()
                .jsonBody(requestBody).responseString()

        try {
            val resString = res.get().replace("\\.0([,}]+)".toRegex(), "$1")
            val json = klaxon.parse<ResultPayload>(resString)
            if (json?.code == 0) {
                return json.result
            } else {
                throw APIException(resp.statusCode, json?.msg ?: "HTTP Status code ${resp.statusCode}")
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

    /**
     * [ResultPayload] 유형의 요청 결과를 Koala의 [Sentence] 목록으로 변환합니다.
     *
     * @param result 변환할 요청 결과
     * @return 변환된 [Sentence]의 목록.
     */
    fun convertPayload(result: SentenceResponse): Sentence = result.sentence
}

/**
 * ETRI API와 통신을 하는 [CanAnalyzeProperty] 인터페이스입니다.
 */
interface CanParseWithETRI : CanCommunicateETRIApi, CanAnalyzeProperty<SentenceResponse> {
    /** 분석을 요청할 유형 */
    val requestType: String

    /**
     * [item]을 분석하여 property 값을 반환합니다.
     *
     * @since 2.0.0
     * @param item 분석 단위 1개입니다.
     * @param sentence 원본 문장입니다.
     * @return 분석의 결과물입니다.
     */
    override fun attachProperty(item: SentenceResponse, sentence: String): Sentence = convert(item)

    /**
     * 분석기의 중간 결과인 [sentence]를 조합하여 [Sentence] 객체로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 변환할 문장입니다.
     * @return [Sentence] 객체입니다.
     */
    override fun convert(sentence: SentenceResponse): Sentence = sentence.sentence

    /**
     * String [sentence]를 품사 분석하여 분석기가 받아들이는 [List]<Pair<[SentenceResponse], String>>으로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 텍스트에서 변환할 문장입니다.
     * @return 분석기가 받아들일 수 있는 형태의 데이터입니다. 각 Pair의 [Pair.first] 값은 변환된 문장 객체이며, [Pair.second] 값은 해당 문장의 String 값입니다.
     */
    override fun convert(sentence: String): List<Pair<SentenceResponse, String>> {
        val result = request(apiKey, requestType, sentence)

        return result.sentences.map { it to it.surfaceString }
    }

    /**
     * Sentence [sentence]를 해체하여 분석기가 받아들이는 [SentenceResponse]로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 변환할 문장입니다.
     * @return 분석기가 받아들일 수 있는 형태의 데이터입니다.
     */
    override fun convert(sentence: Sentence): SentenceResponse {
        throw UnsupportedOperationException("ETRI Open API는 텍스트 문장만 입력으로 받을 수 있습니다.")
    }
}

/**
 * ETRI 품사 분석 API의 Wrapper입니다.
 *
 * * 품사 분석이 가장 기본적인 분석이기 때문에, ETRI에 다른 분석을 요청 할 때 품사 분석이 자동으로 진행됩니다.
 *   따라서, [Parser], [EntityRecognizer], [RoleLabeler]등을 사용하는 경우, Tagger는 호출하지 않아도 됩니다.
 *
 * * Open API 통신 과정에서 [APIException]이 발생할 수 있습니다.
 *
 * ## 참고
 * **형태소**는 의미를 가지는 요소로서는 더 이상 분석할 수 없는 가장 작은 말의 단위로 정의됩니다.
 *
 * **형태소 분석**은 문장을 형태소의 단위로 나누는 작업을 의미합니다.
 * 예) '문장을 형태소로 나눠봅시다'의 경우,
 * * 문장/일반명사, -을/조사,
 * * 형태소/일반명사, -로/조사,
 * * 나누-(다)/동사, -어-/어미, 보-(다)/동사, -ㅂ시다/어미
 * 로 대략 나눌 수 있습니다.
 *
 * 아래를 참고해보세요.
 * * [Morpheme] 형태소를 저장하는 클래스입니다.
 * * [POS] 형태소의 분류를 담은 Enum class
 *
 * ## 사용법 예제
 *
 * ### Kotlin
 * ```kotlin
 * val tagger = Tagger()
 * val sentence = tagger.tagSentence("문장 1개입니다.")
 * val sentences = tagger.tag("문장들입니다. 결과는 목록이 됩니다.")
 * // 또는
 * val sentences = tagger("문장들입니다. 결과는 목록이 됩니다.")
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/scala-support/)
 * ```scala
 * import kr.bydelta.koala.Implicits._
 * val tagger = new Tagger()
 * val sentence = tagger.tagSentence("문장 1개입니다.")
 * val sentences = tagger.tag("문장들입니다. 결과는 목록이 됩니다.")
 * // 또는
 * val sentences = tagger("문장들입니다. 결과는 목록이 됩니다.")
 * ```
 *
 * ### Java
 * ```java
 * Tagger tagger = new Tagger()
 * Sentence sentence = tagger.tagSentence("문장 1개입니다.")
 * List<Sentence> sentences = tagger.tag("문장들입니다. 결과는 목록이 됩니다.")
 * // 또는
 * List<Sentence> sentences = tagger.invoke("문장들입니다. 결과는 목록이 됩니다.")
 * ```
 *
 * @since 2.0.0
 * @constructor 분석기를 생성합니다.
 * @param apiKey ETRI Open API Access token
 * @param analysisType 품사 분석을 위한 분석 타입: "wsd"(동형이의어 분석), "wsd_poly"(다의어 분석), "morp"(형태소 분석)이며, 기본값은 "wsd"입니다.
 */
class Tagger(override val apiKey: String,
             val analysisType: String = "wsd") : CanTagOnlyAParagraph<SentenceResponse>(), CanCommunicateETRIApi {
    init {
        assert(analysisType in listOf("wsd", "wsd_poly", "morp"))
    }

    /**
     * [ResultPayload] 타입의 분석결과 [result]를 변환, [Sentence]를 구성합니다.
     *
     * @since 2.0.0
     * @param result 변환할 분석결과.
     * @return 변환된 [Sentence] 객체
     */
    override fun convertSentence(result: SentenceResponse): Sentence = convertPayload(result)

    /**
     * 변환되지않은, [text]의 분석결과 [List]<[ResultPayload]>를 반환합니다.
     *
     * @since 2.0.0
     * @throws APIException ETRI 서버가 분석 결과값이 아닌 오류를 반환한 경우
     * @param text 분석할 String.
     * @return 원본 분석기의 결과인 문장의 목록입니다.
     */
    @Throws(APIException::class)
    override fun tagParagraphOriginal(text: String): List<SentenceResponse> =
            if (text.isBlank()) emptyList()
            else request(apiKey, analysisType, text).sentences
}

/**
 * 의존구문분석을 수행하는 Interface입니다.
 *
 * * 의존구문분석을 수행하면 자동으로 [Tagger], [Parser], [EntityRecognizer]가 함께 호출됩니다.
 * * 의존구문분석은 [RoleLabeler]를 하기 위한 기초 분석 과정이므로, [RoleLabeler]를 수행하면 의존구문분석이 자동으로 진행됩니다.
 *
 * * Open API 통신 과정에서 [APIException]이 발생할 수 있습니다.
 *
 * ## 참고
 * **의존구조 분석**은 문장의 구성 어절들이 의존 또는 기능하는 관계를 분석하는 방법입니다.
 * 예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
 * 가장 마지막 단어인 '쌌다'가 핵심 어구가 되며,
 * * '먹었고'가 '쌌다'와 대등하게 연결되고
 * * '나는'은 '먹었고'의 주어로 기능하며
 * * '밥을'은 '먹었고'의 목적어로 기능합니다.
 * * '영희는'은 '쌌다'의 주어로 기능하고,
 * * '짐을'은 '쌌다'의 목적어로 기능합니다.
 *
 * 아래를 참고해보세요.
 * * [Word.getDependentEdges] 어절이 직접 지배하는 하위 의존구조 [DepEdge]의 목록을 가져오는 API
 * * [Word.getGovernorEdge] 어절이 지배당하는 상위 의존구조 [DepEdge]를 가져오는 API
 * * [Sentence.getDependencies] 전체 문장을 분석한 의존구조 [DepEdge]의 목록을 가져오는 API
 * * [DepEdge] 의존구조를 저장하는 형태
 * * [PhraseTag] 의존구조의 형태 분류를 갖는 Enum 값 (구구조 분류와 같음)
 * * [DependencyTag] 의존구조의 기능 분류를 갖는 Enum 값
 *
 * ## 사용법 예제
 * 분석기 `Parser`가 `CanParseDependency`를 상속받았다면,
 *
 * ### Kotlin
 * ```kotlin
 * // 문장에서 바로 분석할 때
 * val parser = Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: List<Sentence> = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/scala-support/)
 * ```scala
 * import kr.bydelta.koala.Implicits._
 * // 문장에서 바로 분석할 때
 * val parser = new Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: java.util.List[Sentence] = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Java
 * ```java
 * // 문장에서 바로 분석할 때
 * Parser parser = Parser()
 * List<Sentence> sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser.invoke("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * Sentence taggedSentence = ...
 * Sentence sentence = parser.analyze(taggedSentence) // 또는 parser.invoke(taggedSentence)
 *
 * List<Sentence> taggedSentList = ...
 * List<Sentence> sentences = parser.analyze(taggedSentList) // 또는 parser.invoke(taggedSentList)
 * ```
 *
 * @since 2.0.0
 * @constructor 분석기를 생성합니다.
 * @param apiKey ETRI Open API Access token
 */
class Parser(override val apiKey: String) : CanParseDependency<SentenceResponse>, CanParseWithETRI {
    /** 분석을 요청할 유형 */
    override val requestType: String get() = "dparse"
}

/**
 * 의미역 분석(Semantic Role Labeling)을 수행하는 Interface입니다.
 *
 * * 의미역 분석을 수행하면 자동으로 [Tagger], [Parser], [EntityRecognizer], [RoleLabeler]가 함께 호출됩니다.
 *
 * * Open API 통신 과정에서 [APIException]이 발생할 수 있습니다.
 *
 * ## 참고
 * **의미역 결정**은 문장의 구성 어절들의 역할/기능을 분석하는 방법입니다.
 * 예) '나는 밥을 어제 집에서 먹었다'라는 문장에는
 * 동사 '먹었다'를 중심으로
 * * '나는'은 동작의 주체를,
 * * '밥을'은 동작의 대상을,
 * * '어제'는 동작의 시점을
 * * '집에서'는 동작의 장소를 나타냅니다.
 *
 * 아래를 참고해보세요.
 * * [Word.getArgumentRoles] 어절이 술어인 논항들의 [RoleEdge] 목록을 가져오는 API
 * * [Word.getPredicateRole] 어절이 논항인 [RoleEdge]의 술어를 가져오는 API
 * * [Sentence.getRoles] 전체 문장을 분석한 의미역 구조 [RoleEdge]를 가져오는 API
 * * [RoleEdge] 의미역 구조를 저장하는 형태
 * * [RoleType] 의미역 분류를 갖는 Enum 값
 *
 * ## 사용법 예제
 * 분석기 `Parser`가 `CanLabelSemanticRole`을 상속받았다면,
 *
 * ### Kotlin
 * ```kotlin
 * // 문장에서 바로 분석할 때
 * val parser = Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: List<Sentence> = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/scala-support/)
 * ```scala
 * import kr.bydelta.koala.Implicits._
 * // 문장에서 바로 분석할 때
 * val parser = new Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: java.util.List[Sentence] = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Java
 * ```java
 * // 문장에서 바로 분석할 때
 * Parser parser = Parser()
 * List<Sentence> sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser.invoke("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * Sentence taggedSentence = ...
 * Sentence sentence = parser.analyze(taggedSentence) // 또는 parser.invoke(taggedSentence)
 *
 * List<Sentence> taggedSentList = ...
 * List<Sentence> sentences = parser.analyze(taggedSentList) // 또는 parser.invoke(taggedSentList)
 * ```
 *
 * @since 2.0.0
 * @constructor 분석기를 생성합니다.
 * @param apiKey ETRI Open API Access token
 */
class RoleLabeler(override val apiKey: String) : CanLabelSemanticRole<SentenceResponse>, CanParseWithETRI {
    /** 분석을 요청할 유형 */
    override val requestType: String get() = "srl"
}

/**
 * 개체명 인식 (Named Entity Recognition)을 수행하는 Interface입니다.
 *
 * * 개체명 인식을 수행하면 자동으로 [Tagger]가 함께 호출됩니다.
 * * 개체명 인식은 [RoleLabeler], [Parser]를 하기 위한 기초 분석 과정이므로, 두 분석을 수행하면 의존구문분석이 자동으로 진행됩니다.
 *
 * * Open API 통신 과정에서 [APIException]이 발생할 수 있습니다.
 *
 * ## 참고
 * **개체명 인식**은 문장에서 인물, 장소, 기관, 대상 등을 인식하는 기술입니다.
 * 예) '철저한 진상 조사를 촉구하는 국제사회의 목소리가 커지고 있는 가운데, 트럼프 미국 대통령은 되레 사우디를 감싸고 나섰습니다.'에서, 다음을 인식하는 기술입니다.
 * * '트럼프': 인물
 * * '미국' : 국가
 * * '대통령' : 직위
 * * '사우디' : 국가
 *
 * 아래를 참고해보세요.
 * * [Word.getEntities] 어절이 속하는 [Entity]를 가져오는 API
 * * [Sentence.getEntities] 문장에 포함된 모든 [Entity]를 가져오는 API
 * * [Entity] 개체명을 저장하는 형태
 * * [CoarseEntityType] [Entity]의 대분류 개체명 분류구조 Enum 값
 *
 * ## 사용법 예제
 * 분석기 `Parser`가 `CanRecognizeEntity`를 상속받았다면,
 *
 * ### Kotlin
 * ```kotlin
 * // 문장에서 바로 분석할 때
 * val parser = Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: List<Sentence> = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/scala-support/)
 * ```scala
 * import kr.bydelta.koala.Implicits._
 * // 문장에서 바로 분석할 때
 * val parser = new Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: java.util.List[Sentence] = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Java
 * ```java
 * // 문장에서 바로 분석할 때
 * Parser parser = Parser()
 * List<Sentence> sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser.invoke("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * Sentence taggedSentence = ...
 * Sentence sentence = parser.analyze(taggedSentence) // 또는 parser.invoke(taggedSentence)
 *
 * List<Sentence> taggedSentList = ...
 * List<Sentence> sentences = parser.analyze(taggedSentList) // 또는 parser.invoke(taggedSentList)
 * ```
 *
 * @since 2.0.0
 * @constructor 분석기를 생성합니다.
 * @param apiKey ETRI Open API Access token
 */
class EntityRecognizer(override val apiKey: String) : CanRecognizeEntity<SentenceResponse>, CanParseWithETRI {
    /** 분석을 요청할 유형 */
    override val requestType: String get() = "ner"
}