package kr.bydelta.koala.twt

import com.twitter.penguin.korean.TwitterKoreanProcessor
import kr.bydelta.koala.traits.CanSplitSentence

/**
  * Created by bydelta on 16. 7. 22.
  */
class SentenceSplitter extends CanSplitSentence {
  @throws[Exception]
  override def sentences(text: String): Seq[String] =
    TwitterKoreanProcessor.splitSentences(text).map(_.text)
}


