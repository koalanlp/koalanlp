[kr.bydelta.koala.dic](../index.md) / [CanCompileDict](index.md) / [importFrom](./import-from.md)

# importFrom

`@JvmOverloads fun importFrom(dict: `[`CanCompileDict`](index.md)`, fastAppend: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, filter: (`[`POS`](../../kr.bydelta.koala/-p-o-s/index.md)`) -> `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = { it.isNoun() }): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

다른 사전을 참조하여, 선택된 사전에 없는 단어를 사용자사전으로 추가합니다.

### Parameters

`dict` - 참조할 사전

`fastAppend` - 선택된 사전에 존재하는지를 검사하지 않고, 빠르게 추가하고자 할 때 (기본값 false)

`filter` - 추가할 품사를 지정하는 함수. (기본값 isNoun)