package kr.bydelta.koala.kryo

import com.esotericsoftware.kryo.io.{Input, Output}
import com.esotericsoftware.kryo.{Kryo, Serializer}
import kr.bydelta.koala.FunctionalTag
import kr.bydelta.koala.data.{Morpheme, Word}

/**
  * Created by bydelta on 16. 8. 5.
  */
object WordSerializer extends Serializer[Word] {
  override def write(kryo: Kryo, output: Output, value: Word): Unit = {
    output.writeString(value.surface)
    output.writeInt(value.size)
    value.morphemes.foreach(kryo.writeObject(output, _))
    output.writeString(value.rawDepTag)
    output.writeInt(value.depTag.id)
    output.writeInt(value.dependents.size)
    value.dependents.foreach(kryo.writeObject(output, _))
  }

  override def read(kryo: Kryo, input: Input, `type`: Class[Word]): Word = {
    val surface = input.readString
    val szMorph = input.readInt
    val morphs = (0 until szMorph).map {
      _ => kryo.readObject(input, classOf[Morpheme])
    }

    val word = new Word(surface = surface, morphemes = morphs)
    word.rawDepTag = input.readString
    word.depTag = FunctionalTag(input.readInt)
    val szDeps = input.readInt
    (0 until szDeps).foreach {
      _ =>
        word.dependents += kryo.readObject(input, `type`)
    }

    word
  }
}
