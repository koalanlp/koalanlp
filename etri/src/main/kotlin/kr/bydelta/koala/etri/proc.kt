package kr.bydelta.koala.etri

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpPost
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.proc.*
import java.util.concurrent.TimeoutException

data class PostRequest(@Json(name = "analysis_code") val type: String,
                       val text: String)

data class PostPayload(@Json(name = "access_key") val apiKey: String,
                       @Json(name = "argument") val request: PostRequest)

data class MorphemeResponse(val id: Int, val lemma: String, val type: String,
                            val position: Int, val weight: Double)

data class WordResponse(val id: Int, val text: String, val type: String,
                        val begin: Int, val end: Int)

data class WordSenseResponse(val id: Int, val text: String, val type: String,
                             val scode: String, val weight: Double, val position: Int,
                             val begin: Int, val end: Int)

data class EntityResponse(val id: Int, val text: String, val type: String,
                          val weight: Double, val pronoun: Boolean,
                          val begin: Int, val end: Int)

data class DependencyResponse(val id: Int, val text: String, val head: Int,
                              val label: String, val mod: List<Int>, val weight: Double)

data class SRLResponse(val verb: String, val sense: String,
                       val word_id: Int, val weight: Double,
                       val argument: String, val type: String) {
    init {
        TODO("Implementation check required.")
    }
}

data class SentenceResponse(@Json(name = "morp") val morphemes: List<MorphemeResponse>?,
                            @Json(name = "word") val words: List<WordResponse>?,
                            @Json(name = "wsd") val senses: List<WordSenseResponse>?,
                            @Json(name = "ne") val entities: List<EntityResponse>?,
                            @Json(name = "dependency") val deps: List<DependencyResponse>?,
                            @Json(name = "srl") val roles: List<SRLResponse>?,
                            @Json(name = "text") val surfaceString: String)

data class ResultContent(@Json(name = "sentence") val sentences: List<SentenceResponse>)

data class ResultPayload(@Json(name = "result") val code: String,
                         @Json(name = "return_object") val result: ResultContent)

@Throws(IllegalArgumentException::class,
        IllegalStateException::class,
        IllegalAccessException::class,
        UnsupportedOperationException::class,
        TimeoutException::class,
        InternalError::class)
fun request(apiKey: String, type: String, text: String): String {
    val klaxon = Klaxon()
    val (req, resp, res) = "http://aiopen.etri.re.kr:8000/WiseNLU".httpPost()
            .jsonBody(klaxon.toJsonString(PostPayload(apiKey, PostRequest(type, text)))).responseString()

    when (resp.statusCode) {
        400 -> throw IllegalArgumentException("필수 항목의 값이 없거나 유효하지 않습니다.")
//            400 	-1 	Required arguments is empty 	필수 파라미터의 값이 없는 경우
//        400 	-1 	One or more arguments are not valid 	파라미터의 값이 유효하지 않는 경우

        413 -> throw IllegalStateException("요청한 문장 또는 어휘의 크기가 서버 용량보다 큽니다.")
//        413 	-1 	Request Entity Too Large 	요청 문장 또는 어휘의 크기가 서버가 처리 할 수 있는 것보다 큰 경우

        429 -> throw IllegalAccessException("주어진 시간 내에 서버에 너무 많은 요청을 전달했습니다.")
//                429 	-1 	Too Many Requests 	사용자가 주어진 시간 내에 서버에 너무 많은 요청을 하는 경우

        404 -> throw UnsupportedOperationException("등록되지 않은 서비스를 요청했습니다.")
//                404 	-1 	Unknown Handler 	등록되지 않는 서비스를 요청한 경우

        408 -> throw TimeoutException("서버 요청이 서버에서 지정된 시간을 초과했습니다.")
//                408 	-1 	Handler Timeout 	서버의 요청 대기가 시간을 초과한 경우

        500 -> throw InternalError("ETRI 분석 서버에 장애가 발생했거나, 현재 요청을 처리할 수 없습니다.")
//            500 	-1 	ETRI service server connection refused 	ETRI 분석 서버에서 요청을 받지 못하는 경우
//        500 	-1 	ETRI service server is not exists 	수행 가능한 ETRI 분석 서버가 없는 경우
//                500 	-1 	Recipient Failure 	ETRI 분석 서버에서 요청을 처리하지 못하는 경우
//                500 	-1 	Unknown ReplyFailure 	API 요청에 알 수 없는 내부 오류가 발생한 경우
//                500 	-1 	Unknown Exception 	알 수 없는 내부 오류가 발생한 경우

        else -> {
            println(res)
//            val resJson = klaxon.parse<ResultPayload>(res.get())
        }
    }
}

class Tagger(apiKey: String) : CanTagOnlyAParagraph<ResultPayload>() {
    /**
     * [ResultPayload] 타입의 분석결과 [result]를 변환, [Sentence]를 구성합니다.
     *
     * @since 1.x
     * @param result 변환할 분석결과.
     * @return 변환된 [Sentence] 객체
     */
    override fun convertSentence(result: ResultPayload): Sentence {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 변환되지않은, [text]의 분석결과 [List]<[ResultPayload]>를 반환합니다.
     *
     * @since 1.x
     * @param text 분석할 String.
     * @return 원본 분석기의 결과인 문장의 목록입니다.
     */
    override fun tagParagraphOriginal(text: String): List<ResultPayload> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class Parser(apiKey: String) : CanParseSyntax<String>, CanParseDependency<String> {
    /**
     * [item]을 분석하여 property 값을 반환합니다.
     *
     * @since 2.0.0
     * @param item 분석 단위 1개입니다.
     * @param sentence 원본 문장입니다.
     * @return 분석의 결과물입니다.
     */
    override fun attachProperty(item: String, sentence: String): Sentence {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 분석기의 중간 결과인 [sentence]를 조합하여 [Sentence] 객체로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 변환할 문장입니다.
     * @return [Sentence] 객체입니다.
     */
    override fun convert(sentence: String): Sentence {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * String [sentence]를 품사 분석하여 분석기가 받아들이는 [List]<Pair<[INTERMEDIATE], String>>으로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 텍스트에서 변환할 문장입니다.
     * @return 분석기가 받아들일 수 있는 형태의 데이터입니다. 각 Pair의 [Pair.first] 값은 변환된 문장 객체이며, [Pair.second] 값은 해당 문장의 String 값입니다.
     */
    override fun convert(sentence: String): List<Pair<String, String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Sentence [sentence]를 해체하여 분석기가 받아들이는 [INTERMEDIATE]로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 변환할 문장입니다.
     * @return 분석기가 받아들일 수 있는 형태의 데이터입니다.
     */
    override fun convert(sentence: Sentence): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class EntityRecongizer(apiKey: String) : CanRecognizeEntity<String> {
    /**
     * [item]을 분석하여 property 값을 반환합니다.
     *
     * @since 2.0.0
     * @param item 분석 단위 1개입니다.
     * @param sentence 원본 문장입니다.
     * @return 분석의 결과물입니다.
     */
    override fun attachProperty(item: String, sentence: String): Sentence {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 분석기의 중간 결과인 [sentence]를 조합하여 [Sentence] 객체로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 변환할 문장입니다.
     * @return [Sentence] 객체입니다.
     */
    override fun convert(sentence: String): Sentence {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * String [sentence]를 품사 분석하여 분석기가 받아들이는 [List]<Pair<[INTERMEDIATE], String>>으로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 텍스트에서 변환할 문장입니다.
     * @return 분석기가 받아들일 수 있는 형태의 데이터입니다. 각 Pair의 [Pair.first] 값은 변환된 문장 객체이며, [Pair.second] 값은 해당 문장의 String 값입니다.
     */
    override fun convert(sentence: String): List<Pair<String, String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Sentence [sentence]를 해체하여 분석기가 받아들이는 [INTERMEDIATE]로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 변환할 문장입니다.
     * @return 분석기가 받아들일 수 있는 형태의 데이터입니다.
     */
    override fun convert(sentence: Sentence): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class RoleLabler(apiKey: String) : CanLabelSemanticRole<String> {
    /**
     * [item]을 분석하여 property 값을 반환합니다.
     *
     * @since 2.0.0
     * @param item 분석 단위 1개입니다.
     * @param sentence 원본 문장입니다.
     * @return 분석의 결과물입니다.
     */
    override fun attachProperty(item: String, sentence: String): Sentence {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 분석기의 중간 결과인 [sentence]를 조합하여 [Sentence] 객체로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 변환할 문장입니다.
     * @return [Sentence] 객체입니다.
     */
    override fun convert(sentence: String): Sentence {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * String [sentence]를 품사 분석하여 분석기가 받아들이는 [List]<Pair<[INTERMEDIATE], String>>으로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 텍스트에서 변환할 문장입니다.
     * @return 분석기가 받아들일 수 있는 형태의 데이터입니다. 각 Pair의 [Pair.first] 값은 변환된 문장 객체이며, [Pair.second] 값은 해당 문장의 String 값입니다.
     */
    override fun convert(sentence: String): List<Pair<String, String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Sentence [sentence]를 해체하여 분석기가 받아들이는 [INTERMEDIATE]로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 변환할 문장입니다.
     * @return 분석기가 받아들일 수 있는 형태의 데이터입니다.
     */
    override fun convert(sentence: Sentence): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class WordSenseDisambiguator(apiKey: String) : CanDisambiguateSense<String> {
    /**
     * [item]을 분석하여 property 값을 반환합니다.
     *
     * @since 2.0.0
     * @param item 분석 단위 1개입니다.
     * @param sentence 원본 문장입니다.
     * @return 분석의 결과물입니다.
     */
    override fun attachProperty(item: String, sentence: String): Sentence {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 분석기의 중간 결과인 [sentence]를 조합하여 [Sentence] 객체로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 변환할 문장입니다.
     * @return [Sentence] 객체입니다.
     */
    override fun convert(sentence: String): Sentence {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * String [sentence]를 품사 분석하여 분석기가 받아들이는 [List]<Pair<[INTERMEDIATE], String>>으로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 텍스트에서 변환할 문장입니다.
     * @return 분석기가 받아들일 수 있는 형태의 데이터입니다. 각 Pair의 [Pair.first] 값은 변환된 문장 객체이며, [Pair.second] 값은 해당 문장의 String 값입니다.
     */
    override fun convert(sentence: String): List<Pair<String, String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Sentence [sentence]를 해체하여 분석기가 받아들이는 [INTERMEDIATE]로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 변환할 문장입니다.
     * @return 분석기가 받아들일 수 있는 형태의 데이터입니다.
     */
    override fun convert(sentence: Sentence): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}