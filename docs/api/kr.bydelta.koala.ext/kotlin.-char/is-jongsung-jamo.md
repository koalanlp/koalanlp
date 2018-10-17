[kr.bydelta.koala.ext](../index.md) / [kotlin.Char](index.md) / [isJongsungJamo](./is-jongsung-jamo.md)

# isJongsungJamo

`fun `[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`.isJongsungJamo(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

현재 문자 [this](is-jongsung-jamo/-this-.md)가 한글 종성 자음 문자인지 확인합니다.

## 사용법

### Kotlin

``` kotlin
'가'.isJongsungJamo()
```

### Scala

``` kotlin
import kr.bydelta.koala.Implicits.*
'가'.isJongsungJamo
```

### Java

``` java
Utils.isJongsungJamo('가')
```

**Since**
2.0.0

**Return**
조건에 맞으면 true

