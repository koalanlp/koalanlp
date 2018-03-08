package kr.bydelta.koala.eunjeon

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala._
import kr.bydelta.koala.traits.CanCompileDict
import org.bitbucket.eunjeon.seunjeon._

import scala.io.Source

/**
  * 은전한닢 사용자사전
  */
object Dictionary extends CanCompileDict {
  private lazy val rightIDMap =
    Source.fromInputStream(classOf[NngUtil].getResourceAsStream(DictBuilder.RIGHT_ID_DEF)).getLines()
      .map { line =>
        val splits = line.split(" ")
        splits(1).replaceAll("^([^,]+,[^,]+,[^,]+),.*", "$1") -> splits.head.toShort
      }.toMap
  private lazy val leftIDMap =
    Source.fromInputStream(classOf[NngUtil].getResourceAsStream(DictBuilder.LEFT_ID_DEF)).getLines()
      .map { line =>
        val splits = line.split(" ")
        splits(1).replaceAll("^([^,]+,[^,]+),.*", "$1") -> splits.head.toShort
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
    * 사용자사전에 등재되기 전의 리스트.
    */
  private[koala] var rawDict = Set[String]()
  /**
    * 사용자사전 변경여부.
    */
  private[koala] var isDicChanged = false

  /**
    * 사전에 항목이 있는지 확인.
    *
    * @return True: 항목이 있을 때.
    */
  def nonEmpty: Boolean = rawDict.nonEmpty

  override def addUserDictionary(dict: (String, POSTag)*): Unit = {
    rawDict ++= dict.map {
      case (word, tag) =>
        val oTag = fromSejongPOS(tag)
        if (word.endsWithHangul) {
          val jong = if (word.endsWithJongsung) "T" else "F"
          s"$word,${getLeftId(oTag)},${getRightId(oTag, jong)},0,$oTag,*,$jong,$word,*,*,*,*"
        } else
          s"$word,${getLeftId(oTag)},${getRightId(oTag)},0,$oTag,*,*,$word,*,*,*,*"
    }
    isDicChanged = true
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

  override def items: Set[(String, POSTag)] =
    rawDict.map {
      line =>
        val segs = line.split(",")
        segs(0) -> toSejongPOS(segs(4))
    }

  override def getNotExists(onlySystemDic: Boolean, word: (String, POSTag)*): Seq[(String, POSTag)] = {
    reloadDic()
    word.groupBy(_._1).iterator.flatMap {
      case (w, tags) =>
        val searched = (
          if (onlySystemDic) lexiconDict.commonPrefixSearch(w)
          else lexiconDict.commonPrefixSearch(w) ++ userDict.commonPrefixSearch(w)
          ).filter(m => m.surface == w && m.feature.last == "*")

        // Filter out existing morphemes!
        if (searched.nonEmpty) {
          val found = searched.map(_.feature.head)
          tags.filterNot {
            case (_, tag) =>
              found.contains(fromSejongPOS(tag))
          }
        } else tags // The case of not found
    }.toSeq
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

  override def baseEntriesOf(filter: (POSTag) => Boolean): Iterator[(String, POSTag)] = {
    lexiconDict.termDict.toIterator.flatMap(m => {
      val converted = convertMorpheme(m)
      if (converted.length == 1 && filter(converted.head.tag)) {
        Some(converted.head.surface -> converted.head.tag)
      } else {
        None
      }
    })
  }

  def convertMorpheme(m: Morpheme): Seq[data.Morpheme] = {
    val array = m.feature
    val compoundTag = array.head
    val tokens = array.last

    if (array.length == 1) {
      Seq(
        data.Morpheme(m.surface, compoundTag, toSejongPOS(compoundTag))
      )
    } else if (tokens == "*") {
      Seq(
        data.Morpheme(m.surface, compoundTag, toSejongPOS(compoundTag))
      )
    } else {
      tokens.split("\\+").map {
        tok =>
          val arr = tok.split("/")
          data.Morpheme(arr.head, arr(1), toSejongPOS(arr(1)))
      }
    }
  }
}
