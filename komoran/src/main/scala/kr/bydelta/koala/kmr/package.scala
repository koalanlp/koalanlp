package kr.bydelta.koala

/**
  * 코모란 분석기 패키지입니다.
  */
package object kmr {
  /**
    * 원본품사로 변환.
    *
    * @param tag 원본품사로 변환할 통합표기.
    * @return 변환된 품사.
    */
  def fromSejongPOS(tag: _root_.kr.bydelta.koala.POS.Value): String = {
    tag match {
      case POS.NNM => "NNB"
      case POS.XSM | POS.XSO => "XSN"
      case POS.XPV => "XR"
      case POS.NF | POS.NV => "NA"
      case _ => tag.toString
    }
  }

  /**
    * 통합품사로 변환.
    *
    * @param tag 통합품사로 변환할 원본표기.
    * @return 변환된 통합품사.
    */
  def toSejongPOS(tag: String): POS.Value = {
    POS withName tag.toUpperCase
  }
}
