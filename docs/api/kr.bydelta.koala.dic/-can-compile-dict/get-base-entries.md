[kr.bydelta.koala.dic](../index.md) / [CanCompileDict](index.md) / [getBaseEntries](./get-base-entries.md)

# getBaseEntries

`abstract fun getBaseEntries(filter: (`[`POS`](../../kr.bydelta.koala/-p-o-s/index.md)`) -> `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Iterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)`<`[`DicEntry`](../-dic-entry.md)`>`

원본 사전에 등재된 항목 중에서, 지정된 형태소의 항목만을 가져옵니다. (복합 품사 결합 형태는 제외)

### Parameters

`filter` - 가져올 품사인지 판단하는 함수.

**Return**
(형태소, 품사)의 Iterator.

