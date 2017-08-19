package kr.bydelta.koala.twt

import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.traits.CanTag
import org.openkoreantext.processor.OpenKoreanTextProcessor
import org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken

/**
  * 트위터 품사분석기.
  */
class Tagger extends CanTag[Seq[KoreanToken]] {
  override def tagParagraph(text: String): Seq[Sentence] = {
    OpenKoreanTextProcessor.splitSentences(text).map {
      sent =>
        tagSentence(sent.text)
    }
  }

  override def tagSentenceRaw(text: String): Seq[KoreanToken] =
    OpenKoreanTextProcessor.tokenize(
      OpenKoreanTextProcessor.normalize(text)
    )

  override private[koala] def convert(result: Seq[KoreanToken]): Sentence = {
    Sentence(
      new Iterator[Seq[KoreanToken]]{
        val it = result.iterator

        override def hasNext: Boolean = it.hasNext

        override def next(): Seq[KoreanToken] = {
          it.takeWhile(!_.text.matches("(?U)^\\s+$")).toSeq
        }
      }.map {
        tokens =>
          Word(tokens.map(_.text).mkString,
            tokens.map {
              tok => Morpheme(tok.text, tok.pos.toString, fromTwtTag(tok.pos.toString))
            }
          )
      }.toSeq
    )
  }
}


