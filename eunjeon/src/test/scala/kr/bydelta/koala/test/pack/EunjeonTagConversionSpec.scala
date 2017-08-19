package kr.bydelta.koala.test.pack

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.eunjeon.{fromEunjeonTag, tagToEunjeon}

/**
  * Created by bydelta on 17. 8. 19.
  */
class EunjeonTagConversionSpec extends TagConversionSpec {
  val tagMap =
    Map(
      "NNG" -> "NNG",
      "NNP" -> "NNP",
      "NNB" -> "NNB",
      "NNM" -> "NNBC",
      "NR" -> "NR",
      "NP" -> "NP",
      "VV" -> "VV",
      "VA" -> "VA",
      "VX" -> "VX",
      "VCP" -> "VCP",
      "VCN" -> "VCN",
      "MM" -> "MM",
      "MAG" -> "MAG",
      "MAJ" -> "MAJ",
      "IC" -> "IC",
      "JKS" -> "JKS",
      "JKC" -> "JKC",
      "JKG" -> "JKG",
      "JKO" -> "JKO",
      "JKB" -> "JKB",
      "JKV" -> "JKV",
      "JKQ" -> "JKQ",
      "JC" -> "JC",
      "JX" -> "JX",
      "EP" -> "EP",
      "EF" -> "EF",
      "EC" -> "EC",
      "ETN" -> "ETN",
      "ETM" -> "ETM",
      "XPN" -> "XPN",
      "XPV" -> ">XR",
      "XSN" -> "XSN",
      "XSV" -> "XSV",
      "XSA" -> "XSA",
      "XSM" -> "",
      "XSO" -> "",
      "XR" -> "XR",
      "SF" -> "SF",
      "SP" -> "SC",
      "SS" -> "SSO\n<SSC",
      "SE" -> "SE",
      "SY" -> "SY",
      "UN" -> ">UNKNOWN",
      "UV" -> ">UNKNOWN",
      "UE" -> "UNKNOWN",
      "SL" -> "SL\n<SH",
      "SN" -> "SN"
    )

  override def from(x: String) = fromEunjeonTag(x)

  override def to(x: POSTag): String = tagToEunjeon(x)

  Map(
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
    "MM" -> Seq("MDT\n<MDN", "<MMD\nMMA", "MM", "Modifier\n<Determiner", "MM"),
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
    "XPN" -> Seq("XPN", "<XP\n<XPN", "XPN", ">Unknown", "XPN"),
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
}
