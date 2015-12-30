KoreanAnalyzer v 0.2.2
==============

# 소개
한국어 형태소 및 구문 분석기의 모음입니다.

이 분석기는 KAIST의 [한나눔 형태소 분석기](http://kldp.net/projects/hannanum/)와 그 이후에 추가된 KAIST의 [NLP_HUB 구문분석기](http://semanticweb.kaist.ac.kr/home/index.php/NLP_HUB),
그리고 서울대의 [꼬꼬마 형태소/구문 분석기](http://kkma.snu.ac.kr/documents/index.jsp)가 들어있습니다.

이 프로젝트는 서로 다른 형태의 형태소 분석기를 모아,
동일한 인터페이스 아래에서 사용할 수 있도록 하는 것이 목적입니다.

이 프로젝트의 라이선스는 각 분석기의 라이선스를 쫓아 GPLv3를 따릅니다.

형태소 분석과 구문 분석의 결과는 세종 말뭉치의 지침에 따라 처리되었습니다.

# 사용방법

사용방법은 간단합니다.
자세한 사항은 apidocs(javadoc)을 참고하십시오.

## 컴파일

1. 프로젝트 소스코드를 받아 컴파일합니다.
 * 이 과정에서 models.zip은 jar파일에 포함되어야 합니다.
 * Maven이 설치되어 있을 경우 필요한 연결 작업을 자동으로 수행하지만, 설치되어있지 않을 경우 lib 내부의 jar를 build path에 추가하셔야 합니다.

2. 컴파일 된 소스코드를 사용하고자 하는 프로젝트에 첨부합니다.

## 작성

다음과 같이 소스코드를 작성할 수 있습니다.
* 형태소 분석을 할 경우 Tagger의 확장클래스를 사용합니다.

```java
// 각 Tagger 선언
Tagger iTagger = null;

// 각 Tagger 초기화
try {
	iTagger = new IntegratedTagger(ParseStructure.KKMA);
} catch (Exception e) {
	e.printStackTrace();
}
...

// 실질적인 분석 과정은 한 문장.
TaggedSentence s = iTagger.analyzeSentence(text);
```

* 구문 분석을 할 경우 Parser의 확장 클래스를 사용합니다.

```java
// 각 Parser 선언
Parser iParser = null;

// 각 Parser 초기화
try {
	iParser = new IntegratedParser();
} catch (Exception e) {
	e.printStackTrace();
}
...

// 실질적인 분석 과정은 한 문장.
TaggedSentence s = iParser.dependencyOf(text);
```

## 구조
### 사용가능한 분석기 클래스
* HannanumTagger : 한나눔 형태소 분석기
* KkokkomaTagger : 꼬꼬마 형태소 분석기
* IntegratedTagger : 두 형태소 분석기의 피상적 통합 (BETA)

 * TaggedSentence analyzeSentence(String)
 * List<TaggedSentence> analyzeParagraph(String)

* HannanumParser : 한나눔 구문 분석기
* KkokkomaParser : 꼬꼬마 구문 분석기
* IntegratedParser : 두 구문 분석기의 피상적 통합 (BETA)

 * TaggedSentence dependencyOf(String)

### 자료 클래스
모든 결과는 다음의 클래스를 사용합니다.

* TaggedSentence : 분석 결과인 문장입니다.
* TaggedWord : 분석 결과인 어절입니다.
* TaggedMorpheme : 분석 결과인 형태소입니다.

어절 간의 의존관계 역시 TaggedSentence를 사용하며, 의존관계는 Enum형으로 정의되어 있습니다.

** javadoc을 보시면 더 자세합니다. **

# Build with gradle
```
./gradlew build
```

# Run Testers with gradle
```
./gradlew runKoreanDependencyParser
./gradlew runKoreanPosTagger
```
