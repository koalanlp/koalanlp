[kr.bydelta.koala.ext](../index.md) / [kotlin.CharSequence](index.md) / [alphaToHangul](./alpha-to-hangul.md)

# alphaToHangul

`fun `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`.alphaToHangul(): `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)

주어진 문자열 [this](alpha-to-hangul/-this-.md) 에서 알파벳이 발음되는 대로 국문 문자열로 표기하여 값으로 돌려줍니다.

## 사용법

### Kotlin

``` kotlin
"ABC".alphaToHangul()
```

### Scala

``` kotlin
import kr.bydelta.koala.Implicits.*
"ABC".alphaToHangul
```

### Java

``` java
Utils.alphaToHangul("ABC")
```

**Since**
2.0.0

**Return**
국문 발음 표기된 문자열

