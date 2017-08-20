package kr.bydelta.koala.kmr

import kr.bydelta.koala.POS
import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.traits.CanTag
import kr.bydelta.koala.util.{reduceVerbApply, reunionKorean}
import kr.co.shineware.nlp.komoran.core.Komoran
import kr.co.shineware.nlp.komoran.model.KomoranResult

import scala.collection.mutable.ArrayBuffer

/**
  * 코모란 형태소분석기.
  */
class Tagger extends CanTag[Sentence] {
  /**
    * 코모란 분석기 객체.
    */
  lazy val komoran = {
    val komoran = Tagger.komoran
    if (Dictionary.userDict.exists())
      komoran.setUserDic(Dictionary.userDict.getAbsolutePath)
    komoran
  }

  override def tagParagraphRaw(text: String): Seq[Sentence] =
    if (text.trim.nonEmpty) {
      splitSentences(convertParagraph(komoran.analyze(text)).words)
    } else Seq.empty

  override def convert(result: Sentence): Sentence = result

  def constructWordSurface(wAsScala: Seq[Morpheme]) = {
    reunionKorean(wAsScala.foldLeft((Seq.empty[Char], false))({
      case ((prevSeq, wasVerb), curr) =>
        val tag = curr.rawTag.toUpperCase
        if (tag.startsWith("E")) {
          val newSeq = reduceVerbApply(prevSeq, wasVerb, curr.surface.toSeq)
          (newSeq, false)
        } else {
          val isVerb = (tag.startsWith("V") && tag != "VA") || tag == "XSV"
          (prevSeq ++ curr.surface.toSeq, isVerb)
        }
    })._1)
  }

  private def convertParagraph(result: KomoranResult): Sentence = {
    if (result != null) {
      val words = ArrayBuffer[Word]()
      var morphs = ArrayBuffer[Morpheme]()
      var lastIdx = 0
      val tokenIt = result.getTokenList.iterator()

      while (tokenIt.hasNext) {
        val token = tokenIt.next()
        val tag = token.getPos
        if (token.getBeginIndex > lastIdx) {
          words += Word(surface = constructWordSurface(morphs), morphemes = morphs)
          morphs = ArrayBuffer[Morpheme]()
        }

        morphs += Morpheme(surface = token.getMorph, rawTag = token.getPos, fromKomoranTag(tag))
        lastIdx = token.getEndIndex
      }

      if (morphs.nonEmpty) {
        words += Word(surface = constructWordSurface(morphs), morphemes = morphs)
      }

      Sentence(words)
    } else
      Sentence(Seq.empty)
  }

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
                             open: List[Char] = List(),
                             acc: ArrayBuffer[Sentence] = ArrayBuffer()): Seq[Sentence] =
    if (para.isEmpty) acc
    else {
      import kr.bydelta.koala.Implicit._
      val rawEndmark = para.indexWhere(_.exists(POS.SF), pos)
      val rawParen = para.indexWhere({
        e =>
          (e.exists(POS.SS) ||
            Tagger.openParenRegex.findFirstMatchIn(e.surface).isDefined ||
            Tagger.closeParenRegex.findFirstMatchIn(e.surface).isDefined ||
            Tagger.quoteRegex.findFirstMatchIn(e.surface).isDefined) &&
            Tagger.matchRegex.findFirstIn(e.surface).isEmpty
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
          val surface = Tagger.filterRegex.replaceAllIn(parenStr.surface, "")
          var nOpen = open
          if (Tagger.closeParenRegex.findFirstMatchIn(surface).isEmpty) {
            nOpen ++:= surface.toSeq
          }
          splitSentences(para, paren + 1, nOpen, acc)
        }
      } else {
        if (paren == para.length) {
          acc += Sentence(para)
          acc
        } else {
          val parenStr = para(paren)
          val surface = Tagger.filterRegex.replaceAllIn(parenStr.surface, "")
          var nOpen = open
          if (Tagger.openParenRegex.findFirstMatchIn(surface).isDefined) {
            nOpen ++:= surface.toSeq
          } else if (Tagger.closeParenRegex.findFirstMatchIn(surface).isDefined) {
            nOpen = nOpen.tail
          } else {
            val top = nOpen.head
            if (surface.last == top) nOpen = nOpen.tail
            else nOpen ++:= surface.toSeq
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
  /**
    * 코모란 분석기 객체.
    */
  private lazy val komoran = {
    Dictionary.extractResource()
    val komoran = new Komoran()
    komoran
  }
  private val quoteRegex = "(?U)[\'\"]{1}".r
  private val openParenRegex = "(?U)[\\(\\[\\{<〔〈《「『【‘“]{1}".r
  private val closeParenRegex = "(?U)[\\)\\]\\}>〕〉》」』】’”]{1}".r
  private val matchRegex = ("(?U)(\'[^\']*\'|\"[^\"]*\"|\\([^\\(\\)]*\\)|\\[[^\\[\\]]*\\]|\\{[^\\{\\}]*\\}|" +
    "<[^<>]*>|〔[^〔〕]*〕|〈[^〈〉]*〉|《[^《》]*》|「[^「」]*」|『[^『』]*』|【[^【】]*】|‘[^‘’]*’|“[^“”]*”)").r
  private val filterRegex = "(?U)[^\'\"\\(\\[\\{<〔〈《「『【‘“\\)\\]\\}>〕〉》」』】’”]+".r
}

