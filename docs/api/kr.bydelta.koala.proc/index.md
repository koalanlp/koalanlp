[kr.bydelta.koala.proc](./index.md)

## Package kr.bydelta.koala.proc

### Types

| Name | Summary |
|---|---|
| [CanAnalyzeProperty](-can-analyze-property/index.md) | `interface CanAnalyzeProperty<IN : `[`Property`](../kr.bydelta.koala.data/-property/index.md)`, OUT>`<br>[Sentence](../kr.bydelta.koala.data/-sentence/index.md) 객체에 property를 추가할 수 있는 interface |
| [CanAnalyzeSentenceProperty](-can-analyze-sentence-property/index.md) | `interface CanAnalyzeSentenceProperty<P : `[`Property`](../kr.bydelta.koala.data/-property/index.md)`> : `[`CanAnalyzeProperty`](-can-analyze-property/index.md)`<`[`Sentence`](../kr.bydelta.koala.data/-sentence/index.md)`, `[`P`](-can-analyze-sentence-property/index.md#P)`>`<br>[Sentence](../kr.bydelta.koala.data/-sentence/index.md) 객체에 property를 추가할 수 있는 interface |
| [CanDepParse](-can-dep-parse.md) | `interface CanDepParse : `[`CanAnalyzeSentenceProperty`](-can-analyze-sentence-property/index.md)`<`[`DepTree`](../kr.bydelta.koala.data/-dep-tree/index.md)`>`<br>의존구문분석 Interface |
| [CanDisambiguateSense](-can-disambiguate-sense/index.md) | `interface CanDisambiguateSense : `[`CanAnalyzeProperty`](-can-analyze-property/index.md)`<`[`Sentence`](../kr.bydelta.koala.data/-sentence/index.md)`, `[`Sentence`](../kr.bydelta.koala.data/-sentence/index.md)`>`<br>다의어 분별 (Word sense disambiguation) Interface |
| [CanLabelSemanticRole](-can-label-semantic-role.md) | `interface CanLabelSemanticRole : `[`CanAnalyzeSentenceProperty`](-can-analyze-sentence-property/index.md)`<`[`RoleTree`](../kr.bydelta.koala.data/-role-tree/index.md)`>`<br>의미역 분석(Semantic Role Labeling) Interface |
| [CanRecognizeEntity](-can-recognize-entity.md) | `interface CanRecognizeEntity : `[`CanAnalyzeSentenceProperty`](-can-analyze-sentence-property/index.md)`<`[`ListProperty`](../kr.bydelta.koala.data/-list-property/index.md)`<`[`Entity`](../kr.bydelta.koala.data/-entity/index.md)`>>`<br>개체명 인식 (Named Entity Recognition) Interface |
| [CanSplitSentence](-can-split-sentence/index.md) | `interface CanSplitSentence`<br>문장분리기 Interface |
| [CanSyntaxParse](-can-syntax-parse.md) | `interface CanSyntaxParse : `[`CanAnalyzeSentenceProperty`](-can-analyze-sentence-property/index.md)`<`[`SyntaxTree`](../kr.bydelta.koala.data/-syntax-tree/index.md)`>`<br>구문분석 Interface |
| [CanTag](-can-tag/index.md) | `interface CanTag`<br>품사분석기 interface |
| [CanTagAParagraph](-can-tag-a-paragraph/index.md) | `abstract class CanTagAParagraph<S> : `[`CanTagASentence`](-can-tag-a-sentence/index.md)`<`[`S`](-can-tag-a-paragraph/index.md#S)`>`<br>문단1개, 문장1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S](-can-tag-a-paragraph/index.md#S) 타입으로 돌려줌. |
| [CanTagASentence](-can-tag-a-sentence/index.md) | `abstract class CanTagASentence<S> : `[`CanTag`](-can-tag/index.md)<br>문장 1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S](-can-tag-a-sentence/index.md#S) 타입으로 돌려줌. |
| [CanTagOnlyAParagraph](-can-tag-only-a-paragraph/index.md) | `abstract class CanTagOnlyAParagraph<S> : `[`CanTag`](-can-tag/index.md)<br>문장1개는 불가하지만, 문단1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S](-can-tag-only-a-paragraph/index.md#S) 타입으로 돌려줌. |
| [CanTagOnlyASentence](-can-tag-only-a-sentence/index.md) | `abstract class CanTagOnlyASentence<S> : `[`CanTag`](-can-tag/index.md)<br>문단1개는 불가하지만, 문장1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S](-can-tag-only-a-sentence/index.md#S) 타입으로 돌려줌. |
| [SentenceSplitter](-sentence-splitter/index.md) | `object SentenceSplitter`<br>세종 태그셋에 기반한 Heuristic 문장분리기 |
