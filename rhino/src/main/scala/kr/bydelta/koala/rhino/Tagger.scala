package kr.bydelta.koala.rhino

import kr.bydelta.koala.data.{Sentence, Word}
import kr.bydelta.koala.helper.RHINOTagger
import kr.bydelta.koala.traits.CanTagOnlyASentence

/**
  * 라이노 형태소 분석기입니다.
  */
class Tagger extends CanTagOnlyASentence[Seq[Word]] {
  override def tagSentenceOriginal(text: String): Seq[Word] = {
    RHINOTagger.tag(text)
  }

  override private[koala] def convertSentence(text: String, result: Seq[Word]): Sentence =
    Sentence(result)
}