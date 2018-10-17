[kr.bydelta.koala.ext](../index.md) / [kotlin.CharSequence](index.md) / [hanjaToHangul](./hanja-to-hangul.md)

# hanjaToHangul

`@JvmOverloads fun `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`.hanjaToHangul(headCorrection: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)

국사편찬위원회 한자음가사전에 따라 한자 표기된 내용을 국문 표기로 전환합니다.

## 참고

* [headCorrection](hanja-to-hangul.md#kr.bydelta.koala.ext$hanjaToHangul(kotlin.CharSequence, kotlin.Boolean)/headCorrection) 값이 true인 경우, whitespace에 따라오는 문자에 두음법칙을 자동 적용함. (기본값 true)
* 단, 다음 의존명사는 예외: 냥(兩), 년(年), 리(里), 리(理), 량(輛)

다음 두음법칙은 사전을 조회하지 않기 때문에 적용되지 않음에 유의:

* 한자 파생어나 합성어에서 원 단어의 두음법칙: 예) "신여성"이 옳은 표기이나 "신녀성"으로 표기됨
* 외자가 아닌 이름: 예) "허난설헌"이 옳은 표기이나 "허란설헌"으로 표기됨

## 사용법

### Kotlin

``` kotlin
"可口可樂".hanjaToHangul()
```

### Scala

``` kotlin
import kr.bydelta.koala.Implicits.*
"可口可樂".hanjaToHangul
```

### Java

``` java
Utils.hanjaToHangul("可口可樂")
```

**Since**
2.0.0

**Return**
해당 범위의 한자라면 true

