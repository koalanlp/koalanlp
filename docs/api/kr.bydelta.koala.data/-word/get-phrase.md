[kr.bydelta.koala.data](../index.md) / [Word](index.md) / [getPhrase](./get-phrase.md)

# getPhrase

`fun getPhrase(): `[`SyntaxTree`](../-syntax-tree/index.md)

구문분석을 했다면, 현재 어절이 속한 직속 상위 구구조(Phrase)를 돌려줍니다.

## 참고

**구문구조 분석**은 문장의 구성요소들(어절, 구, 절)이 이루는 문법적 구조를 분석하는 방법입니다.
예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
2개의 절이 있습니다

* 나는 밥을 먹었고
* 영희는 짐을 쌌다
각 절은 3개의 구를 포함합니다
* 나는, 밥을, 영희는, 짐을: 체언구
* 먹었고, 쌌다: 용언구

아래를 참고해보세요.

* [kr.bydelta.koala.proc.CanParseSyntax](../../kr.bydelta.koala.proc/-can-syntax-parse.md) 구문구조 분석을 수행하는 interface.
* [Sentence.getSyntaxTree](../-sentence/get-syntax-tree.md) 전체 문장을 분석한 [SyntaxTree](../-syntax-tree/index.md)를 가져오는 API
* [SyntaxTree](../-syntax-tree/index.md) 구문구조를 저장하는 형태
* [PhraseTag](../../kr.bydelta.koala/-phrase-tag/index.md) 구구조의 형태 분류를 갖는 Enum 값

### Exceptions

`UninitializedPropertyAccessException` - 분석을 하지 않은 경우 발생합니다.

**Since**
2.0.0

**Return**
어절의 상위 구구조 [SyntaxTree](../-syntax-tree/index.md)

