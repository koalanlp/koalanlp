[kr.bydelta.koala.data](../index.md) / [Morpheme](index.md) / [component2](./component2.md)

# component2

`operator fun component2(): `[`POS`](../../kr.bydelta.koala/-p-o-s/index.md)

[tag](tag.md) 값을 두번째 component로 반환합니다.

## Kotlin

다음과 같이 사용할 수 있습니다.

```
val (surface, tag) = Morpheme("String", POS.NNP)
// surface == "String"
// tag == POs.NNP
```

