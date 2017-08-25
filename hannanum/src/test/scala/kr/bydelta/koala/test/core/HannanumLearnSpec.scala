package kr.bydelta.koala.test.core

import kr.bydelta.koala.hnn.{Dictionary, Tagger}

/**
  * Created by bydelta on 16. 7. 30.
  */
class HannanumLearnSpec extends BasicWordLearnerSpecs {
  sequential

  override def getTagger = new Tagger
  override def getDict = Dictionary
}
