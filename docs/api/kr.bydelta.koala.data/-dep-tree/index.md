[kr.bydelta.koala.data](../index.md) / [DepTree](./index.md)

# DepTree

`class DepTree : `[`Tree`](../-tree/index.md)`<`[`DepTree`](./index.md)`>`

의존구문구조 분석의 결과.

## 참고

**의존구조 분석**은 문장의 구성 어절들이 의존 또는 기능하는 관계를 분석하는 방법입니다.
예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
가장 마지막 단어인 '쌌다'가 핵심 어구가 되며,

* '먹었고'가 '쌌다'와 대등하게 연결되고
* '나는'은 '먹었고'의 주어로 기능하며
* '밥을'은 '먹었고'의 목적어로 기능합니다.
* '영희는'은 '쌌다'의 주어로 기능하고,
* '짐을'은 '쌌다'의 목적어로 기능합니다.

아래를 참고해보세요.

* [kr.bydelta.koala.proc.CanDepParse](../../kr.bydelta.koala.proc/-can-dep-parse.md) 의존구조 분석을 수행하는 interface.
* [Word.getDependency](../-word/get-dependency.md) 어절이 직접 지배하는 하위 의존구조 [DepTree](./index.md)를 가져오는 API
* [Sentence.getDependencyTree](../-sentence/get-dependency-tree.md) 전체 문장을 분석한 의존구조 [DepTree](./index.md)를 가져오는 API
* [PhraseTag](../../kr.bydelta.koala/-phrase-tag/index.md) 의존구조의 형태 분류를 갖는 Enum 값 (구구조 분류와 같음)
* [DependencyTag](../../kr.bydelta.koala/-dependency-tag/index.md) 의존구조의 기능 분류를 갖는 Enum 값

**Since**
2.0.0

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DepTree(head: `[`Word`](../-word/index.md)`, type: `[`PhraseTag`](../../kr.bydelta.koala/-phrase-tag/index.md)`, depType: `[`DependencyTag`](../../kr.bydelta.koala/-dependency-tag/index.md)`?, vararg children: `[`DepTree`](./index.md)`)`<br>의존구문구조 분석의 결과를 생성합니다.`DepTree(head: `[`Word`](../-word/index.md)`, type: `[`PhraseTag`](../../kr.bydelta.koala/-phrase-tag/index.md)`, depType: `[`DependencyTag`](../../kr.bydelta.koala/-dependency-tag/index.md)`? = null, children: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`DepTree`](./index.md)`> = emptyList())`<br>의존구문구조 분석 결과를 생성합니다. |

### Properties

| Name | Summary |
|---|---|
| [depType](dep-type.md) | `val depType: `[`DependencyTag`](../../kr.bydelta.koala/-dependency-tag/index.md)`?`<br>의존기능 표지자, [DependencyTag](../../kr.bydelta.koala/-dependency-tag/index.md) Enum 값. 별도의 기능이 지정되지 않으면 null. (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함) |
| [head](head.md) | `val head: `[`Word`](../-word/index.md)<br>현재 의존구조의 지배소 [Word](../-word/index.md) |
| [type](type.md) | `val type: `[`PhraseTag`](../../kr.bydelta.koala/-phrase-tag/index.md)<br>구구조 표지자, [PhraseTag](../../kr.bydelta.koala/-phrase-tag/index.md) Enum 값 (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함) |

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
