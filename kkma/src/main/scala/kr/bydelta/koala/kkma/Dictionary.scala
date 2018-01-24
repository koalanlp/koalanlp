package kr.bydelta.koala.kkma

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.helper.UserDicReader
import kr.bydelta.koala.traits.CanCompileDict
import org.snu.ids.kkma.dic.{RawDicFileReader, SimpleDicFileReader, Dictionary => Dict}

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
            new SimpleDicFileReader("/dic/00nng.dic"),
            new SimpleDicFileReader("/dic/01nnp.dic"),
            new SimpleDicFileReader("/dic/02nnb.dic"),
            new SimpleDicFileReader("/dic/03nr.dic"),
            new SimpleDicFileReader("/dic/04np.dic"),
            new SimpleDicFileReader("/dic/05comp.dic"),
            new SimpleDicFileReader("/dic/06slang.dic"),
            new SimpleDicFileReader("/dic/10verb.dic"),
            new SimpleDicFileReader("/dic/11vx.dic"),
            new SimpleDicFileReader("/dic/12xr.dic"),
            new SimpleDicFileReader("/dic/20md.dic"),
            new SimpleDicFileReader("/dic/21ma.dic"),
            new SimpleDicFileReader("/dic/30ic.dic"),
            new SimpleDicFileReader("/dic/40x.dic"),
            new RawDicFileReader("/dic/50josa.dic"),
            new RawDicFileReader("/dic/51eomi.dic"),
            new RawDicFileReader("/dic/52raw.dic"),
            userdic
          ).asJava
        )
        isDicChanged = false
      }
    }
  }
}
