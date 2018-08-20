package kr.bydelta.koala.data

import scala.collection.JavaConverters._
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable.ArrayBuffer
import scala.collection.{IndexedSeqLike, mutable}

/**
  * 문장 Class / Sentence Class
  *
  * @param words 문장에 포함되는 어절의 나열. / Word sequence
  */
@SerialVersionUID(1100101L)
final class Sentence private(val words: Vector[Word])
  extends IndexedSeq[Word] with IndexedSeqLike[Word, Sentence] with Serializable {

  override def canEqual(that: Any): Boolean = that.isInstanceOf[Sentence]

  /**
    * (Java) 주어진 품사 표기의 Sequence를 포함하는지 확인.
    *
    * <br/>
    * `POS$.Value[][]`의 형태이며, 이는 품사가 어절을 구성하고^POS$.Value[]^,
    * 어절이 문장을 구성한 형태^POS$.Value[][]^를 따른 것임.
    * <br/>
    * Sequence가 *연속되지 않더라도* 확인함. 즉, "나/NP는/JKS 밥/NNG을/JKO 먹/VV고/EC,"라는 문장구조가 있다면,
    * `{{POSTag.NP,POSTag.JKS},{POSTag.VV}}`는 중간 어절에 대응하는 품사의 Sequence가 없지만, 순서는 포함되므로,
    * `true`를 반환함.
    *
    * @param tag 확인할 통합 품사 표기의 Sequence. `POS$.Value[][]` 객체.
    * @return True: 존재하는 경우
    */
  def matches(tag: Array[Array[String]]): Boolean = matches(tag.map(_.toSeq).toSeq)

  /**
    * 주어진 품사 표기의 Sequence를 포함하는지 확인.
    * <br/>
    * `Seq[Seq[POSTag] ]`의 형태이며, 이는 품사가 어절을 구성하고^Seq[POSTag]^,
    * 어절이 문장을 구성한 형태^Seq[Seq[POSTag.Value] ]^를 따른 것임.
    * <br/>
    * Sequence가 *연속되지 않더라도* 확인함. 즉, "나/NP는/JKS 밥/NNG을/JKO 먹/VV고/EC,"라는 문장구조가 있다면,
    * `Seq(Seq(POS.NP,POS.JKS),Seq(POS.VV))`는 중간 어절에 대응하는 품사의 Sequence가 없지만, 순서는 포함되므로,
    * `true`를 반환함.
    *
    * @param tag 확인할 통합 품사 표기의 Sequence. `Seq[Seq[POSTag] ]` 객체.
    * @return True: 존재하는 경우
    */
  def matches(tag: Seq[Seq[String]]): Boolean =
    words.foldLeft(tag) {
      case (list, w) =>
        if (list.nonEmpty && w.matches(list.head)) list.tail
        else list
    }.isEmpty

  /**
    * (Java) 체언^명사, 수사, 대명사^을 포함하는 어절들
    *
    * (Java) List of words which contains Nominal morphemes (Noun, Ordinal/Cardinal, Pronoun)
    *
    * @return 체언을 포함하는 어절들의 Sequence
    */
  def jNouns: java.util.List[Word] = nouns.asJava

  /**
    * 체언^명사, 수사, 대명사^을 포함하는 어절들
    *
    * Sequence of words which contains Nominal morphemes (Noun, Ordinal/Cardinal, Pronoun)
    *
    * @return 체언을 포함하는 어절들의 Sequence
    */
  def nouns: Seq[Word] = words.filter(_.exists(_.isNoun))

  /**
    * (Java) 용언^동사, 형용사^을 포함하는 어절들
    *
    * (Java) List of words which contains Predicative morphemes (Verb, Adjective)
    *
    * @return 용언을 포함하는 어절들의 Sequence
    */
  def jVerbs: java.util.List[Word] = verbs.asJava

  /**
    * 용언^동사, 형용사^을 포함하는 어절들
    *
    * Sequence of words which contains Predicative morphemes (Verb, Adjective)
    *
    * @return 용언을 포함하는 어절들의 Sequence
    */
  def verbs: Seq[Word] = words.filter(_.exists(_.isPredicate))

  /**
    * (Java) 수식언^관형사, 부사^을 포함하는 어절들
    *
    * (Java) List of words which contains Modifying morphemes (Determiner, Adverb)
    *
    * @return 수식언을 포함하는 어절들의 Sequence
    */
  def jModifiers: java.util.List[Word] = modifiers.asJava

  /**
    * 수식언^관형사, 부사^을 포함하는 어절들
    *
    * Sequence of words which contains Modifying morphemes (Determiner, Adverb)
    *
    * @return 수식언을 포함하는 어절들의 Sequence
    */
  def modifiers: Seq[Word] = words.filter(_.exists(_.isModifier))

  override def apply(idx: Int): Word = words(idx)

  override def length: Int = words.size

  /**
    * (Java) 어절을 문장의 순서대로 순회하는 iterator.
    *
    * @return 어절 순회 Iterator.
    */
  def jIterator: java.util.Iterator[Word] = iterator.asJava

  override def toString: String = surfaceString()

  /**
    * 띄어쓰기 된 문장을 반환.
    *
    * @param delimiter 어절 사이의 띄어쓰기 방식. 기본값 = 공백(" ")
    * @return 띄어쓰기 된 문장.
    */
  def surfaceString(delimiter: String = " "): String =
  words.map(_.surface).mkString(delimiter)

  /**
    * 품사분석 결과를, 1행짜리 String으로 변환.
    *
    * @return 품사분석 결과를 담은 1행짜리 String.
    */
  def singleLineString: String =
  words.map(_.singleLineString).mkString(" ")

  override protected[this] def newBuilder: mutable.Builder[Word, Sentence] = Sentence.newBuilder

  private var phraseTree: Option[Phrase] = Option.empty
  private var dependency: Option[Dependency] = Option.empty

  private[koala] def withPhraseTree(phrase: SyntacticPhraseLike) = {
    this.phraseTree = Option(phrase.asInstanceOf[Phrase])
    this
  }

  private[koala] def withDependency(dep: DependencyRelationLike) = {
    this.dependency = Option(dep.asInstanceOf[Dependency])
    this
  }

  /**
    * 구구조 분석 결과 (Option)
    * @return 구구조 분석결과
    */
  def phrases:Option[SyntacticPhraseLike] = phraseTree

  /**
    * 의존구조 분석 결과 (Option)
    * @return 의존구조 분석결과
    */
  def dependencies:Option[DependencyRelationLike] = dependency

  /**
    * 어절에 직접 대응하는 최하위 구구조
    * @param word 구구조를 찾고자 하는 어절
    * @return 어절을 포함하는 말단 구구조 (찾지 못하면 null)
    */
  def getPhraseOf(word: Word): Option[SyntacticPhraseLike] =
    if(contains(word)) getPhraseOf(word.id)
    else None
  /**
    * 어절에 직접 대응하는 최하위 구구조
    * @param word 구구조를 찾고자 하는 어절의 번호
    * @return 어절을 포함하는 말단 구구조 (찾지 못하면 null)
    */
  def getPhraseOf(word: Int): Option[SyntacticPhraseLike] =
    if (word < length) phrases.flatMap(_.leaves.get(word))
    else None

  /**
    * 어절에 직접 대응하는 최하위 의존구조
    * @param word 의존구조를 찾고자 하는 어절
    * @return 어절을 포함하는 말단 의존구조 (찾지 못하면 null)
    */
  def getDependencyOf(word: Word): Option[DependencyRelationLike] =
    if(word == Word.ROOT) dependencies
    else if(contains(word)) getDependencyOf(word.id)
    else None
  /**
    * 어절에 직접 대응하는 최하위 의존구조
    * @param word 의존구조를 찾고자 하는 어절의 번호
    * @return 어절을 포함하는 말단 의존구조 (찾지 못하면 null)
    */
  def getDependencyOf(word: Int): Option[DependencyRelationLike] =
    if(word < length) dependencies.flatMap(_.leaves.get(word))
    else None
}

/**
  * Companion object for Sentence
  */
object Sentence {

  def empty = new Sentence(Vector.empty)

  /**
    * Create a sentence.
    *
    * @param words Word sequence.
    * @return a new Sentence.
    */
  def apply(words: collection.Seq[Word]): Sentence = applySeq(words)

  /**
    * Create a sentence.
    *
    * @note Access is restricted because (i) Sentence should be created within Koala package,
    *       and (ii) most developers using this package seldom needs this operation.
    * @param words Word sequence.
    * @return a new Sentence.
    */
  private def applySeq(words: collection.Seq[Word]) = new Sentence(words.toVector)

  /**
    * Extractor for the sentences.
    *
    * @note "Extractor" is for pattern matching. That is, a sentence `s` can be matched as:
    *       <pre>
    *       s match { case Sentence(word1, word2, _*) => ... }
    *       </pre>
    *       or can be matched as:
    *       <pre>
    *       s match { case Sentence(wordseq @ _*) => ... }
    *       </pre>
    * @param target Sentence to be matched
    * @return Some(word sequence)
    */
  def unapplySeq(target: Sentence): Option[Seq[Word]] = {
    Some(target.words)
  }

  /**
    * Builder factory for any sentence.
    *
    * @return Builder factory instance.
    */
  implicit def canBuildFrom: CanBuildFrom[Sentence, Word, Sentence] =
  new CanBuildFrom[Sentence, Word, Sentence] {
    override def apply(from: Sentence): mutable.Builder[Word, Sentence] = newBuilder

    override def apply(): mutable.Builder[Word, Sentence] = newBuilder
  }

  /**
    * Create new builder for type Sentence.
    *
    * @return a new Builder
    */
  private def newBuilder = new ArrayBuffer[Word] mapResult applySeq
}