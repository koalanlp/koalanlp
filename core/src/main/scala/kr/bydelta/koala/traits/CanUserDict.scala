package kr.bydelta.koala.traits

import kr.bydelta.koala.POS.POSTag

/**
  * Created by bydelta on 16. 7. 22.
  */
trait CanUserDict {
  def addUserDictionary(dict: (String, POSTag)*)

  def addUserDictionary(morph: String, tag: POSTag)
}
