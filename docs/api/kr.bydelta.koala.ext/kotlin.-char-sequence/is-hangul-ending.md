[kr.bydelta.koala.ext](../index.md) / [kotlin.CharSequence](index.md) / [isHangulEnding](./is-hangul-ending.md)

# isHangulEnding

`fun `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`.isHangulEnding(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

현재 문자열 [this](is-hangul-ending/-this-.md)가 한글 (완성/조합)로 끝나는지 확인합니다.

## 사용법

### Kotlin

``` kotlin
"가나다".isHangulEnding()
```

### Scala

``` kotlin
import kr.bydelta.koala.Implicits.*
"가나다".isHangulEnding
```

### Java

``` java
Utils.isHangulEnding("가나다")
```

**Since**
2.0.0

**Return**
조건에 맞으면 true

