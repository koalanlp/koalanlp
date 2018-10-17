[kr.bydelta.koala.ext](../index.md) / [kotlin.Char](index.md) / [isIncompleteHangul](./is-incomplete-hangul.md)

# isIncompleteHangul

`fun `[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`.isIncompleteHangul(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

현재 문자 [this](is-incomplete-hangul/-this-.md)가 불완전한 한글 문자인지 확인합니다.

## 사용법

### Kotlin

``` kotlin
'가'.isIncompleteHangul()
```

### Scala

``` kotlin
import kr.bydelta.koala.Implicits.*
'가'.isIncompleteHangul
```

### Java

``` java
Utils.isIncompleteHangul('가')
```

**Since**
2.0.0

**Return**
조건에 맞으면 true

