[kr.bydelta.koala](../index.md) / [RoleTree](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`RoleTree(node: `[`Word`](../-word/index.md)`, type: `[`RoleType`](../-role-type/index.md)`, vararg children: `[`RoleTree`](index.md)`)`
`RoleTree(node: `[`Word`](../-word/index.md)`, type: `[`RoleType`](../-role-type/index.md)`, children: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`RoleTree`](index.md)`> = emptyList())`

의미역 구조 분석의 결과.

### Parameters

`node` - 의미역 구조를 표현하는 지배소

`type` - 의미역 표지자

`children` - 지배를 받는 다른 의미역 구조