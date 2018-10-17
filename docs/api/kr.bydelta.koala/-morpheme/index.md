[kr.bydelta.koala](../index.md) / [Morpheme](./index.md)

# Morpheme

`class Morpheme : `[`Property`](../-property/index.md)

형태소 class

### Parameters

`surface` - 형태소 표면형 String

`originalTag` - 원본 형태소 분석기의 품사 String

`tag` - 세종 품사표기

### Properties

| Name | Summary |
|---|---|
| [id](id.md) | `var id: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>형태소의 어절 내 위치. [equals](equals.md) 연산에 [id](id.md) 값은 포함되지 않으며, 한번 값이 설정된 경우 다시 설정 불가능 (재설정시 [AlreadySetIDException](../-already-set-i-d-exception/index.md) 발생). |
| [originalTag](original-tag.md) | `val originalTag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>원본 형태소 분석기의 품사 String |
| [surface](surface.md) | `val surface: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>형태소 표면형 String |
| [tag](tag.md) | `val tag: `[`POS`](../-p-o-s/index.md)<br>세종 품사표기 |

### Inherited Properties

| Name | Summary |
|---|---|
| [properties](../-property/properties.md) | `val properties: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)`, `[`Property`](../-property/index.md)`>`<br>가질 수 있는 속성값을 저장할 [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html) |

### Functions

| Name | Summary |
|---|---|
| [component1](component1.md) | `operator fun component1(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>[surface](surface.md) 값을 첫 component로 반환함. |
| [component2](component2.md) | `operator fun component2(): `[`POS`](../-p-o-s/index.md)<br>[tag](tag.md) 값을 두번째 component로 반환함. |
| [equals](equals.md) | `fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Indicates whether some other object is "equal to" this one (without [id](id.md)). |
| [equalsWithoutTag](equals-without-tag.md) | `fun equalsWithoutTag(another: `[`Morpheme`](./index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>타 형태소 객체 [another](equals-without-tag.md#kr.bydelta.koala.Morpheme$equalsWithoutTag(kr.bydelta.koala.Morpheme)/another)와 형태소의 표면형이 같은지 비교함. |
| [getWordSense](get-word-sense.md) | `fun getWordSense(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>다의어 분석 결과인, 이 형태소의 사전 속 어깨번호 값을 돌려줌. 다의어 분석을 한 적이 없다면 [UninitializedPropertyAccessException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-uninitialized-property-access-exception/index.html)이 발생함. |
| [hasOriginalTag](has-original-tag.md) | `fun hasOriginalTag(partialTag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>원본 품사 [originalTag](original-tag.md)가 주어진 품사 표기 [partialTag](has-original-tag.md#kr.bydelta.koala.Morpheme$hasOriginalTag(kotlin.String)/partialTag) 묶음에 포함되는지 확인. 원본 품사가 없으면 false. |
| [hasTag](has-tag.md) | `fun hasTag(partialTag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>세종 품사 [tag](tag.md)가 주어진 품사 표기 [partialTag](has-tag.md#kr.bydelta.koala.Morpheme$hasTag(kotlin.String)/partialTag) 묶음에 포함되는지 확인. |
| [hasTagOneOf](has-tag-one-of.md) | `fun hasTagOneOf(vararg tags: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>세종 품사 [tag](tag.md)가 주어진 품사 표기들 [tags](has-tag-one-of.md#kr.bydelta.koala.Morpheme$hasTagOneOf(kotlin.Array((kotlin.String)))/tags) 묶음들 중 하나에 포함되는지 확인. |
| [hashCode](hash-code.md) | `fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns a hash code value for the object.  The general contract of hashCode is: |
| [isJosa](is-josa.md) | `fun isJosa(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>관계언(조사) 형태소인지 확인. |
| [isModifier](is-modifier.md) | `fun isModifier(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>수식언(관형사, 부사) 형태소인지 확인. |
| [isNoun](is-noun.md) | `fun isNoun(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>체언(명사, 수사, 대명사) 형태소인지 확인. |
| [isPredicate](is-predicate.md) | `fun isPredicate(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>용언(동사, 형용사) 형태소인지 확인. |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a string representation of the object. |

### Inherited Functions

| Name | Summary |
|---|---|
| [getProperty](../-property/get-property.md) | `fun <T : `[`Property`](../-property/index.md)`> getProperty(key: `[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)`): `[`T`](../-property/get-property.md#T)`?`<br>[key](../-property/get-property.md#kr.bydelta.koala.Property$getProperty(kotlin.Byte)/key)로 지정된 [T](../-property/get-property.md#T)-type의 property를 가져옵니다. |
| [setListProperty](../-property/set-list-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setListProperty(vararg properties: `[`T`](../-property/set-list-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-list-property.md#T)-type 값들을 가변변수로 하여 [properties](../-property/set-list-property.md#kr.bydelta.koala.Property$setListProperty(kotlin.Array((kr.bydelta.koala.Property.setListProperty.T)))/properties)로 받은 뒤, 저장합니다. |
| [setProperty](../-property/set-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setProperty(property: `[`T`](../-property/set-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-property.md#T)-type 값 [property](../-property/set-property.md#kr.bydelta.koala.Property$setProperty(kr.bydelta.koala.Property.setProperty.T)/property)를 저장합니다. |
