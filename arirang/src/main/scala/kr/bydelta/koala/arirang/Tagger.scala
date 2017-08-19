package kr.bydelta.koala.arirang

import java.util

import kr.bydelta.koala.POS
import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.traits.CanTag
import org.apache.lucene.analysis.ko.morph.{AnalysisOutput, PatternConstants, WordSegmentAnalyzer}

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

/**
  * Created by bydelta on 17. 8. 19.
  */
class Tagger extends CanTag[Seq[AnalysisOutput]] {
  val tagger = new WordSegmentAnalyzer

  @deprecated("Sentence split is not implemented. Please use other sentence splitter before tagging.", "1.6.0")
  override def tagParagraph(text: String): Seq[Sentence] = {
    Seq(tagSentence(text))
  }

  override def tagSentenceRaw(text: String): Seq[AnalysisOutput] =
    if (text.trim.isEmpty) Seq.empty
    else {
      val list = new util.LinkedList[util.List[AnalysisOutput]]()
      tagger.analyze(text, list, false)
      list.asScala.map(_.asScala.maxBy(_.getScore))
    }

  override private[koala] def convert(result: Seq[AnalysisOutput]): Sentence = {
    Sentence(
      result.filter(_.getSource.trim.nonEmpty).map {
        word =>
          val morphs = interpretOutput(word)
          Word(surface = word.getSource.trim, morphemes = morphs)
      }
    )
  }

  private def interpretOutput(o: AnalysisOutput): Seq[Morpheme] = {
    val morphs = ArrayBuffer[Morpheme]()
    morphs.append(Morpheme(o.getStem.trim, "" + o.getPos, o.getPos match {
      case PatternConstants.POS_NPXM => POS.NNG
      case PatternConstants.POS_VJXV => POS.VV
      case PatternConstants.POS_AID => POS.MAG
      case _ => POS.SY
    }))

    if (o.getNsfx != null) {
      //NounSuffix
      morphs.append(Morpheme(o.getNsfx.trim, "_s", POS.XSN))
    }

    if (o.getPatn != 2 && o.getPatn != 22) {
      // 2: 체언 + 조사, 부사 + 조사 : '빨리도'
      if (o.getPatn == 3) {
        //* 체언 + 용언화접미사 + 어미 */
        morphs.append(Morpheme(o.getVsfx.trim, "_t", POS.XSV))
        if (o.getPomi != null) {
          morphs.append(Morpheme(o.getPomi.trim, "_f", POS.EP))
        }

        morphs.append(Morpheme(o.getEomi.trim, "_e", POS.EF))
      } else if (o.getPatn == 4) {
        //* 체언 + 용언화접미사 + '음/기' + 조사 */
        morphs.append(Morpheme(o.getVsfx.trim, "_t", POS.XSV))
        if (o.getPomi != null) {
          morphs.append(Morpheme(o.getPomi.trim, "_f", POS.EP))
        }

        morphs.append(Morpheme(o.getElist.get(0).trim, "_n", POS.ETN))
        morphs.append(Morpheme(o.getJosa.trim, "_j", POS.JX))
      } else if (o.getPatn == 5) {
        //* 체언 + 용언화접미사 + '아/어' + 보조용언 + 어미 */
        morphs.append(Morpheme(o.getVsfx.trim, "_t", POS.XSV))
        morphs.append(Morpheme(o.getElist.get(0).trim, "_c", POS.EC))
        morphs.append(Morpheme(o.getXverb.trim, "_W", POS.VX))
        if (o.getPomi != null) {
          morphs.append(Morpheme(o.getPomi.trim, "_f", POS.EP))
        }

        morphs.append(Morpheme(o.getEomi.trim, "_e", POS.EF))
      } else if (o.getPatn == 6) {
        //* 체언 + '에서/부터/에서부터' + '이' + 어미 */
        morphs.append(Morpheme(o.getJosa.trim, "_j", POS.JKB))
        morphs.append(Morpheme(o.getElist.get(0).trim, "_t", POS.VCP))
        if (o.getPomi != null) {
          morphs.append(Morpheme(o.getPomi.trim, "_f", POS.EP))
        }

        morphs.append(Morpheme(o.getEomi.trim, "_e", POS.EF))
      } else if (o.getPatn == 7) {
        //* 체언 + 용언화접미사 + '아/어' + 보조용언 + '음/기' + 조사 */
        morphs.append(Morpheme(o.getVsfx.trim, "_t", POS.XSV))
        morphs.append(Morpheme(o.getElist.get(0).trim, "_c", POS.EC))
        morphs.append(Morpheme(o.getXverb.trim, "_W", POS.VX))
        if (o.getPomi != null) {
          morphs.append(Morpheme(o.getPomi.trim, "_f", POS.EP))
        }

        morphs.append(Morpheme(o.getElist.get(0).trim, "_n", POS.ETN))
        morphs.append(Morpheme(o.getJosa.trim, "_j", POS.JX))
      } else if (o.getPatn == 11) {
        //* 용언 + 어미 */
        if (o.getPomi != null) {
          morphs.append(Morpheme(o.getPomi.trim, "_f", POS.EP))
        }

        morphs.append(Morpheme(o.getEomi.trim, "_e", POS.EF))
      } else if (o.getPatn == 12) {
        //* 용언 + '음/기' + 조사 */
        if (o.getPomi != null) {
          morphs.append(Morpheme(o.getPomi.trim, "_f", POS.EP))
        }

        morphs.append(Morpheme(o.getElist.get(0).trim, "_n", POS.ETN))
        morphs.append(Morpheme(o.getJosa.trim, "_j", POS.JX))
      } else if (o.getPatn == 13) {
        //* 용언 + '음/기' + '이' + 어미 */
        morphs.append(Morpheme(o.getElist.get(0).trim, "_n", POS.ETN))
        morphs.append(Morpheme(o.getElist.get(1).trim, "_s", POS.VCP))
        if (o.getPomi != null) {
          morphs.append(Morpheme(o.getPomi.trim, "_f", POS.EP))
        }

        morphs.append(Morpheme(o.getEomi.trim, "_e", POS.EF))
      } else if (o.getPatn == 14) {
        //* 용언 + '아/어' + 보조용언 + 어미 */
        morphs.append(Morpheme(o.getElist.get(0).trim, "_c", POS.EC))
        morphs.append(Morpheme(o.getXverb.trim, "_W", POS.VX))
        if (o.getPomi != null) {
          morphs.append(Morpheme(o.getPomi.trim, "_f", POS.EP))
        }

        morphs.append(Morpheme(o.getEomi.trim, "_e", POS.EF))
      } else if (o.getPatn == 15) {
        //* 용언 + '아/어' + 보조용언 + '음/기' + 조사 */
        morphs.append(Morpheme(o.getElist.get(1).trim, "_c", POS.EC))
        morphs.append(Morpheme(o.getXverb.trim, "_W", POS.VX))
        if (o.getPomi != null) {
          morphs.append(Morpheme(o.getPomi.trim, "_f", POS.EP))
        }

        morphs.append(Morpheme(o.getElist.get(0).trim, "_n", POS.ETN))
        morphs.append(Morpheme(o.getJosa.trim, "_j", POS.JX))
      }
    } else {
      morphs.append(Morpheme(o.getJosa.trim, "_j", POS.JX))
    }

    morphs
  }
}
