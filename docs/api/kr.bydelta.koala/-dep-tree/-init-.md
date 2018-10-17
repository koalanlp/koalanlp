[kr.bydelta.koala](../index.md) / [DepTree](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`DepTree(head: `[`Word`](../-word/index.md)`, type: `[`PhraseTag`](../-phrase-tag/index.md)`, depType: `[`DependencyTag`](../-dependency-tag/index.md)`?, vararg children: `[`DepTree`](index.md)`)`
`DepTree(head: `[`Word`](../-word/index.md)`, type: `[`PhraseTag`](../-phrase-tag/index.md)`, depType: `[`DependencyTag`](../-dependency-tag/index.md)`? = null, children: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`DepTree`](index.md)`> = emptyList())`

의존구문구조 분석의 결과.

### Parameters

`head` - 현재 의존구조의 지배소

`type` - 구구조 표지자 (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함)

`depType` - 의존기능 표지자 (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함)

`children` - 지배를 받는 다른 의존구조