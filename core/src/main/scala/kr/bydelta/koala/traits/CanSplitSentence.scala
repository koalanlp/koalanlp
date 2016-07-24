package kr.bydelta.koala.traits

import scala.collection.JavaConverters._

/**
  * 문장분리기 Trait
  */
trait CanSplitSentence {
  /**
    * 주어진 문단을 문장단위로 분리함.
    *
    * @param text 문장단위로 분리할 String.
    * @return 문장단위로 분리된 String의 Sequence.
    */
  def sentences(text: String): Seq[String]

  /**
    * (Java) 주어진 문단을 문장단위로 분리함.
    *
    * @param text 문장단위로 분리할 String.
    * @return 문장단위로 분리된 String의 Sequence.
    */
  def jSentences(text: String): java.util.List[String] =
  sentences(text).asJava
}
