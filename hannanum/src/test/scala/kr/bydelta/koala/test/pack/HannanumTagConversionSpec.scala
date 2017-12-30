package kr.bydelta.koala.test.pack

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.hnn._

/**
  * Created by bydelta on 17. 8. 19.
  */
class HannanumTagConversionSpec extends TagConversionSpec {
  val tagMap =
    Map(
      "NNG" -> "<NCPA\n<NCPS\nNCN\n<NCR",
      "NNP" -> "<NQPA\n<NQPB\n<NQPC\nNQQ",
      "NNB" -> "NBN\n<NBS",
      "NNM" -> "NBU",
      "NR" -> "NNC\n<NNO",
      "NP" -> "<NPP\nNPD",
      "VV" -> "<PVD\nPVG",
      "VA" -> "<PAD\nPAA",
      "VX" -> "PX",
      "VCP" -> "JP",
      "VCN" -> ">PAA",
      "MM" -> "<MMD\nMMA",
      "MAG" -> "<MAD\nMAG",
      "MAJ" -> "MAJ",
      "IC" -> "II",
      "JKS" -> "JCS",
      "JKC" -> "JCC",
      "JKG" -> "JCM",
      "JKO" -> "JCO",
      "JKB" -> "JCA",
      "JKV" -> "JCV",
      "JKQ" -> "JCR",
      "JC" -> "JCT\n<JCJ",
      "JX" -> "<JXC\nJXF",
      "EP" -> "EP",
      "EF" -> "EF",
      "EC" -> "ECC\n<ECS\n<ECX",
      "ETN" -> "ETN",
      "ETM" -> "ETM",
      "XPN" -> "<XP\n<XPN",
      "XPV" -> "<XPV",
      "XSN" -> "<XSNU\n<XSNA\n<XSNCA\n<XSNCC\n<XSNS\n<XSNP\nXSNX",
      "XSV" -> "<XSVV\n<XSVA\nXSVN",
      "XSA" -> "<XSMS\nXSMN",
      "XSM" -> "<XSAM\nXSAS",
      "XSO" -> "",
      "XR" -> "",
      "SF" -> "SF",
      "SP" -> "SP",
      "SS" -> "SL\n<SR",
      "SE" -> "SE",
      "SO" -> "SD",
      "SW" -> "<SU\nSY",
      "UN" -> "",
      "UV" -> "",
      "UE" -> "",
      "SL" -> "F",
      "SH" -> ">F",
      "SN" -> ">NNC"
    )

  override def from(x: String) = fromHNNTag(x)

  override def to(x: POSTag): String = tagToHNN(x).toUpperCase
}
