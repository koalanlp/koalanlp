[kr.bydelta.koala.data](../index.md) / [Sentence](./index.md)

# Sentence

`class Sentence : `[`Property`](../-property/index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`

문장을 표현하는 [Property](../-property/index.md) class입니다.

**Since**
1.x

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Sentence(words: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>)`<br>문장을 구성합니다. |

### Inherited Properties

| Name | Summary |
|---|---|
| [properties](../-property/properties.md) | `val properties: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)`, `[`Property`](../-property/index.md)`>`<br>가질 수 있는 속성값을 저장할 [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html) |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | `fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>표면형과 더불어, 포함된 어절과 형태소의 배열 순서가 같은지 확인합니다. (reference 기준의 동일함 판단이 아닙니다) |
| [getDependencyTree](get-dependency-tree.md) | `fun getDependencyTree(): `[`DepTree`](../-dep-tree/index.md)<br>의존구문분석을 했다면, 최상위 의존구문 구조의 값을 돌려줍니다. |
| [getEntities](get-entities.md) | `fun getEntities(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Entity`](../-entity/index.md)`>`<br>개체명 분석을 했다면, 문장의 모든 개체명 목록을 돌려줍니다. |
| [getModifiers](get-modifiers.md) | `fun getModifiers(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`<br>수식언(관형사, 부사) 및 수식언 성격의 어휘를 포함하는 어절들을 가져옵니다. (관형형 전성어미 [POS.ETM](../../kr.bydelta.koala/-p-o-s/-e-t-m.md), 부사 파생 접미사 [POS.XSA](../../kr.bydelta.koala/-p-o-s/-x-s-a.md) 포함) |
| [getNouns](get-nouns.md) | `fun getNouns(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`<br>체언(명사, 수사, 대명사) 및 체언 성격의 어휘를 포함하는 어절들을 가져옵니다. (명사형 전성어미 [POS.ETN](../../kr.bydelta.koala/-p-o-s/-e-t-n.md), 명사 파생 접미사 [POS.XSN](../../kr.bydelta.koala/-p-o-s/-x-s-n.md) 포함) |
| [getSemRoleTree](get-sem-role-tree.md) | `fun getSemRoleTree(): `[`RoleTree`](../-role-tree/index.md)<br>의미역 분석을 했다면, 최상위 의미역 구조를 돌려줌. |
| [getSyntaxTree](get-syntax-tree.md) | `fun getSyntaxTree(): `[`SyntaxTree`](../-syntax-tree/index.md)<br>구문분석을 했다면, 최상위 구구조(Phrase)를 돌려줍니다. |
| [getVerbs](get-verbs.md) | `fun getVerbs(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`<br>용언(동사, 형용사) 및 용언 성격의 어휘를 포함하는 어절들을 가져옵니다. (용언의 활용형: V+E, 동사/형용사 파생 접미사 [POS.XSV](../../kr.bydelta.koala/-p-o-s/-x-s-v.md), [POS.XSM](../../kr.bydelta.koala/-p-o-s/-x-s-m.md) 포함) |
| [hashCode](hash-code.md) | `fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns a hash code value for the object.  The general contract of hashCode is: |
| [singleLineString](single-line-string.md) | `fun singleLineString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>품사분석 결과를, 1행짜리 String으로 변환합니다. |
| [surfaceString](surface-string.md) | `fun surfaceString(delimiter: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = " "): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>어절의 표면형을 이어붙이되, 지정된 [delimiter](surface-string.md#kr.bydelta.koala.data.Sentence$surfaceString(kotlin.String)/delimiter)로 띄어쓰기 된 문장을 반환합니다. |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a string representation of the object. |

### Inherited Functions

| Name | Summary |
|---|---|
| [setListProperty](../-property/set-list-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setListProperty(vararg properties: `[`T`](../-property/set-list-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-list-property.md#T)-type 값들을 가변변수로 하여 [properties](../-property/set-list-property.md#kr.bydelta.koala.data.Property$setListProperty(kotlin.Array((kr.bydelta.koala.data.Property.setListProperty.T)))/properties)로 받은 뒤, 저장합니다. |
| [setProperty](../-property/set-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setProperty(property: `[`T`](../-property/set-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-property.md#T)-type 값 [property](../-property/set-property.md#kr.bydelta.koala.data.Property$setProperty(kr.bydelta.koala.data.Property.setProperty.T)/property)를 저장합니다. |

### Companion Object Properties

| Name | Summary |
|---|---|
| [empty](empty.md) | `val empty: `[`Sentence`](./index.md)<br>빈 문장입니다. |
