package kr.bydelta.koala

/**
  * Created by bydelta on 17. 8. 19.
  */
package object twt {
  /**
    * 원본품사로 변환.
    *
    * @param tag 원본품사로 변환할 통합표기.
    * @return 변환된 품사.
    */
  def tagToTwt(tag: POS.Value): String = {
    tag match {
      case POS.NNG | POS.NNB |
           POS.NNM | POS.NP => "Noun"
      case POS.NNP => "ProperNoun"
      case POS.NR | POS.SN => "Number"
      case POS.VV | POS.VX |
           POS.VCP | POS.VCN => "Verb"
      case POS.VA => "Adjective"
      case POS.MM =>
        if (scala.util.Properties.versionString.startsWith("2.11"))
          "Determiner"
        else
          "Modifier"
      case POS.MAG | POS.MAJ => "Adverb"
      case POS.IC => "Exclamation"
      case POS.JKB | POS.JKC |
           POS.JKG | POS.JKO |
           POS.JKQ | POS.JKS |
           POS.JKV | POS.JX => "Josa"
      case POS.JC => "Conjunction"
      case POS.EP => "PreEomi"
      case POS.EF | POS.EC |
           POS.ETM | POS.ETN => "Eomi"
      case POS.XPN => "Unknown"
      case POS.XPV => "VerbPrefix"
      case POS.XSA | POS.XSM |
           POS.XSN | POS.XSO | POS.XSV => "Suffix"
      case POS.SF => "Punctuation"
      case POS.SS | POS.SP |
           POS.SE | POS.SY | POS.XR => "Others"
      case POS.UE | POS.UN | POS.UV => "Unknown"
      case POS.SL => "Foreign"
    }
  }

  /**
    * 통합품사로 변환.
    *
    * @param tag 통합품사로 변환할 원본표기.
    * @return 변환된 통합품사.
    */
  def fromTwtTag(tag: String): POS.Value = {
    tag match {
      case "Noun" => POS.NNG
      case "ProperNoun" => POS.NNP
      case "Number" => POS.NR
      case "Verb" => POS.VV
      case "Adjective" => POS.VA
      case "Determiner" | "Modifier" => POS.MM
      case "Adverb" => POS.MAG
      case "Exclamation" => POS.IC
      case "Josa" => POS.JX
      case "Conjunction" => POS.JC
      case "PreEomi" => POS.EP
      case "Eomi" => POS.EF
      case "VerbPrefix" => POS.XPV
      case "Suffix" => POS.XSO
      case "Punctuation" => POS.SF
      case "Unknown" => POS.UE
      case "Foreign" | "Alpha" => POS.SL
      case _ => POS.SY
    }
  }
}
