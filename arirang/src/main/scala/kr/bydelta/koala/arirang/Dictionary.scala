package kr.bydelta.koala.arirang

import kr.bydelta.koala.POS
import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.traits.CanCompileDict
import org.apache.lucene.analysis.ko.morph.WordEntry
import org.apache.lucene.analysis.ko.utils.DictionaryUtil

import scala.collection.mutable.ArrayBuffer

/**
  * Created by bydelta on 17. 8. 19.
  */
object Dictionary extends CanCompileDict {
  val userItems = ArrayBuffer[(String, POSTag)]()
  DictionaryUtil.loadDictionary()

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
    throw new UnsupportedOperationException("The operation is not implemented.")
  }

  override def getNotExists(onlySystemDic: Boolean, word: (String, POSTag)*): Seq[(String, POSTag)] = {
    // Filter out existing morphemes!
    val (_, system) =
    if (onlySystemDic) (Seq.empty[(String, POSTag)], word)
    else word.partition(items.contains)

    system.groupBy(_._2).toSeq.flatMap {
      case (pos, words) =>
        pos match {
          case p if POS.isPostposition(p) => words.filterNot(w => DictionaryUtil.existJosa(w._1))
          case POS.XPN | POS.XPV => words.filterNot(w => DictionaryUtil.existPrefix(w._1))
          case p if POS.isSuffix(p) => words.filterNot(w => DictionaryUtil.existSuffix(w._1))
          case p if POS.isEnding(p) => words.filterNot(w => DictionaryUtil.existEomi(w._1))
          case p if POS.isNoun(p) => words.filter(w => DictionaryUtil.getAllNoun(w._1) == null)
          case p if p == POS.IC || POS.isModifier(p) => words.filter(w => DictionaryUtil.getAdverb(w._1) == null)
          case p if POS.isPredicate(p) => words.filter(w => DictionaryUtil.getVerb(w._1) == null)
          case _ => words.filter(w => DictionaryUtil.getWord(w._1) == null)
        }
    }
  }

  override def items: Set[(String, POSTag)] = userItems.toSet
}
