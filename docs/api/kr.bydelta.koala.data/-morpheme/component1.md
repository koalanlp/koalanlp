[kr.bydelta.koala.data](../index.md) / [Morpheme](index.md) / [component1](./component1.md)

# component1

`operator fun component1(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

[surface](surface.md) 값을 첫 component로 반환합니다.

## Kotlin

다음과 같이 사용할 수 있습니다.

```
val (surface, tag) = Morpheme("String", POS.NNP)
// surface == "String"
// tag == POs.NNP
```

