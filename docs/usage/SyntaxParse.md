--------

[목차로 이동](./index.md)

--------

## 목차 

- [개관](#구문-구조-분석하기)
- [Kotlin](#kotlin)
- [Scala](#scala)
- [Java](#java)
- [NodeJS](#javascript) (구현중)
- [Python 3](#python-3)

## 구문 구조 분석하기

문장 또는 문단을 분석해서 각 문장의 구문구조를 묶어낼 수 있습니다. 한나눔 분석기만 구문구조 분석을 지원합니다.

결과물은 Sentence 객체에 포함된 SyntaxTree가 됩니다.
- [Java, Scala, Kotlin의 Sentence](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.data/-sentence/index.html),
  [SyntaxTree](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.data/-syntax-tree/index.html)
- [NodeJS의 Sentence](https://koalanlp.github.io/nodejs-support/module-koalanlp_data.Sentence.html),
  [SyntaxTree](https://koalanlp.github.io/nodejs-support/module-koalanlp_data.SyntaxTree.html)
- [Python의 Sentence](https://koalanlp.github.io/python-support/html/koalanlp.html#koalanlp.data.Sentence),
  [SyntaxTree](https://koalanlp.github.io/python-support/html/koalanlp.html#koalanlp.data.SyntaxTree)

아래 분석 예시는 **'텍스트 문단'** 을 기준으로 분석한 결과들입니다. 
이미 타 분석기에서 분석된 `Sentence` 객체나, `Sentence`의 List인 경우에도 같은 방식으로 호출이 가능합니다. 

## 참고
**구문구조 분석**은 문장의 구성요소들(어절, 구, 절)이 이루는 문법적 구조를 분석하는 방법입니다.

예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
2개의 절이 있습니다
* 나는 밥을 먹었고
* 영희는 짐을 쌌다

각 절은 3개의 구를 포함합니다
* 나는, 밥을, 영희는, 짐을: 체언구
* 먹었고, 쌌다: 용언구

### Kotlin
Reference: [CanParseSyntax](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-can-parse-syntax/index.html),
[Parser](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.hnn/-parser/index.html)

```kotlin
import kr.bydelta.koala.hnn.Parser

val parser = Parser()

val parsed = parser.analyze("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.") 
// 또는 parser(...), parser.invoke(...)

println(parsed[0].getSyntaxTree().getTreeString()) // 첫번째 문장의 구문구조 트리를 출력합니다.
```

#### Scala
Reference: [CanParseSyntax](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-can-parse-syntax/index.html),
[Parser](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.hnn/-parser/index.html)

* [koalanlp-scala](https://koalanlp.github.io/scala-support)가 dependency로 포함되었다고 가정합니다.

```scala
import kr.bydelta.koala.hnn.Parser
import kr.bydelta.koala.Implicits._

val parser = new Parser()

val parsed = parser.analyze("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.")
// 또는 parser(...), parser.invoke(...)

println(parsed(0).getSyntaxTree().getTreeString()) // 첫번째 문장의 구문구조 트리를 출력합니다.
```

#### Java
Reference: [CanParseSyntax](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-can-parse-syntax/index.html),
           [Parser](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.hnn/-parser/index.html)

```java
import kr.bydelta.koala.hnn.Parser;
import kr.bydelta.koala.data.Sentence;

Parser parser = new Parser();

List<Sentence> parsed = parser.analyze("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.");
// 또는 parser.invoke(...)

System.out.println(parsed.get(0).getSyntaxTree().getTreeString()); // 첫번째 문장의 구문구조 트리를 출력합니다.
```

#### JavaScript (구현중)
Reference: [Parser](https://koalanlp.github.io/nodejs-support/module-koalanlp.Parser.html)

```javascript
let Parser = koalanlp.Parser;
let parser = new Parser(API.HNN);

/****** Asynchronous request ******/
let promise = parser.analyze("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.");
promise.then(function(result){ 
    /* Result는 Sentence[] 타입입니다. */ 
    console.log(result[0].getSyntaxTree().getTreeString());  // 첫번째 문장의 구문구조 트리를 출력합니다.
});

/****** Synchronous request ******/
let parsed = parser.analyzeSync("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.");
console.log(parsed[0].getSyntaxTree().getTreeString());  // 첫번째 문장의 구문구조 트리를 출력합니다.
```

#### Python 3
Reference: [Parser](https://koalanlp.github.io/python-support/html/koalanlp.html#koalanlp.proc.Parser)

```python
from koalanlp import API
from koalanlp.proc import Parser

parser = Parser(API.HNN) # 또는 API.KKMA, API.ETRI
# ETRI 분석기의 경우 API 키를 필수적으로 전달해야 합니다. 예: Parser(API.ETRI, apiKey=API_KEY)

parsed = parser("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.")
# 또는 parser.analyze(...), parser.invoke(...)

print(parsed[0].getSyntaxTree().getTreeString())  # 첫번째 문장의 구문구조 트리를 출력합니다.
```

--------

[목차로 이동](./index.md)

--------
