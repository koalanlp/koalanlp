package kr.bydelta.koala.hnn

import kaist.cilab.jhannanum.common.Eojeol
import kaist.cilab.jhannanum.common.communication.{Sentence => HSent}
import kaist.cilab.parser.berkeleyadaptation.Configuration
import kaist.cilab.parser.corpusconverter.sejong2treebank.sejongtree.ParseTree
import kaist.cilab.parser.dependency.DTree
import kaist.cilab.parser.psg2dg.Converter
import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.Processor
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.helper.BerkeleyParserWrap
import kr.bydelta.koala.traits.CanDepParse

/**
  * 한나눔 통합 구문분석기
  */
class Parser extends CanDepParse {
  Configuration.hanBaseDir = Dictionary.getExtractedPath
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
  private lazy val conv: Converter = new Converter

  override def parse(sentence: String): Sentence = {
    val taggedRaw = tagger.tagSentenceRaw(
      sentence.replaceAll("\\(", "[").replaceAll("\\)", "]")
    )
    val tagged = tagger.convert(taggedRaw)
    convert(taggedRaw, tagged)
  }

  override def parse(sentence: Sentence): Sentence = {
    convert(deParse(sentence), sentence)
  }

  /**
    * 의존관계분석을 수행하고, 그 결과를 변환.
    *
    * @param hSent    한나눔의 문장 객체.
    * @param sentence 통합분석기의 문장 객체.
    * @return 의존관계분석이 포함된 결과.
    */
  private def convert(hSent: HSent, sentence: Sentence): Sentence = {
    val depTree: DTree = conv.convert(parseTreeOf(hSent))
    depTree.getNodeList.foreach {
      node =>
        try {
          val thisWord = sentence.get(node.getWordIdx)
          val headWord = sentence.get(node.getHead.getWordIdx)
          val rawTag = node.getdType
          val tag = Processor.Hannanum dependencyOf rawTag
          headWord.addDependant(thisWord, tag, rawTag)
        } catch {
          case e: Exception =>
            if (node.getWordIdx != -1 && node.getWordIdx < sentence.size) {
              sentence.topLevels += sentence.get(node.getWordIdx)
            }
        }
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
    new ParseTree(
      sentence.getPlainEojeols.mkString(" "), conv.StringforDepformat(
        Converter.functionTagReForm(
          wrapper.parseForced(sentence)
        )
      ), 0, true)

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
          m => (m.surface, Processor.Hannanum originalPOSOf m.tag)
        }.unzip
        (word.surface, new Eojeol(morphs.toArray, tags.toArray))
    }.unzip

    new HSent(0, 0, true, plainEojeols.toArray, eojeols.toArray)
  }

  override def addUserDictionary(dict: (String, POSTag)*) {
    Dictionary.addUserDictionary(dict: _*)
  }

  override def addUserDictionary(morph: String, tag: POSTag) {
    Dictionary.addUserDictionary(morph, tag)
  }
}

