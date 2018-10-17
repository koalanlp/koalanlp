[kr.bydelta.koala](../index.md) / [Entity](./index.md)

# Entity

`class Entity : `[`Property`](../-property/index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`

개체명 분석 결과를 저장할 Property class

### Parameters

`type` - 개체명 대분류 값 (CoarseEntityType)

`fineType` - 개체명 세분류 값 (String)

`words` - 개체명을 이루는 어절의 목록

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Entity(type: `[`CoarseEntityType`](../-coarse-entity-type/index.md)`, fineType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, words: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>)`<br>개체명 분석 결과를 저장할 Property class |

### Properties

| Name | Summary |
|---|---|
| [fineType](fine-type.md) | `val fineType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>개체명 세분류 값 (String) |
| [surface](surface.md) | `val surface: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>개체명의 표면형 |
| [type](type.md) | `val type: `[`CoarseEntityType`](../-coarse-entity-type/index.md)<br>개체명 대분류 값 (CoarseEntityType) |
| [words](words.md) | `val words: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`<br>개체명을 이루는 어절의 목록 |

### Inherited Properties

| Name | Summary |
|---|---|
| [properties](../-property/properties.md) | `val properties: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)`, `[`Property`](../-property/index.md)`>`<br>가질 수 있는 속성값을 저장할 [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html) |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | `fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Indicates whether some other object is "equal to" this one. |
| [hashCode](hash-code.md) | `fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns a hash code value for the object.  The general contract of hashCode is: |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a string representation of the object. |

### Inherited Functions

| Name | Summary |
|---|---|
| [getProperty](../-property/get-property.md) | `fun <T : `[`Property`](../-property/index.md)`> getProperty(key: `[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)`): `[`T`](../-property/get-property.md#T)`?`<br>[key](../-property/get-property.md#kr.bydelta.koala.Property$getProperty(kotlin.Byte)/key)로 지정된 [T](../-property/get-property.md#T)-type의 property를 가져옵니다. |
| [setListProperty](../-property/set-list-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setListProperty(vararg properties: `[`T`](../-property/set-list-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-list-property.md#T)-type 값들을 가변변수로 하여 [properties](../-property/set-list-property.md#kr.bydelta.koala.Property$setListProperty(kotlin.Array((kr.bydelta.koala.Property.setListProperty.T)))/properties)로 받은 뒤, 저장합니다. |
| [setProperty](../-property/set-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setProperty(property: `[`T`](../-property/set-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-property.md#T)-type 값 [property](../-property/set-property.md#kr.bydelta.koala.Property$setProperty(kr.bydelta.koala.Property.setProperty.T)/property)를 저장합니다. |
