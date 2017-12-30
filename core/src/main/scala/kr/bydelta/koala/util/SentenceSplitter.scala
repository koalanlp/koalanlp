package kr.bydelta.koala.util

import kr.bydelta.koala.POS
import kr.bydelta.koala.data.{Sentence, Word}

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

object SentenceSplitter {
  private val quoteRegex = "(?U)[\'\"]{1}".r
  private val openParenRegex = "(?U)[\\(\\[\\{<〔〈《「『【‘“]{1}".r
  private val closeParenRegex = "(?U)[\\)\\]\\}>〕〉》」』】’”]{1}".r
  private val matchRegex = ("(?U)(\'[^\']*\'|\"[^\"]*\"|\\([^\\(\\)]*\\)|\\[[^\\[\\]]*\\]|\\{[^\\{\\}]*\\}|" +
    "<[^<>]*>|〔[^〔〕]*〕|〈[^〈〉]*〉|《[^《》]*》|「[^「」]*」|『[^『』]*』|【[^【】]*】|‘[^‘’]*’|“[^“”]*”)").r
  private val filterRegex = "(?U)[^\'\"\\(\\[\\{<〔〈《「『【‘“\\)\\]\\}>〕〉》」』】’”]+".r

  /**
    * 분석결과를 토대로 문장을 분리함.
    *
    * @param para 분리할 문단.
    * @return 문장단위로 분리된 결과
    */
  def apply(para: Seq[Word]): Seq[Sentence] = split(para)

  /**
    * 분석결과를 토대로 문장을 분리함.
    *
    * @param para 분리할 문단.
    * @param pos  현재 읽고있는 위치.
    * @param open 현재까지 열려있는 묶음기호 Stack.
    * @param acc  현재까지 분리된 문장들.
    * @return 문장단위로 분리된 결과
    */
  @tailrec
  private def split(para: Seq[Word],
                           pos: Int = 0,
                           open: List[Char] = List(),
                           acc: ArrayBuffer[Sentence] = ArrayBuffer()): Seq[Sentence] =
  if (para.isEmpty) acc
  else {
    import kr.bydelta.koala.Implicit._
    val rawEndmark = para.indexWhere(_.exists(POS.SF), pos)
    val rawParen = para.indexWhere({
      e =>
        (e.exists(POS.SS) ||
          openParenRegex.findFirstMatchIn(e.surface).isDefined ||
          closeParenRegex.findFirstMatchIn(e.surface).isDefined ||
          quoteRegex.findFirstMatchIn(e.surface).isDefined) &&
          matchRegex.findFirstIn(e.surface).isEmpty
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
        split(next, 0, open, acc)
      } else {
        val parenStr = para(paren)
        val surface = filterRegex.replaceAllIn(parenStr.surface, "")
        var nOpen = open
        if (closeParenRegex.findFirstMatchIn(surface).isEmpty) {
          nOpen ++:= surface.toSeq
        }
        split(para, paren + 1, nOpen, acc)
      }
    } else {
      if (paren == para.length) {
        acc += Sentence(para)
        acc
      } else {
        val parenStr = para(paren)
        val surface = filterRegex.replaceAllIn(parenStr.surface, "")
        var nOpen = open
        if (openParenRegex.findFirstMatchIn(surface).isDefined) {
          nOpen ++:= surface.toSeq
        } else if (closeParenRegex.findFirstMatchIn(surface).isDefined) {
          nOpen = nOpen.tail
        } else {
          val top = nOpen.head
          if (surface.nonEmpty) {
            if (surface.last == top) nOpen = nOpen.tail
            else nOpen ++:= surface.toSeq
          }
        }
        split(para, paren + 1, nOpen, acc)
      }
    }
  }
}