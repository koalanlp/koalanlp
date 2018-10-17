[kr.bydelta.koala](../index.md) / [Word](./index.md)

# Word

`class Word : `[`Property`](../-property/index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Morpheme`](../-morpheme/index.md)`>`

어절 Class.

초기화 과정에서 ID값을 각 [Morpheme](../-morpheme/index.md)에 할당할 수 없으면, [AlreadySetIDException](../-already-set-i-d-exception/index.md)이 발생함.

### Parameters

`surface` - 어절의 표면형 String.

`morphemes` - 어절에 포함된 형태소의 목록: List&lt;[Morpheme](../-morpheme/index.md)&gt;.

### Properties

| Name | Summary |
|---|---|
| [id](id.md) | `var id: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>어절의 문장 내 위치. [equals](equals.md) 연산에 [id](id.md) 값은 포함되지 않음. |
| [surface](surface.md) | `val surface: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>어절의 표면형 String. |

### Inherited Properties

| Name | Summary |
|---|---|
| [properties](../-property/properties.md) | `val properties: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)`, `[`Property`](../-property/index.md)`>`<br>가질 수 있는 속성값을 저장할 [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html) |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | `fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Indicates whether some other object is "equal to" this one (without [id](id.md)). |
| [equalsWithoutTag](equals-without-tag.md) | `fun equalsWithoutTag(another: `[`Word`](./index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>[another](equals-without-tag.md#kr.bydelta.koala.Word$equalsWithoutTag(kr.bydelta.koala.Word)/another) 어절과 표면형이 같은지 비교합니다. |
| [getDependency](get-dependency.md) | `fun getDependency(): `[`DepTree`](../-dep-tree/index.md)<br>의존구문분석을 했다면, 현재 어절이 지배소인 의존구문 구조의 값을 돌려줌. 분석을 하지 않은 경우 [UninitializedPropertyAccessException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-uninitialized-property-access-exception/index.html) 발생 |
| [getEntities](get-entities.md) | `fun getEntities(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Entity`](../-entity/index.md)`>`<br>개체명 분석을 했다면, 현재 어절이 속한 개체명 값을 돌려줌. 분석을 하지 않은 경우 [UninitializedPropertyAccessException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-uninitialized-property-access-exception/index.html) 발생 |
| [getPhrase](get-phrase.md) | `fun getPhrase(): `[`SyntaxTree`](../-syntax-tree/index.md)<br>구문분석을 했다면, 현재 어절이 속한 직속 상위 구구조(Phrase)를 돌려줌. 분석을 하지 않은 경우 [UninitializedPropertyAccessException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-uninitialized-property-access-exception/index.html) 발생 |
| [getRole](get-role.md) | `fun getRole(): `[`RoleTree`](../-role-tree/index.md)<br>의미역 분석을 했다면, 현재 어절이 지배하는 의미역 구조를 돌려줌. 분석을 하지 않았거나, 의미역 구조에 포함되지 않는 경우 [UninitializedPropertyAccessException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-uninitialized-property-access-exception/index.html) 발생 |
| [hashCode](hash-code.md) | `fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns a hash code value for the object.  The general contract of hashCode is: |
| [singleLineString](single-line-string.md) | `fun singleLineString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>품사분석 결과를, 1행짜리 String으로 변환합니다. |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a string representation of the object. |

### Inherited Functions

| Name | Summary |
|---|---|
| [getProperty](../-property/get-property.md) | `fun <T : `[`Property`](../-property/index.md)`> getProperty(key: `[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)`): `[`T`](../-property/get-property.md#T)`?`<br>[key](../-property/get-property.md#kr.bydelta.koala.Property$getProperty(kotlin.Byte)/key)로 지정된 [T](../-property/get-property.md#T)-type의 property를 가져옵니다. |
| [setListProperty](../-property/set-list-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setListProperty(vararg properties: `[`T`](../-property/set-list-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-list-property.md#T)-type 값들을 가변변수로 하여 [properties](../-property/set-list-property.md#kr.bydelta.koala.Property$setListProperty(kotlin.Array((kr.bydelta.koala.Property.setListProperty.T)))/properties)로 받은 뒤, 저장합니다. |
| [setProperty](../-property/set-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setProperty(property: `[`T`](../-property/set-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-property.md#T)-type 값 [property](../-property/set-property.md#kr.bydelta.koala.Property$setProperty(kr.bydelta.koala.Property.setProperty.T)/property)를 저장합니다. |
