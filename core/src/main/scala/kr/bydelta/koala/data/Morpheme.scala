package kr.bydelta.koala.data

import java.io.Serializable

import kr.bydelta.koala.Processor.Processor

/**
  * Created by bydelta on 16. 7. 20.
  */
@SerialVersionUID(586158448235147684L)
class Morpheme(val morpheme: String, val rawTag: String, var processor: Processor) extends Serializable {
  val tag = processor integratedPOSOf rawTag

  final def isNoun: Boolean = tag.toString.startsWith("N")

  final def isVerb: Boolean = tag.toString.startsWith("V")

  final def isModifier: Boolean = tag.toString.startsWith("M")

  final def isJosa: Boolean = tag.toString.startsWith("J")

  final def hasTag(tag: String): Boolean = this.tag.toString.startsWith(tag)

  final def hasRawTag(tag: String): Boolean = rawTag.startsWith(tag)

  def equalsWithoutTag(another: Morpheme): Boolean = another.morpheme == this.morpheme

  override def toString: String = s"$morpheme($tag/$rawTag)"

  override def equals(obj: Any): Boolean = {
    obj match {
      case m: Morpheme =>
        m.morpheme == this.morpheme && m.tag == this.tag
      case s: String =>
        s == this.morpheme
      case _ =>
        false
    }
  }
}
