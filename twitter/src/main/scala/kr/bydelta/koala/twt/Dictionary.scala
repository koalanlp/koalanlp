package kr.bydelta.koala.twt

import com.twitter.penguin.korean.util.{KoreanDictionaryProvider, KoreanPos}
import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.traits.CanCompileDict
import kr.bydelta.koala.{POS, tagToTwt}

import scala.collection.mutable.ListBuffer

/**
  * 트위터 분석기 사용자사전
  */
object Dictionary extends CanCompileDict {
  private val userDict = ListBuffer[(String, POSTag)]()

  override def addUserDictionary(dict: (String, POSTag)*): Unit = {
    userDict ++= dict
    dict.groupBy(x => KoreanPos withName tagToTwt(x._2)).foreach {
      case (twtTag, seq) =>
        add(twtTag, seq.map(_._1))
    }
  }

  override def addUserDictionary(morph: String, tag: POSTag): Unit = {
    userDict += morph -> tag
    add(KoreanPos withName tagToTwt(tag), Seq(morph))
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

  /**
    * 사용자 사전에 등재된 모든 Item을 불러옵니다.
    *
    * @return (형태소, 통합품사)의 Sequence.
    */
  override def items: Seq[(String, POSTag)] = userDict

  /**
    * 현재 사용중인 패키지의 사전에 등재되어 있는지 확인합니다.
    *
    * @param word   확인할 형태소
    * @param posTag 품사(기본값: NNP 고유명사)
    */
  override def contains(word: String, posTag: Set[POSTag] = Set(POS.NNP, POS.NNG)): Boolean = {
    val oTag = posTag.map(x => KoreanPos.withName(tagToTwt(x)))
    KoreanDictionaryProvider.koreanDictionary.filterKeys(x => oTag.contains(x)).exists(_._2.contains(word))
  }
}
