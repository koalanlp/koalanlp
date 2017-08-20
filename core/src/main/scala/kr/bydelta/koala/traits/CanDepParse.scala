package kr.bydelta.koala.traits

import kr.bydelta.koala.data.Sentence

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
}

