package kr.bydelta.koala.kmr

import kr.bydelta.koala.util.BasicWordLearnerSpecs
import kr.co.shineware.util.common.model.Pair
import org.specs2.mutable.Specification

import scala.collection.JavaConverters._

/**
  * Created by bydelta on 16. 7. 30.
  */
class UnkLearnSpec extends Specification with BasicWordLearnerSpecs[java.util.List[java.util.List[Pair[String, String]]]] {
  override def getTagger = new Tagger
  override def getDict = Dictionary

  "BasicWordLearner" should {
    "extract all nouns" in {
      val level0 = learner.extractNouns(text.toIterator, minOccurrence = 1, minVariations = 1)
      val level2 = learner.extractNouns(text.toIterator, minOccurrence = 2, minVariations = 2)

      level0.size must be_>(level2.size)
      level0 must not(containAnyOf(EXCLUDED_SET))
      level0 must containAllOf(INC_1._1)
      level0 must not(containAllOf(INC_1._2))
      level2 must containAllOf(INC_2._1)
      level2 must not(containAllOf(INC_2._2))
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
}
