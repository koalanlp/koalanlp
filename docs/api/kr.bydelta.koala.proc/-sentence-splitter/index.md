[kr.bydelta.koala.proc](../index.md) / [SentenceSplitter](./index.md)

# SentenceSplitter

`object SentenceSplitter`

세종 태그셋에 기반한 Heuristic 문장분리기

다음 조건에 따라 문장을 분리합니다:

1. 열린 괄호나 인용부호가 없고,
2. 숫자나 외국어로 둘러싸이지 않은 문장부호([POS.SF](../../kr.bydelta.koala/-p-o-s/-s-f.md))가 어절의 마지막에 왔을 경우.

## 사용법 예제

`SentenceSplitter`는 이미 singleton object이므로 초기화가 필요하지 않습니다.

### Kotlin

``` kotlin
val sentence = ... //Tagged result
val splitted = SentenceSplitter.sentences(sentence)
// 또는
val splitted = SentenceSplitter(sentence)
```

### Scala

``` scala
import kr.bydelta.koala.Implicits.*
val sentence = ... //Tagged result
val splitted = SentenceSplitter.sentences(sentence)
// 또는
val splitted = SentenceSplitter(sentence)
```

### Java

``` java
Sentence sentence = ... //Tagged result
List<Sentence> splitted = SentenceSplitter.sentences(sentence);
// 또는
List<Sentence> splitted = SentenceSplitter.invoke(sentence);
```

### Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | `operator fun invoke(para: `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`Word`](../../kr.bydelta.koala.data/-word/index.md)`>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`>`<br>분석결과를 토대로 문장을 분리함. |
