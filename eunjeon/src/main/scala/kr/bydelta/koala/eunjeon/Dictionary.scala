package kr.bydelta.koala.eunjeon

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.Processor
import kr.bydelta.koala.traits.CanUserDict
import org.bitbucket.eunjeon.seunjeon.{ConnectionCostDict, DictBuilder, LexiconDict, NngUtil}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
  * 은전한닢 사용자사전
  */
object Dictionary extends CanUserDict {
  /**
    * 사용자사전에 등재되기 전의 리스트.
    */
  private[koala] lazy val rawDict = ArrayBuffer[String]()
  /**
    * 은전한닢 어휘사전.
    */
  private[koala] val lexiconDict = new LexiconDict().load()
  /**
    * 은전한닢 연결성 사전.
    */
  private[koala] val connectionCostDict = new ConnectionCostDict().load()
  /**
    * 은전한닢 사용자사전 객체
    */
  private[koala] val userDict = new LexiconDict()
  /**
    * 사용자사전 변경여부.
    */
  private var isDicChanged = false

  /**
    * 사전에 항목이 있는지 확인.
    *
    * @return True: 항목이 있을 때.
    */
  def nonEmpty = rawDict.nonEmpty

  /**
    * 사용자사전을 다시 불러옴.
    */
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
          s"$word,0,0,0,${Processor.Eunjeon originalPOSOf tag},*,${hasJongsung(lastchar)},$word,*,*,*,*"
        else
          s"$word,0,0,0,${Processor.Eunjeon originalPOSOf tag},*,*,$word,*,*,*,*"
    }
    isDicChanged = true
  }

  /**
    * (Code modified from Seunjeon package)
    * 종성이 있는지 확인.
    *
    * @param ch 종성이 있는지 확인할 글자.
    * @return 종성이 있다면, "T"를, 없다면 "F"를 반환.
    */
  private def hasJongsung(ch: Char) = {
    if (((ch - 0xAC00) % 0x001C) == 0) {
      "F"
    } else {
      "T"
    }
  }

  /**
    * (Code modified from Seunjeon package)
    * 한글 문자인지 확인.
    *
    * @param ch 확인할 글자.
    * @return True: 한글일 경우.
    */
  private def isHangul(ch: Char): Boolean = {
    if ((0x0AC00 <= ch && ch <= 0xD7A3)
      || (0x1100 <= ch && ch <= 0x11FF)
      || (0x3130 <= ch && ch <= 0x318F)) {
      true
    } else {
      false
    }
  }

  override def addUserDictionary(word: String, tag: POSTag): Unit = {
    val lastchar = word.last
    val oTag = Processor.Eunjeon originalPOSOf tag
    rawDict +=
      (if (isHangul(lastchar)) {
        val jong = hasJongsung(lastchar)
        s"$word,${getLeftId(oTag)},${getRightId(oTag, jong)},0,$oTag,*,$jong,$word,*,*,*,*"
      } else
        s"$word,${getLeftId(oTag)},${getRightId(oTag)},0,$oTag,*,*,$word,*,*,*,*")
    isDicChanged = true
  }

  private def getLeftId(tag: String = "NNG"): Short = {
    val leftIdDefStream = classOf[NngUtil].getResourceAsStream(DictBuilder.LEFT_ID_DEF)
    Source.fromInputStream(leftIdDefStream).getLines()
      .map(_.split(" "))
      .collectFirst {
        case x if x(1).startsWith(s"$tag,*,") => x(0).toShort
      }.get
  }

  private def getRightId(tag: String = "NNG", hasJongsung: String = "*"): Short = {
    val rightIdDefStream = classOf[NngUtil].getResourceAsStream(DictBuilder.RIGHT_ID_DEF)

    Source.fromInputStream(rightIdDefStream).getLines()
      .map(_.split(" "))
      .collectFirst {
        case x if x(1).startsWith(s"$tag,*,$hasJongsung") => x(0).toShort
      }.get
  }
}
