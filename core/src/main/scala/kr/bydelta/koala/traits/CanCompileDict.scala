package kr.bydelta.koala.traits

import kr.bydelta.koala.POS
import kr.bydelta.koala.POS.POSTag

import scala.collection.JavaConverters._

/**
  * 사용자 사전추가 기능을 위한 Trait.
  */
trait CanCompileDict {
  private[this] val logger = org.log4s.getLogger
  /**
    * 사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가.
    *
    * @param dict 추가할 (표면형,품사)의 순서쌍.
    */
  def addUserDictionary(dict: (String, POSTag)*): Unit

  /**
    * 사용자 사전에, 표면형과 그 품사를 추가.
    *
    * @param morph 표면형.
    * @param tag   품사.
    */
  def addUserDictionary(morph: String, tag: POSTag): Unit = addUserDictionary(morph -> tag)

  /**
    * (Java) 사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가.
    *
    * @param morphs 추가할 단어의 표면형의 목록.
    * @param tags   추가할 단어의 품사의 목록.
    */
  def jUserDictionary(morphs: java.util.List[String], tags: java.util.List[POSTag]): Unit =
  addUserDictionary(morphs.asScala.zip(tags.asScala): _*)

  /**
    * 사용자 사전에 등재된 모든 Item을 불러옵니다.
    *
    * @return (형태소, 통합품사)의 Sequence.
    */
  def items: Set[(String, POSTag)]

  /**
    * 원본 사전에 등재된 항목 중에서, 지정된 형태소의 항목만을 가져옵니다. (복합 품사 결합 형태는 제외)
    *
    * @param filter 가져올 품사인지 판단하는 함수.
    * @return (형태소, 품사)의 Iterator.
    */
  def baseEntriesOf(filter: POSTag => Boolean): Iterator[(String, POSTag)]

  /**
    * 사전에 등재되어 있는지 확인합니다.
    *
    * @param word   확인할 형태소
    * @param posTag 품사들(기본값: NNP 고유명사, NNG 일반명사)
    */
  def contains(word: String, posTag: Set[POSTag] = Set(POS.NNP, POS.NNG)): Boolean = {
    getNotExists(false, posTag.map(word -> _).toSeq: _*).length < posTag.size
  }

  /**
    * 사전에 등재되어 있는지 확인하고, 사전에 없는단어만 반환합니다.
    *
    * @param onlySystemDic 시스템 사전에서만 검색할지 결정합니다.
    * @param word          확인할 (형태소, 품사)들.
    * @return 사전에 없는 단어들.
    */
  def getNotExists(onlySystemDic: Boolean, word: (String, POSTag)*): Seq[(String, POSTag)]

  /**
    * 다른 사전을 참조하여, 선택된 사전에 없는 단어를 사용자사전으로 추가합니다.
    *
    * @param dict       참조할 사전
    * @param filter     추가할 품사를 지정하는 함수.
    * @param fastAppend 선택된 사전에 존재하는지를 검사하지 않고, 빠르게 추가하고자 할 때 (기본값 false)
    */
  def importFrom(dict: CanCompileDict, filter: POSTag => Boolean = POS.isNoun, fastAppend: Boolean = false) {
    val entries = dict.baseEntriesOf(filter)

    logger info s"Start to import dictionary: ${dict.getClass.getPackage} → ${this.getClass.getPackage}"
    val total = entries.sliding(10000, 10000).foldLeft(0) {
      case (count, raw) =>
        val seq = if (fastAppend) raw else getNotExists(true, raw: _*)
        this.addUserDictionary(seq: _*)
        val next = count + seq.length
        logger info s"$next word(s) imported (In Progress)"
        next
    }

    logger info s"Import finished. ($total words)"
  }
}
