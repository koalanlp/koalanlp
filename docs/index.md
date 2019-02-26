# Module KoalaNLP

## KoalaNLP

KoalaNLP는 한국어 처리의 통합 인터페이스를 지향하는 Java/Kotlin/Scala Library입니다.

이 프로젝트는 __서로 다른 형태의 형태소 분석기를__ 모아,
__동일한 인터페이스__ 아래에서 사용할 수 있도록 하는 것이 목적입니다.
* KAIST의 [한나눔 형태소 분석기](http://kldp.net/projects/hannanum/)와 [NLP_HUB 구문분석기](http://semanticweb.kaist.ac.kr/home/index.php/NLP_HUB)
* 서울대의 [꼬꼬마 형태소/구문 분석기 v2.1](http://kkma.snu.ac.kr/documents/index.jsp)
* Shineware의 [코모란 v3.3.4](https://github.com/shin285/KOMORAN)
* OpenKoreanText의 [오픈 소스 한국어 처리기 v2.3.1](http://openkoreantext.org) (구 Twitter 한국어 분석기)
* 은전한닢 프로젝트의 [SEunjeon(S은전)](https://bitbucket.org/eunjeon/seunjeon) (Mecab-ko의 Scala/Java 판본)
* 이수명님의 [Arirang Morpheme Analyzer](http://cafe.naver.com/korlucene) <sup>1-1</sup>
* 최석재님의 [RHINO v2.5.4](https://github.com/SukjaeChoi/RHINO)
* 김상준님의 [Daon 분석기](https://github.com/rasoio/daon/tree/master/daon-core)
* ETRI의 [공공 인공지능 Open API](http://aiopen.etri.re.kr/)
* Kakao의 [카이(Khaiii) v0.3](https://github.com/kakao/khaiii) <sup>(별도설치 필요)</sup>

> <sup>주1-1</sup> Arirang 분석기의 출력을 형태소분석에 적합하게 조금 다듬었으므로, 원본과 약간 다른 결과를 낼 수도 있습니다.

분석기의 개선이나 추가 등을 하고 싶으시다면,
* 개발이 직접 가능하시다면 pull request를 보내주세요. 테스트 후 반영할 수 있도록 하겠습니다.
* 개발이 어렵다면 issue tracker에 등록해주세요. 검토 후 답변해드리겠습니다.

## 사용방법

* [Usage](https://koalanlp.github.io/koalanlp/usage/)
* [API Doc](http://koalanlp.github.io/koalanlp/api/koalanlp/index.html)
* [Sample](https://koalanlp.github.io/sample)

## 특징

KoalaNLP는 다음과 같은 특징을 가지고 있습니다.

1. 복잡한 설정이 필요없는 텍스트 분석:

   모델은 자동으로 Maven으로 배포되기 때문에, 각 모델을 별도로 설치할 필요가 없습니다.

2. 코드 2~3 줄로 수행하는 텍스트 처리:

   모델마다 다른 복잡한 설정 과정, 초기화 과정은 필요하지 않습니다. Dependency에 추가하고, 객체를 생성하고, 분석 메소드를 호출하는 3줄이면 끝납니다.

3. 모델에 상관 없는 동일한 코드, 동일한 결과:

   모델마다 실행 방법, 실행 결과를 표현하는 형태가 다릅니다. KoalaNLP는 이를 정부 및 관계기관의 표준안에 따라 표준화합니다. 따라서 모델에 독립적으로 응용 프로그램 개발이 가능합니다.

4. Java, Kotlin, [Scala](https://koalanlp.github.io/scala-support), [Python 3](https://koalanlp.github.io/python-support), [NodeJS](https://koalanlp.github.io/nodejs-support)에서 크게 다르지 않은 코드:

   KoalaNLP는 여러 프로그래밍 언어에서 사용할 수 있습니다. 어디서 개발을 하더라도 크게 코드가 다르지 않습니다. 

## 참고
모든 코드는 Kotlin으로 작성되어 있습니다. Java나 Scala에 비해서 유지 보수에 편리하다고 판단하여 변경하였습니다.
* Kotlin의 사용법은 [Try Kotlin (공식)](https://try.kotlinlang.org/)에서 학습하실 수 있습니다.
* 모든 코드는 Java와 Kotlin에서 완벽히 상호 호환됩니다.
* 더불어 기존 1.x의 Scala 사용을 지원하기 위해서 [`koalanlp-scala`](https://koalanlp.github.io/scala-support)를 통해 Implicit 변환을 지원합니다. 

# License 조항

이 프로젝트 자체(KoalaNLP-core)와 인터페이스 통합을 위한 코드는
소스코드에 저작권 귀속에 대한 별도 지시사항이 없는 한 v1.8.0부터 [*MIT License*](https://tldrlegal.com/license/mit-license)을 따르며,
원본 분석기의 License와 저작권은 각 저작권자가 지정한 바를 따릅니다.

단, GPL의 저작권 조항에 따라, GPL 하에서 이용이 허가되는 패키지들의 저작권은 해당 저작권 규정을 따릅니다.

* Hannanum 및 NLP_HUB: [GPL v3](https://tldrlegal.com/license/gnu-general-public-license-v3-(gpl-3))
* KKMA: [GPL v2](https://tldrlegal.com/license/gnu-general-public-license-v2) (GPL v2를 따르지 않더라도, 상업적 이용시 별도 협의 가능)
* KOMORAN 3.x: [Apache License 2.0](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0))
* Open Korean Text: [Apache License 2.0](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0))
* SEunjeon: [Apache License 2.0](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0))
* 아리랑: [Apache License 2.0](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0))
* RHINO: [GPL v3](https://tldrlegal.com/license/gnu-general-public-license-v3-(gpl-3)) (참고: 다운로드 위치별로 조항 상이함)
* Daon: 지정된 조항 없음
* ETRI: 별도 API 키 발급 동의 필요
* Khaiii: [Apache License 2.0](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0))

# Dependency 추가

## Java 패키지 목록

다음과 같은 하위 패키지가 있습니다.

| 패키지명            | 설명                                                                 |  버전    | License (원본)     |
| ------------------ | ------------------------------------------------------------------ | ------- | ------------ |
| `koalanlp-core`    | 통합 인터페이스의 정의가 등재된 중심 묶음입니다.                            | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-core.svg?style=flat-square&label=r)](http://search.maven.org/search?q=koalanlp-core) | MIT |
| `koalanlp-scala`   | Scala를 위한 편의기능 (Implicit conversion 등)                         | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-scala_2.12.svg?style=flat-square&label=r)](http://search.maven.org/search?q=koalanlp-scala_2.12) | MIT |
| `koalanlp-server`  | HTTP 서비스 구성을 위한 패키지입니다.                                    | (2.x 개발중) | MIT |
| `koalanlp-kmr`     | 코모란 Wrapper, 분석범위: 형태소                                       | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-kmr.svg?style=flat-square&label=r)](http://search.maven.org/search?q=koalanlp-kmr)         | Apache 2.0 |
| `koalanlp-eunjeon` | 은전한닢 Wrapper, 분석범위: 형태소                                     | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-eunjeon.svg?style=flat-square&label=r)](http://search.maven.org/search?q=koalanlp-eunjeon) | Apache 2.0 |
| `koalanlp-arirang` | 아리랑 Wrapper, 분석범위: 형태소 <sup>2-1</sup>                        | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-arirang.svg?style=flat-square&label=r)](http://search.maven.org/search?q=koalanlp-arirang) | Apache 2.0 |
| `koalanlp-rhino`   | RHINO Wrapper, 분석범위: 형태소 <sup>2-1</sup>                        | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-rhino.svg?style=flat-square&label=r)](http://search.maven.org/search?q=koalanlp-rhino)     | GPL v3 |
| `koalanlp-daon`    | Daon Wrapper, 분석범위: 형태소 <sup>2-1</sup>                         | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-daon.svg?style=flat-square&label=r)](http://search.maven.org/search?q=koalanlp-daon)       | MIT(별도 지정 없음) |
| `koalanlp-khaiii`  | Kakao Khaiii Wrapper / 분석범위: 형태소 <sup>2-3</sup>                 | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-khaiii.svg?style=flat-square&label=r)](http://search.maven.org/search?q=koalanlp-khaiii)       | Apache 2.0 |
| `koalanlp-okt`     | Open Korean Text Wrapper, 분석범위: 문장분리, 형태소                    | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-okt.svg?style=flat-square&label=r)](http://search.maven.org/search?q=koalanlp-okt)        | Apache 2.0  |
| `koalanlp-kkma`    | 꼬꼬마 Wrapper, 분석범위: 형태소, 의존구문 <sup>2-1</sup>                | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-kkma.svg?style=flat-square&label=r)](http://search.maven.org/search?q=koalanlp-kkma)       | GPL v2    |
| `koalanlp-hnn`     | 한나눔 Wrapper, 분석범위: 문장분리, 형태소, 구문분석, 의존구문 <sup>2-1</sup>| [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-hnn.svg?style=flat-square&label=r)](http://search.maven.org/search?q=koalanlp-hnn)        | GPL v3    |
| `koalanlp-etri`    | ETRI Open API Wrapper, 분석범위: 형태소, 구문분석, 의존구문, 개체명, 의미역 | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-etri.svg?style=flat-square&label=r)](http://search.maven.org/search?q=koalanlp-etri)      | MIT<sup>2-2</sup> |

> <sup>주2-1</sup> 꼬꼬마, 한나눔, 아리랑, RHINO 분석기는 타 분석기와 달리 Maven repository에 등재되어 있지 않아, 원래는 수동으로 직접 추가하셔야 합니다.
> 이 점이 불편하다는 것을 알기에, KoalaNLP는 assembly 형태로 해당 패키지를 포함하여 배포하고 있습니다. 포함된 패키지를 사용하려면, `assembly` classifier를 사용하십시오.
> "assembly" classifier가 지정되지 않으면, 각 분석기 라이브러리가 빠진 채로 dependency가 참조됩니다.
>
> <sup>주2-2</sup> ETRI의 경우 Open API를 접근하기 위한 코드 부분은 KoalaNLP의 License 정책에 귀속되지만, Open API 접근 이후의 사용권에 관한 조항은 ETRI에서 별도로 정한 바를 따릅니다.
> 따라서, ETRI의 사용권 조항에 동의하시고 키를 발급하셔야 하며, 다음 위치에서 발급을 신청할 수 있습니다: [키 발급 신청](http://aiopen.etri.re.kr/key_main.php)
>
> <sup>주2-3</sup> Khaiii 분석기의 경우는 Java가 아닌 C++로 구현되어 사용 전 분석기의 설치가 필요합니다. Python3.6 및 CMake 3.10+만 설치되어 있다면 설치 자체가 복잡한 편은 아니니 [여기](https://github.com/kakao/khaiii/blob/v0.1/doc/setup.md)를 참조하여 설치해보세요. (단, v0.1에서는 빌드시 'python3' 호출시 'python3.6'이 연결되어야 합니다.) 참고로, KoalaNLP가 Travis CI에서 패키지를 자동 테스트하기 위해 구현된 bash script는 [여기](https://github.com/koalanlp/koalanlp/blob/master/khaiii/install.sh)에 있습니다.

## 실행환경
* KoalaNLP의 모든 코드는 Java 8을 기준으로 컴파일되고 OpenJDK 11에서 테스트 되었습니다.
* Kotlin의 경우는 1.3.10이 기준입니다.
* Scala의 경우는 `KoalaNLP-scala` 패키지의 버전에 따릅니다.
  단, 은전한닢과 Open Korean Text는 의존하는 라이브러리가 Scala에서 개발되어 Scala에서 사용시 제한이 있을 수 있으며, 이는 [![scala-koalanlp](https://img.shields.io/badge/Scala-KoalaNLP-red.svg?style=flat-square)](https://koalanlp.github.io/scala-support)를 참조하십시오.  
* Python과 NodeJS는 각각의 저장소를 참고하십시오: 
    [![nodejs-koalanlp](https://img.shields.io/badge/Nodejs-KoalaNLP-green.svg?style=flat-square)](https://koalanlp.github.io/nodejs-support)
    [![py-koalanlp](https://img.shields.io/badge/Python-KoalaNLP-blue.svg?style=flat-square)](https://koalanlp.github.io/python-support)

## Gradle
```groovy
ext.koala_version = '2.0.5'

repositories {
    mavenCentral()
    jcenter()
    maven { url "https://jitpack.io" } // 코모란의 경우에만 추가.
}

dependencies{
    // 코모란의 경우
    implementation "kr.bydelta:koalanlp-kmr:${ext.koala_version}" 
    // 은전한닢 프로젝트(Mecab-ko)의 경우
    implementation "kr.bydelta:koalanlp-eunjeon:${ext.koala_version}"
    // 아리랑의 경우
    implementation "kr.bydelta:koalanlp-arirang:${ext.koala_version}:assembly"
    // RHINO의 경우 
    implementation "kr.bydelta:koalanlp-rhino:${ext.koala_version}:assembly"
    // Daon의 경우
    implementation "kr.bydelta:koalanlp-daon:${ext.koala_version}"
    // OpenKoreanText의 경우
    implementation "kr.bydelta:koalanlp-okt:${ext.koala_version}" 
    // 꼬꼬마의 경우
    implementation "kr.bydelta:koalanlp-kkma:${ext.koala_version}:assembly"
    // 한나눔의 경우
    implementation "kr.bydelta:koalanlp-hnn:${ext.koala_version}:assembly" 
    // ETRI Open API의 경우
    implementation "kr.bydelta:koalanlp-etri:${ext.koala_version}"
    // Khaiii의 경우 (Khaiii C++ 별도 설치 필요)
    implementation "kr.bydelta:koalanlp-khaiii:2.0.0-alpha.1"
    // REST Server Service의 경우 (준비중)
    implementation "kr.bydelta:koalanlp-server:${ext.koala_version}"
}
```

## SBT
(버전은 Latest Release 기준입니다. SNAPSHOT을 사용하시려면, `latest.integration`을 사용하세요.)
```sbtshell
val koalaVer = "2.0.0"

// 코모란 분석기의 경우
resolvers += "jitpack" at "https://jitpack.io/"
libraryDependencies += "kr.bydelta" % "koalanlp-kmr" % koalaVer

// 은전한닢 분석기의 경우
libraryDependencies += "kr.bydelta" % "koalanlp-eunjeon" % koalaVer

// 아리랑 분석기의 경우
libraryDependencies += "kr.bydelta" % "koalanlp-arirang" % koalaVer classifier "assembly"

// RHINO 분석기의 경우
libraryDependencies += "kr.bydelta" % "koalanlp-rhino" % koalaVer classifier "assembly"

// Daon 분석기의 경우
libraryDependencies += "kr.bydelta" % "koalanlp-daon" % koalaVer

// Open Korean Text 분석기의 경우
libraryDependencies += "kr.bydelta" % "koalanlp-okt" % koalaVer

// 꼬꼬마 분석기의 경우
libraryDependencies += "kr.bydelta" % "koalanlp-kkma" % koalaVer classifier "assembly"

// 한나눔 분석기의 경우
libraryDependencies += "kr.bydelta" % "koalanlp-hannanum" % koalaVer classifier "assembly"

// ETRI 분석기의 경우
resolvers += Resolver.JCenterRepository
libraryDependencies += "kr.bydelta" % "koalanlp-etri" % koalaVer

// Khaiii 분석기의 경우 (Khaiii C++ 별도 설치 필요)
resolvers += Resolver.JCenterRepository
libraryDependencies += "kr.bydelta" % "koalanlp-khaiii" % "2.0.0-alpha.1"

// REST Server Service의 경우 (준비중)
libraryDependencies += "kr.bydelta" % "koalanlp-server" % koalaVer
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
