package kr.bydelta.koala.kryo

import com.esotericsoftware.kryo.io.{Input, Output}
import com.esotericsoftware.kryo.{Kryo, Serializer}
import kr.bydelta.koala.FunctionalTag
import kr.bydelta.koala.data.Relationship

/**
  * KryoSerializer object for Word class
  */
object RelationSerializer extends Serializer[Relationship] {
  override def write(kryo: Kryo, output: Output, value: Relationship): Unit = {
    output.writeInt(value.head)
    output.writeInt(value.target)
    output.writeInt(value.relation.id)
    output.writeString(value.rawRel)
  }

  override def read(kryo: Kryo, input: Input, `type`: Class[Relationship]): Relationship = {
    val head = input.readInt
    val target = input.readInt
    val funcrel = FunctionalTag(input.readInt)
    val rawrel = input.readString

    Relationship(head, funcrel, rawrel, target)
  }
}
