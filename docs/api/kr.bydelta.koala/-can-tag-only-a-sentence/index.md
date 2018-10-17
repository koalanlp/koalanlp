[kr.bydelta.koala](../index.md) / [CanTagOnlyASentence](./index.md)

# CanTagOnlyASentence

`abstract class CanTagOnlyASentence<S> : `[`CanTag`](../-can-tag/index.md)

문단1개는 불가하지만, 문장1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S](index.md#S) 타입으로 돌려줌.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `CanTagOnlyASentence()`<br>문단1개는 불가하지만, 문장1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S](index.md#S) 타입으로 돌려줌. |

### Functions

| Name | Summary |
|---|---|
| [convertSentence](convert-sentence.md) | `abstract fun convertSentence(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, result: `[`S`](index.md#S)`): `[`Sentence`](../-sentence/index.md)<br>[S](index.md#S) 타입의 분석결과 [result](convert-sentence.md#kr.bydelta.koala.CanTagOnlyASentence$convertSentence(kotlin.String, kr.bydelta.koala.CanTagOnlyASentence.S)/result)를 변환, [Sentence](../-sentence/index.md)를 구성함. |
| [tag](tag.md) | `fun tag(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>`<br>주어진 문단 [text](tag.md#kr.bydelta.koala.CanTagOnlyASentence$tag(kotlin.String)/text)을 분석하여 품사를 부착하고, 결과로 [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Sentence](../-sentence/index.md)&gt; 객체를 돌려줌. |
| [tagSentence](tag-sentence.md) | `fun tagSentence(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Sentence`](../-sentence/index.md)<br>주어진 문장 [text](tag-sentence.md#kr.bydelta.koala.CanTagOnlyASentence$tagSentence(kotlin.String)/text)을 분석하여 품사를 부착하고, 결과로 [Sentence](../-sentence/index.md) 객체를 돌려줌. |
| [tagSentenceOriginal](tag-sentence-original.md) | `abstract fun tagSentenceOriginal(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`S`](index.md#S)<br>변환되지않은, [text](tag-sentence-original.md#kr.bydelta.koala.CanTagOnlyASentence$tagSentenceOriginal(kotlin.String)/text)의 분석결과 [S](index.md#S)를 반환. |

### Inherited Functions

| Name | Summary |
|---|---|
| [invoke](../-can-tag/invoke.md) | `open operator fun invoke(text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>`<br>주어진 문단 [text](../-can-tag/invoke.md#kr.bydelta.koala.CanTag$invoke(kotlin.String)/text)을 분석하여 품사를 부착하고, 결과로 [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Sentence](../-sentence/index.md)&gt; 객체를 돌려줌. |
