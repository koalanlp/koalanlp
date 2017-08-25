package kr.bydelta.koala.test.core

import kr.bydelta.koala.twt.{Dictionary, Tagger}

/**
  * Created by bydelta on 16. 7. 30.
  */
class TwitterLearnSpec extends BasicWordLearnerSpecs {
  sequential
  def getTagger = new Tagger
  def getDict = Dictionary
}
