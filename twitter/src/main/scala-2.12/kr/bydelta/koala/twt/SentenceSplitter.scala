package kr.bydelta.koala.twt

import kr.bydelta.koala.traits.CanSplitSentence
import org.openkoreantext.processor.OpenKoreanTextProcessor

/**
  * 트위터 문장분리기.
  */
class SentenceSplitter extends CanSplitSentence {
  override def sentences(text: String): Seq[String] =
    OpenKoreanTextProcessor.splitSentences(text).map(_.text)
}


