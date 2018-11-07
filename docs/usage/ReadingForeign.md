--------

[목차로 이동](./index.md)

--------

## 영문, 한문 음독하기

알파벳 또는 한문 표기를 읽을 수 있습니다. 한문은 국사편찬위원회의 한자 음가사전과 몇가지 규칙에 따라 한글 음차로 변환합니다.

* `koalanlp-core` 패키지만 있어도 가능합니다.

### Kotlin
Reference: [CharSequence Extension](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala/kotlin.-char-sequence/index.html)

```kotlin
import kr.bydelta.koala.*

"ABCD".alphaToHangul() // "에이비씨디"
"갤럭시S".alphaToHangul() // "갤럭시에스"
"cup".alphaToHangul() // "씨유피"

"에스비에스".isAlphaPronounced() // true
"갤럭시에스".isAlphaPronounced() // false

"갤럭시에스".hangulToAlpha() // "갤럭시S"
"메이디에이치디".hangulToAlpha() // "ADHD"

'國'.isHanja() // true
'國'.isCJKHanja() // true

"國篇".hanjaToHangul() // "국편"
"國篇은 오늘".hanjaToHangul() // "국편은 오늘"
"300 兩의 돈".hanjaToHangul() // "300 냥의 돈"
"樂園".hanjaToHangul() // "낙원" (두음법칙)
```

#### Scala
Reference: [CharSequence Extension](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala/kotlin.-char-sequence/index.html)

* [koalanlp-scala](https://koalanlp.github.io/scala-support)가 dependency로 포함되었다고 가정합니다.

```scala
import kr.bydelta.koala._
import kr.bydelta.koala.Implicits._

"ABCD".alphaToHangul // "에이비씨디"
"갤럭시S".alphaToHangul // "갤럭시에스"
"cup".alphaToHangul // "씨유피"

"에스비에스".isAlphaPronounced // true
"갤럭시에스".isAlphaPronounced // false

"갤럭시에스".hangulToAlpha // "갤럭시S"
"메이디에이치디".hangulToAlpha // "ADHD"

'國'.isHanja // true
'國'.isCJKHanja // true

"國篇".hanjaToHangul() // "국편"
"國篇은 오늘".hanjaToHangul() // "국편은 오늘"
"300 兩의 돈".hanjaToHangul() // "300 냥의 돈"
"樂園".hanjaToHangul() // "낙원" (두음법칙)
```

#### Java
Reference: [CharSequence Extension](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala/kotlin.-char-sequence/index.html)

```java
import kr.bydelta.koala.ExtUtil;

ExtUtil.alphaToHangul("ABCD"); // "에이비씨디"
ExtUtil.alphaToHangul("갤럭시S"); // "갤럭시에스"
ExtUtil.alphaToHangul("cup"); // "씨유피"

ExtUtil.isAlphaPronounced("에스비에스"); // true
ExtUtil.isAlphaPronounced("갤럭시에스"); // false

ExtUtil.hangulToAlpha("갤럭시에스"); // "갤럭시S"
ExtUtil.hangulToAlpha("메이디에이치디"); // "ADHD"

ExtUtil.isHanja('國'); // true
ExtUtil.isCJKHanja('國'); // true

ExtUtil.hanjaToHangul("國篇"); // "국편"
ExtUtil.hanjaToHangul("國篇은 오늘"); // "국편은 오늘"
ExtUtil.hanjaToHangul("300 兩의 돈"); // "300 냥의 돈"
ExtUtil.hanjaToHangul("樂園"); // "낙원" (두음법칙)
```

#### NodeJS (구현중)
Reference: [ExtUtil](https://koalanlp.github.io/nodejs-support/module-koalanlp.ExtUtil.html)

```javascript
let ExtUtil = koalanlp.ExtUtil;

ExtUtil.alphaToHangul("ABCD"); // "에이비씨디"
ExtUtil.alphaToHangul("갤럭시S"); // "갤럭시에스"
ExtUtil.alphaToHangul("cup"); // "씨유피"

ExtUtil.isAlphaPronounced("에스비에스"); // true
ExtUtil.isAlphaPronounced("갤럭시에스"); // false

ExtUtil.hangulToAlpha("갤럭시에스"); // "갤럭시S"
ExtUtil.hangulToAlpha("메이디에이치디"); // "ADHD"

ExtUtil.isHanja('國'); // true
ExtUtil.isCJKHanja('國'); // true

ExtUtil.hanjaToHangul("國篇"); // "국편"
ExtUtil.hanjaToHangul("國篇은 오늘"); // "국편은 오늘"
ExtUtil.hanjaToHangul("300 兩의 돈"); // "300 냥의 돈"
ExtUtil.hanjaToHangul("樂園"); // "낙원" (두음법칙)
```

#### Python 3 (구현중)
Reference: [ExtUtil](https://koalanlp.github.io/python-support/build/html/koalanlp.api.html#koalanlp.api.ExtUtil)

```python
from koalanlp import *

ExtUtil.alphaToHangul("ABCD") # "에이비씨디"
ExtUtil.alphaToHangul("갤럭시S") # "갤럭시에스"
ExtUtil.alphaToHangul("cup") # "씨유피"

ExtUtil.isAlphaPronounced("에스비에스") # true
ExtUtil.isAlphaPronounced("갤럭시에스") # false

ExtUtil.hangulToAlpha("갤럭시에스") # "갤럭시S"
ExtUtil.hangulToAlpha("메이디에이치디") # "ADHD"

ExtUtil.isHanja('國') # true
ExtUtil.isCJKHanja('國') # true

ExtUtil.hanjaToHangul("國篇") # "국편"
ExtUtil.hanjaToHangul("國篇은 오늘") # "국편은 오늘"
ExtUtil.hanjaToHangul("300 兩의 돈") # "300 냥의 돈"
ExtUtil.hanjaToHangul("樂園") # "낙원" (두음법칙)
```

--------

[목차로 이동](./index.md)

--------
