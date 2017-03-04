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
  def tagSentence(text: String): Sentence = convert(tagSentenceRaw(text))

  /**
    * 주어진 String 문단을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @return 품사분석 결과를 문장단위로 잘라서 포함한 Seq[Sentence] 객체
    */
  def tagParagraph(text: String): Seq[Sentence]

  /**
    * (Java) 주어진 String 문단을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @return 품사분석 결과를 문장단위로 잘라서 포함한 `java.util.List<Sentence>` 객체
    */
  def jTagParagraph(text: String): java.util.List[Sentence] =
  tagParagraph(text).asJava


  /**
    * 변환되지않은, 분석결과를 반환.
    *
    * @param text 분석할 String.
    * @return 원본 문장객체.
    */
  def tagSentenceRaw(text: String): S

  /**
    * 분석결과를 변환함.
    *
    * @param result 변환할 분석결과.
    * @return 변환된 Sentence 객체
    */
  private[koala] def convert(result: S): Sentence
}

