package kr.bydelta.koala.traits

import kr.bydelta.koala.POS

import scala.collection.JavaConverters._

/**
  * 품사분석기가 분석하지 못한 신조어, 전문용어 등을 확인하여 추가하는 작업을 할 수 있는 Trait.
  */
trait CanLearnWord {
  /**
    * 신조어 등을 등록할 사용자사전들.
    */
  protected val targets: Seq[CanUserDict]

  /**
    * 품사분석기가 분석하지 못한 신조어, 전문용어 등을 파악.
    *
    * @param corpora       새로운 단어를 발굴할 말뭉치.
    * @param minOccurrence 단어 등록을 위한, 최소 출현 횟수. (기본값 10회)
    * @param minVariations 단어 등록을 위한, 최소 활용(변형) 횟수. (기본값 2회) 용언의 경우는 활용형의 변화를, 체언의 경우는 조사의 변화를 파악함.
    * @return 새로운 단어와 그 품사의 Sequence.
    */
  def extractNouns(corpora: Seq[String], minOccurrence: Int = 10, minVariations: Int = 2): Set[String]

  /**
    * (Java) 품사분석기가 분석하지 못한 신조어, 전문용어 등을 확인하여 추가
    *
    * @param corpora       새로운 단어를 발굴할 말뭉치.
    * @param minOccurrence 단어 등록을 위한, 최소 출현 횟수. (기본값 10회)
    * @param minVariations 단어 등록을 위한, 최소 활용(변형) 횟수. (기본값 2회) 용언의 경우는 활용형의 변화를, 체언의 경우는 조사의 변화를 파악함.
    */
  def jLearn(corpora: java.util.List[String], minOccurrence: Int, minVariations: Int) =
  learn(corpora.asScala, minOccurrence, minVariations)

  /**
    * 품사분석기가 분석하지 못한 신조어, 전문용어 등을 확인하여 추가
    *
    * @param corpora       새로운 단어를 발굴할 말뭉치.
    * @param minOccurrence 단어 등록을 위한, 최소 출현 횟수. (기본값 10회)
    * @param minVariations 단어 등록을 위한, 최소 활용(변형) 횟수. (기본값 2회) 용언의 경우는 활용형의 변화를, 체언의 경우는 조사의 변화를 파악함.
    */
  def learn(corpora: Seq[String], minOccurrence: Int = 10, minVariations: Int = 2): Unit = {
    val set = extractNouns(corpora, minOccurrence, minVariations).map(_ -> POS.NNP).toSeq
    targets.foreach(_.addUserDictionary(set: _*))
  }
}


