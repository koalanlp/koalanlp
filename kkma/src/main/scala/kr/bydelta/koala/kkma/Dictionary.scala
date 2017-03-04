package kr.bydelta.koala.kkma

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala._
import kr.bydelta.koala.helper.UserDicReader
import kr.bydelta.koala.traits.CanCompileDict
import org.snu.ids.ha.dic.{RawDicFileReader, SimpleDicFileReader, Dictionary => Dict}

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

/**
  * 꼬꼬마 사용자사전
  */
object Dictionary extends CanCompileDict {
  /** 원본사전의 어휘목록 **/
  private lazy val systemDic = Dict.getInstance.getAsList.asScala.flatMap {
    _.map(m => m.getTag -> m.getExp) // (품사, 표현식)으로 변환.
  }.groupBy(_._1).mapValues {
    // 품사로 묶음.
    _.map(_._2).toSet // 각 묶음단위로 표현식 집합 구성.
  }
  /** 사용자사전 Reader **/
  val userdic = new UserDicReader
  /** 사전 목록의 변화여부 **/
  var isDicChanged = false

  override def addUserDictionary(dict: (String, POSTag)*) {
    if (dict.nonEmpty) {
      userdic ++=
        dict.map {
          case (word, integratedTag) if word.nonEmpty => (word, tagToKKMA(integratedTag))
        }
      isDicChanged = true
    }
  }

  override def items: Set[(String, POSTag)] = this synchronized {
    userdic.reset()
    userdic.map {
      line =>
        val segs = line.split('/')
        segs(0) -> fromKKMATag(segs(1))
    }.toSet
  }

  override def contains(word: String, posTag: Set[POSTag] = Set(POS.NNP, POS.NNG)): Boolean = {
    val oTag = posTag.map(tagToKKMA)
    val dictLines = oTag.map(word + "/" + _)
    systemDic.filterKeys(oTag.contains).exists(_._2.contains(word)) ||
      userdic.morphemes.exists(x => dictLines.contains(x))
  }

  override def baseEntriesOf(filter: (POSTag) => Boolean): Iterator[String] = {
    systemDic.filterKeys(tag => tag != null && filter(fromKKMATag(tag))).iterator.flatMap(_._2)
  }

  /**
    * 사전 다시읽기.
    */
  private[koala] def reloadDic() {
    userdic synchronized {
      if (isDicChanged) {
        userdic.reset()
        Dict.reload(
          Seq(
            new SimpleDicFileReader("/dic/kcc.dic"),
            new SimpleDicFileReader("/dic/noun.dic"),
            new SimpleDicFileReader("/dic/person.dic"),
            new RawDicFileReader("/dic/raw.dic"),
            new SimpleDicFileReader("/dic/simple.dic"),
            new SimpleDicFileReader("/dic/verb.dic"),
            userdic
          ).asJava
        )
        isDicChanged = false
      }
    }
  }
}
