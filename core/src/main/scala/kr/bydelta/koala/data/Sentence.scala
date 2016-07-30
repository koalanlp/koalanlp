package kr.bydelta.koala.data

import kr.bydelta.koala.POS.POSTag

import scala.annotation.tailrec
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * 문장 Class
  *
  * @param words 문장에 포함되는 어절의 나열.
  */
class Sentence(val words: Seq[Word]) extends Iterable[Word] {
  /**
    * 의존 구문 분석 결과, 나타난 핵심어들.
    */
  val topLevels: ArrayBuffer[Word] = ArrayBuffer()

  /**
    * 문장의 첫 어절
    *
    * @return 문장의 첫 어절인 Word 객체.
    */
  override final def head = words.head

  /**
    * 문장의 끝 어절
    *
    * @return 문장의 끝 어절인 Word 객체.
    */
  override final def last = words.last

  /**
    * (Java)의존 구문 분석 결과, 나타난 핵심어들.
    */
  def jTopLevels = topLevels.asJava

  /**
    * 문장을 이어붙여, 새로운 문장을 생성함.
    *
    * @param s 이어붙일 다른 문장 Sentence 객체
    * @return 문장이 이어붙여진 새로운 Sentence 객체
    */
  def concat(s: Sentence) = ++(s)

  /**
    * 문장을 이어붙여, 새로운 문장을 생성함.
    *
    * @param s 이어붙일 다른 문장 Sentence 객체
    * @return 문장이 이어붙여진 새로운 Sentence 객체
    */
  def ++(s: Sentence) = new Sentence(this.words ++ s.words)

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
  final def existsMorpheme(tag: String): Boolean = words.exists(_.existsMorpheme(tag))

  /**
    * (Java) 주어진 품사 표기의 Sequence를 포함하는지 확인.
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
  final def matches(tag: Array[Array[String]]): Boolean = matches(tag.map(_.toSeq).toSeq)

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
  final def matches(tag: Seq[Seq[String]]): Boolean =
    words.foldLeft(tag) {
      case (list, w) =>
        if (w.matches(list.head)) list.tail
        else list
    }.isEmpty

  /**
    * (Java) 체언^명사, 수사, 대명사^을 포함하는 어절들
    *
    * @return 체언을 포함하는 어절들의 Sequence
    */
  final def jNouns = nouns.asJava

  /**
    * 체언^명사, 수사, 대명사^을 포함하는 어절들
    *
    * @return 체언을 포함하는 어절들의 Sequence
    */
  final def nouns = words.filter(_.exists(_.isNoun))

  /**
    * (Java) 용언^동사, 형용사^을 포함하는 어절들
    *
    * @return 용언을 포함하는 어절들의 Sequence
    */
  final def jVerbs = verbs.asJava

  /**
    * 용언^동사, 형용사^을 포함하는 어절들
    *
    * @return 용언을 포함하는 어절들의 Sequence
    */
  final def verbs = words.filter(_.exists(_.isVerb))

  /**
    * (Java) 수식언^관형사, 부사^을 포함하는 어절들
    *
    * @return 수식언을 포함하는 어절들의 Sequence
    */
  final def jModifiers = modifiers.asJava

  /**
    * 수식언^관형사, 부사^을 포함하는 어절들
    *
    * @return 수식언을 포함하는 어절들의 Sequence
    */
  final def modifiers = words.filter(_.exists(_.isModifier))

  /**
    * 주어진 품사의 형태소가 포함된 어절을 모음.
    *
    * @param tag 확인할 품사.
    * @return 주어진 품사를 포함하는 어절들의 Sequence
    */
  final def filter(tag: String): Seq[Word] = words.filter(_.existsMorpheme(tag))

  /**
    * 주어진 품사의 형태소가 포함된 어절을 모음.
    *
    * @param tag 확인할 품사.
    * @return 주어진 품사를 포함하는 어절들의 Sequence
    */
  final def filter(tag: Seq[POSTag]): Seq[Word] = words.filter(_.existsMorpheme(tag))

  /**
    * 주어진 품사의 형태소가 포함되지 않은 어절을 모음.
    *
    * @param tag 확인할 품사.
    * @return 주어진 품사를 포함하는 어절들의 Sequence
    */
  final def filterNot(tag: String): Seq[Word] = words.filter(!_.existsMorpheme(tag))

  /**
    * 주어진 품사의 형태소가 포함되지 않은 어절을 모음.
    *
    * @param tag 확인할 품사.
    * @return 주어진 품사를 포함하는 어절들의 Sequence
    */
  final def filterNot(tag: Seq[POSTag]): Seq[Word] = words.filter(!_.existsMorpheme(tag))

  /**
    * 주어진 어절의 위치를 찾는다.
    *
    * @param word 위치를 확인할 어절
    * @return 어절의 위치.
    */
  final def indexOf(word: Word): Int = words.indexOf(word)

  /**
    * 주어진 위치의 어절을 반환. Option[Word] 객체.
    *
    * @param index 어절의 위치.
    * @return 어절이 있다면, Some(어절)을, 없다면 None.
    */
  final def apply(index: Int): Option[Word] =
  if (index < 0 || index >= size) None
  else Some(words(index))

  /**
    * 문장의 길이
    *
    * @return 문장의 길이
    */
  override final def size: Int = words.size

  /**
    * 주어진 위치의 어절을 반환. Word 객체.
    *
    * @param index 어절의 위치.
    * @return 어절이 있다면, 어절을, 없다면 Exception.
    */
  final def get(index: Int): Word = words(index)

  /**
    * (Java) 어절을 문장의 순서대로 순회하는 iterator.
    *
    * @return 어절 순회 Iterator.
    */
  def jIterator = iterator.asJava

  /**
    * 어절을 문장의 순서대로 순회하는 iterator.
    *
    * @return 어절 순회 Iterator.
    */
  def iterator: Iterator[Word] = words.iterator

  /**
    * (Java) 어절 목록. `java.util.List<Word>`
    *
    * @return 어절 목록.
    */
  def jWords = words.asJava

  /**
    * 구문분석과 품사분석의 결과를 String으로 변환.
    *
    * @return 본 객체의 정보를 담은 String.
    */
  override def toString: String =
  surfaceString() + "\n" +
    words.map {
      w =>
        w.toString + (if (topLevels.contains(w)) "[ROOT]" else "")
    }.mkString("\n")

  /**
    * 띄어쓰기 된 문장을 반환.
    *
    * @param delimiter 어절 사이의 띄어쓰기 방식. 기본값 = 공백(" ")
    * @return 띄어쓰기 된 문장.
    */
  final def surfaceString(delimiter: String = " "): String =
  words.map(_.surface).mkString(delimiter)

  /**
    * 품사분석 결과를, 1행짜리 String으로 변환.
    *
    * @return 품사분석 결과를 담은 1행짜리 String.
    */
  def singleLineString: String =
  words.map(_.singleLineString).mkString(" ")

  /**
    * 의존구문분석트리를 String형태로 그립니다.
    *
    * @return 트리 String.
    */
  def treeString: String =
  if (topLevels.isEmpty) singleLineString
  else {
    val word = topLevels.head
    val stack = mutable.Stack[(Word, Int)]()
    stack pushAll topLevels.tail.map(_ -> 0)
    treeString(word, 0, stack)
  }

  @tailrec
  private def treeString(head: Word, depth: Int,
                         stack: mutable.Stack[(Word, Int)] = mutable.Stack(),
                         printed: ArrayBuffer[String] = ArrayBuffer()): String = {
    printed += (" " * depth + s"+${head.depTag} : ${head.singleLineString} ... ${head.rawDepTag}")
    stack pushAll head.dependents.map(_ -> (depth + 1))
    if (stack.nonEmpty) {
      val (nextHead, nextDepth) = stack.pop()
      treeString(nextHead, nextDepth, stack, printed)
    } else
      printed.mkString("\n")
  }
}

