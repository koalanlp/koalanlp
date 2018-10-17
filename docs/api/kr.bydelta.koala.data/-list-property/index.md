[kr.bydelta.koala.data](../index.md) / [ListProperty](./index.md)

# ListProperty

`class ListProperty<T : `[`Property`](../-property/index.md)`> : `[`Property`](../-property/index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](index.md#T)`>`

목록형 값을 저장할 [Property](../-property/index.md).

**Since**
2.0.0

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ListProperty(children: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](index.md#T)`>)`<br>목록을 저장하기 위해 Property로 Wrapping합니다. |

### Properties

| Name | Summary |
|---|---|
| [children](children.md) | `val children: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](index.md#T)`>`<br>저장할 [T](index.md#T)-type 값의 목록 |

### Inherited Properties

| Name | Summary |
|---|---|
| [properties](../-property/properties.md) | `val properties: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)`, `[`Property`](../-property/index.md)`>`<br>가질 수 있는 속성값을 저장할 [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html) |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | `fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Indicates whether some other object is "equal to" this one. |
| [hashCode](hash-code.md) | `fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns a hash code value for the object.  The general contract of hashCode is: |

### Inherited Functions

| Name | Summary |
|---|---|
| [setListProperty](../-property/set-list-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setListProperty(vararg properties: `[`T`](../-property/set-list-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-list-property.md#T)-type 값들을 가변변수로 하여 [properties](../-property/set-list-property.md#kr.bydelta.koala.data.Property$setListProperty(kotlin.Array((kr.bydelta.koala.data.Property.setListProperty.T)))/properties)로 받은 뒤, 저장합니다. |
| [setProperty](../-property/set-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setProperty(property: `[`T`](../-property/set-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-property.md#T)-type 값 [property](../-property/set-property.md#kr.bydelta.koala.data.Property$setProperty(kr.bydelta.koala.data.Property.setProperty.T)/property)를 저장합니다. |
