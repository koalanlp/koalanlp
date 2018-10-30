# Package kr.bydelta.koala.rhino

Open Korean Text 형태소분석기의 Wrapper를 정의합니다.

Open Korean Text 형태소 분석기는 최석재님이 개발한 것으로,
원본 분석기는 [RHINO v2.5.4](https://github.com/SukjaeChoi/RHINO)에서 확인 가능합니다.

원본의 저작권 조항은 업로드 위치별로 다르게 지정되어 있습니다. 
- sourceforge에는 Creative Commons Attribution License로 지정되어 있으나 구체적인 조항은 확인할 수 없습니다.
- github에는 [GPL v3](https://tldrlegal.com/license/gnu-general-public-license-v3-(gpl-3))을 따른다고 DESCRIPTION 파일에 지정되어 있습니다.
따라서, 보수적인 추정에 따라, GPL v3를 따른다고 가정합니다.

KoalaNLP의 코드가 MIT license를 따르지만, 원본에 적용된 GPL v3의 저작권 조항에 따라,
본 패키지(`koalalnlp-rhino`)의 저작권 조항은 GPL v3를 자동으로 따르게 되므로, 이용시 주의하시길 바랍니다.


## 자바 및 스칼라 개발자를 위한 노트

- 여기 수록된 항목 중에서 Types는 Java의 Class를 의미합니다.
- Exception은 예외 발생에 대한 항목입니다.
- Extension은 Kotlin이 제공하는 Wrapper 기능입니다. 
  (만약 있다면), Java와 Scala에서 `kr.bydelta.koala.rhino.Util`의 Static Member로 참조됩니다.
- Property는 지정된 상수값입니다.
- Function은 Utility function의 모음입니다. 
  (만약 있다면), Java와 Scala에서 `kr.bydelta.koala.rhino.Util`의 Static Member로 참조됩니다.
