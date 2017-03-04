package kr.bydelta.koala.twt

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.traits.CanCompileDict
import kr.bydelta.koala.{POS, fromTwtTag, tagToTwt}

/**
  * 트위터 분석기 사용자사전
  */
object Dictionary extends CanCompileDict {
  private var userDict = Set[(String, POSTag)]()

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

  /**
    * 사용자 사전에 등재된 모든 Item을 불러옵니다.
    *
    * @return (형태소, 통합품사)의 Sequence.
    */
  override def items: Set[(String, POSTag)] = userDict

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

  override def baseEntriesOf(filter: (POSTag) => Boolean): Iterator[String] = {
    KoreanDictionaryProvider.koreanDictionary.filterKeys(x => filter(fromTwtTag(x.toString)))
      .iterator.flatMap(_._2.iterator().asScala.map {
      case x: String => x
      case x: Array[Char] => new String(x)
      case x => x.toString
    })
  }
}
