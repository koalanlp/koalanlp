KoreanAnalyzer
==============

한국어 형태소 및 구문 분석기의 모음입니다.
이 분석기는 KAIST의 한나눔 형태소 분석기와 그 이후에 추가된 KAIST의 구문분석기,
그리고 서울대의 꼬꼬마 구문 분석기가 들어있습니다.

사용방법은 간단합니다.

* 프로젝트 소스코드를 받아 컴파일합니다.
** 이 과정에서 models.zip은 jar파일에 포함되어야 합니다.
** Maven이 설치되어 있을 경우 필요한 연결 작업을 자동으로 수행하지만, 설치되어있지 않을 경우 lib 내부의 jar를 build path에 추가하셔야 합니다.
* 컴파일 된 소스코드를 사용하고자 하는 프로젝트에 첨부합니다.

다음과 같이 소스코드를 작성할 수 있습니다.
* 형태소 분석을 할 경우 Tagger의 확장클래스를 사용합니다.

[code]
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
[/code]

* 구문 분석을 할 경우 Parser의 확장 클래스를 사용합니다.

[code]
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
[/code]
