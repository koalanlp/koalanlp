--------

[목차로 이동](./index.md)

--------

# 설치

- [Java, Kotlin, Scala의 경우](#Java,-Kotlin,-Scala)
- [NodeJS의 경우](#NodeJS)
- [Python3의 경우](#Python3)

## Java, Kotlin, Scala
### Java 패키지 목록

다음과 같은 하위 패키지가 있습니다.

| 패키지명            | 설명                                                                 |  버전    | License (원본)     |
| ------------------ | ------------------------------------------------------------------ | ------- | ------------ |
| `koalanlp-core`    | 통합 인터페이스의 정의가 등재된 중심 묶음입니다.                            | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-core.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-core%22) | MIT |
| `koalanlp-scala`   | Scala를 위한 편의기능 (Implicit conversion 등)                         | (준비중) | MIT |
| `koalanlp-server`  | HTTP 서비스 구성을 위한 패키지입니다.                                    | (2.x 개발중) | MIT |
| `koalanlp-kmr`     | 코모란 Wrapper, 분석범위: 형태소                                       | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-kmr.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-kmr%22)         | Apache 2.0 |
| `koalanlp-eunjeon` | 은전한닢 Wrapper, 분석범위: 형태소                                     | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-eunjeon.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-eunjeon%22) | Apache 2.0 |
| `koalanlp-arirang` | 아리랑 Wrapper, 분석범위: 형태소 <sup>2-1</sup>                        | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-arirang.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-arirang%22) | Apache 2.0 |
| `koalanlp-rhino`   | RHINO Wrapper, 분석범위: 형태소 <sup>2-1</sup>                        | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-rhino.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-rhino%22)     | GPL v3 |
| `koalanlp-daon`    | Daon Wrapper, 분석범위: 형태소 <sup>2-1</sup>                         | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-daon.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-daon%22)       | MIT(별도 지정 없음) |
| `koalanlp-okt`     | Open Korean Text Wrapper, 분석범위: 문장분리, 형태소                    | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-okt.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-okt%22)        | Apache 2.0  |
| `koalanlp-kkma`    | 꼬꼬마 Wrapper, 분석범위: 형태소, 의존구문 <sup>2-1</sup>                | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-kkma.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-kkma%22)       | GPL v2    |
| `koalanlp-hnn`     | 한나눔 Wrapper, 분석범위: 문장분리, 형태소, 구문분석, 의존구문 <sup>2-1</sup>| [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-hnn.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-hnn%22)        | GPL v3    |
| `koalanlp-etri`    | ETRI Open API Wrapper, 분석범위: 형태소, 구문분석, 의존구문, 개체명, 의미역 | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-etri.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-etri%22)      | MIT<sup>2-2</sup> |

> <sup>주2-1</sup> 꼬꼬마, 한나눔, 아리랑, RHINO 분석기는 타 분석기와 달리 Maven repository에 등재되어 있지 않아, 원래는 수동으로 직접 추가하셔야 합니다.
> 이 점이 불편하다는 것을 알기에, KoalaNLP는 assembly 형태로 해당 패키지를 포함하여 배포하고 있습니다. 포함된 패키지를 사용하려면, `assembly` classifier를 사용하십시오.
> "assembly" classifier가 지정되지 않으면, 각 분석기 라이브러리가 빠진 채로 dependency가 참조됩니다.
>
> <sup>주2-2</sup> ETRI의 경우 Open API를 접근하기 위한 코드 부분은 KoalaNLP의 License 정책에 귀속되지만, Open API 접근 이후의 사용권에 관한 조항은 ETRI에서 별도로 정한 바를 따릅니다.
> 따라서, ETRI의 사용권 조항에 동의하시고 키를 발급하셔야 하며, 다음 위치에서 발급을 신청할 수 있습니다: [키 발급 신청](http://aiopen.etri.re.kr/key_main.php)

### Gradle
```groovy
ext.koala_version = '2.0.0'

repositories {
    mavenCentral()
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
    // REST Server Service의 경우 (준비중)
    implementation "kr.bydelta:koalanlp-server:${ext.koala_version}"
}
```

### SBT
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
libraryDependencies += "kr.bydelta" % "koalanlp-etri" % koalaVer

// REST Server Service의 경우 (준비중)
libraryDependencies += "kr.bydelta" % "koalanlp-server" % koalaVer
```

### Maven
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

## NodeJS
우선 Java 8 이상을 설치하시고, 아래와 같이 설치하십시오.
```bash
$ npm install --save koalanlp
```

### 지원 API 유형
각 형태소 분석기는 별도의 패키지로 나뉘어 있습니다. 패키지 이름은 `koalanlp.API`에 상수로 정의되어 있습니다.

| 패키지명            | 설명                                                                 |  버전    | License (원본)     |
| ------------------ | ------------------------------------------------------------------ | ------- | ------------ |
| API.KMR          | 코모란 Wrapper, 분석범위: 형태소                                       | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-kmr.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-kmr%22)         | Apache 2.0 |
| API.EUNJEON      | 은전한닢 Wrapper, 분석범위: 형태소                                     | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-eunjeon.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-eunjeon%22) | Apache 2.0 |
| API.ARIRANG      | 아리랑 Wrapper, 분석범위: 형태소                                       | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-arirang.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-arirang%22) | Apache 2.0 |
| API.RHINO        | RHINO Wrapper, 분석범위: 형태소                                       | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-rhino.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-rhino%22)     | GPL v3 |
| API.DAON         | Daon Wrapper, 분석범위: 형태소                                        | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-daon.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-daon%22)       | MIT(별도 지정 없음) |
| API.OKT          | Open Korean Text Wrapper, 분석범위: 문장분리, 형태소                    | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-okt.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-okt%22)        | Apache 2.0  |
| API.KKMA         | 꼬꼬마 Wrapper, 분석범위: 형태소, 의존구문                               | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-kkma.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-kkma%22)       | GPL v2    |
| API.HNN          | 한나눔 Wrapper, 분석범위: 문장분리, 형태소, 구문분석, 의존구문               | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-hnn.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-hnn%22)        | GPL v3    |
| API.ETRI         | ETRI Open API Wrapper, 분석범위: 형태소, 구문분석, 의존구문, 개체명, 의미역 | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-etri.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-etri%22)      | MIT<sup>2-2</sup> |

> <sup>주2-2</sup> ETRI의 경우 Open API를 접근하기 위한 코드 부분은 KoalaNLP의 License 정책에 귀속되지만, Open API 접근 이후의 사용권에 관한 조항은 ETRI에서 별도로 정한 바를 따릅니다.
> 따라서, ETRI의 사용권 조항에 동의하시고 키를 발급하셔야 하며, 다음 위치에서 발급을 신청할 수 있습니다: [키 발급 신청](http://aiopen.etri.re.kr/key_main.php)

### 초기화
초기화 과정에서 KoalaNLP는 필요한 Java Library를 자동으로 다운로드하여 설치합니다. 설치에는 시간이 다소 소요됩니다.
때문에, 프로그램 실행시 최초 1회에 한하여 초기화 작업이 필요합니다.

```javascript
let koalanlp = require('koalanlp');
let TYPES = koalanlp.util.TYPES;

// 꼬꼬마와 은전한닢 분석기의 1.9.1 버전을 참조합니다.
koalanlp.initialize({
  packages: [TYPES.KKMA, TYPES.EUNJEON], 
  version: "1.9.1",
  javaOptions: ["-Xmx4g"],
  debug: true,
})
```

* 첫번째 인자는 초기화 option입니다.
  * `packages` 인자는 Python 프로그램에서 사용할 모든 패키지의 array입니다. (상단 표 참고)
  * `version` 인자는 다운로드할 해당 패키지의 버전 string입니다. (상단 표 참고)
  * `java_options` 인자는 JVM을 실행하기 위한 option string의 array입니다.
  * `debug`는 초기화 과정을 표시할지를 결정하는 인자입니다. `true`이면 초기화 과정이 표시됩니다.
* Promise 객체가 반환됩니다.
* 아래 문서는 초기화 과정이 모두 완료되었다고 보고 진행합니다.
* API 참고: [initialize](https://koalanlp.github.io/nodejs-support/module-koalanlp.html#.initialize)

## Python 3
우선 Java 8 이상을 설치하고, `JAVA_HOME`을 환경변수에 등록해주십시오.
그런 다음, 아래와 같이 설치하십시오.

```bash
$ pip install Cython # Cython은 별도 설치가 필요합니다.
$ pip install koalanlp
```

### Packages
각 형태소 분석기는 별도의 패키지로 나뉘어 있습니다.

| 패키지명            | 설명                                                                 |  버전    | License (원본)     |
| ------------------ | ------------------------------------------------------------------ | ------- | ------------ |
| API.KMR          | 코모란 Wrapper, 분석범위: 형태소                                       | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-kmr.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-kmr%22)         | Apache 2.0 |
| API.EUNJEON      | 은전한닢 Wrapper, 분석범위: 형태소                                     | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-eunjeon.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-eunjeon%22) | Apache 2.0 |
| API.ARIRANG      | 아리랑 Wrapper, 분석범위: 형태소                                       | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-arirang.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-arirang%22) | Apache 2.0 |
| API.RHINO        | RHINO Wrapper, 분석범위: 형태소                                       | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-rhino.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-rhino%22)     | GPL v3 |
| API.DAON         | Daon Wrapper, 분석범위: 형태소                                        | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-daon.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-daon%22)       | MIT(별도 지정 없음) |
| API.OKT          | Open Korean Text Wrapper, 분석범위: 문장분리, 형태소                    | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-okt.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-okt%22)        | Apache 2.0  |
| API.KKMA         | 꼬꼬마 Wrapper, 분석범위: 형태소, 의존구문                               | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-kkma.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-kkma%22)       | GPL v2    |
| API.HNN          | 한나눔 Wrapper, 분석범위: 문장분리, 형태소, 구문분석, 의존구문               | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-hnn.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-hnn%22)        | GPL v3    |
| API.ETRI         | ETRI Open API Wrapper, 분석범위: 형태소, 구문분석, 의존구문, 개체명, 의미역 | [![Version](https://img.shields.io/maven-central/v/kr.bydelta/koalanlp-etri.svg?style=flat-square&label=r)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22koalanlp-etri%22)      | MIT<sup>2-2</sup> |

> <sup>주2-2</sup> ETRI의 경우 Open API를 접근하기 위한 코드 부분은 KoalaNLP의 License 정책에 귀속되지만, Open API 접근 이후의 사용권에 관한 조항은 ETRI에서 별도로 정한 바를 따릅니다.
> 따라서, ETRI의 사용권 조항에 동의하시고 키를 발급하셔야 하며, 다음 위치에서 발급을 신청할 수 있습니다: [키 발급 신청](http://aiopen.etri.re.kr/key_main.php)

### 초기화
초기화 과정에서 KoalaNLP는 필요한 Java Library를 자동으로 다운로드하여 설치합니다. 설치에는 시간이 다소 소요됩니다.
때문에, 프로그램 실행시 최초 1회에 한하여 초기화 작업이 필요합니다.

```python
from koalanlp.Util import initialize

# 꼬꼬마와 은전한닢 분석기의 2.0.0 버전을 참조합니다.
initialize(packages=[API.KKMA, API.EUNJEON], version="2.0.0", java_options="-Xmx4g")
```

* `packages` 인자는 Python 프로그램에서 사용할 모든 패키지의 list입니다. (상단 표 참고)
* `version` 인자는 다운로드할 해당 패키지의 버전 string입니다. (상단 표 참고)
* `java_options` 인자는 JVM을 실행하기 위한 option string입니다.
* 아래 문서는 초기화 과정이 모두 완료되었다고 보고 진행합니다.
* API 참고: [initialize](https://koalanlp.github.io/python-support/html/koalanlp.html#koalanlp.Util.initialize)


--------

[목차로 이동](./index.md)

--------