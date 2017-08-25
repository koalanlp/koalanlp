package kr.bydelta.koala.rhino

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.traits.CanCompileDict

/**
  * Created by bydelta on 17. 8. 23.
  */
object Dictionary extends CanCompileDict {
  override def addUserDictionary(dict: (String, POSTag)*): Unit = {
    throw new UnsupportedOperationException("RHINO does not have method for adding user dictionary")
  }

  override def items: Set[(String, POSTag)] = {
    throw new UnsupportedOperationException("RHINO does not have method for adding user dictionary")
  }

  override def baseEntriesOf(filter: (POSTag) => Boolean): Iterator[(String, POSTag)] = {
    throw new UnsupportedOperationException("RHINO does not have method for handling a dictionary")
  }

  override def getNotExists(onlySystemDic: Boolean, word: (String, POSTag)*): Seq[(String, POSTag)] = {
    throw new UnsupportedOperationException("RHINO does not have method for handling a dictionary")
  }
}
