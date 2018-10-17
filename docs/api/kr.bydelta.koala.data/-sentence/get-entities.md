[kr.bydelta.koala.data](../index.md) / [Sentence](index.md) / [getEntities](./get-entities.md)

# getEntities

`fun getEntities(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Entity`](../-entity/index.md)`>`

개체명 분석을 했다면, 문장의 모든 개체명 목록을 돌려줍니다.

## 참고

**개체명 인식**은 문장에서 인물, 장소, 기관, 대상 등을 인식하는 기술입니다.
예) '철저한 진상 조사를 촉구하는 국제사회의 목소리가 커지고 있는 가운데, 트럼프 미국 대통령은 되레 사우디를 감싸고 나섰습니다.'에서, 다음을 인식하는 기술입니다.

* '트럼프': 인물
* '미국' : 국가
* '대통령' : 직위
* '사우디' : 국가

아래를 참고해보세요.

* [kr.bydelta.koala.proc.CanRecognizeEntity](../../kr.bydelta.koala.proc/-can-recognize-entity.md) 개체명 인식기 interface
* [Word.getEntities](../-word/get-entities.md) 해당 어절을 포함하는 [Entity](../-entity/index.md)를 가져오는 API
* [Entity](../-entity/index.md) 개체명을 저장하는 형태
* [CoarseEntityType](../-entity/index.md)의 대분류 개체명 분류구조 Enum 값

### Exceptions

`UninitializedPropertyAccessException` - 분석을 하지 않은 경우 발생합니다.

**Since**
2.0.0

**Return**
문장에 포함된 모든 [Entity](../-entity/index.md)의 목록입니다.

