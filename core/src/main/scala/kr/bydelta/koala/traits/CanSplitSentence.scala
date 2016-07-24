package kr.bydelta.koala.traits

/**
  * Created by bydelta on 16. 7. 23.
  */
trait CanSplitSentence {
  def sentences(text: String): Seq[String]
}
