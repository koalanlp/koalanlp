package kr.bydelta.koala.data

import java.io.Serializable

@SerialVersionUID(4683667063955634926L)
class Sentence(val words: Seq[Word]) extends Serializable with Iterable[Word] {
  private[koala] var root: Int = -1

  override final def size: Int = words.size

  override final def head = words.head

  override final def last = words.last

  def ++(s: Sentence) = new Sentence(this.words ++ s.words)

  final def hasMorphemeOf(tag: String): Boolean = words.exists(_.existsMorpheme(tag))

  final def matches(tag: Seq[Seq[String]]): Boolean =
    words.foldLeft(tag) {
      case (list, w) =>
        if (w.matches(list.head)) list.tail
        else list
    }.isEmpty

  final def nouns = words.filter(_.exists(_.isNoun))

  final def verbs = words.filter(_.exists(_.isVerb))

  final def adjectives = words.filter(_.exists(_.isModifier))

  final def indexOf(word: Word): Int = words.indexOf(word)

  final def get(index: Int): Word = apply(index)

  final def apply(index: Int): Word = words(index)

  def iterator: Iterator[Word] = words.iterator

  override def toString: String =
    originalString() + "\n" +
      words.zipWithIndex.map {
        case (w, i) if i == root =>
          w.toString + "[ROOT]"
        case (w, _) =>
          w.toString
      }.mkString("\n")

  final def originalString(delimiter: String = " "): String =
    words.map(_.originalWord).mkString(delimiter)

  def singleLineString: String =
    words.map {
      _.map {
        morph =>
          s"${morph.morpheme}/${morph.tag}"
      }.mkString
    }.mkString("  ")

  def rootWord: Word = words(root)
}

