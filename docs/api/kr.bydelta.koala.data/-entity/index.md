[kr.bydelta.koala.data](../index.md) / [Entity](./index.md)

# Entity

`class Entity : `[`Property`](../-property/index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`

개체명 분석 결과를 저장할 [Property](../-property/index.md) class

## 참고

**개체명 인식**은 문장에서 인물, 장소, 기관, 대상 등을 인식하는 기술입니다.
예) '철저한 진상 조사를 촉구하는 국제사회의 목소리가 커지고 있는 가운데, 트럼프 미국 대통령은 되레 사우디를 감싸고 나섰습니다.'에서, 다음을 인식하는 기술입니다.

* '트럼프': 인물
* '미국' : 국가
* '대통령' : 직위
* '사우디' : 국가

아래를 참고해보세요.

* [kr.bydelta.koala.proc.CanRecognizeEntity](../../kr.bydelta.koala.proc/-can-recognize-entity.md) 개체명 인식기 interface
* [Word.getEntities](../-word/get-entities.md) 어절이 속하는 [Entity](./index.md)를 가져오는 API
* [Sentence.getEntities](../-sentence/get-entities.md) 문장에 포함된 모든 [Entity](./index.md)를 가져오는 API
* [CoarseEntityType](./index.md)의 대분류 개체명 분류구조 Enum 값

**Since**
2.0.0

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Entity(type: `[`CoarseEntityType`](../../kr.bydelta.koala/-coarse-entity-type/index.md)`, fineType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, words: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>)`<br>개체명 분석 결과를 저장합니다. |

### Properties

| Name | Summary |
|---|---|
| [fineType](fine-type.md) | `val fineType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>개체명 세분류 값으로, [type](type.md)으로 시작하는 문자열. |
| [surface](surface.md) | `val surface: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>개체명의 표면형입니다. |
| [type](type.md) | `val type: `[`CoarseEntityType`](../../kr.bydelta.koala/-coarse-entity-type/index.md)<br>개체명 대분류 값, [CoarseEntityType](../../kr.bydelta.koala/-coarse-entity-type/index.md)에 기록된 개체명 중 하나. |
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
| [setListProperty](../-property/set-list-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setListProperty(vararg properties: `[`T`](../-property/set-list-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-list-property.md#T)-type 값들을 가변변수로 하여 [properties](../-property/set-list-property.md#kr.bydelta.koala.data.Property$setListProperty(kotlin.Array((kr.bydelta.koala.data.Property.setListProperty.T)))/properties)로 받은 뒤, 저장합니다. |
| [setProperty](../-property/set-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setProperty(property: `[`T`](../-property/set-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-property.md#T)-type 값 [property](../-property/set-property.md#kr.bydelta.koala.data.Property$setProperty(kr.bydelta.koala.data.Property.setProperty.T)/property)를 저장합니다. |
