

### All Types

| Name | Summary |
|---|---|
| [kr.bydelta.koala.data.AlreadySetIDException](../kr.bydelta.koala.data/-already-set-i-d-exception/index.md) | ID가 이미 설정되었을 때 발생하는 Exception |
| [kr.bydelta.koala.proc.CanAnalyzeProperty](../kr.bydelta.koala.proc/-can-analyze-property/index.md) | [Sentence](../kr.bydelta.koala.data/-sentence/index.md) 객체에 property를 추가할 수 있는 interface |
| [kr.bydelta.koala.proc.CanAnalyzeSentenceProperty](../kr.bydelta.koala.proc/-can-analyze-sentence-property/index.md) | [Sentence](../kr.bydelta.koala.data/-sentence/index.md) 객체에 property를 추가할 수 있는 interface |
| [kr.bydelta.koala.dic.CanCompileDict](../kr.bydelta.koala.dic/-can-compile-dict/index.md) | 사용자 사전추가 기능을 위한 interface. |
| [kr.bydelta.koala.proc.CanParseDependency](../kr.bydelta.koala.proc/-can-dep-parse.md) | 의존구문분석 Interface |
| [kr.bydelta.koala.proc.CanDisambiguateSense](../kr.bydelta.koala.proc/-can-disambiguate-sense/index.md) | 다의어 분별 (Word sense disambiguation) Interface |
| [kr.bydelta.koala.dic.CanExtractResource](../kr.bydelta.koala.dic/-can-extract-resource/index.md) | Jar Resource에 포함된 모형을 임시디렉터리에 압축해제하기 위한 interface |
| [kr.bydelta.koala.proc.CanLabelSemanticRole](../kr.bydelta.koala.proc/-can-label-semantic-role.md) | 의미역 분석(Semantic Role Labeling) Interface |
| [kr.bydelta.koala.proc.CanRecognizeEntity](../kr.bydelta.koala.proc/-can-recognize-entity.md) | 개체명 인식 (Named Entity Recognition) Interface |
| [kr.bydelta.koala.proc.CanSplitSentence](../kr.bydelta.koala.proc/-can-split-sentence/index.md) | 문장분리기 Interface |
| [kr.bydelta.koala.proc.CanParseSyntax](../kr.bydelta.koala.proc/-can-syntax-parse.md) | 구문분석 Interface |
| [kr.bydelta.koala.proc.CanTag](../kr.bydelta.koala.proc/-can-tag/index.md) | 품사분석기 interface |
| [kr.bydelta.koala.proc.CanTagAParagraph](../kr.bydelta.koala.proc/-can-tag-a-paragraph/index.md) | 문단1개, 문장1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S](../kr.bydelta.koala.proc/-can-tag-a-paragraph/index.md#S) 타입으로 돌려줌. |
| [kr.bydelta.koala.proc.CanTagASentence](../kr.bydelta.koala.proc/-can-tag-a-sentence/index.md) | 문장 1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S](../kr.bydelta.koala.proc/-can-tag-a-sentence/index.md#S) 타입으로 돌려줌. |
| [kr.bydelta.koala.proc.CanTagOnlyAParagraph](../kr.bydelta.koala.proc/-can-tag-only-a-paragraph/index.md) | 문장1개는 불가하지만, 문단1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S](../kr.bydelta.koala.proc/-can-tag-only-a-paragraph/index.md#S) 타입으로 돌려줌. |
| [kr.bydelta.koala.proc.CanTagOnlyASentence](../kr.bydelta.koala.proc/-can-tag-only-a-sentence/index.md) | 문단1개는 불가하지만, 문장1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S](../kr.bydelta.koala.proc/-can-tag-only-a-sentence/index.md#S) 타입으로 돌려줌. |
| [kotlin.Char](../kr.bydelta.koala.ext/kotlin.-char/index.md) (extensions in package kr.bydelta.koala.ext) |  |
| [kotlin.CharSequence](../kr.bydelta.koala.ext/kotlin.-char-sequence/index.md) (extensions in package kr.bydelta.koala.ext) |  |
| [kotlin.CharSequence](../kr.bydelta.koala/kotlin.-char-sequence/index.md) (extensions in package kr.bydelta.koala) |  |
| [kr.bydelta.koala.CoarseEntityType](../kr.bydelta.koala/-coarse-entity-type/index.md) | 대분류 개체명(Named Entity) 유형을 담은 Enum class입니다. (ETRI 표준안) |
| [kr.bydelta.koala.data.DepTree](../kr.bydelta.koala.data/-dep-tree/index.md) | 의존구문구조 분석의 결과. |
| [kr.bydelta.koala.DependencyTag](../kr.bydelta.koala/-dependency-tag/index.md) | 의존구문구조 기능표지자를 담은 Enum class입니다. (ETRI 표준안) |
| [kr.bydelta.koala.dic.DicEntry](../kr.bydelta.koala.dic/-dic-entry.md) | Dictionary Entry 타입: 표면형을 나타내는 [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) 값과, 품사태그를 나타내는 [POS](../kr.bydelta.koala/-p-o-s/index.md)값으로 구성. |
| [kr.bydelta.koala.data.Entity](../kr.bydelta.koala.data/-entity/index.md) | 개체명 분석 결과를 저장할 [Property](../kr.bydelta.koala.data/-property/index.md) class |
| [kr.bydelta.koala.data.IntProperty](../kr.bydelta.koala.data/-int-property/index.md) | 정수형 값을 저장할 [Property](../kr.bydelta.koala.data/-property/index.md) class. |
| [kotlin.collections.Iterable](../kr.bydelta.koala/kotlin.collections.-iterable/index.md) (extensions in package kr.bydelta.koala) |  |
| [kr.bydelta.koala.data.ListProperty](../kr.bydelta.koala.data/-list-property/index.md) | 목록형 값을 저장할 [Property](../kr.bydelta.koala.data/-property/index.md). |
| [kr.bydelta.koala.data.Morpheme](../kr.bydelta.koala.data/-morpheme/index.md) | 형태소를 저장하는 [Property](../kr.bydelta.koala.data/-property/index.md) class입니다. |
| [kr.bydelta.koala.POS](../kr.bydelta.koala/-p-o-s/index.md) | 세종 품사표기 표준안을 Enum Class로 담았습니다. |
| [kr.bydelta.koala.PhraseTag](../kr.bydelta.koala/-phrase-tag/index.md) | 세종 구문구조 표지자를 Enum class로 담았습니다. |
| [kr.bydelta.koala.data.Property](../kr.bydelta.koala.data/-property/index.md) | 텍스트 분석 과정에서 얻어지는 여러가지 값들을 표현하는 class. |
| [kr.bydelta.koala.data.RoleTree](../kr.bydelta.koala.data/-role-tree/index.md) | 의미역 구조 분석의 결과. |
| [kr.bydelta.koala.RoleType](../kr.bydelta.koala/-role-type/index.md) | 의미역(Semantic Role) 분석 표지를 담은 Enum class입니다. (ETRI 표준안) |
| [kr.bydelta.koala.data.Sentence](../kr.bydelta.koala.data/-sentence/index.md) | 문장을 표현하는 [Property](../kr.bydelta.koala.data/-property/index.md) class입니다. |
| [kr.bydelta.koala.proc.SentenceSplitter](../kr.bydelta.koala.proc/-sentence-splitter/index.md) | 세종 태그셋에 기반한 Heuristic 문장분리기 |
| [kr.bydelta.koala.data.SyntaxTree](../kr.bydelta.koala.data/-syntax-tree/index.md) | 구문구조 분석의 결과를 저장할 [Property](../kr.bydelta.koala.data/-property/index.md). |
| [kr.bydelta.koala.data.Tree](../kr.bydelta.koala.data/-tree/index.md) | [T](../kr.bydelta.koala.data/-tree/index.md#T)-type들의 트리 또는 DAG 구조를 저장할 [Property](../kr.bydelta.koala.data/-property/index.md) |
| [kotlin.Triple](../kr.bydelta.koala.ext/kotlin.-triple/index.md) (extensions in package kr.bydelta.koala.ext) |  |
| [kr.bydelta.koala.data.Word](../kr.bydelta.koala.data/-word/index.md) | 어절을 표현하는 [Property](../kr.bydelta.koala.data/-property/index.md) class입니다. |
