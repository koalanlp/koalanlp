package kr.bydelta.koala.test.core

import kr.bydelta.koala.traits.CanSplitSentence
import org.specs2.execute.Result
import org.specs2.mutable._

/**
  * Created by bydelta on 16. 7. 26.
  */
trait SplitterSpec extends Specification with Examples {
  sequential

  def getSplitter: CanSplitSentence

  def getOriginalSplitterCount(para: String): Int

  "SentenceSplitter" should {
    "handle empty sentence" in {
      val sent = getSplitter.sentences("")
      sent must beEmpty
    }

    "split sentences" in {
      Result.foreach(exampleSequence(requireMultiLine = true)) {
        case (_, sent) =>
          getSplitter.sentences(sent).length must_== getOriginalSplitterCount(sent)
      }
    }

    "be thread-safe" in {
      val sents = exampleSequence(requireMultiLine = true, duplicate = 2)

      val multithreaded = sents.par.map {
        case (_, sent) =>
          getSplitter.sentences(sent)
      }.seq.mkString("\n")

      val splitter = getSplitter
      val singlethreaded = sents.map {
        case (_, sent) =>
          splitter.sentences(sent)
      }.mkString("\n")

      Result.foreach(multithreaded.zip(singlethreaded)) {
        case (s, m) => s must_== m
      }
    }
  }
}
