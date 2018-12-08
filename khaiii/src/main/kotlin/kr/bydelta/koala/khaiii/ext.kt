@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.khaiii

import kr.bydelta.koala.POS
import java.util.logging.Level

/**
 * 입력받은 세종 품사 표기를 카이 분석기의 품사 표기로 변환합니다.
 *
 * * 변환표는 [여기](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s Conversion Table (Korean))에서 볼 수 있습니다.
 *
 * ## 사용 예
 * ### Kotlin
 * ```kotlin
 * POS.NNG.fromSejongPOS()
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/scala-support)
 * ```scala
 * POS.NNG.fromSejongPOS()
 * ```
 *
 * ### Java
 * ```java
 * Util.fromSejongPOS(POS.NNG)
 * ```
 *
 * @since 1.x
 * @return 대응하는 코모란 품사의 값
 */
fun POS.fromSejongPOS(): String =
        when (this) {
            POS.NF -> "ZN"
            POS.NV -> "ZV"
            POS.NA -> "ZZ"
            POS.XPV -> "ZZ"
            POS.XSM -> "ZZ"
            POS.XSO -> "ZZ"
            else -> this.toString()
        }

/**
 * 카이 분석기의 품사 표기를 세종 품사 표기 [POS] 값으로 변환합니다.
 *
 * * 변환표는 [여기](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s Conversion Table (Korean))에서 볼 수 있습니다.
 *
 * ## 사용 예
 * ### Kotlin
 * ```kotlin
 * "NNG".toSejongPOS()
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/scala-support)
 * ```scala
 * "NNG".toSejongPOS()
 * ```
 *
 * ### Java
 * ```java
 * Util.toSejongPOS("NNG")
 * ```
 *
 * @since 1.x
 * @return 대응하는 세종 품사의 값
 */
fun String?.toSejongPOS(): POS =
        when (this?.toUpperCase()) {
            null -> POS.NA
            "ZN" -> POS.NF
            "ZV" -> POS.NV
            "ZZ" -> POS.NA
            "SWK" -> POS.SW
            else -> POS.valueOf(this.toUpperCase())
        }

/**
 * 주어진 string의 UTF-8 바이트 표현에 맞춰 문자 index를 표기함.
 * 예) '한글 english'는 (0,0,0, 1,1,1, 2, 3, 4, 5, 6, 7, 8, 9)가 됨
 * @return 각 바이트당 실제 string의 문자 위치를 표현한 list
 */
fun String.byteAlignment(): List<Int> {
    val utf8IndexList = mutableListOf<Int>()

    for ((index, ch) in this.withIndex()) {
        val utf8Len = when {
            ch.toInt() <= 0x7F -> 1
            ch.toInt() <= 0x7FF -> 2
            ch.isHighSurrogate() -> 4
            else -> 3
        }

        for (i in 1..utf8Len) {
            utf8IndexList.add(index)
        }
    }

    return utf8IndexList
}

/**
 * Khaiii에서 설정된 Logger 유형들.
 */
enum class KhaiiiLoggerType {
    /**
     * 전체 Logger
     */
    all,
    /**
     * Khaiii console. (현재 버전 v0.1에서는 Java와 무관함)
     */
    console,
    /**
     * 오분석 사전
     */
    ErrPatch,
    /**
     * 분석 리소스
     */
    Resource,
    /**
     * 기분석 사전
     */
    Preanal,
    /**
     * 원형 복원
     */
    Restore,
    /**
     * 문장 구성
     */
    Sentence,
    /**
     * 어절 구성
     */
    Word,
    /**
     * 형태소 구성
     */
    Morph,
    /**
     * 형태소 분석기
     */
    Tagger,
    /**
     * Word Embedding
     */
    Embed,
    /**
     * Trie 구조
     */
    Trie,
    /**
     * Khaiii 구현체
     */
    KhaiiiImpl
}

/**
 * Java의 LogLevel을 Khaiii가 사용하는 spdlog의 level로 변경한다.
 * @return spdlig의 level값 (String)
 */
fun Level.getSpdlogLevel(): String = when (this) {
    Level.SEVERE, Level.OFF -> "critical"   // Off 불가능
    Level.WARNING -> "warn"
    Level.INFO -> "info"
    Level.CONFIG, Level.FINE -> "debug"
    Level.FINER, Level.FINEST, Level.ALL -> "trace"
    else -> "info"
}


/**
 * [Khaiii.analyzeBeforeErrorPatch]의 결과를 해석하기 위해, Khaiii에서 사용하는 태그들의 순서를 그대로 가져왔습니다.
 *
 * 참고: [https://github.com/kakao/khaiii/blob/v0.1/src/main/cpp/khaiii/Morph.cpp#L32]
 */
val posTagsInKhaiii = listOf("EC", "EF", "EP", "ETM", "ETN", "IC", "JC", "JKB", "JKC", "JKG",
        "JKO", "JKQ", "JKS", "JKV", "JX", "MAG", "MAJ", "MM", "NNB", "NNG",
        "NNP", "NP", "NR", "SE", "SF", "SH", "SL", "SN", "SO", "SP",
        "SS", "SW", "SWK", "VA", "VCN", "VCP", "VV", "VX", "XPN", "XR",
        "XSA", "XSN", "XSV", "ZN", "ZV", "ZZ")
