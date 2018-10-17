[kr.bydelta.koala.data](../index.md) / [IntProperty](./index.md)

# IntProperty

`data class IntProperty : `[`Property`](../-property/index.md)

정수형 값을 저장할 [Property](../-property/index.md) class.

보통, 다의어/동형이의어 분별 작업에서 어깨/의미번호를 저장하는 데 활용합니다.

## 참고

**다의어 분별**은 동일한 단어의 여러 의미를 구분하는 작업입니다.
예) '말1'은 다음 의미를 갖는 다의어이며, 다의어 분별 작업은 이를 구분합니다.

1. 사람의 생각이나 느낌 따위를 표현하고 전달하는 데 쓰는 음성 기호.
2. 음성 기호로 생각이나 느낌을 표현하고 전달하는 행위. 또는 그런 결과물.
3. 일정한 주제나 줄거리를 가진 이야기.

**동형이의어 분별**은 동일한 형태지만 다른 의미를 갖는 어절을 구분하는 작업입니다.
예) '말'은 다음과 같은 여러 동형이의어의 표면형입니다.

1. '말1': 사람의 생각이나 느낌 따위를 표현하고 전달하는 데 쓰는 음성 기호
2. '말2': 톱질을 하거나 먹줄을 그을 때 밑에 받치는 나무
3. '말3': 곡식, 액체, 가루 따위의 분량을 되는 데 쓰는 그릇
4. '말4': 말과의 포유류
...

아래를 참고해보세요.

* [kr.bydelta.koala.proc.CanDisambiguateSense](../../kr.bydelta.koala.proc/-can-disambiguate-sense/index.md) 동형이의어/다의어 분별 interface
* [Morpheme.getWordSense](../-morpheme/get-word-sense.md) 형태소의 어깨번호/의미번호를 가져오는 API

**Since**
2.0.0

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `IntProperty(value: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)`<br>정수값을 저장하는  Property를 생성합니다. |

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `val value: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>저장될 정수 값 |

### Inherited Properties

| Name | Summary |
|---|---|
| [properties](../-property/properties.md) | `val properties: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)`, `[`Property`](../-property/index.md)`>`<br>가질 수 있는 속성값을 저장할 [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html) |

### Inherited Functions

| Name | Summary |
|---|---|
| [setListProperty](../-property/set-list-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setListProperty(vararg properties: `[`T`](../-property/set-list-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-list-property.md#T)-type 값들을 가변변수로 하여 [properties](../-property/set-list-property.md#kr.bydelta.koala.data.Property$setListProperty(kotlin.Array((kr.bydelta.koala.data.Property.setListProperty.T)))/properties)로 받은 뒤, 저장합니다. |
| [setProperty](../-property/set-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setProperty(property: `[`T`](../-property/set-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-property.md#T)-type 값 [property](../-property/set-property.md#kr.bydelta.koala.data.Property$setProperty(kr.bydelta.koala.data.Property.setProperty.T)/property)를 저장합니다. |
