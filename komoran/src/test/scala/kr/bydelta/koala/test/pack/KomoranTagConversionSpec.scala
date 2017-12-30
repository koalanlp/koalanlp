package kr.bydelta.koala.test.pack

import kr.bydelta.koala.POS._
import kr.bydelta.koala.kmr._

/**
  * Created by bydelta on 17. 8. 19.
  */
class KomoranTagConversionSpec extends TagConversionSpec {

  protected override def tagMap: PartialFunction[POSTag, Seq[Conversion]] = {
    case NNM => Seq(Conversion("NNB", toSejong = false))
    case XPV => Seq(Conversion("XR", toSejong = false))
    case XSM | XSO => Seq.empty
    case NF | NV => Seq(Conversion("NA", toSejong = false))
    case x => Seq(Conversion(x.toString))
  }

  override def from(x: String): POSTag = toSejongPOS(x)

  override def to(x: POSTag): String = fromSejongPOS(x)
}
