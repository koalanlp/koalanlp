package kr.bydelta.koala

/**
  * Created by bydelta on 17. 8. 19.
  */
package object eunjeon {
  /**
    * 원본품사로 변환.
    *
    * @param tag 원본품사로 변환할 통합표기.
    * @return 변환된 품사.
    */
  def tagToEunjeon(tag: _root_.kr.bydelta.koala.POS.Value): String = {
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
  def fromEunjeonTag(tag: String): POS.Value = {
    tag.toUpperCase match {
      case "NNBC" => POS.NNM
      case "SC" => POS.SP
      case "SSO" | "SSC" => POS.SS
      case "UNKNOWN" => POS.NA
      case x => POS withName x
    }
  }
}
