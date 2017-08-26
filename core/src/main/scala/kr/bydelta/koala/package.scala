package kr.bydelta

import scala.annotation.tailrec

/**
  * <h3>통합 Scala 한국어 분석기, Koala</h3>
  *
  * <p>각 Package의 Parser와 Tagger를 참조하세요.</p>
  */
package object koala {

  /** '가' 위치 **/
  final val HANGUL_START: Int = '가'
  /** '힣' 위치 **/
  final val HANGUL_END: Int = '힣'
  /** 종성 범위 **/
  final val JONGSUNG_RANGE = 0x001C
  /** 중성 범위 **/
  final val JUNGSUNG_RANGE = 0x024C
  protected[koala] final val ALPHA_PRON = Seq(
    "에이치", "더블유", "에이", "에프", "아이", "제이", "케이",
    "에스", "브이", "엑스", "와이", "제트", "비", "씨", "디",
    "이", "지", "엘", "엠", "엔", "오", "피", "큐", "알",
    "티", "유"
  )
  protected[koala] final val ALPHA_PRON_ORDER = Seq(
    'H', 'W', 'A', 'F', 'I', 'J', 'K', 'S', 'V', 'X', 'Y', 'Z',
    'B', 'C', 'D', 'E', 'G', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'T', 'U'
  )

  /**
    * 세종품사를 통합품사로 변환.
    *
    * @param tag 통합품사로 변환할 원본표기.
    * @return 변환된 통합품사.
    */
  def fromSejongTag(tag: String): POS.Value = {
    tag.toUpperCase match {
      case "SO" | "SW" => POS.SY
      case "NF" => POS.UN
      case "NV" => POS.UV
      case "NA" => POS.UE
      case "SH" => POS.SL
      case x => POS withName x
    }
  }

  /**
    * 통합품사를 세종품사로 변환.
    *
    * @param tag 세종품사로 변환할 원본표기.
    * @return 변환된 품사.
    */
  def tagToSejong(tag: POS.Value): String = {
    tag match {
      case POS.SY => "SW"
      case POS.UN => "NF"
      case POS.UV => "NV"
      case POS.UE => "NA"
      case POS.NNM => "NNB"
      case POS.XPV => "XR"
      case x => x.toString
    }
  }

  /**
    * 알파벳 발음나는 대로 국문표기
    *
    * @param x   변환할 문자열.
    * @param acc (누적변수)
    * @return 변환한 문자열
    */
  @tailrec
  final def pronounceAlphabet(x: String, acc: String = ""): String =
    if (x.isEmpty) acc
    else {
      pronounceAlphabet(x.tail, acc + ALPHA_PRON(ALPHA_PRON_ORDER.indexOf(x.head)))
    }

  /**
    * 알파벳 발음을 알파벳으로 변환
    *
    * @param x   변환할 문자열.
    * @param acc (누적변수)
    * @return 변환된 문자열.
    */
  @tailrec
  final def writeAlphabet(x: String, acc: String = ""): String =
    if (x.isEmpty) acc
    else {
      ALPHA_PRON.indexWhere(x.startsWith) match {
        case -1 => acc + x
        case ind => writeAlphabet(x.substring(ALPHA_PRON(ind).length), acc + ALPHA_PRON_ORDER(ind))
      }
    }

  /**
    * 발음나는대로 표기된 알파벳인지 확인.
    *
    * @param word 확인할 단어.
    * @return True: 단어 전체가 발음나는대로 표기된 경우.
    */
  @tailrec
  final def isAlphabetPronounced(word: String): Boolean =
    if (word.isEmpty) true
    else {
      ALPHA_PRON.find(word.startsWith) match {
        case Some(p) => isAlphabetPronounced(word.substring(p.length))
        case None => false
      }
    }

  /**
    * 한글 문자 재구성
    *
    * @param cho  초성 위치
    * @param jung 중성 위치
    * @param jong 종성 위치
    * @return 조합된 문자
    */
  def reconstructKorean(cho: Int = 11, jung: Int = 0, jong: Int = 0): Char =
    (HANGUL_START + cho * JUNGSUNG_RANGE + jung * JONGSUNG_RANGE + jong).toChar

  /**
    * 한국어를 구성하는 문자의 연산.
    *
    * @param ch 검사할 문자.
    */
  implicit class KoreanCharacterExtension(ch: Char) {
    /**
      * (Code modified from Seunjeon package)
      * 종성으로 끝나는지 확인.
      *
      * @return 종성으로 끝난다면, true를, 없다면 false를 반환.
      */
    def endsWithJongsung = isCompleteHangul && getJongsungCode != 0

    /**
      * (Code modified from Seunjeon package)
      * 종성 종료 코드
      *
      * @return 종성으로 끝난다면, 해당 위치를, 없다면 0을 반환.
      */
    def getJongsungCode =
    if (isJongsungJamo) ch - 0x11A7
    else if (isCompleteHangul) (ch - HANGUL_START) % JONGSUNG_RANGE
    else 0

    /**
      * 한글의 종성자음 문자인지 확인
      *
      * @return True: 종료 문자가 종성자음인 경우.
      */
    def isJongsungJamo = 0x11A7 <= ch && ch <= 0x11C2

    /**
      * 완성된 문자의 범위인지 확인.
      *
      * @return True: 완성된 문자일 경우.
      */
    def isCompleteHangul = HANGUL_START <= ch && ch <= HANGUL_END

    /**
      * (Code modified from Seunjeon package)
      * 한글 문자로 끝나는 지 확인.
      *
      * @return True: 종료 문자가 한글일 경우.
      */
    def isHangul = {
      ((HANGUL_START <= ch && ch <= HANGUL_END)
        || (0x1100 <= ch && ch <= 0x11FF)
        || (0x3130 <= ch && ch <= 0x318F))
    }

    /**
      * (Code modified from Seunjeon package)
      * 한글의 불완전 문자인지 확인
      *
      * @return True: 종료 문자가 한글일 경우.
      */
    def isIncompleteHangul = {
      ((0x1100 <= ch && ch <= 0x11FF)
        || (0x3130 <= ch && ch <= 0x318F))
    }

    /**
      * 한글의 자모를 분리함.
      *
      * @return 분리된 자모의 Sequence. (종성없는 경우는 종성자리에 0x11A7)
      */
    def toDissembledSeq =
    if (isCompleteHangul)
      Seq((0x1100 + getChosungCode).toChar,
        (0x1161 + getJungsungCode).toChar,
        (0x11A7 + getJongsungCode).toChar)
    else Seq(ch)

    /**
      * (Code modified from Seunjeon package)
      * 중성 종료 코드
      *
      * @return 중성으로 끝난다면, 해당 위치를, 없다면 -1을 반환.
      */
    def getJungsungCode =
    if (isCompleteHangul) (ch - HANGUL_START) % JUNGSUNG_RANGE / JONGSUNG_RANGE
    else if (isJungsungJamo) ch - 0x1161
    else -1

    /**
      * 한글의 중성자음 문자인지 확인
      *
      * @return True: 종료 문자가 중성자음인 경우.
      */
    def isJungsungJamo = 0x1161 <= ch && ch <= 0x1175

    /**
      * (Code modified from Seunjeon package)
      * 초성 종료 코드
      *
      * @return 초성으로 끝난다면, 해당 위치를, 없다면 -1을 반환.
      */
    def getChosungCode =
    if (isCompleteHangul) (ch - HANGUL_START) / JUNGSUNG_RANGE
    else if (isChosungJamo) ch - 0x1100
    else -1

    /**
      * 한글의 초성자음 문자인지 확인
      *
      * @return True: 종료 문자가 초성자음인 경우.
      */
    def isChosungJamo = 0x1100 <= ch && ch <= 0x1112
  }

  /**
    * 한국어를 구성하는 문자열의 연산.
    *
    * @param word 검사할 문자열.
    */
  implicit class KoreanStringExtension(word: String) {
    /**
      * (Code modified from Seunjeon package)
      * 종성으로 끝나는지 확인.
      *
      * @return 종성으로 끝난다면, true를, 없다면 false를 반환.
      */
    def endsWithJongsung = word.last.endsWithJongsung

    /**
      * (Code modified from Seunjeon package)
      * 한글 문자로 끝나는 지 확인.
      *
      * @return True: 종료 문자가 한글일 경우.
      */
    def endsWithHangul = word.last.isHangul

    /**
      * 한글이 아닌 문자 제거
      *
      * @return 제거된 문자열
      */
    def filterNonHangul = {
      word.replaceAll("(?U)[^가-힣\\s]+", " ").trim.split("(?U)\\s+").toSeq
    }

    /**
      * 한글 자모를 분리함
      *
      * @return 자모가 분리된 문자열
      */
    def dissembleHangul: String = {
      word.flatMap(_.toDissembledSeq)
    }
  }
}