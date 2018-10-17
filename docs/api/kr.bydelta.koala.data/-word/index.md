[kr.bydelta.koala.data](../index.md) / [Word](./index.md)

# Word

`class Word : `[`Property`](../-property/index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Morpheme`](../-morpheme/index.md)`>`

어절을 표현하는 [Property](../-property/index.md) class입니다.

**Since**
1.x

### Properties

| Name | Summary |
|---|---|
| [id](id.md) | `var id: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>어절의 문장 내 위치입니다. |
| [surface](surface.md) | `val surface: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>어절의 표면형 String. |

### Inherited Properties

| Name | Summary |
|---|---|
| [properties](../-property/properties.md) | `val properties: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)`, `[`Property`](../-property/index.md)`>`<br>가질 수 있는 속성값을 저장할 [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html) |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | `fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>표면형과 더불어, 포함된 형태소의 배열 순서가 같은지 확인합니다. (reference 기준의 동일함 판단이 아닙니다) |
| [equalsWithoutTag](equals-without-tag.md) | `fun equalsWithoutTag(another: `[`Word`](./index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>[another](equals-without-tag.md#kr.bydelta.koala.data.Word$equalsWithoutTag(kr.bydelta.koala.data.Word)/another) 어절과 표면형이 같은지 비교합니다. |
| [getDependency](get-dependency.md) | `fun getDependency(): `[`DepTree`](../-dep-tree/index.md)<br>의존구문분석을 했다면, 현재 어절이 지배소인 의존구문 구조의 값을 돌려줍니다. |
| [getEntities](get-entities.md) | `fun getEntities(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Entity`](../-entity/index.md)`>`<br>개체명 분석을 했다면, 현재 어절이 속한 개체명 값을 돌려줍니다. |
| [getPhrase](get-phrase.md) | `fun getPhrase(): `[`SyntaxTree`](../-syntax-tree/index.md)<br>구문분석을 했다면, 현재 어절이 속한 직속 상위 구구조(Phrase)를 돌려줍니다. |
| [getRole](get-role.md) | `fun getRole(): `[`RoleTree`](../-role-tree/index.md)<br>의미역 분석을 했다면, 현재 어절이 지배하는 의미역 구조를 돌려줌. |
| [hashCode](hash-code.md) | `fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns a hash code value for the object.  The general contract of hashCode is: |
| [singleLineString](single-line-string.md) | `fun singleLineString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>품사분석 결과를, 1행짜리 String으로 변환합니다. |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a string representation of the object. |

### Inherited Functions

| Name | Summary |
|---|---|
| [setListProperty](../-property/set-list-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setListProperty(vararg properties: `[`T`](../-property/set-list-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-list-property.md#T)-type 값들을 가변변수로 하여 [properties](../-property/set-list-property.md#kr.bydelta.koala.data.Property$setListProperty(kotlin.Array((kr.bydelta.koala.data.Property.setListProperty.T)))/properties)로 받은 뒤, 저장합니다. |
| [setProperty](../-property/set-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setProperty(property: `[`T`](../-property/set-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-property.md#T)-type 값 [property](../-property/set-property.md#kr.bydelta.koala.data.Property$setProperty(kr.bydelta.koala.data.Property.setProperty.T)/property)를 저장합니다. |
