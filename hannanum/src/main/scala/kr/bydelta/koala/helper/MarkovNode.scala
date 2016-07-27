package kr.bydelta.koala.helper

import kaist.cilab.jhannanum.common.Eojeol

/**
  * 한나눔의 MarkovNode 클래스를, Scala에 맞게 수정.
  *
  * 원본의 Copyright: KAIST 한나눔 개발팀.
  *
  * @param eojeol 어절
  * @param tag    품사
  * @param ptProb 전이확률값
  */
private[koala] class MarkovNode(var eojeol: Eojeol, var tag: String, var ptProb: Double) {
  /** 출력확률값 **/
  var cmProb: Double = .0
  /** 뒷 노드 **/
  var backward: MarkovNode = _
  /** 형제 노드 **/
  var sibling: MarkovNode = _
}