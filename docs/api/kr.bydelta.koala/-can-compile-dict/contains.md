[kr.bydelta.koala](../index.md) / [CanCompileDict](index.md) / [contains](./contains.md)

# contains

`@JvmOverloads fun contains(word: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, posTag: `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`POS`](../-p-o-s/index.md)`> = setOf(POS.NNP, POS.NNG)): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

사전에 등재되어 있는지 확인합니다.

### Parameters

`word` - 확인할 형태소

`posTag` - 품사들(기본값: NNP 고유명사, NNG 일반명사)