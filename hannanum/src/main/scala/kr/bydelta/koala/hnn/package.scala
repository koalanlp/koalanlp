package kr.bydelta.koala

/**
  * 한나눔 분석기 패키지입니다.
  */
package object hnn {

  /**
    * 통합 의존구문분석표기로 변경.
    *
    * @param tag 통합표기로 변경할 의존구문분석 결과.
    * @return 통합표기.
    */
  def HNNdepTag(tag: String): FunctionalTag.Value = {
    tag match {
      case "SBJ" => FunctionalTag.Subject
      case "OBJ" => FunctionalTag.Object
      case "CMP" => FunctionalTag.Complement
      case "VMOD" | "NMOD" | "MOD" => FunctionalTag.Modifier
      case "ADV" | "AJT" => FunctionalTag.Adjunct
      case "CNJ" => FunctionalTag.Conjunctive
      case "INT" => FunctionalTag.Interjective
      case "PRN" => FunctionalTag.Parenthetical
      case _ => FunctionalTag.Undefined
    }
  }

  /**
    * 원본품사로 변환.
    *
    * @param tag 원본품사로 변환할 통합표기.
    * @return 변환된 품사.
    */
  def fromSejongPOS(tag: POS.Value): String = {
    (tag match {
      case POS.NNG | POS.NF => "NCN"
      case POS.NNP => "NQQ"
      case POS.NNB => "NBN"
      case POS.NNM => "NBU"
      case POS.NR => "NNC"
      case POS.NP => "NPD"
      case POS.VV | POS.NV => "PVG"
      case POS.VA => "PAA"
      case POS.VX => "PX"
      case POS.VCP => "JP"
      case POS.VCN => "PAA"
      case POS.MM => "MMA"
      case POS.IC => "II"
      case POS.JKS => "JCS"
      case POS.JKC => "JCC"
      case POS.JKG => "JCM"
      case POS.JKO => "JCO"
      case POS.JKB => "JCA"
      case POS.JKV => "JCV"
      case POS.JKQ => "JCR"
      case POS.JC => "JCT"
      case POS.JX => "JXF"
      case POS.EC => "ECC"
      case POS.XSO | POS.XSN => "XSNX"
      case POS.XSV => "XSVN"
      case POS.XSA => "XSMN"
      case POS.XSM => "XSAS"
      case POS.SS => "SL"
      case POS.SL | POS.SH => "F"
      case POS.SN => "NNC"
      case POS.SO => "SD"
      case POS.SW | POS.XR | POS.NA => "SY"
      case POS.XPN | POS.XPV => "XP"
      case x => x.toString
    }).toLowerCase
  }

  /**
    * 통합품사로 변환.
    *
    * @param tag 통합품사로 변환할 원본표기.
    * @return 변환된 통합품사.
    */
  def toSejongPOS(tag: String): POS.Value = {
    tag.toLowerCase match {
      case x if x startsWith "nc" => POS.NNG
      case x if x startsWith "nq" => POS.NNP
      case "nbn" | "nbs" => POS.NNB
      case "nbu" => POS.NNM
      case x if x startsWith "nn" => POS.NR
      case x if x startsWith "np" => POS.NP
      case x if x startsWith "pv" => POS.VV
      case x if x startsWith "pa" => POS.VA
      case "px" => POS.VX
      case "jp" => POS.VCP
      case x if x startsWith "mm" => POS.MM
      case "mad" | "mag" => POS.MAG
      case "maj" => POS.MAJ
      case "ii" => POS.IC
      case "jcs" => POS.JKS
      case "jcc" => POS.JKC
      case "jcm" => POS.JKG
      case "jco" => POS.JKO
      case "jca" => POS.JKB
      case "jcv" => POS.JKV
      case "jcr" => POS.JKQ
      case "jct" | "jcj" => POS.JC
      case "jxc" | "jxf" => POS.JX
      case x if x startsWith "ec" => POS.EC
      case "xp" => POS.XPN
      case x if x startsWith "xsn" => POS.XSN
      case x if x startsWith "xsv" => POS.XSV
      case x if x startsWith "xsm" => POS.XSA
      case x if x startsWith "xsa" => POS.XSM
      case "sl" | "sr" => POS.SS
      case "sd" => POS.SO
      case "su" | "sy" => POS.SW
      case "f" => POS.SL
      case x => POS withName x.toUpperCase
    }
  }
}
