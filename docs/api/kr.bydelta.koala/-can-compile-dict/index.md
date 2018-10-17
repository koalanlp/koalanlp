[kr.bydelta.koala](../index.md) / [CanCompileDict](./index.md)

# CanCompileDict

`abstract class CanCompileDict`

사용자 사전추가 기능을 위한 interface.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `CanCompileDict()`<br>사용자 사전추가 기능을 위한 interface. |

### Functions

| Name | Summary |
|---|---|
| [addUserDictionary](add-user-dictionary.md) | `abstract fun addUserDictionary(vararg dict: `[`DicEntry`](../-dic-entry.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`fun addUserDictionary(morphs: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, tags: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`POS`](../-p-o-s/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가.`fun addUserDictionary(morph: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, tag: `[`POS`](../-p-o-s/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>사용자 사전에, 표면형과 그 품사를 추가. |
| [contains](contains.md) | `fun contains(word: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, posTag: `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`POS`](../-p-o-s/index.md)`> = setOf(POS.NNP, POS.NNG)): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>사전에 등재되어 있는지 확인합니다. |
| [getBaseEntries](get-base-entries.md) | `abstract fun getBaseEntries(filter: (`[`POS`](../-p-o-s/index.md)`) -> `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Iterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)`<`[`DicEntry`](../-dic-entry.md)`>`<br>원본 사전에 등재된 항목 중에서, 지정된 형태소의 항목만을 가져옵니다. (복합 품사 결합 형태는 제외) |
| [getItems](get-items.md) | `abstract fun getItems(): `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`DicEntry`](../-dic-entry.md)`>`<br>사용자 사전에 등재된 모든 Item을 불러옵니다. |
| [getNotExists](get-not-exists.md) | `abstract fun getNotExists(onlySystemDic: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, vararg word: `[`DicEntry`](../-dic-entry.md)`): `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`DicEntry`](../-dic-entry.md)`>`<br>사전에 등재되어 있는지 확인하고, 사전에 없는단어만 반환합니다. |
| [importFrom](import-from.md) | `fun importFrom(dict: `[`CanCompileDict`](./index.md)`, fastAppend: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, filter: (`[`POS`](../-p-o-s/index.md)`) -> `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = { it.isNoun() }): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>다른 사전을 참조하여, 선택된 사전에 없는 단어를 사용자사전으로 추가합니다. |
| [plusAssign](plus-assign.md) | `operator fun plusAssign(entry: `[`DicEntry`](../-dic-entry.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가. |
