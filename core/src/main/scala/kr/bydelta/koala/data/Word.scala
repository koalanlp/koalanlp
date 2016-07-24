package kr.bydelta.koala.data

/**
  * Created by bydelta on 16. 7. 20.
  */

import java.io.Serializable

import kr.bydelta.koala.FunctionalTag
import kr.bydelta.koala.FunctionalTag.FunctionalTag

import scala.collection.mutable.ArrayBuffer

@SerialVersionUID(6198162325200883107L)
class Word(val originalWord: String, val morphemes: collection.Seq[Morpheme])
  extends Serializable with Iterable[Morpheme] {
  val dependents = ArrayBuffer[Word]()
  val numOfMeaningful: Int = morphemes.count {
    m => m.isNoun || m.isVerb || m.isModifier
  }
  var rawTag: String = _
  var tag: FunctionalTag = FunctionalTag.Undefined

  override final def head = morphemes.head

  override final def last = morphemes.last

  override final def size: Int = morphemes.size

  final def matches(tag: Seq[String]): Boolean =
    tag.foldLeft(true) {
      case (true, t) =>
        morphemes.exists(_.hasTag(t))
      case (false, _) =>
        false
    }

  final def existsMorpheme(tag: String) = morphemes.exists(_.hasTag(tag))

  final def get(index: Int): Morpheme = apply(index)

  final def apply(index: Int): Morpheme = morphemes(index)

  final def get(tag: String, default: Morpheme = null): Morpheme = apply(tag).getOrElse(default)

  final def apply(tag: String): Option[Morpheme] = morphemes.find(_.hasTag(tag))

  final def getNextOf(m: Morpheme): Option[Morpheme] = {
    val index = morphemes.indexOf(m) + 1
    if (index < morphemes.size) Some(morphemes(index))
    else None
  }

  final def getPrevOf(m: Morpheme): Option[Morpheme] = {
    val index: Int = morphemes.indexOf(m) - 1
    if (index > -1) Some(morphemes(index))
    else None
  }

  def equalsWithoutTag(another: Word): Boolean = another.originalWord == this.originalWord

  override def toString: String = {
    val prefix = s"$originalWord\t= " + morphemes.mkString("")
    if (dependents.isEmpty) prefix
    else {
      prefix + "\n\t의존관계:" + dependents.map {
        w =>
          s"${w.originalWord}(${w.tag}/${w.rawTag})"
      }.mkString(" ")
    }
  }

  def singleLineString: String =
    morphemes.map {
      morph =>
        s"${morph.morpheme}/${morph.tag}"
    }.mkString

  def iterator: Iterator[Morpheme] = morphemes.iterator

  @throws[IllegalStateException]
  private[koala] final def addDependant(word: Word, tag: FunctionalTag, rawTag: String) {
    if (tag == null)
      throw new IllegalStateException("의존구문분석의 결과 태그는, Null일 수 없습니다.")
    word.tag = tag
    word.rawTag = rawTag
    dependents += word
  }
}