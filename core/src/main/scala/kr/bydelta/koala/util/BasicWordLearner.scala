package kr.bydelta.koala.util

import kr.bydelta.koala.POS
import kr.bydelta.koala.traits.{CanLearnWord, CanTag}

import scala.collection.mutable

/**
  * Basic implementation of word learner
  */
trait BasicWordLearner[S] extends CanLearnWord[S] {
  /** 인식되지 않은 단어의 품사 **/
  private final val UNKNOWN_TAGS = Seq(POS.UE, POS.UN)
  /** 조사 목록 (단음절, 앞단어 종성 종결) **/
  private final val JOSA_SINGLE_JONG = Seq('이', '을', '과', '은')
  /** 조사 목록 (단음절, 앞단어 중성 종결) **/
  private final val JOSA_SINGLE_NONE = Seq('가', '를', '와', '는', '로')
  /** 조사 목록 (단음절, 앞단어 종결 무관) **/
  private final val JOSA_SINGLE_ALL = Seq('의', '에', '도', '서')
  /** 조사 목록 (다음절, 앞단어 종결 무관.) **/
  private final val JOSA_LONG_ALL = Seq("에게", "에서")
  /** 조사 목록 (다음절, 앞단어 종성 종결) **/
  private final val JOSA_LONG_JONG = Seq("으로")
  /** Tagger **/
  protected val tagger: CanTag[_]

  def extract(corpora: Iterator[String], minOccurrence: Int = 100, minVariations: Int = 3): collection.Set[String] = {
    corpora.foldLeft(mutable.HashMap[String, mutable.HashMap[String, Int]]()) {
      case (map, para) =>
        val tagged = tagger.tagParagraph(para).flatMap(_.words)
        val words = tagged.filter { w =>
          w.morphemes.forall(m => m.isNoun || m.isJosa)
        }.map(w => w.surface.replaceAll("[^가-힣]+", "") -> w.count(_.isNoun))

        para.replaceAll("[^가-힣\\s]+", " ").split("\\s+").toSeq
          .filter(word => words.exists(w => word.endsWith(w._1) && (word.length > w._1.length || w._2 > 1)))
          .foreach { word =>
            val (root, josa) = extractJosa(word)
            val josamap = map.getOrElseUpdate(root, mutable.HashMap())
            josamap(josa) = josamap.getOrElse(josa, 0) + 1
          }
        map
    }.filter {
      case (word, josaMap) =>
        josaMap.values.sum > minOccurrence && josaMap.size > minVariations
    }.keySet
  }

  /**
    * 단어의 원형과 조사를 Heuristic으로 분리.
    *
    * @param word 분리할 어절.
    * @return (단어 원형, 조사)
    */
  private def extractJosa(word: String): (String, String) = {
    val ch = word.last
    val lastTwo = word.takeRight(2)
    if (isHangul(ch) && word.length > 1) {
      if (JOSA_LONG_ALL contains lastTwo) word.splitAt(word.length - 2)
      else if (JOSA_LONG_JONG contains lastTwo) word.splitAt(word.length - 2)
      else if (JOSA_SINGLE_ALL contains ch)
        word.splitAt(word.length - 1)
      else {
        val secondLast = word(word.length - 2)
        val isJong = hasJongsung(secondLast)
        if (isJong && JOSA_SINGLE_JONG.contains(ch))
          word.splitAt(word.length - 1)
        else if (!isJong && JOSA_SINGLE_NONE.contains(ch))
          word.splitAt(word.length - 1)
        else
          word -> ""
      }
    } else word -> ""
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
