package kr.bydelta.koala.custom

import java.io.{File, FileInputStream, InputStream}

import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.traits.{CanTag, CanTagOnlyASentence}
import kr.bydelta.koala.{KoreanStringExtension, POS}
import opennlp.tools.postag.{POSModel, POSTaggerME}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by bydelta on 17. 8. 18.
  */
class Tagger(model: POSModel) extends CanTagOnlyASentence[Sentence] {
  private val tag = new POSTaggerME(model)
  private val tokenizer = new POSTaggerME(TOK_MODEL)

  def this(stream: InputStream) {
    this(new POSModel(stream))
  }

  def this(path: String) {
    this(new FileInputStream(new File(path)))
  }

  def this() {
    this(POS_MODEL)
  }

  /**
    * 주어진 String 문단을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @return 품사분석 결과를 문장단위로 잘라서 포함한 Seq[Sentence] 객체
    */
  override def tagParagraph(text: String): Seq[Sentence] = ???

  override def tagSentenceRaw(text: String): Sentence = {
    val dissembled = text.dissembleHangul.replaceAll("\\s+", " <SP> ")
    val tokenized = tokenizer.tokenize(text)
    val tags = tag.tag(tokenized)

    new Sentence(
      words = tokenized.zip(tags).map {
        case (disToken, tag) =>
          val token = assembleKorean(disToken)


      }.toVector
    )
  }

  private def readMorphemes(tokens: Seq[String], tags: Seq[String],
                            words: ArrayBuffer[Word] = ArrayBuffer(),
                            morphs: ArrayBuffer[Morpheme] = ArrayBuffer()): Vector[Word] =
    if (tokens.isEmpty) {
      if (morphs.nonEmpty) {
        words += Word(surface = assembleKorean(morphs.map(_.surface).mkString), morphemes = morphs)
      }

      words.toVector
    } else {
      if (tokens.head == "<SP>") {
        words += Word(surface = assembleKorean(morphs.map(_.surface)), morphemes = morphs)
        readMorphemes(tokens.tail, tags.tail, words)
      } else {
        val tag = tags.head
        morphs += Morpheme(surface = assembleKorean(tokens.head), rawTag = tag, tag = POS.withName(tag))
        readMorphemes(tokens.tail, tags.tail, words, morphs)
      }
    }

  override private[koala] def convert(result: Sentence): Sentence = result
}
