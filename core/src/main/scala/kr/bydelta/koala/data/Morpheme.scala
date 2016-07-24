package kr.bydelta.koala.data

import kr.bydelta.koala.Processor.Processor

/**
  * 형태소 class
  *
  * @param surface   형태소 표면형 String
  * @param rawTag    원본 형태소 분석기의 품사 String
  * @param processor 형태소 분석을 실시한 분석기
  */
class Morpheme(val surface: String, val rawTag: String, var processor: Processor) {
  /**
    * 통합 품사
    */
  val tag = processor integratedPOSOf rawTag

  /**
    * 체언^명사, 수사, 대명사^ 형태소인지 확인.
    *
    * @return True : 체언 형태소일 경우.
    */
  final def isNoun: Boolean = tag.toString.startsWith("N")

  /**
    * 용언^동사, 형용사^ 형태소인지 확인.
    *
    * @return True : 용언 형태소일 경우.
    */
  final def isVerb: Boolean = tag.toString.startsWith("V")

  /**
    * 수식언^관형사, 부사^ 형태소인지 확인.
    *
    * @return True : 수식언 형태소일 경우.
    */
  final def isModifier: Boolean = tag.toString.startsWith("M")

  /**
    * 관계언^조사^ 형태소인지 확인.
    *
    * @return True : 관계언 형태소일 경우.
    */
  final def isJosa: Boolean = tag.toString.startsWith("J")

  /**
    * 통합 품사가 주어진 품사 표기에 대응하는지 확인.
    * <br/>
    * 예를 들어, N은 체언인지 확인하고, NP는 대명사인지 확인.
    * 품사 표기는 [[https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing 여기]]
    * 에서 확인
    *
    * @param tag 확인할 품사 표기 String
    * @return True: 주어진 품사를 가질경우
    */
  final def hasTag(tag: String): Boolean = this.tag.toString.startsWith(tag)

  /**
    * 원본 품사가 주어진 품사 표기에 대응하는지 확인.
    * <br/>
    * 각 분석기별 품사 표기는 [[https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing 여기]]
    * 에서 확인
    *
    * @param tag 확인할 품사 표기 String
    * @return True: 주어진 품사를 가질경우
    */
  final def hasRawTag(tag: String): Boolean = rawTag.startsWith(tag)

  /**
    * 타 형태소 객체와 형태소의 표면형이 같은지 비교함.
    *
    * @param another 비교할 형태소 객체
    * @return True: 표면형이 같은 경우
    */
  def equalsWithoutTag(another: Morpheme): Boolean = another.surface == this.surface

  /**
    * String형태로 변환
    *
    * @return "표면형/통합품사표기(원본품사표기)"형태의 String
    */
  override def toString: String = s"$surface/$tag($rawTag)"

  /**
    * 두 객체가 같은지 비교.
    *
    * @param obj 비교할 객체.
    * @return True: 같은 경우.
    */
  override def equals(obj: Any): Boolean = {
    obj match {
      case m: Morpheme =>
        m.surface == this.surface && m.tag == this.tag
      case s: String =>
        s == this.surface
      case _ =>
        false
    }
  }
}
