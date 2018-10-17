[kr.bydelta.koala.ext](../index.md) / [kotlin.CharSequence](index.md) / [hangulToAlpha](./hangul-to-alpha.md)

# hangulToAlpha

`fun `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`.hangulToAlpha(): `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)

주어진 문자열 [this](hangul-to-alpha/-this-.md)에 적힌 알파벳 발음을 알파벳으로 변환하여 문자열로 반환합니다.

## 사용법

### Kotlin

``` kotlin
"에이비씨".hangulToAlpha()
```

### Scala

``` kotlin
import kr.bydelta.koala.Implicits.*
"에이비씨".hangulToAlpha
```

### Java

``` java
Utils.hangulToAlpha("에이비씨")
```

**Since**
2.0.0

**Return**
영문 변환된 문자열

