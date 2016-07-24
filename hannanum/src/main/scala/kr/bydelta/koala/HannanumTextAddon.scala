package kr.bydelta.koala

/**
  * 한나눔 분석기의 문장처리 Addon 종류.
  */
object HannanumTextAddon extends Enumeration {
  /** Addon 타입 **/
  type HannanumTextAddon = Value
  /** 문장나누기 **/
  val SentenceSegment = Value
  /** 비격식문장 필터 **/
  val InformalSentenceFilter = Value
}
