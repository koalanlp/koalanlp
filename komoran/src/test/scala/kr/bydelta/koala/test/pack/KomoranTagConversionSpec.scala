package kr.bydelta.koala.test.pack

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.kmr._

/**
  * Created by bydelta on 17. 8. 19.
  */
class KomoranTagConversionSpec extends TagConversionSpec {
  val tagMap =
    Map(
      //통합->꼬꼬마,한나눔,은전,트위터,코모란
      "NNG" -> "NNG",
      "NNP" -> "NNP",
      "NNB" -> "NNB",
      "NNM" -> ">NNB",
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
      "SP" -> "SP",
      "SS" -> "SS",
      "SE" -> "SE",
      "SO" -> "SO",
      "SW" -> "SW",
      "NF" -> ">NA",
      "NV" -> ">NA",
      "NA" -> "NA",
      "SL" -> "SL",
      "SH" -> "SH",
      "SN" -> "SN"
    )

  override def from(x: String) = toSejongPOS(x)

  override def to(x: POSTag): String = fromSejongPOS(x)
}
