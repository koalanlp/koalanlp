package kr.bydelta.koala.kkma

import kr.bydelta.koala.POS._
import kr.bydelta.koala.Processor
import kr.bydelta.koala.data.{Sentence => KSent}
import kr.bydelta.koala.traits.CanDepParse
import org.snu.ids.ha.ma.{Eojeol, MCandidate, MExpression, Sentence}
import org.snu.ids.ha.sp.{Parser => KParser}

import scala.collection.JavaConversions._

class Parser extends CanDepParse {
  lazy val parser = KParser.getInstance()
  lazy val tagger = new Tagger

  @throws[Exception]
  def parse(sentence: String): KSent = {
    val rawSentence: Sentence = tagger.analyzeSentenceRaw(sentence).head
    parseRaw(rawSentence)
  }

  private def parseRaw(rawSentence: Sentence): KSent = {
    val tagged = tagger.parseResult(rawSentence)
    val parse = parser.parse(rawSentence)
    val edgeList = parse.getEdgeList
    val nodeList = parse.getNodeList

    edgeList.foreach {
      e =>
        val from: Int = rawSentence.indexOf(nodeList.get(e.getFromId).getEojeol)
        val to: Int = rawSentence.indexOf(nodeList.get(e.getToId).getEojeol)
        if (from != -1 && to != -1) {
          val thisWord = tagged(to)
          val headWord = tagged(from)
          val rawTag = e.getRelation
          val tag = Processor.KKMA dependencyOf rawTag
          headWord.addDependant(thisWord, tag, rawTag)
        } else if (to != -1) {
          tagged.root = to
        }
    }
    tagged
  }

  override def parse(sentence: KSent): KSent = {
    parseRaw(deparse(sentence))
  }

  def deparse(result: KSent): Sentence = {
    val sent = new Sentence
    result.foreach {
      word =>
        val mexp = new MExpression(word.originalWord,
          new MCandidate(word.head.morpheme, Processor.KKMA originalPOSOf word.head.tag))
        word.tail.foreach {
          morph =>
            mexp.add(new MCandidate(morph.morpheme, Processor.KKMA originalPOSOf morph.tag))
        }
        sent.add(new Eojeol(mexp))
    }
    sent
  }

  def addUserDictionary(dict: (String, POSTag)*) {
    Dictionary.addUserDictionary(dict: _*)
  }

  def addUserDictionary(morph: String, tag: POSTag) {
    Dictionary.addUserDictionary(morph, tag)
  }
}
