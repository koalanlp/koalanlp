@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.utagger

import com.beust.klaxon.Json
import kr.bydelta.koala.POS
import kr.bydelta.koala.data.Morpheme
import kr.bydelta.koala.data.Word

/**
 * UTagger의 형태소 JSON 표현 규격
 *
 * @param morphemeInfo 형태소 정보. `[표면형, 의미번호, 품사표기]`
 * @param meaning 형태소 대역어 정보. `[;로 분리된 대역어 목록, 의미]`
 */
data class UMorpheme(@Json(name = "BSP") private val morphemeInfo: List<String>,
                     @Json(name = "ML") private val meaning: List<String>? = null) {

    /** 형태소 표면형 **/
    val surface: String by lazy { morphemeInfo[0] }

    /** 형태소 의미번호. (앞 2자리는 동형이의어, 뒷 6자리는 다의어) **/
    val wordSenseId: String? by lazy { morphemeInfo[1].takeIf { it.isNotBlank() } }

    /** 형태소 품사 표지 **/
    val tag: String by lazy { morphemeInfo[2] }

    /** 대역어가 있는 경우, 대역어 목록 **/
    val translatedWord: List<String>? by lazy { meaning?.get(0)?.split(';')?.map { it.trim() } }

    /** 대역어가 있는 경우, 대역어 언어권에서의 의미 **/
    val translatedMeaning: String? by lazy { meaning?.get(1) }

    /**
     * KoalaNLP의 [Morpheme] 형태로 변환
     *
     * @return 대응하는 [Morpheme] 인스턴스. 대역어 정보는 제외.
     */
    fun toMorpheme(): Morpheme {
        val morpheme = Morpheme(surface, POS.valueOf(tag), originalTag = tag)
        wordSenseId?.let { morpheme.setWordSense(it) }

        return morpheme
    }
}

/**
 * UTagger의 어절 JSON 표현 규격
 *
 * @param surface 어절의 표면형.
 * @param morphemes 형태소 목록. List<[UMorpheme]>.
 */
data class UWord(@Json(name = "SURF") val surface: String,
                 @Json(name = "MA") val morphemes: List<UMorpheme>) {

    /**
     * KoalaNLP의 [Word] 형태로 변환
     *
     * @return 대응하는 [Word] 인스턴스. 대역어 정보는 제외.
     */
    fun toWord(): Word {
        return Word(surface, morphemes.map { it.toMorpheme() })
    }
}
