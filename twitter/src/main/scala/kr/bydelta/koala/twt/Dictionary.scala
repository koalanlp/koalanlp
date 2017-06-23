package kr.bydelta.koala.twt

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.traits.CanCompileDict
import kr.bydelta.koala.{POS, fromTwtTag, tagToTwt}
import org.openkoreantext.processor.util.{KoreanDictionaryProvider, KoreanPos}

import scala.collection.JavaConverters._

/**
  * 트위터 분석기 사용자사전
  */
object Dictionary extends CanCompileDict {
  val dicKeys = Seq(KoreanPos.Noun, KoreanPos.Verb, KoreanPos.Adjective, KoreanPos.Adverb, KoreanPos.Determiner,
    KoreanPos.Exclamation, KoreanPos.Josa, KoreanPos.Eomi, KoreanPos.PreEomi, KoreanPos.Conjunction, KoreanPos.Modifier,
    KoreanPos.VerbPrefix, KoreanPos.Suffix)
  private val logger = org.log4s.getLogger
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
         KoreanPos.PreEomi | KoreanPos.Conjunction | KoreanPos.Modifier | KoreanPos.VerbPrefix |
         KoreanPos.Suffix | KoreanPos.Adverb =>
      KoreanDictionaryProvider.addWordsToDictionary(tag, morph)
    case KoreanPos.ProperNoun =>
      morph.foreach(KoreanDictionaryProvider.properNouns.add)
    case KoreanPos.Verb | KoreanPos.Adjective =>
    case _ =>
      KoreanDictionaryProvider.addWordsToDictionary(KoreanPos.Noun, morph)
  }

  override def items: Set[(String, POSTag)] = userDict

  /**
    * 사전에 등재되어 있는지 확인하고, 사전에 없는단어만 반환합니다.
    *
    * @param dummy (제공하지 않는 기능.)
    * @param word  확인할 (형태소, 품사)들.
    * @return 사전에 없는 단어들.
    */
  override def getNotExists(dummy: Boolean, word: (String, POSTag)*): Seq[(String, POSTag)] = {
    word.groupBy(w => KoreanPos.withName(tagToTwt(w._2))).iterator.flatMap {
      case (tag, words) =>
        val tagDic =
          if (tag == KoreanPos.ProperNoun) Some(KoreanDictionaryProvider.properNouns)
          else if (KoreanDictionaryProvider.koreanDictionary.containsKey(tag))
            Some(KoreanDictionaryProvider.koreanDictionary.get(tag))
          else None

        // Filter out existing morphemes!
        if (tagDic.isDefined)
          words.filterNot(w => tagDic.get.contains(w._1))
        else
          words // for the case of not found!
    }.toSeq
  }

  override def baseEntriesOf(filter: (POSTag) => Boolean): Iterator[(String, POSTag)] = {
    val np =
      if (filter(POS.NNP)) KoreanDictionaryProvider.properNouns.iterator.asScala.map {
        case x: String => x -> POS.NNP
        case x: Array[Char] => new String(x) -> POS.NNP
        case x => x.toString -> POS.NNP
      }
      else Iterator.empty

    val dicKeys = this.dicKeys.filter(x => filter(fromTwtTag(x.toString)))
    val dic = dicKeys.iterator.flatMap(key => {
      val keydic = KoreanDictionaryProvider.koreanDictionary.get(key)
      if (keydic != null)
        keydic.asScala.map {
          case x: String => x -> fromTwtTag(key.toString)
          case x: Array[Char] => new String(x) -> fromTwtTag(key.toString)
          case x => x.toString -> fromTwtTag(key.toString)
        }
      else
        Map.empty
    })
    np ++ dic
  }
}
