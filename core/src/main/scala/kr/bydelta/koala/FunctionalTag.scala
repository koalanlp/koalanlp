package kr.bydelta.koala

/**
  * 의존구문분석 표기의 종류.
  */
object FunctionalTag extends Enumeration {
  type FunctionalTag = Value
  /** ''주어'': 술어가 나타내는 동작이나 상태의 주체가 되는 말
    *
    * 주격 체언구(NP_SBJ), 명사 전성 용언구(VP_SBJ), 명사절(S_SBJ) */
  val Subject: FunctionalTag = Value

  /** ''목적어'': 타동사가 쓰인 문장에서 동작의 대상이 되는 말
    *
    * 목적격 체언구(NP_OBJ), 명사 전성 용언구(VP_OBJ), 명사절(S_OBJ) */
  val Object: FunctionalTag = Value

  /** ''보어'': 주어와 서술어만으로는 뜻이 완전하지 못한 문장에서, 그 불완전한 곳을 보충하여 뜻을 완전하게 하는 수식어.
    *
    * 보격 체언구(NP_CMP), 명사 전성 용언구(VP_CMP), 인용절(S_CMP) */
  val Complement: FunctionalTag = Value

  /** 체언 수식어(관형격). 관형격 체언구(NP_MOD), 관형형 용언구(VP_MOD), 관형절(S_MOD) */
  val Modifier: FunctionalTag = Value

  /** 용언 수식어(부사격). 부사격 체언구(NP_AJT), 부사격 용언구(VP_AJT) 문말어미+부사격 조사(S_AJT) */
  val Adjunct: FunctionalTag = Value

  /** ''접속어'': 단어와 단어, 구절과 구절, 문장과 문장을 이어 주는 구실을 하는 문장 성분.
    *
    * 접속격 체언(NP_CNJ) */
  val Conjunctive: FunctionalTag = Value

  /** ''독립어'': 문장의 다른 성분과 밀접한 관계없이 독립적으로 쓰는 말.
    *
    * 체언(NP_INT) */
  val Interjective: FunctionalTag = Value

  /** 삽입어구. 삽입된 성분의 기능표지 위치 (예: NP_PRN) */
  val Parenthetical: FunctionalTag = Value

  /** 정의되지 않음 */
  val Undefined: FunctionalTag = Value
}

