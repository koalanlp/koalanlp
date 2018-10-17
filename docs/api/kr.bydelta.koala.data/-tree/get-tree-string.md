[kr.bydelta.koala.data](../index.md) / [Tree](index.md) / [getTreeString](./get-tree-string.md)

# getTreeString

`@JvmOverloads fun getTreeString(depth: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0, buffer: `[`StringBuffer`](http://docs.oracle.com/javase/6/docs/api/java/lang/StringBuffer.html)` = StringBuffer()): `[`StringBuffer`](http://docs.oracle.com/javase/6/docs/api/java/lang/StringBuffer.html)

이 트리구조를 표현하는 텍스트 표현을 [buffer](get-tree-string.md#kr.bydelta.koala.data.Tree$getTreeString(kotlin.Int, java.lang.StringBuffer)/buffer)에 담아 반환합니다.

### Parameters

`depth` - 들여쓰기할 수준입니다. 숫자만큼 들여쓰기됩니다. (기본값 0)

`buffer` - 결과를 저장할 버퍼입니다.

**Since**
2.0.0

**Return**
트리구조의 표현을 담아 [buffer](get-tree-string.md#kr.bydelta.koala.data.Tree$getTreeString(kotlin.Int, java.lang.StringBuffer)/buffer)에 저장한 후 돌려줍니다.

