package kr.bydelta.koala.traits

import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.util.SentenceSplitter

import scala.collection.JavaConverters._

/**
  * 품사분석기 Trait
  */
trait CanTag{
  /**
    * 주어진 String 문장을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문장 String
    * @return 품사분석 결과를 포함한 Sentence 객체
    */
  def tagSentence(text: String): Sentence

  /**
    * 주어진 String 문단을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @return 품사분석 결과를 문장단위로 잘라서 포함한 Seq[Sentence] 객체
    */
  def tag(text: String): Seq[Sentence]

  /**
    * 주어진 String 문단을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @return 품사분석 결과를 문장단위로 잘라서 포함한 Seq[Sentence] 객체
    */
  @deprecated("Use tag(String) instead. This will be removed at v2.0.0", "1.6.0")
  final def tagParagraph(text: String): Seq[Sentence] = tag(text)

  /**
    * (Java) 주어진 String 문단을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @return 품사분석 결과를 문장단위로 잘라서 포함한 `java.util.List<Sentence>` 객체
    */
  @deprecated("Use jTag(String) instead. This will be removed at v2.0.0", "2.0.0")
  final def jTagParagraph(text: String): java.util.List[Sentence] = jTag(text)

  /**
    * (Java) 주어진 String 문단을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @return 품사분석 결과를 문장단위로 잘라서 포함한 `java.util.List<Sentence>` 객체
    */
  final def jTag(text: String): java.util.List[Sentence] = tag(text).asJava
}

/**
  * 문장1개가 분석가능한 품사분석기 Trait
  *
  * @tparam S 분석기의 출력객체(문장)
  */
trait CanTagASentence[S] extends CanTag{
  /**
    * 주어진 String 문장을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문장 String
    * @return 품사분석 결과를 포함한 Sentence 객체
    */
  override final def tagSentence(text: String): Sentence = {
    val trim = text.trim
    if (trim.nonEmpty) convertSentence(tagSentenceOriginal(trim))
    else Sentence.empty
  }

  /**
    * 변환되지않은, 분석결과를 반환.
    *
    * @param text 분석할 String.
    * @return 원본 문단객체의 Sequence
    */
  def tagSentenceOriginal(text: String): S

  /**
    * 분석결과를 변환함.
    *
    * @param result 변환할 분석결과.
    * @return 변환된 Sentence 객체
    */
  private[koala] def convertSentence(result: S): Sentence
}

/**
  * 문단1개, 문장1개가 분석가능한 품사분석기 Trait
  *
  * @tparam S 분석기의 출력객체(문장)
  */
trait CanTagAParagraph[S] extends CanTagASentence[S]{
  /**
    * 주어진 String 문단을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @return 품사분석 결과를 문장단위로 잘라서 포함한 Seq[Sentence] 객체
    */
  override final def tag(text: String): Seq[Sentence] = {
    val trim = text.trim
    if (trim.nonEmpty) tagParagraphOriginal(trim).map(convertSentence)
    else Seq.empty
  }

  /**
    * 변환되지않은, 분석결과를 반환.
    *
    * @param text 분석할 String.
    * @return 원본 문단객체의 Sequence
    */
  def tagParagraphOriginal(text: String): Seq[S]
}

/**
  * 문단1개는 불가하지만, 문장1개가 분석가능한 품사분석기 Trait
  *
  * @tparam S 분석기의 출력객체(문장)
  */
trait CanTagOnlyASentence[S] extends CanTag{
  /**
    * 주어진 String 문장을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문장 String
    * @return 품사분석 결과를 포함한 Sentence 객체
    */
  override final def tagSentence(text: String): Sentence = {
    val trim = text.trim
    if (trim.nonEmpty) convertSentence(trim, tagSentenceOriginal(trim))
    else Sentence.empty
  }

  /**
    * 주어진 String 문단을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @return 품사분석 결과를 문장단위로 잘라서 포함한 Seq[Sentence] 객체
    */
  override final def tag(text: String): Seq[Sentence] = {
    val trim = text.trim
    if (trim.nonEmpty) SentenceSplitter(convertSentence(trim, tagSentenceOriginal(trim)))
    else Seq.empty
  }

  /**
    * 변환되지않은, 분석결과를 반환.
    *
    * @param text 분석할 String.
    * @return 원본 문단객체의 Sequence
    */
  def tagSentenceOriginal(text: String): S

  /**
    * 분석결과를 변환함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @param result 변환할 분석결과.
    * @return 변환된 Sentence 객체
    */
  private[koala] def convertSentence(text: String, result: S): Sentence
}

/**
  * 문장1개는 불가하지만, 문단1개가 분석가능한 품사분석기 Trait
  *
  * @tparam S 분석기의 출력객체(문장)
  */
trait CanTagOnlyAParagraph[S] extends CanTag{
  /**
    * 주어진 String 문장을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문장 String
    * @return 품사분석 결과를 포함한 Sentence 객체
    */
  override final def tagSentence(text: String): Sentence = {
    val trim = text.trim
    if (trim.nonEmpty) Sentence(tag(trim).flatten)
    else Sentence.empty
  }

  /**
    * 주어진 String 문단을 분석하여 품사를 부착함.
    *
    * @param text 품사분석을 시행할 문단 String
    * @return 품사분석 결과를 문장단위로 잘라서 포함한 Seq[Sentence] 객체
    */
  override final def tag(text: String): Seq[Sentence] = {
    val trim = text.trim
    if (trim.nonEmpty) tagParagraphOriginal(trim).map(convertSentence)
    else Seq.empty
  }

  /**
    * 변환되지않은, 분석결과를 반환.
    *
    * @param text 분석할 String.
    * @return 원본 문단객체의 Sequence
    */
  def tagParagraphOriginal(text: String): Seq[S]

  /**
    * 분석결과를 변환함.
    *
    * @param result 변환할 분석결과.
    * @return 변환된 Sentence 객체
    */
  private[koala] def convertSentence(result: S): Sentence
}
