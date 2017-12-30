package kr.bydelta.koala.test.pack

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.twt._

/**
  * Created by bydelta on 17. 8. 19.
  */
class TwitterConversionSpec extends TagConversionSpec {
  val tagMap =
    Map(
      "NNG" -> "Noun",
      "NNP" -> "ProperNoun",
      "NNB" -> ">Noun",
      "NNM" -> ">Noun",
      "NR" -> "Number",
      "NP" -> ">Noun",
      "VV" -> "Verb",
      "VA" -> "Adjective",
      "VX" -> ">Verb",
      "VCP" -> ">Verb",
      "VCN" -> ">Verb",
      "MM" ->
        (if (scala.util.Properties.versionNumberString.startsWith("2.11")) "Determiner"
        else "Modifier\n<Determiner"),
      "MAG" -> "Adverb",
      "MAJ" -> ">Adverb",
      "IC" -> "Exclamation",
      "JKS" -> ">Josa",
      "JKC" -> ">Josa",
      "JKG" -> ">Josa",
      "JKO" -> ">Josa",
      "JKB" -> ">Josa",
      "JKV" -> ">Josa",
      "JKQ" -> ">Josa",
      "JC" -> "Conjunction",
      "JX" -> "Josa",
      "EP" -> "PreEomi",
      "EF" -> "Eomi",
      "EC" -> ">Eomi",
      "ETN" -> ">Eomi",
      "ETM" -> ">Eomi",
      "XPN" -> ">Unknown",
      "XPV" -> "VerbPrefix",
      "XSN" -> ">Suffix",
      "XSV" -> ">Suffix",
      "XSA" -> ">Suffix",
      "XSM" -> ">Suffix",
      "XSO" -> "Suffix",
      "XR" -> "",
      "SF" -> "Punctuation",
      "SP" -> ">Others",
      "SS" -> ">Others",
      "SE" -> ">Others",
      "SO" -> ">Others",
      "SW" -> "<CashTag\nOthers",
      "NF" -> ">Unknown",
      "NV" -> ">Unknown",
      "NA" -> "Unknown",
      "SL" -> "<Alpha\nForeign",
      "SH" -> ">Foreign",
      "SN" -> ">Number"
    )

  override def from(x: String) = toSejongPOS(x)

  override def to(x: POSTag): String = fromSejongPOS(x)
}
