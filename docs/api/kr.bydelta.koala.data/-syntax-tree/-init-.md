[kr.bydelta.koala.data](../index.md) / [SyntaxTree](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`SyntaxTree(type: `[`PhraseTag`](../../kr.bydelta.koala/-phrase-tag/index.md)`, node: `[`Word`](../-word/index.md)`?, vararg children: `[`SyntaxTree`](index.md)`)`

구문구조 분석의 결과를 생성합니다.

### Parameters

`type` - 구구조 표지자

`node` - 현재 구구조에 직접 속하는 어절. 중간 구문구조인 경우 null.

`children` - 현재 구구조에 속하는 하위 구구조

**Since**
2.0.0

`SyntaxTree(type: `[`PhraseTag`](../../kr.bydelta.koala/-phrase-tag/index.md)`, node: `[`Word`](../-word/index.md)`? = null, children: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SyntaxTree`](index.md)`> = emptyList())`

구문구조 분석의 결과를 생성합니다.

### Parameters

`type` - 구구조 표지자입니다. [PhraseTag](../../kr.bydelta.koala/-phrase-tag/index.md) Enum 값입니다.

`node` - 현재 구구조에 직접 속하는 [Word](../-word/index.md)들. 중간 구문구조인 경우 leaf를 직접 포함하지 않으므로 null.

`children` - 현재 구구조에 속하는 하위 구구조 [SyntaxTree](index.md). 하위 구구조가 없다면 빈 리스트.

**Constructor**
구문구조 분석의 결과를 생성합니다.

