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

class Parser extends CanDepParse {
  Configuration.hanBaseDir = Dictionary.getExtractedPath
  private lazy val tagger = new Tagger
  private lazy val wrapper = new BerkeleyParserWrap(Configuration.parserModel)
  private lazy val conv: Converter = new Converter

  @throws[Exception]
  def parse(sentence: String): Sentence = {
    val taggedRaw = tagger.tagSentenceRaw(
      sentence.replaceAll("\\(", "[").replaceAll("\\)", "]")
    )
    val tagged = tagger.parseResult(taggedRaw)

    val depTree: DTree = conv.convert(parseTreeOf(taggedRaw))
    depTree.getNodeList.foreach {
      node =>
        try {
          val thisWord = tagged.apply(node.getWordIdx)
          val headWord = tagged.apply(node.getHead.getWordIdx)
          val rawTag = node.getdType
          val tag = Processor.Hannanum dependencyOf rawTag
          headWord.addDependant(thisWord, tag, rawTag)
        } catch {
          case e: Exception =>
            if (node.getWordIdx != -1 && node.getWordIdx < tagged.size) {
              tagged.root = node.getWordIdx
            }
        }
    }

    tagged
  }

  override def parse(sentence: Sentence): Sentence = {
    val depTree: DTree = conv.convert(parseTreeOf(deparse(sentence)))
    depTree.getNodeList.foreach {
      node =>
        try {
          val thisWord = sentence.apply(node.getWordIdx)
          val headWord = sentence.apply(node.getHead.getWordIdx)
          val rawTag = node.getdType
          val tag = Processor.Hannanum dependencyOf rawTag
          headWord.addDependant(thisWord, tag, rawTag)
        } catch {
          case e: Exception =>
            if (node.getWordIdx != -1 && node.getWordIdx < sentence.size) {
              sentence.root = node.getWordIdx
            }
        }
    }

    sentence
  }

  private def parseTreeOf(sentence: HSent): ParseTree =
    new ParseTree(
      sentence.getPlainEojeols.mkString(" "), conv.StringforDepformat(
        Converter.functionTagReForm(
          wrapper.parseForced(sentence)
        )
      ), 0, true)

  private def deparse(result: Sentence): HSent = {
    val (plainEojeols, eojeols) = result.words.map {
      word =>
        val (morphs, tags) = word.morphemes.map {
          m => (m.morpheme, Processor.Hannanum originalPOSOf m.tag)
        }.unzip
        (word.originalWord, new Eojeol(morphs.toArray, tags.toArray))
    }.unzip

    new HSent(0, 0, true, plainEojeols.toArray, eojeols.toArray)
  }

  def addUserDictionary(dict: (String, POSTag)*) {
    Dictionary.addUserDictionary(dict: _*)
  }

  def addUserDictionary(morph: String, tag: POSTag) {
    Dictionary.addUserDictionary(morph, tag)
  }
}

