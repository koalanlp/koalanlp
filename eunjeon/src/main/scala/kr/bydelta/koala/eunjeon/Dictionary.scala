package kr.bydelta.koala.eunjeon

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.Processor
import kr.bydelta.koala.traits.CanUserDict
import org.bitbucket.eunjeon.seunjeon.LexiconDict

import scala.collection.mutable.ArrayBuffer

/**
  * Created by bydelta on 16. 7. 24.
  */
object Dictionary extends CanUserDict {
  val userDict = new LexiconDict()
  private val rawDict = ArrayBuffer[String]()
  private var isDicChanged = false

  def nonEmpty = rawDict.nonEmpty

  def reloadDic(): Unit = {
    if (isDicChanged) {
      userDict.loadFromIterator(rawDict.iterator)
      isDicChanged = false
    }
  }

  override def addUserDictionary(dict: (String, POSTag)*): Unit = {
    rawDict ++= dict.map {
      case (word, tag) =>
        val lastchar = word.last
        if (isHangul(lastchar))
          s"$word,,,,${Processor.Eunjeon originalPOSOf tag},*,${hasJongsung(lastchar)},$word,*,*,*,*"
        else
          s"$word,,,,${Processor.Eunjeon originalPOSOf tag},*,*,$word,*,*,*,*"
    }
    isDicChanged = true
  }

  override def addUserDictionary(word: String, tag: POSTag): Unit = {
    val lastchar = word.last
    rawDict +=
      (if (isHangul(lastchar))
        s"$word,,,,${Processor.Eunjeon originalPOSOf tag},*,${hasJongsung(lastchar)},$word,*,*,*,*"
      else
        s"$word,,,,${Processor.Eunjeon originalPOSOf tag},*,*,$word,*,*,*,*")
    isDicChanged = true
  }

  private def hasJongsung(ch: Char) = {
    if (((ch - 0xAC00) % 0x001C) == 0) {
      "F"
    } else {
      "T"
    }
  }

  private def isHangul(ch: Char): Boolean = {
    if ((0x0AC00 <= ch && ch <= 0xD7A3)
      || (0x1100 <= ch && ch <= 0x11FF)
      || (0x3130 <= ch && ch <= 0x318F)) {
      true
    } else {
      false
    }
  }
}
