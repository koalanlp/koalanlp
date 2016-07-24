package kr.bydelta.koala.twt

import com.twitter.penguin.korean.TwitterKoreanProcessor
import kr.bydelta.koala.Processor
import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.traits.CanTag

/**
  * Created by bydelta on 16. 7. 22.
  */
class Tagger extends CanTag {
  @throws[Exception]
  override def tagParagraph(text: String): Seq[Sentence] = {
    TwitterKoreanProcessor.splitSentences(text).map {
      sent =>
        tagSentence(sent.text)
    }
  }

  @throws[Exception]
  override def tagSentence(text: String): Sentence =
    new Sentence(
      TwitterKoreanProcessor.tokenize(
        TwitterKoreanProcessor.normalize(text)
      ).map {
        tok =>
          new Word(originalWord = tok.text,
            morphemes = Seq(
              new Morpheme(morpheme = tok.text, rawTag = tok.pos.toString, processor = Processor.Twitter)
            )
          )
      }
    )
}


