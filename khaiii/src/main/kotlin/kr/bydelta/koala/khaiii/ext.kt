import kr.bydelta.koala.POS

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