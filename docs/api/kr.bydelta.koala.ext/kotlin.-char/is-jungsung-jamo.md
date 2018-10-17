[kr.bydelta.koala.ext](../index.md) / [kotlin.Char](index.md) / [isJungsungJamo](./is-jungsung-jamo.md)

# isJungsungJamo

`fun `[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`.isJungsungJamo(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

현재 문자 [this](is-jungsung-jamo/-this-.md)가 현대 한글 중성 모음 문자인지 확인합니다.

## 사용법

### Kotlin

``` kotlin
'가'.isJungsungJamo()
```

### Scala

``` kotlin
import kr.bydelta.koala.Implicits.*
'가'.isJungsungJamo
```

### Java

``` java
Utils.isJungsungJamo('가')
```

**Since**
2.0.0

**Return**
조건에 맞으면 true

