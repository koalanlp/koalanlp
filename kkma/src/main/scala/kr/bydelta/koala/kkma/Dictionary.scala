package kr.bydelta.koala.kkma

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.Processor
import kr.bydelta.koala.helper.UserDicReader
import kr.bydelta.koala.traits.CanUserDict
import org.snu.ids.ha.dic.{RawDicFileReader, SimpleDicFileReader, Dictionary => Dict}

import scala.collection.JavaConverters._

/**
  * 꼬꼬마 사용자사전
  */
object Dictionary extends CanUserDict {
  /** 사용자사전 Reader **/
  val userdic = new UserDicReader
  /** 사전 목록의 변화여부 **/
  var isDicChanged = false

  override def addUserDictionary(dict: (String, POSTag)*) {
    if (dict.nonEmpty) {
      try {
        userdic ++=
          dict.map {
            case (word, integratedTag) => (word, Processor.KKMA originalPOSOf integratedTag)
          }
        isDicChanged = true
      } catch {
        case e: Exception =>
          e.printStackTrace()
      }
    }
  }

  override def addUserDictionary(morph: String, tag: POSTag) {
    if (morph.length > 0) {
      try {
        userdic +=(morph, Processor.KKMA originalPOSOf tag)
        isDicChanged = true
      } catch {
        case e: Exception =>
          e.printStackTrace()
      }
    }
  }

  /**
    * 사전 다시읽기.
    */
  private[koala] def reloadDic() {
    this synchronized {
      if (isDicChanged) {
        userdic.reset()
        Dict.reload(
          Seq(
            new SimpleDicFileReader("/dic/kcc.dic"),
            new SimpleDicFileReader("/dic/noun.dic"),
            new SimpleDicFileReader("/dic/person.dic"),
            new RawDicFileReader("/dic/raw.dic"),
            new SimpleDicFileReader("/dic/simple.dic"),
            new SimpleDicFileReader("/dic/verb.dic"),
            userdic
          ).asJava
        )
        isDicChanged = false
      }
    }
  }
}
