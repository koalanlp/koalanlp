[kr.bydelta.koala.data](../index.md) / [Sentence](index.md) / [equals](./equals.md)

# equals

`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

표면형과 더불어, 포함된 어절과 형태소의 배열 순서가 같은지 확인합니다. (reference 기준의 동일함 판단이 아닙니다)

## 참고

* 각 어절의 [id](#)가 다르더라도 형태소와 표면형이 같으면 같다고 판단합니다.
* 세부 동작은 [Word.equals](../-word/equals.md)와 [Morpheme.equals](../-morpheme/equals.md) 참고

**Return**
모두 같으면 true.

