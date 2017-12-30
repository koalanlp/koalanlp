package kr.bydelta.koala.twt

import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.traits.CanTagAParagraph
import org.openkoreantext.processor.OpenKoreanTextProcessor
import org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken


/**
  * 트위터 품사분석기.
  */
class Tagger extends CanTagAParagraph[Seq[KoreanToken]] {
  override def tagParagraphOriginal(text: String): Seq[Seq[KoreanToken]] = {
    OpenKoreanTextProcessor.splitSentences(text).map(x => tagSentenceOriginal(x.text))
  }

  override def tagSentenceOriginal(text: String): Seq[KoreanToken] = {
    OpenKoreanTextProcessor.tokenize(
      OpenKoreanTextProcessor.normalize(text)
    )
  }

  override private[koala] def convertSentence(result: Seq[KoreanToken]): Sentence = {
    Sentence(
      new Iterator[Seq[KoreanToken]] {
        val it: Iterator[KoreanToken] = result.iterator

        override def hasNext: Boolean = it.hasNext

        override def next(): Seq[KoreanToken] = {
          it.takeWhile(!_.text.matches("(?U)^\\s+$")).toSeq
        }
      }.map {
        tokens =>
          Word(tokens.map(_.text).mkString,
            tokens.map {
              tok => Morpheme(tok.text, tok.pos.toString, toSejongPOS(tok.pos.toString))
            }
          )
      }.toSeq
    )
  }
}


