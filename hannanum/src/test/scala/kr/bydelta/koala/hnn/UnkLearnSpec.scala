package kr.bydelta.koala.hnn

import kaist.cilab.jhannanum.common.communication.Sentence
import kr.bydelta.koala.data.BasicWordLearnerSpecs
import org.specs2.execute.Result
import org.specs2.mutable.Specification

import scala.collection.JavaConverters._

/**
  * Created by bydelta on 16. 7. 30.
  */
class UnkLearnSpec extends Specification with BasicWordLearnerSpecs[Sentence] {
  sequential

  override def getTagger = new Tagger
  override def getDict = Dictionary

  "BasicWordLearner" should {
    var lv0Empty = false

    "extract all nouns" in {
      val level0 = learner.extractNouns(text.toIterator, minOccurrence = 2, minVariations = 2)
      val level2 = learner.extractNouns(text.toIterator, minOccurrence = 5, minVariations = 3)

      lv0Empty = level0.isEmpty
      level0.size must be_>=(level2.size)
      level0 must not(containAnyOf(EXCLUDED_SET))
      level0 must containAllOf(INC_1._1)
      level0 must not(containAnyOf(INC_1._2))
      level2 must containAllOf(INC_2._1)
      level2 must not(containAnyOf(INC_2._2))
    }

    "learn all nouns" in {
      Result.unit {
        if (!lv0Empty) {
          val tagger1 = getTagger
          val beforeLearn = text.map(s => tagger1.tagSentence(s).singleLineString).mkString("\n")

          learner.jLearn(text.toIterator.asJava, minOccurrence = 1, minVariations = 1)

          val tagger2 = getTagger
          val afterLearn = text.map(s => tagger2.tagSentence(s).singleLineString).mkString("\n")

          beforeLearn must_!= afterLearn
        }
      }
    }
  }
}
