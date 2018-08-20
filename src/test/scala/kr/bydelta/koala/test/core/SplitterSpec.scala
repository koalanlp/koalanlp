package kr.bydelta.koala.test.core

import kr.bydelta.koala.POS
import kr.bydelta.koala.data._
import kr.bydelta.koala.traits.CanSplitSentence
import kr.bydelta.koala.util.SentenceSplitter
import org.specs2.execute.Result
import org.specs2.mutable._

/**
  * Created by bydelta on 16. 7. 26.
  */
class SplitterSpec extends Specification {
  sequential

  val sent1 =
    """집/NNG 앞/NNG+에서/JKB 고추/NNG+를/JKO 말리/VV+던/ETM 이숙희/NNP (/SS 가명/NNG ·/SP 75/SN )/SS 할머니/NNG+의/JKG 얼굴/NNG+에/JKB+는/JX 웃음/NNG+기/NNG+가/JKS 없/VA+었/EP+다/EF ./SF
      |"/SW 나라/NNG+가/JKS 취로/NNG 사업/NNG+이/VCP 라도/EC 만들/VV+어/EC 주/VX+지/EC 않/VX+으면/EC 일/NNG+이/JKS 없/VA+어/EF ./SF 섬/NNG+이/VCP 라서/EC 어디/NP 다른/MM 데/NNB 나가/VV+서/EC 일/NNG+하/XSV+ᆯ/ETM 수/NNB+도/JX 없/VA+고/EC ./SW "/SW 가난/NNG+에/JKB 익숙/XR+하/XSA+아/EC+지/VX+ᆫ/ETM 연평/NNG+도/NNG 사람/NNG+들/XSN+은/JX '/SW 정당/NNG '/SW 과/JC '/SW 은혜/NNG '/SW 이/VCP+라는/ETM 말/NNG+을/JKO 즐기/VV+어/EC 쓰/VV+었/EP+다/EF ./SF""".stripMargin
    .split("\n").map(w => Sentence(w.split(" ").zipWithIndex.map{
      case (w, i) =>
        val morphemes = w.split("\\+").zipWithIndex.map{
          case (m, mid) =>
            val Array(surf, tag) = m.split("/")
            Morpheme(mid, surf, tag, POS withName tag)
        }
        Word(i, morphemes.map(_.surface).mkString, morphemes)
    })).toSeq

  val sent2 =
    """집/NNG 앞/NNG+에서/JKB 고추/NNG+를/JKO 말리/VV+던/ETM 이숙희/NNP (/SS 가명/NNG ·/SP 75/SN )/SS 할머니/NNG+의/JKG 얼굴/NNG+에/JKB+는/JX 웃음/NNG+기/NNG+가/JKS 없/VA+었/EP+다/EF ./SF
      |"/SW 나라/NNG+가/JKS 취로/NNG 사업/NNG+이/VCP 라도/EC 만들/VV+어/EC 주/VX+지/EC 않/VX+으면/EC 일/NNG+이/JKS 없/VA+어/EF ./SF 섬/NNG+이/VCP 라서/EC 어디/NP 다른/MM 데/NNB 나가/VV+서/EC 일/NNG+하/XSV+ᆯ/ETM 수/NNB+도/JX 없/VA+고/EC ./SW "/SW+라고/JKB 하/VV+시/EP+었/EP+다/EF ./SF
      |가난/NNG+에/JKB 익숙/XR+하/XSA+아/EC+지/VX+ᆫ/ETM 연평/NNG+도/NNG 사람/NNG+들/XSN+은/JX '/SW 정당/NNG '/SW 과/JC '/SW 은혜/NNG '/SW 이/VCP+라는/ETM 말/NNG+을/JKO 즐기/VV+어/EC 쓰/VV+었/EP+다/EF ./SF""".stripMargin
    .split("\n").map(w => Sentence(w.split(" ").zipWithIndex.map{
      case (w, i) =>
        val morphemes = w.split("\\+").zipWithIndex.map{
          case (m, mid) =>
            val Array(surf, tag) = m.split("/")
            Morpheme(mid, surf, tag, POS withName tag)
        }
        Word(i, morphemes.map(_.surface).mkString, morphemes)
    })).toSeq

  "SentenceSplitter" should {
    "handle empty sentence" in {
      val sent = SentenceSplitter(Sentence(Seq.empty))
      sent must beEmpty
    }

    "split sentences" in {
      val split1 = SentenceSplitter(sent1.flatten)
      split1 must haveLength(2)
      split1 mustEqual sent1

      val split2 = SentenceSplitter(sent2.flatten)
      split2 must haveLength(3)
      split2 mustEqual sent2
    }
  }
}
