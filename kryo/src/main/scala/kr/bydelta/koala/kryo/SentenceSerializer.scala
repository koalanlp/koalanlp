package kr.bydelta.koala.kryo

import com.esotericsoftware.kryo.io.{Input, Output}
import com.esotericsoftware.kryo.{Kryo, Serializer}
import kr.bydelta.koala.data.{Sentence, Word}

/**
  * KryoSerializer object for Sentence class
  */
object SentenceSerializer extends Serializer[Sentence] {
  override def write(kryo: Kryo, output: Output, value: Sentence): Unit = {
    output.writeInt(value.size)
    value.words.foreach(kryo.writeObject(output, _))
    output.writeInt(value.topLevels.size)
    value.topLevels.foreach(kryo.writeObject(output, _))
  }

  override def read(kryo: Kryo, input: Input, `type`: Class[Sentence]): Sentence = {
    val szWords = input.readInt
    val words = (0 until szWords).map {
      _ => kryo.readObject(input, classOf[Word])
    }

    val sent = new Sentence(words = words)
    val szTops = input.readInt
    (0 until szTops).foreach {
      _ => sent.topLevels += kryo.readObject(input, classOf[Word])
    }

    sent
  }
}
