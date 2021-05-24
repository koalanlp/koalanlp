@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.etri

import com.beust.klaxon.Json
import kr.bydelta.koala.*
import kr.bydelta.koala.data.*
import kotlin.math.max

/********* RESPONSE PAYLOADS *********/
/**
 * 형태소 분석 결과물
 *
 * @param id 형태소의 순번
 * @param lemma 형태소의 원형
 * @param type 형태소 품사분류
 * @param position 형태소의 텍스트 내 위치
 * @param weight 형태소 분석 결과의 신뢰도
 */
data class MorphemeResponse(val id: Int,
                            val lemma: String,
                            val type: String,
                            val position: Int,
                            val weight: Double) {

    /** [Morpheme]으로 변환합니다. */
    internal val morpheme by lazy { Morpheme(lemma, POS.valueOf(type), type) }
}

/**
 * 의미 분석 결과물
 *
 * @param id 의미 분석의 순번
 * @param text 분석 단위의 표면형
 * @param type 형태소 품사 분류
 * @param scode 의미 번호 (앞 2자리는 동형이의어, 뒷 6자리는 다의어)
 * @param weight 의미 분석 결과의 신뢰도
 * @param position 의미 분석 결과의 텍스트 내 위치
 * @param begin 의미 분석 결과의 시작 형태소 번호
 * @param end 의미 분석 결과의 끝 형태소 번호
 */
data class WordSenseResponse(val id: Int, val text: String, val type: String,
                             val scode: String, val weight: Double, val position: Int,
                             val begin: Int, val end: Int) {

    /** 의미 분석에 해당하는 형태소 결과값 */
    internal val morpheme: Morpheme by lazy {
        val morph = Morpheme(text, POS.valueOf(type), type)
        morph.setWordSense(scode)
        morph
    }
}

/**
 * 어절 분석 결과물
 * @param id 어절 순번
 * @param text 어절의 표면형
 * @param begin 어절 분석 결과의 시작 형태소 번호
 * @param end 어절 분석 결과의 끝 형태소 번호
 */
data class WordResponse(val id: Int, val text: String, val begin: Int, val end: Int) {

    /** 의미에 따라 형태소를 합쳐서 하나의 [Word]로 변환합니다. */
    fun toWords(senses: List<WordSenseResponse>): Word {
        val morphemes = senses.filter { it.begin >= begin && it.end <= end }.sortedBy { it.id }.map { it.morpheme }
        return Word(text, morphemes)
    }

    /** 의미에 따라 형태소를 합쳐서 하나의 [Word]로 변환합니다. */
    fun toWordsWithMorph(senses: List<MorphemeResponse>): Word {
        val morphemes = senses.filter { it.id in begin..end }.sortedBy { it.id }.map { it.morpheme }
        return Word(text, morphemes)
    }
}

/**
 * 개체명 인식 결과물
 * @param id 개체명 순번
 * @param text 개체명 표면형
 * @param type 개체명의 유형 (세분류)
 * @param begin 개체명 분석 결과의 시작 형태소 번호
 * @param end 개체명 분석 결과의 끝 형태소 번호
 * @param weight 개체명 분석 결과의 신뢰도
 * @param commonNoun 개체명 분석 결과가 일반명사이면 1, 고유명사이면 0
 */
data class EntityResponse(val id: Int, val text: String, val type: String,
                          val weight: Double, val begin: Int, val end: Int,
                          @Json(name = "common_noun") val commonNoun: Int) {

    /** 개체명 [Entity]로 변환합니다. */
    fun toEntity(senses: List<WordSenseResponse>): Entity {
        val morphemes = senses.filter { it.begin >= begin && it.end <= end }.sortedBy { it.id }.map { it.morpheme }
        return Entity(text, CoarseEntityType.valueOf(type.substring(0, 2)), type, morphemes, type)
    }

    /** 개체명 [Entity]로 변환합니다. */
    fun toEntityWithMorph(senses: List<MorphemeResponse>): Entity {
        val morphemes = senses.filter { it.id in begin..end }.sortedBy { it.id }.map { it.morpheme }
        return Entity(text, CoarseEntityType.valueOf(type.substring(0, 2)), type, morphemes, type)
    }
}

/**
 * 의존구문 분석 결과물
 *
 * @param id 의존구문분석 결과 어절의 ID
 * @param text 어절의 표면형
 * @param governor 부모 어절 (지배소)의 ID (Json Response의 'head')
 * @param label 의존관계 명칭 (지배소와 현재 어절의)
 * @param dependents 현재 어절의 피지배소의 ID 목록 (Json Response의 'mod')
 * @param weight 의존구문분석 결과의 신뢰도
 */
data class DependencyResponse(val id: Int, val text: String,
                              val label: String,
                              @Json(name = "head") val governor: Int,
                              @Json(name = "mod") val dependents: List<Int>,
                              val weight: Double) {

    /** 의존관계 Edge로 변환합니다. */
    fun toDepEdge(words: List<Word>): DepEdge {
        val head = if (governor == -1) null else words[governor]
        val tags = label.split("_", limit = 2)
        val ptag = PhraseTag.valueOf(tags[0])
        val dtype = if (tags.size > 1) DependencyTag.valueOf(tags[1].replace("PRN","UNDEF")) else null

        return DepEdge(head, words[id], ptag, dtype, originalLabel = label)
    }
}

/**
 * 의미역 부착의 각 논항 결과
 *
 * @param type 논항의 유형
 * @param id 논항의 어절 위치 (Json Response의 'word_id')
 * @param text 논항의 표면형
 * @param weight 논항 분석의 신뢰도.
 */
data class SRLArgument(val type: String,
                       @Json(name = "word_id") val id: Int,
                       val text: String, val weight: Double)

/**
 * 의미역 부착의 결과
 *
 * @param id 술어 어절의 위치 (Json Response의 'word_id')
 * @param predicate 술어 원형 (Json Response의 'verb')
 * @param sense 술어의 의미번호 (정수형)
 * @param weight 의미역 부착의 신뢰도
 * @param arguments 논항들 [SRLArgument]의 목록. (Json Response의 'argument')
 */
data class SRLResponse(@Json(name = "word_id") val id: Int,
                       @Json(name = "verb") val predicate: String,
                       val sense: Int,
                       val weight: Double,
                       @Json(name = "argument") val arguments: List<SRLArgument>) {

    /** 의미역 Edge로 변환합니다. */
    fun toRoleEdges(words: List<Word>): List<RoleEdge> {
        val predicateWord = words[id]
        return arguments.map {
            val argWord = words[it.id]
            val textWords = it.text.trim().split(' ')
            val wordIndex = textWords.indexOf(argWord.surface)

            val begin = max(0, if (wordIndex == -1) {
                // 찾을 수 없는 경우
                it.id - textWords.size
            } else {
                it.id - wordIndex
            })

            val end = begin + textWords.size
            val modifiers = words.subList(begin, end).sortedBy { w -> w.id }.filterNot { w -> it.id == w.id }

            val type = RoleType.valueOf(when (it.type) {
                "ARGA" -> "ARG0"
                else -> it.type.replace('-', '_')
            })

            RoleEdge(predicateWord, argWord, type, modifiers = modifiers, originalLabel = it.type)
        }
    }
}

/**
 * 분석 결과로 얻은 문장 1개를 표현하는 객체
 * @param id 문장 순번
 * @param surfaceString 문장의 표면형 (Json Response의 'text')
 * @param morphemes 형태소 분석 결과물 [MorphemeResponse]의 목록 (Json Response의 'morp')
 * @param words 어절 구분 결과물 [WordResponse]의 목록 (Json Response의 'word')
 * @param senses 의미 분석 결과물 [WordSenseResponse]의 목록 (Json Response의 'WSD')
 * @param entities 개체명 인식 결과물 [EntityResponse]의 목록 (Json Response의 'NE')
 * @param deps 의존구문분석 결과물 [DependencyResponse]의 목록 (Json Response의 'dependency')
 * @param roles 의미역 분석 결과물 [SRLResponse]의 목록 (Json Response의 'SRL')
 */
data class SentenceResponse(val id: Int,
                            @Json(name = "text") val surfaceString: String,
                            @Json(name = "morp") val morphemes: List<MorphemeResponse>,
                            @Json(name = "word") val words: List<WordResponse>,
                            @Json(name = "WSD") val senses: List<WordSenseResponse>,
                            @Json(name = "NE") val entities: List<EntityResponse>,
                            @Json(name = "dependency") val deps: List<DependencyResponse>,
                            @Json(name = "SRL") val roles: List<SRLResponse>) {

    /** 문장 1개로 변환합니다. */
    val sentence: Sentence by lazy {
        val sentence =
                if (senses.isEmpty()) {
                    // 형태소분석 결과만 사용가능한 경우, 형태소분석 결과로 처리

                    val sentence = Sentence(words.map { it.toWordsWithMorph(morphemes) })

                    if (entities.isNotEmpty())
                        sentence.setEntities(entities.map { it.toEntityWithMorph(morphemes) })

                    sentence
                } else {
                    // 이외의 경우는 동음이의어/다의어 분석 결과를 사용하도록 함.

                    val sentence = Sentence(words.map { it.toWords(senses) })

                    if (entities.isNotEmpty())
                        sentence.setEntities(entities.map { it.toEntity(senses) })

                    sentence
                }

        if (deps.isNotEmpty())
            sentence.setDepEdges(deps.map { it.toDepEdge(sentence) })

        if (roles.isNotEmpty())
            sentence.setRoleEdges(roles.flatMap { it.toRoleEdges(sentence) })

        sentence
    }
}

/**
 * 분석 결과 객체 (문장 정보 이외의 다른 정보는 무시)
 * @param sentences 분석된 문장들 [SentenceResponse]의 목록
 */
data class ResultContent(@Json(name = "sentence") val sentences: List<SentenceResponse> = emptyList())

/**
 * ETRI Open API의 분석 결과 Json을 해석한 객체
 *
 * @param code 분석 결과코드. 0: 정상, -1: 비정상
 * @param msg 서버 메시지 (오류가 있을때만 not null)
 * @param result 분석 결과 객체. [ResultContent] 또는 null
 */
data class ResultPayload(@Json(name = "result") val code: Int,
                         @Json(name = "reason") val msg: String = "",
                         @Json(name = "return_object") val result: ResultContent = ResultContent())
