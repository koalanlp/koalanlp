package kr.bydelta.koala

/**
  * 의존구문분석 표기의 종류. (엑소브레인 기능표지 기준)
  *
  * [참고](http://aiopen.etri.re.kr/data/1.%20%EC%9D%98%EC%A1%B4%20%EA%B5%AC%EB%AC%B8%EB%B6%84%EC%84%9D%EC%9D%84%20%EC%9C%84%ED%95%9C%20%ED%95%9C%EA%B5%AD%EC%96%B4%20%EC%9D%98%EC%A1%B4%EA%B4%80%EA%B3%84%20%EA%B0%80%EC%9D%B4%EB%93%9C%EB%9D%BC%EC%9D%B8%20%EB%B0%8F%20%EC%97%91%EC%86%8C%EB%B8%8C%EB%A0%88%EC%9D%B8%20%EC%96%B8%EC%96%B4%EB%B6%84%EC%84%9D%20%EB%A7%90%EB%AD%89%EC%B9%98.pdf)
  */
object FunctionalTag extends Enumeration {
  type FunctionalTag = Value
  /** ''주어'': 술어가 나타내는 동작이나 상태의 주체가 되는 말
    *
    * 주격 체언구(NP_SBJ), 명사 전성 용언구(VP_SBJ), 명사절(S_SBJ) */
  val SBJ: FunctionalTag = Value

  /** ''목적어'': 타동사가 쓰인 문장에서 동작의 대상이 되는 말
    *
    * 목적격 체언구(NP_OBJ), 명사 전성 용언구(VP_OBJ), 명사절(S_OBJ) */
  val OBJ: FunctionalTag = Value

  /** ''보어'': 주어와 서술어만으로는 뜻이 완전하지 못한 문장에서, 그 불완전한 곳을 보충하여 뜻을 완전하게 하는 수식어.
    *
    * 보격 체언구(NP_CMP), 명사 전성 용언구(VP_CMP), 인용절(S_CMP) */
  val CMP: FunctionalTag = Value

  /** 체언 수식어(관형격). 관형격 체언구(NP_MOD), 관형형 용언구(VP_MOD), 관형절(S_MOD) */
  val MOD: FunctionalTag = Value

  /** 용언 수식어(부사격). 부사격 체언구(NP_AJT), 부사격 용언구(VP_AJT) 문말어미+부사격 조사(S_AJT) */
  val AJT: FunctionalTag = Value

  /** ''접속어'': 단어와 단어, 구절과 구절, 문장과 문장을 이어 주는 구실을 하는 문장 성분.
    *
    * 접속격 체언(NP_CNJ) */
  val CNJ: FunctionalTag = Value

  /** 기능 표지가 없음 (다른 구와 연속하여 연결된 경우): 기존 INT(감탄어구) 포함 */
  val EMPTY: FunctionalTag = Value

  /** 정의되지 않음: 기존 PRN(삽입어구) 포함 */
  val UNDEF: FunctionalTag = Value

  /** ROOT 지시자 */
  val ROOT: FunctionalTag = Value
}

