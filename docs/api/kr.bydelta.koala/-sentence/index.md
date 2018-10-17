[kr.bydelta.koala](../index.md) / [Sentence](./index.md)

# Sentence

`class Sentence : `[`Property`](../-property/index.md)`, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`

문장 Class

### Parameters

`words` - 문장에 포함되는 어절의 목록

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Sentence(words: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>)`<br>문장 Class |

### Inherited Properties

| Name | Summary |
|---|---|
| [properties](../-property/properties.md) | `val properties: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)`, `[`Property`](../-property/index.md)`>`<br>가질 수 있는 속성값을 저장할 [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html) |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | `fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Indicates whether some other object is "equal to" this one (deep equal). |
| [getDependencyTree](get-dependency-tree.md) | `fun getDependencyTree(): `[`DepTree`](../-dep-tree/index.md)<br>의존구문분석을 했다면, 문장의 최상위 지배소의 의존구문결과를 돌려줌. 분석을 하지 않은 경우 [UninitializedPropertyAccessException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-uninitialized-property-access-exception/index.html) 발생 |
| [getModifiers](get-modifiers.md) | `fun getModifiers(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`<br>수식언(관형사, 부사) 및 수식언 성격의 어휘를 포함하는 어절들 (관형형 전성어미 [POS.ETM](../-p-o-s/-e-t-m.md), 부사 파생 접미사 [POS.XSA](../-p-o-s/-x-s-a.md) 포함) |
| [getNamedEntities](get-named-entities.md) | `fun getNamedEntities(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Entity`](../-entity/index.md)`>`<br>개체명 분석을 했다면, 문장의 개체명 목록을 돌려줌. 분석을 하지 않은 경우 [UninitializedPropertyAccessException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-uninitialized-property-access-exception/index.html) 발생 |
| [getNouns](get-nouns.md) | `fun getNouns(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`<br>체언(명사, 수사, 대명사) 및 체언 성격의 어휘를 포함하는 어절들 (명사형 전성어미 [POS.ETN](../-p-o-s/-e-t-n.md), 명사 파생 접미사 [POS.XSN](../-p-o-s/-x-s-n.md) 포함) |
| [getSemRoleTree](get-sem-role-tree.md) | `fun getSemRoleTree(): `[`RoleTree`](../-role-tree/index.md)<br>의미역 분석을 했다면, 문장의 최상위 의미역 구조를 돌려줌. 분석을 하지 않은 경우 [UninitializedPropertyAccessException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-uninitialized-property-access-exception/index.html) 발생 |
| [getSyntaxTree](get-syntax-tree.md) | `fun getSyntaxTree(): `[`SyntaxTree`](../-syntax-tree/index.md)<br>구문분석을 했다면, 문장의 최상위 구구조를 돌려줌. 분석을 하지 않은 경우 [UninitializedPropertyAccessException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-uninitialized-property-access-exception/index.html) 발생 |
| [getVerbs](get-verbs.md) | `fun getVerbs(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>`<br>용언(동사, 형용사) 및 용언 성격의 어휘를 포함하는 어절들 (용언의 활용형: V+E, 동사/형용사 파생 접미사 [POS.XSV](../-p-o-s/-x-s-v.md), [POS.XSM](../-p-o-s/-x-s-m.md) 포함) |
| [hashCode](hash-code.md) | `fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns a hash code value for the object.  The general contract of hashCode is: |
| [singleLineString](single-line-string.md) | `fun singleLineString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>품사분석 결과를, 1행짜리 String으로 변환. |
| [surfaceString](surface-string.md) | `fun surfaceString(delimiter: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = " "): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>띄어쓰기 된 문장을 반환. |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a string representation of the object. |

### Inherited Functions

| Name | Summary |
|---|---|
| [getProperty](../-property/get-property.md) | `fun <T : `[`Property`](../-property/index.md)`> getProperty(key: `[`Byte`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)`): `[`T`](../-property/get-property.md#T)`?`<br>[key](../-property/get-property.md#kr.bydelta.koala.Property$getProperty(kotlin.Byte)/key)로 지정된 [T](../-property/get-property.md#T)-type의 property를 가져옵니다. |
| [setListProperty](../-property/set-list-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setListProperty(vararg properties: `[`T`](../-property/set-list-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-list-property.md#T)-type 값들을 가변변수로 하여 [properties](../-property/set-list-property.md#kr.bydelta.koala.Property$setListProperty(kotlin.Array((kr.bydelta.koala.Property.setListProperty.T)))/properties)로 받은 뒤, 저장합니다. |
| [setProperty](../-property/set-property.md) | `fun <T : `[`Property`](../-property/index.md)`> setProperty(property: `[`T`](../-property/set-property.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>[T](../-property/set-property.md#T)-type 값 [property](../-property/set-property.md#kr.bydelta.koala.Property$setProperty(kr.bydelta.koala.Property.setProperty.T)/property)를 저장합니다. |

### Companion Object Properties

| Name | Summary |
|---|---|
| [empty](empty.md) | `val empty: `[`Sentence`](./index.md)<br>빈 문장 |
