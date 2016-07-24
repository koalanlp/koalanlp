package kr.bydelta.koala.twt

import com.twitter.penguin.korean.TwitterKoreanProcessor
import com.twitter.penguin.korean.tokenizer.KoreanTokenizer.KoreanToken
import kr.bydelta.koala.Processor
import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.traits.CanTag

/**
  * 트위터 품사분석기.
  */
class Tagger extends CanTag[Seq[KoreanToken]] {
  override def tagParagraph(text: String): Seq[Sentence] = {
    TwitterKoreanProcessor.splitSentences(text).map {
      sent =>
        tagSentence(sent.text)
    }
  }

  override def tagSentence(text: String): Sentence =
    convert(tagSentenceRaw(text))

  override def tagSentenceRaw(text: String): Seq[KoreanToken] =
    TwitterKoreanProcessor.tokenize(
      TwitterKoreanProcessor.normalize(text)
    )

  override private[koala] def convert(result: Seq[KoreanToken]): Sentence =
    new Sentence(
      result.map {
        tok =>
          new Word(surface = tok.text,
            morphemes = Seq(
              new Morpheme(surface = tok.text, rawTag = tok.pos.toString, processor = Processor.Twitter)
            )
          )
      }
    )
}


