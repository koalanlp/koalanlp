package kr.bydelta.koala.traits

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.data.Sentence

trait CanDepParse {
  @throws[Exception]
  def parse(sentence: String): Sentence

  def parse(sentence: Sentence): Sentence

  def addUserDictionary(dict: (String, POSTag)*)

  def addUserDictionary(morph: String, tag: POSTag)
}

