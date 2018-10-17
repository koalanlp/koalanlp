[kr.bydelta.koala.data](../index.md) / [Property](index.md) / [setListProperty](./set-list-property.md)

# setListProperty

`fun <T : `[`Property`](index.md)`> setListProperty(vararg properties: `[`T`](set-list-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

[T](set-list-property.md#T)-type 값들을 가변변수로 하여 [properties](set-list-property.md#kr.bydelta.koala.data.Property$setListProperty(kotlin.Array((kr.bydelta.koala.data.Property.setListProperty.T)))/properties)로 받은 뒤, 저장합니다.

만약, 이전에 저장을 한 적이 있다면, 덮어쓰기 하지 않고 [IllegalStateException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-state-exception/index.html)를 발생합니다.

### Parameters

`properties` - 리스트로 저장할 속성값들 (가변변수)

**Since**
2.0.0

