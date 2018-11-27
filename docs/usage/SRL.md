--------

[목차로 이동](./index.md)

--------

## 개체명 인식하기

문장 또는 문단을 분석해서 각 문장 속에서 사람이나 장소 등을 나타내는 개체명을 묶어낼 수 있습니다. ETRI 분석기만 의존구조 분석을 지원합니다.

결과물은 Sentence 객체에 포함된 RoleEdge의 List가 됩니다.
- [Java, Scala, Kotlin의 Sentence](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.data/-sentence/index.html),
  [RoleEdge](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.data/-role-edge/index.html)
- [NodeJS의 Sentence](https://koalanlp.github.io/nodejs-support/module-koalanlp_data.Sentence.html),
  [RoleEdge](https://koalanlp.github.io/nodejs-support/module-koalanlp_data.RoleEdge.html)
- [Python의 Sentence](https://koalanlp.github.io/python-support/html/koalanlp.html#koalanlp.data.Sentence),
  [RoleEdge](https://koalanlp.github.io/python-support/html/koalanlp.html#koalanlp.data.RoleEdge)

아래 분석 예시는 **'텍스트 문단'** 을 기준으로 분석한 결과들입니다. 
이미 타 분석기에서 분석된 `Sentence` 객체나, `Sentence`의 List인 경우에도 같은 방식으로 호출이 가능합니다. 

## 참고
**의미역 결정**은 문장의 구성 어절들의 역할/기능을 분석하는 방법입니다.

예) '나는 밥을 어제 집에서 먹었다'라는 문장에는
동사 '먹었다'를 중심으로
* '나는'은 동작의 주체를,
* '밥을'은 동작의 대상을,
* '어제'는 동작의 시점을
* '집에서'는 동작의 장소를 나타냅니다.

### Kotlin
Reference: [CanLabelSemanticRole](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-can-label-semantic-role/index.html),
ETRI [RoleLabeler](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.etri/-role-labeler/index.html)

```kotlin
import kr.bydelta.koala.etri.RoleLabeler

val API_KEY = /** ETRI에서 발급받은 키 **/
val recognizer = RoleLabeler(API_KEY)

val parsed = recognizer.analyze("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.") 
// 또는 recognizer(...), recognizer.invoke(...)

// 첫번째 문장의 의미역들을 출력합니다.
parsed[0].getRoles().forEach{ role ->
    println(role)
}
```

#### Scala
Reference: [CanLabelSemanticRole](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-can-label-semantic-role/index.html),
           ETRI [RoleLabeler](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.etri/-role-labeler/index.html)

* [koalanlp-scala](https://koalanlp.github.io/scala-support)가 dependency로 포함되었다고 가정합니다.

```scala
import kr.bydelta.koala.etri.EntityRecognizer
import kr.bydelta.koala.Implicits._

val API_KEY = /** ETRI에서 발급받은 키 **/
val recognizer = new RoleLabeler(API_KEY)

val parsed = recognizer.analyze("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.") 
// 또는 recognizer(...), recognizer.invoke(...)

// 첫번째 문장의 의미역들을 출력합니다.
parsed[0].getRoles().forEach{ role =>
    println(role)
}
```

#### Java
Reference: [CanLabelSemanticRole](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-can-label-semantic-role/index.html),
           ETRI [RoleLabeler](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.etri/-role-labeler/index.html)

```java
import kr.bydelta.koala.etri.RoleLabeler;
import kr.bydelta.koala.data.Sentence;
import kr.bydelta.koala.data.RoleEdge;

String API_KEY = /** ETRI에서 발급받은 키 **/
RoleLabeler recognizer = new RoleLabeler(API_KEY);

List<Sentence> parsed = recognizer.analyze("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.") 
// 또는 recognizer.invoke(...)

// 첫번째 문장의 의미역들을 출력합니다.
for(RoleEdge role : parsed[0].getRoles()) {
    println(role);
}
```

#### JavaScript (구현중)
Reference: [RoleLabeler](https://koalanlp.github.io/nodejs-support/module-koalanlp.RoleLabeler.html)

```javascript
let RoleLabeler = koalanlp.RoleLabeler;
let recognizer = new RoleLabeler(API.ETRI, {'apiKey': API_KEY});

/****** Asynchronous request ******/
let promise = recognizer.analyze("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.");
promise.then(function(result){ 
    /* Result는 Sentence[] 타입입니다. */
    
    // 첫번째 문장의 의미역들을 출력합니다.
    result[0].getRoles().forEach((role) => console.log(role));
});

/****** Synchronous request ******/
let parsed = recognizer.analyzeSync("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.");
// 첫번째 문장의 의존구조를 출력합니다.
parsed[0].getRoles().forEach((role) => console.log(role));
```

#### Python 3
Reference: [RoleLabeler](https://koalanlp.github.io/python-support/html/koalanlp.html#koalanlp.proc.RoleLabeler)

```python
from koalanlp import API
from koalanlp.proc import EntityRecognizer

recognizer = RoleLabeler(API.ETRI, apiKey=API_KEY)

parsed = recognizer("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.")
// 또는 recognizer.analyze(...)

// 첫번째 문장의 의미역들을 출력합니다.
for role in parsed[0].getRoles():
    print(role)
```

--------

[목차로 이동](./index.md)

--------
