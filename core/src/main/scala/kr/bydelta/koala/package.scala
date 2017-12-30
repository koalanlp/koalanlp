package kr.bydelta

import scala.annotation.tailrec

/**
  * = KoalaNLP =
  *
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
  final val JONGSUNG_RANGE: Int = 0x001C
  /** 중성 범위 **/
  final val JUNGSUNG_RANGE: Int = 0x024C
  final val ALPHABET_READING: Seq[(Char, String)] = Map(
    'H' -> "에이치", 'W' -> "더블유",
    'A' -> "에이", 'F' -> "에프", 'I' -> "아이", 'J' -> "제이", 'K' -> "케이",
    'S' -> "에스", 'V' -> "브이", 'X' -> "엑스", 'Y' -> "와이", 'Z' -> "제트",
    'B' -> "비", 'C' -> "씨", 'D' -> "디", 'E' -> "이", 'G' -> "지",
    'L' -> "엘", 'M' -> "엠", 'N' -> "엔", 'O' -> "오", 'P' -> "피",
    'Q' -> "큐", 'R' -> "알", 'T' -> "티", 'U' -> "유"
  ).toSeq.sortBy(-_._2.length)

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
      val head = x.head
      ALPHABET_READING.find(_._1 == head) match {
        case Some((_, pron)) =>
          pronounceAlphabet(x.tail, acc + pron)
        case None =>
          pronounceAlphabet(x.tail, acc + head)
      }
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
      ALPHABET_READING.find {
        case (_, pron) => x.startsWith(pron)
      } match {
        case None => acc + x
        case Some((ch, pron)) => writeAlphabet(x.substring(pron.length), acc + ch)
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
      ALPHABET_READING.find {
        case (_, pron) => word.startsWith(pron)
      } match {
        case Some((_, p)) => isAlphabetPronounced(word.substring(p.length))
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
    def endsWithJongsung: Boolean = isCompleteHangul && getJongsungCode != 0

    /**
      * (Code modified from Seunjeon package)
      * 한글 문자로 끝나는 지 확인.
      *
      * @return True: 종료 문자가 한글일 경우.
      */
    def isHangul: Boolean = {
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
    def isIncompleteHangul: Boolean = {
      ((0x1100 <= ch && ch <= 0x11FF)
        || (0x3130 <= ch && ch <= 0x318F))
    }

    /**
      * 한글의 자모를 분리함.
      *
      * @return 분리된 자모의 Sequence. (종성없는 경우는 종성자리에 0x11A7)
      */
    def toDissembledSeq: Seq[Char] =
    if (isCompleteHangul)
      Seq((0x1100 + getChosungCode).toChar,
        (0x1161 + getJungsungCode).toChar,
        (0x11A7 + getJongsungCode).toChar)
    else Seq(ch)

    /**
      * (Code modified from Seunjeon package)
      * 종성 종료 코드
      *
      * @return 종성으로 끝난다면, 해당 위치를, 없다면 0을 반환.
      */
    def getJongsungCode: Int =
      if (isJongsungJamo) ch - 0x11A7
      else if (isCompleteHangul) (ch - HANGUL_START) % JONGSUNG_RANGE
      else 0

    /**
      * 한글의 종성자음 문자인지 확인
      *
      * @return True: 종료 문자가 종성자음인 경우.
      */
    def isJongsungJamo: Boolean = 0x11A7 <= ch && ch <= 0x11C2

    /**
      * 완성된 문자의 범위인지 확인.
      *
      * @return True: 완성된 문자일 경우.
      */
    def isCompleteHangul: Boolean = HANGUL_START <= ch && ch <= HANGUL_END

    /**
      * (Code modified from Seunjeon package)
      * 중성 종료 코드
      *
      * @return 중성으로 끝난다면, 해당 위치를, 없다면 -1을 반환.
      */
    def getJungsungCode: Int =
    if (isCompleteHangul) (ch - HANGUL_START) % JUNGSUNG_RANGE / JONGSUNG_RANGE
    else if (isJungsungJamo) ch - 0x1161
    else -1

    /**
      * 한글의 중성자음 문자인지 확인
      *
      * @return True: 종료 문자가 중성자음인 경우.
      */
    def isJungsungJamo: Boolean = 0x1161 <= ch && ch <= 0x1175

    /**
      * (Code modified from Seunjeon package)
      * 초성 종료 코드
      *
      * @return 초성으로 끝난다면, 해당 위치를, 없다면 -1을 반환.
      */
    def getChosungCode: Int =
    if (isCompleteHangul) (ch - HANGUL_START) / JUNGSUNG_RANGE
    else if (isChosungJamo) ch - 0x1100
    else -1

    /**
      * 한글의 초성자음 문자인지 확인
      *
      * @return True: 종료 문자가 초성자음인 경우.
      */
    def isChosungJamo: Boolean = 0x1100 <= ch && ch <= 0x1112

    /**
      * 한자 범위인지 확인.
      *
      * @return True: 한자 범위인 경우.
      */
    def isHanja: Boolean =
      (0x2E80 <= ch && ch <= 0x2EFF) ||
        //    한중일 부수 보충 	 2E80 	 2EFF
        (0x3400 <= ch && ch <= 0x4DBF) ||
        //    한중일 통합 한자 확장 - A 	 3400 	 4DBF
        (0x4E00 <= ch && ch <= 0x9FBF) ||
        //    한중일 통합 한자 	 4E00 	 9FBF
        (0xF900 <= ch && ch <= 0xFAFF) ||
        //    한중일 호환용 한자 	 F900 	 FAFF
        (0x20000 <= ch && ch <= 0x2A6DF) ||
        //    한중일 통합 한자 확장 	 20000 	 2A6DF
        (0x2F800 <= ch && ch <= 0x2FA1F)

    //    한중일 호환용 한자 보충 	 2F800 	 2FA1F
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
    def endsWithJongsung: Boolean = word.last.endsWithJongsung

    /**
      * (Code modified from Seunjeon package)
      * 한글 문자로 끝나는 지 확인.
      *
      * @return True: 종료 문자가 한글일 경우.
      */
    def endsWithHangul: Boolean = word.last.isHangul

    /**
      * 한글이 아닌 문자 제거
      *
      * @return 제거된 문자열
      */
    def filterNonHangul: String = {
      word.replaceAll("(?U)[^가-힣\\s]+", " ").trim
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