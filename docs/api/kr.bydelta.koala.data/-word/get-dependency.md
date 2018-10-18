[kr.bydelta.koala.data](../index.md) / [Word](index.md) / [getDependency](./get-dependency.md)

# getDependency

`fun getDependency(): `[`DepTree`](../-dep-tree/index.md)

의존구문분석을 했다면, 현재 어절이 지배소인 의존구문 구조의 값을 돌려줍니다.

## 참고

**의존구조 분석**은 문장의 구성 어절들이 의존 또는 기능하는 관계를 분석하는 방법입니다.
예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
가장 마지막 단어인 '쌌다'가 핵심 어구가 되며,

* '먹었고'가 '쌌다'와 대등하게 연결되고
* '나는'은 '먹었고'의 주어로 기능하며
* '밥을'은 '먹었고'의 목적어로 기능합니다.
* '영희는'은 '쌌다'의 주어로 기능하고,
* '짐을'은 '쌌다'의 목적어로 기능합니다.

아래를 참고해보세요.

* [kr.bydelta.koala.proc.CanParseDependency](../../kr.bydelta.koala.proc/-can-dep-parse.md) 의존구조 분석을 수행하는 interface.
* [Sentence.getDependencyTree](../-sentence/get-dependency-tree.md) 전체 문장을 분석한 의존구조 [DepTree](../-dep-tree/index.md)를 가져오는 API
* [DepTree](../-dep-tree/index.md) 의존구문구조의 저장형태
* [PhraseTag](../../kr.bydelta.koala/-phrase-tag/index.md) 의존구조의 형태 분류를 갖는 Enum 값 (구구조 분류와 같음)
* [DependencyTag](../../kr.bydelta.koala/-dependency-tag/index.md) 의존구조의 기능 분류를 갖는 Enum 값

### Exceptions

`UninitializedPropertyAccessException` - 분석을 하지 않은 경우 발생합니다.

**Since**
2.0.0

**Return**
어절이 지배하는 의존구문구조 [DepTree](../-dep-tree/index.md)

