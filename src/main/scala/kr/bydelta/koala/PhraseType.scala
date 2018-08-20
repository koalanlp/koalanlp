package kr.bydelta.koala

object PhraseType extends Enumeration {
  /**
    * 구문구조 표지자: 문장
    */
  val S: Value = Value
  /**
    * 구문구조 표지자: 체언 구
    *
    * 문장에서 주어 따위의 기능을 하는 명사, 대명사, 수사 또는 이 역할을 하는 구
    */
  val NP: Value = Value
  /**
    * 구문구조 표지자: 용언 구
    *
    * 문장에서 서술어의 기능을 하는 동사, 형용사 또는 이 역할을 하는 구
    */
  val VP: Value = Value
  /**
    * 구문구조 표지자: 긍정지정사구
    *
    * 무엇이 무엇이라고 지정하는 단어(이다) 또는 이 역할을 하는 구
    */
  val VNP: Value = Value
  /**
    * 구문구조 표지자: 부사구
    *
    * 용언구 또는 다른 말 앞에 놓여 그 뜻을 분명하게 하는 단어 또는 이 역할을 하는 구
    */
  val AP: Value = Value
  /**
    * 구문구조 표지자: 관형사구
    *
    * 체언구 앞에 놓여서, 그 체언구의 내용을 자세히 꾸며 주는 단어 또는 이 역할을 하는 구
    */
  val DP: Value = Value
  /**
    * 구문구조 표지자: 감탄사구
    *
    * 말하는 이의 본능적인 놀람이나 느낌, 부름, 응답 따위를 나타내는 단어 또는 이 역할을 하는 구
    */
  val IP: Value = Value
  /**
    * 구문구조 표지자: 의사(Pseudo) 구
    *
    * 인용부호와 괄호를 제외한 나머지 부호나, 조사, 어미가 단독으로 어절을 이룰 때 (즉, 구를 보통 이루지 않는 것이 구를 이루는 경우)
    */
  val X: Value = Value
  /**
    * 구문구조 표지자: 왼쪽 인용부호
    *
    * 열림 인용부호
    */
  val L: Value = Value
  /**
    * 구문구조 표지자: 오른쪽 인용부호
    *
    * 닫힘 인용부호
    */
  val R: Value = Value
  /**
    * 구문구조 표지자: 인용절
    *
    * 인용 부호 내부에 있는 인용된 절. 세종 표지에서는 Q, U, W, Y, Z가 사용되나 KoalaNLP에서는 하나로 통일함.
    */
  val Q: Value = Value
}
