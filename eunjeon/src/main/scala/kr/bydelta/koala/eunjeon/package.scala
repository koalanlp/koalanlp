package kr.bydelta.koala

/**
  * 은전한닢 분석기 패키지입니다.
  */
package object eunjeon {
  /**
    * 원본품사로 변환.
    *
    * @param tag 원본품사로 변환할 통합표기.
    * @return 변환된 품사.
    */
  def fromSejongPOS(tag: POS.Value): String = {
    tag match {
      case POS.NNM => "NNBC"
      case POS.SS => "SSO"
      case POS.SP => "SC"
      case POS.XPV => "XR"
      case POS.XSM | POS.XSO => "XSN"
      case POS.SW | POS.SO => "SY"
      case POS.NF | POS.NV | POS.NA => "UNKNOWN"
      case x => x.toString
    }
  }

  /**
    * 통합품사로 변환.
    *
    * @param tag 통합품사로 변환할 원본표기.
    * @return 변환된 통합품사.
    */
  def toSejongPOS(tag: String): POS.Value = {
    tag.toUpperCase match {
      case "NNBC" => POS.NNM
      case "SC" => POS.SP
      case "SSO" | "SSC" => POS.SS
      case "SY" => POS.SW
      case "UNKNOWN" => POS.NA
      case x => POS withName x
    }
  }
}
