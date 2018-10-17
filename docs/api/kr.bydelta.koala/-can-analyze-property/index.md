[kr.bydelta.koala](../index.md) / [CanAnalyzeProperty](./index.md)

# CanAnalyzeProperty

`interface CanAnalyzeProperty<IN : `[`Property`](../-property/index.md)`, OUT>`

[Sentence](../-sentence/index.md) 객체에 property를 추가할 수 있는 interface

### Functions

| Name | Summary |
|---|---|
| [attachProperty](attach-property.md) | `abstract fun attachProperty(sentence: `[`Sentence`](../-sentence/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[sentence](attach-property.md#kr.bydelta.koala.CanAnalyzeProperty$attachProperty(kr.bydelta.koala.Sentence)/sentence)를 분석하여 property 값을 추가함 |
| [convert](convert.md) | `abstract fun convert(sentence: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>`<br>String [sentence](convert.md#kr.bydelta.koala.CanAnalyzeProperty$convert(kotlin.String)/sentence)를 [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Sentence](../-sentence/index.md)&gt;로 변환함. |
| [getProperty](get-property.md) | `abstract fun getProperty(item: `[`IN`](index.md#IN)`): `[`OUT`](index.md#OUT)<br>[item](get-property.md#kr.bydelta.koala.CanAnalyzeProperty$getProperty(kr.bydelta.koala.CanAnalyzeProperty.IN)/item)을 분석하여 property 값을 반환함 |
| [invoke](invoke.md) | `open operator fun invoke(sentence: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>`<br>`open operator fun invoke(sentence: `[`Sentence`](../-sentence/index.md)`): `[`Sentence`](../-sentence/index.md)<br>[sentence](invoke.md#kr.bydelta.koala.CanAnalyzeProperty$invoke(kotlin.String)/sentence)를 분석함. 결과는 각 [Sentence](../-sentence/index.md)의 property로 저장됨.`open operator fun invoke(sentences: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>`<br>[sentences](invoke.md#kr.bydelta.koala.CanAnalyzeProperty$invoke(kotlin.collections.List((kr.bydelta.koala.Sentence)))/sentences)를 분석함. 결과는 각 [Sentence](../-sentence/index.md)의 property로 저장됨. |
| [parse](parse.md) | `open fun parse(sentence: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>`<br>String [sentence](parse.md#kr.bydelta.koala.CanAnalyzeProperty$parse(kotlin.String)/sentence)를 분석함. 결과는 각 [Sentence](../-sentence/index.md)의 property로 저장됨.`open fun parse(sentence: `[`Sentence`](../-sentence/index.md)`): `[`Sentence`](../-sentence/index.md)<br>[sentence](parse.md#kr.bydelta.koala.CanAnalyzeProperty$parse(kr.bydelta.koala.Sentence)/sentence)를 분석함. 결과는 각 [Sentence](../-sentence/index.md)의 property로 저장됨.`open fun parse(sentences: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>`<br>[sentences](parse.md#kr.bydelta.koala.CanAnalyzeProperty$parse(kotlin.collections.List((kr.bydelta.koala.Sentence)))/sentences)를 분석함. 결과는 각 [Sentence](../-sentence/index.md)의 property로 저장됨. |

### Inheritors

| Name | Summary |
|---|---|
| [CanAnalyzeSentenceProperty](../-can-analyze-sentence-property/index.md) | `interface CanAnalyzeSentenceProperty<P : `[`Property`](../-property/index.md)`> : `[`CanAnalyzeProperty`](./index.md)`<`[`Sentence`](../-sentence/index.md)`, `[`P`](../-can-analyze-sentence-property/index.md#P)`>`<br>[Sentence](../-sentence/index.md) 객체에 property를 추가할 수 있는 interface |
| [CanDisambiguateSense](../-can-disambiguate-sense/index.md) | `interface CanDisambiguateSense : `[`CanAnalyzeProperty`](./index.md)`<`[`Sentence`](../-sentence/index.md)`, `[`Sentence`](../-sentence/index.md)`>`<br>다의어 분별 (Word sense disambiguation) Interface |
