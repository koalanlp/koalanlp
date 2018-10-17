[kr.bydelta.koala.ext](./index.md)

## Package kr.bydelta.koala.ext

### Extensions for External Classes

| Name | Summary |
|---|---|
| [kotlin.Char](kotlin.-char/index.md) |  |
| [kotlin.CharSequence](kotlin.-char-sequence/index.md) |  |
| [kotlin.Triple](kotlin.-triple/index.md) |  |

### Properties

| Name | Summary |
|---|---|
| [ALPHABET_READING](-a-l-p-h-a-b-e-t_-r-e-a-d-i-n-g.md) | `val ALPHABET_READING: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>`<br>알파벳의 독음방법 |
| [ChoToJong](-cho-to-jong.md) | `val ChoToJong: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`, `[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`>`<br>초성 문자를 종성 조합형 문자로 변경 |
| [HANGUL_END](-h-a-n-g-u-l_-e-n-d.md) | `const val HANGUL_END: `[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)<br>'힣'(hih) 위치 |
| [HANGUL_START](-h-a-n-g-u-l_-s-t-a-r-t.md) | `const val HANGUL_START: `[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)<br>'가'(ga) 위치 |
| [HanFirstList](-han-first-list.md) | `val HanFirstList: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`>`<br>초성 조합형 문자열 리스트 (UNICODE 순서) |
| [HanLastList](-han-last-list.md) | `val HanLastList: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`?>`<br>종성 조합형 문자열 리스트 (UNICODE 순서). 가장 첫번째는 null (받침 없음) |
| [HanSecondList](-han-second-list.md) | `val HanSecondList: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`>`<br>중성 조합형 문자열 리스트 (UNICODE 순서) |
| [JONGSUNG_RANGE](-j-o-n-g-s-u-n-g_-r-a-n-g-e.md) | `const val JONGSUNG_RANGE: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>종성 범위의 폭 |
| [JUNGSUNG_RANGE](-j-u-n-g-s-u-n-g_-r-a-n-g-e.md) | `const val JUNGSUNG_RANGE: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>중성 범위의 폭 |

### Functions

| Name | Summary |
|---|---|
| [assembleHangul](assemble-hangul.md) | `fun assembleHangul(cho: `[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`, jung: `[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`, jong: `[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)`? = null): `[`Char`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)<br>초성을 [cho](assemble-hangul.md#kr.bydelta.koala.ext$assembleHangul(kotlin.Char, kotlin.Char, kotlin.Char)/cho) 문자로, 중성을 [jung](assemble-hangul.md#kr.bydelta.koala.ext$assembleHangul(kotlin.Char, kotlin.Char, kotlin.Char)/jung) 문자로, 종성을 [jong](assemble-hangul.md#kr.bydelta.koala.ext$assembleHangul(kotlin.Char, kotlin.Char, kotlin.Char)/jong) 문자로 갖는 한글 문자를 재구성합니다. |
| [correctVerbApply](correct-verb-apply.md) | `fun correctVerbApply(verb: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, isVerb: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, rest: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>주어진 용언의 원형 [verb](correct-verb-apply.md#kr.bydelta.koala.ext$correctVerbApply(kotlin.String, kotlin.Boolean, kotlin.String)/verb)이 뒷 부분 [rest](correct-verb-apply.md#kr.bydelta.koala.ext$correctVerbApply(kotlin.String, kotlin.Boolean, kotlin.String)/rest)와 같이 어미가 붙어 활용될 때, 불규칙 활용 용언과 모음조화를 교정함. |
