package kr.bydelta.koala.util

import kr.bydelta.koala.isAlphabetPronounced
import kr.bydelta.koala.traits.{CanCompileDict, CanLearnWord, CanTag}

/**
  * 품사분석기가 분석하지 못한 신조어, 전문용어 등을 확인하여 추가하는 작업을 돕는 Class.
  *
  * @param tagger  품사분석의 기준이 되는 Tagger
  * @param targets 신조어 등을 등록할 사용자사전들.
  */
class BasicWordLearner(protected val tagger: CanTag, override protected val targets: CanCompileDict*)
  extends SimpleWordLearner(targets: _*) {
  override def extractNouns(corpora: Iterator[String],
                            minOccurrence: Int = 100,
                            minVariations: Int = CanLearnWord.JOSA_COUNT_MAJOR): Stream[String] = {
    super.extractNouns(corpora, minOccurrence, minVariations).filter(_.trim.nonEmpty).flatMap {
      case word if word.matches("^[^0-9]+.*$") =>
        val tagged = tagger.tagSentence(word)
        val morphs = tagged.flatMap(_.morphemes)
        if (tagged.length > 1 && CanLearnWord.ALLOWED_ENDING(morphs.last.tag) &&
          !morphs.exists(CanLearnWord.DENIED_MORPS) && !isAlphabetPronounced(word)) {
          Some(word)
        } else
          None.asInstanceOf[Option[String]]
      case _ =>
        None.asInstanceOf[Option[String]]
    }
  }
}
