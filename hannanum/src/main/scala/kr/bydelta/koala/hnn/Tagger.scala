package kr.bydelta.koala.hnn

import java.io.File

import kaist.cilab.jhannanum.common.communication.Sentence
import kaist.cilab.jhannanum.common.workflow.Workflow
import kaist.cilab.jhannanum.plugin.supplement.MorphemeProcessor.UnknownMorphProcessor.UnknownProcessor
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.InformalSentenceFilter.InformalSentenceFilter
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.SentenceSegmentor.SentenceSegmentor
import kaist.cilab.jhannanum.plugin.supplement.PosProcessor.NounExtractor.NounExtractor
import kr.bydelta.koala.HannanumTextAddon.HannanumTextAddon
import kr.bydelta.koala.data.{Morpheme, Word, Sentence => KSent}
import kr.bydelta.koala.helper.{SafeChartMorphAnalyzer, SafeHMMTagger}
import kr.bydelta.koala.traits.CanTag
import kr.bydelta.koala.{HannanumTextAddon, Processor}

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

/**
  * 한나눔 품사분석기.
  *
  * @param useUnknownMorph  등재되지 않은 형태소 처리기 사용여부 (기본값 true)
  * @param useNounExtractor 명사구 추출기 사용여부 (기본값 false)
  * @param addons           기타 한나눔 문장분석 Addon들
  */
final class Tagger(useUnknownMorph: Boolean,
                   useNounExtractor: Boolean,
                   addons: Seq[HannanumTextAddon] = Seq(
                     HannanumTextAddon.SentenceSegment, HannanumTextAddon.InformalSentenceFilter
                   )) extends CanTag[Sentence] {

  /** 한나눔 품사분석 Workflow **/
  private lazy val workflow = {
    val workflow = new Workflow
    val basePath = Dictionary.extractResource()

    addons.foreach {
      case HannanumTextAddon.SentenceSegment =>
        workflow.appendPlainTextProcessor(new SentenceSegmentor,
          basePath + File.separator + "conf" + File.separator + "SentenceSegment.json")
      case HannanumTextAddon.InformalSentenceFilter =>
        workflow.appendPlainTextProcessor(new InformalSentenceFilter,
          basePath + File.separator + "conf" + File.separator + "InformalSentenceFilter.json")
      case _ =>
    }

    workflow.setMorphAnalyzer(analyzer,
      basePath + File.separator + "conf" + File.separator + "ChartMorphAnalyzer.json")
    if (useUnknownMorph) {
      workflow.appendMorphemeProcessor(new UnknownProcessor,
        basePath + File.separator + "conf" + File.separator + "UnknownMorphProcessor.json")
    }

    workflow.setPosTagger(new SafeHMMTagger,
      basePath + File.separator + "conf" + File.separator + "HmmPosTagger.json")
    if (useNounExtractor) {
      workflow.appendPosProcessor(new NounExtractor,
        basePath + File.separator + "conf" + File.separator + "NounExtractor.json")
    }
    workflow.activateWorkflow(false)
    workflow
  }
  /** 한나눔 형태소분석기 (사용자사전 개량형) **/
  private lazy val analyzer = {
    val analyzer = new SafeChartMorphAnalyzer
    analyzer.addMorphemes(Dictionary.userDict)
    analyzer
  }

  /**
    * Java를 위한 생성자 분리.
    *
    * @return 생성된 객체.
    */
  def this() = this(true, false, Seq(
    HannanumTextAddon.SentenceSegment, HannanumTextAddon.InformalSentenceFilter
  ))

  def tagSentence(text: String): KSent = {
    convert(tagSentenceRaw(text))
  }

  override private[koala] def convert(result: Sentence): KSent =
    new KSent(
      words =
        result.getEojeols.zip(result.getPlainEojeols).map {
          case (eojeol, plain) =>
            new Word(
              surface = plain,
              morphemes =
                eojeol.getMorphemes.zip(eojeol.getTags).map {
                  case (morph, tag) =>
                    new Morpheme(surface = morph, rawTag = tag, processor = Processor.Hannanum)
                }
            )
        }
    )

  override def tagSentenceRaw(text: String): Sentence = {
    workflow.analyze(text)
    workflow.getResultOfSentence(new Sentence(0, 0, true))
  }

  def tagParagraph(text: String): Seq[KSent] = {
    workflow.analyze(text.replaceAll("\\s*([^ㄱ-힣0-9A-Za-z,\\.!\\?\'\"]+)\\s*", " $1 "))
    retrieveSentences()
  }

  @throws[Throwable]
  override protected def finalize() {
    workflow.close()
    super.finalize()
  }

  /**
    * 문장결과를 읽어들임.
    *
    * @param acc 읽어들인 문장들이 누적되는 버퍼.
    * @return 문장분리 결과.
    */
  @tailrec
  private def retrieveSentences(acc: ArrayBuffer[KSent] = ArrayBuffer()): ArrayBuffer[KSent] = {
    (try {
      Some(workflow.getResultOfSentence(new Sentence(0, 0, false)))
    } catch {
      case _: Throwable => None.asInstanceOf[Sentence]
    }) match {
      case Some(sent: Sentence) if sent.getEojeols != null =>
        acc += convert(sent)
        if (!sent.isEndOfDocument)
          retrieveSentences(acc)
        else
          acc
      case _ => acc
    }
  }
}
