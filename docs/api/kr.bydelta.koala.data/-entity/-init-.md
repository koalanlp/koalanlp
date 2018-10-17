[kr.bydelta.koala.data](../index.md) / [Entity](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Entity(type: `[`CoarseEntityType`](../../kr.bydelta.koala/-coarse-entity-type/index.md)`, fineType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, words: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Word`](../-word/index.md)`>)`

개체명 분석 결과를 저장합니다.

### Parameters

`type` - 개체명 대분류 값, [CoarseEntityType](../../kr.bydelta.koala/-coarse-entity-type/index.md)에 기록된 개체명 중 하나.

`fineType` - 개체명 세분류 값으로, [type](-init-.md#kr.bydelta.koala.data.Entity$<init>(kr.bydelta.koala.CoarseEntityType, kotlin.String, kotlin.collections.List((kr.bydelta.koala.data.Word)))/type)으로 시작하는 문자열.

`words` - 개체명을 이루는 어절의 목록

**Constructor**
개체명 분석 결과를 저장합니다.

