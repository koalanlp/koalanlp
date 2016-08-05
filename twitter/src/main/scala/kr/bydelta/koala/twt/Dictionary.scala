package kr.bydelta.koala.twt

import com.twitter.penguin.korean.util.{KoreanDictionaryProvider, KoreanPos}
import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.tagToTwt
import kr.bydelta.koala.traits.CanUserDict

import scala.collection.mutable.ListBuffer

/**
  * 트위터 분석기 사용자사전
  */
object Dictionary extends CanUserDict {
  private val userDict = ListBuffer[(String, POSTag)]()

  override def addUserDictionary(dict: (String, POSTag)*): Unit = {
    userDict ++= dict
    dict.groupBy(x => KoreanPos withName tagToTwt(x._2)).foreach {
      case (twtTag, seq) =>
        add(twtTag, seq.map(_._1))
    }
  }

  /**
    * Add morphemes
    *
    * @param tag   POS Tag
    * @param morph Morpheme sequence.
    */
  private def add(tag: KoreanPos.KoreanPos, morph: Seq[String]) =
  tag match {
    case KoreanPos.Noun | KoreanPos.Determiner | KoreanPos.Exclamation | KoreanPos.Josa | KoreanPos.Eomi |
         KoreanPos.PreEomi | KoreanPos.Conjunction | KoreanPos.NounPrefix | KoreanPos.VerbPrefix |
         KoreanPos.Suffix | KoreanPos.Adverb =>
      KoreanDictionaryProvider.addWordsToDictionary(tag, morph)
    case KoreanPos.Verb | KoreanPos.Adjective =>
    case _ =>
      KoreanDictionaryProvider.addWordsToDictionary(KoreanPos.Noun, morph)
  }

  override def addUserDictionary(morph: String, tag: POSTag): Unit = {
    userDict += morph -> tag
    add(KoreanPos withName tagToTwt(tag), Seq(morph))
  }

  /**
    * 사용자 사전에 등재된 모든 Item을 불러옵니다.
    *
    * @return (형태소, 통합품사)의 Sequence.
    */
  override def items: Seq[(String, POSTag)] = userDict
}
