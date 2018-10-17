[kr.bydelta.koala.data](../index.md) / [Morpheme](./index.md)

# Morpheme

`class Morpheme : `[`Property`](../-property/index.md)

형태소를 저장하는 [Property](../-property/index.md) class입니다.

## 참고

**형태소**는 의미를 가지는 요소로서는 더 이상 분석할 수 없는 가장 작은 말의 단위로 정의됩니다.

**형태소 분석**은 문장을 형태소의 단위로 나누는 작업을 의미합니다.
예) '문장을 형태소로 나눠봅시다'의 경우,

* 문장/일반명사, -을/조사,
* 형태소/일반명사, -로/조사,
* 나누-(다)/동사, -어-/어미, 보-(다)/동사, -ㅂ시다/어미
로 대략 나눌 수 있습니다.

아래를 참고해보세요.

* [kr.bydelta.koala.proc.CanTag](../../kr.bydelta.koala.proc/-can-tag/index.md) 형태소 분석기의 최상위 Interface
* [POS](../../kr.bydelta.koala/-p-o-s/index.md) 형태소의 분류를 담은 Enum class

**Since**
1.0.0

### Properties

| Name | Summary |
|---|---|
| [id](id.md) | `var id: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>형태소의 어절 내 위치입니다. |
| [originalTag](original-tag.md) | `val originalTag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>원본 형태소 분석기의 품사 String |
| [surface](surface.md) | `val surface: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>형태소 표면형 String |
| [tag](tag.md) | `val tag: `[`POS`](../../kr.bydelta.koala/-p-o-s/index.md)<br>세종 품사표기 |

### Inherited Properties

| Name | Summary |
|---|---|
| [properties](../-property/properties.md) | `val properties: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)`, `[`Property`](../-property/index.md)`>`<br>가질 수 있는 속성값을 저장할 [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html) |

### Functions

| Name | Summary |
|---|---|
| [component1](component1.md) | `operator fun component1(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>[surface](surface.md) 값을 첫 component로 반환합니다. |
| [component2](component2.md) | `operator fun component2(): `[`POS`](../../kr.bydelta.koala/-p-o-s/index.md)<br>[tag](tag.md) 값을 두번째 component로 반환합니다. |
| [equals](equals.md) | `fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>표면형과 더불어, 형태소의 품사 표기도 같은지 확인합니다. |
| [equalsWithoutTag](equals-without-tag.md) | `fun equalsWithoutTag(another: `[`Morpheme`](./index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>타 형태소 객체 [another](equals-without-tag.md#kr.bydelta.koala.data.Morpheme$equalsWithoutTag(kr.bydelta.koala.data.Morpheme)/another)와 형태소의 표면형이 같은지 비교합니다. |
| [getWordSense](get-word-sense.md) | `fun getWordSense(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>다의어 분석 결과인, 이 형태소의 사전 속 의미/어깨번호 값을 돌려줍니다. |
| [hasOriginalTag](has-original-tag.md) | `fun hasOriginalTag(partialTag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>원본 품사 [originalTag](original-tag.md)가 주어진 품사 표기 [partialTag](has-original-tag.md#kr.bydelta.koala.data.Morpheme$hasOriginalTag(kotlin.String)/partialTag) 묶음에 포함되는지 확인합니다. |
| [hasTag](has-tag.md) | `fun hasTag(partialTag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>세종 품사 [tag](tag.md)가 주어진 품사 표기 [partialTag](has-tag.md#kr.bydelta.koala.data.Morpheme$hasTag(kotlin.String)/partialTag) 묶음에 포함되는지 확인합니다. |
| [hasTagOneOf](has-tag-one-of.md) | `fun hasTagOneOf(vararg tags: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>세종 품사 [tag](tag.md)가 주어진 품사 표기들 [tags](has-tag-one-of.md#kr.bydelta.koala.data.Morpheme$hasTagOneOf(kotlin.Array((kotlin.String)))/tags) 묶음들 중 하나에 포함되는지 확인합니다. |
| [hashCode](hash-code.md) | `fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns a hash code value for the object.  The general contract of hashCode is: |
| [isJosa](is-josa.md) | `fun isJosa(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>관계언(조사) 형태소인지 확인합니다. |
| [isModifier](is-modifier.md) | `fun isModifier(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>수식언(관형사, 부사) 형태소인지 확인합니다. |
| [isNoun](is-noun.md) | `fun isNoun(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>체언(명사, 수사, 대명사) 형태소인지 확인합니다. |
| [isPredicate](is-predicate.md) | `fun isPredicate(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>용언(동사, 형용사) 형태소인지 확인합니다. |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a string representation of the object. |

### Inherited Functions

| Name | Summary |
|---|---|
| [setListProperty](../-property/set-list-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setListProperty(vararg properties: `[`T`](../-property/set-list-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-list-property.md#T)-type 값들을 가변변수로 하여 [properties](../-property/set-list-property.md#kr.bydelta.koala.data.Property$setListProperty(kotlin.Array((kr.bydelta.koala.data.Property.setListProperty.T)))/properties)로 받은 뒤, 저장합니다. |
| [setProperty](../-property/set-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setProperty(property: `[`T`](../-property/set-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-property.md#T)-type 값 [property](../-property/set-property.md#kr.bydelta.koala.data.Property$setProperty(kr.bydelta.koala.data.Property.setProperty.T)/property)를 저장합니다. |
