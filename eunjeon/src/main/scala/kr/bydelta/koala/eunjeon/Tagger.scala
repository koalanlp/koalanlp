package kr.bydelta.koala.eunjeon

import kr.bydelta.koala.data.{Sentence, Word}
import kr.bydelta.koala.traits.CanTag
import kr.bydelta.koala.{Processor, data}
import org.bitbucket.eunjeon.seunjeon._

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by bydelta on 16. 7. 21.
  */
class Tagger extends CanTag {
  lazy val tokenizer: Tokenizer = {
    val lexiconDict = new LexiconDict().load()
    val connectionCostDict = new ConnectionCostDict().load()
    val tok = new Tokenizer(lexiconDict, connectionCostDict)
    Dictionary.reloadDic()
    if (Dictionary.nonEmpty)
      tok.setUserDict(Dictionary.userDict)
    tok
  }

  @throws[Exception]
  override def tagSentence(text: String): Sentence =
    parseResult(Eojeoler.build(tokenizer.parseText(text, dePreAnalysis = false)))

  private def parseResult(seq: Seq[Eojeol]): Sentence =
    new Sentence(
      seq.map {
        eojeol =>
          new Word(
            originalWord = eojeol.surface,
            morphemes =
              eojeol.nodes.flatMap { node =>
                val array = node.morpheme.feature
                val compoundTag = array.head
                val tokenized = array.last

                if (tokenized == "*") {
                  Seq(
                    new data.Morpheme(morpheme = node.morpheme.surface,
                      rawTag = compoundTag, processor = Processor.Eunjeon)
                  )
                } else {
                  tokenized.split("\\+").map {
                    tok =>
                      val arr = tok.split("/")
                      new data.Morpheme(morpheme = arr.head,
                        rawTag = arr(1), processor = Processor.Eunjeon)
                  }
                }
              }
          )
      }
    )

  @throws[Exception]
  override def tagParagraph(text: String): Seq[Sentence] = {
    val parsed = Eojeoler.build(tokenizer.parseText(text, dePreAnalysis = false))
    splitSentences(parsed).map(parseResult)
  }

  @tailrec
  private def splitSentences(para: Seq[Eojeol],
                             pos: Int = 0,
                             open: mutable.Stack[String] = mutable.Stack(),
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
          if (!parenStr.nodes.last.morpheme.feature.head.equals("SSC")) {
            open push parenStr.surface
          }
          splitSentences(para, paren + 1, open, acc)
        }
      } else {
        if (paren == para.length) {
          acc += para
          acc
        } else {
          val parenStr = para(paren)
          val surface = parenStr.surface
          val tag = parenStr.nodes.last.morpheme.feature.head
          if (tag.equals("SSO")) {
            open push surface
          } else if (tag.equals("SSC")) {
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
}

