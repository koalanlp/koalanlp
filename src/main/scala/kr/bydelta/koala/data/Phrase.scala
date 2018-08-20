package kr.bydelta.koala.data

import kr.bydelta.koala.{FunctionalTag, PhraseType}

/**
  * Trait: 문법 구조 표지 (구구조문법, 의존구조문법)
  * @tparam T: 문법표지자 유형
  */
sealed trait PhraseLike[T <: PhraseLike[T]] extends IndexedSeq[T] with Serializable {
  /**
    * 이 문법 구조의 하위 구조
    */
  val children: IndexedSeq[T]
  /**
    * 최하위 어절에 직접 대응하는 문법 구조
    */
  val leaves: Map[Int, T]

  /**
    * 부모 문법 구조 (최상위인 경우 null)
    */
  var parent: T = _

  /**
    * 이 문법 구조의 표면형
    * @return 표면형 문자열
    */
  def surface: String = children.map(_.surface).mkString(" ")

  /**
    * 이 문법 구조의 트리형 표현
    * @param depth 현재 깊이
    * @return 트리 표현 문자열
    */
  def treeString(depth: Int = 0): String = {
    val prefix = "|  " * depth

    if(children.isEmpty)
      prefix + toString
    else
      prefix + toString + "\n" + children.map(_.treeString(depth + 1)).mkString("\n")
  }

  override def length: Int = children.length

  override def apply(idx: Int): T = children(idx)
}

/**
  * 구문구조 문법 표지
  */
sealed trait SyntacticPhraseLike extends PhraseLike[SyntacticPhraseLike] {
  /**
    * 현재 구문 구조의 명칭 (세종 구문표지)
    */
  val typeName: PhraseType.Value

  override def toString: String = s"$typeName($surface)"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[SyntacticPhraseLike]

  override def equals(that: Any): Boolean =
    if(canEqual(that)){
      val other = that.asInstanceOf[SyntacticPhraseLike]
      other.typeName == this.typeName &&
        other.size == this.size &&
        other.children.zip(this.children).forall(t => t._1 == t._2)
    }else false
}

/**
  * 구문구조 문법 표지 (비말단)
  * @param typeName 구문표지
  * @param children 하위 구문구조
  */
@SerialVersionUID(1100101l)
class Phrase private[data](override val typeName: PhraseType.Value,
                     override val children: IndexedSeq[SyntacticPhraseLike]) extends SyntacticPhraseLike{
  children.foreach(_.parent = this)

  /**
    * 최하위 어절에 직접 대응하는 문법 구조
    */
  override val leaves: Map[Int, SyntacticPhraseLike] = children.flatMap(_.leaves).toMap
}

/**
  * 구문구조 문법 표지 (말단)
  * @param typeName 구문표지
  * @param word 연결되는 어절
  */
@SerialVersionUID(1100101l)
class WordPhrase private[data](override val typeName: PhraseType.Value,
                         val word: Word) extends SyntacticPhraseLike {
  /**
    * 최하위 어절에 직접 대응하는 문법 구조
    */
  override val leaves: Map[Int, SyntacticPhraseLike] = Map(word.id -> this)

  override val children: IndexedSeq[SyntacticPhraseLike] = IndexedSeq.empty

  override def surface: String = word.surface

  override def toString: String = super.toString + s" @ ${word.id}"
}

/**
  * 구문구조 문법표지의 companion object.
  */
object Phrase {
  /**
    * 비말단 구문구조 문법 표지를 생성함
    * @param typeName 세종 구문표지
    * @param children 하위 구문구조들
    * @return 새 구문구조
    */
  def apply(typeName: PhraseType.Value, children: SyntacticPhraseLike*): Phrase =
    new Phrase(typeName, children.toIndexedSeq)

  /**
    * 말단 구문구조 문법표지를 생성함
    * @param word 연결되는 어절
    * @param typeName 세종 구문표지
    * @return 새 구문구조
    */
  def apply(typeName: PhraseType.Value, word: Word): WordPhrase =
    new WordPhrase(typeName, word)
}

/**
  * 의존구문구조 문법표지
  */
sealed trait DependencyRelationLike extends PhraseLike[DependencyRelationLike] {
  /**
    * 의존구문분석 표지 (세종 기능표지)
    */
  val function: FunctionalTag.Value
  /**
    * 현재 구문 구조의 명칭 (세종 구문표지)
    */
  val typeName: PhraseType.Value

  /**
    * 현재 의존구문구조 분석 대상인 어절
    */
  val word: Word

  override def surface: String = word.surface

  override def toString: String = s"$typeName-$function($word) @ ${word.id}"

  override def canEqual(that: Any): Boolean = that.isInstanceOf[DependencyRelationLike]

  override def equals(that: Any): Boolean =
    if(canEqual(that)){
      val other = that.asInstanceOf[DependencyRelationLike]
      other.typeName == this.typeName &&
        other.function == this.function &&
        other.word == this.word &&
        other.size == this.size &&
        other.children.zip(this.children).forall(t => t._1 == t._2)
    }else false
}

/**
  * 의존구문구조 문법표지 (비말단)
  * @param typeName 구구조 분류
  * @param function 지배소(부모)와의 기능적 관계
  * @param word 현재 의존구문구조 분석 대상인 어절
  * @param children 하위 의존구문구조
  */
@SerialVersionUID(1100101l)
class Dependency private[data](override val typeName: PhraseType.Value,
                               override val function: FunctionalTag.Value,
                         override val word: Word,
                         override val children: IndexedSeq[DependencyRelationLike])
  extends DependencyRelationLike{
  children.foreach(_.parent = this)

  /**
    * 최하위 어절에 직접 대응하는 문법 구조
    */
  override val leaves: Map[Int, DependencyRelationLike] =
    children.flatMap(_.leaves).toMap + (word.id -> this)
}

/**
  * 의존구문구조 문법표지 (말단)
  * @param typeName 구구조 분류
  * @param function 지배소(부모)와의 기능적 관계
  * @param word 현재 의존구문구조 분석 대상인 어절
  */
@SerialVersionUID(1100101l)
class WordDependency private[data](override val typeName: PhraseType.Value,
                                   override val function: FunctionalTag.Value,
                             override val word: Word) extends DependencyRelationLike {
  /**
    * 최하위 어절에 직접 대응하는 문법 구조
    */
  override val leaves: Map[Int, DependencyRelationLike] = Map(word.id -> this)

  override val children: IndexedSeq[DependencyRelationLike] = IndexedSeq.empty
}

/**
  * 의존구문구조 문법표지의 companion object
  */
object Dependency {
  /**
    * 비말단 의존구문구조 문법표지를 생성함
    * @param typeName 구구조 분류
    * @param function 지배소(부모)와의 기능적 관계
    * @param word 현재 의존구문구조 분석 대상인 어절
    * @param children 하위 의존구문구조들
    * @return 새 의존구문구조
    */
  def apply(typeName: PhraseType.Value,
            function: FunctionalTag.Value, word: Word,
            children: DependencyRelationLike*): Dependency =
    new Dependency(typeName, function, word, children.toIndexedSeq)

  /**
    * 말단 의존구문구조 문법표지를 생성함
    * @param typeName 구구조 분류
    * @param function 지배소(부모)와의 기능적 관계
    * @param word 현재 의존구문구조 분석 대상인 어절
    * @return 새 의존구문구조
    */
  def apply(typeName: PhraseType.Value,
            function: FunctionalTag.Value,
            word: Word): WordDependency =
    new WordDependency(typeName, function, word)
}