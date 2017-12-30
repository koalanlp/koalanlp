package kr.bydelta.koala.test.pack

import kr.bydelta.koala.POS._
import kr.bydelta.koala.eunjeon.{toSejongPOS, fromSejongPOS}

/**
  * Created by bydelta on 17. 8. 19.
  */
class EunjeonTagConversionSpec extends TagConversionSpec {

  protected override def tagMap: PartialFunction[POSTag, Seq[Conversion]] = {
    case NNM => Seq(Conversion("NNBC"))
    case XPV => Seq(Conversion("XR", toSejong = false))
    case XSM | XSO => Seq.empty
    case SS => Seq(Conversion("SSO"), Conversion("SSC", toTagger = false))
    case SW => Seq(Conversion("SY"))
    case SO => Seq(Conversion("SY", toSejong = false))
    case NF | NV => Seq(Conversion("UNKNOWN", toSejong = false))
    case NA => Seq(Conversion("UNKNOWN"))
    case SP => Seq(Conversion("SC"))
    case x => Seq(Conversion(x.toString))
  }

  override def from(x: String): POSTag = toSejongPOS(x)

  override def to(x: POSTag): String = fromSejongPOS(x)
}
