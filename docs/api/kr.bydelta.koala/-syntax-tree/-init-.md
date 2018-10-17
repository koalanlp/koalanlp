[kr.bydelta.koala](../index.md) / [SyntaxTree](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`SyntaxTree(type: `[`PhraseTag`](../-phrase-tag/index.md)`, node: `[`Word`](../-word/index.md)`?, vararg children: `[`SyntaxTree`](index.md)`)`

구문구조 분석의 결과.

### Parameters

`type` - 구구조 표지자

`node` - 현재 구구조에 직접 속하는 어절. 중간 구문구조인 경우 null.

`children` - 현재 구구조에 속하는 하위 구구조`SyntaxTree(type: `[`PhraseTag`](../-phrase-tag/index.md)`, node: `[`Word`](../-word/index.md)`? = null, children: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SyntaxTree`](index.md)`> = emptyList())`

구문구조 분석의 결과.

### Parameters

`type` - 구구조 표지자

`node` - 현재 구구조에 직접 속하는 어절. 중간 구문구조인 경우 null.

`children` - 현재 구구조에 속하는 하위 구구조. 하위 구구조가 없다면 빈 리스트.