[kr.bydelta.koala](../index.md) / [RoleTree](./index.md)

# RoleTree

`class RoleTree : `[`Tree`](../-tree/index.md)`<`[`RoleTree`](./index.md)`>`

의미역 구조 분석의 결과.

### Parameters

`node` - 의미역 구조를 표현하는 지배소

`type` - 의미역 표지자

`children` - 지배를 받는 다른 의미역 구조

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RoleTree(node: `[`Word`](../-word/index.md)`, type: `[`RoleType`](../-role-type/index.md)`, vararg children: `[`RoleTree`](./index.md)`)`<br>`RoleTree(node: `[`Word`](../-word/index.md)`, type: `[`RoleType`](../-role-type/index.md)`, children: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`RoleTree`](./index.md)`> = emptyList())`<br>의미역 구조 분석의 결과. |

### Properties

| Name | Summary |
|---|---|
| [type](type.md) | `val type: `[`RoleType`](../-role-type/index.md)<br>의미역 표지자 |

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
