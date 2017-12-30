package kr.bydelta.koala.arirang

import kr.bydelta.koala.POS
import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.traits.CanCompileDict
import org.apache.lucene.analysis.ko.morph.WordEntry
import org.apache.lucene.analysis.ko.utils.{DictionaryUtil, FileUtil, KoreanEnv}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * 아리랑 분석기의 사용자사전입니다.
  */
object Dictionary extends CanCompileDict {
  val userItems: ArrayBuffer[(String, POSTag)] = ArrayBuffer.empty
  val systemdic: mutable.HashMap[POS.POSTag, Set[String]] = mutable.HashMap.empty

  override def addUserDictionary(dict: (String, POSTag)*): Unit = {
    userItems appendAll dict
    dict.foreach {
      case (word, pos) =>
        val features = //NVZDBIPSCC
          if (POS.isNoun(pos)) "100000000X"
          else if (POS.isPredicate(pos)) "010000000X"
          else if (POS.isModifier(pos) || pos == POS.IC) "001000000X"
          else "000000000X"
        DictionaryUtil.addEntry(new WordEntry(word, features.toCharArray))
    }
  }

  override def baseEntriesOf(filter: (POSTag) => Boolean): Iterator[(String, POSTag)] = {
    systemdic.filterKeys(filter).iterator.flatMap {
      case (key, item) =>
        item.map(_ -> key)
    }
  }

  override def getNotExists(onlySystemDic: Boolean, word: (String, POSTag)*): Seq[(String, POSTag)] = {
    if (systemdic.isEmpty) load()

    // Filter out existing morphemes!
    val (_, system) =
    if (onlySystemDic) (Seq.empty[(String, POSTag)], word)
    else word.partition(items.contains)

    system.groupBy(_._2).toSeq.flatMap {
      case (pos, words) =>
        systemdic.get(mapToTag(pos)) match {
          case Some(dict) => words.filterNot(w => dict.contains(w._1))
          case None => Seq.empty
        }
    }
  }

  private def load(): Unit = Dictionary.synchronized {
    readAuxDict("josa.dic", POS.JX)
    readAuxDict("eomi.dic", POS.EP)
    readAuxDict("prefix.dic", POS.XPN)
    readAuxDict("suffix.dic", POS.XSN)
    readDict()
  }

  private def readAuxDict(dic: String, tag: POS.POSTag) = {
    val path = KoreanEnv.getInstance().getValue(dic)

    try {
      systemdic.put(tag, FileUtil.readLines(path, "UTF-8").asScala.map(_.trim).toSet)
    } catch {
      case _: Throwable =>
    }
  }

  private def readDict(): Unit = {
    try {
      val list = FileUtil.readLines(KoreanEnv.getInstance().getValue("dictionary.dic"), "UTF-8")
      list.addAll(FileUtil.readLines(KoreanEnv.getInstance().getValue("extension.dic"), "UTF-8"))
      list.asScala.flatMap {
        item =>
          val segs = item.split("[,]+")
          if (segs.length == 2) {
            val buffer = ArrayBuffer[(POS.POSTag, String)]()
            val word = segs.head.trim
            if (segs.last(0) != '0') buffer += (POS.NNG -> word)
            if (segs.last(1) != '0') buffer += (POS.VV -> word)
            if (segs.last(2) != '0') buffer += (POS.MAG -> word)
            if (segs.last.substring(0, 3) == "000") buffer += (POS.NA -> word)
            buffer
          } else Seq.empty
      }.groupBy(_._1).foreach {
        case (tag, group) =>
          systemdic.put(tag, group.map(_._2).toSet)
      }
    } catch {
      case _: Throwable =>
    }
  }

  override def items: Set[(String, POSTag)] = userItems.toSet

  private def mapToTag(tag: POS.POSTag): POS.POSTag =
    tag match {
      case p if POS.isPostPosition(p) => POS.JX
      case POS.XPN | POS.XPV => POS.XPN
      case p if POS.isSuffix(p) => POS.XSN
      case p if POS.isEnding(p) => POS.EP
      case p if POS.isNoun(p) => POS.NNG
      case p if POS.isPredicate(p) => POS.VV
      case p if p == POS.IC || POS.isModifier(p) => POS.MAG
      case _ => POS.NA
    }
}
