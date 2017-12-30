package kr.bydelta.koala.test.pack

import kr.bydelta.koala.POS._
import kr.bydelta.koala.hnn._

/**
  * Created by bydelta on 17. 8. 19.
  */
class HannanumTagConversionSpec extends TagConversionSpec {

  protected override def tagMap: PartialFunction[POSTag, Seq[Conversion]] = {
    case NNG =>
      Seq(Conversion("ncpa", toTagger = false), Conversion("ncps", toTagger = false),
        Conversion("ncn"), Conversion("ncr", toTagger = false))
    case NNP =>
      Seq(Conversion("nqpa", toTagger = false), Conversion("nqpb", toTagger = false),
        Conversion("nqpc", toTagger = false), Conversion("nqq"))
    case NNB => Seq(Conversion("nbn"), Conversion("nbs", toTagger = false))
    case NNM => Seq(Conversion("nbu"))
    case NR => Seq(Conversion("nnc"), Conversion("nno", toTagger = false))
    case NP => Seq(Conversion("npd"), Conversion("npp", toTagger = false))
    case VV => Seq(Conversion("pvg"), Conversion("pvd", toTagger = false))
    case VA => Seq(Conversion("paa"), Conversion("pad", toTagger = false))
    case VX => Seq(Conversion("px"))
    case VCP => Seq(Conversion("jp"))
    case VCN => Seq(Conversion("paa", toSejong = false))
    case MM => Seq(Conversion("mma"), Conversion("mmd", toTagger = false))
    case MAG => Seq(Conversion("mag"), Conversion("mad", toTagger = false))
    case IC => Seq(Conversion("ii"))
    case JKS => Seq(Conversion("jcs"))
    case JKC => Seq(Conversion("jcc"))
    case JKG => Seq(Conversion("jcm"))
    case JKO => Seq(Conversion("jco"))
    case JKB => Seq(Conversion("jca"))
    case JKV => Seq(Conversion("jcv"))
    case JKQ => Seq(Conversion("jcr"))
    case JC => Seq(Conversion("jct"), Conversion("jcj", toTagger = false))
    case JX => Seq(Conversion("jxf"), Conversion("jxc", toTagger = false))
    case EC =>
      Seq(Conversion("ecc"), Conversion("ecs", toTagger = false), Conversion("ecx", toTagger = false))
    case XPN => Seq(Conversion("xp"))
    case XPV => Seq(Conversion("xp", toSejong = false))
    case XSN =>
      Seq(Conversion("xsnx"), Conversion("xsnu", toTagger = false),
        Conversion("xsna", toTagger = false), Conversion("xsnca", toTagger = false),
        Conversion("xsncc", toTagger = false), Conversion("xsns", toTagger = false),
        Conversion("xsnp", toTagger = false))
    case XSV =>
      Seq(Conversion("xsvn"), Conversion("xsvv", toTagger = false), Conversion("xsva", toTagger = false))
    case XSA =>
      Seq(Conversion("xsmn"), Conversion("xsms", toTagger = false))
    case XSM =>
      Seq(Conversion("xsas"), Conversion("xsam", toTagger = false))
    case XSO | XR | NF | NV | NA => Seq.empty
    case SS => Seq(Conversion("sl"), Conversion("sr", toTagger = false))
    case SO => Seq(Conversion("sd"))
    case SW => Seq(Conversion("sy"), Conversion("su", toTagger = false))
    case SL => Seq(Conversion("f"))
    case SH => Seq(Conversion("f", toSejong = false))
    case SN => Seq(Conversion("nnc", toSejong = false))
    case x => Seq(Conversion(x.toString.toLowerCase))
  }

  override def from(x: String): POSTag = toSejongPOS(x)

  override def to(x: POSTag): String = fromSejongPOS(x).toLowerCase
}
