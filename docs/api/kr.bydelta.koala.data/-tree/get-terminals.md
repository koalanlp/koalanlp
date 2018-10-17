[kr.bydelta.koala.data](../index.md) / [Tree](index.md) / [getTerminals](./get-terminals.md)

# getTerminals

`fun getTerminals(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`

이 노드의 자식노드들에 속하는 leaf/terminal node들을 모읍니다.

* 구문분석 구조에서는 이 구문구조에 속하는 어절의 모음입니다.
* SRL/의존구문분석 구조에서는 어절과 그 어절이 지배하는 하위 피지배소의 포함한 목록입니다.

**Since**
2.0.0

**Return**
Terminal node의 목록

