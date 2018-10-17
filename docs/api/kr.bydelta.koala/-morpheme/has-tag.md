[kr.bydelta.koala](../index.md) / [Morpheme](index.md) / [hasTag](./has-tag.md)

# hasTag

`fun hasTag(partialTag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

세종 품사 [tag](tag.md)가 주어진 품사 표기 [partialTag](has-tag.md#kr.bydelta.koala.Morpheme$hasTag(kotlin.String)/partialTag) 묶음에 포함되는지 확인.

예를 들어, N은 체언인지 확인하고, NP는 대명사인지 확인함.

참고: 분석불능범주(NA, NV, NF)는 체언(N) 범주에 포함되지 않음

품사 표기는 [여기](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)
에서 확인

