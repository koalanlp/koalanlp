package kr.bydelta.koala.data

import kr.bydelta.koala.POS
import kr.bydelta.koala.POS.POSTag

/**
  * 형태소 class / Morpheme class
  *
  * @param surface 형태소 표면형 String / Surface form of the morpheme
  * @param rawTag  원본 형태소 분석기의 품사 String / POS Tag, produced by the original tagger.
  * @param tag     통합 품사 / POS Tag, induced by Koala.
  */
@SerialVersionUID(1080201L)
final class Morpheme private(val surface: String, val rawTag: String, val tag: POSTag) extends Serializable {
  /**
    * Index of this morpheme
    */
  private[koala] var index: Int = _

  /**
    * Index of this morpheme in the word.
    *
    * @return index
    */
  def id: Int = index

  /**
    * 체언^명사, 수사, 대명사^ 형태소인지 확인.
    *
    * Check whether this morpheme is a Noun (Noun, Cardinal/Ordinal, Pronoun)
    *
    * @return True : 체언 형태소일 경우.
    */
  def isNoun: Boolean = POS.isNoun(tag)

  /**
    * 용언^동사, 형용사^ 형태소인지 확인.
    *
    * Check whether this morpheme is a Predicate (Verb, Adjective)
    *
    * @return True : 용언 형태소일 경우.
    */
  def isPredicate: Boolean = POS.isPredicate(tag)

  /**
    * 수식언^관형사, 부사^ 형태소인지 확인.
    *
    * Check whether this morpheme is a Modifier (Determiner, Adverb)
    *
    * @return True : 수식언 형태소일 경우.
    */
  def isModifier: Boolean = POS.isModifier(tag)

  /**
    * 관계언^조사^ 형태소인지 확인.
    *
    * Check whether this morpheme is a Postposition
    *
    * @return True : 관계언 형태소일 경우.
    */
  def isJosa: Boolean = POS.isPostPosition(tag)

  /**
    * 통합 품사가 주어진 품사 표기에 대응하는지 확인.
    * <br/>
    * 예를 들어, N은 체언인지 확인하고, NP는 대명사인지 확인.
    * 품사 표기는 [[https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing 여기]]
    * 에서 확인
    *
    * @param tag 확인할 품사 표기 String
    * @return True: 주어진 품사를 가질경우
    */
  def hasTag(tag: String): Boolean = this.tag.toString.startsWith(tag)

  /**
    * 통합 품사가 주어진 품사 표기에 대응하는지 확인.
    * <br/>
    * 품사 표기는 [[https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing 여기]]
    * 에서 확인
    *
    * @param tag 확인할 품사 표기 String
    * @return True: 주어진 품사를 가질경우
    */
  def hasTag(tag: Seq[POSTag]): Boolean = tag.contains(this.tag)

  /**
    * 원본 품사가 주어진 품사 표기에 대응하는지 확인.
    * <br/>
    * 각 분석기별 품사 표기는 [[https://docs.google.com/spreadsheets/d/1OGM4JDdLk6URuegFKXg1huuKWynhg_EQnZYgTmG4h0s/edit?usp=sharing 여기]]
    * 에서 확인
    *
    * @param tag 확인할 품사 표기 String
    * @return True: 주어진 품사를 가질경우
    */
  def hasRawTag(tag: String): Boolean = rawTag.startsWith(tag)

  /**
    * 타 형태소 객체와 형태소의 표면형이 같은지 비교함.
    *
    * Compare surface form with other morpheme instance.
    *
    * @param another 비교할 형태소 객체 / Morpheme instance to be compared
    * @return True: 표면형이 같은 경우
    */
  def equalsWithoutTag(another: Morpheme): Boolean = another.surface == this.surface

  override def toString: String = s"$surface/$tag($rawTag)"

  override def equals(obj: Any): Boolean = {
    obj match {
      case m: Morpheme =>
        m.surface == this.surface && m.tag == this.tag
      case _ =>
        false
    }
  }

  override def hashCode(): Int = (41 + surface.hashCode) * 41 + tag.hashCode
}

/**
  * Companion object for Morpheme class.
  */
object Morpheme {
  /**
    * Create a Morpheme.
    *
    * @param surface Surface form of the morpheme
    * @param rawTag  POS Tag, produced by the original tagger.
    * @param tag     POS Tag, induced by Koala.
    * @return a new Morpheme.
    */
  def apply(surface: String, rawTag: String, tag: POSTag) =
  new Morpheme(surface, rawTag, tag)

  /**
    * Extractor for the morpheme.
    *
    * @note "Extractor" is for pattern matching. That is, a morpheme `m` can be matched as:
    *       <pre>
    *       m match { case Morpheme(surface, tag) => ... }
    *       </pre>
    * @param target target morpheme to be matched.
    * @return Some(surface, tag)
    */
  def unapply(target: Morpheme): Option[(String, POSTag)] = {
    Some(target.surface, target.tag)
  }
}