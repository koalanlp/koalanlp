package kr.bydelta.koala.hnn

import java.io.File

import kaist.cilab.jhannanum.common.communication.Sentence
import kaist.cilab.jhannanum.common.workflow.Workflow
import kaist.cilab.jhannanum.plugin.supplement.MorphemeProcessor.UnknownMorphProcessor.UnknownProcessor
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.InformalSentenceFilter.InformalSentenceFilter
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.SentenceSegmentor.SentenceSegmentor
import kr.bydelta.koala.data.{Morpheme, Word, Sentence => KSent}
import kr.bydelta.koala.helper.{SafeChartMorphAnalyzer, SafeHMMTagger}
import kr.bydelta.koala.traits.CanTagOnlyAParagraph

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

/**
  * 한나눔 품사분석기.
  */
final class Tagger extends CanTagOnlyAParagraph[Sentence] {
  /** 한나눔 품사분석 Workflow **/
  private lazy val workflow = {
    val workflow = new Workflow
    val basePath = Dictionary.extractResource()

    workflow.appendPlainTextProcessor(new SentenceSegmentor,
      basePath + File.separator + "conf" + File.separator + "SentenceSegment.json")
    workflow.appendPlainTextProcessor(new InformalSentenceFilter,
      basePath + File.separator + "conf" + File.separator + "InformalSentenceFilter.json")

    workflow.setMorphAnalyzer(analyzer,
      basePath + File.separator + "conf" + File.separator + "ChartMorphAnalyzer.json")
    workflow.appendMorphemeProcessor(new UnknownProcessor,
      basePath + File.separator + "conf" + File.separator + "UnknownMorphProcessor.json")

    workflow.setPosTagger(new SafeHMMTagger,
      basePath + File.separator + "conf" + File.separator + "HmmPosTagger.json")
    workflow.activateWorkflow(false)
    workflow
  }
  /** 한나눔 형태소분석기 (사용자사전 개량형) **/
  private lazy val analyzer = new SafeChartMorphAnalyzer
  private[this] val logger = org.log4s.getLogger

  override def tagParagraphOriginal(text: String): Seq[Sentence] =
    if (text.trim.isEmpty) Seq()
    else {
      try {
        Dictionary synchronized {
          workflow.analyze(text)
          retrieveSentences()
        }
      } catch {
        case e: Throwable =>
          logger.error(e)("Sentence Tagging failed.")
          throw e
      }
    }

  override private[koala] def convertSentence(result: Sentence): KSent =
    KSent(
      result.getEojeols.view.zip(result.getPlainEojeols.view).map {
        case (eojeol, plain) =>
          Word(
            plain,
            eojeol.getMorphemes.view.zip(eojeol.getTags.view).map {
              case (morph, tag) =>
                Morpheme(morph, tag, toSejongPOS(tag))
            }
          )
      }
    )

  /**
    * 문장결과를 읽어들임.
    *
    * @param acc 읽어들인 문장들이 누적되는 버퍼.
    * @return 문장분리 결과.
    */
  @tailrec
  private def retrieveSentences(acc: ArrayBuffer[Sentence] = ArrayBuffer()): ArrayBuffer[Sentence] = {
    (try {
      Some(workflow.getResultOfSentence(new Sentence(0, 0, false)))
    } catch {
      case _: Throwable => None.asInstanceOf[Sentence]
    }) match {
      case Some(sent: Sentence) if sent.getEojeols != null =>
        acc += sent
        if (!sent.isEndOfDocument)
          retrieveSentences(acc)
        else
          acc
      case _ => acc
    }
  }
}
