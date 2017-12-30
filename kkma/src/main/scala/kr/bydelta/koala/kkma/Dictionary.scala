package kr.bydelta.koala.kkma

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.helper.UserDicReader
import kr.bydelta.koala.traits.CanCompileDict
import org.snu.ids.ha.dic.{RawDicFileReader, SimpleDicFileReader, Dictionary => Dict}

import scala.collection.JavaConverters._

/**
  * 꼬꼬마 사용자사전
  */
object Dictionary extends CanCompileDict {
  /** 원본사전의 어휘목록 **/
  private lazy val systemDicByTag = Dict.getInstance.getAsList.asScala.flatMap {
    _.asScala.filter(_.getTag != null)
      .map(m => toSejongPOS(m.getTag) -> m.getExp) // (품사, 표현식)으로 변환.
  }.groupBy(_._1).mapValues(_.map(_.swap))

  /** 사용자사전 Reader **/
  val userdic = new UserDicReader
  /** 사전 목록의 변화여부 **/
  var isDicChanged = false

  override def addUserDictionary(dict: (String, POSTag)*) {
    if (dict.nonEmpty) {
      userdic ++=
        dict.map {
          case (word, integratedTag) if word.nonEmpty => (word, fromSejongPOS(integratedTag))
        }
      isDicChanged = true
    }
  }

  override def items: Set[(String, POSTag)] = this synchronized {
    userdic.reset()
    userdic.flatMap {
      line =>
        try {
          val segments = line.split('/')
          Some(segments(0) -> toSejongPOS(segments(1)))
        } catch {
          case _: NullPointerException | _: ArrayIndexOutOfBoundsException =>
            None
          case e: Throwable =>
            throw e
        }
    }.toSet
  }

  override def getNotExists(onlySystemDic: Boolean, word: (String, POSTag)*): Seq[(String, POSTag)] = {
    val converted = word.map {
      case tup@(w, t) => (w, tup, fromSejongPOS(t))
    }

    // Filter out existing morphemes!
    val (_, system) =
      if (onlySystemDic) (Seq.empty[(String, (String, POSTag), String)], converted)
      else converted.partition(w => userdic.morphemes.contains(s"${w._1}/${w._3}"))
    system.groupBy(_._1).iterator.flatMap {
      case (w, tags) =>
        // Filter out existing morphemes!
        tags.filterNot {
          t =>
            val mexp = Dict.getInstance.getMExpression(w)
            mexp != null && mexp.asScala.map(_.getTag).contains(t._3)
        }.map(_._2)
    }.toSeq
  }

  override def baseEntriesOf(filter: (POSTag) => Boolean): Iterator[(String, POSTag)] = {
    systemDicByTag.filterKeys(filter).iterator.flatMap(_._2)
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
