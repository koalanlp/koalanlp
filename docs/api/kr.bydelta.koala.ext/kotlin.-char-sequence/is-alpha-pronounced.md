[kr.bydelta.koala.ext](../index.md) / [kotlin.CharSequence](index.md) / [isAlphaPronounced](./is-alpha-pronounced.md)

# isAlphaPronounced

`fun `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`.isAlphaPronounced(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

주어진 문자열 [this](is-alpha-pronounced/-this-.md)가 알파벳이 발음되는 대로 표기된 문자열인지 확인합니다.

## 사용법

### Kotlin

``` kotlin
"에이비씨".isAlphaPronounced()
```

### Scala

``` kotlin
import kr.bydelta.koala.Implicits.*
"에이비씨".isAlphaPronounced
```

### Java

``` java
Utils.isAlphaPronounced("에이비씨")
```

**Since**
2.0.0

**Return**
영문 발음으로만 구성되었다면 true

