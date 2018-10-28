@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.okt

import kr.bydelta.koala.POS
import scala.collection.`JavaConverters$`


/**
 * 세종 품사 표기를 OpenKoreanText의 품사로 변환합니다.
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
            POS.NNG, POS.NNB, POS.NNM, POS.NP -> "Noun"
            POS.NNP -> "ProperNoun"
            POS.NR, POS.SN -> "Number"
            POS.VV, POS.VX, POS.VCP, POS.VCN -> "Verb"
            POS.VA -> "Adjective"
            POS.MM -> "Modifier"
            POS.MAG, POS.MAJ -> "Adverb"
            POS.IC -> "Exclamation"
            POS.JKB, POS.JKC, POS.JKG, POS.JKO, POS.JKQ, POS.JKS, POS.JKV, POS.JX -> "Josa"
            POS.JC -> "Conjunction"
            POS.EP -> "PreEomi"
            POS.EF, POS.EC, POS.ETM, POS.ETN -> "Eomi"
            POS.XPN -> "Unknown"
            POS.XPV -> "VerbPrefix"
            POS.XSA, POS.XSM, POS.XSN, POS.XSO, POS.XSV -> "Suffix"
            POS.SF -> "Punctuation"
            POS.SS, POS.SP, POS.SE, POS.SO, POS.SW, POS.XR -> "Others"
            POS.NF, POS.NV, POS.NA -> "Unknown"
            POS.SL, POS.SH -> "Foreign"
            else -> "Unknown"
        }

/**
 * OpenKoreanText의 품사를 세종 품사표기로 변환합니다.
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
        when (this) {
            null -> POS.NA
            "Noun" -> POS.NNG
            "ProperNoun" -> POS.NNP
            "Number" -> POS.NR
            "Verb" -> POS.VV
            "Adjective" -> POS.VA
            "Determiner", "Modifier" -> POS.MM
            "Adverb" -> POS.MAG
            "Exclamation" -> POS.IC
            "Josa" -> POS.JX
            "Conjunction" -> POS.JC
            "PreEomi" -> POS.EP
            "Eomi" -> POS.EF
            "VerbPrefix" -> POS.XPV
            "Suffix" -> POS.XSO
            "Punctuation" -> POS.SF
            "Unknown" -> POS.NA
            "Foreign", "Alpha" -> POS.SL
            else -> POS.SW
        }

/** Scala Collection --> Java Collection */
internal fun <T> scala.collection.Iterable<T>.asJava() = `JavaConverters$`.`MODULE$`.asJavaCollection(this).toList<T>()

/** Java Collection --> Scala Collection */
internal fun <T> kotlin.collections.Iterable<T>.asScala() = `JavaConverters$`.`MODULE$`.asScalaIterator(this.iterator())
