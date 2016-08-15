package kr.bydelta.koala.traits

import kr.bydelta.koala.POS
import kr.bydelta.koala.POS.POSTag

import scala.collection.JavaConversions._

/**
  * 사용자 사전추가 기능을 위한 Trait.
  */
trait CanCompileDict {
  /**
    * 사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가.
    *
    * @param dict 추가할 (표면형,품사)의 순서쌍.
    */
  def addUserDictionary(dict: (String, POSTag)*)

  /**
    * 사용자 사전에, 표면형과 그 품사를 추가.
    *
    * @param morph 표면형.
    * @param tag   품사.
    */
  def addUserDictionary(morph: String, tag: POSTag)

  /**
    * (Java) 사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가.
    *
    * @param morphs 추가할 단어의 표면형의 목록.
    * @param tags   추가할 단어의 품사의 목록.
    */
  def jUserDictionary(morphs: java.util.List[String], tags: java.util.List[POSTag]) =
  addUserDictionary(morphs.zip(tags): _*)

  /**
    * 사용자 사전에 등재된 모든 Item을 불러옵니다.
    *
    * @return (형태소, 통합품사)의 Sequence.
    */
  def items: Seq[(String, POSTag)]

  /**
    * 사전에 등재되어 있는지 확인합니다.
    *
    * @param word   확인할 형태소
    * @param posTag 품사들(기본값: NNP 고유명사, NNG 일반명사)
    */
  def contains(word: String, posTag: Set[POSTag] = Set(POS.NNP, POS.NNG)): Boolean
}
