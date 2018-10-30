@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.kkma

import kr.bydelta.koala.DependencyTag
import kr.bydelta.koala.POS
import kr.bydelta.koala.PhraseTag
import kr.bydelta.koala.data.DepEdge
import kr.bydelta.koala.data.Morpheme
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.data.Word
import kr.bydelta.koala.proc.CanParseDependency
import kr.bydelta.koala.proc.CanTagOnlyAParagraph
import org.snu.ids.kkma.ma.Eojeol
import org.snu.ids.kkma.ma.MCandidate

/**
 * 꼬꼬마 형태소분석기의 KoalaNLP Wrapper입니다.
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
 * @since 1.x
 */
class Tagger : CanTagOnlyAParagraph<org.snu.ids.kkma.ma.Sentence>() {

    /** 꼬꼬마 형태소분석기 객체. **/
    private val ma by lazy {
        Dictionary.reloadDic()
        org.snu.ids.kkma.ma.MorphemeAnalyzer()
    }

    /**
     * 변환되지않은, [text]의 분석결과 [List]<org.snu.ids.kkma.ma.Sentence>를 반환합니다.
     *
     * @since 1.x
     * @param text 분석할 String.
     * @return 원본 분석기의 결과인 문장의 목록입니다.
     */
    override fun tagParagraphOriginal(text: String): List<org.snu.ids.kkma.ma.Sentence> =
            if (text.isBlank()) emptyList()
            else
                ma.divideToSentences(
                        ma.leaveJustBest(
                                ma.postProcess(
                                        synchronized(Tagger) {
                                            ma.analyze(
                                                    text.trim()
                                            )
                                        }
                                )
                        )
                )

    /**
     * org.snu.ids.kkma.ma.Sentence 타입의 분석결과 [result]를 변환, [Sentence]를 구성합니다.
     *
     * @since 1.x
     * @param result 변환할 분석결과.
     * @return 변환된 [Sentence] 객체
     */
    override fun convertSentence(result: org.snu.ids.kkma.ma.Sentence): Sentence = Tagger.convertSentence(result)

    companion object {
        /**
         * org.snu.ids.kkma.ma.Sentence 타입의 분석결과 [result]를 변환, [Sentence]를 구성합니다.
         *
         * @since 1.x
         * @param result 변환할 분석결과.
         * @return 변환된 [Sentence] 객체
         */
        internal fun convertSentence(result: org.snu.ids.kkma.ma.Sentence): Sentence =
                Sentence(
                        result.map { eojeol ->
                            Word(
                                    eojeol.exp,
                                    eojeol.map { morph ->
                                        Morpheme(
                                                morph.string,
                                                morph.tag.toSejongPOS(),
                                                morph.tag
                                        )
                                    }
                            )
                        }
                )
    }
}

/**
 * 꼬꼬마 의존구문분석기의 KoalaNLP Wrapper입니다.
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
 * @since 1.x
 */
class Parser : CanParseDependency<org.snu.ids.kkma.ma.Sentence> {
    /** 의존관계분석기 **/
    val parser by lazy { org.snu.ids.kkma.sp.Parser() }
    /** 형태소분석기 **/
    val tagger by lazy { Tagger() }

    /**
     * org.snu.ids.kkma.ma.Sentence [sentence]를 변환하여 [List]<[Sentence]>로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 변환할 문장입니다.
     * @return 변환된 문장입니다.
     */
    override fun convert(sentence: org.snu.ids.kkma.ma.Sentence): Sentence = Tagger.convertSentence(sentence)

    /**
     * String [sentence]를 품사 분석하여 [List]<Pair<org.snu.ids.kkma.ma.Sentence, String>>로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 변환할 문장입니다.
     * @return 변환된 문장입니다.
     */
    override fun convert(sentence: String): List<Pair<org.snu.ids.kkma.ma.Sentence, String>> =
            tagger.tagParagraphOriginal(sentence).map { it to it.sentence }

    /**
     * [Sentence]를 변환하여 org.snu.ids.kkma.ma.Sentence로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 변환할 문장입니다.
     * @return 변환된 문장입니다.
     */
    override fun convert(sentence: Sentence): org.snu.ids.kkma.ma.Sentence {
        val sent = org.snu.ids.kkma.ma.Sentence()
        sentence.forEach { word ->
            val cand = MCandidate.create(word.surface,
                    word.joinToString("+") {
                        "${it.surface.replace("[", LSB) // []가 surface에 있는 경우 피함
                                .replace("]", RSB)
                                .replace("+", PLUS)
                                .replace("/", SLASH)}/${it.tag.fromSejongPOS()}"
                    })

            /** 대괄호 복원 */
            for (morph in cand) {
                if ("----" in morph.string) {
                    morph.string = morph.string.replace(LSB, "[")
                            .replace(RSB, "]")
                            .replace(PLUS, "+")
                            .replace(SLASH, "/")
                }
            }

            sent.add(Eojeol(cand))
        }

        return sent
    }

    /**
     * [item]을 분석하여 property 값을 반환합니다.
     *
     * @since 2.0.0
     * @param item 분석 단위 1개입니다.
     * @param sentence 원본 문장입니다.
     * @return 분석의 결과물입니다.
     */
    override fun attachProperty(item: org.snu.ids.kkma.ma.Sentence, sentence: String): Sentence {
        item.forEachIndexed { index, eojeol ->
            eojeol.firstMorp.index = index
        }

        val parse = parser.parse(item)
        val edgeList = parse.edgeList
        val nodeList = parse.nodeList
        val result = convert(item)

        result.setDepEdges(edgeList.map { e ->
            val from: Int = nodeList[e.fromId].eojeol?.firstMorp?.index ?: -1
            val to: Int = nodeList[e.toId].eojeol?.firstMorp?.index ?: -1
            val type = e.relation

            DepEdge(result.getOrNull(from), result[to], type.toETRIPhraseTag(), type.toETRIDepTag(),
                    originalLabel = type)
        })

        return result
    }

    /** Static fields */
    companion object {
        /** 왼쪽 대괄호 표시자 **/
        private const val LSB = "----LSB----"
        /** 오른쪽 대괄호 표시자 **/
        private const val RSB = "----RSB----"
        /** 덧셈기호 표시자 **/
        private const val PLUS = "----PLS----"
        /** Slash 표시자 **/
        private const val SLASH = "----SLS----"
    }
}
