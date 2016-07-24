package kr.bydelta.koala.kkma

import kr.bydelta.koala.POS._
import kr.bydelta.koala.Processor
import kr.bydelta.koala.data.{Sentence => KSent}
import kr.bydelta.koala.traits.CanDepParse
import org.snu.ids.ha.ma.{Eojeol, MCandidate, MExpression, Sentence}
import org.snu.ids.ha.sp.{Parser => KParser}

import scala.collection.JavaConversions._

/**
  * 꼬꼬마 의존구문분석기.
  */
class Parser extends CanDepParse {
  /** 의존관계분석기 **/
  lazy val parser = new KParser()
  /** 형태소분석기 **/
  lazy val tagger = new Tagger

  override def parse(sentence: String): KSent = {
    val rawSentence: Sentence = tagger.tagParagraphRaw(sentence).head
    val tagged = tagger.convert(rawSentence)
    parseRaw(rawSentence, tagged)
  }

  /**
    * 꼬꼬마 형태소분석 결과를 토대로 의존구문분석 진행
    *
    * @param rawSentence 분석할 문장 (꼬꼬마 문장)
    * @param tagged      분석 결과를 입력할 문장 (통합 문장)
    * @return 분석된 결과 (통합 문장).
    */
  private def parseRaw(rawSentence: Sentence, tagged: KSent): KSent = {
    rawSentence.zipWithIndex.foreach {
      case (e, idx) => e.getFirstMorp.setIndex(idx)
    }
    val morphIdxs = rawSentence.indices
    val parse = parser.parse(rawSentence)
    val edgeList = parse.getEdgeList
    val nodeList = parse.getNodeList

    edgeList.foreach {
      e =>
        val from: Int =
          nodeList.get(e.getFromId).getEojeol match {
            case null => -1
            case x => morphIdxs.indexOf(x.getStartIndex)
          }
        val to: Int =
          nodeList.get(e.getToId).getEojeol match {
            case null => -1
            case x => morphIdxs.indexOf(x.getStartIndex)
          }
        if (from != -1 && to != -1) {
          val thisWord = tagged.get(to)
          val headWord = tagged.get(from)
          val rawTag = e.getRelation
          val tag = Processor.KKMA dependencyOf rawTag
          headWord.addDependant(thisWord, tag, rawTag)
        } else if (to != -1) {
          val thisWord = tagged.get(to)
          tagged.topLevels += thisWord
          thisWord.rawDepTag = e.getRelation
          thisWord.depTag = Processor.KKMA dependencyOf e.getRelation
        }
    }
    tagged
  }

  override def parse(sentence: KSent): KSent = {
    parseRaw(deParse(sentence), sentence)
  }

  /**
    * 통합 분석결과를 꼬꼬마 분석결과로 복원.
    *
    * @param result 복원할 통합 분석결과.
    * @return 복원된 문장.
    */
  private[koala] def deParse(result: KSent): Sentence = {
    val sent = new Sentence
    result.foreach {
      word =>
        val mexp = new MExpression(word.surface,
          new MCandidate(word.head.surface, Processor.KKMA originalPOSOf word.head.tag))
        word.tail.foreach {
          morph =>
            mexp.add(new MCandidate(morph.surface, Processor.KKMA originalPOSOf morph.tag))
        }
        sent.add(new Eojeol(mexp))
    }
    sent
  }

  override def addUserDictionary(dict: (String, POSTag)*) {
    Dictionary.addUserDictionary(dict: _*)
  }

  override def addUserDictionary(morph: String, tag: POSTag) {
    Dictionary.addUserDictionary(morph, tag)
  }
}
