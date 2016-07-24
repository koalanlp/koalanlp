package kr.bydelta

/**
  * Created by bydelta on 16. 7. 20.
  */
package object koala {

  implicit class TagConversion(val proc: Processor.Value) {
    def integratedPOSOf(tag: String): POS.Value =
      proc match {
        case Processor.KKMA =>
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
            case "XPN" | "XPV" => POS.XP
            case "SO" | "SW" => POS.SY
            case "OL" | "OH" => POS.SL
            case "ON" => POS.SN
            case x => POS withName x
          }
        case Processor.Hannanum =>
          tag match {
            case x if x startsWith "nc" => POS.NNG
            case x if x startsWith "nq" => POS.NNP
            case "nbn" | "nbs" => POS.NNB
            case "nbu" => POS.NNM
            case x if x startsWith "nn" => POS.NR
            case x if x startsWith "np" => POS.NP
            case x if x startsWith "pv" => POS.VV
            case x if x startsWith "pa" => POS.VA
            case "px" => POS.VX
            case "jp" => POS.VCP
            case x if x startsWith "mm" => POS.MM
            case "mad" | "mag" => POS.MAG
            case "maj" => POS.MAJ
            case "ii" => POS.IC
            case "jcs" => POS.JKS
            case "jcc" => POS.JKC
            case "jcm" => POS.JKG
            case "jco" => POS.JKO
            case "jca" => POS.JKB
            case "jcv" => POS.JKV
            case "jcr" => POS.JKQ
            case "jct" | "jcj" => POS.JC
            case "jxc" | "jxf" => POS.JX
            case x if x startsWith "ec" => POS.EC
            case x if x startsWith "xsn" => POS.XSN
            case x if x startsWith "xsv" => POS.XSV
            case x if x startsWith "xsm" => POS.XSA
            case x if x startsWith "xsa" => POS.XSM
            case "sl" | "sr" => POS.SS
            case "sd" | "su" | "sy" => POS.SY
            case "f" => POS.SL
            case x => POS withName x.toUpperCase
          }
        case Processor.Eunjeon =>
          tag.toUpperCase match {
            case "NNBC" => POS.NNM
            case "XPN" => POS.XP
            case "SC" => POS.SP
            case "SSO" | "SSC" => POS.SS
            case "SL" | "SH" => POS.SL
            case x => POS withName x
          }

        case Processor.Twitter =>
          tag match {
            case "Noun" => POS.NNG
            case "ProperNoun" => POS.NNP
            case "Number" => POS.NR
            case "Verb" => POS.VV
            case "Adjective" => POS.VA
            case "Determiner" => POS.MM
            case "Adverb" => POS.MAG
            case "Exclamation" => POS.IC
            case "Josa" => POS.JX
            case "Conjunction" => POS.JC
            case "PreEomi" => POS.EP
            case "Eomi" => POS.EF
            case "NounPrefix" | "VerbPrefix" => POS.XP
            case "Suffix" => POS.XSO
            case "Punctuation" => POS.SF
            case "Unknown" => POS.UE
            case "Foreign" => POS.SL
            case _ => POS.SY
          }
        case Processor.Komoran =>
          tag match {
            case "XPN" => POS.XP
            case "SW" | "SO" => POS.SY
            case "NA" => POS.UE
            case "SL" | "SH" => POS.SL
            case _ => POS withName tag
          }
      }

    def originalPOSOf(tag: POS.Value): String =
      proc match {
        case Processor.KKMA =>
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
            case POS.XP => "XPN"
            case POS.SY => "SW"
            case POS.SL => "OL"
            case POS.SN => "ON"
            case x => x.toString
          }
        case Processor.Hannanum =>
          (tag match {
            case POS.NNG | POS.UN => "NCN"
            case POS.NNP => "NQQ"
            case POS.NNB => "NBN"
            case POS.NNM => "NBU"
            case POS.NR => "NNC"
            case POS.NP => "NPD"
            case POS.VV | POS.UV => "PVG"
            case POS.VA => "PAA"
            case POS.VX => "PX"
            case POS.VCP => "JP"
            case POS.VCN => "PAA"
            case POS.MM => "MMA"
            case POS.IC => "II"
            case POS.JKS => "JCS"
            case POS.JKC => "JCC"
            case POS.JKG => "JCM"
            case POS.JKO => "JCO"
            case POS.JKB => "JCA"
            case POS.JKV => "JCV"
            case POS.JKQ => "JCR"
            case POS.JC => "JCT"
            case POS.JX => "JXF"
            case POS.EC => "ECC"
            case POS.XSO | POS.XSN => "XSNX"
            case POS.XSV => "XSVN"
            case POS.XSA => "XSMN"
            case POS.XSM => "XSAS"
            case POS.SS => "SL"
            case POS.SL => "F"
            case POS.SN => "NNC"
            case POS.SY | POS.XR | POS.UE => "SW"
            case x => x.toString
          }).toLowerCase
        case Processor.Eunjeon =>
          tag match {
            case POS.NNM => "NNBC"
            case POS.SS => "SSO"
            case POS.SP => "SC"
            case POS.XP => "XPN"
            case POS.XSM | POS.XSO => "XSN"
            case POS.UN | POS.UE => "NNG"
            case POS.UV => "VV"
            case x => x.toString
          }
        case Processor.Twitter =>
          tag match {
            case POS.NNG | POS.NNB |
                 POS.NNM | POS.NP => "Noun"
            case POS.NNP => "ProperNoun"
            case POS.NR | POS.SN => "Number"
            case POS.VV | POS.VX |
                 POS.VCP | POS.VCN => "Verb"
            case POS.VA => "Adjective"
            case POS.MM => "Determiner"
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
            case POS.XP => "NounPrefix"
            case POS.XSA | POS.XSM |
                 POS.XSN | POS.XSO | POS.XSV => "Suffix"
            case POS.SF => "Punctuation"
            case POS.SS | POS.SP |
                 POS.SE | POS.SY | POS.XR => "Others"
            case POS.UE | POS.UN | POS.UV => "Unknown"
            case POS.SL => "Foreign"
          }
        case Processor.Komoran =>
          tag match {
            case POS.NNM => "NNB"
            case POS.XP => "XPN"
            case POS.XSM | POS.XSO => "XSN"
            case POS.SY => "SW"
            case POS.UN | POS.UV | POS.UE => "NA"
            case _ => tag.toString
          }
      }

    def dependencyOf(tag: String): FunctionalTag.Value =
      proc match {
        case Processor.Hannanum =>
          tag match {
            case "SBJ" => FunctionalTag.Subject
            case "OBJ" => FunctionalTag.Object
            case "CMP" => FunctionalTag.Complement
            case "VMOD" | "NMOD" | "MOD" => FunctionalTag.Modifier
            case "ADV" | "AJT" => FunctionalTag.Adjunct
            case "CNJ" => FunctionalTag.Conjunctive
            case "INT" => FunctionalTag.Interjective
            case "PRN" => FunctionalTag.Parenthetical
            case _ => FunctionalTag.Undefined
          }
        case Processor.KKMA =>
          tag match {
            case "목적어" => FunctionalTag.Object
            case "주어" => FunctionalTag.Subject
            case "부사어" => FunctionalTag.Adjunct
            case "보어" | "인용" => FunctionalTag.Complement
            case "수식" | "명사구" | "이유" => FunctionalTag.Modifier
            case "동일" => FunctionalTag.Conjunctive
            case _ =>
              if (tag.contains("연결"))
                FunctionalTag.Conjunctive
              else if (tag.contains("대상"))
                FunctionalTag.Object
              else
                FunctionalTag.Conjunctive
          }
        case _ =>
          throw new UnsupportedOperationException("의존구문분석은 KKMA와 Hannanum Processor만 제공합니다.")
      }
  }

  object POS extends Enumeration {
    type POSTag = Value

    /** 일반명사, 고유명사, 일반 의존명사, 단위성 의존명사, 수사, 대명사 **/
    val NNG, NNP, NNB, NNM, NR, NP,

    /** 동사, 형용사, 보조용언, 긍정지정사(이다), 부정지정사(아니다) **/
    VV, VA, VX, VCP, VCN,

    /** 관형사, 부사, 접속부사 **/
    MM, MAG, MAJ,

    /** 감탄사 **/
    IC,

    /** 주격 조사, 보격 조사, 관형격 조사, 목적격 조사, 부사격 조사, 호격 조사, 인용격 조사, 접속 조사, 보조사 **/
    JKS, JKC, JKG, JKO, JKB, JKV, JKQ, JC, JX,

    /** 선어말 어미, 종결 어미, 연결 어미, 명사형 전성어미, 관형형 전성 어미 **/
    EP, EF, EC, ETN, ETM,

    /** 접두사, 명사 파생 접미사, 동사 파생 접미사, 형용사 파생 접미사, 부사 파생 접미사, 기타 접미사, 어근 **/
    XP, XSN, XSV, XSA, XSM, XSO, XR,

    /** 마침/물음/느낌표, 쉼표/가운뎃점/빗금, 괄호/묶음표, 줄임표, 기타기호 **/
    SF, SP, SS, SE, SY,

    /** 명사 추정 범주, 동사 추정 범주, 분석 불능 범주 **/
    UN, UV, UE,

    /** 외국어/한자, 숫자 **/
    SL, SN = Value


  }

  object Processor extends Enumeration {
    type Processor = Value
    val Hannanum, KKMA, Twitter, Eunjeon, Komoran = Value
  }

  object HannanumTextAddon extends Enumeration {
    type HannanumTextAddon = Value
    val SentenceSegment, InformalSentenceFilter = Value
  }

  object FunctionalTag extends Enumeration {
    type FunctionalTag = Value
    /** 주어. 주격 체언구(NP_SBJ), 명사 전성 용언구(VP_SBJ), 명사절(S_SBJ) */
    val Subject,

    /** 목적어. 목적격 체언구(NP_OBJ), 명사 전성 용언구(VP_OBJ), 명사절(S_OBJ) */
    Object,

    /** 보어. 보격 체언구(NP_CMP), 명사 전성 용언구(VP_CMP), 인용절(S_CMP) */
    Complement,

    /** 체언 수식어(관형격). 관형격 체언구(NP_MOD), 관형형 용언구(VP_MOD), 관형절(S_MOD) */
    Modifier,

    /** 용언 수식어(부사격). 부사격 체언구(NP_AJT), 부사격 용언구(VP_AJT) 문말어미+부사격 조사(S_AJT) */
    Adjunct,

    /** 접속어. 접속격 체언(NP_CNJ) */
    Conjunctive,

    /** 독립어. 체언(NP_INT) */
    Interjective,

    /** 삽입어구. 삽입된 성분의 기능표지 위치 (예: NP_PRN) */
    Parenthetical,

    /** 정의되지 않음 */
    Undefined = Value
  }

}
