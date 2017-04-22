KoalaNLP
==============
(구) KoreanAnalyzer

[![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-core_2.12.svg?style=flat-square&label=release)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-core_2.12%22)
[![Build Status](https://img.shields.io/travis/nearbydelta/KoalaNLP.svg?style=flat-square&branch=master)](https://travis-ci.org/nearbydelta/KoalaNLP)
[![codecov](https://img.shields.io/codecov/c/github/nearbydelta/KoalaNLP.svg?style=flat-square)](https://codecov.io/gh/nearbydelta/KoalaNLP)
[![GPL Licence](https://img.shields.io/github/license/nearbydelta/KoalaNLP.svg?style=flat-square)](https://opensource.org/licenses/GPL-3.0/)

[![ScalaDoc](http://javadoc-badge.appspot.com/kr.bydelta/koalanlp-core_2.12.svg?label=scaladoc)](http://nearbydelta.github.io/KoalaNLP/api/scala/kr/bydelta/koala/index.html)
[![JavaDoc](http://javadoc-badge.appspot.com/kr.bydelta/koalanlp-core_2.12.svg?label=javadoc)](http://nearbydelta.github.io/KoalaNLP/api/java/kr/bydelta/koala/index.html)
[![분석기별 품사비교표](https://img.shields.io/badge/%ED%92%88%EC%82%AC-%EB%B9%84%EA%B5%90%ED%91%9C-blue.svg?style=flat-square)](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)
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
아래에 대부분의 사항에 대해 기술하겠지만, 상세한 사항은 [![ScalaDoc](http://javadoc-badge.appspot.com/kr.bydelta/koalanlp-core_2.12.svg?label=scaladoc&style=flat-sqaure)](http://nearbydelta.github.io/KoalaNLP/api/scala/kr/bydelta/koala/index.html)을 참고하십시오.

## 문장 분리
품사 태깅을 거치지 않은 문장 분리는, 한나눔과 트위터(OpenKoreanText) 분석기만 지원됩니다. 타 패키지의 경우 문장 분리 작업이 품사 태깅 이후에 이루어집니다.
> __NOTE:__
> * 긴 문단의 경우, 문장 분리를 한나눔 또는 트위터(OpenKoreanText)로 작업한 다음 각 문장별로 태깅하는 것을 권합니다.
> * 한나눔이 트위터(OpenKoreanText)보다 문장분리가 정확하지만, 반대로 무겁습니다.

```scala
/* 패키지 명: 한나눔(hnn), 트위터(twt) */
// 예시에서는 트위터 문장 분리기 활용
import kr.bydelta.koala.twt.SentenceSplitter

val sentSplit = new SentenceSplitter
val paragraph = "누군가가 말했다. Python에는 KoNLPy가 있다. Scala는 KoalaNLP가 있었다."
val sentences: Seq[String] = sentSplit.sentences(paragraph)
```

Java는 다음과 같습니다.
```java
import kr.bydelta.koala.twt.SentenceSplitter;
import java.util.List;

SentenceSplitter sentSplit = new SentenceSplitter();
String paragraph = "누군가가 말했다. Python에는 KoNLPy가 있다. Java는 KoalaNLP가 있었다.";
List<String> sentences = sentSplit.jSentences(paragraph);
```

## 단순 품사 분석
모든 패키지가 품사 분석을 지원합니다.

> __Note:__ 
> * 형태소 분석의 결과는 세종 말뭉치의 지침에 따라 통합되었으며, 통합 태그와 각 분석기 태그의 비교표는 [![분석기별 품사비교표](https://img.shields.io/badge/%ED%92%88%EC%82%AC-%EB%B9%84%EA%B5%90%ED%91%9C-blue.svg?style=flat-square)](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)에서 보실 수 있습니다.
> * 은전한닢과 코모란 라이브러리는 문장분리기(Sentence splitter)를 지원하지 않아, Koala가 품사 분석 결과를 토대로 Heuristic을 사용하여 문장을 분리합니다. 때문에, 그 정확성이 떨어질 수 있습니다.
> * 트위터(OpenKoreanText)의 경우 품사 태깅을 세부적으로 진행하지 않아, 통합 변경 과정에서 임의로 대응되므로(예: Noun → NNG), 통합 태그가 실제와 다를 수 있으나, 큰 무리는 없습니다.

```scala
/* 패키지 명: 한나눔(hnn), 코모란(kmr), 꼬꼬마(kkma), 은전한닢(eunjeon), 트위터(twt) */
// 예시에서는 은전한닢 분석기 활용
import kr.bydelta.koala.eunjeon.Tagger
import kr.bydelta.koala.Sentence

val tagger = new Tagger

/* 단일 문장 분석 */
val sentence = "이것은 코알라 통합 품사분석기에서 은전한닢 분석기를 돌린 결과입니다."
val analyzed: Sentence = tagger.tagSentence(sentence)

/* 문단 분석 */
val paragraph = "누군가가 말했다. Python에는 KoNLPy가 있다. Scala는 KoalaNLP가 있었다."
val sentences: Seq[Sentence] = tagger.tagParagraph(paragraph)
```

Java는 다음과 같습니다.
```java
import kr.bydelta.koala.eunjeon.Tagger;
import kr.bydelta.koala.Sentence;
import java.util.List;

Tagger tagger = new Tagger();

/* 단일 문장 분석 */
String sentence = "이것은 코알라 통합 품사분석기에서 은전한닢 분석기를 돌린 결과입니다.";
Sentence analyzed = tagger.tagSentence(sentence);

/* 문단 분석 */
String paragraph = "누군가가 말했다. Python에는 KoNLPy가 있다. Scala는 KoalaNLP가 있었다.";
List<Sentence> sentences = tagger.jTagParagraph(paragraph);
```

## 단순 구문 분석
의존 구문 분석은 한나눔과 꼬꼬마가 지원합니다. (타 패키지는 지원하지 않습니다)

> __NOTE:__
> 구문 분석의 결과는 세종 말뭉치의 지침에 따라 통합되었으며, 통합 태그와 각 분석기 태그의 비교표는 [![분석기별 품사비교표](https://img.shields.io/badge/%ED%92%88%EC%82%AC-%EB%B9%84%EA%B5%90%ED%91%9C-blue.svg?style=flat-square)](https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing)에서 보실 수 있습니다.

```scala
/* 패키지 명: 한나눔(hnn), 꼬꼬마(kkma) */
// 예시에서는 꼬꼬마 구문 분석기 활용
import kr.bydelta.koala.kkma.Parser
import kr.bydelta.koala.Sentence

val parser = new Parser
val sentence = "이것은 코알라 통합 품사분석기에서 은전한닢 분석기를 돌린 결과입니다."
val analyzed: Sentence = parser.parse(sentence)
```

Java는 다음과 같습니다.
```java
import kr.bydelta.koala.kkma.Parser;
import kr.bydelta.koala.Sentence;

Parser parser = new Parser();
String sentence = "이것은 코알라 통합 품사분석기에서 은전한닢 분석기를 돌린 결과입니다.";
Sentence analyzed = parser.parse(sentence);
```

## 사용자 정의 사전
모든 품사 분석기는 사용자 정의 사전을 등록할 수 있습니다. 단, 트위터(OpenKoreanText)는 동사나 형용사와 같이, 어근에 어미가 붙어 활용되는 단어는 등록이 불가능합니다.

> __Note:__
> 사전에 등재되어도, 일부 라이브러리의 경우, 신규 추가된 단어의 우선순위가 낮아 적용되지 않을 수도 있습니다.

```scala
/* 패키지 명: 한나눔(hnn), 코모란(kmr), 꼬꼬마(kkma), 은전한닢(eunjeon), 트위터(twt) */
// 예시에서는 한나눔 사전에 추가
import kr.bydelta.koala.hnn.Dictionary
import kr.bydelta.koala.POS

Dictionary.addUserDictionary(
  "설빙" -> POS.NNP, /* 고유명사 '설빙' 추가 */
  "구글하" -> POS.VV /* 동사 '구글하다' 추가 */
)
```

Java는 다음과 같습니다.
```java
import kr.bydelta.koala.hnn.JavaDictionary;
import kr.bydelta.koala.POS;
import kr.bydelta.koala.POS$.Value;
import java.util.LinkedList;

LinkedList<String> morphemes = new LinkedList<>();
LinkedList<POS$.Value> pos = new LinkedList<>();

morphemes.add("설빙");
pos.add(POS.NNP()); /* 고유명사 '설빙' 추가 */

morphemes.add("구글하");
pos.add(POS.VV()); /* 동사 '구글하다' 추가 */

JavaDictionary.addUserDictionary(morphems, pos);

// 또는, 아래와 같이 사용할 수 있습니다.
Dictionary.jAddUserDictionary(morphemes, pos);
```

또한, 다른 사전의 항목을 불러올 수 있습니다.
```scala
import kr.bydelta.koala.kkma.Dictionary
import kr.bydelta.koala.eunjeon.Dictionary
import kr.bydelta.koala.POS

eunjeon.Dictionary.importFrom(kkma.Dictionary, filter = POS.isNoun, fastAppend = false)
```

Java는 아래와 같이 사용할 수 있습니다.
```java
import kr.bydelta.koala.kkma.JavaDictionary;
import kr.bydelta.koala.eunjeon.JavaDictionary;
import kr.bydelta.koala.POS;

// Java 8+, with lambda expression
eunjeon.JavaDictionary.get().importFrom(kkma.JavaDictionary.get(), 
    (POS$.Value p) -> POS.isNoun(p), false);
```

아래와 같이, 고유명사를 말뭉치로부터 학습할 수 있습니다.
> 고유명사가 아닌, 다른 품사의 자동학습기능은 아직 지원하지 않습니다.

```scala
import kr.bydelta.koala.kkma.{Tagger, Dictionary}
import kr.bydelta.koala.util.BasicWordLearner

val corpora: Iterator[String] = ... /* 말뭉치 Text의 Iterator */
val learner = new BasicWordLearner(new Tagger, Dictionary)
/*
 * 최소 10번 반복등장하고, 서로 다른 조사가 최소 3개 이상 부착되어 활용된 단어를 학습합니다.
 */
learner.learn(corpora, minOccurrence = 10, minVariations = 3)
```

Java는 아래와 같습니다.
```java
import kr.bydelta.koala.kkma.Tagger
import kr.bydelta.koala.kkma.JavaDictionary
import kr.bydelta.koala.util.BasicWordLearner

Iterator<String> corpora = ... /* 말뭉치 Text의 Iterator */
BasicWordLearner learner = new BasicWordLearner(new Tagger, JavaDictionary.get()))
/*
 * 최소 10번 반복등장하고, 서로 다른 조사가 최소 3개 이상 부착되어 활용된 단어를 학습합니다.
 */
learner.learn(corpora, 10, 3)
```

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

## HTTP 통신 Server
아래와 같이 서버를 정의합니다.
> __NOTE:__
> Scala만 지원합니다.

```scala
import kr.bydelta.koala.server._
import kr.bydelta.koala.kkma.{Dictionary, Parser, Tagger}

object KKMAServer extends Server{
  override val port: Int = 8080
  override def getTagger = new Tagger
  override def getParser = new Parser
  override val dict = Dictionary
}
```

KoalaNLP는, 개발자 여러분의 편의를 위해, `Server` Trait에 미리 `main`함수를 정의해 두었습니다.

따라서, `Server`를 상속받은 `object`는 실행이 가능합니다. SBT를 사용하면 아래와 같이 서비스 실행이 가능합니다.

```bash
sbt runMain KKMAServer
```

통신은 다음의 3가지 Path로 이루어집니다.

| Path     | 지원 Method     | 요청 Body | 응답 Body     |  설명                 |
| -------- | -------------- | -------- | ------------- | -------------------- |
| `/tag`   | GET, POST, PUT | String   | Json Object<sup>4</sup> | 요청 본문에 전달된 문단에 품사를 답니다. |
| `/parse` | GET, POST, PUT | String   | Json Object<sup>4</sup> | 요청 본문에 전달된 문단의 각 문장마다 의존관계를 분석합니다. |
| `/dict`  | POST, PUT      | Json Array<sup>5</sup> | Json Object<sup>4</sup> | 주어진 사전을 사용자사전에 추가합니다. |

> <sup>주4</sup> 응답의 형태:
> ```javascript
> { "success": Boolean,     //성공여부,
>   "message": String,      //[실패시] 서버 메시지,
>   "data": [{              //[Tag, Parse의 경우만] 문장 Array
>     "words":[               //문장 1개의 어절 Array
>       {"word": String,    //어절 표면형
>        "in": [            //어절의 형태소 Array
>         {"morph": String, //형태소 표면형
>          "tag": String    //통합품사
>         }...],
>        "children": [      //[Parse의 경우만] 의존소 Array
>         {"rel": String,     //표준화된 의존관계
>          "rawRel": String,  //의존관계 원본
>          "childID": Int     //의존소 위치
>         }...]
>       }...
>     ],
>     "root": [             //[Parse의 경우만] 문장 Root의 의존소 Array
>       {"rel": String,       //표준화된 의존관계
>        "rawRel": String,    //의존관계 원본
>        "childID": Int       //의존소 위치
>       }...
>     ]
>   }...]
> }
> ```

> <sup>주5</sup> 사전의 형태:
> ```javascript
> [{"morph": String,   //형태소 표면형
>   "tag": String      //통합품사.
>  }...]
> ```

## 결과의 저장
`koalanlp-kryo` 묶음이 필요합니다.
```scala
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.kryo._
import com.twitter.chill.{Input, Output}
import java.io.{FileOutputStream, FileInputStream}  

/** ... Parsing ... **/
val file = new File("target.path")
val parsed = parser.parse(sent)
val kryo = KryoWrap.kryo

/** Procedure for Saving **/
val output = new Output(new FileOutputStream(file))
kryo.writeObject(output, parsed)
output.close()

/** Procedure for Loading **/
val input = new Input(new FileInputStream(file))
val sentence = kryo.readObject(input, classOf[Sentence])
input.close()
```

Java는 다음과 같습니다.
```java
import kr.bydelta.koala.data.Sentence;
import kr.bydelta.koala.kryo.*;
import com.twitter.chill.*;
import java.io.*;  

/* ... Parsing ... */
File file = new File("target.path");
Sentence parsed = parser.parse(sent);
Kryo kryo = KryoWrap.kryo();

/* Procedure for Saving */
Output output = new Output(new FileOutputStream(file));
kryo.writeObject(output, parsed);
output.close();

/* Procedure for Loading */
Input input = new Input(new FileInputStream(file));
Sentence sentence = kryo.readObject(input, classOf[Sentence]);
input.close();
```

사용자정의 사전은 다음과 같이 저장합니다.
```scala
import kr.bydelta.koala.kryo._
import kr.bydelta.koala.kkma.Dictionary

/** Save dictionary **/
Dictionary >> new File("dictionary.path")
/** The following is equivalent.
 Dictionary saveTo new File("dictionary.path")
 * Or you can write into an outputStream
 Dictionary >> outputStream
 **/
 
/** Load dictionary **/
Dictionary << new File("dictionary.path")
/** The following is equivalent.
 Dictionary readFrom new File("dictionary.path")
 * Or you can read from an inputStream
 Dictionary << inputStream
 **/
```

Java는 다음과 같습니다.
```java
import kr.bydelta.koala.kryo.DictionaryStream;
import kr.bydelta.koala.kkma.JavaDictionary;

DictionaryStream stream = 
  new DictionaryStream(JavaDictionary.get());

/* Save dictionary */
stream.saveTo(new File("dictionary.path"));
/* Or you can write into an outputStream
stream.saveTo(outputStream)
 */
 
/* Load dictionary */
stream.readFrom(new File("dictionary.path"))
/* Or you can read from an inputStream
stream.readFrom(inputStream)
 */
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

## 자료 구조
아래는 중심 자료 구조가 지원하는 주요 API 목록입니다.

### Class `Morpheme` (형태소)
* String `surface` 형태소 표면형
* String `rawTag` 품사 분석을 진행한 분석기에서 부여한 품사 (통합 전)
* Int `id` 형태소의 단어 내 위치
* koala.POSTag `tag` 통합 품사 
* Boolean `isNoun`, `isPredicate`, `isModifier`, `isJosa` 체언/용언/수식언/관계언 여부
* Boolean `hasTag(tag:String)` 통합 품사가 tag로 제시한 해당 통합 품사의 하위 분류인지 확인
* Boolean `hasRawTag(tag:String)` 원본 품사가 tag로 제시한 품사의 하위 분류인지 확인

### Class `Relationship` (의존관계)
* Int `head` 문장 내에서 지배소(head)의 위치
* Int `target` 문장 내에서 의존소의 위치
* String `rawRel` 표준화되지 않은 의존관계 명칭(원본)
* FunctionalTag `relation` 표준화된 의존관계

### Class `Word` (어절)
Word는 `IndexedSeq[Morpheme]`과 `IndexedSeqLike[Morpheme, Word]`를 상속합니다.
* String `surface` 원본 어절 또는 복원된 어절의 표면형
* Int `id` 단어의 문장 내 위치
* Seq[Morpheme] `morphemes`, java.util.List<Morpheme> `jMorphemes` 어절에 포함된 형태소들
* Set[Relationship] `dependents`, java.util.List<Relationship> `jDependents` 현재 어절이 지배소(Dominant)인 의존소(Dependent) 목록. 즉, 현재 어절에 의존하는 어절의 목록.
* Boolean `matches(seq: Seq[String])`, `matches(POS$.Value[] arr)` 주어진 품사 목록을 순서대로 가지고 있는지 검사함.

### Class `Sentence` (문장)
Sentence는 `IndexedSeq[Word]`와 `IndexedSeqLike[Word, Sentence]`를 상속합니다.
* Seq[Word] `words`, java.util.List<Word> `jWords` 문장 내의 단어들
* Seq[Word] `topLevels`, java.util.List<Word> `jTopLevels` 의존구문분석에서 Root(뿌리)에 의존하는 단어, 즉, 핵심어들.
* Boolean `matches(seq: Seq[Seq[String]])`, `matches(POS$.Value[][] arr)` 주어진 품사 목록의 단어 목록을 순서대로 가지고 있는지 검사함.
* Seq[Word] `nouns`, `verbs`, `modifiers` java.util.List<Word> `jNouns`, `jVerbs`, `jModifiers` 문장 내 체언, 용언, 수식언을 포함한 단어들
* String `surfaceString(delimiter: String = " ")` 띄어쓰기를 교정한 원본 문장을 구성하여 돌려줌.

# License 조항
이 프로젝트 자체(KoalaNLP-core)와 인터페이스 통합을 위한 코드는  *GPL v3*을 따르며,
각 분석기의 License와 저작권은 각 프로젝트에서 지정한 바를 따릅니다. (`kr.bydelta.koala.helper` 하위에 새로 수정되어 등록된 Class/Object는 각 프로젝트의 결과물을 조금 수정한 판본이며, 저작권은 각 프로젝트에 귀속됩니다.)
* Hannanum: GPL v3
* KKMA: GPL v2 (GPL v2를 따르지 않더라도, 상업적 이용시 별도 협의 가능)
* Komoran: GPL v2, 역컴파일 불가, 비상업적 용도 사용 가능, 상업적 용도 사용시 별도 협의 (3년미만의 10명 이하 사업장 자유)
* Twitter: Apache License 2.0
* Eunjeon: Apache License 2.0


# 결과 비교
실제 사용 성능을 보여드리기 위해서, 네이버 뉴스에서 임의로 문장을 선택하였습니다.

## 일반 문장
아래 문장은 **국토부**라는 단어 이외에는 일반적으로 큰 문제가 없는 문장입니다.
> 국토부는 시장 상황과 맞지 않는 일률적인 규제를 탄력적으로 적용할 수 있도록 법 개정을 추진하는 것이라고 설명하지만, 투기 세력에 기대는 부동산 부양책이라는 비판이 일고 있다.

### 은전한닢
```text
국토/NNG 부/NNG+는/JX 시장/NNG 상황/NNG+과/JKB 맞/VV+지/EC 않/VX+는/ETM 일률/NNG+적/XSN+이/VCP+ᆫ/ETM 규제/NNG+를/JKO 탄력/NNG+적/XSN+으로/JKB 적용/NNG+하/XSV+ᆯ/ETM 수/NNB 있/VV+도록/EC 법/NNG 개정/NNG+을/JKO 추진/NNG+하/XSV+는/ETM 것/NNB+이/VCP 라고/EC 설명/NNG+하/XSV+지만/EC ,/SP 투기/NNG 세력/NNG+에/JKB 기대/VV+는/ETM 부동/NNG+산/NNG 부양/NNG+책/NNG+이/VCP 라는/ETM 비판/NNG+이/JKS 일/VV+고/EC 있/VX+다/EF ./SF
```
은전한닢은 **국토부**의 국토와 부, **부동산**의 부동과 산, **부양책**의 부양과 책을 별개로 인식하였고, **것이 라고**, **부양책이 라는**과 같이, 띄어쓰기를 하였으며, 문장의 종결부호나 구분부호(**./SF ,/SP**)를 띄어쓰기 하였습니다. 품사 부착에는 큰 문제가 없어보입니다.

### 한나눔
```text
국토/NNG+부/NNG+는/JX 시장/NNG 상황/NNG+과/JC 맞/VV+지/EC 않/VX+는/ETM 일률/NNG+적/NNG+이/VCP+ㄴ/ETM 규제/NNG+를/JKO 탄력/NNG+적/NNG+으로/JKB 적용/NNG+하/XSV+ㄹ/ETM 수/NNB 있/VX+도록/EC 법/NNG 개정/NNG+을/JKO 추진/NNG+하/XSV+는/ETM 것/NNB+이/VCP+라/EF+고/JKQ 설명/NNG+하/XSV+지/EC+말/VX+ㄴ/ETM+,/SP 투/NNB+이/VCP+기/ETN 세력/NNG+에/JKB 기대/VV+는/ETM 부동산/NNG 부양책/NNG+이/VCP+라/EF+는/ETM 비판/NNG+이/JKC 일/VV+고/EC 있/VX+다/EF ./SF
```
한나눔은 **국토부**의 국토와 부를 별개로 인식하였고, 문장 종결 부호(**./SF**)를 띄어 쓰기 하였습니다. **투기**라는 단어를 의존명사 **투** + 긍정지정사 **이-** + **명사형 전성어미** -기로 인식, "~라고 말하는 투기"의 투기로 인식하였습니다. 동사와 그 변형의 분석에 가장 자세합니다.

### 꼬꼬마
```text
국토/NNG 부/NNG+는/JX 시장/NNG 상황/NNG+과/JKB 맞/VV+지/EC 않/VX+는/ETM 일률적/NNG+이/VCP+ㄴ/ETM 규제/NNG+를/JKO 탄력/NNG+적/XSN+으로/JKB 적용/NNG+하/XSV+ㄹ/ETM 수/NNB 있/VV+도록/EC 법/NNG 개정/NNG+을/JKO 추진/NNG+하/XSV+는/ETM 것/NNB+이/VCP+라고/EC 설명/NNG+하/XSV+지만/EC+,/SP 투기/NNG 세력/NNG+에/JKB 기대/VV+는/ETM 부동산/NNG 부양책/NNG+이/VCP+라는/ETM 비판/NNG+이/JKS 일/VV+고/EC 있/VX+다/EF+./SF
```
꼬꼬마는 **국토부**의 국토와 부를 별개로 인식하였습니다.

### 코모란
```text
국토/NNG+부/NNG+는/JX 시장/NNG 상황/NNG+과/JC 맞/VV+지/EC 않/VX+는/ETM 일률/NNG+적/XSN+이/VCP+ㄴ/ETM 규제/NNG+를/JKO 탄력/NNG+적/XSN+으로/JKB 적용/NNG+하/XSV+ㄹ/ETM 수/NNB 있/VV+도록/EC 법/NNG 개정/NNG+을/JKO 추진/NNG+하/XSV+는/ETM 것/NNB+이/VCP+라고/EC 설명/NNG+하/XSV+지만/EC+,/SP 투기/NNG 세력/NNG+에/JKB 기대/NNG+는/JX 부동산/NNG 부양책/NNG+이/VCP+라는/ETM 비판/NNG+이/JKS 일/NNB+이/VCP+고/EC 있/VX+다/EF+./SF
```
코모란은 **국토부**의 국토와 부를 별개로 인식하였고, 문장의 종결부호나 구분부호(**./SF ,/SP**)를 앞선 단어에 붙여 쓰기 하였습니다. "의견 따위가 나타나고"라는 뜻의 **일고**를 의존명사 일 + 긍정 지정사 이- + 접속조사 -고 와 분석, "~하는 일이고"의 "일이고"로 잘못 분석하였습니다.

### 트위터(OpenKoreanText)
```text
국토부/NNP+는/JX 시장/NNG 상황/NNG+과/JX 맞지/VV 않는/VV 일률/NNG+적/XSO+인/JX 규제/NNG+를/JX 탄력/NNG+적/XSO+으로/JX 적용할/VV 수/NNG 있도/VA+록/EF 법/NNG 개정/NNG+을/JX 추진하는/VV 것/NNG+이라고/JX 설명하지/VV+만/EF+,/SF 투기/NNG 세력/NNG+에/JX 기대는/VV 부동산/NNG 부양책/NNG+이라는/JX 비판/NNG+이/JX 일/NNG+고/JX 있다/VA+./SF
```
OpenKoreanText 분석기는 가장 넓은 범위로 분석, 품사 내부의 세부 구분이 나타나지 않습니다. 어절 단위의 묶음은 KoalaNLP에서 공백 기준으로 묶은 것입니다.

## 문장 분리 성능 (따옴표 안에 여러 문장이 인용될 때)
아래 문장은 겹따옴표("") 사이에 3개의 문장이 포함되어, 문장 분리에 까다로운 문장입니다.

> 집 앞에서 고추를 말리던 이숙희(가명·75) 할머니의 얼굴에는 웃음기가 없었다. "나라가 취로사업이라도 만들어주지 않으면 일이 없어. 섬이라서 어디 다른 데 나가서 일할 수도 없고." 가난에 익숙해진 연평도 사람들은 '정당'과 '은혜'라는 말을 즐겨 썼다.

### 은전한닢 (Koala 구현)
```text
집/NNG 앞/NNG+에서/JKB 고추/NNG+를/JKO 말리/VV+던/ETM 이숙희/NNP (/SS 가명/NNG ·/SP 75/SN )/SS 할머니/NNG+의/JKG 얼굴/NNG+에/JKB+는/JX 웃음/NNG+기/NNG+가/JKS 없/VA+었/EP+다/EF ./SF

"/SY 나라/NNG+가/JKS 취로/NNG 사업/NNG+이/VCP 라도/EC 만들/VV+어/EC 주/VX+지/EC 않/VX+으면/EC 일/NNG+이/JKS 없/VA+어/EF ./SF 섬/NNG+이/VCP 라서/EC 어디/NP 다른/MM 데/NNB 나가/VV+서/EC 일/NNG+하/XSV+ᆯ/ETM 수/NNB+도/JX 없/VA+고/EC ./SY "/SY 가난/NNG+에/JKB 익숙/XR+하/XSA+아/EC+지/VX+ᆫ/ETM 연평/NNG+도/NNG 사람/NNG+들/XSN+은/JX '/SY 정당/NNG '/SY 과/JC '/SY 은혜/NNG '/SY 이/VCP+라는/ETM 말/NNG+을/JKO 즐기/VV+어/EC 쓰/VV+었/EP+다/EF ./SF
```
정상 분리입니다. 따옴표가 끝나는 부분에서 문제가 발생하지만, 이는 처리할 수 없는 부분입니다. ("~하였다."라고 말했다)라는 문장을 분리할 수 없기 때문입니다.

### 한나눔
```text
집/NNG 앞/NNG+에서/JKB 고추/NNG+를/JKO 말/VX+ㄹ/ETM+리/NNB+이/VCP+던/ETM 이숙희(가명·75)/NNG 할머니/NNG+의/JKG 얼굴/NNG+에/JKB+는/JX 웃음/NNG+기/NNG+가/JKS 없/VA+었/EP+다/EF ./SF

"/SS+나라/NNG+가/JKS 취로사업/NNG+이/VCP+라/EC+도/JX 만들/VV+어/EC+주/VX+지/EC 않/VX+으면/EC 일/NNG+이/JKC 없/VA+어/EC ./SF

서/VV+ㅁ/ETN+이/VCP+라서/EC 어디/MAG 다른/MM 데/NNB 나가/VV+아/EC 일/NR+할/NNM 수/NNB+도/JX 없/VA+고/EC ./SF+"/SS

가난/NNG+에/JKB 익숙해지/VV+ㄴ/ETM 연평도/NNG 사람/NNG+들/XSN+은/JX '/SS+정당/NNG+'/SS+과/JC '/SS+은혜/NNG+'/SS+라는/JKG 말/NNG+을/JKO 즐기/VV+어/EC 쓰/VV+었/EP+다/EF ./SF
```
한나눔은 문장부호를 단어에 붙여쓰는 경향과, 동사를 우선하여 인식하려는 경향으로 인해서, 따옴표 안의 문장이 분리되었습니다. 다만 홑따옴표로 강조된 단어는 한 어절로 바르게 인식되었습니다.

### 꼬꼬마
```text
집/NNG 앞/NNG+에서/JKB 고추/NNG+를/JKO 말리/VV+더/EP+ㄴ/ETM 이/NNG 숙희/UN+(/SS 가명/NNG ·/SP+75/NR+)/SS 할머니/NNG+의/JKG 얼굴/NNG+에/JKB+는/JX 웃음기/NNG+가/JKS 없/VA+었/EP+다/EF+./SF

"/SS 나라/NNG+가/JKS 취로/NNG 사업/NNG+이/VCP+라도/EC 만들/VV+어/EC 주/VX+지/EC 않/VX+으면/EC 일/NNG+이/JKS 없/VA+어/EC+./SF 섬/NNG+이/VCP+라서/EC 어디/NP 다른/MM 데/NNB 나가/VV+서/EC 일하/VV+ㄹ/ETM 수/NNB+도/JX 없/VA+고/EC+./SF+"/SS 가난/NNG+에/JKB 익숙/XR 해지/VV+ㄴ/ETM 연평도/NNP 사람/NNG+들/XSN+은/JX '/SS 정당/NNG+'/SS 과/NNG '/SS 은혜/NNG+'/SS 이/VCP+라는/ETM 말/NNG+을/JKO 즐기/VV+어/EC 쓰/VV+었/EP+다/EF+./SF
```
꼬꼬마는 정상적으로 분리되었습니다.

### 코모란 (Koala 구현)
```text
집/NNG 앞/NNG+에서/JKB 고추/NNG+를/JKO 말리/VV+던/ETM 이/MM+숙희/NNP+(/SS+가명/NNG+·/SP+75/SN+)/SS 할머니/NNG+의/JKG 얼굴/NNG+에/JKB+는/JX 웃음기/NNG+가/JKS 없/VA+었/EP+다/EF+./SF

"/SS+나라/NNG+가/JKS 취로사업이라도/UE 만들/VV+어/EC+주/VX+지/EC 않/VX+으면/EC 일/NNG+이/JKS 없/VA+어/EF+./SF 섬/NNB+이/VCP+라서/EC 어디/NP 다른/MM 데/NNB 나가/VV+아서/EC 일/NNG+하/XSV+ㄹ/ETM 수/NNB+도/JX 없/VA+시/EP+고/EF+./SF+"/SS 가난/NNG+에/JKB 익숙/XR+하/XSA+아/EC+지/VX+ㄴ/ETM 연평도/NNP 사람/NNG+들/XSN+은/JX '/SS+정당/NNG+'/SS+과/JC '/SS+은혜/NNP+'/SS+이/VCP+라는/ETM 말/NNG+을/JKO 즐기/VV+어/EC 쓰/VV+었/EP+다/EF+./SF
```
정상적으로 분리되었습니다.

### 트위터(OpenKoreanText)
```text
집/NNG 앞/NNG+에서/JX 고추/NNG+를/JX 말리/VV+던/EF 이숙희/NNG+(/SF+가명/NNG+·/SF+75/NR+)/SF 할머니/NNG+의/JX 얼굴/NNG+에는/JX 웃음/NNG+기/NNG+가/JX 없었/VA+다/EF+./SF

"/SF+나라/NNG+가/JX 취/NNP+로/JX+사업/NNG+이라도/JX 만들어/VV+주/EP+지/EF 않으/VV+면/EF 일이/NNG 없어/VA+./SF

섬/NNG+이라서/JX 어디/NNG 다른/NNG 데/NNG 나가서/VV 일할/VV 수도/NNG 없고/VA+."/SF

가난/NNG+에/JX 익숙해진/VV 연평도/NNG 사람/NNG+들/XSO+은/JX '/SF+정당/NNG+'/SF+과/NNG '/SF+은혜/NNG+'/SF+라는/JX 말/NNG+을/JX 즐겨/VV 썼/VV+다/EF+./SF
```
OpenKoreanText 분석기는 겹따옴표 내의 문장을 인용문으로 인식하지 않았습니다.

## 사전에 없는 단어 1
아래는 사전에 없을 법한 단어인 **포켓몬**과 **와이파이존**이 있고, 지명인 **속초**가 등장하는 문장입니다.

> 포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐고, 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다.

### 은전한닢
```text
포털/NNP+의/JKG '/SY 속초/NNP '/SY 연관/NNG 검색/NNG+어/NNG+로/JKB '/SY 포켓몬/NNP 고/NNG '/SY 가/JKS 오르/VV+았/EP+고/EC ,/SP 속초/NNG 시청/NNG+이/JKS 관광/NNG+객/NNG+의/JKG 편의/NNG+를/JKO 위하/VV+아/EC 예전/NNG+에/JKB 만들/VV+었/EP+던/ETM 무료/NNG 와이파이/NNP 존/NNP 지도/NNG+는/JX 순식/NNG+간/NNG+에/JKB 인기/NNG 게시/NNG+물/NNG+이/JKS 되/VV+었/EP+다/EF ./SF
```
속초, 포켓몬, 와이파이존 모두 명사구 또는 복합명사구로 정상 인식되었습니다.

### 한나눔
```text
포털/NNG+의/JKG '/SS+속/NNM+초/XSN+'/SS 연관/NNG 검색/NNG+어로/NNG '포켓몬/NNG 고/MM+'/SS+가/JKC 오르/VV+아/EP+고/EC+,/SP 속초시청/NNG+이/JKC 관광객/NNG+의/JKG 편의/NNG+를/JKO 위하/VV+어/EC 예전/NNG+에/JKB 만들/VV+었/EP+던/ETM 무료/NNG 와이파이존/NNG 지/NNB+도/JX+는/JX 순식간/NNG+에/JKB 인기/NNG 게시/NNG+물/NNG+이/JKC 되/VV+었/EP+다/EF ./SF
```
속초는 분리되었으나, 속초시청은 하나로 인식되었으며, 포켓몬과 와이파이존은 명사로 인식되었습니다.

### 꼬꼬마
```text
포털/NNG+의/JKG '/SS 속/NNG 초/NNB+'/SS 연관/NNG 검색어/NNG+로/JKB '/SS 포켓/NNG 몬/MAG 고/NNG+'/SS 가/VV+아/EC 오르/VV+았/EP+고/EC+,/SP 속초시/NNP+청/XSN+이/JKS 관광객/NNG+의/JKG 편의/NNG+를/JKO 위하/VV+어/EC 예전/NNG+에/JKB 만들/VV+었/EP+더/EP+ㄴ/ETM 무료/NNG 와이/NNG 파이/NNG 존/NNP 지도/NNG+는/JX 순식간/NNG+에/JKB 인기/NNG 게시물/NNG+이/JKC 되/VV+었/EP+다/EF+./SF
```
속초는 속(일반명사)+초(의존명사)로 어절이 분리되었으며, 속초시청은 속초시+청(명사파생 접미사)으로 분리되었으며, 포켓몬은 포켓+몬(관형사)으로 어절이 분리되고, 와이파이존은 와이, 파이, 존으로 분리되었습니다.

### 코모란
```text
포털/NNG+의/JKG '/SS+속초/NNP+'/SS 연관/NNG 검색어/NNG+로/JKB '포켓몬/UE 고/NNG+'/SS+가/JKS 오르/VV+았/EP+고/EC+,/SP 속초/NNP+시청/NNG+이/JKS 관광객/NNG+의/JKG 편의/NNG+를/JKO 위하/VV+아/EC 예전/NNG+에/JKB 만들/VV+었/EP+던/ETM 무료/NNG 와이/NNG+파이/NNG+존/NNG 지도/NNG+는/JX 순식간/NNG+에/JKB 인기/NNG 게시물/NNG+이/JKS 되/VV+었/EP+다/EF+./SF
```
속초, 속초시청(속초+시청)은 정상 인식되었고, 포켓몬은 알 수 없는 단어로 정상 인식 되었지만, 와이파이존은 와이+파이+존으로 인식되었습니다.

### 트위터(OpenKoreanText)
```text
포털/NNP+의/JX '/SF+속초/NNG+'/SF 연관/NNG 검색어/NNG+로/JX '/SF+포켓몬/NNG 고/NNG+'/SF+가/VV 올랐/VV+고/EF+,/SF 속초/NNP+시청/NNG+이/JX 관광객/NNG+의/JX 편의/NNG+를/JX 위해/NNG 예전/NNG+에/JX 만들었/VV+던/EF 무료/NNG 와이파이존/NNG 지도/NNG+는/JX 순식간/NNG+에/JX 인기/NNG 게시/NNG+물이/NNG 됐/VV+다/EF+./SF
```
속초, 속초 시청, 포켓몬, 와이파이존 모두 정상 인식되었습니다.

## 사전에 없는 단어 2
아래 문장에서는 **사드**, **월스트리트 저널**이 문제가 될 수 있는 단어입니다.
> 미국 국방부가 미국 미사일방어망(MD)의 핵심 무기체계인 사드(THAAD)를 한국에 배치하는 방안을 검토하고 있다고 <월스트리트 저널>이 28일(현지시각) 보도했다.

### 은전한닢
```text
미국/NNP 국방/NNG+부/NNG+가/JKS 미국/NNP 미사일/NNG 방어/NNG+망/NNG (/SS MD/SL )/SS 의/JKG 핵심/NNG 무기/NNG 체계/NNG+이/VCP+ᆫ/ETM 사드/NNP (/SS THAAD/SL )/SS 를/JKO 한국/NNP+에/JKB 배치/NNG+하/XSV+는/ETM 방안/NNG+을/JKO 검토/NNG+하/XSV+고/EC 있/VX+다고/EC </SY 월/NNG+스트리트/NNP 저널/NNG >/SY 이/JKS 28/SN 일/NNM (/SS 현지/NNG 시각/NNG )/SS 보도/NNG+하/XSV+았/EP+다/EF ./SF
```
사드는 정상인식되었으나, 월스트리트(월+스트리트) 저널은 일부 오류가 있습니다.

### 한나눔
```text
미국/NNG 국방부/NNG+가/JKS 미국/NNG 미사일방어망(MD)/NNG+의/JKG 핵심/NNG 무기체계/NNG+이/VCP+ㄴ/ETM 사드(THAAD)/NNG+를/JKO 한국/NNG+에/JKB 배치/NNG+하/XSV+는/ETM 방안/NNG+을/JKO 검토/NNG+하고/JC 있/VX+다/EF+고/JKQ </SS+월스트리트/NNG 저널/NNG+>/SS+이/JKC 28일(현지시각)/NNG 보도/NNG+하/XSV+었/EP+다/EF ./SF
```
사드, 월스트리트 저널 모두 정상 인식되었습니다. 특이한 것은, 사드, 미사일방어망의 영문 표현을 묶어 인식했다는 점입니다.

### 꼬꼬마
```text
미국/NNP 국방부/NNG+가/JKS 미국/NNP 미사일/NNG 방어망/NNG (/SS+MD/SL+)/SS 의/NNG 핵심/NNG 무기/NNG+체계/NNG+이/VCP+ㄴ/ETM 사드/UN (/SS+THAAD/SL )/SS+를/JKO 한국/NNG+에/JKB 배치/NNG+하/XSV+는/ETM 방안/NNG+을/JKO 검토/NNG+하/XSV+고/EC 있/VX+다고/EF

</SS 월/NNG 스트리트/NNG 저널/NNG+>/SS 이/MM 28/NR+일/NNM+(/SS 현지/NNG 시각/NNG+)/SS 보도/NNG+하/XSV+었/EP+다/EF+./SF
```
사드는 명사로 추정되는 알 수 없는 단어로 정상 처리되었지만, 월스트리트 저널은 월, 스트리트, 저널로 분리되어 일부 문제가 있습니다.

### 코모란
```text
미국/NNP 국방부/NNG+가/JKS 미국/NNP 미사일/NNG+방어망/NNG+(/SS+MD/SL+)/SS+의/JKG 핵심/NNG 무기/NNG+체계/NNG+이/VCP+ㄴ/ETM 사/NNG+드/NNG+(/SS+THAAD/SL+)/SS+를/JKO 한국/NNP+에/JKB 배치/NNG+하/XSV+는/ETM 방안/NNG+을/JKO 검토/NNG+하/XSV+고/EC 있/VV+다고/EC </SS+월스트리트/NNP 저널/NNG+>/SS+이/MM 28/SN+일/NNB+(/SS+현지/NNG+시각/NNG+)/SS 보도/NNG+하/XSV+았/EP+다/EF+./SF
```
사드는 사+드로 분리되어 인식되었고, 월스트리트 저널은 정상 인식되었습니다.

### 트위터(OpenKoreanText)
```text
미국/NNG 국방부/NNG+가/JX 미국/NNG 미사일방어/NNP+망/NNG+(/SF+MD/SY+)/SF+의/NNG 핵심/NNG 무기체계/NNP+인/JX 사드/NNG+(/SF+THAAD/SY+)/SF+를/NNG 한국/NNG+에/JX 배치/NNG+하는/VV 방안/NNG+을/JX 검토/NNG+하고/JX 있다/VA+고/EF </SF+월스트리트/NNG 저/MM+널/NNG+>/SF+이/NNG 28일/NR+(/SF+현지/NNP+시각/NNG+)/SF 보도했/VV+다/EF+./SF
```

사드는 정상 인식되었지만, 월스트리트 저널은 월스트리트, 저, 널로 인식되었습니다.
