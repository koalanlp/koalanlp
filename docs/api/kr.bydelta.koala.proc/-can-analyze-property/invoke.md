[kr.bydelta.koala.proc](../index.md) / [CanAnalyzeProperty](index.md) / [invoke](./invoke.md)

# invoke

`open operator fun invoke(sentence: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`>`
`open operator fun invoke(sentence: `[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`): `[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)

[sentence](invoke.md#kr.bydelta.koala.proc.CanAnalyzeProperty$invoke(kotlin.String)/sentence)를 분석함. 결과는 각 [Sentence](../../kr.bydelta.koala.data/-sentence/index.md)의 property로 저장됨.

`open operator fun invoke(sentences: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`>`

[sentences](invoke.md#kr.bydelta.koala.proc.CanAnalyzeProperty$invoke(kotlin.collections.List((kr.bydelta.koala.data.Sentence)))/sentences)를 분석함. 결과는 각 [Sentence](../../kr.bydelta.koala.data/-sentence/index.md)의 property로 저장됨.

