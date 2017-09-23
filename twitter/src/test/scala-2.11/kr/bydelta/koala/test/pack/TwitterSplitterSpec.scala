package kr.bydelta.koala.test.pack

import kr.bydelta.koala.traits.CanSplitSentence
import kr.bydelta.koala.twt.SentenceSplitter

/**
  * Created by bydelta on 16. 7. 26.
  */
class TwitterSplitterSpec extends SplitterSpec {

  override def getOriginalSplitterCount(para: String): Int = {
    TwitterKoreanProcessor.splitSentences(para).length
  }

  override def getSplitter: CanSplitSentence = new SentenceSplitter()
}
