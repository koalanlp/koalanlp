--------

[목차로 이동](./index.md)

--------

## 품사 분석하기

문장 또는 문단을 분석해서 품사를 부착할 수 있습니다. 모든 분석기 API가 품사 분석을 지원합니다.

결과물은 Sentence 객체가 됩니다.
- [Java, Scala, Kotlin의 Sentence](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.data/-sentence/index.html)
- [NodeJS의 Sentence](https://koalanlp.github.io/nodejs-support/module-koalanlp_data.Sentence.html)
- [Python의 Sentence](https://koalanlp.github.io/python-support/build/html/koalanlp.data.html#koalanlp.data.Sentence)

아래 분석 예시는 **'문단'** 을 기준으로 분석한 결과들입니다. 문장 1개를 분석하고 싶은 경우 `tag` 대신에 `tagSentence`를 사용하면 됩니다. 

### 참고
**형태소**는 의미를 가지는 요소로서는 더 이상 분석할 수 없는 가장 작은 말의 단위로 정의됩니다.

**형태소 분석**은 문장을 형태소의 단위로 나누는 작업을 의미합니다.

예) '문장을 형태소로 나눠봅시다'의 경우, 아래와 같이 대략적으로 형태소를 나눌 수 있습니다.
* 문장/일반명사, -을/조사,
* 형태소/일반명사, -로/조사,
* 나누-(다)/동사, -어-/어미, 보-(다)/동사, -ㅂ시다/어미

### Kotlin
Reference: [CanTag](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-can-tag/index.html) 및 
이를 상속한 class들.

```kotlin
import kr.bydelta.koala.eunjeon.Tagger
// 또는 eunjeon 대신 다른 분석기 가능: arirang, daon, etri, eunjeon, hnn, kkma, kmr, okt, rhino 

val tagger = Tagger()
// 코모란 분석기는 경량 분석기를 사용하는 옵션이 있습니다. 예: Tagger(useLightTagger = true)
// ETRI 분석기의 경우 API 키를 필수적으로 전달해야 합니다. 예: Tagger(API_KEY)

val taggedParagraph = tagger.tag("문단을 분석합니다. 자동으로 분리되어 목록을 만듭니다.")
// 또는 tagger(...), tagger.invoke(...)

// 분석 결과는 Sentence 클래스의 List입니다.
println(taggedParagraph[0].singleLineString()) // "문단을 분석합니다."의 품사분석 결과 출력
```

#### Scala
Reference: [CanTag](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-can-tag/index.html) 및 
이를 상속한 클래스들.

* [koalanlp-scala](https://koalanlp.github.io/scala-support)가 dependency로 포함되었다고 가정합니다.

```scala
import kr.bydelta.koala.eunjeon.Tagger
import kr.bydelta.koala.Implicits._
// 또는 eunjeon 대신 다른 분석기 가능: arirang, daon, etri, eunjeon, hnn, kkma, kmr, okt, rhino 

val tagger = new Tagger()
// 코모란 분석기는 경량 분석기를 사용하는 옵션이 있습니다. 예: new Tagger(true)
// ETRI 분석기의 경우 API 키를 필수적으로 전달해야 합니다. 예: new Tagger(API_KEY)

val taggedParagraph = tagger("문단을 분석합니다. 자동으로 분리되어 목록을 만듭니다.")
// 또는 tagger(...), tagger.invoke(...)

// 분석 결과는 Sentence 클래스의 List입니다.
println(taggedParagraph(0).singleLineString()) // "문단을 분석합니다."의 품사분석 결과 출력

```

#### Java
Reference: [CanTag](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.proc/-can-tag/index.html) 및 
이를 상속한 class들.

```java
import kr.bydelta.koala.eunjeon.Tagger;
import kr.bydelta.koala.data.Sentence;
// 또는 eunjeon 대신 다른 분석기 가능: arirang, daon, etri, eunjeon, hnn, kkma, kmr, okt, rhino 

Tagger tagger = new Tagger();
// 코모란 분석기는 경량 분석기를 사용하는 옵션이 있습니다. 예: new Tagger(true)
// ETRI 분석기의 경우 API 키를 필수적으로 전달해야 합니다. 예: new Tagger(API_KEY)

// 문단을 분석해서 문장들로 얻기 (각 API가 문단 분리를 지원하지 않아도, KoalaNLP가 자동으로 구분합니다)
List<Sentence> taggedParagraph = tagger.tag("문단을 분석합니다. 자동으로 분리되어 목록을 만듭니다.");
// 또는 tagger.invoke(...)

// 분석 결과는 Sentence 클래스의 List입니다.
println(taggedParagraph.get(0).singleLineString()) // "문단을 분석합니다."의 품사분석 결과 출력
```

#### JavaScript (구현중)
Reference: [Tagger](https://koalanlp.github.io/nodejs-support/module-koalanlp.Tagger.html)

```javascript
let Tagger = koalanlp.Tagger;
let tagger = new Tagger(API.EUNJEON); // 또는 다른 API 값.
// 코모란 분석기는 경량 분석기를 사용하는 옵션이 있습니다. 예: new Tagger(API.KMR, {'useLightTagger: true})
// ETRI 분석기의 경우 API 키를 필수적으로 전달해야 합니다. 예: new Tagger(API.ETRI, {'apiKey': API_KEY})

/****** Asynchronous request ******/
let paraPromise = tagger.tag("문단을 분석합니다. 자동으로 분리되어 목록을 만듭니다.");
paraPromise.then(function(taggedParagraph){
    console.log(taggedParagraph[0].singleLineString()); // "문단을 분석합니다."의 품사분석 결과 출력
    /* 결과는 Sentence[] 타입입니다. */ 
});

/****** Synchronous request ******/
let taggedParagraph = tagger.tagSync("문단을 분석합니다. 자동으로 분리되어 목록을 만듭니다."); // Sentence[] 타입
console.log(taggedParagraph[0].singleLineString()); // "문단을 분석합니다."의 품사분석 결과 출력
```

#### Python (구현중)
Reference: [Tagger](https://koalanlp.github.io/python-support/build/html/koalanlp.api.html#koalanlp.api.Tagger)

```python
from koalanlp import *

tagger = Tagger(API.EUNJEON) 
// 코모란 분석기는 경량 분석기를 사용하는 옵션이 있습니다. 예: Tagger(API.KMR, use_light_tagger=true)
// ETRI 분석기의 경우 API 키를 필수적으로 전달해야 합니다. 예: Tagger(API.ETRI, api_key=API_KEY)

// 문단을 분석해서 문장들로 얻기 (각 API가 문단 분리를 지원하지 않아도, KoalaNLP가 자동으로 구분합니다)
taggedParagraph = tagger.tag("문단을 분석합니다. 자동으로 분리되어 목록을 만듭니다.")

print(taggedParagraph[0].singleLineString()); // "문단을 분석합니다."의 품사분석 결과 출력
```

--------

[목차로 이동](./index.md)

--------
