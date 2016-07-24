package kr.bydelta.koala

/**
  * 분석기의 종류
  */
object Processor extends Enumeration {
  /** 분석기 타입 **/
  type Processor = Value
  /** 한나눔 분석기 **/
  val Hannanum = Value
  /** 꼬꼬마 분석기 **/
  val KKMA = Value
  /** 트위터 분석기 **/
  val Twitter = Value
  /** 은전한닢 분석기 **/
  val Eunjeon = Value
  /** 코모란 분석기 **/
  val Komoran = Value
}
