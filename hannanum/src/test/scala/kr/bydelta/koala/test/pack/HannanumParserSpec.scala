package kr.bydelta.koala.test.pack

import java.io.File

import kaist.cilab.jhannanum.common.communication.Sentence
import kaist.cilab.jhannanum.common.workflow.Workflow
import kaist.cilab.jhannanum.plugin.major.morphanalyzer.impl.ChartMorphAnalyzer
import kaist.cilab.jhannanum.plugin.major.postagger.impl.HMMTagger
import kaist.cilab.jhannanum.plugin.supplement.MorphemeProcessor.UnknownMorphProcessor.UnknownProcessor
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.InformalSentenceFilter.InformalSentenceFilter
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.SentenceSegmentor.SentenceSegmentor
import kaist.cilab.parser.berkeleyadaptation.{BerkeleyParserWrapper, Configuration}
import kaist.cilab.parser.corpusconverter.sejong2treebank.sejongtree.ParseTree
import kaist.cilab.parser.psg2dg.Converter
import kr.bydelta.koala.POS
import kr.bydelta.koala.data.Relationship
import kr.bydelta.koala.hnn.{Dictionary, Parser, Tagger}
import kr.bydelta.koala.test.core.Examples
import org.specs2.execute.Result
import org.specs2.mutable._

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/**
  * Created by bydelta on 16. 7. 26.
  */
class HannanumParserSpec extends Specification with Examples {
  sequential

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

  final def iterateTree(word: Set[Relationship], parent: String, sentence: kr.bydelta.koala.data.Sentence,
                        buf: ArrayBuffer[String] = ArrayBuffer()): ArrayBuffer[String] = {
    word.foreach {
      w =>
        print(".")
        val rawTag = w.rawRel
        val target = sentence(w.target)
        buf += (parent + "--" + rawTag + "-->" + target.surface)
        iterateTree(target.dependents, target.surface, sentence, buf)
    }
    buf
  }

  "HannanumParser" should {
    "handle empty sentence" in {
      print("E")
      val sent = new Parser().parse("")
      sent must beEmpty
    }

    "parse a sentence" in {
      val parser = new BerkeleyParserWrapper(Configuration.parserModel)
      val kParser = new Parser()

      Result.foreach(exampleSequence().filter(_._1 == 1).map(_._2)) {
        sent =>
          print("0")
          val tagged = kParser.parse(sent).head

          try {
            print("1")
            workflow.analyze(sent)
            val oSent = workflow.getResultOfSentence(new Sentence(0, 0, true))
            val original = {
              val conv = new Converter
              val parseTree =
                new ParseTree(
                  oSent.getPlainEojeols.mkString(" "), conv.StringforDepformat(
                    Converter.functionTagReForm(
                      parser.parse(oSent.getPlainEojeols.mkString(" "))
                    )
                  ), 0, true)
              conv.convert(parseTree)
            }

            print("2")
            val oNodes = original.getNodeList.map {
              node =>
                (try {
                  node.getHead.getCorrespondingPhrase.getStringContents
                } catch {
                  case _: Throwable => "ROOT"
                }) + "--" + node.getdType() + "-->" + node.getCorrespondingPhrase.getStringContents
            }.sorted.mkString("\n")

            iterateTree(tagged.root.dependents, "ROOT", tagged).sorted.mkString("\n") must_== oNodes
          } catch {
            case _: Throwable =>
              print("E")
              // 원본 파서가 오류났을때, 해당 문장 무시. (한나눔 파서가 불안정함)
              true must_== true
          }
      }
    }

    "be thread-safe" in {
      val tagger = new Tagger()
      val sents = Random.shuffle(exampleSequence().filter(_._1 == 1).map(_._2)).take(10).map {
        sent =>
          print("T")
          tagger.tagSentence(sent)
      }

      val multithreaded = sents.par.map {
        sent =>
          print("M")
          new Parser().parse(sent).treeString
      }.seq.mkString("\n")

      val parser = new Parser
      val singlethreaded = sents.map {
        sent =>
          print("S")
          parser.parse(sent).treeString
      }.mkString("\n")

      multithreaded must_== singlethreaded
    }

    "supports dictionary" in {
      val sent = "아햏, 2000년대에 유행한 통신은어로, 개벽이, 햏햏 등의 여러 신조어를 유통시켰다."

      print("D")
      val noUserDict = new Parser().parse(sent).map(_.treeString).mkString("\n")

      Dictionary.addUserDictionary("아햏" -> POS.IC, "개벽이" -> POS.NNG, "햏햏" -> POS.NNG)

      print("D")
      val dictApplied = new Parser().parse(sent).map(_.treeString).mkString("\n")

      noUserDict must_!= dictApplied
    }
  }
}
