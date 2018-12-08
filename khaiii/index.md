# Package kr.bydelta.koala.khaiii

Kakao Khaiii 형태소 분석기의 Wrapper를 정의합니다.

해당 API는 Kakao 개발한 것으로, 원본 분석기의 저작권은 Kakao에 있으며,
형태소 분석기는 [Khaiii 형태소 분석기](http://github.com/kakao/khaiii)에서 확인 가능합니다.

원본의 저작권 조항은 [Apache 2.0](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0))을 따르며,
KoalaNLP 코드의 저작권 조항은 MIT license를 따릅니다.

## 참고

`khaiii` 분석기는 C++으로 개발되어, 현재 KoalaNLP가 자동으로 설치하는 범위에는 포함되지 않습니다.
따라서, 사용 전 분석기의 설치가 필요합니다. 

Python3.6 및 CMake 3.10+만 설치되어 있다면 설치 자체가 복잡한 편은 아니니 [여기](https://github.com/kakao/khaiii/blob/v0.1/doc/setup.md)를 참조하여 설치해보세요. (단, v0.1에서는 빌드시에만 'python3' 호출시 'python3.6'이 연결되어야 합니다.) 

참고로, KoalaNLP가 Travis CI에서 패키지를 자동 테스트하기 위해 구현된 bash script는 [여기](https://github.com/koalanlp/koalanlp/blob/master/khaiii/install.sh)에 있습니다.

## 자바 및 스칼라 개발자를 위한 노트

- 여기 수록된 항목 중에서 Types는 Java의 Class를 의미합니다.
- Exception은 예외 발생에 대한 항목입니다.
- Extension은 Kotlin이 제공하는 Wrapper 기능입니다. 
  (만약 있다면), Java와 Scala에서 `kr.bydelta.koala.khaiii.Util`의 Static Member로 참조됩니다.
- Property는 지정된 상수값입니다.
- Function은 Utility function의 모음입니다. 
  (만약 있다면), Java와 Scala에서 `kr.bydelta.koala.khaiii.Util`의 Static Member로 참조됩니다.
