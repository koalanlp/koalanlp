package kr.bydelta.koala.test.pack

import kr.bydelta.koala.POS._
import kr.bydelta.koala.kkma._

/**
  * Created by bydelta on 17. 8. 19.
  */
class KKMATagConversionSpec extends TagConversionSpec {

  protected override def tagMap: PartialFunction[POSTag, Seq[Conversion]] = {
    case VX => Seq(Conversion("VXV"), Conversion("VXA", toTagger = false))
    case MM => Seq(Conversion("MDT"), Conversion("MDN", toTagger = false))
    case MAJ => Seq(Conversion("MAC"))
    case JKB => Seq(Conversion("JKM"))
    case JKV => Seq(Conversion("JKI"))
    case EP =>
      Seq(Conversion("EPT"), Conversion("EPH", toTagger = false), Conversion("EPP", toTagger = false))
    case EF =>
      Seq(Conversion("EFN"), Conversion("EFQ", toTagger = false), Conversion("EFO", toTagger = false),
        Conversion("EFA", toTagger = false), Conversion("EFI", toTagger = false), Conversion("EFR", toTagger = false))
    case EC =>
      Seq(Conversion("ECE"), Conversion("ECD", toTagger = false), Conversion("ECS", toTagger = false))
    case ETM => Seq(Conversion("ETD"))
    case SL => Seq(Conversion("OL"))
    case SH => Seq(Conversion("OH"))
    case SN => Seq(Conversion("ON"))
    case NF => Seq(Conversion("UN"))
    case NV => Seq(Conversion("UV"))
    case NA => Seq(Conversion("UE"))
    case x => Seq(Conversion(x.toString))
  }

  override def from(x: String): POSTag = toSejongPOS(x)

  override def to(x: POSTag): String = fromSejongPOS(x)
}
