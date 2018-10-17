[kr.bydelta.koala.ext](../index.md) / [kotlin.Char](index.md) / [isHangul](./is-hangul.md)

# isHangul

`fun `[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`.isHangul(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

현재 문자 [this](is-hangul/-this-.md)가 한글 완성형 또는 조합용 문자인지 확인합니다.

## 사용법

### Kotlin

``` kotlin
'가'.isHangul()
```

### Scala

``` kotlin
import kr.bydelta.koala.Implicits.*
'가'.isHangul
```

### Java

``` java
Utils.isHangul('가')
```

**Since**
2.0.0

**Return**
조건에 맞으면 true

