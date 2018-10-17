[kr.bydelta.koala.data](../index.md) / [Property](./index.md)

# Property

`abstract class Property : `[`Serializable`](http://docs.oracle.com/javase/6/docs/api/java/io/Serializable.html)

텍스트 분석 과정에서 얻어지는 여러가지 값들을 표현하는 class.

다음 값을 속성으로 갖습니다:

* [IntProperty](../-int-property/index.md) 정수형 값 속성
* [Entity](../-entity/index.md) 개체명 정보 속성
* [SyntaxTree](../-syntax-tree/index.md) 구문구조 속성
* [DepTree](../-dep-tree/index.md) 의존구문구조 속성
* [RoleTree](../-role-tree/index.md) 의미역 구조 속성

**Since**
2.0.0

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Property()`<br>텍스트 분석 과정에서 얻어지는 여러가지 값들을 표현하는 class. |

### Properties

| Name | Summary |
|---|---|
| [properties](properties.md) | `val properties: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)`, `[`Property`](./index.md)`>`<br>가질 수 있는 속성값을 저장할 [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html) |

### Functions

| Name | Summary |
|---|---|
| [setListProperty](set-list-property.md) | `fun <T : `[`Property`](./index.md)`> setListProperty(vararg properties: `[`T`](set-list-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](set-list-property.md#T)-type 값들을 가변변수로 하여 [properties](set-list-property.md#kr.bydelta.koala.data.Property$setListProperty(kotlin.Array((kr.bydelta.koala.data.Property.setListProperty.T)))/properties)로 받은 뒤, 저장합니다. |
| [setProperty](set-property.md) | `fun <T : `[`Property`](./index.md)`> setProperty(property: `[`T`](set-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](set-property.md#T)-type 값 [property](set-property.md#kr.bydelta.koala.data.Property$setProperty(kr.bydelta.koala.data.Property.setProperty.T)/property)를 저장합니다. |

### Inheritors

| Name | Summary |
|---|---|
| [Entity](../-entity/index.md) | `class Entity : `[`Property`](./index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`<br>개체명 분석 결과를 저장할 [Property](./index.md) class |
| [IntProperty](../-int-property/index.md) | `data class IntProperty : `[`Property`](./index.md)<br>정수형 값을 저장할 [Property](./index.md) class. |
| [ListProperty](../-list-property/index.md) | `class ListProperty<T : `[`Property`](./index.md)`> : `[`Property`](./index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](../-list-property/index.md#T)`>`<br>목록형 값을 저장할 [Property](./index.md). |
| [Morpheme](../-morpheme/index.md) | `class Morpheme : `[`Property`](./index.md)<br>형태소를 저장하는 [Property](./index.md) class입니다. |
| [Sentence](../-sentence/index.md) | `class Sentence : `[`Property`](./index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`<br>문장을 표현하는 [Property](./index.md) class입니다. |
| [Tree](../-tree/index.md) | `open class Tree<T : `[`Tree`](../-tree/index.md)`<`[`T`](../-tree/index.md#T)`>> : `[`Property`](./index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](../-tree/index.md#T)`>`<br>[T](../-tree/index.md#T)-type들의 트리 또는 DAG 구조를 저장할 [Property](./index.md) |
| [Word](../-word/index.md) | `class Word : `[`Property`](./index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Morpheme`](../-morpheme/index.md)`>`<br>어절을 표현하는 [Property](./index.md) class입니다. |
