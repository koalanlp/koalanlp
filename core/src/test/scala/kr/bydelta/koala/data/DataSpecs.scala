package kr.bydelta.koala.data

import kr.bydelta.koala.{POS, Processor}
import org.specs2.execute.Result
import org.specs2.mutable.Specification

/**
  * Created by bydelta on 16. 7. 31.
  */
class DataSpecs extends Specification {
  "Morpheme" should {
    "provide correct equality" in {
      val morph1 = new Morpheme(surface = "밥", rawTag = "NNP", processor = Processor.KKMA)
      val morph2 = new Morpheme(surface = "밥", rawTag = "ncn", processor = Processor.Hannanum)

      morph1 must_!= morph2
      morph1 must_== "밥"
      morph1.isModifier must beFalse
      morph1.isNoun must beTrue
      morph1.hasTag(Seq(POS.NNG, POS.NNB)) must beFalse
      morph1.toString must_== "밥/NNP(NNP)"
      morph1.equalsWithoutTag(morph2) must beTrue
    }
  }

  "Word" should {
    "provide correct equality" in {
      val word = new Word(
        surface = "밥을",
        morphemes = Seq(
          new Morpheme(surface = "밥", rawTag = "NNG", processor = Processor.KKMA),
          new Morpheme(surface = "을", rawTag = "JKO", processor = Processor.KKMA)
        )
      )

      word.iterator.next must_== word.jIterator.next()
      word.iterator.next must_== word.jIterator.next()
      word.iterator.hasNext must_== word.jIterator.hasNext

      word.getPrevOf(word.get(1)).get must_== word.head
      word.getPrevOf(word.get(0)).isDefined must beFalse
      word.getNextOf(word.get(0)).get must_== word.last
      word.getNextOf(word.get(1)).isDefined must beFalse
      word(3).isDefined must beFalse
      word(1).get must_== word.last
      word.size must_== 2

      word.existsMorpheme(Seq(POS.NNP, POS.NNG)) must beTrue
      word.matches(Array("NNG", "JX")) must beFalse
      word.matches(Seq("NNG", "JK")) must beTrue
    }
  }

  "Sentence" should {
    "provide correct equality" in {
      val sent = new Sentence(
        words = Seq(
          new Word(
            surface = "나는",
            morphemes = Seq(
              new Morpheme(surface = "나", rawTag = "NP", processor = Processor.KKMA),
              new Morpheme(surface = "는", rawTag = "JX", processor = Processor.KKMA)
            )
          ),
          new Word(
            surface = "밥을",
            morphemes = Seq(
              new Morpheme(surface = "밥", rawTag = "NNG", processor = Processor.KKMA),
              new Morpheme(surface = "을", rawTag = "JKO", processor = Processor.KKMA)
            )
          )
        )
      )

      sent.concat(sent).size must_== 4
      sent.head.surface must_== "나는"
      sent.last.surface must_== "밥을"
      sent.nouns.map(_.surface) must containAllOf(Seq("나는", "밥을"))
      sent.jVerbs.size() must_== 0
      sent.jModifiers.size() must_== 0
      sent.matches(Seq(Seq("NN", "J"), Seq("N", "J"))) must beFalse
      sent.matches(Array(Array("NP", "J"), Array("N", "JKO"))) must beTrue
      sent.existsMorpheme("X") must beFalse
      sent.filter("J").size must_== 2
      sent.filter(Seq(POS.JX, POS.JKS)).size must_== 1
      sent.filterNot("X").size must_== 2
      sent.filterNot(Seq(POS.JX, POS.JKS)).size must_== 1
    }
  }

  "TagConversion" should {
    "convert tags correctly" in {
      val procOrder = Seq(Processor.KKMA, Processor.Hannanum,
        Processor.Eunjeon, Processor.Twitter, Processor.Komoran)
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
        "XP" -> Seq("XPN\n<XPV", "XP", "XPN", "NounPrefix\n<VerbPrefix", "XPN"),
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
            tags.zip(procOrder).foreach {
              case (taglist, proc) =>
                val list =
                  if (proc == Processor.Hannanum) taglist.toLowerCase
                  else taglist
                if (list.nonEmpty) {
                  list.split("\n").foreach {
                    case tag if tag startsWith "<" =>
                      proc.integratedPOSOf(tag.substring(1)) must_== iPOS
                    case tag if tag startsWith ">" =>
                      proc.originalPOSOf(iPOS) must_== tag.substring(1)
                    case tag =>
                      proc.integratedPOSOf(tag) must_== iPOS
                      proc.originalPOSOf(iPOS) must_== tag
                  }
                }
            }
        }
      }
    }
  }
}
