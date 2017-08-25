package kr.bydelta.koala.rhino

import kr.bydelta.koala.data.{Sentence, Word}
import kr.bydelta.koala.helper.RHINOTagger
import kr.bydelta.koala.traits.CanTagOnlyASentence

/**
  * Created by bydelta on 17. 8. 23.
  */
class Tagger extends CanTagOnlyASentence[Seq[Word]] {
  override def tagSentenceOriginal(text: String): Seq[Word] = {
    RHINOTagger.tag(text)
  }

  override private[koala] def convertSentence(text: String, result: Seq[Word]): Sentence =
    Sentence(result)
}