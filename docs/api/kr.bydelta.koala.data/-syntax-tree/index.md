[kr.bydelta.koala.data](../index.md) / [SyntaxTree](./index.md)

# SyntaxTree

`class SyntaxTree : `[`Tree`](../-tree/index.md)`<`[`SyntaxTree`](./index.md)`>`

구문구조 분석의 결과를 저장할 [Property](../-property/index.md).

## 참고

**구문구조 분석**은 문장의 구성요소들(어절, 구, 절)이 이루는 문법적 구조를 분석하는 방법입니다.
예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
2개의 절이 있습니다

* 나는 밥을 먹었고
* 영희는 짐을 쌌다
각 절은 3개의 구를 포함합니다
* 나는, 밥을, 영희는, 짐을: 체언구
* 먹었고, 쌌다: 용언구

아래를 참고해보세요.

* [kr.bydelta.koala.proc.CanParseSyntax](../../kr.bydelta.koala.proc/-can-syntax-parse.md) 구문구조 분석을 수행하는 interface.
* [Word.getPhrase](../-word/get-phrase.md) 어절이 직접 속하는 가장 작은 구구조 [SyntaxTree](./index.md)를 가져오는 API
* [Sentence.getSyntaxTree](../-sentence/get-syntax-tree.md) 전체 문장을 분석한 [SyntaxTree](./index.md)를 가져오는 API
* [PhraseTag](../../kr.bydelta.koala/-phrase-tag/index.md) 구구조의 형태 분류를 갖는 Enum 값

**Since**
2.0.0

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SyntaxTree(type: `[`PhraseTag`](../../kr.bydelta.koala/-phrase-tag/index.md)`, node: `[`Word`](../-word/index.md)`?, vararg children: `[`SyntaxTree`](./index.md)`)`<br>`SyntaxTree(type: `[`PhraseTag`](../../kr.bydelta.koala/-phrase-tag/index.md)`, node: `[`Word`](../-word/index.md)`? = null, children: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SyntaxTree`](./index.md)`> = emptyList())`<br>구문구조 분석의 결과를 생성합니다. |

### Properties

| Name | Summary |
|---|---|
| [type](type.md) | `val type: `[`PhraseTag`](../../kr.bydelta.koala/-phrase-tag/index.md)<br>구구조 표지자입니다. [PhraseTag](../../kr.bydelta.koala/-phrase-tag/index.md) Enum 값입니다. |

### Inherited Properties

| Name | Summary |
|---|---|
| [children](../-tree/children.md) | `val children: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](../-tree/index.md#T)`>`<br>트리/DAG의 자식 노드들 |
| [leaf](../-tree/leaf.md) | `val leaf: `[`Word`](../-word/index.md)`?`<br>트리/DAG의 노드에서 연결되는 [Word](../-word/index.md) |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | `fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Indicates whether some other object is "equal to" this one. |
| [hashCode](hash-code.md) | `fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns a hash code value for the object.  The general contract of hashCode is: |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a string representation of the object. |

### Inherited Functions

| Name | Summary |
|---|---|
| [getParent](../-tree/get-parent.md) | `fun getParent(): `[`T`](../-tree/index.md#T)`?`<br>부모 노드를 반환합니다. |
| [getTerminals](../-tree/get-terminals.md) | `fun getTerminals(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`<br>이 노드의 자식노드들에 속하는 leaf/terminal node들을 모읍니다. |
| [getTreeString](../-tree/get-tree-string.md) | `fun getTreeString(depth: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0, buffer: `[`StringBuffer`](http://docs.oracle.com/javase/6/docs/api/java/lang/StringBuffer.html)` = StringBuffer()): `[`StringBuffer`](http://docs.oracle.com/javase/6/docs/api/java/lang/StringBuffer.html)<br>이 트리구조를 표현하는 텍스트 표현을 [buffer](../-tree/get-tree-string.md#kr.bydelta.koala.data.Tree$getTreeString(kotlin.Int, java.lang.StringBuffer)/buffer)에 담아 반환합니다. |
| [hasNonTerminals](../-tree/has-non-terminals.md) | `fun hasNonTerminals(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>이 노드가 (leaf node를 제외하고) 자식 노드를 갖는지 확인합니다. |
| [isRoot](../-tree/is-root.md) | `fun isRoot(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>이 노드가 최상위 노드인지 확인합니다. |
