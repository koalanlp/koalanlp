[kr.bydelta.koala.proc](../index.md) / [CanTag](./index.md)

# CanTag

`interface CanTag`

품사분석기 interface

### Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | `open operator fun invoke(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`>`<br>주어진 문단 [text](invoke.md#kr.bydelta.koala.proc.CanTag$invoke(kotlin.String)/text)을 분석하여 품사를 부착하고, 결과로 [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Sentence](../../kr.bydelta.koala.data/-sentence/index.md)&gt; 객체를 돌려줌. |
| [tag](tag.md) | `abstract fun tag(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`>`<br>주어진 문단 [text](tag.md#kr.bydelta.koala.proc.CanTag$tag(kotlin.String)/text)을 분석하여 품사를 부착하고, 결과로 [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Sentence](../../kr.bydelta.koala.data/-sentence/index.md)&gt; 객체를 돌려줌. |
| [tagSentence](tag-sentence.md) | `abstract fun tagSentence(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)<br>주어진 문장 [text](tag-sentence.md#kr.bydelta.koala.proc.CanTag$tagSentence(kotlin.String)/text)을 분석하여 품사를 부착하고, 결과로 [Sentence](../../kr.bydelta.koala.data/-sentence/index.md) 객체를 돌려줌. |

### Inheritors

| Name | Summary |
|---|---|
| [CanTagASentence](../-can-tag-a-sentence/index.md) | `abstract class CanTagASentence<S> : `[`CanTag`](./index.md)<br>문장 1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S](../-can-tag-a-sentence/index.md#S) 타입으로 돌려줌. |
| [CanTagOnlyAParagraph](../-can-tag-only-a-paragraph/index.md) | `abstract class CanTagOnlyAParagraph<S> : `[`CanTag`](./index.md)<br>문장1개는 불가하지만, 문단1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S](../-can-tag-only-a-paragraph/index.md#S) 타입으로 돌려줌. |
| [CanTagOnlyASentence](../-can-tag-only-a-sentence/index.md) | `abstract class CanTagOnlyASentence<S> : `[`CanTag`](./index.md)<br>문단1개는 불가하지만, 문장1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S](../-can-tag-only-a-sentence/index.md#S) 타입으로 돌려줌. |
