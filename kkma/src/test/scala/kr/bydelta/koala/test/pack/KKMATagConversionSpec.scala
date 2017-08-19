package kr.bydelta.koala.test.pack

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.kkma._

/**
  * Created by bydelta on 17. 8. 19.
  */
class KKMATagConversionSpec extends TagConversionSpec {
  val tagMap =
    Map(
      "NNG" -> "NNG",
      "NNP" -> "NNP",
      "NNB" -> "NNB",
      "NNM" -> "NNM",
      "NR" -> "NR",
      "NP" -> "NP",
      "VV" -> "VV",
      "VA" -> "VA",
      "VX" -> "VXV\n<VXA",
      "VCP" -> "VCP",
      "VCN" -> "VCN",
      "MM" -> "MDT\n<MDN",
      "MAG" -> "MAG",
      "MAJ" -> "MAC",
      "IC" -> "IC",
      "JKS" -> "JKS",
      "JKC" -> "JKC",
      "JKG" -> "JKG",
      "JKO" -> "JKO",
      "JKB" -> "JKM",
      "JKV" -> "JKI",
      "JKQ" -> "JKQ",
      "JC" -> "JC",
      "JX" -> "JX",
      "EP" -> "<EPH\nEPT\n<EPP",
      "EF" -> "EFN\n<EFQ\n<EFO\n<EFA\n<EFI\n<EFR",
      "EC" -> "ECE\n<ECD\n<ECS",
      "ETN" -> "ETN",
      "ETM" -> "ETD",
      "XPN" -> "XPN",
      "XPV" -> "XPV",
      "XSN" -> "XSN",
      "XSV" -> "XSV",
      "XSA" -> "XSA",
      "XSM" -> "XSM",
      "XSO" -> "XSO",
      "XR" -> "XR",
      "SF" -> "SF",
      "SP" -> "SP",
      "SS" -> "SS",
      "SE" -> "SE",
      "SY" -> "<SO\nSW",
      "UN" -> "UN",
      "UV" -> "UV",
      "UE" -> "UE",
      "SL" -> "OL\n<OH",
      "SN" -> "ON"
    )

  override def from(x: String) = fromKKMATag(x)

  override def to(x: POSTag): String = tagToKKMA(x)
}
