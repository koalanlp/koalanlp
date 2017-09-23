package kr.bydelta.koala.twt

import kr.bydelta.koala.traits.CanSplitSentence

/**
  * 트위터 문장분리기.
  */
class SentenceSplitter extends CanSplitSentence {
  override def sentences(text: String): Seq[String] =
    TwitterKoreanProcessor.splitSentences(text).map(_.text)
}


