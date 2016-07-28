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

  override private[koala] def convert(result: Seq[KoreanToken]): Sentence = {
    new Sentence(
      new Iterator[Seq[KoreanToken]]{
        val it = result.iterator

        override def hasNext: Boolean = it.hasNext

        override def next(): Seq[KoreanToken] = {
          it.takeWhile(!_.text.matches("(?U)^\\s+$")).toSeq
        }
      }.map {
        tokens =>
          new Word(surface = tokens.map(_.text).mkString,
            morphemes =
              tokens.map{
                tok => new Morpheme(surface = tok.text, rawTag = tok.pos.toString, processor = Processor.Twitter)
              }
          )
      }.toSeq
    )
  }
}


