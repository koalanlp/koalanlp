package kr.bydelta.koala.test.core

import kr.bydelta.koala.eunjeon.{Dictionary, Tagger}
import kr.bydelta.koala.util.SimpleWordLearner

import scala.collection.JavaConverters._

/**
  * Created by bydelta on 16. 7. 30.
  */
class EunjeonLearnSpec extends BasicWordLearnerSpecs {
  sequential

  "SimpleWordLearner" should {
    lazy val learner = {
      Dictionary.rawDict = Set()
      Dictionary.isDicChanged = true
      Dictionary.reloadDic()
      new SimpleWordLearner(Dictionary)
    }

    "extract all nouns" in {
      val level0 = learner.extractNouns(text.toIterator, minOccurrence = 2, minVariations = 2)
      val level2 = learner.extractNouns(text.toIterator, minOccurrence = 5, minVariations = 3)

      level0.size must be_>=(level2.size)
      level0 must not(containAnyOf(EXCLUDED_SET))
      level0 must containAllOf(INCLUDED_SET)
      level2 must containAllOf(INCLUDED_SET_2)
    }

    "learn all nouns" in {
      val tagger1 = getTagger
      val beforeLearn = text.map(s => tagger1.tagSentence(s).singleLineString).mkString("\n")

      learner.jLearn(text.toIterator.asJava, minOccurrence = 1, minVariations = 1)

      val tagger2 = getTagger
      val afterLearn = text.map(s => tagger2.tagSentence(s).singleLineString).mkString("\n")

      beforeLearn must_!= afterLearn
    }
  }

  override def getTagger = new Tagger
  override def getDict = Dictionary
}
