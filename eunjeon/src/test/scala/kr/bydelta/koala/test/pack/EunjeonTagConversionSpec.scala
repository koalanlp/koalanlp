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
      "SW" -> "SY",
      "SO" -> ">SY",
      "NF" -> ">UNKNOWN",
      "NV" -> ">UNKNOWN",
      "NA" -> "UNKNOWN",
      "SL" -> "SL",
      "SH" -> "SH,"
      "SN" -> "SN"
    )

  override def from(x: String) = fromEunjeonTag(x)

  override def to(x: POSTag): String = tagToEunjeon(x)
}
