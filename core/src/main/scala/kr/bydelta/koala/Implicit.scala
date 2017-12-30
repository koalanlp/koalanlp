package kr.bydelta.koala

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.traits.{CanDepParse, CanSplitSentence, CanTag}

import scala.language.implicitConversions

/**
  * Object for implicit conversions.
  */
object Implicit {
  /**
    * Implicit conversion from POSTag into Boolean filter
    *
    * @param pos POSTag to be checked
    * @return Boolean function, which gives true if morpheme has given pos tag.
    */
  implicit def filterMorpheme(pos: POSTag): Morpheme => Boolean = (m: Morpheme) => m.tag == pos

  /**
    * Implicit conversion from POSTags into Boolean filter
    *
    * @param poses Sequence of POSTag to be checked
    * @return Boolean function, which gives true if morpheme has given pos tags.
    */
  implicit def filterMorpheme(poses: collection.Seq[POSTag]): Morpheme => Boolean = {
    val set = poses.toSet
    (m: Morpheme) => set.contains(m.tag)
  }

  /**
    * Implicit conversion from POSTag into Boolean filter
    *
    * @param pos POSTag to be checked
    * @return Boolean function, which gives true if word contains given pos tag.
    */
  implicit def filterWord(pos: POSTag): Word => Boolean = (w: Word) => w.exists(pos)

  /**
    * Implicit conversion from POSTags into Boolean filter
    *
    * @param poses Sequence of POSTags to be checked
    * @return Boolean function, which gives true if word contains given pos tags.
    */
  implicit def filterWord(poses: collection.Seq[POSTag]): Word => Boolean = (w: Word) => w.exists(poses)

  /**
    * String operation
    *
    * @param str String
    */
  implicit class StringOp(str: String) {
    /**
      * Tag given sentence.
      *
      * @param tagger Tagger to be used
      * @return Sequence of Sentence instances (tagged)
      */
    def toTagged(implicit tagger: CanTag): Seq[Sentence] = tagger.tag(str)

    /**
      * Parse given sentence
      *
      * @param parser Parser to be used
      * @return Sentence instance (parsed)
      */
    def toParsed(implicit parser: CanDepParse): Seq[Sentence] = parser.parse(str)

    /**
      * Split string into sentences
      *
      * @param split SentenceSplitter to be used
      * @return Sequence of Sentence Strings
      */
    def sentences(implicit split: CanSplitSentence): Seq[String] = split.sentences(str)
  }

  /**
    * Sentence operation
    *
    * @param sent Sentence
    */
  implicit class SentenceOp(sent: Sentence) {
    /**
      * Parse given sentence
      *
      * @param parser Parser to be used
      * @return Sentence instance (parsed)
      */
    def toParsed(implicit parser: CanDepParse): Sentence = parser.parse(sent)
  }

  /**
    * Sentence Sequence operation
    *
    * @param sent Sentence Seq
    */
  implicit class SentenceSeqOp(sent: Seq[Sentence]) {
    /**
      * Parse given sentences
      *
      * @param parser Parser to be used
      * @return Sentence Seq instance (parsed)
      */
    def toParsed(implicit parser: CanDepParse): Seq[Sentence] = parser.parse(sent)
  }

}
