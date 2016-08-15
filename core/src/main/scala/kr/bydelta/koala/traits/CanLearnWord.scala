package kr.bydelta.koala.traits

import kr.bydelta.koala.POS

/**
  * 품사분석기가 분석하지 못한 신조어, 전문용어 등을 확인하여 추가하는 작업을 할 수 있는 Trait.
  *
  * @tparam S Sequence Type for corpora in Scala
  * @tparam J List Type for corpora in Java
  */
trait CanLearnWord[S, J] {
  /** 인식되지 않은 단어의 품사 **/
  protected final val UNKNOWN_TAGS = Seq(POS.UE, POS.UN)
  /** 조사 목록 (단음절, 앞단어 종성 종결) **/
  protected final val JOSA_SINGLE_JONG = Seq('이', '을', '과', '은')
  /** 조사 목록 (단음절, 앞단어 중성 종결) **/
  protected final val JOSA_SINGLE_NONE = Seq('가', '를', '와', '는', '로')
  /** 조사 목록 (단음절, 앞단어 종결 무관) **/
  protected final val JOSA_SINGLE_ALL = Seq('의', '에', '도', '서')
  /** 조사 목록 (다음절, 앞단어 종결 무관.) **/
  protected final val JOSA_LONG_ALL = Seq("에게", "에서", "과의", "에의", "에도", "서도")
  /** 조사 목록 (다음절, 앞단어 종성 종결) **/
  protected final val JOSA_LONG_JONG = Seq("으로", "와의")
  /** 조사 목록 (다음절, 앞단어 중성 종결) **/
  protected final val JOSA_LONG_NONE = Seq("과의")
  /** 호칭으로 붙는 의존 명사 및 명사형 전성어미 목록 (단음절) **/
  protected final val DEPS_CALL = Seq('씨', '님', '임')
  /** 호칭으로 붙는 의존 명사 및 명사형 전성어미 목록 (다음절) **/
  protected final val DEPS_CALL_LONG = Seq("하기", "되기")
  /** Type conversion from J to S **/
  protected val converter: J => S
  /**
    * 신조어 등을 등록할 사용자사전들.
    */
  protected val targets: Seq[CanCompileDict]

  /**
    * 품사분석기가 분석하지 못한 신조어, 전문용어 등을 파악.
    *
    * @param corpora       새로운 단어를 발굴할 말뭉치.
    * @param minOccurrence 단어 등록을 위한, 최소 출현 횟수. (기본값 10회)
    * @param minVariations 단어 등록을 위한, 최소 활용(변형) 횟수. (기본값 2회) 용언의 경우는 활용형의 변화를, 체언의 경우는 조사의 변화를 파악함.
    * @return 새로운 단어와 그 품사의 Sequence.
    */
  def extractNouns(corpora: S, minOccurrence: Int = 10, minVariations: Int = 2): Stream[String]

  /**
    * (Java) 품사분석기가 분석하지 못한 신조어, 전문용어 등을 확인하여 추가
    *
    * @param corpora       새로운 단어를 발굴할 말뭉치.
    * @param minOccurrence 단어 등록을 위한, 최소 출현 횟수. (기본값 10회)
    * @param minVariations 단어 등록을 위한, 최소 활용(변형) 횟수. (기본값 2회) 용언의 경우는 활용형의 변화를, 체언의 경우는 조사의 변화를 파악함.
    */
  def jLearn(corpora: J, minOccurrence: Int = 10, minVariations: Int = 2): Unit =
  learn(converter(corpora), minOccurrence, minVariations)

  /**
    * 품사분석기가 분석하지 못한 신조어, 전문용어 등을 확인하여 추가
    *
    * @param corpora       새로운 단어를 발굴할 말뭉치.
    * @param minOccurrence 단어 등록을 위한, 최소 출현 횟수. (기본값 10회)
    * @param minVariations 단어 등록을 위한, 최소 활용(변형) 횟수. (기본값 2회) 용언의 경우는 활용형의 변화를, 체언의 경우는 조사의 변화를 파악함.
    */
  def learn(corpora: S, minOccurrence: Int = 10, minVariations: Int = 2): Unit = {
    extractNouns(corpora, minOccurrence, minVariations).map(_ -> POS.NNP).sliding(100, 100).foreach {
      set =>
        targets.par.foreach(_.addUserDictionary(set: _*))
    }
  }

  /**
    * 단어의 원형과 조사를 Heuristic으로 분리.
    *
    * @param word 분리할 어절.
    * @return (단어 원형, 조사)
    */
  protected def extractJosa(word: String): Option[(String, String)] =
  if (word.isEmpty) None
  else {
    val ch = word.last
    val lastTwo = word.takeRight(2)
    val isThirdJong = hasJongsung(word.applyOrElse(word.length - 3, _ => '가'))
    if (isHangul(ch) && word.length > 1) {
      if (JOSA_LONG_ALL contains lastTwo) Some(word.splitAt(word.length - 2))
      else if (isThirdJong && JOSA_LONG_JONG.contains(lastTwo)) Some(word.splitAt(word.length - 2))
      else if (!isThirdJong && JOSA_LONG_NONE.contains(lastTwo)) Some(word.splitAt(word.length - 2))
      else if (JOSA_SINGLE_ALL contains ch)
        Some(word.splitAt(word.length - 1))
      else {
        val secondLast = word(word.length - 2)
        val isJong = hasJongsung(secondLast)
        if (isJong && JOSA_SINGLE_JONG.contains(ch))
          Some(word.splitAt(word.length - 1))
        else if (!isJong && JOSA_SINGLE_NONE.contains(ch))
          Some(word.splitAt(word.length - 1))
        else
          None
      }
    } else None
  }

  /**
    * (Code modified from Seunjeon package)
    * 종성이 있는지 확인.
    *
    * @param ch 종성이 있는지 확인할 글자.
    * @return 종성이 있다면, true
    */
  private def hasJongsung(ch: Char) = {
    ((ch - 0xAC00) % 0x001C) != 0
  }

  /**
    * (Code modified from Seunjeon package)
    * 한글 문자인지 확인.
    *
    * @param ch 확인할 글자.
    * @return True: 한글일 경우.
    */
  private def isHangul(ch: Char): Boolean = {
    (0x0AC00 <= ch && ch <= 0xD7A3) || (0x1100 <= ch && ch <= 0x11FF) || (0x3130 <= ch && ch <= 0x318F)
  }

}


