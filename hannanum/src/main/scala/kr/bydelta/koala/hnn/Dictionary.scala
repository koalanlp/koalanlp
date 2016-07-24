package kr.bydelta.koala.hnn

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.Processor
import kr.bydelta.koala.traits.{CanExtractResource, CanUserDict}

import scala.collection.mutable

/**
  * Created by bydelta on 16. 7. 24.
  */
object Dictionary extends CanUserDict with CanExtractResource {
  override protected val modelName: String = "hannanum"
  val userDict = mutable.HashMap[String, String]()

  def addUserDictionary(dict: (String, POSTag)*) {
    userDict ++= dict.map {
      case (word, tag) => (word, Processor.Hannanum originalPOSOf tag)
    }
  }

  def addUserDictionary(morph: String, tag: POSTag) {
    userDict += morph -> (Processor.Hannanum originalPOSOf tag)
  }
}
