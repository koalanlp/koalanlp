[kr.bydelta.koala.proc](../index.md) / [CanTagAParagraph](./index.md)

# CanTagAParagraph

`abstract class CanTagAParagraph<S> : `[`CanTagASentence`](../-can-tag-a-sentence/index.md)`<`[`S`](index.md#S)`>`

문단1개, 문장1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S](index.md#S) 타입으로 돌려줌.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `CanTagAParagraph()`<br>문단1개, 문장1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S](index.md#S) 타입으로 돌려줌. |

### Functions

| Name | Summary |
|---|---|
| [tag](tag.md) | `fun tag(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`>`<br>주어진 문단 [text](tag.md#kr.bydelta.koala.proc.CanTagAParagraph$tag(kotlin.String)/text)을 분석하여 품사를 부착하고, 결과로 [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Sentence](../../kr.bydelta.koala.data/-sentence/index.md)&gt; 객체를 돌려줌. |
| [tagParagraphOriginal](tag-paragraph-original.md) | `abstract fun tagParagraphOriginal(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`S`](index.md#S)`>`<br>변환되지않은, [text](tag-paragraph-original.md#kr.bydelta.koala.proc.CanTagAParagraph$tagParagraphOriginal(kotlin.String)/text)의 분석결과 [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[S](index.md#S)&gt;를 반환. |

### Inherited Functions

| Name | Summary |
|---|---|
| [convertSentence](../-can-tag-a-sentence/convert-sentence.md) | `abstract fun convertSentence(result: `[`S`](../-can-tag-a-sentence/index.md#S)`): `[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)<br>[S](../-can-tag-a-sentence/index.md#S) 타입의 분석결과 [result](../-can-tag-a-sentence/convert-sentence.md#kr.bydelta.koala.proc.CanTagASentence$convertSentence(kr.bydelta.koala.proc.CanTagASentence.S)/result)를 변환, [Sentence](../../kr.bydelta.koala.data/-sentence/index.md)를 구성함. |
| [tagSentence](../-can-tag-a-sentence/tag-sentence.md) | `fun tagSentence(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)<br>주어진 문장 [text](../-can-tag-a-sentence/tag-sentence.md#kr.bydelta.koala.proc.CanTagASentence$tagSentence(kotlin.String)/text)을 분석하여 품사를 부착하고, 결과로 [Sentence](../../kr.bydelta.koala.data/-sentence/index.md) 객체를 돌려줌. |
| [tagSentenceOriginal](../-can-tag-a-sentence/tag-sentence-original.md) | `abstract fun tagSentenceOriginal(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`S`](../-can-tag-a-sentence/index.md#S)<br>변환되지않은, [text](../-can-tag-a-sentence/tag-sentence-original.md#kr.bydelta.koala.proc.CanTagASentence$tagSentenceOriginal(kotlin.String)/text)의 분석결과 [S](../-can-tag-a-sentence/index.md#S)를 반환. |
