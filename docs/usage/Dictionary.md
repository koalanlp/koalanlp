--------

[목차로 이동](./index.md)

--------

## 사전 사용하기

사전에 접근하여 내용을 불러오거나 새 항목을 추가할 수 있습니다. 아리랑, 은전한닢, 한나눔, 꼬꼬마, 코모란, OpenKoreanText에서 사용 가능합니다. 

### Kotlin
Reference: 
아리랑 [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.arirang/-dictionary/index.html),
은전한닢 [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.eunjeon/-dictionary/index.html),
한나눔 [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.hnn/-dictionary/index.html),
꼬꼬마 [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.kkma/-dictionary/index.html),
코모란 [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.kmr/-dictionary/index.html),
OpenKoreanText [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.okt/-dictionary/index.html)

```kotlin
import kr.bydelta.koala.kkma.Dictionary // 또는 다른 패키지 사용
import kr.bydelta.koala.POS

/***** 사전에 등록하기 *****/
Dictionary.addUserDictionary("코알라NLP", POS.NNP) // 1개 등록
Dictionary.addUserDictionary("코모란" to POS.NNP, "은전한닢" to POS.NNP) // 2개 이상 등록
Dictionary.addUserDictionary(listOf("한나눔", "아리랑"), listOf(POS.NNP, POS.NNP)) // 별도 목록으로 등록

/***** 사전에 있는지 확인하기 *****/
Dictionary.contains("코알라NLP", setOf(POS.NNP, POS.NNG))
("코알라NLP" to POS.NNP) in Dictionary

/***** 다른 패키지 사전에서 단어 불러오기 *****/
Dictionary.importFrom(kr.bydelta.koala.eunjeon.Dictionary)

/***** 사전 항목 추출하기 *****/
Dictionary.items() // 사용자 사전 항목
Dictionary.getBaseEntries{ it.isNoun() } // 시스템 사전 항목
```

#### Scala
Reference: 
아리랑 [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.arirang/-dictionary/index.html),
은전한닢 [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.eunjeon/-dictionary/index.html),
한나눔 [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.hnn/-dictionary/index.html),
꼬꼬마 [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.kkma/-dictionary/index.html),
코모란 [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.kmr/-dictionary/index.html),
OpenKoreanText [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.okt/-dictionary/index.html)

```scala
import kr.bydelta.koala.kkma.Dictionary // 또는 다른 패키지 사용
import kr.bydelta.koala.POS

/***** 사전에 등록하기 *****/
Dictionary.addUserDictionary("코알라NLP", POS.NNP) // 1개 등록
Dictionary.addUserDictionary("코모란" -> POS.NNP, "은전한닢" -> POS.NNP) // 2개 이상 등록
Dictionary.addUserDictionary(Seq("한나눔", "아리랑"), Seq(POS.NNP, POS.NNP)) // 별도 목록으로 등록

/***** 사전에 있는지 확인하기 *****/
Dictionary.contains("코알라NLP", Set(POS.NNP, POS.NNG))
Dictionary.contains("코알라NLP" -> POS.NNP)

/***** 다른 패키지 사전에서 단어 불러오기 *****/
Dictionary.importFrom(kr.bydelta.koala.eunjeon.Dictionary)

/***** 사전 항목 추출하기 *****/
Dictionary.items() // 사용자 사전 항목
Dictionary.getBaseEntries(_.isNoun) // 시스템 사전 항목
```

#### Java
Reference: 
아리랑 [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.arirang/-dictionary/index.html),
은전한닢 [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.eunjeon/-dictionary/index.html),
한나눔 [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.hnn/-dictionary/index.html),
꼬꼬마 [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.kkma/-dictionary/index.html),
코모란 [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.kmr/-dictionary/index.html),
OpenKoreanText [Dictionary](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala.okt/-dictionary/index.html)

```java
import kr.bydelta.koala.kkma.Dictionary; // 또는 다른 패키지 사용
import kr.bydelta.koala.POS;
import kotlin.Pair;

/***** 사전에 등록하기 *****/
Dictionary.addUserDictionary("코알라NLP", POS.NNP); // 1개 등록
Dictionary.addUserDictionary(new Pair("코모란", POS.NNP), new Pair("은전한닢", POS.NNP)); // 2개 이상 등록
Dictionary.addUserDictionary(surfaceList, posList); // 별도 목록으로 등록

/***** 사전에 있는지 확인하기 *****/
Set<POS> posSet = ...
Dictionary.contains("코알라NLP", posSet);
Dictionary.contains(Pair("코알라NLP", POS.NNP));

/***** 다른 패키지 사전에서 단어 불러오기 *****/
Dictionary.importFrom(kr.bydelta.koala.eunjeon.Dictionary.INSTANCE);

/***** 사전 항목 추출하기 *****/
Dictionary.items(); // 사용자 사전 항목
Dictionary.getBaseEntries(pos -> {
    return pos.isNoun();
}); // 시스템 사전 항목
```

#### JavaScript
Reference: [Dictionary](https://koalanlp.github.io/nodejs-support/module-koalanlp.Dictionary.html)
```javascript 1.7

let KDict = new Dictionary(API.KKMA);
/***** 사전에 등록하기 *****/
KDict.addUserDictionary("코알라NLP", POS.NNP); // 1개 등록
KDict.addUserDictionary(["코모란", POS.NNP], ["은전한닢", POS.NNP]); // 2개 이상 등록

/***** 사전에 있는지 확인하기 *****/
KDict.contains("코알라NLP", [POS.NNP, POS.NNG]);
KDict.contains(["코알라NLP", POS.NNP]);

/***** 다른 패키지 사전에서 단어 불러오기 *****/
let EDict = new Dictionary(API.EUNJEON)
KDict.importFrom(EDict);

/***** 사전 항목 추출하기 *****/
KDict.items(); // 사용자 사전 항목
KDict.getBaseEntries(POS.isNoun); // 시스템 사전 항목
```

#### Python
Reference: [Dictionary](https://koalanlp.github.io/python-support/build/html/koalanlp.api.html#koalanlp.api.Dictionary)
```python
from koalanlp import *

KDict = Dictionary(API.KKMA)
/***** 사전에 등록하기 *****/
KDict.addUserDictionary("코알라NLP", POS.NNP) // 1개 등록
KDict.addUserDictionary(("코모란", POS.NNP), ("은전한닢", POS.NNP)) // 2개 이상 등록

/***** 사전에 있는지 확인하기 *****/
KDict.contains("코알라NLP", {POS.NNP, POS.NNG})
KDict.contains(("코알라NLP", POS.NNP))

/***** 다른 패키지 사전에서 단어 불러오기 *****/
KDict.importFrom(Dictionary(API.EUNJEON))

/***** 사전 항목 추출하기 *****/
KDict.items() // 사용자 사전 항목
KDict.getBaseEntries(lambda pos: pos.isNoun()) // 시스템 사전 항목
```

--------

[목차로 이동](./index.md)

--------
