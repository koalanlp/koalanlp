package kr.bydelta.koala.twt

import com.twitter.penguin.korean.TwitterKoreanProcessor
import kr.bydelta.koala.POS
import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.traits.CanUserDict

/**
  * Created by bydelta on 16. 7. 24.
  */
object Dictionary extends CanUserDict {
  override def addUserDictionary(dict: (String, POSTag)*): Unit = {
    TwitterKoreanProcessor.addNounsToDictionary(dict.filter(_._2.id <= POS.NP.id).map(_._1))
  }

  override def addUserDictionary(morph: String, tag: POSTag): Unit = {
    if (tag.id <= POS.NP.id)
      TwitterKoreanProcessor.addNounsToDictionary(Seq(morph))
  }
}
