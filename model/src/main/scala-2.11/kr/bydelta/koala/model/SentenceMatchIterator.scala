package kr.bydelta.koala.model

import kr.bydelta.koala.traits.CanTag

import scala.annotation.tailrec
import scala.collection.mutable

/**
  * Created by bydelta on 17. 1. 11.
  */
class SentenceMatchIterator(val references: Seq[CanTag[_]], val corpora: Corpora) extends Iterator[Seq[Label]] {
  private val refs = references.tail.iterator
  private val irregularMap = mutable.HashMap[String, String]()
  private var tagRef = references.head
  private var cIter = corpora.iterator

  def getIrregulars: Map[String, String] = irregularMap.toMap

  def toLabels(sent: String, tagger: CanTag[_]): Seq[Label] = {
    lazy val tags = tagger.tagSentence(sent).flatMap { word =>
      word.flatMap { morph =>
        morph.surface.map { ch =>
          ch -> morph.tag.toString
        }
      } :+ (' ' -> "WA")
    }

    matchUpLabels(sent, tags)
  }

  override def next(): Seq[Label] =
    if (cIter.hasNext) {
      toLabels(cIter.next(), tagRef)
    } else if (refs.hasNext) {
      cIter = corpora.iterator
      tagRef = refs.next()
      toLabels(cIter.next(), tagRef)
    } else {
      throw new NoSuchElementException("Iterator is Empty!")
    }

  override def hasNext: Boolean =
    cIter.hasNext || refs.hasNext

  @tailrec
  private def matchUpLabels(sent: Seq[Char], tags: Seq[(Char, String)],
                            acc: Seq[Label] = Seq.empty): Seq[Label] =
    if (tags.isEmpty && sent.isEmpty) acc.reverse
    else if (tags.isEmpty || sent.isEmpty) throw new IllegalStateException(s"Matching failed between $sent and $tags")
    else {
      val char = sent.head
      val (seg, tag) = tags.head

      val (compoundTag, rest) =
        if (char == seg) (tag, tags.tail)
        else if (char == ' ') ("WD", tags)
        else if (sent.length > 1) {
          val nextChar = sent.tail.head.toString
          val ind = tags.indexWhere(_._1 == nextChar) - 1
          val slice = tags.slice(0, ind)
          val concatTag = slice.map(_._2).mkString(".")
          irregularMap(s"$char/$concatTag") = slice.map(x => s"${x._1}/${x._2}").mkString(" ")
          (concatTag, tags.drop(ind))
        } else throw new IllegalStateException(s"Matching failed between $sent and $tags")

      matchUpLabels(sent.tail, rest, Label(compoundTag, char.toString) +: acc)
    }
}
