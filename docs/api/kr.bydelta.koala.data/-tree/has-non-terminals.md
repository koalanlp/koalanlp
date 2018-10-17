[kr.bydelta.koala.data](../index.md) / [Tree](index.md) / [hasNonTerminals](./has-non-terminals.md)

# hasNonTerminals

`fun hasNonTerminals(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

이 노드가 (leaf node를 제외하고) 자식 노드를 갖는지 확인합니다.

* 구문분석 구조에서 leaf node는 [Word](../-word/index.md)가 됩니다.
* SRL/의존구문분석 구조에서는 node에 대응하는 [Word](../-word/index.md)를 terminal node (leaf)로 별도 저장하고, 노드 사이의 DAG를 구성합니다.

**Since**
2.0.0

**Return**
자식노드가 있다면 true.

