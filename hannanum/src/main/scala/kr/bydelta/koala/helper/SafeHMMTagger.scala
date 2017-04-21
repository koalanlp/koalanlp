package kr.bydelta.koala.helper

import java.io.File

import kaist.cilab.jhannanum.common.communication.{Sentence, SetOfSentences}
import kaist.cilab.jhannanum.common.{Eojeol, JSONReader}
import kaist.cilab.jhannanum.plugin.major.postagger.PosTagger
import kaist.cilab.jhannanum.postagger.hmmpostagger.{PhraseTag, ProbabilityDBM}
import kaist.cilab.parser.berkeleyadaptation.Configuration
import kr.bydelta.koala.hnn.Dictionary

import scala.annotation.tailrec
import scala.collection.JavaConverters._
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * HMMTagger의 확률 저장공간.
  */
private[koala] object SafeHMMTagger {
  private var pwtPOS: ProbabilityDBM = _
  private var pttPOS: ProbabilityDBM = _
  private var pttWP: ProbabilityDBM = _
}

/**
  * 한나눔의 HMMTagger를 Scala에 맞게 개량한 버전.
  *
  * 원본의 Copyright: KAIST 한나눔 개발팀.
  */
private[koala] class SafeHMMTagger extends PosTagger {
  private final val SF = -4.60517018598809136803598290936873
  private final val PCONSTANT = -20.0
  private val wordPts = ArrayBuffer[MarkovNode]()
  private val markovNet = ListBuffer[MarkovNode]()

  def tagPOS(sos: SetOfSentences): Sentence = {
    val eojeolSetArray = sos.getEojeolSetArray
    reset()

    eojeolSetArray.asScala.foreach {
      _.foldLeft(None.asInstanceOf[Option[MarkovNode]]) {
        case (opt, e) =>
          val now_tag: String = PhraseTag.getPhraseTag(e.getTags)
          val probability: Double = computeWT(e)
          val node: MarkovNode = new MarkovNode(e, now_tag, probability)
          markovNet += node
          opt match {
            case Some(prev) => prev.sibling = node
            case None => wordPts += node
          }
          Some(node)
      }
    }

    endSentence(sos)
  }

  def shutdown() {
  }

  @throws[Exception]
  def initialize(configFile: String) {
    initialize(configFile, Configuration.hanBaseDir)
  }

  @throws[Exception]
  def initialize(configFile: String, _dummy: String) {
    val baseDir = Dictionary.extractResource()
    wordPts.clear
    markovNet.clear
    if (SafeHMMTagger.pttPOS == null) {
      val json: JSONReader = new JSONReader(configFile)
      val PWT_POS_TDBM_FILE: String = baseDir + File.separator + json.getValue("pwt.pos")
      val PTT_POS_TDBM_FILE: String = baseDir + File.separator + json.getValue("ptt.pos")
      val PTT_WP_TDBM_FILE: String = baseDir + File.separator + json.getValue("ptt.wp")
      SafeHMMTagger.pwtPOS = new ProbabilityDBM(PWT_POS_TDBM_FILE)
      SafeHMMTagger.pttWP = new ProbabilityDBM(PTT_WP_TDBM_FILE)
      SafeHMMTagger.pttPOS = new ProbabilityDBM(PTT_POS_TDBM_FILE)
    }
  }

  private def computeWT(eojeol: Eojeol): Double = {
    def getProb(tag: String = "bnk", morpheme: String = "", prevTag: String = "bnk") = {
      val tbigram = SafeHMMTagger.pttPOS.get(s"$prevTag-$tag") match {
        case arr: Array[Double] => arr(0)
        case _ => PCONSTANT
      }
      val tunigram = SafeHMMTagger.pttPOS.get(tag) match {
        case arr: Array[Double] => arr(0)
        case _ => PCONSTANT
      }
      val lexicon =
        if (morpheme.isEmpty) 0
        else SafeHMMTagger.pwtPOS.get(morpheme + "/" + tag) match {
          case arr: Array[Double] => arr(0)
          case _ => PCONSTANT
        }
      lexicon + tbigram - tunigram
    }

    val init = getProb(tag = eojeol.getTag(0), morpheme = eojeol.getMorpheme(0))
    eojeol.getTags.zip(eojeol.getMorphemes).sliding(2).foldLeft(init) {
      case (prevProb, Array((prevTag, _), (tag, morpheme))) =>
        prevProb + getProb(prevTag = prevTag, tag = tag, morpheme = morpheme)
      case (prevProb, _) => prevProb
    } + getProb(prevTag = eojeol.getTags.last)
  }

  @tailrec
  private def traverseCurrent(prev: MarkovNode, curr: MarkovNode): Unit =
    if (curr != null) {
      updateProbability(prev, curr)
      traverseCurrent(prev, curr.sibling)
    }

  @tailrec
  private def traverse(prev: MarkovNode, curr: MarkovNode): Unit =
    if (prev != null) {
      traverseCurrent(prev, curr)
      traverse(prev.sibling, curr)
    }

  @tailrec
  private def getEojeols(curr: MarkovNode, buf: ArrayBuffer[Eojeol] = ArrayBuffer()): ArrayBuffer[Eojeol] =
    if (curr != null) {
      buf += curr.eojeol
      getEojeols(curr.backward, buf)
    } else
      buf.reverse

  private def endSentence(sos: SetOfSentences): Sentence = {
    wordPts.sliding(2).foreach {
      case ArrayBuffer(prev, curr) =>
        traverse(prev, curr)
      case _ =>
    }

    val eojeols = getEojeols(wordPts.last)
    val s = new Sentence(sos.getDocumentID, sos.getSentenceID, sos.isEndOfDocument,
      sos.getPlainEojeolArray.toArray(new Array[String](0)), eojeols.toArray)
    s.length = eojeols.size
    s
  }

  private def reset() {
    wordPts.clear
    markovNet.clear
  }

  private def updateProbability(prev: MarkovNode, curr: MarkovNode) {
    var P: Double = .0
    val PTT = (SafeHMMTagger.pttWP.get(prev.tag + "-" + curr.tag) match {
      case arr: Array[Double] => arr(0)
      case _ => SF
    }) - (SafeHMMTagger.pttWP.get(curr.tag) match {
      case arr: Array[Double] => arr(0)
      case _ => 0.0
    })

    if (prev.backward == null) {
      prev.cmProb = prev.ptProb
    }
    P = prev.cmProb + PTT + curr.ptProb
    if (curr.backward == null || P > curr.cmProb) {
      curr.backward = prev
      curr.cmProb = P
    }
  }
}
