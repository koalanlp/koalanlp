[kr.bydelta.koala](../index.md) / [CanCompileDict](index.md) / [getNotExists](./get-not-exists.md)

# getNotExists

`abstract fun getNotExists(onlySystemDic: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, vararg word: `[`DicEntry`](../-dic-entry.md)`): `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`DicEntry`](../-dic-entry.md)`>`

사전에 등재되어 있는지 확인하고, 사전에 없는단어만 반환합니다.

### Parameters

`onlySystemDic` - 시스템 사전에서만 검색할지 결정합니다.

`word` - 확인할 (형태소, 품사)들.

**Return**
사전에 없는 단어들.

