package kr.bydelta.koala.hnn

import java.io.File

import kaist.cilab.jhannanum.common.communication.Sentence
import kaist.cilab.jhannanum.common.workflow.Workflow
import kaist.cilab.jhannanum.plugin.major.morphanalyzer.impl.ChartMorphAnalyzer
import kaist.cilab.jhannanum.plugin.major.postagger.impl.HMMTagger
import kaist.cilab.jhannanum.plugin.supplement.MorphemeProcessor.UnknownMorphProcessor.UnknownProcessor
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.InformalSentenceFilter.InformalSentenceFilter
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.SentenceSegmentor.SentenceSegmentor
import kr.bydelta.koala.POS
import org.specs2.mutable._

/**
  * Created by bydelta on 16. 7. 26.
  */
class TaggerSpec extends Specification {
  sequential

  val workflow = {
    Dictionary synchronized {
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

  "HannanumTagger" should {
    "tag a sentence" in {
      val sent = "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐고, 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다."
      val tagged = new Tagger().tagSentence(sent)
      workflow.analyze(sent)
      val original = workflow.getResultOfSentence(new Sentence(0, 0, true))

      tagged.map(_.map(_.surface).mkString("+")).mkString(" ") must_==
        original.getEojeols.map(_.getMorphemes.mkString("+")).mkString(" ")
      tagged.map(_.surface).mkString(" ") must_== original.getPlainEojeols.mkString(" ")
      tagged.map(_.surface).mkString must_== sent.replaceAll("\\s+", "")
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
          new Tagger().tagSentence(sent).map(_.map(_.surface).mkString("+")).mkString(" ")
      }.seq.mkString("\n")

      val singlethreaded = sents.map {
        sent =>
          workflow.analyze(sent)
          workflow.getResultOfSentence(new Sentence(0, 0, true))
            .getEojeols.map(_.getMorphemes.mkString("+")).mkString(" ")
      }.mkString("\n")

      multithreaded must_== singlethreaded
    }

    "supports dictionary" in {
      val sent = "아햏햏, 2000년대에 유행한 통신은어로, 개벽이, 햏햏 등의 여러 신조어를 유통시켰다."

      val noUserDict = new Tagger().tagSentence(sent).singleLineString

      val prevEnd = Dictionary.userDic.search_end

      Dictionary.addUserDictionary("아햏햏" -> POS.IC, "개벽이" -> POS.NNG, "햏햏" -> POS.NNG)

      val dictApplied = new Tagger().tagSentence(sent).singleLineString

      Dictionary.userDic.search_end != prevEnd

      noUserDict must_!= dictApplied
    }
  }
}
