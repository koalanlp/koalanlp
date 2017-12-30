package kr.bydelta.koala.arirang

import java.util

import kr.bydelta.koala.Implicit._
import kr.bydelta.koala.POS
import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.traits.CanTagOnlyASentence
import org.apache.lucene.analysis.ko.morph.{AnalysisOutput, PatternConstants, WordSegmentAnalyzer}

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

/**
  * 아리랑 형태소 분석기입니다.
  */
class Tagger extends CanTagOnlyASentence[Seq[AnalysisOutput]] {
  val tagger = new WordSegmentAnalyzer

  override def tagSentenceOriginal(text: String): Seq[AnalysisOutput] =
  if (text.trim.isEmpty) Seq.empty
  else {
    val list = new util.LinkedList[util.List[AnalysisOutput]]()
    tagger.analyze(text.trim, list, false)
    list.asScala.map(_.asScala.maxBy(_.getScore))
  }

  override private[koala] def convertSentence(text: String, result: Seq[AnalysisOutput]): Sentence = {
    var sentence = text
    var wordList =
      result.filter(_.getSource.trim.nonEmpty).flatMap {
        word =>
          val words = ArrayBuffer[Word]()
          var surface = word.getSource.trim
          var morphs = Seq[Morpheme]()

          val (sentCurr, sentRemain) = sentence.splitAt(sentence.indexOf(surface))
          sentence = sentRemain.substring(surface.length)

          if (sentCurr.trim.nonEmpty) {
            morphs +:= Morpheme(sentCurr.trim, " ", POS.NA)
            surface = sentCurr + surface
          }
          morphs = morphs ++: interpretOutput(word)
          morphs = morphs.flatMap {
            // Find Special characters and separate them as morphemes
            case m@Morpheme(s, tag) if Tagger.SPRegex.findFirstMatchIn(s).isDefined ||
              Tagger.SFRegex.findFirstMatchIn(s).isDefined ||
              Tagger.filterRegex.findFirstMatchIn(s).isDefined =>
              s.split(Tagger.punctuationsSplit).map {
                case x if Tagger.SPRegex.findFirstMatchIn(x).isDefined =>
                  Morpheme(x, m.rawTag, POS.SP)
                case x if Tagger.SFRegex.findFirstMatchIn(x).isDefined =>
                  Morpheme(x, m.rawTag, POS.SF)
                case x if Tagger.SSRegex.findFirstMatchIn(x).isDefined =>
                  Morpheme(x, m.rawTag, POS.SS)
                case x if x.matches("\\s+") =>
                  Morpheme(x, m.rawTag, POS.TEMP)
                case x => Morpheme(x.trim, m.rawTag, tag)
              }
            case m => Seq(m)
          }

          // Now separate special characters as words
          while (morphs.exists(Tagger.checkSet)) {
            val (morph, after) = morphs.splitAt(morphs.indexWhere(Tagger.checkSet))
            val symbol = after.head.surface
            val (prev, next) = surface.splitAt(surface.indexOf(symbol))
            if (prev.trim.nonEmpty) {
              words.append(Word(surface = prev.trim, morphemes = morph))
            }
            if (symbol.trim.nonEmpty) {
              words.append(Word(surface = symbol.trim, morphemes = Seq(after.head)))
            }

            surface = next.substring(symbol.length)
            morphs = after.tail
          }

          if (surface.trim.nonEmpty) {
            words.append(Word(surface = surface.trim, morphemes = morphs))
          }

          words
      }

    if (sentence.trim.nonEmpty) {
      wordList :+= Word(surface = sentence.trim,
        morphemes = Seq(Morpheme(surface = sentence.trim, rawTag = " ", tag = POS.NA)))
    }

    Sentence(wordList)
  }

  private def interpretOutput(o: AnalysisOutput): Seq[Morpheme] = {
    val morphs = ArrayBuffer[Morpheme]()
    morphs.append(Morpheme(o.getStem.trim, "" + o.getPos, o.getPos match {
      case PatternConstants.POS_NPXM => POS.NNG
      case PatternConstants.POS_VJXV => POS.VV
      case PatternConstants.POS_AID => POS.MAG
      case _ => POS.SW
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

object Tagger {
  private val checkSet = Seq(POS.SF, POS.SP, POS.SS, POS.TEMP)
  private val SFRegex = "(?U)[\\.\\?\\!]+".r
  private val SPRegex = "(?U)[,:;·/]+".r
  private val punctuationsSplit = "(?U)((?<=[,\\.:;\\?\\!/·\\s\'\"\\(\\[\\{<〔〈《「『【‘“\\)\\]\\}>〕〉》」』】’”]+)|" +
    "(?=[,\\.:;\\?\\!/·\\s\'\"\\(\\[\\{<〔〈《「『【‘“\\)\\]\\}>〕〉》」』】’”]+))"
  private val filterRegex = "(?U)[^\'\"\\(\\[\\{<〔〈《「『【‘“\\)\\]\\}>〕〉》」』】’”]+".r
  private val SSRegex = "(?U)[\'\"\\(\\[\\{<〔〈《「『【‘“\\)\\]\\}>〕〉》」』】’”]+".r
}