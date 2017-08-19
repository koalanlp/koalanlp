package kr.bydelta.koala.eunjeon

import kr.bydelta.koala.data.{Sentence, Word}
import kr.bydelta.koala.traits.CanTag
import org.bitbucket.eunjeon.seunjeon._

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

/**
  * 은전한닢 통합분석기
  */
class Tagger extends CanTag[Seq[Eojeol]] {
  /**
    * 은전한닢의 내장 Tokenizer.
    */
  lazy val tokenizer: Tokenizer = {
    val tok = new Tokenizer(Dictionary.lexiconDict, Dictionary.connectionCostDict)
    if (Dictionary.nonEmpty) {
      Dictionary.reloadDic()
      tok.setUserDict(Dictionary.userDict)
    }
    tok
  }

  override def tagParagraphRaw(text: String): Seq[Seq[Eojeol]] = {
    val parsed = Eojeoler.build(tokenizer.parseText(text, dePreAnalysis = false))
    splitSentences(parsed)
  }

  override private[koala] def convert(seq: Seq[Eojeol]): Sentence =
    Sentence(
      seq.map {
        eojeol =>
          Word(
            eojeol.surface,
            eojeol.nodes.flatMap(node => Dictionary.convertMorpheme(node.morpheme))
          )
      }
    )

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
  private def splitSentences(para: Seq[Eojeol],
                             pos: Int = 0,
                             open: List[String] = List(),
                             acc: ArrayBuffer[Seq[Eojeol]] = ArrayBuffer()): Seq[Seq[Eojeol]] =
    if (para.isEmpty) acc
    else {
      val rawEndmark = para.indexWhere(_.nodes.exists(_.morpheme.feature.head == "SF"), pos)
      val rawParen = para.indexWhere({
        e =>
          e.nodes.last.morpheme.feature.head.startsWith("SS") ||
            Tagger.quoteRegex.findFirstMatchIn(e.surface).isDefined
      }, pos)

      val endmark = if (rawEndmark == -1) para.length else rawEndmark
      val paren = if (rawParen == -1) para.length else rawParen

      if (endmark == paren && paren == para.length) {
        acc += para
        acc
      } else if (open.isEmpty) {
        if (endmark < paren) {
          val (sent, next) = para.splitAt(endmark + 1)
          acc += sent
          splitSentences(next, 0, open, acc)
        } else {
          val parenStr = para(paren)
          var nOpen = open
          if (!parenStr.nodes.last.morpheme.feature.head.equals("SSC")) {
            nOpen +:= parenStr.surface
          }
          splitSentences(para, paren + 1, nOpen, acc)
        }
      } else {
        if (paren == para.length) {
          acc += para
          acc
        } else {
          val parenStr = para(paren)
          val surface = parenStr.surface
          val tag = parenStr.nodes.last.morpheme.feature.head
          var nOpen = open
          if (tag.equals("SSO")) {
            nOpen +:= surface
          } else if (tag.equals("SSC")) {
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
  * 은전한닢 Tagger의 Companion object.
  */
private[koala] object Tagger {
  private val quoteRegex = "(?U)[\'\"]{1}".r
}

