package kr.bydelta.koala.kryo

import com.esotericsoftware.kryo.io.{Input, Output}
import com.esotericsoftware.kryo.{Kryo, Serializer}
import kr.bydelta.koala.Processor
import kr.bydelta.koala.data.Morpheme

/**
  * Created by bydelta on 16. 8. 5.
  */
object MorphemeSerializer extends Serializer[Morpheme] {

  override def write(kryo: Kryo, output: Output, value: Morpheme): Unit = {
    output.writeString(value.surface)
    output.writeString(value.rawTag)
    output.writeInt(value.processor.id)
  }

  override def read(kryo: Kryo, input: Input, cls: Class[Morpheme]): Morpheme = {
    val surface = input.readString
    val rawTag = input.readString
    val proc = Processor(input.readInt)
    new Morpheme(surface = surface, rawTag = rawTag, processor = proc)
  }
}
