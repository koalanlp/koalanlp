package kr.bydelta.koala

/**
  * 꼬꼬마 분석기 패키지입니다.
  */
package object kkma {

  /**
    * 통합 의존구문분석표기로 변경.
    *
    * @param tag 통합표기로 변경할 의존구문분석 결과.
    * @return 통합표기.
    */
  def KKMAdepTag(tag: String): FunctionalTag.Value = {
    tag match {
      case "목적어" | "(주어,목적)대상" => FunctionalTag.Object
      case "주어" => FunctionalTag.Subject
      case "부사어" | "이유" => FunctionalTag.Adjunct
      case "보어" | "인용" => FunctionalTag.Complement
      case "수식" => FunctionalTag.Modifier
      case "동일" | "보조 연결" | "의존 연결" | "대등 연결" | "체언 연결" | "연결" => FunctionalTag.Conjunctive
      case _ =>
        FunctionalTag.Undefined
    }
  }


  /**
    * 원본품사로 변환.
    *
    * @param tag 원본품사로 변환할 통합표기.
    * @return 변환된 품사.
    */
  def fromSejongPOS(tag: _root_.kr.bydelta.koala.POS.Value): String = {
    tag match {
      case POS.VX => "VXV"
      case POS.MM => "MDT"
      case POS.MAJ => "MAC"
      case POS.JKB => "JKM"
      case POS.JKV => "JKI"
      case POS.EP => "EPT"
      case POS.EF => "EFN"
      case POS.EC => "ECE"
      case POS.ETM => "ETD"
      case POS.SL => "OL"
      case POS.SH => "OH"
      case POS.SN => "ON"
      case POS.NF => "UN"
      case POS.NV => "UV"
      case POS.NA => "UE"
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
      case "VXV" | "VXA" => POS.VX
      case "MDT" | "MDN" => POS.MM
      case "MAC" => POS.MAJ
      case "JKM" => POS.JKB
      case "JKI" => POS.JKV
      case x if x startsWith "EP" => POS.EP
      case x if x startsWith "EF" => POS.EF
      case x if x startsWith "EC" => POS.EC
      case "ETD" => POS.ETM
      case "OL" => POS.SL
      case "OH" => POS.SH
      case "ON" => POS.SN
      case "UN" => POS.NF
      case "UV" => POS.NV
      case "UE" => POS.NA
      case "EMO" => POS.SW //Emoticons
      case x => POS withName x
    }
  }
}
