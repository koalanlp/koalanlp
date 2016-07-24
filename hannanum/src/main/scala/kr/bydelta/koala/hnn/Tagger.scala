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


final class Tagger(useUnknownMorph: Boolean = true,
                   useKnownExtractor: Boolean = false,
                   addons: Seq[HannanumTextAddon] = Seq(
                     HannanumTextAddon.SentenceSegment, HannanumTextAddon.InformalSentenceFilter
                   )) extends CanTag {
  private lazy val workflow = {
    val workflow = new Workflow
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
    if (useKnownExtractor) {
      workflow.appendPosProcessor(new NounExtractor,
        basePath + File.separator + "conf" + File.separator + "NounExtractor.json")
    }
    workflow.activateWorkflow(false)
    workflow
  }

  private lazy val analyzer = {
    val analyzer = new SafeChartMorphAnalyzer
    analyzer.addMorphemes(Dictionary.userDict)
    analyzer
  }

  private lazy val basePath = Dictionary.extractResource()

  @throws[Exception]
  def tagSentence(text: String): KSent = {
    parseResult(tagSentenceRaw(text))
  }

  private[koala] def parseResult(result: Sentence): KSent =
    new KSent(
      words =
        result.getEojeols.zip(result.getPlainEojeols).map {
          case (eojeol, plain) =>
            new Word(
              originalWord = plain,
              morphemes =
                eojeol.getMorphemes.zip(eojeol.getTags).map {
                  case (morph, tag) =>
                    new Morpheme(morpheme = morph, rawTag = tag, processor = Processor.Hannanum)
                }
            )
        }
    )

  private[koala] def tagSentenceRaw(text: String): Sentence = {
    workflow.analyze(text)
    workflow.getResultOfSentence(new Sentence(0, 0, true))
  }

  @throws[Exception]
  def tagParagraph(text: String): Seq[KSent] = {
    workflow.analyze(text.replaceAll("\\s*([^ㄱ-힣0-9A-Za-z,\\.!\\?\'\"]+)\\s*", " $1 "))
    retrieveSentences()
  }

  @throws[Throwable]
  override protected def finalize() {
    workflow.close()
    super.finalize()
  }

  @tailrec
  private def retrieveSentences(acc: ArrayBuffer[KSent] = ArrayBuffer()): ArrayBuffer[KSent] = {
    (try {
      Some(workflow.getResultOfSentence(new Sentence(0, 0, true)))
    } catch {
      case _: Throwable => None.asInstanceOf[Sentence]
    }) match {
      case Some(sent: Sentence) if sent.getEojeols != null =>
        acc += parseResult(sent)
        if (!sent.isEndOfDocument)
          retrieveSentences(acc)
        else
          acc
      case _ => acc
    }
  }
}

object Tagger {
  lazy val defaultTagger: Tagger = new Tagger
}
