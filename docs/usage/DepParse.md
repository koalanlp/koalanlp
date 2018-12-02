--------

[목차로 이동](./index.md)

--------

## 목차 

- [개관](#의존-구조-분석하기)
- [Kotlin](#kotlin)
- [Scala](#scala)
- [Java](#java)
- [NodeJS](#javascript) (구현중)
- [Python 3](#python-3)

## 의존 구조 분석하기

문장 또는 문단을 분석해서 각 문장 속 단어 사이의 의존관계를 묶어낼 수 있습니다. 한나눔, 꼬꼬마, ETRI 분석기만 의존구조 분석을 지원합니다.

결과물은 Sentence 객체에 포함된 DepEdge의 List가 됩니다.
- [Java, Scala, Kotlin의 Sentence](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.data/-sentence/index.html),
  [DepEdge](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.data/-dep-edge/index.html)
- [NodeJS의 Sentence](https://koalanlp.github.io/nodejs-support/module-koalanlp_data.Sentence.html),
  [DepEdge](https://koalanlp.github.io/nodejs-support/module-koalanlp_data.DepEdge.html)
- [Python의 Sentence](https://koalanlp.github.io/python-support/html/koalanlp.html#koalanlp.data.Sentence),
  [DepEdge](https://koalanlp.github.io/python-support/html/koalanlp.html#koalanlp.data.DepEdge)

아래 분석 예시는 **'텍스트 문단'** 을 기준으로 분석한 결과들입니다. 
이미 타 분석기에서 분석된 `Sentence` 객체나, `Sentence`의 List인 경우에도 같은 방식으로 호출이 가능합니다. 

## 참고
**의존구조 분석**은 문장의 구성 어절들이 의존 또는 기능하는 관계를 분석하는 방법입니다.

예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
가장 마지막 단어인 '쌌다'가 핵심 어구가 되며,

* '먹었고'가 '쌌다'와 대등하게 연결되고
* '나는'은 '먹었고'의 주어로 기능하며
* '밥을'은 '먹었고'의 목적어로 기능합니다.
* '영희는'은 '쌌다'의 주어로 기능하고,
* '짐을'은 '쌌다'의 목적어로 기능합니다.

### Kotlin
Reference: [CanParseDependency](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-can-parse-dependency/index.html),
한나눔 [Parser](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.hnn/-parser/index.html),
꼬꼬마 [Parser](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.kkma/-parser/index.html),
ETRI [Parser](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.etri/-parser/index.html)

```kotlin
import kr.bydelta.koala.hnn.Parser // 또는 kkma.Parser, etri.Parser

val parser = Parser()
// ETRI 분석기의 경우 API 키를 필수적으로 전달해야 합니다. 예: Parser(API_KEY)

val parsed = parser.analyze("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.") 
// 또는 parser(...), parser.invoke(...)

// 첫번째 문장의 의존구조를 출력합니다.
parsed[0].getDependencies().forEach{ dep ->
    println(dep)
}
```

#### Scala
Reference: [CanParseDependency](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-can-parse-dependency/index.html),
           한나눔 [Parser](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.hnn/-parser/index.html),
           꼬꼬마 [Parser](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.kkma/-parser/index.html),
           ETRI [Parser](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.etri/-parser/index.html)

* [koalanlp-scala](https://koalanlp.github.io/scala-support)가 dependency로 포함되었다고 가정합니다.

```scala
import kr.bydelta.koala.hnn.Parser // 또는 kkma.Parser, etri.Parser
import kr.bydelta.koala.Implicits._

val parser = new Parser()
// ETRI 분석기의 경우 API 키를 필수적으로 전달해야 합니다. 예: new Parser(API_KEY)

val parsed = parser.analyze("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.")
// 또는 parser(...), parser.invoke(...)

// 첫번째 문장의 의존구조를 출력합니다.
parsed[0].getDependencies().forEach{ dep =>
    println(dep)
}
```

#### Java
Reference: [CanParseDependency](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-can-parse-dependency/index.html),
           한나눔 [Parser](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.hnn/-parser/index.html),
           꼬꼬마 [Parser](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.kkma/-parser/index.html),
           ETRI [Parser](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.etri/-parser/index.html)

```java
import kr.bydelta.koala.hnn.Parser; // 또는 kkma.Parser, etri.Parser
import kr.bydelta.koala.data.Sentence;
import kr.bydelta.koala.data.DepEdge;

Parser parser = new Parser();
// ETRI 분석기의 경우 API 키를 필수적으로 전달해야 합니다. 예: new Parser(API_KEY)

List<Sentence> parsed = parser.analyze("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.");
// 또는 parser.invoke(...)

// 첫번째 문장의 의존구조를 출력합니다.
for(DepEdge dep: parsed[0].getDependencies()){
    System.out.println(parsed.get(0).getSyntaxTree().getTreeString());    
}
```

#### JavaScript
Reference: [Parser](https://koalanlp.github.io/nodejs-support/module-koalanlp_proc.Parser.html)

* 아래 코드는 ES8과 호환되는 CommonJS (NodeJS > 8) 기준으로 작성되어 있습니다.

##### Async/Await

```javascript
const {Parser} = require('koalanlp/proc');
const {HNN} = require('koalanlp/API');

async function someAsyncFunction(){
    // ....
    
    let parser = new Parser(HNN);
    let result = await parser("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.");
    // 또는 parser.analyze(...)

    /* Result는 Sentence[] 타입입니다. */
    // 첫번째 문장의 의존구조를 출력합니다.
    result[0].getDependencies().forEach((dep) => console.log(dep));
    
    // ...
}

someAsyncFunction().then(
    () => console.log('After function finished'),
    (error) => console.error('Error occurred!', error)
);
```

##### Promise

```javascript
const {Parser} = require('koalanlp/proc');
const {HNN} = require('koalanlp/API');

let parser = new Parser(HNN);
parser("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.")  // 또는 parser.analyze(...)
    .then((result) => {
        /* Result는 Sentence[] 타입입니다. */
        // 첫번째 문장의 의존구조를 출력합니다.
        result[0].getDependencies().forEach((dep) => console.log(dep));
    }, (error) => console.error('Error occurred!', error));
```

##### Synchronous Call (준비중)

#### Python 3
Reference: [Parser](https://koalanlp.github.io/python-support/html/koalanlp.html#koalanlp.proc.Parser)

```python
from koalanlp import API
from koalanlp.proc import Parser

parser = Parser(API.HNN) # 또는 API.KKMA, API.ETRI
# ETRI 분석기의 경우 API 키를 필수적으로 전달해야 합니다. 예: Parser(API.ETRI, apiKey=API_KEY)

parsed = parser("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.")
# 또는 parser.analyze(...), parser.invoke(...)

# 첫번째 문장의 의존구조를 출력합니다.
for dep in parsed[0].getDependencies():
    print(dep)
```

--------

[목차로 이동](./index.md)

--------
