@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.kkma

import kr.bydelta.koala.DependencyTag
import kr.bydelta.koala.POS
import kr.bydelta.koala.PhraseTag


/**
 * 꼬꼬마 분석 결과 ETRI 표준안 기반의 의존구문분석표기로 변경합니다.
 *
 * @return ETRI 표준안 기반의 의존구문분석 표기
 */
fun String?.toETRIDepTag(): DependencyTag? =
        when (this) { // 주석 부분은 KKMA 코드에 내장된 규칙
            "이유" -> // NP_MOD (~기/NP --> 때문/NP)
                DependencyTag.MOD
            "술부" -> // VP_SBJ (NP --> 없/VP)
                DependencyTag.SBJ
            "동일", "명사구" -> // NP (NP --> NP)
                null
            "부사어" -> // AP_AJT (NP --> VP)
                DependencyTag.AJT
            "수식" -> // DP_MOD (DP --> NP/VP)
                DependencyTag.MOD
            "보조 연결", "의존 연결", "대등 연결" -> // VP_CNJ (VP --> VP)
                DependencyTag.CNJ
            "체언 연결" -> // NP_CNJ (NP --> NP)
                DependencyTag.CNJ
            "주어" -> // NP_SBJ (NP --> VP)
                DependencyTag.SBJ
            "보어" -> // NP_CMP (NP --> VP)
                DependencyTag.CMP
            "목적어" -> // NP_OBJ (NP --> VP)
                DependencyTag.OBJ
            "(주어,목적)대상" -> //NP_AJT (NP --> VP). 부터/까지
                DependencyTag.AJT
            "인용" -> // ?_CMP (? --> VP)
                DependencyTag.CMP
            else -> DependencyTag.UNDEF
        }


/**
 * 꼬꼬마 분석 결과 ETRI 표준안 기반의 구문분석표기로 변경합니다.
 *
 * @return ETRI 표준안 기반의 구문분석 표기
 */
fun String?.toETRIPhraseTag(): PhraseTag =
        when (this) { // 주석 부분은 KKMA 코드에 내장된 규칙
            "이유" -> // NP_MOD (~기/NP --> 때문/NP)
                PhraseTag.NP
            "술부" -> // VP_SBJ (NP --> 없/VP)
                PhraseTag.VP
            "동일", "명사구" -> // NP (NP --> NP)
                PhraseTag.NP
            "부사어" -> // AP_AJT (NP --> VP)
                PhraseTag.NP
            "수식" -> // DP_MOD (DP --> NP/VP)
                PhraseTag.DP
            "보조 연결", "의존 연결", "대등 연결" -> // VP_CNJ (VP --> VP)
                PhraseTag.VP
            "체언 연결" -> // NP_CNJ (NP --> NP)
                PhraseTag.NP
            "주어" -> // NP_SBJ (NP --> VP)
                PhraseTag.NP
            "보어" -> // NP_CMP (NP --> VP)
                PhraseTag.NP
            "목적어" -> // NP_OBJ (NP --> VP)
                PhraseTag.NP
            "(주어,목적)대상" -> //NP_AJT (NP --> VP). 부터/까지
                PhraseTag.NP
            "인용" -> // ?_CMP (? --> VP)
                PhraseTag.X
            else ->
                PhraseTag.X
        }

/**
 * 세종 품사 표기를 꼬꼬마의 품사로 변환합니다.
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
            POS.VX -> "VXV"
            POS.MM -> "MDT"
            POS.MAJ -> "MAC"
            POS.JKB -> "JKM"
            POS.JKV -> "JKI"
            POS.EP -> "EPT"
            POS.EF -> "EFN"
            POS.EC -> "ECE"
            POS.ETM -> "ETD"
            POS.SL -> "OL"
            POS.SH -> "OH"
            POS.SN -> "ON"
            POS.NF -> "UN"
            POS.NV -> "UV"
            POS.NA -> "UE"
            else -> this.toString()
        }


/**
 * 꼬꼬마의 품사를 세종 품사표기로 변환합니다.
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
fun String?.toSejongPOS(): POS {
    val upper = this?.toUpperCase()
    return when (upper) {
        null -> POS.NA
        "VXV", "VXA" -> POS.VX
        "MDT", "MDN" -> POS.MM
        "MAC" -> POS.MAJ
        "JKM" -> POS.JKB
        "JKI" -> POS.JKV
        "ETD" -> POS.ETM
        "OL" -> POS.SL
        "OH" -> POS.SH
        "ON" -> POS.SN
        "UN" -> POS.NF
        "UV" -> POS.NV
        "UE" -> POS.NA
        "EMO" -> POS.SW //Emoticons
        "SY" -> POS.SW //KKMA 코드 속 오기입(SW) 처리
        else ->
            when {
                upper.startsWith("EP") -> POS.EP
                upper.startsWith("EF") -> POS.EF
                upper.startsWith("EC") -> POS.EC
                else -> POS.valueOf(upper)
            }
    }
}