--------

[목차로 이동](./index.md)

--------

## 목차 

- [개관](#여러-패키지-함께-쓰기)
- [Kotlin](#kotlin)
- [Scala](#scala)
- [Java](#java)
- [NodeJS](#javascript) (구현중)
- [Python 3](#python-3)

## 여러 패키지 함께 쓰기

KoalaNLP는 실험적으로 여러 분석기의 결과를 세종 품사 분석 결과를 기준으로 상호 변환할 수 있도록 하고 있습니다.

API 형태로 제공되어 품사 분석 결과를 넣을 수 없는 ETRI 분석기를 제외하면, 대부분의 분석기를 교차하여 사용하실 수 있습니다.

예를 들어,

1. OKT(Open Korean Text) 분석기로 문장을 분리한 다음, 
2. 각 문장을 KMR(코모란) 분석기로 품사 분석하고, 
3. 그 결과를 이용하여 HNN(한나눔) 분석기로 구문구조 분석을 수행할 수 있습니다.

아래 예시는 이 예시를 따라 작성되어 있는 코드들입니다.

### Kotlin
```kotlin
import kr.bydelta.koala.okt.SentenceSplitter
import kr.bydelta.koala.kmr.Tagger
import kr.bydelta.koala.hnn.Parser

val splitter = SentenceSplitter()
val tagger = Tagger()
val parser = Parser()

val splits = splitter("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.")
val tagged = splits.map{ tagger.tagSentence(it) }
val parsed = parser(tagged)

for(sent in parsed){
    println(sent.getSyntaxTree().getTreeString())
}
```

#### Scala
* [koalanlp-scala](https://koalanlp.github.io/scala-support)가 dependency로 포함되었다고 가정합니다.

```scala
import kr.bydelta.koala.okt.SentenceSplitter
import kr.bydelta.koala.kmr.Tagger
import kr.bydelta.koala.hnn.Parser

import kr.bydelta.koala.Implicits._

val splitter = new SentenceSplitter
val tagger = new Tagger
val parser = new Parser

val splits = splitter("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.")
val tagged = splits.map(tagger.tagSentence)
val parsed = parser(tagged)

for(sent <- parsed){
    println(sent.getSyntaxTree.getTreeString())
}
```

#### Java

```java
import kr.bydelta.koala.data.Sentence;
import kr.bydelta.koala.data.SyntaxTree;

import kr.bydelta.koala.okt.SentenceSplitter;
import kr.bydelta.koala.kmr.Tagger;
import kr.bydelta.koala.hnn.Parser;

SentenceSplitter splitter = new SentenceSplitter();
Tagger tagger = new Tagger();
Parser parser = new Parser();

List<String> splits = splitter.sentences("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.")
List<Sentence> tagged = splits.map(it -> tagger.tagSentence(it))
List<Sentence> parsed = parser.analyze(tagged)

for(Sentence sent : parsed){
    System.out.println(sent.getSyntaxTree().getTreeString())
}
```

#### JavaScript

* 아래 코드는 ES8과 호환되는 CommonJS (NodeJS > 8) 기준으로 작성되어 있습니다.

##### Async/Await

```javascript
const {SentenceSplitter, Tagger, Parser} = require('koalanlp/proc');
const {OKT, KMR, HNN} = require('koalanlp/API');

async function someAsyncFunction(){
    // ....
    
    let splitter = new SentenceSplitter(OKT);
    let tagger = new Tagger(KMR);
    let parser = new Parser(HNN);
    
    let splits = await splitter("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.");
    let tagged = await tagger.tagSentence(splits);
    let parsed = await parser(tagged);
    
    /* Result는 Sentence[] 타입입니다. */
    for(const sent of parsed){
        console.log(sent.getSyntaxTree().getTreeString())
    }
    
    // ...
}

someAsyncFunction().then(
    () => console.log('After function finished'),
    (error) => console.error('Error occurred!', error)
);
```

##### Promise

```javascript
const {SentenceSplitter, Tagger, Parser} = require('koalanlp/proc');
const {OKT, KMR, HNN} = require('koalanlp/API');

let splitter = new SentenceSplitter(OKT);
let tagger = new Tagger(KMR);
let parser = new Parser(HNN);

splitter("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.")
    .then(tagger.tagSentence)
    .then(parser)
    .then((result) => {
        /* Result는 Sentence[] 타입입니다. */
        for(const sent of parsed){
            console.log(sent.getSyntaxTree().getTreeString())
        }
    }, (error) => console.error('Error occurred!', error));
```

##### Synchronous Call

```javascript
const {SentenceSplitter, Tagger, Parser} = require('koalanlp/proc');
const {OKT, KMR, HNN} = require('koalanlp/API');

// ....

let splitter = new SentenceSplitter(OKT);
let tagger = new Tagger(KMR);
let parser = new Parser(HNN);

let splits = splitter.sentencesSync("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.");
let tagged = tagger.tagSentenceSync(splits);
let parsed = parser.analyzeSync(tagged);

/* Result는 Sentence[] 타입입니다. */
for(const sent of parsed){
    console.log(sent.getSyntaxTree().getTreeString())
}
```


#### Python 3

```python
from koalanlp import API
from koalanlp.proc import SentenceSplitter, Tagger, Parser

splitter = SentenceSplitter(API.OKT)
tagger = Tagger(API.KMR)
parser = Parser(API.HNN)

splits = splitter("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.")
tagged = tagger.tagSentence(*splits)
parsed = parser(tagged)

for sent in parsed:
    print(sent.getSyntaxTree().getTreeString())
```

--------

[목차로 이동](./index.md)

--------
