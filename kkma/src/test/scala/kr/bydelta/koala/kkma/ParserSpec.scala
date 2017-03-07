package kr.bydelta.koala.kkma

import kr.bydelta.koala.POS
import kr.bydelta.koala.data.{Relationship, Sentence}
import org.snu.ids.ha.ma.MorphemeAnalyzer
import org.snu.ids.ha.sp.{ParseTreeNode, Parser => KParser}
import org.specs2.mutable._

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

/**
  * Created by bydelta on 16. 7. 26.
  */
class ParserSpec extends Specification {
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
      sent.words must beEmpty
    }

    "parse a sentence" in {
      val sent = "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐고, 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다."
      val tagged = new Parser().parse(sent)
      val kkpa = new KParser
      val kkma = new MorphemeAnalyzer

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
          val parsed = new Parser().parse(sent)
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

      val noUserDict = new Parser().parse(sent).treeString

      Dictionary.addUserDictionary("아햏햏" -> POS.IC, "개벽이" -> POS.NNP, "햏햏" -> POS.NNG)

      val dictApplied = new Parser().parse(sent).treeString

      noUserDict must_!= dictApplied
    }
  }
}
