package kr.bydelta.koala.kmr

import kr.bydelta.koala.Processor
import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.traits.CanTag
import kr.co.shineware.nlp.komoran.core.analyzer.Komoran

import scala.annotation.tailrec
import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by bydelta on 16. 7. 22.
  */
class Tagger extends CanTag {
  lazy val komoran = {
    Dictionary.extractResource()
    val komoran = new Komoran(Dictionary.getExtractedPath)

    if (Dictionary.userDict.exists())
      komoran.setUserDic(Dictionary.userDict.getAbsolutePath)
    komoran
  }

  @throws[Exception]
  override def tagSentence(text: String): Sentence = {
    new Sentence(parse(text))
  }

  private def parse(text: String): mutable.Buffer[Word] = {
    komoran.analyze(text).map {
      word =>
        val originalWord = word.map(_.getFirst).mkString
        new Word(
          originalWord = originalWord,
          morphemes =
            word.map {
              pair =>
                new Morpheme(
                  morpheme = pair.getFirst,
                  rawTag = pair.getSecond,
                  processor = Processor.Komoran
                )
            }
        )
    }
  }

  @throws[Exception]
  override def tagParagraph(text: String): Seq[Sentence] = {
    splitSentences(parse(text))
  }

  @tailrec
  private def splitSentences(para: Seq[Word],
                             pos: Int = 0,
                             open: mutable.Stack[String] = mutable.Stack(),
                             acc: ArrayBuffer[Sentence] = ArrayBuffer()): Seq[Sentence] =
    if (para.isEmpty) acc
    else {
      val rawEndmark = para.indexWhere(_.existsMorpheme("SF"), pos)
      val rawParen = para.indexWhere({
        e =>
          e.existsMorpheme("SS") ||
            Tagger.openParenRegex.findFirstMatchIn(e.originalWord).isDefined ||
            Tagger.closeParenRegex.findFirstMatchIn(e.originalWord).isDefined ||
            Tagger.quoteRegex.findFirstMatchIn(e.originalWord).isDefined
      }, pos)

      val endmark = if (rawEndmark == -1) para.length else rawEndmark
      val paren = if (rawParen == -1) para.length else rawParen

      if (endmark == paren && paren == para.length) {
        acc += new Sentence(para)
        acc
      } else if (open.isEmpty) {
        if (endmark < paren) {
          val (sent, next) = para.splitAt(endmark + 1)
          acc += new Sentence(sent)
          splitSentences(next, 0, open, acc)
        } else {
          val parenStr = para(paren)
          val surface = parenStr.originalWord
          if (Tagger.closeParenRegex.findFirstMatchIn(surface).isEmpty) {
            open push surface
          }
          splitSentences(para, paren + 1, open, acc)
        }
      } else {
        if (paren == para.length) {
          acc += new Sentence(para)
          acc
        } else {
          val parenStr = para(paren)
          val surface = parenStr.originalWord
          if (Tagger.openParenRegex.findFirstMatchIn(surface).isDefined) {
            open push surface
          } else if (Tagger.closeParenRegex.findFirstMatchIn(surface).isDefined) {
            open.pop
          } else {
            val top = open.top
            if (surface == top) open.pop()
            else open push surface
          }
          splitSentences(para, paren + 1, open, acc)
        }
      }
    }
}

object Tagger {
  private val quoteRegex = "(?U)[\'\"]{1}".r
  private val openParenRegex = "(?U)[\\(\\[\\{<〔〈《「『【‘“]{1}".r
  private val closeParenRegex = "(?U)[\\)\\]\\}>〕〉《」』】’”]{1}".r
}

