package kr.bydelta.koala.test.pack

import kr.bydelta.koala.POS._
import kr.bydelta.koala.twt._

/**
  * Created by bydelta on 17. 8. 19.
  */
class TwitterConversionSpec extends TagConversionSpec {

  protected override def tagMap: PartialFunction[POSTag, Seq[Conversion]] = {
    case NNG => Seq(Conversion("Noun"))
    case NNP => Seq(Conversion("ProperNoun"))
    case NNB | NNM | NP => Seq(Conversion("Noun", toSejong = false))
    case NR => Seq(Conversion("Number"))
    case VV => Seq(Conversion("Verb"))
    case VA => Seq(Conversion("Adjective"))
    case VX | VCP | VCN => Seq(Conversion("Verb", toSejong = false))
    case MM =>
      if (scala.util.Properties.versionNumberString.startsWith("2.11"))
        Seq(Conversion("Determiner"))
      else
        Seq(Conversion("Modifier"), Conversion("Determiner", toTagger = false))
    case MAG => Seq(Conversion("Adverb"))
    case MAJ => Seq(Conversion("Adverb", toSejong = false))
    case IC => Seq(Conversion("Exclamation"))
    case JKS | JKC | JKG | JKO | JKB | JKV | JKQ => Seq(Conversion("Josa", toSejong = false))
    case JC => Seq(Conversion("Conjunction"))
    case JX => Seq(Conversion("Josa"))
    case EP => Seq(Conversion("PreEomi"))
    case EF => Seq(Conversion("Eomi"))
    case EC | ETN | ETM => Seq(Conversion("Eomi", toSejong = false))
    case NF | NV | XPN => Seq(Conversion("Unknown", toSejong = false))
    case XPV => Seq(Conversion("VerbPrefix"))
    case XSN | XSV | XSA | XSM => Seq(Conversion("Suffix", toSejong = false))
    case XSO => Seq(Conversion("Suffix"))
    case XR => Seq.empty
    case SF => Seq(Conversion("Punctuation"))
    case SP | SS | SE | SO => Seq(Conversion("Others", toSejong = false))
    case SW => Seq(Conversion("Others"), Conversion("CashTag", toTagger = false))
    case NA => Seq(Conversion("Unknown"))
    case SL => Seq(Conversion("Foreign"), Conversion("Alpha", toTagger = false))
    case SH => Seq(Conversion("Foreign", toSejong = false))
    case SN => Seq(Conversion("Number", toSejong = false))
  }

  override def from(x: String): POSTag = toSejongPOS(x)

  override def to(x: POSTag): String = fromSejongPOS(x)
}
