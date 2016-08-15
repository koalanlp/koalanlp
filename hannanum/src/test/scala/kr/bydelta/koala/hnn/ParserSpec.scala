package kr.bydelta.koala.hnn

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
import kr.bydelta.koala.data.Word
import org.specs2.mutable._

import scala.collection.mutable.ArrayBuffer

/**
  * Created by bydelta on 16. 7. 26.
  */
class ParserSpec extends Specification {
  sequential

  val workflow = {
    Dictionary synchronized {
      Configuration.hanBaseDir = "./"
      val workflow = new Workflow
      val basePath = Dictionary.extractResource()

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

  final def iterateTree(word: Seq[Word], parent: String,
                        buf: ArrayBuffer[String] = ArrayBuffer()): ArrayBuffer[String] = {
    word.foreach {
      w =>
        val rawTag = w.rawDepTag
        buf += (parent + "--" + rawTag + "-->" + w.surface)
        iterateTree(w.dependents, w.surface, buf)
    }
    buf
  }

  "HannanumParser" should {
    "handle empty sentence" in {
      val sent = new Parser().parse("")
      sent.words must beEmpty
    }

    "parse a sentence" in {
      val sent = "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐고, 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다."
      val tagged = new Parser().parse(sent)
      val parser = new BerkeleyParserWrapper(Configuration.parserModel)

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

      val oNodes = original.getNodeList.map {
        node =>
          (try {
            node.getHead.getCorrespondingPhrase.getStringContents
          } catch {
            case _: Throwable => "ROOT"
          }) + "--" + node.getdType() + "-->" + node.getCorrespondingPhrase.getStringContents
      }.sorted.mkString("\n")

      iterateTree(tagged.topLevels, "ROOT").sorted.mkString("\n") must_== oNodes
    }

    "be thread-safe" in {
      val sents = Seq(
        "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐고, 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다.",
        "국토부는 시장 상황과 맞지 않는 일률적인 규제를 탄력적으로 적용할 수 있도록 법 개정을 추진하는 것이라고 설명하지만, 투기 세력에 기대는 부동산 부양책이라는 비판이 일고 있다.",
        "나라가 취로사업이라도 만들어주지 않으면 일이 없어.",
        "미국 국방부가 미국 미사일방어망(MD)의 핵심 무기체계인 사드(THAAD)를 한국에 배치하는 방안을 검토하고 있다고 <월스트리트 저널>이 28일(현지시각) 보도했다.",
        "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐고, 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다.",
        "국토부는 시장 상황과 맞지 않는 일률적인 규제를 탄력적으로 적용할 수 있도록 법 개정을 추진하는 것이라고 설명하지만, 투기 세력에 기대는 부동산 부양책이라는 비판이 일고 있다.",
        "나라가 취로사업이라도 만들어주지 않으면 일이 없어.",
        "미국 국방부가 미국 미사일방어망(MD)의 핵심 무기체계인 사드(THAAD)를 한국에 배치하는 방안을 검토하고 있다고 <월스트리트 저널>이 28일(현지시각) 보도했다.",
        "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐고, 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다.",
        "국토부는 시장 상황과 맞지 않는 일률적인 규제를 탄력적으로 적용할 수 있도록 법 개정을 추진하는 것이라고 설명하지만, 투기 세력에 기대는 부동산 부양책이라는 비판이 일고 있다.",
        "나라가 취로사업이라도 만들어주지 않으면 일이 없어.",
        "미국 국방부가 미국 미사일방어망(MD)의 핵심 무기체계인 사드(THAAD)를 한국에 배치하는 방안을 검토하고 있다고 <월스트리트 저널>이 28일(현지시각) 보도했다.",
        "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐고, 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다.",
        "국토부는 시장 상황과 맞지 않는 일률적인 규제를 탄력적으로 적용할 수 있도록 법 개정을 추진하는 것이라고 설명하지만, 투기 세력에 기대는 부동산 부양책이라는 비판이 일고 있다.",
        "나라가 취로사업이라도 만들어주지 않으면 일이 없어.",
        "미국 국방부가 미국 미사일방어망(MD)의 핵심 무기체계인 사드(THAAD)를 한국에 배치하는 방안을 검토하고 있다고 <월스트리트 저널>이 28일(현지시각) 보도했다."
      )

      val multithreaded = sents.par.map {
        sent =>
          new Parser().parse(sent).treeString
      }.seq.mkString("\n")

      val parser = new Parser
      val singlethreaded = sents.map {
        sent =>
          parser.parse(sent).treeString
      }.mkString("\n")

      multithreaded must_== singlethreaded
    }

    "supports dictionary" in {
      val sent = "아햏, 2000년대에 유행한 통신은어로, 개벽이, 햏햏 등의 여러 신조어를 유통시켰다."

      val noUserDict = new Parser().parse(sent).treeString

      Dictionary.addUserDictionary("아햏" -> POS.IC, "개벽이" -> POS.NNG, "햏햏" -> POS.NNG)

      val dictApplied = new Parser().parse(sent).treeString

      noUserDict must_!= dictApplied
    }
  }
}
