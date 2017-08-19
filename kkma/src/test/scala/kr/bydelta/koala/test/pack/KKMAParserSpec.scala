package kr.bydelta.koala.test.pack

import kr.bydelta.koala.POS
import kr.bydelta.koala.data.{Relationship, Sentence}
import kr.bydelta.koala.kkma.{Dictionary, Parser}
import kr.bydelta.koala.test.core.Examples
import org.snu.ids.ha.ma.MorphemeAnalyzer
import org.snu.ids.ha.sp.{ParseTreeNode, Parser => KParser}
import org.specs2.execute.Result
import org.specs2.mutable._

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

/**
  * Created by bydelta on 16. 7. 26.
  */
class KKMAParserSpec extends Specification with Examples {
  sequential

  final def iterateTree(word: Set[Relationship], parent: String, sentence: Sentence,
                        buf: ArrayBuffer[String] = ArrayBuffer()): ArrayBuffer[String] = {
    word.foreach {
      w =>
        val rawTag = w.rawRel
        val target = sentence(w.target)
        buf += (parent + "--" + rawTag + "-->" + target.surface)
        iterateTree(target.dependents, target.surface, sentence, buf)
    }
    buf
  }

  def getEojeolText(e: ParseTreeNode) =
    if (e == null) "ROOT"
    else e.getExp

  "KKMAParser" should {
    "handle empty sentence" in {
      val sent = new Parser().parse("")
      sent must beEmpty
    }

    "parse a sentence" in {
      val parser = new Parser()
      val kkpa = new KParser
      val kkma = new MorphemeAnalyzer

      Result.foreach(exampleSequence().filter(_._1 == 1).map(_._2)) { sent =>
        val tagged = parser.parse(sent).head

        val original = kkpa.parse(
          kkma.divideToSentences(kkma.leaveJustBest(
            kkma.postProcess(kkma.analyze(sent)))).asScala.head
        )

        val oNodes = original.getNodeList
        val oEdges = original.getEdgeList.asScala.map {
          e => getEojeolText(oNodes.get(e.getFromId)) + "--" + e.getRelation + "-->" + getEojeolText(oNodes.get(e.getToId))
        }.sorted.mkString("\n")

        iterateTree(tagged.root.dependents, "ROOT", tagged).sorted.mkString("\n") must_== oEdges
      }
    }

    "be thread-safe" in {
      val sents = exampleSequence().filter(_._1 == 1).map(_._2)

      val multithreaded = sents.par.map {
        sent =>
          val parsed = new Parser().parse(sent).head
          iterateTree(parsed.root.dependents, "ROOT", parsed).sorted.mkString("\n")
      }.seq.mkString("\n")

      val kkpa = new KParser
      val kkma = new MorphemeAnalyzer

      val singlethreaded = sents.map {
        sent =>
          val original = kkpa.parse(
            kkma.divideToSentences(kkma.leaveJustBest(
              kkma.postProcess(kkma.analyze(sent)))).asScala.head
          )

          val oNodes = original.getNodeList
          original.getEdgeList.asScala.map {
            e => getEojeolText(oNodes.get(e.getFromId)) + "--" + e.getRelation + "-->" + getEojeolText(oNodes.get(e.getToId))
          }.sorted.mkString("\n")
      }.mkString("\n")

      multithreaded must_== singlethreaded
    }

    "supports dictionary" in {
      val sent = "아햏햏, 2000년대에 유행한 통신은어로, 개벽이, 햏햏 등의 여러 신조어를 유통시켰다."

      val noUserDict = new Parser().parse(sent).map(_.treeString).mkString("\n")

      Dictionary.addUserDictionary("아햏햏" -> POS.IC, "개벽이" -> POS.NNP, "햏햏" -> POS.NNG)

      val dictApplied = new Parser().parse(sent).map(_.treeString).mkString("\n")

      noUserDict must_!= dictApplied
    }
  }
}
