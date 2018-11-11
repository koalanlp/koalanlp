--------

[목차로 이동](./index.md)

--------

## 한글 분해/조립하기

한글을 초성, 중성, 종성으로 분해하거나, 초성, 중성, 종성 문자를 한글로 조합할 수 있습니다.

* `koalanlp-core` 패키지만 있어도 가능합니다.

### Kotlin
Reference: [Char Extension](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala/kotlin.-char/index.html),
[CharSequence Extension](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala/kotlin.-char-sequence/index.html),
[Triple Extension](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala/kotlin.-triple/index.html)

```kotlin
import kr.bydelta.koala.*

/**** 한글 여부 확인 ****/
'가'.isHangul() // true
'ㄱ'.isHangul() // true
'a'.isHangul() // false

"갤럭시S는".isHangulEnding() // true
"abc".isHangulEnding() // false
"보기 ㄱ".isHangulEnding() // true

'가'.isCompleteHangul() // true
'ㄱ'.isCompleteHangul() // false
'a'.isCompleteHangul() // false

/**** 종성으로 끝나는지 확인 ****/
'각'.isJongsungEnding() // true
'가'.isJongsungEnding() // false
'm'.isJongsungEnding() // false

/**** 초, 중, 종성 한글 자모 값으로 분해 ****/
'가'.getChosung() // 'ㄱ'에 해당하는 초성 문자 \u1100
'가'.getJungsung() // 'ㅏ'에 해당하는 초성 문자 \u1161
'가'.getJongsung() // null
'각'.getJongsung() // 'ㄱ'에 해당하는 종성문자 \u11A8
'ㄱ'.getChosung() // null
'가'.dissembleHangul() // Triple('\u1100', '\u1161', null)
'각'.dissembleHangul() // Triple('\u1100', '\u1161', '\u11A8')
"가각".dissembleHangul() // "\u1100\u1161\u1100\u1161\u11A8" (문자 사이를 띄워서 표시하면: 'ᄀ ᅡ ᄀ ᅡ ᆨ')

/**** 종성 한글 자모값으로 변환 ****/
ChoToJong['ㄵ'] // \u11AC

/**** 한글 자모 범위에 속하는 초, 중, 종성인지 확인 ****/
'ㄱ'.isChosungJamo() // false
'\u1100'.isChosungJamo() // true
'ㅏ'.isJungsungJamo() // false
'\u1161'.isJungsungJamo() // true
'ㄱ'.isJongsungJamo() // false
'\u11A8'.isJongsungJamo() // true

/**** 한글 자모 결합 ****/
Triple('\u1100', '\u1161', null as Char?).assembleHangul() // '가'
Triple('\u1100', '\u1161', '\u11A9').assembleHangul() // '간'
"\u1100\u1161\u1101\u1161\u11a9".assembleHangul() // "가난"
assembleHangul(jung = '\u1161') // '아'
assembleHangul(cho = '\u1100', jong = '\u11A8') // '윽'
```

#### Scala
Reference: [Char Extension](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala/kotlin.-char/index.html),
[CharSequence Extension](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala/kotlin.-char-sequence/index.html),
[Triple Extension](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala/kotlin.-triple/index.html)

* [koalanlp-scala](https://koalanlp.github.io/scala-support)가 dependency로 포함되었다고 가정합니다.

```scala
import kr.bydelta.koala._
import kr.bydelta.koala.Implicits._

/**** 한글 여부 확인 ****/
'가'.isHangul // true
'ㄱ'.isHangul // true
'a'.isHangul // false

"갤럭시S는".isHangulEnding // true
"abc".isHangulEnding // false
"보기 ㄱ".isHangulEnding // true

'가'.isCompleteHangul // true
'ㄱ'.isCompleteHangul // false
'a'.isCompleteHangul // false

/**** 종성으로 끝나는지 확인 ****/
'각'.isJongsungEnding // true
'가'.isJongsungEnding // false
'm'.isJongsungEnding // false

/**** 초, 중, 종성 한글 자모 값으로 분해 ****/
'가'.getChosung // Some('ㄱ'에 해당하는 초성 문자 \u1100)
'가'.getJungsung // Some('ㅏ'에 해당하는 초성 문자 \u1161)
'가'.getJongsung // None
'각'.getJongsung // Some('ㄱ'에 해당하는 종성문자 \u11A8)
'ㄱ'.getChosung // None
'가'.dissembleHangul // ('\u1100', '\u1161', None)
'각'.dissembleHangul // ('\u1100', '\u1161', Some('\u11A8'))
"가각".dissembleHangul // "\u1100\u1161\u1100\u1161\u11A8" (문자 사이를 띄워서 표시하면: 'ᄀ ᅡ ᄀ ᅡ ᆨ')

/**** 종성 한글 자모값으로 변환 ****/
ExtUtil.ChoToJong('ㄵ') // \u11AC

/**** 한글 자모 범위에 속하는 초, 중, 종성인지 확인 ****/
'ㄱ'.isChosungJamo // false
'\u1100'.isChosungJamo // true
'ㅏ'.isJungsungJamo // false
'\u1161'.isJungsungJamo // true
'ㄱ'.isJongsungJamo // false
'\u11A8'.isJongsungJamo // true

/**** 한글 자모 결합 ****/
('\u1100', '\u1161', None).assembleHangul() // '가'
('\u1100', '\u1161', '\u11A9').assembleHangul() // '간'
"\u1100\u1161\u1101\u1161\u11a9".assembleHangul() // "가난"
ExtUtil.assembleHangul(null, '\u1161') // '아'
ExtUtil.assembleHangul('\u1100', null, '\u11A8') // '윽'
```

#### Java
Reference: [Char Extension](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala/kotlin.-char/index.html),
[CharSequence Extension](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala/kotlin.-char-sequence/index.html),
[Triple Extension](https://koalanlp.github.io/koalanlp/api/koalanlp/kr.bydelta.koala/kotlin.-triple/index.html)

```java
import kr.bydelta.koala.ExtUtil;

/**** 한글 여부 확인 ****/
ExtUtil.isHangul('가'); // true
ExtUtil.isHangul('ㄱ'); // true
ExtUtil.isHangul('a'); // false

ExtUtil.isHangulEnding("갤럭시S는"); // true
ExtUtil.isHangulEnding("abc"); // false
ExtUtil.isHangulEnding("보기 ㄱ"); // true

ExtUtil.isCompleteHangul('가'); // true
ExtUtil.isCompleteHangul('ㄱ'); // false
ExtUtil.isCompleteHangul('a'); // false

/**** 종성으로 끝나는지 확인 ****/
ExtUtil.isJongsungEnding('각'); // true
ExtUtil.isJongsungEnding('가'); // false
ExtUtil.isJongsungEnding('m'); // false

/**** 초, 중, 종성 한글 자모 값으로 분해 ****/
ExtUtil.getChosung('가'); // 'ㄱ'에 해당하는 초성 문자 \u1100
ExtUtil.getJungsung('가'); // 'ㅏ'에 해당하는 초성 문자 \u1161
ExtUtil.getJongsung('가'); // null
ExtUtil.getJongsung('각'); // 'ㄱ'에 해당하는 종성문자 \u11A8
ExtUtil.getChosung('ㄱ'); // null
ExtUtil.dissembleHangul('가'); // kotlin.Triple('\u1100', '\u1161', null)
ExtUtil.dissembleHangul('각'); // kotlin.Triple('\u1100', '\u1161', '\u11A8')
ExtUtil.dissembleHangul("가각"); // "\u1100\u1161\u1100\u1161\u11A8" (문자 사이를 띄워서 표시하면: 'ᄀ ᅡ ᄀ ᅡ ᆨ')

/**** 종성 한글 자모값으로 변환 ****/
ChoToJong.get('ㄵ'); // \u11AC

/**** 한글 자모 범위에 속하는 초, 중, 종성인지 확인 ****/
ExtUtil.isChosungJamo('ㄱ'); // false
ExtUtil.isChosungJamo('\u1100'); // true
ExtUtil.isJungsungJamo('ㅏ'); // false
ExtUtil.isJungsungJamo('\u1161'); // true
ExtUtil.isJongsungJamo('ㄱ'); // false
ExtUtil.isJongsungJamo('\u11A8'); // true

/**** 한글 자모 결합 ****/
ExtUtil.assembleHangul(new kotlin.Triple<Character, Character, Character>('\u1100', '\u1161', null)); // '가'
ExtUtil.assembleHangul(new kotlin.Triple('\u1100', '\u1161', '\u11A9')); // '간'
ExtUtil.assembleHangul("\u1100\u1161\u1101\u1161\u11a9"); // "가난"
ExtUtil.assembleHangul(null, '\u1161'); // '아'
ExtUtil.assembleHangul('\u1100', null, '\u11A8'); // '윽'
```

#### NodeJS (구현중)
Reference: [ExtUtil](https://koalanlp.github.io/nodejs-support/module-koalanlp.ExtUtil.html)

```javascript
let ExtUtil = koalanlp.ExtUtil;

/**** 한글 여부 확인 ****/
ExtUtil.isHangul('가'); // true
ExtUtil.isHangul('ㄱ'); // true
ExtUtil.isHangul('a'); // false

ExtUtil.isHangulEnding("갤럭시S는"); // true
ExtUtil.isHangulEnding("abc"); // false
ExtUtil.isHangulEnding("보기 ㄱ"); // true

ExtUtil.isCompleteHangul('가'); // true
ExtUtil.isCompleteHangul('ㄱ'); // false
ExtUtil.isCompleteHangul('a'); // false

/**** 종성으로 끝나는지 확인 ****/
ExtUtil.isJongsungEnding('각'); // true
ExtUtil.isJongsungEnding('가'); // false
ExtUtil.isJongsungEnding('m'); // false

/**** 초, 중, 종성 한글 자모 값으로 분해 ****/
ExtUtil.getChosung('가'); // 'ㄱ'에 해당하는 초성 문자 \u1100
ExtUtil.getJungsung('가'); // 'ㅏ'에 해당하는 초성 문자 \u1161
ExtUtil.getJongsung('가'); // null
ExtUtil.getJongsung('각'); // 'ㄱ'에 해당하는 종성문자 \u11A8
ExtUtil.getChosung('ㄱ'); // null
ExtUtil.dissembleHangul('가'); // ['\u1100', '\u1161', null]
ExtUtil.dissembleHangul('각'); // ['\u1100', '\u1161', '\u11A8']
ExtUtil.dissembleHangul("가각"); // "\u1100\u1161\u1100\u1161\u11A8" (문자 사이를 띄워서 표시하면: 'ᄀ ᅡ ᄀ ᅡ ᆨ')

/**** 종성 한글 자모값으로 변환 ****/
ChoToJong.get('ㄵ'); // \u11AC

/**** 한글 자모 범위에 속하는 초, 중, 종성인지 확인 ****/
ExtUtil.isChosungJamo('ㄱ'); // false
ExtUtil.isChosungJamo('\u1100'); // true
ExtUtil.isJungsungJamo('ㅏ'); // false
ExtUtil.isJungsungJamo('\u1161'); // true
ExtUtil.isJongsungJamo('ㄱ'); // false
ExtUtil.isJongsungJamo('\u11A8'); // true

/**** 한글 자모 결합 ****/
ExtUtil.assembleHangul("\u1100\u1161\u1101\u1161\u11a9"); // "가난"
ExtUtil.assembleHangul(null, '\u1161'); // '아'
ExtUtil.assembleHangul('\u1100', null, '\u11A8'); // '윽'
```

#### Python 3
Reference: [ExtUtil](https://koalanlp.github.io/python-support/html/koalanlp.html#module-koalanlp.ExtUtil)

```python
from koalanlp import ExtUtil

#### 한글 여부 확인 ####
ExtUtil.isHangul('가') # [true]
ExtUtil.isHangul('ㄱ') # [true]
ExtUtil.isHangul('a나.') # [false, true, false]

ExtUtil.isHangulEnding("갤럭시S는") # true
ExtUtil.isHangulEnding("abc") # false
ExtUtil.isHangulEnding("보기 ㄱ") # true

ExtUtil.isCompleteHangul('가') # [true]
ExtUtil.isCompleteHangul('ㄱ') # [false]
ExtUtil.isCompleteHangul('a나.') # [false, true, false]

#### 종성으로 끝나는지 확인 ####
ExtUtil.isJongsungEnding('각') # true
ExtUtil.isJongsungEnding('가') # false
ExtUtil.isJongsungEnding('m') # false

#### 초, 중, 종성 한글 자모 값으로 분해 ####
ExtUtil.getChosung('가') # ['ㄱ'에 해당하는 초성 문자 \u1100]
ExtUtil.getJungsung('가') # ['ㅏ'에 해당하는 초성 문자 \u1161]
ExtUtil.getJongsung('가') # [None]
ExtUtil.getJongsung('각') # ['ㄱ'에 해당하는 종성문자 \u11A8]
ExtUtil.getChosung('ㄱㄴㄷ') # [None, None, None]
ExtUtil.dissembleHangul('가') # '\u1100\u1161'
ExtUtil.dissembleHangul('각') # '\u1100\u1161\u11A8'
ExtUtil.dissembleHangul("가각") # "\u1100\u1161\u1100\u1161\u11A8" (문자 사이를 띄워서 표시하면: 'ᄀ ᅡ ᄀ ᅡ ᆨ')

#### 종성 한글 자모값으로 변환 ####
ExtUtil.ChoToJong()['ㄵ'] # \u11AC

#### 한글 자모 범위에 속하는 초, 중, 종성인지 확인 ####
ExtUtil.isChosungJamo('ㄱ') # [false]
ExtUtil.isChosungJamo('\u1100') # [true]
ExtUtil.isJungsungJamo('ㅏ') # [false]
ExtUtil.isJungsungJamo('\u1161') # [true]
ExtUtil.isJongsungJamo('ㄱab') # [false, false, false]
ExtUtil.isJongsungJamo('\u11A8,.') # [true, false, false]

#### 한글 자모 결합 ####
ExtUtil.assembleHangulTriple('\u1100', '\u1161', None) # '가'
ExtUtil.assembleHangulTriple('\u1100', '\u1161', '\u11A9') # '간'
ExtUtil.assembleHangulTriple(None, '\u1161') # '아'
ExtUtil.assembleHangulTriple('\u1100', None, '\u11A8') # '윽'
ExtUtil.assembleHangul("\u1100\u1161\u1101\u1161\u11a9") # "가난"
```

--------

[목차로 이동](./index.md)

--------
