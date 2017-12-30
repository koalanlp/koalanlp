package kr.bydelta.koala.twt

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.traits.CanCompileDict
import org.openkoreantext.processor.util.{CharArraySet, KoreanDictionaryProvider, KoreanPos}

import scala.collection.JavaConverters._

/**
  * 트위터 분석기 사용자사전
  */
object Dictionary extends CanCompileDict {
  private var userDict = Set[(String, POSTag)]()

  override def addUserDictionary(dict: (String, POSTag)*): Unit = {
    userDict ++= dict
    dict.groupBy(x => KoreanPos withName fromSejongPOS(x._2)).foreach {
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
  private def add(tag: KoreanPos.KoreanPos, morph: Seq[String]): Unit =
  tag match {
    case t if dictContainsKey(t) =>
      KoreanDictionaryProvider.addWordsToDictionary(t, morph)
    case KoreanPos.ProperNoun =>
      morph.foreach(KoreanDictionaryProvider.properNouns.add)
    case KoreanPos.Verb | KoreanPos.Adjective =>
    // * Verb/Adjective dictionary cannot be modified in OKT.
    // KoreanDictionaryProvider.predicateStems.get(tag)
    case _ =>
    // * No proper dictionary.
    // KoreanDictionaryProvider.addWordsToDictionary(KoreanPos.Noun, morph)
  }

  override def items: Set[(String, POSTag)] = userDict

  // $COVERAGE-ON$

  /**
    * 사전에 등재되어 있는지 확인하고, 사전에 없는단어만 반환합니다.
    *
    * @param dummy (제공하지 않는 기능.)
    * @param word  확인할 (형태소, 품사)들.
    * @return 사전에 없는 단어들.
    */
  override def getNotExists(dummy: Boolean, word: (String, POSTag)*): Seq[(String, POSTag)] = {
    word.groupBy(w => KoreanPos.withName(fromSejongPOS(w._2))).iterator.flatMap {
      case (tag, words) =>
        val tagDic =
          if (tag == KoreanPos.ProperNoun) Some(KoreanDictionaryProvider.properNouns)
          else if (dictContainsKey(tag))
            Some(dictGet(tag))
          else None

        // Filter out existing morphemes!
        if (tagDic.isDefined)
          words.filterNot(w => tagDic.get.contains(w._1))
        else
          words // for the case of not found!
    }.toSeq
  }

  override def baseEntriesOf(filter: (POSTag) => Boolean): Iterator[(String, POSTag)] = {
    KoreanPos.values.filter(x => filter(toSejongPOS(x.toString))).iterator.flatMap {
      case t if dictContainsKey(t) =>
        val key = toSejongPOS(t.toString)
        dictGet(t).asScala.map {
          case x: String => x -> key
          case x: Array[Char] => new String(x) -> key
          case x => x.toString -> key
        }
      case KoreanPos.ProperNoun =>
        val key = toSejongPOS(KoreanPos.ProperNoun.toString)
        KoreanDictionaryProvider.properNouns.asScala.map {
          case x: String => x -> key
          case x: Array[Char] => new String(x) -> key
          case x => x.toString -> key
        }
      case t@(KoreanPos.Verb | KoreanPos.Adjective) =>
        val key = toSejongPOS(t.toString)
        KoreanDictionaryProvider.predicateStems(t).keys.map(_ -> key)
      case _ =>
        Map()
    }
  }

  // $COVERAGE-OFF$
  private def dictContainsKey(tag: KoreanPos.KoreanPos): Boolean =
  KoreanDictionaryProvider.koreanDictionary.containsKey(tag)

  private def dictGet(tag: KoreanPos.KoreanPos): CharArraySet =
    KoreanDictionaryProvider.koreanDictionary.get(tag)
}
