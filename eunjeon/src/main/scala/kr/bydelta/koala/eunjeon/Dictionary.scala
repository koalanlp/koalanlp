package kr.bydelta.koala.eunjeon

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala._
import kr.bydelta.koala.traits.CanCompileDict
import org.bitbucket.eunjeon.seunjeon.{ConnectionCostDict, DictBuilder, LexiconDict, NngUtil}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
  * 은전한닢 사용자사전
  */
object Dictionary extends CanCompileDict {
  /**
    * 사용자사전에 등재되기 전의 리스트.
    */
  private[koala] lazy val rawDict = ArrayBuffer[String]()
  private lazy val rightIDMap =
    Source.fromInputStream(classOf[NngUtil].getResourceAsStream(DictBuilder.RIGHT_ID_DEF)).getLines()
      .map { line =>
        val splitted = line.split(" ")
        splitted(1).replaceAll("^([^,]+,[^,]+,[^,]+),.*", "$1") -> splitted.head.toShort
      }.toMap
  private lazy val leftIDMap =
    Source.fromInputStream(classOf[NngUtil].getResourceAsStream(DictBuilder.LEFT_ID_DEF)).getLines()
      .map { line =>
        val splitted = line.split(" ")
        splitted(1).replaceAll("^([^,]+,[^,]+),.*", "$1") -> splitted.head.toShort
      }.toMap
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
  private[koala] val userDict = new LexiconDict().loadFromIterator(Iterator())
  /**
    * 사용자사전 변경여부.
    */
  private[koala] var isDicChanged = false

  /**
    * 사전에 항목이 있는지 확인.
    *
    * @return True: 항목이 있을 때.
    */
  def nonEmpty = rawDict.nonEmpty

  override def addUserDictionary(dict: (String, POSTag)*): Unit = {
    rawDict ++= dict.map {
      case (word, tag) =>
        val lastchar = word.last
        val oTag = tagToEunjeon(tag)
        if (isHangul(lastchar)) {
          val jong = hasJongsung(lastchar)
          s"$word,${getLeftId(oTag)},${getRightId(oTag, jong)},0,$oTag,*,$jong,$word,*,*,*,*"
        } else
          s"$word,${getLeftId(oTag)},${getRightId(oTag)},0,$oTag,*,*,$word,*,*,*,*"
    }
    isDicChanged = true
  }

  override def addUserDictionary(word: String, tag: POSTag): Unit = {
    val lastchar = word.last
    val oTag = tagToEunjeon(tag)
    rawDict +=
      (if (isHangul(lastchar)) {
        val jong = hasJongsung(lastchar)
        s"$word,${getLeftId(oTag)},${getRightId(oTag, jong)},0,$oTag,*,$jong,$word,*,*,*,*"
      } else
        s"$word,${getLeftId(oTag)},${getRightId(oTag)},0,$oTag,*,*,$word,*,*,*,*")
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

  /**
    * (Code modified from Seunjeon package)
    * Left ID 부여
    *
    * @param tag Left ID를 찾을 Tag
    * @return Left ID
    */
  private def getLeftId(tag: String = "NNG"): Short = {
    leftIDMap.get(s"$tag,*") match {
      case Some(id) => id
      case None => leftIDMap.filterKeys(_.startsWith(tag)).values.max
    }
  }

  /**
    * (Code modified from Seunjeon package)
    * Right ID 부여
    *
    * @param tag         Right ID를 찾을 Tag
    * @param hasJongsung 종성이 있으면 "T", 없으면 "F", 해당없는 문자는 "*"
    * @return Right ID
    */
  private def getRightId(tag: String = "NNG", hasJongsung: String = "*"): Short = {
    rightIDMap.get(s"$tag,*,$hasJongsung") match {
      case Some(id) => id
      case None => rightIDMap.filterKeys(_.startsWith(tag)).values.max
    }
  }

  override def items: Seq[(String, POSTag)] =
    rawDict.map {
      line =>
        val segs = line.split(",")
        segs(0) -> fromEunjeonTag(segs(4))
    }

  override def contains(word: String, posTag: Set[POSTag] = Set(POS.NNP, POS.NNG)): Boolean = {
    reloadDic()
    val oTag = posTag.map(tagToEunjeon)

    lexiconDict.commonPrefixSearch(word).exists {
      m =>
        m.surface == word && oTag.contains(m.feature.head) && m.feature.last == "*"
    } || userDict.commonPrefixSearch(word).exists {
      m =>
        m.surface == word && oTag.contains(m.feature.head) && m.feature.last == "*"
    }
  }

  /**
    * 사용자사전을 다시 불러옴.
    */
  def reloadDic(): Unit = Dictionary synchronized {
    if (isDicChanged) {
      userDict.loadFromIterator(rawDict.iterator)
      isDicChanged = false
    }
  }
}
