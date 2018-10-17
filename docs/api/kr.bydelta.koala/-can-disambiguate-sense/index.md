[kr.bydelta.koala](../index.md) / [CanDisambiguateSense](./index.md)

# CanDisambiguateSense

`interface CanDisambiguateSense : `[`CanAnalyzeProperty`](../-can-analyze-property/index.md)`<`[`Sentence`](../-sentence/index.md)`, `[`Sentence`](../-sentence/index.md)`>`

다의어 분별 (Word sense disambiguation) Interface

### Functions

| Name | Summary |
|---|---|
| [attachProperty](attach-property.md) | `open fun attachProperty(sentence: `[`Sentence`](../-sentence/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[sentence](attach-property.md#kr.bydelta.koala.CanDisambiguateSense$attachProperty(kr.bydelta.koala.Sentence)/sentence)를 분석하여 property 값을 추가함 |

### Inherited Functions

| Name | Summary |
|---|---|
| [convert](../-can-analyze-property/convert.md) | `abstract fun convert(sentence: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>`<br>String [sentence](../-can-analyze-property/convert.md#kr.bydelta.koala.CanAnalyzeProperty$convert(kotlin.String)/sentence)를 [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Sentence](../-sentence/index.md)&gt;로 변환함. |
| [getProperty](../-can-analyze-property/get-property.md) | `abstract fun getProperty(item: `[`IN`](../-can-analyze-property/index.md#IN)`): `[`OUT`](../-can-analyze-property/index.md#OUT)<br>[item](../-can-analyze-property/get-property.md#kr.bydelta.koala.CanAnalyzeProperty$getProperty(kr.bydelta.koala.CanAnalyzeProperty.IN)/item)을 분석하여 property 값을 반환함 |
| [invoke](../-can-analyze-property/invoke.md) | `open operator fun invoke(sentence: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>`<br>`open operator fun invoke(sentence: `[`Sentence`](../-sentence/index.md)`): `[`Sentence`](../-sentence/index.md)<br>[sentence](../-can-analyze-property/invoke.md#kr.bydelta.koala.CanAnalyzeProperty$invoke(kotlin.String)/sentence)를 분석함. 결과는 각 [Sentence](../-sentence/index.md)의 property로 저장됨.`open operator fun invoke(sentences: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>`<br>[sentences](../-can-analyze-property/invoke.md#kr.bydelta.koala.CanAnalyzeProperty$invoke(kotlin.collections.List((kr.bydelta.koala.Sentence)))/sentences)를 분석함. 결과는 각 [Sentence](../-sentence/index.md)의 property로 저장됨. |
| [parse](../-can-analyze-property/parse.md) | `open fun parse(sentence: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>`<br>String [sentence](../-can-analyze-property/parse.md#kr.bydelta.koala.CanAnalyzeProperty$parse(kotlin.String)/sentence)를 분석함. 결과는 각 [Sentence](../-sentence/index.md)의 property로 저장됨.`open fun parse(sentence: `[`Sentence`](../-sentence/index.md)`): `[`Sentence`](../-sentence/index.md)<br>[sentence](../-can-analyze-property/parse.md#kr.bydelta.koala.CanAnalyzeProperty$parse(kr.bydelta.koala.Sentence)/sentence)를 분석함. 결과는 각 [Sentence](../-sentence/index.md)의 property로 저장됨.`open fun parse(sentences: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../-sentence/index.md)`>`<br>[sentences](../-can-analyze-property/parse.md#kr.bydelta.koala.CanAnalyzeProperty$parse(kotlin.collections.List((kr.bydelta.koala.Sentence)))/sentences)를 분석함. 결과는 각 [Sentence](../-sentence/index.md)의 property로 저장됨. |
