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
        KoreanDictionaryProvider.addWordsToDictionary(twtTag, seq.map(_._1))
    }
  }

  override def addUserDictionary(morph: String, tag: POSTag): Unit = {
    userDict += morph -> tag
    KoreanDictionaryProvider.addWordsToDictionary(KoreanPos withName tagToTwt(tag), Seq(morph))
  }

  /**
    * 사용자 사전에 등재된 모든 Item을 불러옵니다.
    *
    * @return (형태소, 통합품사)의 Sequence.
    */
  override def items: Seq[(String, POSTag)] = userDict
}
