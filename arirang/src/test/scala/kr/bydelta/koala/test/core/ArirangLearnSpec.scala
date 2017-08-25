package kr.bydelta.koala.test.core

import kr.bydelta.koala.arirang.{Dictionary, Tagger}

/**
  * Created by bydelta on 16. 7. 30.
  */
class ArirangLearnSpec extends BasicWordLearnerSpecs {
  sequential

  def getTagger = new Tagger

  def getDict = Dictionary
}
