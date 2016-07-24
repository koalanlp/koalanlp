package kr.bydelta.koala.traits

import kr.bydelta.koala.data.Sentence

trait CanTag {
  @throws[Exception]
  def tagSentence(text: String): Sentence

  @throws[Exception]
  def tagParagraph(text: String): Seq[Sentence]
}

