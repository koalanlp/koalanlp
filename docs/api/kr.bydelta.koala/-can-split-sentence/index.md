[kr.bydelta.koala](../index.md) / [CanSplitSentence](./index.md)

# CanSplitSentence

`interface CanSplitSentence`

문장분리기 Interface

### Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | `open operator fun invoke(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>주어진 문단 [text](invoke.md#kr.bydelta.koala.CanSplitSentence$invoke(kotlin.String)/text)를 문장단위로 분리함. |
| [sentences](sentences.md) | `abstract fun sentences(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>주어진 문단 [text](sentences.md#kr.bydelta.koala.CanSplitSentence$sentences(kotlin.String)/text)를 문장단위로 분리함. |
