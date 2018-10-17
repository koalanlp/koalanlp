[kr.bydelta.koala.data](../index.md) / [DepTree](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`DepTree(head: `[`Word`](../-word/index.md)`, type: `[`PhraseTag`](../../kr.bydelta.koala/-phrase-tag/index.md)`, depType: `[`DependencyTag`](../../kr.bydelta.koala/-dependency-tag/index.md)`?, vararg children: `[`DepTree`](index.md)`)`

의존구문구조 분석의 결과를 생성합니다.

### Parameters

`head` - 현재 의존구조의 지배소 [Word](../-word/index.md)

`type` - 구구조 표지자, [PhraseTag](../../kr.bydelta.koala/-phrase-tag/index.md) Enum 값 (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함)

`depType` - 의존기능 표지자, [DependencyTag](../../kr.bydelta.koala/-dependency-tag/index.md) Enum 값. 별도의 기능이 지정되지 않으면 null. (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함)

`children` - 지배를 받는 다른 의존구조 [DepTree](index.md)의 목록 (가변인자)

**Since**
2.0.0

`DepTree(head: `[`Word`](../-word/index.md)`, type: `[`PhraseTag`](../../kr.bydelta.koala/-phrase-tag/index.md)`, depType: `[`DependencyTag`](../../kr.bydelta.koala/-dependency-tag/index.md)`? = null, children: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`DepTree`](index.md)`> = emptyList())`

의존구문구조 분석 결과를 생성합니다.

### Parameters

`head` - 현재 의존구조의 지배소 [Word](../-word/index.md)

`type` - 구구조 표지자, [PhraseTag](../../kr.bydelta.koala/-phrase-tag/index.md) Enum 값 (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함)

`depType` - 의존기능 표지자, [DependencyTag](../../kr.bydelta.koala/-dependency-tag/index.md) Enum 값. 별도의 기능이 지정되지 않으면 null. (ETRI 표준안은 구구조+의존기능으로 의존구문구조를 표기함)

`children` - 지배를 받는 다른 의존구조 [DepTree](index.md)의 목록

**Constructor**
의존구문구조 분석 결과를 생성합니다.

