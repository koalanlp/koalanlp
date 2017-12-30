package kr.bydelta.koala.hnn

import kaist.cilab.jhannanum.common.Eojeol
import kaist.cilab.jhannanum.common.communication.{Sentence => HSent}
import kaist.cilab.parser.berkeleyadaptation.Configuration
import kaist.cilab.parser.corpusconverter.sejong2treebank.sejongtree.ParseTree
import kaist.cilab.parser.dependency.DTree
import kaist.cilab.parser.psg2dg.Converter
import kr.bydelta.koala._
import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.helper.BerkeleyParserWrap
import kr.bydelta.koala.traits.CanDepParse

import scala.collection.JavaConverters._

/**
  * 한나눔 통합 구문분석기
  */
class Parser extends CanDepParse {
  /**
    * 품사분석기
    */
  private lazy val tagger = new Tagger
  /**
    * 의존관계분석 Wrapper
    */
  private lazy val wrapper = new BerkeleyParserWrap(Configuration.parserModel)
  /**
    * 의존관계분석결과 변환기.
    */
  private lazy val converter: Converter = new Converter

  override def parse(sentence: String): Seq[Sentence] = {
    tagger.tagParagraphOriginal(sentence).map(convert)
  }

  override def parse(sentence: Sentence): Sentence = {
    convert(deParse(sentence))
  }

  /**
    * 의존관계분석을 수행하고, 그 결과를 변환.
    *
    * @param hSent    한나눔의 문장 객체.
    * @return 의존관계분석이 포함된 결과.
    */
  private def convert(hSent: HSent): Sentence =
  if (hSent.getEojeols.isEmpty) Sentence(Seq())
  else {
    val tree = parseTreeOf(hSent)
    val depTree: DTree = converter.convert(tree)

    val sentence = Sentence(
      depTree.getNodeList.map {
        node =>
          val phrase = node.getCorrespondingPhrase

          Word(
            phrase.getStringContents,
            phrase.getMyTerminals.asScala.map {
              term =>
                val word =
                  if (term.getWord.matches("^.*\\-[LR]RB\\-.*$"))
                    term.getWord.replaceAll("\\-LRB\\-", "(").replaceAll("\\-RRB\\-", ")")
                  else
                    term.getWord
                // 한나눔의 Parser 결과는 세종 표기를 따름.
                Morpheme(word, term.getPOS, POS.withName(term.getPOS))
            }
          )
      }
    )

    depTree.getNodeList.foreach {
      node =>
        val rawTag = node.getdType
        val tag = HNNdepTag(rawTag)
        val thisWord = node.getWordIdx
        val headWord = try {
          sentence(node.getHead.getWordIdx)
        } catch {
          case _: Throwable => sentence.root
        }
        headWord.addDependant(thisWord, tag, rawTag)
    }

    sentence
  }

  /**
    * 의존관계트리를 구성함.
    *
    * @param sentence 분석할 한나눔 문장.
    * @return 의존관계트리.
    */
  private def parseTreeOf(sentence: HSent): ParseTree =
  if (sentence.getEojeols.isEmpty) new ParseTree("", "", 0, true)
  else
    new ParseTree(
      sentence.getPlainEojeols.mkString(" "), converter.StringforDepformat(
        Converter.functionTagReForm(
          wrapper.parseForced(encodeParenthesis(sentence))
        )
      ), 0, true)

  private def encodeParenthesis(sentence: HSent) = {
    sentence.getEojeols.foreach {
      e =>
        val morphs = e.getMorphemes
        morphs.zipWithIndex.foreach {
          case (m, i) if m matches "^.*[\\(\\)]+.*$" =>
            morphs.update(i, m.replaceAll("\\(", "-LRB-").replaceAll("\\)", "-RRB-"))
          case _ =>
        }
    }
    sentence
  }

  /**
    * 통합분석기의 결과를 한나눔 문장객체로 복원.
    *
    * @param result 복원할 통합결과
    * @return 복원된 객체.
    */
  private def deParse(result: Sentence): HSent = {
    val (plainEojeols, eojeols) = result.words.map {
      word =>
        val (morphs, tags) = word.morphemes.map {
          m => (m.surface, fromSejongPOS(m.tag))
        }.unzip
        (word.surface, new Eojeol(morphs.toArray, tags.toArray))
    }.unzip

    new HSent(0, 0, true, plainEojeols.toArray, eojeols.toArray)
  }
}
