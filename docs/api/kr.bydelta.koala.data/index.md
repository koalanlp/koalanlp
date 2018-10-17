[kr.bydelta.koala.data](./index.md)

## Package kr.bydelta.koala.data

### Types

| Name | Summary |
|---|---|
| [DepTree](-dep-tree/index.md) | `class DepTree : `[`Tree`](-tree/index.md)`<`[`DepTree`](-dep-tree/index.md)`>`<br>의존구문구조 분석의 결과. |
| [Entity](-entity/index.md) | `class Entity : `[`Property`](-property/index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](-word/index.md)`>`<br>개체명 분석 결과를 저장할 [Property](-property/index.md) class |
| [IntProperty](-int-property/index.md) | `data class IntProperty : `[`Property`](-property/index.md)<br>정수형 값을 저장할 [Property](-property/index.md) class. |
| [ListProperty](-list-property/index.md) | `class ListProperty<T : `[`Property`](-property/index.md)`> : `[`Property`](-property/index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](-list-property/index.md#T)`>`<br>목록형 값을 저장할 [Property](-property/index.md). |
| [Morpheme](-morpheme/index.md) | `class Morpheme : `[`Property`](-property/index.md)<br>형태소를 저장하는 [Property](-property/index.md) class입니다. |
| [Property](-property/index.md) | `abstract class Property : `[`Serializable`](http://docs.oracle.com/javase/6/docs/api/java/io/Serializable.html)<br>텍스트 분석 과정에서 얻어지는 여러가지 값들을 표현하는 class. |
| [RoleTree](-role-tree/index.md) | `class RoleTree : `[`Tree`](-tree/index.md)`<`[`RoleTree`](-role-tree/index.md)`>`<br>의미역 구조 분석의 결과. |
| [Sentence](-sentence/index.md) | `class Sentence : `[`Property`](-property/index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](-word/index.md)`>`<br>문장을 표현하는 [Property](-property/index.md) class입니다. |
| [SyntaxTree](-syntax-tree/index.md) | `class SyntaxTree : `[`Tree`](-tree/index.md)`<`[`SyntaxTree`](-syntax-tree/index.md)`>`<br>구문구조 분석의 결과를 저장할 [Property](-property/index.md). |
| [Tree](-tree/index.md) | `open class Tree<T : `[`Tree`](-tree/index.md)`<`[`T`](-tree/index.md#T)`>> : `[`Property`](-property/index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](-tree/index.md#T)`>`<br>[T](-tree/index.md#T)-type들의 트리 또는 DAG 구조를 저장할 [Property](-property/index.md) |
| [Word](-word/index.md) | `class Word : `[`Property`](-property/index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Morpheme`](-morpheme/index.md)`>`<br>어절을 표현하는 [Property](-property/index.md) class입니다. |

### Exceptions

| Name | Summary |
|---|---|
| [AlreadySetIDException](-already-set-i-d-exception/index.md) | `class AlreadySetIDException : `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)<br>ID가 이미 설정되었을 때 발생하는 Exception |
