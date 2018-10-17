[kr.bydelta.koala.data](../index.md) / [Morpheme](index.md) / [hasTag](./has-tag.md)

# hasTag

`fun hasTag(partialTag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

세종 품사 [tag](tag.md)가 주어진 품사 표기 [partialTag](has-tag.md#kr.bydelta.koala.data.Morpheme$hasTag(kotlin.String)/partialTag) 묶음에 포함되는지 확인합니다.

예) "N"은 체언인지 확인하고, "NP"는 대명사인지 확인

## 단축명령

* 체언(명사, 수사, 대명사) [isNoun](is-noun.md)
* 용언(동사, 형용사)는 [isPredicate](is-predicate.md)
* 수식언(관형사, 부사)는 [isModifier](is-modifier.md)
* 관계언(조사)는 [isJosa](is-josa.md)

## 참고

* 분석불능범주(NA, NV, NF)는 체언(N) 범주에 포함되지 않습니다.
* 세종 품사표기는 [POS](../../kr.bydelta.koala/-p-o-s/index.md)를 참고하세요.
* 품사 표기는 [비교표](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)
에서 확인가능합니다.

### Parameters

`partialTag` - 포함 여부를 확인할 상위 형태소 분류 품사표기

**Since**
1.x

**Return**
포함되는 경우 true.

