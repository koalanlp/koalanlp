[kr.bydelta.koala](../index.md) / [DepTree](./index.md)

# DepTree

`class DepTree : `[`Tree`](../-tree/index.md)`<`[`DepTree`](./index.md)`>`

의존구문구조 분석의 결과.

### Parameters

`head` - 현재 의존구조의 지배소

`type` - 구구조 표지자 (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함)

`depType` - 의존기능 표지자 (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함)

`children` - 지배를 받는 다른 의존구조

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DepTree(head: `[`Word`](../-word/index.md)`, type: `[`PhraseTag`](../-phrase-tag/index.md)`, depType: `[`DependencyTag`](../-dependency-tag/index.md)`?, vararg children: `[`DepTree`](./index.md)`)`<br>`DepTree(head: `[`Word`](../-word/index.md)`, type: `[`PhraseTag`](../-phrase-tag/index.md)`, depType: `[`DependencyTag`](../-dependency-tag/index.md)`? = null, children: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`DepTree`](./index.md)`> = emptyList())`<br>의존구문구조 분석의 결과. |

### Properties

| Name | Summary |
|---|---|
| [depType](dep-type.md) | `val depType: `[`DependencyTag`](../-dependency-tag/index.md)`?`<br>의존기능 표지자 (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함) |
| [head](head.md) | `val head: `[`Word`](../-word/index.md)<br>현재 의존구조의 지배소 |
| [type](type.md) | `val type: `[`PhraseTag`](../-phrase-tag/index.md)<br>구구조 표지자 (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함) |

### Inherited Properties

| Name | Summary |
|---|---|
| [children](../-tree/children.md) | `val children: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](../-tree/index.md#T)`>`<br>트리/DAG의 자식 노드들 |
| [leaf](../-tree/leaf.md) | `val leaf: `[`Word`](../-word/index.md)`?`<br>트리/DAG의 노드에서 연결되는 word |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | `fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Indicates whether some other object is "equal to" this one. |
| [hashCode](hash-code.md) | `fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns a hash code value for the object.  The general contract of hashCode is: |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a string representation of the object. |

### Inherited Functions

| Name | Summary |
|---|---|
| [getParent](../-tree/get-parent.md) | `fun getParent(): `[`T`](../-tree/index.md#T)`?`<br>부모 노드를 반환함. 부모 노드가 초기화되지 않은 경우 null. |
| [getTerminals](../-tree/get-terminals.md) | `fun getTerminals(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`<br>이 노드의 자식노드들에 속하는 leaf node들을 모음. |
| [getTreeString](../-tree/get-tree-string.md) | `fun getTreeString(depth: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0, buffer: `[`StringBuffer`](http://docs.oracle.com/javase/6/docs/api/java/lang/StringBuffer.html)` = StringBuffer()): `[`StringBuffer`](http://docs.oracle.com/javase/6/docs/api/java/lang/StringBuffer.html)<br>이 트리구조를 표현하는 텍스트 표현을 [buffer](../-tree/get-tree-string.md#kr.bydelta.koala.Tree$getTreeString(kotlin.Int, java.lang.StringBuffer)/buffer)에 담아 반환함. [depth](../-tree/get-tree-string.md#kr.bydelta.koala.Tree$getTreeString(kotlin.Int, java.lang.StringBuffer)/depth)는 들여쓰기의 수준임. |
| [hasNonTerminals](../-tree/has-non-terminals.md) | `fun hasNonTerminals(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>이 노드가 (leaf node를 제외하고) 자식 노드를 갖는지 확인. |
| [isRoot](../-tree/is-root.md) | `fun isRoot(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>이 노드가 최상위 노드인지 확인함. |
