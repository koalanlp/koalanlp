package kr.bydelta.koala.model

import cc.factorie.infer.BP
import kr.bydelta.koala.Implicit._
import kr.bydelta.koala.POS
import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.traits.CanTag

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

/**
  * Created by bydelta on 17. 1. 11.
  */
class Tagger(private val crf: CRFModel, private val irregulars: Map[String, String]) extends CanTag[Seq[Label]] {
  /**
    * 주어진 String 문단을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @return 품사분석 결과를 문장단위로 잘라서 포함한 Seq[Sentence] 객체
    */
  override def tagParagraph(text: String): Seq[Sentence] =
  splitSentences(tagSentence(text))

  override def tagSentenceRaw(text: String): Seq[Label] = {
    val sentence = text.map(c => Label("", c.toString))
    BP.inferChainMax(sentence, crf)
    sentence
  }

  def cleanUp(seq: Seq[(String, String, String)],
              acc: Seq[Morpheme] = Seq.empty,
              prevTag: String = "",
              prevMorph: StringBuilder = new StringBuilder,
              surface: StringBuilder = new StringBuilder): Word =
    if (seq.isEmpty) Word(surface = surface.toString, morphemes = acc.reverse)
    else {
      val (orig, smorp, tag) = seq.head
      surface append orig

      if (tag == prevTag) {
        prevMorph append smorp
        cleanUp(seq.tail, acc, prevTag, prevMorph, surface)
      } else {
        val morp = Morpheme(surface = prevMorph.toString, rawTag = prevTag, tag = POS.withName(prevTag))
        cleanUp(seq.tail, morp +: acc, tag, new StringBuilder(smorp), surface)
      }
    }

  def restoreVariations(tok: String, tag: String): Seq[(String, String, String)] =
    if (!tag.contains('.')) {
      Seq((tok, tok, tag))
    } else {
      val set = tok + "/" + tag
      irregulars.getOrElse(set, set).split("\\s+").zipWithIndex.map {
        case (tagline, id) =>
          val Array(smorp, tag, _@_*) = tagline.split("/")
          if (id == 0)
            (tok, smorp, tag)
          else
            ("", smorp, tag)
      }
    }

  @tailrec
  private def buildWords(seq: Seq[Label],
                         words: Seq[Word] = Seq.empty,
                         morphs: Seq[(String, String, String)] = Seq.empty): Sentence =
    if (seq.isEmpty) Sentence(words.reverse)
    else {
      val Label(tag, Token(tok)) = seq.head

      val newTag = tag.replaceAll("\\.*W[AD]{1}", "")
      val newMorph =
        if (newTag.nonEmpty) {
          restoreVariations(tok, newTag) ++ morphs
        } else morphs

      if (tag endsWith "WA") {
        buildWords(seq.tail, cleanUp(newMorph.reverse) +: words)
      } else {
        buildWords(seq.tail, words, newMorph)
      }
    }

  override private[koala] def convert(result: Seq[Label]): Sentence = buildWords(result)

  /**
    * 분석결과를 토대로 문장을 분리함.
    *
    * @param para 분리할 문단.
    * @param pos  현재 읽고있는 위치.
    * @param open 현재까지 열려있는 묶음기호 Stack.
    * @param acc  현재까지 분리된 문장들.
    * @return 문장단위로 분리된 결과
    */
  private def splitSentences(para: Seq[Word],
                             pos: Int = 0,
                             open: List[String] = List(),
                             acc: ArrayBuffer[Sentence] = ArrayBuffer()): Seq[Sentence] =
  if (para.isEmpty) acc
  else {
    val rawEndmark = para.indexWhere(_.exists(POS.SF), pos)
    val rawParen = para.indexWhere({
      e =>
        e.exists(POS.SS) ||
          Tagger.openParenRegex.findFirstMatchIn(e.surface).isDefined ||
          Tagger.closeParenRegex.findFirstMatchIn(e.surface).isDefined ||
          Tagger.quoteRegex.findFirstMatchIn(e.surface).isDefined
    }, pos)

    val endmark = if (rawEndmark == -1) para.length else rawEndmark
    val paren = if (rawParen == -1) para.length else rawParen

    if (endmark == paren && paren == para.length) {
      acc += Sentence(para)
      acc
    } else if (open.isEmpty) {
      if (endmark < paren) {
        val (sent, next) = para.splitAt(endmark + 1)
        acc += Sentence(sent)
        splitSentences(next, 0, open, acc)
      } else {
        val parenStr = para(paren)
        val surface = parenStr.surface
        var nOpen = open
        if (Tagger.closeParenRegex.findFirstMatchIn(surface).isEmpty) {
          nOpen +:= surface
        }
        splitSentences(para, paren + 1, nOpen, acc)
      }
    } else {
      if (paren == para.length) {
        acc += Sentence(para)
        acc
      } else {
        val parenStr = para(paren)
        val surface = parenStr.surface
        var nOpen = open
        if (Tagger.openParenRegex.findFirstMatchIn(surface).isDefined) {
          nOpen +:= surface
        } else if (Tagger.closeParenRegex.findFirstMatchIn(surface).isDefined) {
          nOpen = nOpen.tail
        } else {
          val top = nOpen.head
          if (surface == top) nOpen = nOpen.tail
          else nOpen +:= surface
        }
        splitSentences(para, paren + 1, nOpen, acc)
      }
    }
  }
}

/**
  * 코모란 분석기의 Companion object.
  */
private[koala] object Tagger {
  private val quoteRegex = "(?U)[\'\"]{1}".r
  private val openParenRegex = "(?U)[\\(\\[\\{<〔〈《「『【‘“]{1}".r
  private val closeParenRegex = "(?U)[\\)\\]\\}>〕〉《」』】’”]{1}".r
}