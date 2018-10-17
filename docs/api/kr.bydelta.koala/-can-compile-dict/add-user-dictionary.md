[kr.bydelta.koala](../index.md) / [CanCompileDict](index.md) / [addUserDictionary](./add-user-dictionary.md)

# addUserDictionary

`abstract fun addUserDictionary(vararg dict: `[`DicEntry`](../-dic-entry.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가.

### Parameters

`dict` - 추가할 (표면형,품사)의 순서쌍.`fun addUserDictionary(morph: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, tag: `[`POS`](../-p-o-s/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

사용자 사전에, 표면형과 그 품사를 추가.

### Parameters

`morph` - 표면형.

`tag` - 품사.`fun addUserDictionary(morphs: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, tags: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`POS`](../-p-o-s/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가.

### Parameters

`morphs` - 추가할 단어의 표면형의 목록.

`tags` - 추가할 단어의 품사의 목록.