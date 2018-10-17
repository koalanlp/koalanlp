[kr.bydelta.koala.proc](../index.md) / [CanSplitSentence](./index.md)

# CanSplitSentence

`interface CanSplitSentence`

문장분리기 Interface

텍스트를 받아서 텍스트 문장들로 분리합니다.

## 사용법 예제

문장분리기 `SentenceSplitter`가 `CanSplitSentence`를 상속받았다면,

### Kotlin

``` kotlin
val splitter = SentenceSplitter()
val sentence = "분석할 문장을 적었습니다."
val splitted = splitter.sentences(sentence)
// 또는
val splitted = splitter(sentence)
```

### Scala

``` scala
import kr.bydelta.koala.Implicits.*
val splitter = new SentenceSplitter()
val sentence = "분석할 문장을 적었습니다."
val splitted = splitter.sentences(sentence)
// 또는
val splitted = splitter(sentence)
```

### Java

``` java
SentenceSplitter splitter = new SentenceSplitter();
String sentence = "분석할 문장을 적었습니다.";
List<String> splitted = splitter.sentences(sentence);
// 또는
List<String> splitted = splitter.invoke(sentence);
```

### Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | `open operator fun invoke(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>주어진 문단 [text](invoke.md#kr.bydelta.koala.proc.CanSplitSentence$invoke(kotlin.String)/text)를 문장단위로 분리합니다. |
| [sentences](sentences.md) | `abstract fun sentences(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>주어진 문단 [text](sentences.md#kr.bydelta.koala.proc.CanSplitSentence$sentences(kotlin.String)/text)를 문장단위로 분리합니다. |
