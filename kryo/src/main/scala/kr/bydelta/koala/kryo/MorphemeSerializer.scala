package kr.bydelta.koala.kryo

import com.esotericsoftware.kryo.io.{Input, Output}
import com.esotericsoftware.kryo.{Kryo, Serializer}
import kr.bydelta.koala.POS
import kr.bydelta.koala.data.Morpheme

/**
  * KryoSerializer object for Morpheme class
  */
object MorphemeSerializer extends Serializer[Morpheme] {

  override def write(kryo: Kryo, output: Output, value: Morpheme): Unit = {
    output.writeString(value.surface)
    output.writeString(value.rawTag)
    output.writeInt(value.tag.id)
  }

  override def read(kryo: Kryo, input: Input, cls: Class[Morpheme]): Morpheme = {
    val surface = input.readString
    val rawTag = input.readString
    val tag = POS(input.readInt)
    Morpheme(surface, rawTag, tag)
  }
}
