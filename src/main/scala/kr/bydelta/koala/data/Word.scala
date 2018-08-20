package kr.bydelta.koala.data

import scala.collection.JavaConverters._
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable.ArrayBuffer
import scala.collection.{IndexedSeqLike, mutable}

/**
  * 어절 Class
  *
  * @param id        문장 내에서 어절의 위치
  * @param surface   어절의 표면형 String.
  * @param morphemes 어절에 포함된 형태소의 목록 Seq[Morpheme].
  */
@SerialVersionUID(1080201L)
final class Word private(val id: Int, val surface: String, val morphemes: Vector[Morpheme])
  extends IndexedSeq[Morpheme] with IndexedSeqLike[Morpheme, Word] with Serializable {

  override def length: Int = morphemes.length

  override def apply(idx: Int): Morpheme = morphemes(idx)

  /**
    * (Java) 주어진 품사 표기의 Sequence를 포함하는지 확인.
    * <br/>
    * `POS$.Value[]`의 형태이며, 이는 품사가 어절을 구성한 형태를 따른 것임.
    * <br/>
    * Sequence가 *연속되지 않더라도* 확인함. 즉, "초/XPN거대하/VAㄴ/ETM"이란 어절이 있다면,
    * `{POS.XPN(),POS.ETM()}`는 중간 형태소에 대응하는 품사가 없지만, 순서는 포함되므로,
    * `true`를 반환함.
    *
    * @param tag 확인할 통합 품사 표기의 Sequence. `POS$.Value[]` 객체.
    * @return True: 존재하는 경우
    */
  def matches(tag: Array[String]): Boolean = matches(tag.toSeq)

  /**
    * 주어진 품사 표기의 Sequence를 포함하는지 확인.
    * <br/>
    * `Seq[POSTag]`의 형태이며, 이는 품사가 어절을 구성한 형태를 따른 것임.
    * <br/>
    * Sequence가 *연속되지 않더라도* 확인함. 즉, "초/XPN거대하/VAㄴ/ETM"이란 어절이 있다면,
    * `Seq(POS.XPN,POS.ETM)`는 중간 형태소에 대응하는 품사가 없지만, 순서는 포함되므로,
    * `true`를 반환함.
    *
    * @param tag 확인할 통합 품사 표기의 Sequence. `Seq[POSTag]` 객체.
    * @return True: 존재하는 경우
    */
  def matches(tag: Seq[String]): Boolean =
    morphemes.foldLeft(tag) {
      case (list, w) =>
        if (list.nonEmpty && w.tag.toString.startsWith(list.head)) list.tail
        else list
    }.isEmpty

  /**
    * 표면형이 같은지 비교함.
    *
    * @param another 표면형을 비교할 다른 어절 Word 객체.
    * @return True: 표면형이 같을 경우.
    */
  def equalsWithoutTag(another: Word): Boolean = another.surface == this.surface

  override def equals(that: Any): Boolean = that match {
    case w: Word if w.id == this.id => super.equals(w)
    case _ => false
  }

  override def canEqual(that: Any): Boolean = that.isInstanceOf[Word]

  /**
    * 구문분석과 품사분석의 결과를 String으로 변환.
    *
    * @return 본 객체의 정보를 담은 String.
    */
  override def toString: String = s"$surface = $singleLineString"

  /**
    * 품사분석 결과를, 1행짜리 String으로 변환.
    *
    * @return 품사분석 결과를 담은 1행짜리 String.
    */
  def singleLineString: String =
  this.map {
    case Morpheme(surf, tag) =>
      s"$surf/$tag"
  }.mkString("+")

  /**
    * (Java) 형태소를 문장의 순서대로 순회하는 iterator.
    *
    * @return 형태소 순회 Iterator.
    */
  def jIterator: java.util.Iterator[Morpheme] = iterator.asJava

  override protected[this] def newBuilder: mutable.Builder[Morpheme, Word] =
    new ArrayBuffer[Morpheme] mapResult Word.applySeq(id, surface)
}

/**
  * Companion object for Word
  */
object Word{
  /**
    * ROOT Word (Constant)
    */
  lazy final val ROOT: Word = applySeq(-1, "")(Seq.empty)

  /**
    * Create new word from given information
    *
    * @param id        Index in the sentence
    * @param surface   Surface string
    * @param morphemes Sequence of morphemes
    * @return A new word
    */
  def apply(id: Int, surface: String, morphemes: collection.Seq[Morpheme]): Word =
  applySeq(id, surface)(morphemes)

  /**
    * Extract surface form and morphemes for case-matching.
    *
    * @note "Extractor" is for pattern matching. That is, a word `w` can be matched as:
    *       <pre>
    *       w match { case Word(surface, morpheme1, morpheme2, _*) => ... }
    *       </pre>
    *       or can be matched as:
    *       <pre>
    *       w match { case Word(surface, morphemes @ _*) => ... }
    *       </pre>
    * @param arg A Word to be matched
    * @return Some(Surface form, morpheme list)
    */
  def unapplySeq(arg: Word): Option[(String, Seq[Morpheme])] = Some(arg.surface, arg.morphemes)

  /**
    * Builder factory for any word
    *
    * @return Builder factory
    */
  implicit def canBuildFrom: CanBuildFrom[Word, Morpheme, Word] =
  new CanBuildFrom[Word, Morpheme, Word] {
    override def apply(from: Word): mutable.Builder[Morpheme, Word] =
      new ArrayBuffer[Morpheme] mapResult applySeq(from.id, from.surface)

    override def apply(): mutable.Builder[Morpheme, Word] =
      throw new UnsupportedOperationException
  }

  /**
    * Generate word from given information
    *
    * @param id        Index within the sentence
    * @param surface   Surface string
    * @param morphemes Sequence of morphemes
    * @return A new word
    */
  private def applySeq(id: Int, surface: String)(morphemes: collection.Seq[Morpheme]) = {
    new Word(id, surface, morphemes.toVector)
  }
}