package kr.bydelta.koala.helper

import kaist.cilab.jhannanum.common.Eojeol

class MarkovNode(var eojeol: Eojeol, var tag: String, var ptProb: Double) {
  var cmProb: Double = .0
  var backward: MarkovNode = null
  var sibling: MarkovNode = null
}