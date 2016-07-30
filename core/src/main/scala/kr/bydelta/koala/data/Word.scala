package kr.bydelta.koala.data

/**
  * Created by bydelta on 16. 7. 20.
  */

import kr.bydelta.koala.FunctionalTag
import kr.bydelta.koala.FunctionalTag.FunctionalTag
import kr.bydelta.koala.POS.POSTag

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

/**
  * 어절 Class
  *
  * @param surface   어절의 표면형 String.
  * @param morphemes 어절에 포함된 형태소의 목록 Seq[Morpheme].
  */
class Word(val surface: String, val morphemes: collection.Seq[Morpheme]) extends Iterable[Morpheme] {
  /**
    * 현재 어절에 의존하는 의존소의 목록. 즉, 현재 어절의 핵심 의미를 보조하거나, 보강하는 단어들.
    */
  val dependents = ArrayBuffer[Word]()
  /**
    * 현재 어절에 포함된, 체언 또는 용언의 수
    */
  val numOfMeaningful: Int = morphemes.count {
    m => m.isNoun || m.isVerb
  }
  /**
    * 의존관계 분석기가 생성한 원본 관계명칭.
    */
  var rawDepTag: String = _
  /**
    * 통합, 표준화된 관계명칭.
    */
  var depTag: FunctionalTag = FunctionalTag.Undefined

  /**
    * 어절의 첫번째 형태소.
    *
    * @return 첫 형태소 Morpheme 객체
    */
  override final def head = morphemes.head

  /**
    * 어절의 마지막 형태소.
    *
    * @return 끝 형태소 Morpheme 객체
    */
  override final def last = morphemes.last

  /**
    * (Java) 현재 어절에 의존하는 의존소의 목록. 즉, 현재 어절의 핵심 의미를 보조하거나, 보강하는 단어들.
    *
    * @return 의존소 목록 `java.util.List<Word>`
    */
  def jDependents = dependents.asJava

  /**
    * (Java) 현재 어절에 포함된 형태소의 목록
    *
    * @return 형태소 목록 `java.util.List<Morpheme>`
    */
  def jMorphemes = morphemes.asJava

  /**
    * 주어진 품사 표기의 Sequence를 포함하는지 확인.
    * <br/>
    * `Seq[POSTag]`의 형태이며, 이는 품사가 어절을 구성한 형태를 따른 것임.
    * <br/>
    * Sequence가 *연속되지 않더라도* 확인함. 즉, "초/XP거대하/VAㄴ/ETM"이란 어절이 있다면,
    * `Seq(POS.XP,POS.ETM)`는 중간 형태소에 대응하는 품사가 없지만, 순서는 포함되므로,
    * `true`를 반환함.
    *
    * @param tag 확인할 통합 품사 표기의 Sequence. `Seq[POSTag]` 객체.
    * @return True: 존재하는 경우
    */
  final def matches(tag: Seq[String]): Boolean =
  tag.foldLeft(true) {
    case (true, t) =>
      morphemes.exists(_.hasTag(t))
    case (false, _) =>
      false
  }

  /**
    * (Java) 주어진 품사 표기의 Sequence를 포함하는지 확인.
    * <br/>
    * `POS$.Value[]`의 형태이며, 이는 품사가 어절을 구성한 형태를 따른 것임.
    * <br/>
    * Sequence가 *연속되지 않더라도* 확인함. 즉, "초/XP거대하/VAㄴ/ETM"이란 어절이 있다면,
    * `{POS.XP(),POS.ETM()}`는 중간 형태소에 대응하는 품사가 없지만, 순서는 포함되므로,
    * `true`를 반환함.
    *
    * @param tag 확인할 통합 품사 표기의 Sequence. `POS$.Value[]` 객체.
    * @return True: 존재하는 경우
    */
  final def matches(tag: Array[String]): Boolean =
  tag.foldLeft(true) {
    case (true, t) =>
      morphemes.exists(_.hasTag(t))
    case (false, _) =>
      false
  }

  /**
    * 주어진 품사를 갖는 형태소가 존재하는지 확인.
    * <br/>
    * 예를 들어, N은 체언인지 확인하고, NP는 대명사인지 확인.
    * 품사 표기는 [[https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing 여기]]
    * 에서 확인
    *
    * @param tag 확인할 통합 품사 표기
    * @return True: 존재하는 경우.
    */
  final def existsMorpheme(tag: String) = morphemes.exists(_.hasTag(tag))

  /**
    * 주어진 품사를 갖는 형태소가 존재하는지 확인.
    * <br/>
    * 품사 표기는 [[https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing 여기]]
    * 에서 확인
    *
    * @param tag 확인할 통합 품사 표기들
    * @return True: 존재하는 경우.
    */
  final def existsMorpheme(tag: Seq[POSTag]) = morphemes.exists(_.hasTag(tag))

  /**
    * 주어진 위치의 형태소를 반환. Morpheme 객체.
    *
    * @param index 형태소의 위치.
    * @return 형태소가 있다면, 형태소를, 없다면 Exception.
    */
  final def get(index: Int): Morpheme = morphemes(index)

  /**
    * 주어진 위치의 형태소를 반환. Option[Morpheme] 객체.
    *
    * @param index 형태소의 위치.
    * @return 형태소가 있다면, Some(형태소)를, 없다면 None.
    */
  final def apply(index: Int): Option[Morpheme] =
  if (index < 0 || index >= size) None
  else Some(morphemes(index))

  /**
    * 어절에 포함된 형태소의 수.
    *
    * @return 형태소의 수.
    */
  override final def size: Int = morphemes.size

  /**
    * 주어진 품사의 형태소를 반환. Morpheme 객체.
    *
    * @param tag 형태소의 품사
    * @return 형태소가 있다면, 형태소를, 없다면 Exception.
    */
  final def get(tag: String): Morpheme = apply(tag).get

  /**
    * 주어진 품사의 형태소를 반환. Option[Morpheme] 객체.
    *
    * @param tag 형태소의 품사.
    * @return 형태소가 있다면, Some(형태소)를, 없다면 None.
    */
  final def apply(tag: String): Option[Morpheme] = morphemes.find(_.hasTag(tag))

  /**
    * 주어진 형태소의 다음 형태소를 반환.
    *
    * @param m 다음 형태소를 가져올 형태소 객체.
    * @return 형태소가 있다면, Some(형태소)를, 없다면 None.
    */
  final def getNextOf(m: Morpheme): Option[Morpheme] = {
    val index = morphemes.indexOf(m) + 1
    if (index < morphemes.size) Some(morphemes(index))
    else None
  }

  /**
    * 주어진 형태소의 이전 형태소를 반환.
    *
    * @param m 이전 형태소를 가져올 형태소 객체.
    * @return 형태소가 있다면, Some(형태소)를, 없다면 None.
    */
  final def getPrevOf(m: Morpheme): Option[Morpheme] = {
    val index: Int = morphemes.indexOf(m) - 1
    if (index > -1) Some(morphemes(index))
    else None
  }

  /**
    * 표면형이 같은지 비교함.
    *
    * @param another 표면형을 비교할 다른 어절 Word 객체.
    * @return True: 표면형이 같을 경우.
    */
  def equalsWithoutTag(another: Word): Boolean = another.surface == this.surface

  /**
    * 구문분석과 품사분석의 결과를 String으로 변환.
    *
    * @return 본 객체의 정보를 담은 String.
    */
  override def toString: String = {
    val prefix = s"$surface\t= " + morphemes.mkString("")
    if (dependents.isEmpty) prefix
    else {
      prefix + "\n\t의존관계:" + dependents.map {
        w =>
          s"${w.surface}(${w.depTag}/${w.rawDepTag})"
      }.mkString(" ")
    }
  }

  /**
    * 품사분석 결과를, 1행짜리 String으로 변환.
    *
    * @return 품사분석 결과를 담은 1행짜리 String.
    */
  def singleLineString: String =
  morphemes.map {
    morph =>
      s"${morph.surface}/${morph.tag}"
  }.mkString("+")

  /**
    * (Java) 형태소를 문장의 순서대로 순회하는 iterator.
    *
    * @return 형태소 순회 Iterator.
    */
  def jIterator = iterator.asJava

  /**
    * 형태소를 문장의 순서대로 순회하는 iterator.
    *
    * @return 형태소 순회 Iterator.
    */
  def iterator: Iterator[Morpheme] = morphemes.iterator

  /**
    * 어절에 의존하는 새로운 의존소 추가.
    *
    * @param word   의존소가 될 어절 Word 객체.
    * @param tag    의존관계의 유형.
    * @param rawTag 의존관계의 원본 명칭.
    */
  private[koala] final def addDependant(word: Word, tag: FunctionalTag, rawTag: String) {
    if (!dependents.contains(word)) {
      word.depTag = tag
      word.rawDepTag = rawTag
      dependents += word
    }
  }
}