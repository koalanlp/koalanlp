[kr.bydelta.koala.data](../index.md) / [Morpheme](index.md) / [hasOriginalTag](./has-original-tag.md)

# hasOriginalTag

`fun hasOriginalTag(partialTag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

원본 품사 [originalTag](original-tag.md)가 주어진 품사 표기 [partialTag](has-original-tag.md#kr.bydelta.koala.data.Morpheme$hasOriginalTag(kotlin.String)/partialTag) 묶음에 포함되는지 확인합니다.

지정된 원본 품사가 없으면 (즉, null이면) false를 반환합니다.

## 단축명령

* 체언(명사, 수사, 대명사) [isNoun](is-noun.md)
* 용언(동사, 형용사)는 [isPredicate](is-predicate.md)
* 수식언(관형사, 부사)는 [isModifier](is-modifier.md)
* 관계언(조사)는 [isJosa](is-josa.md)

## 참고

* 분석불능범주(NA, NV, NF)는 체언(N) 범주에 포함되지 않습니다.
* 각 분석기의 품사 표기는 [비교표](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)
에서 확인가능합니다.

### Parameters

`partialTag` - 포함 여부를 확인할 상위 형태소 분류 품사표기들

**Since**
1.x

**Return**
하나라도 포함되는 경우 true.

