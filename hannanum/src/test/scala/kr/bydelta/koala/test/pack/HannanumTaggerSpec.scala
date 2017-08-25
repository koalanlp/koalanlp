package kr.bydelta.koala.test.pack

import java.io.File

import kaist.cilab.jhannanum.common.communication.Sentence
import kaist.cilab.jhannanum.common.workflow.Workflow
import kaist.cilab.jhannanum.plugin.major.morphanalyzer.impl.ChartMorphAnalyzer
import kaist.cilab.jhannanum.plugin.major.postagger.impl.HMMTagger
import kaist.cilab.jhannanum.plugin.supplement.MorphemeProcessor.UnknownMorphProcessor.UnknownProcessor
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.InformalSentenceFilter.InformalSentenceFilter
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.SentenceSegmentor.SentenceSegmentor
import kaist.cilab.parser.berkeleyadaptation.Configuration
import kr.bydelta.koala.hnn.{Dictionary, Tagger}
import kr.bydelta.koala.test.core.TaggerSpec
import kr.bydelta.koala.traits.CanTag

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

/**
  * Created by bydelta on 16. 7. 26.
  */
class HannanumTaggerSpec extends TaggerSpec {
  val workflow = {
    Configuration.hanBaseDir synchronized {
      Configuration.hanBaseDir = "./"
      val workflow = new Workflow
      val basePath = "./"

      workflow.appendPlainTextProcessor(new SentenceSegmentor,
        basePath + File.separator + "conf" + File.separator + "SentenceSegment.json")
      workflow.appendPlainTextProcessor(new InformalSentenceFilter,
        basePath + File.separator + "conf" + File.separator + "InformalSentenceFilter.json")

      workflow.setMorphAnalyzer(new ChartMorphAnalyzer,
        basePath + File.separator + "conf" + File.separator + "ChartMorphAnalyzer.json")
      workflow.appendMorphemeProcessor(new UnknownProcessor,
        basePath + File.separator + "conf" + File.separator + "UnknownMorphProcessor.json")

      workflow.setPosTagger(new HMMTagger,
        basePath + File.separator + "conf" + File.separator + "HmmPosTagger.json")
      workflow.activateWorkflow(true)
      workflow
    }
  }
  val prevEnd = Dictionary.userDic.search_end

  override def isSentenceSplitterImplemented: Boolean = false

  override def tagSentByOrig(str: String): (String, String) =
    workflow.synchronized {
      workflow.analyze(str)
      val original = retrieveSentences()
      val tag = original.flatMap(_.getEojeols).map {
        e =>
          e.getMorphemes.zip(e.getTags).map {
            case (m, t) => s"$m/$t"
          }.mkString("+")
      }.mkString(" ")
      val surface = original.flatMap(_.getPlainEojeols).mkString(" ")
      surface -> tag
    }

  override def tagParaByOrig(str: String): Seq[String] =
    workflow.synchronized {
      workflow.analyze(str)
      val original = retrieveSentences()
      original.map(_.getEojeols.map(_.getMorphemes.mkString("+")).mkString(" "))
    }

  override def getTagger: CanTag =
    new Tagger()

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
