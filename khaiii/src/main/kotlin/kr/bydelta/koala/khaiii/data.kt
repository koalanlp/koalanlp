@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.khaiii

import com.sun.jna.Structure

/**
 * Khaiii API에서 출력하는 형태소 결과 Struct의 JNA 대응 Class
 */
@Structure.FieldOrder("lex", "tag", "begin", "length", "reserved", "next")
open class KhaiiiMorph : Structure() {
    /**
     * KhaiiiMorph의 Pointer Reference 값 Class
     */
    class ByReference : KhaiiiMorph(), Structure.ByReference

    init {
        // String encoding UTF8로 강제
        this.stringEncoding = "UTF-8"
    }

    /** Lexical (형태소 원형) */
    @JvmField
    var lex: String? = null
    /** POS Tag */
    @JvmField
    var tag: String? = null
    /** 형태소 시작 위치 */
    @JvmField
    var begin: Int? = null
    /** 형태소 길이 */
    @JvmField
    var length: Int? = null
    /** (예약) */
    @JvmField
    var reserved: ByteArray = ByteArray(8)
    /** 다음 형태소 포인터 */
    @JvmField
    var next: KhaiiiMorph.ByReference? = null
}

/**
 * Khaiii API에서 출력하는 어절 결과 Struct의 JNA 대응 Class
 */
@Structure.FieldOrder("begin", "length", "reserved", "morphs", "next")
open class KhaiiiWord : Structure() {
    /**
     * KhaiiiWord의 Pointer Reference 값 Class
     */
    class ByReference : KhaiiiWord(), Structure.ByReference

    /** 어절 시작 위치 */
    @JvmField
    var begin: Int? = null
    /** 어절 길이 **/
    @JvmField
    var length: Int? = null
    /** (예약) */
    @JvmField
    var reserved: ByteArray = ByteArray(8)
    /** 형태소 목록 (첫 형태소 포인터) */
    @JvmField
    var morphs: KhaiiiMorph.ByReference? = null
    /** 다음 어절 포인터 */
    @JvmField
    var next: KhaiiiWord.ByReference? = null
}


/**
 * Khaiii 설정
 * @param preanal 기분석 사전 사용 여부 (기본값 true)
 * @param errpatch 오분석 사전 사용 여부 (기본값 true)
 * @param restore 형태소 재구성 여부 (기본값 true)
 */
data class KhaiiiConfig(val preanal: Boolean = true,
                        val errpatch: Boolean = true,
                        val restore: Boolean = true)