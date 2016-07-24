package kr.bydelta.koala.hnn

import java.io.File

import kaist.cilab.jhannanum.common.communication.Sentence
import kaist.cilab.jhannanum.common.workflow.Workflow
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.InformalSentenceFilter.InformalSentenceFilter
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.SentenceSegmentor.SentenceSegmentor
import kr.bydelta.koala.data.{Sentence => KSent}
import kr.bydelta.koala.traits.CanSplitSentence

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

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
    workflow.analyze(text.replaceAll("\\s*([^ㄱ-힣0-9A-Za-z,\\.!\\?\'\"]+)\\s*", " $1 "))
    retrieveSentences()
  }

  /**
    * 문장결과를 읽어들임.
    *
    * @param acc 읽어들인 문장들이 누적되는 버퍼.
    * @return 문장분리 결과.
    */
  @tailrec
  private def retrieveSentences(acc: ArrayBuffer[String] = ArrayBuffer()): ArrayBuffer[String] = {
    (try {
      Some(workflow.getResultOfSentence(new Sentence(0, 0, false)))
    } catch {
      case _: Throwable => None.asInstanceOf[Sentence]
    }) match {
      case Some(sent: Sentence) if sent.getEojeols != null =>
        acc += sent.toString
        retrieveSentences(acc)
      case _ => acc
    }
  }
}
