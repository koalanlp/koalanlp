[kr.bydelta.koala.data](../index.md) / [Property](index.md) / [setProperty](./set-property.md)

# setProperty

`fun <T : `[`Property`](index.md)`> setProperty(property: `[`T`](set-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

[T](set-property.md#T)-type 값 [property](set-property.md#kr.bydelta.koala.data.Property$setProperty(kr.bydelta.koala.data.Property.setProperty.T)/property)를 저장합니다.

만약, 이전에 저장을 한 적이 있다면, 덮어쓰기 하지 않고 [IllegalStateException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-state-exception/index.html)를 발생시킵니다.
적당한 키를 찾을 수 없다면, [IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html)를 발생시킵니다.

### Parameters

`property` - 속성으로 저장할 값입니다.

### Exceptions

`IllegalStateException` - 저장한 적이 있는 속성을 덮어씌우려 할 때

`IllegalArgumentException` - 속성이 KoalaNLP 내부에서 정의된 범위를 벗어날 때

**Since**
2.0.0

