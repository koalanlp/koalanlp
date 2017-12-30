package kr.bydelta.koala.kkma

import kr.bydelta.koala
import kr.bydelta.koala.data.{Word, Sentence => KSent}
import kr.bydelta.koala.traits.CanTagOnlyAParagraph
import org.snu.ids.ha.ma._

import scala.collection.JavaConverters._

/**
  * 꼬꼬마 형태소분석기.
  *
  * @param logPath 꼬꼬마 형태소분석기의 log를 저장할 위치.
  */
final class Tagger(logPath: String) extends CanTagOnlyAParagraph[Sentence] {

  /** 꼬꼬마 형태소분석기 객체. **/
  private lazy val ma = {
    Dictionary.reloadDic()
    val ma = new MorphemeAnalyzer
    ma
  }

  /**
    * 꼬꼬마 형태소분석기.
    */
  def this() = this("kkma.log")

  override def tagParagraphOriginal(text: String): Seq[Sentence] =
    if (text.trim.isEmpty)
      Seq()
    else
      ma.divideToSentences(
        ma.leaveJustBest(
          ma.postProcess(
            Dictionary synchronized
              ma.analyze(
                text
              )
          )
        )
      ).asScala

  override private[koala] def convertSentence(result: Sentence): KSent =
    koala.data.Sentence(
      result.asScala.map {
        eojeol =>
          Word(
            eojeol.getExp,
            eojeol.asScala.map {
              morph => koala.data.Morpheme(
                morph.getString,
                morph.getTag,
                toSejongPOS(morph.getTag)
              )
            }
          )
      }
    )
}

