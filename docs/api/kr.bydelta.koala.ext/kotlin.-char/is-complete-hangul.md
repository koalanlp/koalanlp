[kr.bydelta.koala.ext](../index.md) / [kotlin.Char](index.md) / [isCompleteHangul](./is-complete-hangul.md)

# isCompleteHangul

`fun `[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`.isCompleteHangul(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

현재 문자 [this](is-complete-hangul/-this-.md)가 초성, 중성, 종성(선택적)을 다 갖춘 문자인지 확인합니다.

## 사용법

### Kotlin

``` kotlin
'가'.isCompleteHangul()
```

### Scala

``` kotlin
import kr.bydelta.koala.Implicits.*
'가'.isCompleteHangul
```

### Java

``` java
Utils.isCompleteHangul('가')
```

**Since**
2.0.0

**Return**
조건에 맞으면 true

