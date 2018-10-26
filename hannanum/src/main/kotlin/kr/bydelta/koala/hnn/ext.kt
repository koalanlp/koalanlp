@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.hnn

import kr.bydelta.koala.DependencyTag
import kr.bydelta.koala.POS
import kr.bydelta.koala.PhraseTag

/**
 * 한나눔의 의존구문분석 결과표지를 ETRI의 표준안으로 변경합니다.
 *
 * @param tag ETRI 의존구문 표지로 변경할 의존구문분석 결과.
 * @return ETRI 의존구문 표지
 */
fun String?.toETRIDepTag(): DependencyTag? =
        when (this) {
            "VMOD", "NMOD", "MOD" -> DependencyTag.MOD
            "ADV" -> DependencyTag.AJT
            "INT" -> null
            "PRN", null -> DependencyTag.UNDEF
            else -> DependencyTag.valueOf(this)
        }

/**
 * 한나눔의 구문분석 결과표지를 ETRI의 표준안으로 변경합니다.
 *
 * @param tag ETRI 구문 표지로 변경할 구문분석 결과.
 * @return ETRI 구문 표지
 */
fun String?.toETRIPhraseTag(): PhraseTag {
    val tag = this?.split("-")?.firstOrNull()
    return when (tag) {
        "Q", "W", "Y", "Z" -> PhraseTag.Q
        null -> PhraseTag.X
        else -> PhraseTag.valueOf(tag)
    }
}

/**
 * 세종 품사 표기를 한나눔의 품사로 변환합니다.
 *
 * * 변환표는 [여기](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s Conversion Table (Korean))에서 볼 수 있습니다.
 *
 * ## 사용 예
 * ### Kotlin
 * ```kotlin
 * POS.NNG.fromSejongPOS()
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/wrapper-scala)
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
 * @return 변환된 품사.
 */
fun POS.fromSejongPOS(): String =
        when (this) {
            POS.NNG, POS.NF -> "NCN"
            POS.NNP -> "NQQ"
            POS.NNB -> "NBN"
            POS.NNM -> "NBU"
            POS.NR -> "NNC"
            POS.NP -> "NPD"
            POS.VV, POS.NV -> "PVG"
            POS.VA -> "PAA"
            POS.VX -> "PX"
            POS.VCP -> "JP"
            POS.VCN -> "PAA"
            POS.MM -> "MMA"
            POS.IC -> "II"
            POS.JKS -> "JCS"
            POS.JKC -> "JCC"
            POS.JKG -> "JCM"
            POS.JKO -> "JCO"
            POS.JKB -> "JCA"
            POS.JKV -> "JCV"
            POS.JKQ -> "JCR"
            POS.JC -> "JCT"
            POS.JX -> "JXF"
            POS.EC -> "ECC"
            POS.XSO, POS.XSN -> "XSNX"
            POS.XSV -> "XSVN"
            POS.XSA -> "XSMN"
            POS.XSM -> "XSAS"
            POS.SS -> "SL"
            POS.SL, POS.SH -> "F"
            POS.SN -> "NNC"
            POS.SO -> "SD"
            POS.SW, POS.XR, POS.NA -> "SY"
            POS.XPN, POS.XPV -> "XP"
            else -> this.toString()
        }.toLowerCase()

/**
 * 한나눔의 품사를 세종 품사표기로 변환합니다.
 *
 * * 변환표는 [여기](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s Conversion Table (Korean))에서 볼 수 있습니다.
 *
 * ## 사용 예
 * ### Kotlin
 * ```kotlin
 * "NNG".toSejongPOS()
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/wrapper-scala)
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
 * @return 변환된 세종 품사표기
 */
fun String?.toSejongPOS(): POS =
        when {
            this == null -> POS.NA
            startsWith("nc") -> POS.NNG
            startsWith("nq") -> POS.NNP
            this == "nbn" || this == "nbs" -> POS.NNB
            this == "nbu" -> POS.NNM
            startsWith("nn") -> POS.NR
            startsWith("np") -> POS.NP
            startsWith("pv") -> POS.VV
            startsWith("pa") -> POS.VA
            this == "px" -> POS.VX
            this == "jp" -> POS.VCP
            startsWith("mm") -> POS.MM
            this == "mad" || this == "mag" -> POS.MAG
            this == "maj" -> POS.MAJ
            this == "ii" -> POS.IC
            this == "jcs" -> POS.JKS
            this == "jcc" -> POS.JKC
            this == "jcm" -> POS.JKG
            this == "jco" -> POS.JKO
            this == "jca" -> POS.JKB
            this == "jcv" -> POS.JKV
            this == "jcr" -> POS.JKQ
            this == "jct" || this == "jcj" -> POS.JC
            this == "jxc" || this == "jxf" -> POS.JX
            startsWith("ec") -> POS.EC
            this == "xp" -> POS.XPN
            startsWith("xsn") -> POS.XSN
            startsWith("xsv") -> POS.XSV
            startsWith("xsm") -> POS.XSA
            startsWith("xsa") -> POS.XSM
            this == "sl" || this == "sr" -> POS.SS
            this == "sd" -> POS.SO
            this == "su" || this == "sy" -> POS.SW
            this == "f" -> POS.SL
            else -> POS.valueOf(this.toUpperCase())
        }