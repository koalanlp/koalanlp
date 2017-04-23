package kr.bydelta.koala.test.pack

import java.io.File

import kaist.cilab.jhannanum.common.communication.PlainSentence
import kaist.cilab.jhannanum.common.workflow.Workflow
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.SentenceSegmentor.SentenceSegmentor
import kaist.cilab.parser.berkeleyadaptation.Configuration
import kr.bydelta.koala.hnn.SentenceSplitter
import kr.bydelta.koala.test.core.SplitterSpec
import kr.bydelta.koala.traits.CanSplitSentence

import scala.annotation.tailrec

/**
  * Created by bydelta on 16. 7. 26.
  */
class HannanumSplitterSpec extends SplitterSpec {
  val workflow = {
    Configuration.hanBaseDir synchronized {
      Configuration.hanBaseDir = "./"
      val workflow = new Workflow
      val basePath = "./"

      workflow.appendPlainTextProcessor(new SentenceSegmentor,
        basePath + File.separator + "conf" + File.separator + "SentenceSegment.json")
      workflow.activateWorkflow(true)
      workflow
    }
  }

  override def getOriginalSplitterCount(para: String): Int =
    workflow.synchronized {
      workflow.analyze(para)
      countSentences()
    }

  override def getSplitter: CanSplitSentence = new SentenceSplitter()

  @tailrec
  private def countSentences(acc: Int = 0): Int = {
    (try {
      Some(workflow.getResultOfSentence(new PlainSentence(0, 0, false)))
    } catch {
      case _: Throwable => None.asInstanceOf[PlainSentence]
    }) match {
      case Some(sent: PlainSentence) =>
        if (!sent.isEndOfDocument)
          countSentences(acc + 1)
        else
          acc + 1
      case _ => acc
    }
  }
}
