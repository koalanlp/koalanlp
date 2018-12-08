--------

[목차로 이동](./index)

--------

## 문단에서 문장 분리하기

문단 텍스트를 문장들로 분리할 수 있습니다. 이런 작업은 2가지 방법이 있습니다.

1. [품사 분석 없이 문장을 분리하는 경우](#품사-분석-없이-문장을-분리하는-경우)
    - [Kotlin](#kotlin)
    - [Scala](#scala)
    - [Java](#java)
    - [NodeJS](#javascript) (구현중)
    - [Python 3](#python-3)
2. [품사 분석 결과를 토대로 문장을 분리하는 경우](#품사-부착-후-분리-방법)
    - [Kotlin, Scala](#kotlin-scala)
    - [Java](#java-1)
    - [NodeJS](#javascript-1) (구현중)
    - [Python 3](#python-3-1)

### 품사 분석 없이 문장을 분리하는 경우
품사 태깅을 거치지 않은 문장 분리는, 글의 표면 형태만을 토대로 문장을 나누는 것으로, 
한나눔과 OpenKoreanText 분석기만 지원됩니다.

#### Kotlin
Reference: [CanSplitSentence](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-can-split-sentence/index.html), 
[한나눔](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.hnn/-sentence-splitter/index.html), 
[OpenKoreanText](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.okt/-sentence-splitter/index.html)
```kotlin
import kr.bydelta.koala.hnn.SentenceSplitter // 또는 okt.SentenceSplitter

val splitter = SentenceSplitter()

val paragraph = splitter.sentences("분리할 문장을 이렇게 넣으면 문장이 분리됩니다. 간단하죠?")
// 또는 splitter(...), splitter.invoke(...)

println(paragraph[0]) //== 분리할 문장을 이렇게 넣으면 문장이 분리됩니다.
println(paragraph[1]) //== 간단하죠?
```

#### Scala
Reference: [CanSplitSentence](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-can-split-sentence/index.html), 
[한나눔](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.hnn/-sentence-splitter/index.html), 
[OpenKoreanText](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.okt/-sentence-splitter/index.html)

* [koalanlp-scala](https://koalanlp.github.io/scala-support)가 dependency로 포함되었다고 가정합니다.

```scala
import kr.bydelta.koala.hnn.SentenceSplitter // 또는 okt.SentenceSplitter
import kr.bydelta.koala.Implicit._

val splitter = new SentenceSplitter

val paragraph = splitter.sentences("분리할 문장을 이렇게 넣으면 문장이 분리됩니다. 간단하죠?")
// 또는 splitter(...), splitter.invoke(...)

println(paragraph(0)) //== 분리할 문장을 이렇게 넣으면 문장이 분리됩니다.
println(paragraph(1)) //== 간단하죠?
```

#### Java
Reference: [CanSplitSentence](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-can-split-sentence/index.html), 
[한나눔](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.hnn/-sentence-splitter/index.html), 
[OpenKoreanText](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.okt/-sentence-splitter/index.html)
```java
import kr.bydelta.koala.hnn.SentenceSplitter; // 또는 okt.SentenceSplitter

SentenceSplitter splitter = new SentenceSplitter();

List<String> paragraph = splitter.sentences("분리할 문장을 이렇게 넣으면 문장이 분리됩니다. 간단하죠?");
// 또는 splitter.invoke(...)

System.out.println(paragraph.get(0)); //== 분리할 문장을 이렇게 넣으면 문장이 분리됩니다.
System.out.println(paragraph.get(1)); //== 간단하죠?
```

#### JavaScript
Reference: [SentenceSplitter](https://koalanlp.github.io/nodejs-support/module-koalanlp_proc.SentenceSplitter.html)

* 아래 코드는 ES8과 호환되는 CommonJS (NodeJS > 8) 기준으로 작성되어 있습니다.

##### Async/Await

```javascript
const {SentenceSplitter} = require('koalanlp/proc');
const {HNN} = require('koalanlp/API');

async function someAsyncFunction(){
    // ....
    
    let splitter = new SentenceSplitter(HNN);
    let result = await splitter("분리할 문장을 이렇게 넣으면 문장이 분리됩니다. 간단하죠?");
    // 또는 splitter.sentences(...) 

    /* Result는 string[] 타입입니다. */
    console.log(result[0]); //== 분리할 문장을 이렇게 넣으면 문장이 분리됩니다.
    console.log(result[1]); //== 간단하죠?
        
    // ...
}

someAsyncFunction().then(
    () => console.log('After function finished'),
    (error) => console.error('Error occurred!', error)
);
```

##### Promise

```javascript
const {SentenceSplitter} = require('koalanlp/proc');
const {HNN} = require('koalanlp/API');

let splitter = new SentenceSplitter(HNN);
splitter("이 문단을 분석합니다. 문단 구분은 자동으로 합니다.")  // 또는 splitter.sentences(...)
    .then((result) => {
        /* Result는 string[] 타입입니다. */
        console.log(result[0]); //== 분리할 문장을 이렇게 넣으면 문장이 분리됩니다.
        console.log(result[1]); //== 간단하죠?
    }, (error) => console.error('Error occurred!', error));
```

##### Synchronous Call

```javascript
const {SentenceSplitter} = require('koalanlp/proc');
const {HNN} = require('koalanlp/API');

// ....

let splitter = new SentenceSplitter(HNN);
let result = splitter.sentencesSync("분리할 문장을 이렇게 넣으면 문장이 분리됩니다. 간단하죠?");

/* Result는 string[] 타입입니다. */
console.log(result[0]); //== 분리할 문장을 이렇게 넣으면 문장이 분리됩니다.
console.log(result[1]); //== 간단하죠?
    
// ...
```

#### Python 3
Reference: [SentenceSplitter](https://koalanlp.github.io/python-support/html/koalanlp.html#koalanlp.proc.SentenceSplitter)

```python
from koalanlp import API
from koalanlp.proc import SentenceSplitter

splitter = SentenceSplitter(splitter_type=API.HANNANUM)
paragraph = splitter("분리할 문장을 이렇게 넣으면 문장이 분리됩니다. 간단하죠?")
# 또는 splitter.sentences(...), splitter.invoke(...)

print(paragraph[0]) # == 분리할 문장을 이렇게 넣으면 문장이 분리됩니다.
print(paragraph[1]) # == 간단하죠?
```

### 품사 부착 후 분리 방법
또는, 품사 부착된 문단을 토대로 구분할 수도 있습니다.
KoalaNLP는 Heuristic한 방법을 사용하여 문장을 구분하는 기능을 제공합니다.

#### Kotlin, Scala
Reference: [SentenceSplitter](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-sentence-splitter/index.html)

* Scala의 경우 [koalanlp-scala](https://koalanlp.github.io/scala-support)가 dependency로 포함되고, `kr.bydelta.koala.Implicits._`를 import했다고 가정합니다.

```kotlin
import kr.bydelta.koala.proc.SentenceSplitter
import kr.bydelta.koala.data.Sentence

val tagger: Tagger /** 품사분석기 **/
val taggedSentence: Sentence = tagger.tagSentence("무엇인가 품사분석을 수행할 문단")
val paragraph = SentenceSplitter.sentences(taggedSentence)
// 또는 SentenceSplitter(...), SentenceSplitter.invoke(...)

println(paragraph[0].singleLineString()) // taggedSentence가 List<Sentence>로 구분됨 
```

#### Java
Reference: [SentenceSplitter](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-sentence-splitter/index.html)
```java
import kr.bydelta.koala.proc.SentenceSplitter; // 또는 okt.SentenceSplitter
import kr.bydelta.koala.data.Sentence;

Tagger tagger; /** 품사분석기 **/
Sentence taggedSentence = tagger.tagSentence("무엇인가 품사분석을 수행할 문단");
List<Sentence> paragraph = SentenceSplitter.sentences(taggedSentence);
// 또는 SentenceSplitter.invoke(...)

System.out.println(paragraph[0].singleLineString()); // taggedSentence가 List<Sentence>로 구분됨 
```

#### JavaScript 
Reference: [SentenceSplitter](https://koalanlp.github.io/nodejs-support/module-koalanlp.SentenceSplitter.html)
```javascript
let tagger = new koalanlp.Tagger(...); /** 품사분석기 **/
let SentenceSplitter = koalanlp.SentenceSplitter;
let taggedSentence = tagger.tagSentenceSync("무엇인가 품사분석을 수행할 문단");

/****** Asynchronous request ******/
let promise = SentenceSplitter.sentences(taggedSentence);
promise.then(function(result){ /* result는 taggedSentence가 구분된 Sentence[]임. */ });

/****** Synchronous request ******/
let paragraph = SentenceSplitter.sentencesSync("분리할 문장을 이렇게 넣으면 문장이 분리됩니다. 간단하죠?");
// paragraph는 taggedSentence가 구분된 Sentence[]임.
```

#### Python 3
Reference: [SentenceSplitter](https://koalanlp.github.io/python-support/html/koalanlp.html#koalanlp.proc.SentenceSplitter)

```python
from koalanlp.proc import SentenceSplitter, Tagger

tagger = Tagger(...) ### 품사분석기 ###
tagged_sentence = tagger.tagSentence("무엇인가 품사분석을 수행할 문단")
paragraph = SentenceSplitter.sentencesTagged(tagged_sentence[0]) # tagged_sentence는 각 인자별로 한 문장으로 간주된 List[Sentence]임.
```

--------

[목차로 이동](./index)

--------

