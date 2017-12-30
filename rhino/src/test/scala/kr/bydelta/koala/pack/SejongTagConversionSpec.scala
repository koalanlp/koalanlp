package kr.bydelta.koala.pack

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.test.pack.TagConversionSpec
import kr.bydelta.koala.{fromSejongTag, tagToSejong}

/**
  * Created by bydelta on 17. 8. 19.
  */
class SejongTagConversionSpec extends TagConversionSpec {
  val tagMap =
    Map(
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
      "XPV" -> "",
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
      "NF" -> "NF",
      "NV" -> "NV",
      "NA" -> "NA",
      "SL" -> "SL",
      "SH" -> "SH",
      "SN" -> "SN"
    )

  override def from(x: String) = fromSejongTag(x)

  override def to(x: POSTag): String = tagToSejong(x)
}
