package kr.bydelta.koala.helper

import java.io.File
import java.util

import edu.berkeley.nlp.PCFGLA._
import edu.berkeley.nlp.syntax.Tree
import edu.berkeley.nlp.syntax.Trees.PennTreeRenderer
import edu.berkeley.nlp.util.Numberer
import kaist.cilab.jhannanum.common.communication.Sentence
import kr.bydelta.koala.hnn.Dictionary

import scala.collection.JavaConverters._

/**
  * 한나눔 BerkeleyParserWrapper를 개량한 Class.
  * - Scala에 맞게 Logic 수정.
  * - 한나눔 품사분석결과에서 시작하도록 수정.
  * - 모델의 경로를 임시 디렉터리가 되도록 수정.
  *
  * 원본의 Copyright: KAIST 한나눔 개발팀.
  *
  * @param grammarName 문법의 유형.
  */
private[koala] class BerkeleyParserWrap(val grammarName: String) {
  val parser = {
    val args: String = "-in " + grammarName
    val optParser: OptionParser = new OptionParser(classOf[GrammarTester.Options])
    val opts: GrammarTester.Options = optParser.parse(args.split(" "), true).asInstanceOf[GrammarTester.Options]
    val inFileName: String = Dictionary.extractResource() + File.separator + opts.inFileName
    System.out.println("Loading grammar from " + inFileName + ".")
    val finalLevel: Int = opts.finalLevel
    val viterbiParse: Boolean = opts.viterbi
    val doVariational: Boolean = false
    val useGoldPOS: Boolean = opts.useGoldPOS
    val pData: ParserData = ParserData.Load(inFileName)
    if (pData == null) {
      System.out.println("Failed to load grammar from file" + inFileName + ".")
      System.exit(1)
    }
    val grammar: Grammar = pData.getGrammar
    grammar.splitRules()
    val lexicon: Lexicon = pData.getLexicon
    Numberer.setNumberers(pData.getNumbs)

    val p = new CoarseToFineMaxRuleParser(grammar, lexicon, opts.unaryPenalty,
      finalLevel, viterbiParse, false, false, opts.accurate, doVariational, useGoldPOS, true)
    p.binarization = pData.getBinarization
    p
  }

  def parseForced(data: Sentence): String = {
    val testSentence =
      try {
        MorphemeAnalyzerWrap.getSpacedresult(data).asJava
      } catch {
        case e: Throwable =>
          e.printStackTrace()
          System.exit(1)
          null.asInstanceOf
      }

    var parsedTree: Tree[String] = null
    val posTags: Any = null
    val allowedStates = null.asInstanceOf[Array[Array[Array[Array[Boolean]]]]]

    parsedTree = this.parser.getBestConstrainedParse(testSentence,
      posTags.asInstanceOf[util.List[String]], allowedStates)
    parsedTree = TreeAnnotations.unAnnotateTree(parsedTree, false)
    PennTreeRenderer.render(parsedTree)
  }
}
