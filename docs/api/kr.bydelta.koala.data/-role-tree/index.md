[kr.bydelta.koala.data](../index.md) / [RoleTree](./index.md)

# RoleTree

`class RoleTree : `[`Tree`](../-tree/index.md)`<`[`RoleTree`](./index.md)`>`

의미역 구조 분석의 결과.

## 참고

**의미역 결정**은 문장의 구성 어절들의 역할/기능을 분석하는 방법입니다.
예) '나는 밥을 어제 집에서 먹었다'라는 문장에는
동사 '먹었다'를 중심으로

* '나는'은 동작의 주체를,
* '밥을'은 동작의 대상을,
* '어제'는 동작의 시점을
* '집에서'는 동작의 장소를 나타냅니다.

아래를 참고해보세요.

* [kr.bydelta.koala.proc.CanLabelSemanticRole](../../kr.bydelta.koala.proc/-can-label-semantic-role.md) 의미역 분석을 수행하는 interface.
* [Word.getRole](../-word/get-role.md) 어절이 직접 지배하는 하위 의미역 구조 [RoleTree](./index.md)를 가져오는 API
* [Sentence.getSemRoleTree](../-sentence/get-sem-role-tree.md) 전체 문장을 분석한 의미역 구조 [RoleTree](./index.md)를 가져오는 API
* [RoleType](../../kr.bydelta.koala/-role-type/index.md) 의미역 분류를 갖는 Enum 값

**Since**
2.0.0

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RoleTree(node: `[`Word`](../-word/index.md)`, type: `[`RoleType`](../../kr.bydelta.koala/-role-type/index.md)`, vararg children: `[`RoleTree`](./index.md)`)`<br>`RoleTree(node: `[`Word`](../-word/index.md)`, type: `[`RoleType`](../../kr.bydelta.koala/-role-type/index.md)`, children: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`RoleTree`](./index.md)`> = emptyList())`<br>의미역 분석 결과를 생성합니다. |

### Properties

| Name | Summary |
|---|---|
| [type](type.md) | `val type: `[`RoleType`](../../kr.bydelta.koala/-role-type/index.md)<br>의미역 표지자, [RoleType](../../kr.bydelta.koala/-role-type/index.md) Enum 값 |

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
