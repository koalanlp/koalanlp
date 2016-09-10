package kr.bydelta.koala.kryo

import com.esotericsoftware.kryo.io.{Input, Output}
import com.esotericsoftware.kryo.{Kryo, Serializer}
import kr.bydelta.koala.data.{Morpheme, Relationship, Word}

/**
  * KryoSerializer object for Word class
  */
object WordSerializer extends Serializer[Word] {
  override def write(kryo: Kryo, output: Output, value: Word): Unit = {
    output.writeString(value.surface)
    output.writeInt(value.size)
    value.morphemes.foreach(kryo.writeObject(output, _))
    output.writeInt(value.dependents.size)
    value.dependents.foreach(kryo.writeObject(output, _))
  }

  override def read(kryo: Kryo, input: Input, `type`: Class[Word]): Word = {
    val surface = input.readString
    val szMorph = input.readInt
    val morphs = (0 until szMorph).map {
      _ => kryo.readObject(input, classOf[Morpheme])
    }

    val word = Word(surface, morphs)
    val szDeps = input.readInt
    word.deps = (0 until szDeps).map {
      _ => kryo.readObject(input, classOf[Relationship])
    }.toSet

    word
  }
}
