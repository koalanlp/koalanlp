@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.eunjeon

import kr.bydelta.koala.POS

/**
 * 세종 품사 표기를 은전한닢의 품사로 변환합니다
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
 * @return 변환된 품사.
 */
fun POS.fromSejongPOS(): String =
        when (this) {
            POS.NNM -> "NNBC"
            POS.SS -> "SSO"
            POS.SP -> "SC"
            POS.XPV -> "XR"
            POS.XSM, POS.XSO -> "XSN"
            POS.SW, POS.SO -> "SY"
            POS.NF, POS.NV, POS.NA -> "UNKNOWN"
            else -> this.toString()
        }

/**
 * 은전한닢의 품사를 세종 품사표기로 변환합니다.
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
 * @return 변환된 세종 품사표기
 */
fun String?.toSejongPOS(): POS {
    val upper = this?.toUpperCase()
    return when (upper) {
        "NNBC" -> POS.NNM
        "SC" -> POS.SP
        "SSO", "SSC" -> POS.SS
        "SY" -> POS.SW
        null, "UNKNOWN" -> POS.NA
        else -> POS.valueOf(upper)
    }
}

/**
 * Kotlin to Scala iterator
 */
internal class ScalaIterator<T>(private val x: scala.collection.Iterator<T>): Iterator<T> {
    override fun next(): T = x.next()
    override fun hasNext(): Boolean = x.hasNext()
}

/**
 * Scala to Kotlin iterator
 */
internal class JavaIterator<T>(private val x: Iterator<T>): scala.collection.Iterator<T> {
    override fun next(): T = x.next()
    override fun hasNext(): Boolean = x.hasNext()
}

/** Scala Collection --> Java Collection */
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "UNCHECKED_CAST")
internal fun <T> scala.collection.Iterable<T>.asJava(): List<T> = ScalaIterator(this.toIterator()).asSequence().toList()

/** Java Collection --> Scala Collection */
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "UNCHECKED_CAST")
internal fun <T> Iterable<T>.asScala(): scala.collection.Iterator<T> = JavaIterator(this.iterator())
