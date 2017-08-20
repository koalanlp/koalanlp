package kr.bydelta.koala.traits

import kr.bydelta.koala.data.Sentence

import scala.collection.JavaConverters._

/**
  * 품사분석기 Trait
  *
  * @tparam S 분석기의 출력객체(문장)
  */
trait CanTag[S] {
  /**
    * 주어진 String 문장을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문장 String
    * @return 품사분석 결과를 포함한 Sentence 객체
    */
  final def tagSentence(text: String): Sentence = Sentence(tag(text).flatten)

  /**
    * 주어진 String 문단을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @return 품사분석 결과를 문장단위로 잘라서 포함한 Seq[Sentence] 객체
    */
  @deprecated("Use tag(String) instead.", "2.0.0")
  final def tagParagraph(text: String): Seq[Sentence] = tag(text)

  /**
    * 주어진 String 문단을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @return 품사분석 결과를 문장단위로 잘라서 포함한 Seq[Sentence] 객체
    */
  final def tag(text: String, forceSingleSentence: Boolean = false): Seq[Sentence] =
  tagParagraphRaw(text).map(convert)

  /**
    * (Java) 주어진 String 문단을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @return 품사분석 결과를 문장단위로 잘라서 포함한 `java.util.List<Sentence>` 객체
    */
  @deprecated("Use jTag(String) instead.", "2.0.0")
  final def jTagParagraph(text: String): java.util.List[Sentence] = jTag(text)

  /**
    * (Java) 주어진 String 문단을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @return 품사분석 결과를 문장단위로 잘라서 포함한 `java.util.List<Sentence>` 객체
    */
  final def jTag(text: String): java.util.List[Sentence] = tag(text).asJava

  /**
    * 변환되지않은, 분석결과를 반환.
    *
    * @param text 분석할 String.
    * @return 원본 문장객체.
    */
  @deprecated("Use tagParagraphRaw(String) instead.", "2.0.0")
  final def tagSentenceRaw(text: String): S = tagParagraphRaw(text).head

  /**
    * 변환되지않은, 분석결과를 반환.
    *
    * @param text 분석할 String.
    * @return 원본 문단객체의 Sequence
    */
  def tagParagraphRaw(text: String): Seq[S]

  /**
    * 분석결과를 변환함.
    *
    * @param result 변환할 분석결과.
    * @return 변환된 Sentence 객체
    */
  private[koala] def convert(result: S): Sentence
}

