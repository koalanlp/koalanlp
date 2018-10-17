[kr.bydelta.koala.ext](../index.md) / [kotlin.Char](index.md) / [isCJKHanja](./is-c-j-k-hanja.md)

# isCJKHanja

`fun `[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`.isCJKHanja(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

현재 문자 [this](is-c-j-k-hanja/-this-.md)가 한중일 통합한자, 통합한자 확장 - A, 호환용 한자 범위인지 확인합니다.
(국사편찬위원회 한자음가사전은 해당 범위에서만 정의되어 있어, 별도 확인합니다.)

## 사용법

### Kotlin

``` kotlin
'樂'.isCJKHanja()
```

### Scala

``` kotlin
import kr.bydelta.koala.Implicits.*
'樂'.isCJKHanja
```

### Java

``` java
Utils.isCJKHanja('可')
```

**Since**
2.0.0

**Return**
해당 범위의 한자라면 true

