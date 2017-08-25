package kr.bydelta.koala.test.core

import kr.bydelta.koala.kkma.{Dictionary, Tagger}

/**
  * Created by bydelta on 16. 7. 30.
  */
class KKMALearnSpec extends BasicWordLearnerSpecs {
  sequential

  override def getTagger = new Tagger
  override def getDict = Dictionary
}
