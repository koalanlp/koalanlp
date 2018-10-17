[kr.bydelta.koala.data](../index.md) / [Word](index.md) / [getRole](./get-role.md)

# getRole

`fun getRole(): `[`RoleTree`](../-role-tree/index.md)

의미역 분석을 했다면, 현재 어절이 지배하는 의미역 구조를 돌려줌.

## 참고

**의미역 결정**은 문장의 구성 어절들의 역할/기능을 분석하는 방법입니다.
예) '나는 밥을 어제 집에서 먹었다'라는 문장에는
동사 '먹었다'를 중심으로

* '나는'은 동작의 주체를,
* '밥을'은 동작의 대상을,
* '어제'는 동작의 시점을
* '집에서'는 동작의 장소를 나타냅니다.

아래를 참고해보세요.

* [kr.bydelta.koala.proc.CanLabelSemanticRole](../../kr.bydelta.koala.proc/-can-label-semantic-role.md) 의미역 분석을 수행하는 interface.
* [Sentence.getSemRoleTree](../-sentence/get-sem-role-tree.md) 전체 문장을 분석한 의미역 구조 [RoleTree](../-role-tree/index.md)를 가져오는 API
* [RoleTree](../-role-tree/index.md) 의미역 구조를 저장하는 형태
* [RoleType](../../kr.bydelta.koala/-role-type/index.md) 의미역 분류를 갖는 Enum 값

### Exceptions

`UninitializedPropertyAccessException` - 분석을 하지 않았거나 의미역 구조에 이 어절이 포함되지 않는 경우 발생합니다.

**Since**
2.0.0

**Return**
어절이 지배하는 의미역 구조 [RoleTree](../-role-tree/index.md)

