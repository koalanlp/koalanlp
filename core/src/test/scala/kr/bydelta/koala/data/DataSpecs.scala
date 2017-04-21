package kr.bydelta.koala.data

import kr.bydelta.koala._
import org.specs2.execute.Result
import org.specs2.mutable.Specification

import scala.collection.JavaConverters._
import scala.util.Random

/**
  * Created by bydelta on 16. 7. 31.
  */
class DataSpecs extends Specification {
  "Morpheme" should {
    "provide correct equality" in {
      val morph1 = Morpheme("밥", "NNP", fromKKMATag("NNP"))
      val morph2 = Morpheme("밥", "ncn", fromHNNTag("ncn"))
      val morph3 = Morpheme("밥", "NNG", fromEunjeonTag("NNG"))

      (morph1 == morph2) must beFalse
      (morph2 == morph3) must beTrue
      morph2.hashCode must_== morph3.hashCode
      morph1.isModifier must beFalse
      morph1.isNoun must beTrue

      val morph4: Morpheme = Morpheme("은", "JX", fromEunjeonTag("JX"))
      morph4.isJosa must beTrue
      morph4.isPredicate must beFalse

      morph1.hasTag(Seq(POS.NNG, POS.NNB)) must beFalse
      morph2.hasRawTag("nc") must beTrue

      morph1.toString must_== "밥/NNP(NNP)"
      morph1.equalsWithoutTag(morph2) must beTrue
    }
  }

  "Word" should {
    "provide correct equality" in {
      val word = Word(
        "밥을",
        Seq(
          Morpheme("밥", "NNG", POS.NNG),
          Morpheme("을", "JKO", POS.JKO)
        )
      )

      (word.iterator.next == word.jIterator.next()) must beTrue
      (word.iterator.next == word.jIterator.next()) must beTrue
      word.iterator.hasNext must_== word.jIterator.hasNext

      word(0).surface must_== "밥"
      word.head.id must_== 0
      word.last.surface must_== "을"
      word.last.id must_== 1
      word(3) must throwAn[IndexOutOfBoundsException]
      word.length must_== 2

      word.matches(Array("NNG", "JX")) must beFalse
      word.matches(Seq("NNG", "JK")) must beTrue

      import Implicit._
      val filtered = word.filter(POS.JKO)
      filtered must beAnInstanceOf[Word]

      (word ++ word) must beAnInstanceOf[Word]
    }
  }

  "Sentence" should {
    "provide correct equality" in {
      val sent = Sentence(
        Seq(
          Word(
            "나는",
            Seq(
              Morpheme("나", "NP", POS.NP),
              Morpheme("는", "JX", POS.JX)
            )
          ),
          Word(
            "밥을",
            Seq(
              Morpheme("밥", "NNG", POS.NNG),
              Morpheme("을", "JKO", POS.JKO)
            )
          )
        )
      )

      sent.head.id must_== 0
      sent.last.id must_== 1
      sent(0).surface must_== "나는"
      sent.last.surface must_== "밥을"
      sent.nouns.map(_.surface) must containAllOf(Seq("나는", "밥을"))
      sent.nouns must containTheSameElementsAs(sent.jNouns.asScala)
      sent.jVerbs.size() must_== 0
      sent.jModifiers.size() must_== 0

      (sent canEqual sent.head) must beFalse
      (sent canEqual Vector[Word]()) must beFalse

      sent.matches(Seq(Seq("NN", "J"), Seq("N", "J"))) must beFalse
      sent.matches(Array(Array("NP", "J"), Array("N", "JKO"))) must beTrue

      import Implicit._
      val filtered = sent.filter(Seq(POS.JX, POS.JC))
      filtered must beAnInstanceOf[Sentence]
      filtered.size must_== 1

      val filtered2 = sent.filter(POS.JX)
      filtered2 must beAnInstanceOf[Sentence]
      filtered2 must containTheSameElementsAs(filtered)

      val concated = sent ++ sent
      concated must beAnInstanceOf[Sentence]
    }
  }

  "TagConversion" should {
    "convert tags correctly" in {
      val toLC = (x: String) => x.toLowerCase
      val toUC = (x: String) => x.toUpperCase
      val fromOrder = Seq(fromKKMATag(_), toLC.andThen(fromHNNTag), fromEunjeonTag(_),
        fromTwtTag(_), fromKomoranTag(_))
      val toOrder = Seq(tagToKKMA(_), (tagToHNN _).andThen(toUC), tagToEunjeon(_),
        tagToTwt(_), tagToKomoran(_))
      val tagMap = Map(
        //통합->꼬꼬마,한나눔,은전,트위터,코모란
        "NNG" -> Seq("NNG", "<NCPA\n<NCPS\nNCN\n<NCR", "NNG", "Noun", "NNG"),
        "NNP" -> Seq("NNP", "<NQPA\n<NQPB\n<NQPC\nNQQ", "NNP", "ProperNoun", "NNP"),
        "NNB" -> Seq("NNB", "NBN\n<NBS", "NNB", ">Noun", "NNB"),
        "NNM" -> Seq("NNM", "NBU", "NNBC", ">Noun", ">NNB"),
        "NR" -> Seq("NR", "NNC\n<NNO", "NR", "Number", "NR"),
        "NP" -> Seq("NP", "<NPP\nNPD", "NP", ">Noun", "NP"),
        "VV" -> Seq("VV", "<PVD\nPVG", "VV", "Verb", "VV"),
        "VA" -> Seq("VA", "<PAD\nPAA", "VA", "Adjective", "VA"),
        "VX" -> Seq("VXV\n<VXA", "PX", "VX", ">Verb", "VX"),
        "VCP" -> Seq("VCP", "JP", "VCP", ">Verb", "VCP"),
        "VCN" -> Seq("VCN", ">PAA", "VCN", ">Verb", "VCN"),
        "MM" -> Seq("MDT\n<MDN", "<MMD\nMMA", "MM", "Determiner", "MM"),
        "MAG" -> Seq("MAG", "<MAD\nMAG", "MAG", "Adverb", "MAG"),
        "MAJ" -> Seq("MAC", "MAJ", "MAJ", ">Adverb", "MAJ"),
        "IC" -> Seq("IC", "II", "IC", "Exclamation", "IC"),
        "JKS" -> Seq("JKS", "JCS", "JKS", ">Josa", "JKS"),
        "JKC" -> Seq("JKC", "JCC", "JKC", ">Josa", "JKC"),
        "JKG" -> Seq("JKG", "JCM", "JKG", ">Josa", "JKG"),
        "JKO" -> Seq("JKO", "JCO", "JKO", ">Josa", "JKO"),
        "JKB" -> Seq("JKM", "JCA", "JKB", ">Josa", "JKB"),
        "JKV" -> Seq("JKI", "JCV", "JKV", ">Josa", "JKV"),
        "JKQ" -> Seq("JKQ", "JCR", "JKQ", ">Josa", "JKQ"),
        "JC" -> Seq("JC", "JCT\n<JCJ", "JC", "Conjunction", "JC"),
        "JX" -> Seq("JX", "<JXC\nJXF", "JX", "Josa", "JX"),
        "EP" -> Seq("<EPH\nEPT\n<EPP", "EP", "EP", "PreEomi", "EP"),
        "EF" -> Seq("EFN\n<EFQ\n<EFO\n<EFA\n<EFI\n<EFR", "EF", "EF", "Eomi", "EF"),
        "EC" -> Seq("ECE\n<ECD\n<ECS", "ECC\n<ECS\n<ECX", "EC", ">Eomi", "EC"),
        "ETN" -> Seq("ETN", "ETN", "ETN", ">Eomi", "ETN"),
        "ETM" -> Seq("ETD", "ETM", "ETM", ">Eomi", "ETM"),
        "XPN" -> Seq("XPN", "<XP\n<XPN", "XPN", "NounPrefix", "XPN"),
        "XPV" -> Seq("XPV", "<XPV", ">XR", "VerbPrefix", ">XR"),
        "XSN" -> Seq("XSN", "<XSNU\n<XSNA\n<XSNCA\n<XSNCC\n<XSNS\n<XSNP\nXSNX", "XSN", ">Suffix", "XSN"),
        "XSV" -> Seq("XSV", "<XSVV\n<XSVA\nXSVN", "XSV", ">Suffix", "XSV"),
        "XSA" -> Seq("XSA", "<XSMS\nXSMN", "XSA", ">Suffix", "XSA"),
        "XSM" -> Seq("XSM", "<XSAM\nXSAS", "", ">Suffix", ""),
        "XSO" -> Seq("XSO", "", "", "Suffix", ""),
        "XR" -> Seq("XR", "", "XR", "", "XR"),
        "SF" -> Seq("SF", "SF", "SF", "Punctuation", "SF"),
        "SP" -> Seq("SP", "SP", "SC", ">Others", "SP"),
        "SS" -> Seq("SS", "SL\n<SR", "SSO\n<SSC", ">Others", "SS"),
        "SE" -> Seq("SE", "SE", "SE", ">Others", "SE"),
        "SY" -> Seq("<SO\nSW", "<SD\n<SU\nSY", "SY", "<CashTag\nOthers", "<SO\nSW"),
        "UN" -> Seq("UN", "", ">UNKNOWN", ">Unknown", ">NA"),
        "UV" -> Seq("UV", "", ">UNKNOWN", ">Unknown", ">NA"),
        "UE" -> Seq("UE", "", "UNKNOWN", "Unknown", "NA"),
        "SL" -> Seq("OL\n<OH", "F", "SL\n<SH", "<Alpha\nForeign", "SL\n<SH"),
        "SN" -> Seq("ON", ">NNC", "SN", ">Number", "SN")
      )

      Result.unit {
        tagMap.foreach {
          case (iTag, tags) =>
            val iPOS = POS.withName(iTag)
            tags.zip(fromOrder.zip(toOrder)).foreach {
              case (list, (from, to)) =>
                if (list.nonEmpty) {
                  list.split("\n").foreach {
                    case tag if tag startsWith "<" =>
                      from(tag.substring(1)) must_== iPOS
                    case tag if tag startsWith ">" =>
                      to(iPOS) must_== tag.substring(1)
                    case tag =>
                      from(tag) must_== iPOS
                      to(iPOS) must_== tag
                  }
                }
            }
        }
      }
    }
  }

  "KoreanCharacterExtension" should {
    "give the same result with KoreanStringExtension" in {
      reconstructKorean('한'.getChosungCode, '걱'.getJungsungCode, '물'.getJongsungCode) mustEqual '헐'

      Result.unit {
        (1 to 50).foreach {
          _ =>
            val code = (1 to 4).map(_ => (Random.nextInt(18),
              Random.nextInt(20), Random.nextInt(28)))
            val str = code.map(x => reconstructKorean(x._1, x._2, x._3)).mkString

            str.endsWithJongsung mustEqual str.last.endsWithJongsung
            str.last.endsWithJongsung mustEqual (code.last._3 > 0)

            str.head.getChosungCode mustEqual code.head._1
            str.head.getJungsungCode mustEqual code.head._2
            str.head.getJongsungCode mustEqual code.head._3
        }
      }
    }

    "reduce verb application correctly" in {
      val map =
        Seq(("깨닫", "아", true, "깨달아"),
          ("붇", "어나다", true, "불어나다"),
          ("눋", "어", true, "눌어"),
          ("믿", "어", true, "믿어"),
          ("묻", "어", true, "물어"),
          ("구르", "어", true, "굴러"),
          ("모르", "아", true, "몰라"),
          ("벼르", "어", true, "별러"),
          ("마르", "아", true, "말라"),
          ("무르", "어", true, "물러"),
          ("누르", "어", true, "눌러"),
          ("다르", "아", true, "달라"),
          ("사르", "아", true, "살라"),
          ("바르", "아", true, "발라"),
          ("가르", "아", true, "갈라"),
          ("나르", "아", true, "날라"),
          ("자르", "아", true, "잘라"),
          ("아니꼽", "어", false, "아니꼬워"),
          ("무덥", "어", false, "무더워"),
          ("우습", "어", false, "우스워"),
          ("줍", "어", true, "주워"),
          ("더럽", "어", false, "더러워"),
          ("무섭", "어", false, "무서워"),
          ("귀엽", "어", false, "귀여워"),
          ("안쓰럽", "어", false, "안쓰러워"),
          ("아름답", "어", false, "아름다워"),
          ("잡", "아", true, "잡아"),
          ("뽑", "아", true, "뽑아"),
          ("곱", "아", false, "고와"),
          ("돕", "아", true, "도와"),
          ("뽑", "아", true, "뽑아"),
          ("씹", "어", true, "씹어"),
          ("업", "어", true, "업어"),
          ("입", "어", true, "입어"),
          ("잡", "아", true, "잡아"),
          ("접", "아", true, "접어"),
          ("좁", "아", false, "좁아"),
          ("낫", "아", true, "나아"),
          ("긋", "아", true, "그어"),
          ("벗", "아", true, "벗어"),
          ("솟", "아", true, "솟아"),
          ("씻", "아", true, "씻어"),
          ("뺏", "어", true, "뺏어"),
          ("푸", "어", true, "퍼"),
          ("끄", "아", true, "꺼"),
          ("들", "아", true, "들어"),
          ("가", "아라", true, "가거라"),
          ("삼가", "아라", true, "삼가거라"),
          ("들어가", "아라", true, "들어가거라"),
          ("오", "아라", true, "오너라"),
          ("돌아오", "아라", true, "돌아오너라"),
          ("푸르", "어", false, "푸르러"),
          ("하", "았다", true, "하였다"),
          ("영원하", "아", true, "영원하여"),
          ("파랗", "으면", false, "파라면"),
          ("동그랗", "은", false, "동그란"),
          ("파랗", "았다", false, "파랬다"),
          ("파랗", "을", false, "파랄"),
          ("그렇", "아", false, "그래"),
          ("시퍼렇", "었다", false, "시퍼렜다"),
          ("그렇", "네", false, "그렇네"),
          ("파랗", "네", false, "파랗네"),
          ("노랗", "네", false, "노랗네"),
          ("좋", "아", false, "좋아"),
          ("낳", "아", true, "낳아"))

      Result.unit {
        map.foreach {
          case (verb, rest, isVerb, result) =>
            util.reduceVerbApply(verb.toSeq, isVerb, rest.toSeq).mkString mustEqual result
        }
      }
    }
  }
}
