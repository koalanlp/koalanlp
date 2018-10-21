KoalaNLP
==============
[![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-core.svg?style=flat-square&label=release)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-core%22)
[![KotlinDoc](https://img.shields.io/badge/doc-Kotlin-blue.svg?style=flat-square)](http://koalanlp.github.io/KoalaNLP-core/api/index.html)
[![분석기별 품사비교표](https://img.shields.io/badge/%ED%92%88%EC%82%AC-%EB%B9%84%EA%B5%90%ED%91%9C-blue.svg?style=flat-square)](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)

[![Build Status](https://img.shields.io/travis/koalanlp/KoalaNLP-core.svg?style=flat-square&branch=master)](https://travis-ci.org/koalanlp/KoalaNLP-core)
[![codecov](https://img.shields.io/codecov/c/github/koalanlp/KoalaNLP-core.svg?style=flat-square)](https://codecov.io/gh/koalanlp/KoalaNLP-core)
[![Known Vulnerabilities](https://snyk.io/test/github/koalanlp/KoalaNLP-core/badge.svg?style=flat-square)](https://snyk.io/test/github/koalanlp/KoalaNLP-core)

[![MIT License](https://img.shields.io/badge/license-MIT-green.svg?style=flat-square)](https://tldrlegal.com/license/mit-license)
[![nodejs-koalanlp](https://img.shields.io/badge/Nodejs-KoalaNLP-blue.svg?style=flat-square)](https://koalanlp.github.io/nodejs-koalanlp)
[![py-koalanlp](https://img.shields.io/badge/Python-KoalaNLP-blue.svg?style=flat-square)](https://koalanlp.github.io/py-koalanlp)

# 소개
한국어 형태소 및 구문 분석기의 모음입니다.

이 프로젝트는 __서로 다른 형태의 형태소 분석기를__ 모아,
__동일한 인터페이스__ 아래에서 사용할 수 있도록 하는 것이 목적입니다.
* KAIST의 [한나눔 형태소 분석기](http://kldp.net/projects/hannanum/)와 [NLP_HUB 구문분석기](http://semanticweb.kaist.ac.kr/home/index.php/NLP_HUB)
* 서울대의 [꼬꼬마 형태소/구문 분석기 v2.1](http://kkma.snu.ac.kr/documents/index.jsp)
* Junsoo Shin님의 [코모란 v3.3.3](https://github.com/shin285/KOMORAN)
* OpenKoreanText의 [오픈 소스 한국어 처리기 v2.2.0](http://openkoreantext.org) (구 Twitter 한국어 분석기)
* 은전한닢 프로젝트의 [SEunjeon(S은전)](https://bitbucket.org/eunjeon/seunjeon)
* 이수명님의 [Arirang Morpheme Analyzer](http://cafe.naver.com/korlucene) <sup>1-1</sup>
* 최석재님의 [RHINO v2.5.4](https://github.com/SukjaeChoi/RHINO)
* **업데이트 준비중**
  * ETRI Open API Access
  * Daon 분석기

> <sup>주1-1</sup> Arirang 분석기의 출력을 형태소분석에 적합하게 조금 다듬었으므로, 원본과 약간 다른 결과를 낼 수도 있습니다.

분석기의 개선이나 추가 등을 하고 싶으시다면,
* 개발이 직접 가능하시다면 pull request를 보내주세요. 테스트 후 반영할 수 있도록 하겠습니다.
* 개발이 어렵다면 issue tracker에 등록해주세요. 검토 후 답변해드리겠습니다.

## 참고
모든 코드는 Kotlin으로 작성되어 있습니다. Java나 Scala에 비해서 유지 보수에 편리하다고 판단하여 변경하였습니다.
* Kotlin의 사용법은 [Try Kotlin (공식)](https://try.kotlinlang.org/)에서 학습하실 수 있습니다.
* 모든 코드는 Java와 Kotlin에서 완벽히 상호 호환됩니다.
* 더불어 기존 1.x의 Scala 사용을 지원하기 위해서 `koalanlp-scala`를 통해 Implicit 변환을 지원합니다. 

# Dependency 추가

## 패키지 목록
| 패키지명 | 버전 | 설명 | 품사분석 | 구문분석 | 의존구문 | 개체명 | 의미역 |
| ------- | ---- |---- | ------- | ------- | ------- | ----- | ------ |
| `koalanlp-core` | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-core.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-core%22) | 통합 인터페이스의 정의가 등재된 중심 묶음입니다. | | | | | |
| `koalanlp-scala` | (준비중) | Scala를 위한 편의기능 (Implicit conversion 등) | | | | | |
| `koalanlp-hnn` | (2.x 개발중) | 한나눔 분석기 패키지입니다. <sup>2-1</sup> | 가능 | 가능 | 가능 | | |
| `koalanlp-kkma` | (2.x 개발중) | 꼬꼬마 분석기 패키지입니다. <sup>2-1</sup> | 가능 | | 가능 | | |
| `koalanlp-kmr` | (2.x 개발중) | 코모란 분석기 패키지입니다. | 가능 | | | | |
| `koalanlp-okt` | (2.x 개발중) | OpenKoreanText (구 트위터) 분석기 패키지입니다. | 가능 | | | | |
| `koalanlp-eunjeon` | (2.x 개발중) | 은전한닢 분석기 패키지입니다. | 가능 | | | | |
| `koalanlp-arirang` | (2.x 개발중) | 아리랑 분석기 패키지입니다.<sup>2-1</sup> | 가능 | | | | |
| `koalanlp-rhino` | (2.x 개발중) | RHINO 분석기 패키지입니다.<sup>2-1</sup> | 가능 | | | | |
| `koalanlp-etri` | (준비중) | ETRI Open API 연결 패키지입니다. | 가능 | | 가능 | 가능 | 가능 |
| `koalanlp-daon` | (준비중) | Daon 검색엔진 기반의 분석 패키지입니다. | 가능 | | | | |
| `koalanlp-server` | (2.x 개발중) | HTTP 서비스 구성을 위한 패키지입니다. | | | | | |

> <sup>주2-1</sup> 꼬꼬마, 한나눔, 아리랑, RHINO 분석기는 타 분석기와 달리 Maven repository에 등재되어 있지 않아, 원래는 수동으로 직접 추가하셔야 합니다.
> 이 점이 불편하다는 것을 알기에, KoalaNLP는 assembly 형태로 해당 패키지를 포함하여 배포하고 있습니다. 포함된 패키지를 사용하려면, `assembly` classifier를 사용하십시오.
> "assembly" classifier가 지정되지 않으면, 각 분석기 라이브러리가 빠진 채로 dependency가 참조됩니다.

## 실행환경
* KoalaNLP의 모든 코드는 Java 8을 기준으로 컴파일되고 테스트 되었습니다.
* Kotlin의 경우는 1.2.71이 기준입니다.
* Scala의 경우는 `KoalaNLP-scala` 패키지의 버전에 따릅니다.
  단, 일부 의존 패키지가 Scala에서 개발되어 제한이 있을 수 있으며, 이는 [koalanlp/scala](https://koalanlp.github.io/scala-support)를 참조하십시오.  

## Gradle
```groovy
ext.koala_version = '2.0.0'

dependencies{
    // 한나눔의 경우
    implementation "kr.bydelta:koalanlp-hnn:${ext.koala_version}:assembly" 
    // 꼬꼬마의 경우
    implementation "kr.bydelta:koalanlp-kkma:${ext.koala_version}:assembly"
    // 코모란의 경우
    implementation "kr.bydelta:koalanlp-kmr:${ext.koala_version}" 
    // OpenKoreanText의 경우
    implementation "kr.bydelta:koalanlp-okt:${ext.koala_version}" 
    // 은전한닢 프로젝트(Mecab-ko)의 경우
    implementation "kr.bydelta:koalanlp-eunjeon:${ext.koala_version}"
    // 아리랑의 경우
    implementation "kr.bydelta:koalanlp-arirang:${ext.koala_version}:assembly"
    // RHINO의 경우 
    implementation "kr.bydelta:koalanlp-rhino:${ext.koala_version}:assembly"
    // ETRI Open API의 경우 (준비중)
    implementation "kr.bydelta:koalanlp-etri:${ext.koala_version}"
    // Daon의 경우 (준비중)
    implementation "kr.bydelta:koalanlp-daon:${ext.koala_version}"
}
```

## SBT
(버전은 Latest Release 기준입니다. SNAPSHOT을 사용하시려면, `latest.integration`을 사용하세요.)
```sbtshell
val koalaVer = "2.0.0"

// 한나눔 분석기의 경우
libraryDependencies += "kr.bydelta" % "koalanlp-hannanum" % koalaVer classifier "assembly"

// 꼬꼬마 분석기의 경우
libraryDependencies += "kr.bydelta" % "koalanlp-kkma" % koalaVer classifier "assembly"

// 코모란 분석기의 경우
resolvers += "jitpack" at "https://jitpack.io/"
libraryDependencies += "kr.bydelta" % "koalanlp-kmr" % koalaVer

// Open Korean Text 분석기의 경우
libraryDependencies += "kr.bydelta" % "koalanlp-okt" % koalaVer

// 은전한닢 분석기의 경우
libraryDependencies += "kr.bydelta" % "koalanlp-eunjeon" % koalaVer

// 아리랑 분석기의 경우
libraryDependencies += "kr.bydelta" % "koalanlp-arirang" % koalaVer classifier "assembly"

// RHINO 분석기의 경우
libraryDependencies += "kr.bydelta" % "koalanlp-rhino" % koalaVer classifier "assembly"

// ETRI 분석기의 경우 (준비중)
libraryDependencies += "kr.bydelta" % "koalanlp-etri" % koalaVer

// Daon 분석기의 경우 (준비중)
libraryDependencies += "kr.bydelta" % "koalanlp-daon" % koalaVer
```

## Maven
Maven을 사용하시는 경우, 다음과 같습니다. `${TAGGER_PACK}`위치에는 원하는 품사분석기의 패키지를 써주시고, `${TAGGER_VER}`위치에는 품사분석기의 버전을 써주세요.
```xml
<dependency>
  <groupId>kr.bydelta</groupId>
  <artifactId>koalanlp-${TAGGER.PACK}</artifactId>
  <version>${TAGGER_VER}</version>
</dependency>
```

Classifier를 추가하실 경우, `<artifactId>`다음 행에 다음 코드를 추가하세요.
```xml
  <classifier>assembly</classifier>
```

예를 들어서, 꼬꼬마 분석기(koalanlp-kkma) 버전 2.0.0를 추가하고자 한다면, 아래와 같습니다.
```xml
<dependency>
  <groupId>kr.bydelta</groupId>
  <artifactId>koalanlp-kkma</artifactId>
  <classifier>assembly</classifier>
  <version>2.0.0</version>
</dependency>
```

# 사용방법
아래에는 대표적인 특징만 기술되어 있습니다.

상세한 사항은 [Usage](https://koalanlp.github.io/koalanlp/usage/) 또는 [![KotlinDoc](http://javadoc-badge.appspot.com/kr.bydelta/koalanlp-core.svg?label=KotlinDoc&style=flat-square)](http://koalanlp.github.io/koalanlp/api/)을 참고하십시오.

## 특징

1. 복잡한 설정이 필요없는 텍스트 분석:
   모델은 자동으로 Maven으로 배포되기 때문에, 각 모델을 별도로 설치할 필요가 없습니다.

2. 코드 2~3 줄로 수행하는 텍스트 처리:
   모델마다 다른 복잡한 설정 과정, 초기화 과정은 필요하지 않습니다. Dependency에 추가하고, 객체를 생성하고, 분석 메소드를 호출하면 모든 것이 끝납니다.

3. 모델에 상관 없는 동일한 코드, 동일한 결과:
   모델마다 실행 방법, 실행 결과를 표현하는 형태가 다릅니다. KoalaNLP는 이를 정부 및 관계기관의 표준안에 따라 표준화합니다. 따라서 모델에 독립적으로 응용 프로그램 개발이 가능합니다.

4. Java, Kotlin, Scala, Python 3, NodeJS에서 크게 다르지 않은 코드 사용
   KoalaNLP는 여러 프로그래밍 언어에서 찾을 수 있습니다. 어디서 개발을 하더라도 크게 코드가 다르지 않습니다. 

## 여러 패키지의 사용
통합 인터페이스는 여러 패키지간의 호환이 가능하게 설계되어 있습니다. 이론적으로는 타 패키지의 품사 분석 결과를 토대로 구문 분석이 가능합니다.
> __Note:__
> * 본 분석의 결과는 검증되지 않았습니다.
> * 신조어 등으로 인해 한나눔이나 꼬꼬마에서 품사 분석이 제대로 수행되지 않을 경우를 위한 기능입니다.
> * 사용자 정의 사전은 `Tagger`와 `Parser`의 대상이 되는 패키지에 모두에 추가하여야 합니다.

### Kotlin
```kotlin
/* 패키지 명: 한나눔(hnn), 코모란(kmr), 꼬꼬마(kkma), 은전한닢(eunjeon), 트위터(twt), 아리랑(arirang) */
// 예시에서는 트위터 문장분석기, 은전한닢 품사 분석, 꼬꼬마 구문 분석을 진행함.
import kr.bydelta.koala.twt.SentenceSplitter
import kr.bydelta.koala.eunjeon.Tagger
import kr.bydelta.koala.kkma.Parser

val splitter = SentenceSplitter()
val tagger = Tagger()
val parser = Parser()

val paragraph = "누군가가 말했다. Python에는 KoNLPy가 있다. Kotlin은 KoalaNLP가 있다."
val sentences = splitter(paragraph)
val tagged = sentences.map{ tagger.tagSentence(it) }
val parsed = tagged.map{ parser.parse(it) }
```

### Scala
```scala
import kr.bydelta.koala.twt.SentenceSplitter
import kr.bydelta.koala.eunjeon.Tagger
import kr.bydelta.koala.kkma.Parser

val splitter = new SentenceSplitter
val tagger = new Tagger
val parser = new Parser

val paragraph = "누군가가 말했다. Python에는 KoNLPy가 있다. Scala는 KoalaNLP가 있었다."
val sentences = splitter.invoke(paragraph)
val tagged = sentences.map(tagger.tagSentence)
val parsed = tagged.map(parser.parse)
```

### Java
```java
import kr.bydelta.koala.twt.SentenceSplitter;
import kr.bydelta.koala.eunjeon.Tagger;
import kr.bydelta.koala.kkma.Parser;
import kr.bydelta.koala.Sentence;

SentenceSplitter splitter = new SentenceSplitter();
Tagger tagger = new Tagger();
Tagger parser = new Parser();

String paragraph = "누군가가 말했다. Python에는 KoNLPy가 있다. Java는 KoalaNLP가 있었다.";
List<String> sentences = splitter.invoke(paragraph);
for(String line : sentences){
  Sentence tagged = tagger.tagSentence(line);
  Sentence parsed = parser.parse(tagged);
}
```

# License 조항
이 프로젝트 자체(KoalaNLP-core)와 인터페이스 통합을 위한 코드는 v1.8.0부터 [*MIT License*](https://tldrlegal.com/license/mit-license)을 따르며,
각 분석기의 License와 저작권은 각 프로젝트에서 지정한 바를 따릅니다. (`kr.bydelta.koala.helper` 하위에 새로 수정되어 등록된 Class/Object는 각 프로젝트의 결과물을 조금 수정한 판본이며, 저작권은 각 프로젝트에 귀속됩니다.)
* Hannanum: [GPL v3](https://tldrlegal.com/license/gnu-general-public-license-v3-(gpl-3))
* KKMA: [GPL v2](https://tldrlegal.com/license/gnu-general-public-license-v2) (GPL v2를 따르지 않더라도, 상업적 이용시 별도 협의 가능)
* KOMORAN: [Apache License 2.0](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0))
* Twitter: [Apache License 2.0](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0))
* Eunjeon: [Apache License 2.0](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0))
* Arirang: [Apache License 2.0](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0))
* RHINO: 비상업적 용도 사용가능.

# 결과 비교
[Wiki:결과비교](https://github.com/koalanlp/KoalaNLP-core/wiki/4.-결과-비교)를 참조해주세요.
