package kr.bydelta.koala.kkma

/**
  * Created by bydelta on 16. 7. 21.
  */

import kr.bydelta.koala
import kr.bydelta.koala.Processor
import kr.bydelta.koala.data.Word
import kr.bydelta.koala.traits.CanTag
import org.snu.ids.ha.ma._

import scala.collection.JavaConversions._

object Tagger {
  lazy val defaultTagger = new Tagger
}

final class Tagger extends CanTag {
  private lazy val ma = {
    val ma = new MorphemeAnalyzer
    ma.createLogger(null)
    Dictionary.reloadDic()
    ma
  }

  @throws[Exception]
  def tagSentence(text: String): koala.data.Sentence = parseResult(analyzeSentenceRaw(text).head)

  @throws[Exception]
  def tagParagraph(text: String): Seq[koala.data.Sentence] = analyzeSentenceRaw(text).map(parseResult)

  def parseResult(result: Sentence): koala.data.Sentence =
    new koala.data.Sentence(
      words =
        result.map {
          eojeol =>
            new Word(
              originalWord = eojeol.getExp,
              morphemes = eojeol.map {
                morph => new koala.data.Morpheme(
                  morpheme = morph.getString,
                  rawTag = morph.getTag,
                  processor = Processor.KKMA
                )
              }
            )
        }
    )

  @throws[Exception]
  private[koala] def analyzeSentenceRaw(text: String): Seq[Sentence] =
    ma.divideToSentences(
      ma.leaveJustBest(
        ma.postProcess(
          ma.analyze(
            text.replaceAll("\\s*([^ㄱ-힣0-9A-Za-z,\\.!\\?\'\"]+)\\s*", " $1 ")
          )
        )
      )
    ).toSeq

  @throws[Throwable]
  override protected def finalize() {
    ma.closeLogger()
    super.finalize()
  }
}

