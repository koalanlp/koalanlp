package kr.bydelta.koala.hnn

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.Processor
import kr.bydelta.koala.traits.{CanExtractResource, CanUserDict}

import scala.collection.mutable

/**
  * 한나눔 사용자사전
  */
object Dictionary extends CanUserDict with CanExtractResource {
  override protected val modelName: String = "hannanum"
  /**
    * 사용자사전에 등재되기 전의 리스트.
    */
  val userDict = mutable.HashMap[String, String]()

  override def addUserDictionary(dict: (String, POSTag)*) {
    userDict ++= dict.map {
      case (word, tag) => (word, Processor.Hannanum originalPOSOf tag)
    }
  }

  override def addUserDictionary(morph: String, tag: POSTag) {
    userDict += morph -> (Processor.Hannanum originalPOSOf tag)
  }
}
