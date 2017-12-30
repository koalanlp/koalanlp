package kr.bydelta.koala

/**
  * Package for OpenKoreanText tagger
  *
  * @see [[kr.bydelta.koala.twt.SentenceSplitter]] for segmenting sentences
  * @see [[kr.bydelta.koala.twt.Tagger]] for POS tagging
  * @see [[kr.bydelta.koala.twt.Dictionary]] for using user-defined dictionary.
  * @note Dependencies: OpenKoreanText v2.1.2 (For Scala 2.12+) or TwitterKoreanProcessor v4.4.4 (For Scala 2.11)
  */
package object twt {
  /**
    * Convert tag: from Sejong (Standard) POS tag, to OpenKoreanText's POS tag
    *
    * @usecase {{{fromSejong(POS.NNP)}}} will return "ProperNoun"
    * @see [[https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s Conversion Table (Korean)]]
    * @param tag The POS Tag value to be converted (POSTag type)
    * @return The name of POS tag which is used in OpenKoreanText (String)
    */
  def fromSejongPOS(tag: POS.Value): String = {
    tag match {
      case POS.NNG | POS.NNB |
           POS.NNM | POS.NP => "Noun"
      case POS.NNP => "ProperNoun"
      case POS.NR | POS.SN => "Number"
      case POS.VV | POS.VX |
           POS.VCP | POS.VCN => "Verb"
      case POS.VA => "Adjective"
      case POS.MM =>
        // Before Scala 2.11, OpenKoreanText(TwitterKorean) used different name for determiner.
        if (scala.util.Properties.versionNumberString.startsWith("2.11"))
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
           POS.SE | POS.SO | POS.SW | POS.XR => "Others"
      case POS.NF | POS.NV | POS.NA => "Unknown"
      case POS.SL | POS.SH => "Foreign"
    }
  }

  /**
    * Convert tag: from OpenKoreanText's POS tag, to Sejong (Standard) POS tag
    *
    * @usecase {{{toSejong("Noun")}}} will return POS.NNG
    * @see [[https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s Conversion Table (Korean)]]
    * @param tag The name of POS Tag to be converted (String)
    * @return The POS Tag value which is the most suitable (POSTag type)
    */
  def toSejongPOS(tag: String): POS.Value = {
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
      case "Unknown" => POS.NA
      case "Foreign" | "Alpha" => POS.SL
      case _ => POS.SW
    }
  }
}
