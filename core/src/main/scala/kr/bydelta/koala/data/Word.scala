package kr.bydelta.koala.data

import kr.bydelta.koala.FunctionalTag.FunctionalTag

import scala.collection.JavaConverters._
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable.ArrayBuffer
import scala.collection.{IndexedSeqLike, mutable}

/**
  * 어절 Class
  *
  * @param surface   어절의 표면형 String.
  * @param morphemes 어절에 포함된 형태소의 목록 Seq[Morpheme].
  */
@SerialVersionUID(1080201L)
final class Word private(val surface: String, val morphemes: Vector[Morpheme])
  extends IndexedSeq[Morpheme] with IndexedSeqLike[Morpheme, Word] with Serializable {

  /**
    * Index of this word
    */
  private[koala] var index: Int = -1
  /**
    * 현재 어절에 의존하는 의존소의 목록. 즉, 현재 어절의 핵심 의미를 보조하거나, 보강하는 단어들.
    */
  private[koala] var deps = Set[Relationship]()

  morphemes.zipWithIndex.par.foreach {
    case (m, mid) => m.index = mid
  }

  override def length: Int = morphemes.length

  override def apply(idx: Int): Morpheme = morphemes(idx)

  /**
    * 현재 어절에 의존하는 의존소의 목록. 즉, 현재 어절의 핵심 의미를 보조하거나, 보강하는 단어들.
    */
  def dependents: Set[Relationship] = deps

  /**
    * (Java) 현재 어절에 의존하는 의존소의 목록. 즉, 현재 어절의 핵심 의미를 보조하거나, 보강하는 단어들.
    *
    * @return 의존소 목록 `java.util.List<Word>`
    */
  def jDependents: java.util.Set[Relationship] = deps.asJava

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
  override def toString: String = {
    s"$surface\t= " + morphemes.mkString("") +
      (if (deps.isEmpty) ""
      else {
        "\n" + deps.map {
          case Relationship(_, tag, target) =>
            f".... 이 어절의 $tag: 어절 [#$target]"
        }.mkString("\n")
      })
  }

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

  /**
    * Index of this word within the sentence.
    *
    * @return index
    */
  def id: Int = index

  /**
    * 구문분석과 품사분석의 결과를 String으로 변환.
    *
    * @return 본 객체의 정보를 담은 String.
    */
  private[koala] def toStringWithSentence(sent: Sentence): String = {
    s"$surface\t= " + morphemes.mkString("") +
      (if (deps.isEmpty) ""
      else {
        "\n" + deps.map {
          case Relationship(_, tag, target) =>
            f".... 이 어절의 $tag%15s인 어절: [#$target%2d] ${sent(target).surface}"
        }.mkString("\n")
      })
  }

  /**
    * 어절에 의존하는 새로운 의존소 추가.
    *
    * @param word   의존소가 될 어절 Word 객체.
    * @param tag    의존관계의 유형.
    * @param rawTag 의존관계의 원본 명칭.
    */
  private[koala] def addDependant(word: Int, tag: FunctionalTag, rawTag: String) {
    deps += Relationship(id, tag, rawTag, word)
  }
}

/**
  * Companion object for Word
  */
object Word{
  /**
    * Create new word from given information
    *
    * @param surface   Surface string
    * @param morphemes Sequence of morphemes
    * @return A new word
    */
  def apply(surface: String, morphemes: collection.Seq[Morpheme]): Word =
  applySeq(-1, surface)(morphemes)

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
    val w = new Word(surface, morphemes.toVector)
    w.index = id
    w
  }

  /**
    * Create empty word (for root)
    *
    * @return Empty word
    */
  private[koala] def apply() = applySeq(-1, "")(Seq.empty)
}

/**
  * Dependency Relationship container class
  *
  * @param head     Index of head word (within Sentence)
  * @param relation Relation name (Normalized)
  * @param rawRel   Relation name (Not-normalized, original)
  * @param target   Index of target word (within Sentence)
  */
@SerialVersionUID(1080201L)
final class Relationship private(val head: Int,
                                 val relation: FunctionalTag, val rawRel: String,
                                 val target: Int) extends Serializable {

  override def equals(obj: scala.Any): Boolean =
    obj match {
      case Relationship(h, r, t) => this.head == h && this.relation == r && this.target == t
      case _ => false
    }

  override def hashCode(): Int =
    ((41 + head.hashCode()) * 41 + relation.hashCode()) * 41 + target.hashCode()

  override def toString: String = s"Rel:$relation (ID:$head → ID:$target)"
}

/**
  * Companion object for Relationship
  */
object Relationship {
  /**
    * Create new relationship
    *
    * @param head   Index of head word
    * @param rel    Normalized relationship
    * @param rawRel Raw name for the relationship
    * @param target Index of target word
    * @return A new relationship
    */
  def apply(head: Int, rel: FunctionalTag, rawRel: String, target: Int) =
  new Relationship(head, rel, rawRel, target)

  /**
    * Extractor for relationship
    *
    * @note "Extractor" is for pattern matching. That is, a Relationship `r` can be matched as:
    *       <pre>
    *       r match { case Relationship(head, rel, target) => ... }
    *       </pre>
    * @param arg Target relation
    * @return Some(head Index, relationship, target Index)
    */
  def unapply(arg: Relationship): Option[(Int, FunctionalTag, Int)] =
  Some(arg.head, arg.relation, arg.target)
}