package kr.bydelta.koala.hnn

import java.io.File

import kaist.cilab.jhannanum.common.communication.Sentence
import kaist.cilab.jhannanum.common.workflow.Workflow
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.InformalSentenceFilter.InformalSentenceFilter
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.SentenceSegmentor.SentenceSegmentor
import kr.bydelta.koala.data.{Sentence => KSent}
import kr.bydelta.koala.traits.CanSplitSentence

import scala.collection.JavaConversions._

/**
  * 한나눔 문장분리기
  */
final class SentenceSplitter extends CanSplitSentence {
  /** 한나눔 분석 Workflow **/
  private val workflow = {
    val workflow = new Workflow
    val basePath = Dictionary.extractResource()

    workflow.appendPlainTextProcessor(new SentenceSegmentor,
      basePath + File.separator + "conf" + File.separator + "SentenceSegment.json")
    workflow.appendPlainTextProcessor(new InformalSentenceFilter,
      basePath + File.separator + "conf" + File.separator + "InformalSentenceFilter.json")
    workflow.activateWorkflow(false)

    workflow
  }

  override def sentences(text: String): Seq[String] = {
    workflow.analyze(text)
    workflow.getResultOfDocument(new Sentence(0, 0, false)).map(_.toString)
  }
}
