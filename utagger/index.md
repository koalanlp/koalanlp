# Package kr.bydelta.koala.utagger

울산대학교 UTagger 형태소 분석기의 Wrapper를 정의합니다.

해당 API는 울산대학교 자연어처리연구실에서 개발한 것으로, 원본 분석기의 저작권은 해당 연구실에 있으며,
형태소 분석기는 [울산대학교 자연어처리 연구실](http://nlplab.ulsan.ac.kr/doku.php?id=start)에서 확인 가능합니다.

원본의 저작권 조항은 아래와 같으며,
KoalaNLP 코드의 저작권 조항은 MIT license를 따릅니다.

* UTagger 프로그램(윈도우즈용) 및 오픈소스는 교육 및 연구용으로 제한없이 사용할 수 있습니다.
* UTagger 라이브러리(윈도우즈/리눅스, C/C++/C#/JAVA/Python3), UWordMap API(C/C++/C#/JAVA/Python3), 말뭉치 등은 기술이전(연구용 무료, 상업용 유료)이 필요합니다.

## 참고

`utagger` 분석기는 C로 개발되어, 현재 KoalaNLP가 자동으로 설치하는 범위에는 포함되지 않습니다.
따라서, 사용 전 분석기의 설치가 필요합니다. 

울산대는 미리 컴파일 된 파일을 제공하므로, 설치 자체가 복잡한 편은 아니니 [여기](https://koalanlp.github.io/koalnlp/usage/Install-UTagger.md)를 참조하여 설치해보세요. (단, 일부 환경에서는 지원하지 않습니다.) 

참고로, KoalaNLP가 Travis CI에서 패키지를 자동 테스트하기 위해 구현된 bash script는 [여기](https://github.com/koalanlp/koalanlp/blob/master/utagger/install.sh)에 있습니다.

> **[참고]** Travis CI에서 울산대 FTP로 접근할 때, 다운로드에 시간이 오래 걸리기 때문에, KoalaNLP의 CI Test 과정에서는 울산대 Tagger에서 일부 사전을 null 처리 한 사전들을 사용하므로, Shell script 실행시 울산대 사전을 그대로 받는지 확인하십시오.

## 자바 및 스칼라 개발자를 위한 노트

- 여기 수록된 항목 중에서 Types는 Java의 Class를 의미합니다.
- Exception은 예외 발생에 대한 항목입니다.
- Extension은 Kotlin이 제공하는 Wrapper 기능입니다. 
  (만약 있다면), Java와 Scala에서 `kr.bydelta.koala.utagger.Util`의 Static Member로 참조됩니다.
- Property는 지정된 상수값입니다.
- Function은 Utility function의 모음입니다. 
  (만약 있다면), Java와 Scala에서 `kr.bydelta.koala.utagger.Util`의 Static Member로 참조됩니다.
