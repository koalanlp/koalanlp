[kr.bydelta.koala](../index.md) / [Morpheme](index.md) / [hasTagOneOf](./has-tag-one-of.md)

# hasTagOneOf

`fun hasTagOneOf(vararg tags: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

세종 품사 [tag](tag.md)가 주어진 품사 표기들 [tags](has-tag-one-of.md#kr.bydelta.koala.Morpheme$hasTagOneOf(kotlin.Array((kotlin.String)))/tags) 묶음들 중 하나에 포함되는지 확인.

예를 들어, (N, MM)의 경우, 체언 또는 관형사인지 확인.

참고: 분석불능범주(NA, NV, NF)는 체언(N) 범주에 포함되지 않음

품사 표기는 [여기](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)
에서 확인

