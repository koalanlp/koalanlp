[kr.bydelta.koala.proc](../index.md) / [CanAnalyzeSentenceProperty](./index.md)

# CanAnalyzeSentenceProperty

`interface CanAnalyzeSentenceProperty<P : `[`Property`](../../kr.bydelta.koala.data/-property/index.md)`> : `[`CanAnalyzeProperty`](../-can-analyze-property/index.md)`<`[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`, `[`P`](index.md#P)`>`

[Sentence](../../kr.bydelta.koala.data/-sentence/index.md) 객체에 property를 추가할 수 있는 interface

### Functions

| Name | Summary |
|---|---|
| [attachProperty](attach-property.md) | `open fun attachProperty(sentence: `[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[sentence](attach-property.md#kr.bydelta.koala.proc.CanAnalyzeSentenceProperty$attachProperty(kr.bydelta.koala.data.Sentence)/sentence)를 분석하여 property 값을 추가함 |

### Inherited Functions

| Name | Summary |
|---|---|
| [convert](../-can-analyze-property/convert.md) | `abstract fun convert(sentence: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`>`<br>String [sentence](../-can-analyze-property/convert.md#kr.bydelta.koala.proc.CanAnalyzeProperty$convert(kotlin.String)/sentence)를 [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Sentence](../../kr.bydelta.koala.data/-sentence/index.md)&gt;로 변환함. |
| [getProperty](../-can-analyze-property/get-property.md) | `abstract fun getProperty(item: `[`IN`](../-can-analyze-property/index.md#IN)`): `[`OUT`](../-can-analyze-property/index.md#OUT)<br>[item](../-can-analyze-property/get-property.md#kr.bydelta.koala.proc.CanAnalyzeProperty$getProperty(kr.bydelta.koala.proc.CanAnalyzeProperty.IN)/item)을 분석하여 property 값을 반환함 |
| [invoke](../-can-analyze-property/invoke.md) | `open operator fun invoke(sentence: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`>`<br>`open operator fun invoke(sentence: `[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`): `[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)<br>[sentence](../-can-analyze-property/invoke.md#kr.bydelta.koala.proc.CanAnalyzeProperty$invoke(kotlin.String)/sentence)를 분석함. 결과는 각 [Sentence](../../kr.bydelta.koala.data/-sentence/index.md)의 property로 저장됨.`open operator fun invoke(sentences: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`>`<br>[sentences](../-can-analyze-property/invoke.md#kr.bydelta.koala.proc.CanAnalyzeProperty$invoke(kotlin.collections.List((kr.bydelta.koala.data.Sentence)))/sentences)를 분석함. 결과는 각 [Sentence](../../kr.bydelta.koala.data/-sentence/index.md)의 property로 저장됨. |
| [parse](../-can-analyze-property/parse.md) | `open fun parse(sentence: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`>`<br>String [sentence](../-can-analyze-property/parse.md#kr.bydelta.koala.proc.CanAnalyzeProperty$parse(kotlin.String)/sentence)를 분석함. 결과는 각 [Sentence](../../kr.bydelta.koala.data/-sentence/index.md)의 property로 저장됨.`open fun parse(sentence: `[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`): `[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)<br>[sentence](../-can-analyze-property/parse.md#kr.bydelta.koala.proc.CanAnalyzeProperty$parse(kr.bydelta.koala.data.Sentence)/sentence)를 분석함. 결과는 각 [Sentence](../../kr.bydelta.koala.data/-sentence/index.md)의 property로 저장됨.`open fun parse(sentences: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Sentence`](../../kr.bydelta.koala.data/-sentence/index.md)`>`<br>[sentences](../-can-analyze-property/parse.md#kr.bydelta.koala.proc.CanAnalyzeProperty$parse(kotlin.collections.List((kr.bydelta.koala.data.Sentence)))/sentences)를 분석함. 결과는 각 [Sentence](../../kr.bydelta.koala.data/-sentence/index.md)의 property로 저장됨. |

### Inheritors

| Name | Summary |
|---|---|
| [CanDepParse](../-can-dep-parse.md) | `interface CanDepParse : `[`CanAnalyzeSentenceProperty`](./index.md)`<`[`DepTree`](../../kr.bydelta.koala.data/-dep-tree/index.md)`>`<br>의존구문분석 Interface |
| [CanLabelSemanticRole](../-can-label-semantic-role.md) | `interface CanLabelSemanticRole : `[`CanAnalyzeSentenceProperty`](./index.md)`<`[`RoleTree`](../../kr.bydelta.koala.data/-role-tree/index.md)`>`<br>의미역 분석(Semantic Role Labeling) Interface |
| [CanRecognizeEntity](../-can-recognize-entity.md) | `interface CanRecognizeEntity : `[`CanAnalyzeSentenceProperty`](./index.md)`<`[`ListProperty`](../../kr.bydelta.koala.data/-list-property/index.md)`<`[`Entity`](../../kr.bydelta.koala.data/-entity/index.md)`>>`<br>개체명 인식 (Named Entity Recognition) Interface |
| [CanSyntaxParse](../-can-syntax-parse.md) | `interface CanSyntaxParse : `[`CanAnalyzeSentenceProperty`](./index.md)`<`[`SyntaxTree`](../../kr.bydelta.koala.data/-syntax-tree/index.md)`>`<br>구문분석 Interface |
