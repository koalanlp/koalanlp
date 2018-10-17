[kr.bydelta.koala.ext](../index.md) / [kotlin.Char](index.md) / [isHanja](./is-hanja.md)

# isHanja

`fun `[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`.isHanja(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

현재 문자 [this](is-hanja/-this-.md)가 한자 범위인지 확인합니다.

## 사용법

### Kotlin

``` kotlin
'樂'.isHanja()
```

### Scala

``` kotlin
import kr.bydelta.koala.Implicits.*
'樂'.isHanja
```

### Java

``` java
Utils.isHanja('可')
```

**Since**
2.0.0

**Return**
한자범위라면 true

