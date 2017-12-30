package kr.bydelta.koala.helper

import java.io.File
import java.util

import edu.berkeley.nlp.PCFGLA._
import edu.berkeley.nlp.syntax.Tree
import edu.berkeley.nlp.syntax.Trees.PennTreeRenderer
import edu.berkeley.nlp.util.Numberer
import kaist.cilab.jhannanum.common.communication.Sentence
import kaist.cilab.parser.berkeleyadaptation.Configuration
import kr.bydelta.koala.hnn.Dictionary

import scala.collection.JavaConverters._

private[koala] object BerkeleyParserWrap {
  lazy val args: String = "-in " + Configuration.parserModel
  lazy val opts: GrammarTester.Options = new OptionParser(classOf[GrammarTester.Options]).parse(args.split(" "), true).asInstanceOf[GrammarTester.Options]
  lazy val finalLevel: Int = opts.finalLevel
  lazy val viterbiParse: Boolean = opts.viterbi
  lazy val doVariational: Boolean = false
  lazy val useGoldPOS: Boolean = opts.useGoldPOS

  lazy val inFileName: String = Dictionary.extractResource() + File.separator + opts.inFileName
  lazy val pData: ParserData = {
    val p = ParserData.Load(inFileName)
    Numberer.setNumberers(p.getNumbs)
    p
  }
  lazy val grammar: Grammar = {
    val g = pData.getGrammar
    g.splitRules()
    g
  }
  lazy val lexicon: Lexicon = pData.getLexicon
}

/**
  * 한나눔 BerkeleyParserWrapper를 개량한 Class.
  * - Scala에 맞게 Logic 수정.
  * - 한나눔 품사분석결과에서 시작하도록 수정.
  * - 모델의 경로를 임시 디렉터리가 되도록 수정.
  *
  * 원본의 Copyright: KAIST 한나눔 개발팀.
  */
private[koala] class BerkeleyParserWrap(val grammarName: String) {
  val parser: CoarseToFineMaxRuleParser = {
    val p = new CoarseToFineMaxRuleParser(BerkeleyParserWrap.grammar, BerkeleyParserWrap.lexicon,
      BerkeleyParserWrap.opts.unaryPenalty, BerkeleyParserWrap.finalLevel,
      BerkeleyParserWrap.viterbiParse, false, false, BerkeleyParserWrap.opts.accurate,
      BerkeleyParserWrap.doVariational, BerkeleyParserWrap.useGoldPOS, true)
    p.binarization = BerkeleyParserWrap.pData.getBinarization
    p
  }
  private[this] val logger = org.log4s.getLogger

  def parseForced(data: Sentence): String = {
    val testSentence =
      try {
        MorphemeAnalyzerWrap synchronized {
          MorphemeAnalyzerWrap.getSpacedresult(data).asJava
        }
      } catch {
        case e: Throwable =>
          logger.error(e)("Synchronized Parsing Failed")
          System.exit(1)
          null.asInstanceOf
      }

    var parsedTree: Tree[String] = null
    val posTags = null.asInstanceOf[util.List[String]]
    val allowedStates = null.asInstanceOf[Array[Array[Array[Array[Boolean]]]]]

    parsedTree = this.parser.getBestConstrainedParse(testSentence, posTags, allowedStates)
    parsedTree = TreeAnnotations.unAnnotateTree(parsedTree, false)
    PennTreeRenderer.render(parsedTree)
  }
}
