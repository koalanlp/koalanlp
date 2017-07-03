KoalaNLP
==============
(구) KoreanAnalyzer

[![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-core_2.12.svg?style=flat-square&label=release)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-core_2.12%22)
[![ScalaDoc](https://img.shields.io/badge/doc-Scala-red.svg?style=flat-square)](http://nearbydelta.github.io/KoalaNLP/api/scala/kr/bydelta/koala/index.html)
[![JavaDoc](https://img.shields.io/badge/doc-Java-blue.svg?style=flat-square)](http://nearbydelta.github.io/KoalaNLP/api/java/index.html)
[![분석기별 품사비교표](https://img.shields.io/badge/%ED%92%88%EC%82%AC-%EB%B9%84%EA%B5%90%ED%91%9C-blue.svg?style=flat-square)](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)

[![Build Status](https://img.shields.io/travis/nearbydelta/KoalaNLP.svg?style=flat-square&branch=master)](https://travis-ci.org/nearbydelta/KoalaNLP)
[![codecov](https://img.shields.io/codecov/c/github/nearbydelta/KoalaNLP.svg?style=flat-square)](https://codecov.io/gh/nearbydelta/KoalaNLP)
[![Dependency Status](https://www.versioneye.com/user/projects/58fadf836ac171431cf95062/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/58fadf836ac171431cf95062)

[![GPL Licence](https://img.shields.io/badge/license-GPL%20v3-green.svg?style=flat-square)](https://opensource.org/licenses/GPL-3.0/)
[![Gitter](https://img.shields.io/gitter/room/nearbydelta/KoalaNLP.svg?style=flat-square)](https://gitter.im/nearbydelta/KoalaNLP?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

# 소개
한국어 형태소 및 구문 분석기의 모음입니다.

이 프로젝트는 __서로 다른 형태의 형태소 분석기를__ 모아,
__동일한 인터페이스__ 아래에서 사용할 수 있도록 하는 것이 목적입니다.
* Hannanum: KAIST의 [한나눔 형태소 분석기](http://kldp.net/projects/hannanum/)와 [NLP_HUB 구문분석기](http://semanticweb.kaist.ac.kr/home/index.php/NLP_HUB)
* KKMA: 서울대의 [꼬꼬마 형태소/구문 분석기](http://kkma.snu.ac.kr/documents/index.jsp)
* Komoran: Shineware의 [코모란 v2.4](http://shineware.tistory.com/entry/KOMORAN-ver-24)
* Twitter: OpenKoreanText의 [오픈 소스 한국어 처리기](http://openkoreantext.org) (구 Twitter 한국어 분석기)<sup>1</sup>
* Eunjeon: 은전한닢 프로젝트의 [SEunjeon(S은전)](https://bitbucket.org/eunjeon/seunjeon)

> <sup>1</sup> 이전 코드와의 연속성을 위해서, OpenKoreanText의 패키지 명칭은 twitter로 유지합니다.

KoalaNLP의 Contributor가 되고 싶으시다면, 언제든지 Issue에 등록해주십시오.
또한, 추가하고자 하는 새로운 프로젝트가 있으시면, Issue에 등록해주십시오.

# SBT/Maven

## Packages
각 형태소 분석기는 별도의 패키지로 Maven Central에 등재되어 있습니다.

| 패키지명 | 버전 | Java | Scala| 설명 |
| -------- | ---- | ---- | ---- |---- |
| `koalanlp-core` | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-core_2.12.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-core_2.12%22) | 8+ | 2.10+ | 통합 인터페이스의 정의가 등재된 중심 묶음입니다. |
| `koalanlp-hannanum` | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-hannanum_2.12.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-hannanum_2.12%22) | 8+ | 2.10+ | 한나눔 분석기 패키지입니다. <sup>2</sup> |
| `koalanlp-kkma` | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-kkma_2.12.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-kkma_2.12%22) | 8+ | 2.10+ | 꼬꼬마 분석기 패키지입니다. <sup>2</sup> |
| `koalanlp-komoran` | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-komoran_2.12.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-komoran_2.12%22) | 8+ | 2.10+ | 코모란 분석기 패키지입니다. <sup>2</sup> |
| `koalanlp-twitter` | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-twitter_2.12.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-twitter_2.12%22) | 8+ | **2.12+**<sup>3</sup> | 트위터(OpenKoreanText) 분석기 패키지입니다. |
| `koalanlp-eunjeon` | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-eunjeon_2.12.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-eunjeon_2.12%22) | 8+ | **2.11+**<sup>3</sup> | 은전한닢 분석기 패키지입니다. |
| `koalanlp-kryo` | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-kryo_2.12.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-kryo_2.12%22) | 8+ | 2.10+ | Kryo Serialization을 지원하기 위한 패키지입니다. |
| `koalanlp-server` | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-server_2.11.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-server_2.11%22) | 8+ | **2.10 - 2.11**<sup>3</sup> | HTTP 서비스 구성을 위한 패키지입니다. |

> <sup>주2</sup> 꼬꼬마, 한나눔, 코모란 분석기는 타 분석기와 달리 Maven repository에 등재되어 있지 않아, 원래는 수동으로 직접 추가하셔야 합니다.
> 이 점이 불편하다는 것을 알기에, KoalaNLP는 assembly 형태로 해당 패키지를 포함하여 배포하고 있습니다. 포함된 패키지를 사용하려면, `assembly` classifier를 사용하십시오.
> "assembly" classifier가 지정되지 않으면, 각 분석기 라이브러리가 빠진 채로 dependency가 참조됩니다.
>
> <sup>주3</sup>
> 의존하고 있는 일부 패키지가 Scala 2.10, Java 7을 지원하지 않아, 더 높은 버전만 사용 가능 합니다.

## Dependency 추가하기
KoalaNLP는 Scala 2.12.1에서 컴파일 되었으며, Scala 2.10+과 Java 8+을 지원합니다.

SBT를 사용하시는 경우, 다음과 같이 추가하시면 됩니다.
(버전은 Latest Release 기준입니다. SNAPSHOT을 사용하시려면, `latest.integration`을 사용하세요.)
```sbt
libraryDependencies += "kr.bydelta" %% "koalanlp-twitter" % "latest.release"	//트위터 분석기의 경우
libraryDependencies += "kr.bydelta" %% "koalanlp-eunjeon" % "latest.release"	//은전한닢 분석기의 경우

libraryDependencies += "kr.bydelta" %% "koalanlp-kkma" % "latest.release" classifier "assembly"	//꼬꼬마 분석기의 경우
libraryDependencies += "kr.bydelta" %% "koalanlp-komoran" % "latest.release" classifier "assembly"	//코모란 분석기의 경우
libraryDependencies += "kr.bydelta" %% "koalanlp-hannanum" % "latest.release" classifier "assembly"	//한나눔 분석기의 경우

libraryDependencies += "kr.bydelta" %% "koalanlp-kryo" % "latest.release" // Kryo Serialization
libraryDependencies += "kr.bydelta" %% "koalanlp-server" % "latest.release" // HTTP 서비스
```

Maven을 사용하시는 경우, 다음과 같습니다.
(버전은 Latest Release 기준입니다. SNAPSHOT을 사용하시려면, `LATEST`를 사용하세요.)
```xml
<dependency>
  <groupId>kr.bydelta</groupId>
  <artifactId>koalanlp-{TAGGER.PACK}_2.12</artifactId>
  <version>RELEASE</version>
</dependency>
```

Classifier를 추가하실 경우, `<artifactId>`다음 행에 다음 코드를 추가하세요.
```xml
  <classifier>assembly</classifier>
```

# 사용방법
아래에는 대표적인 특징만 기술되어 있습니다.

상세한 사항은 [Wiki](/wiki/Home) 또는 [![ScalaDoc](http://javadoc-badge.appspot.com/kr.bydelta/koalanlp-core_2.12.svg?label=scaladoc&style=flat-square)](http://nearbydelta.github.io/KoalaNLP/api/scala/kr/bydelta/koala/index.html)을 참고하십시오.

## 여러 패키지의 사용
통합 인터페이스는 여러 패키지간의 호환이 가능하게 설계되어 있습니다. 이론적으로는 타 패키지의 품사 분석 결과를 토대로 구문 분석이 가능합니다.
> __Note:__
> * 본 분석의 결과는 검증되지 않았습니다.
> * 신조어 등으로 인해 한나눔이나 꼬꼬마에서 품사 분석이 제대로 수행되지 않을 경우를 위한 기능입니다.
> * 사용자 정의 사전은 `Tagger`와 `Parser`의 대상이 되는 패키지에 모두에 추가하여야 합니다.

```scala
/* 패키지 명: 한나눔(hnn), 코모란(kmr), 꼬꼬마(kkma), 은전한닢(eunjeon), 트위터(twt) */
// 예시에서는 트위터 문장분석기, 은전한닢 품사 분석, 꼬꼬마 구문 분석을 진행함.
import kr.bydelta.koala.twt.SentenceSplitter
import kr.bydelta.koala.eunjeon.Tagger
import kr.bydelta.koala.kkma.Parser

val splitter = new SentenceSplitter
val tagger = new Tagger
val parser = new Parser

val paragraph = "누군가가 말했다. Python에는 KoNLPy가 있다. Scala는 KoalaNLP가 있었다."
val sentences = splitter.sentences(paragraph)
val tagged = sentences.map(tagger.tagSentence)
val parsed = tagged.map(parser.parse)
```

Java는 아래와 같습니다.
```java
import kr.bydelta.koala.twt.SentenceSplitter;
import kr.bydelta.koala.eunjeon.Tagger;
import kr.bydelta.koala.kkma.Parser;
import kr.bydelta.koala.Sentence;

SentenceSplitter splitter = new SentenceSplitter();
Tagger tagger = new Tagger();
Tagger parser = new Parser();

String paragraph = "누군가가 말했다. Python에는 KoNLPy가 있다. Java는 KoalaNLP가 있었다.";
List<String> sentences = splitter.jSentences(paragraph);
for(String line : sentences){
  Sentence tagged = tagger.tagSentence(line);
  Sentence parsed = parser.parse(tagged);
}
```

## Implicit 변환 & 패턴 매칭
Scala의 경우 다음과 같은 암시적 변환을 지원합니다.

```scala
import kr.bydelta.koala.Implicit._   //암시적 변환들

import kr.bydelta.koala.POS
import kr.bydelta.koala.hnn.SentenceSplitter
import kr.bydelta.koala.kkma._

// implicit 변환이 사용할 tagger/parser 설정
implicit val split = new SentenceSplitter
implicit val tagger = new Tagger
implicit val parser = new Parser

// 1. 문장분리
val sentences: Seq[String] = "나눠봅시다. 문장들로.".sentences

// 2. 품사표기
val tagged:Sentence = "분석할 문장입니다".toTagged

// 3. 의존구문분석
val parsed:Sentence = "분석할 문장입니다".toParsed
val parsed2 = tagged.toParsed

// 4. POSTag 확인
tagged.exists(POS.VV)      // 암시적 변환: POSTag --> (Word => Boolean)
tagged.head.exists(POS.VV) // 암시적 변환: POSTag --> (Morpheme => Boolean)

// 5. Set of POSTag
val posSet = Seq(POS.VV, POS.VA, POS.VCP)  //세 태그 중 하나라도 일치하는지 확인하고자 함.
tagged.exists(posSet)      // 암시적 변환: POSTag --> (Word => Boolean)
tagged.head.exists(posSet) // 암시적 변환: POSTag --> (Morpheme => Boolean)
```

또한 아래와 같이 패턴 매칭이 가능합니다.

```scala
morpheme match {
  case Morpheme(surf, pos) if POS.isNoun(pos) =>
  case Morpheme(surf, POS.VV) => ...
  case Morpheme(surf, tag) => ...
}

word match {
  case Word(surf, Morpheme(_, POS.VV), rest @ _*) =>
  case Word(surf, morphemes @ _*) => ...
}

sentence match {
  case Sentence(Word("나는", _), rest @ _*) =>
  case Sentence(words @ _*) => ...
}
```

# License 조항
이 프로젝트 자체(KoalaNLP-core)와 인터페이스 통합을 위한 코드는  *GPL v3*을 따르며,
각 분석기의 License와 저작권은 각 프로젝트에서 지정한 바를 따릅니다. (`kr.bydelta.koala.helper` 하위에 새로 수정되어 등록된 Class/Object는 각 프로젝트의 결과물을 조금 수정한 판본이며, 저작권은 각 프로젝트에 귀속됩니다.)
* Hannanum: GPL v3
* KKMA: GPL v2 (GPL v2를 따르지 않더라도, 상업적 이용시 별도 협의 가능)
* Komoran: GPL v2, 역컴파일 불가, 비상업적 용도 사용 가능, 상업적 용도 사용시 별도 협의 (3년미만의 10명 이하 사업장 자유)
* Twitter: Apache License 2.0
* Eunjeon: Apache License 2.0

# 결과 비교
[Wiki:결과비교](/wiki/4.-결과-비교)를 참조해주세요.
