[kr.bydelta.koala](../index.md) / [CanTagOnlyASentence](index.md) / [convertSentence](./convert-sentence.md)

# convertSentence

`protected abstract fun convertSentence(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, result: `[`S`](index.md#S)`): `[`Sentence`](../-sentence/index.md)

[S](index.md#S) 타입의 분석결과 [result](convert-sentence.md#kr.bydelta.koala.CanTagOnlyASentence$convertSentence(kotlin.String, kr.bydelta.koala.CanTagOnlyASentence.S)/result)를 변환, [Sentence](../-sentence/index.md)를 구성함.

### Parameters

`text` - 품사분석을 시행할 문단 String

`result` - 변환할 분석결과.

**Return**
변환된 Sentence 객체

