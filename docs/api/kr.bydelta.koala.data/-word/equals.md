[kr.bydelta.koala.data](../index.md) / [Word](index.md) / [equals](./equals.md)

# equals

`fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

표면형과 더불어, 포함된 형태소의 배열 순서가 같은지 확인합니다. (reference 기준의 동일함 판단이 아닙니다)

## 참고

* [id](id.md)가 다르더라도 형태소와 표면형이 같으면 같다고 판단합니다.
* 형태소 역시 품사와 표면형으로 판단하며, reference 기준으로 판단하지 않습니다. ([Morpheme.equals](../-morpheme/equals.md) 참고)

**Return**
모두 같으면 true.

