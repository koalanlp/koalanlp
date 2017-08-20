package kr.bydelta.koala.traits

import kr.bydelta.koala.data.Sentence

import scala.collection.JavaConverters._

/**
  * 의존구문분석 Trait
  */
trait CanDepParse {
  /**
    * String 텍스트의 의존구문을 분석함.
    *
    * @param sentence 의존구문분석을 할 String.
    * @return 의존구문분석 결과가 포함된 Sentence 객체의 Sequence
    */
  def parse(sentence: String): Seq[Sentence]

  /**
    * (Java) String 텍스트의 의존구문을 분석함.
    *
    * @param sentence 의존구문분석을 할 String.
    * @return 의존구문분석 결과가 포함된 Sentence 객체의 List
    */
  def jParse(sentence: String): java.util.List[Sentence] = parse(sentence).asJava

  /**
    * Sentence 객체의 의존구문을 분석함.
    *
    * @param sentence 의존구문분석을 할 Sentence 객체.
    * @return 의존구문분석 결과가 포함된 Sentence 객체. (입력값과 동일한 객체)
    */
  def parse(sentence: Sentence): Sentence

  /**
    * Sentence Sequence의 의존구문을 분석함.
    *
    * @param sentences 의존구문분석을 할 Sentence Sequence.
    * @return 의존구문분석 결과가 포함된 Sentence Sequence. (입력값과 동일한 객체의 Sequence)
    */
  def parse(sentences: Seq[Sentence]): Seq[Sentence] = sentences.map(parse)

  /**
    * (Java) Sentence List의 의존구문을 분석함.
    *
    * @param sentences 의존구문분석을 할 Sentence Sequence.
    * @return 의존구문분석 결과가 포함된 Sentence List. (입력값과 동일한 객체의 Sequence)
    */
  def jParse(sentences: java.util.List[Sentence]): java.util.List[Sentence] =
  sentences.asScala.map(parse).asJava
}

