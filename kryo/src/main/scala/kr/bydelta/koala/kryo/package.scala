package kr.bydelta.koala

import java.io._

import com.twitter.chill.{Input, Output, ScalaKryoInstantiator}
import kr.bydelta.koala.traits.CanUserDict

/**
  * Created by bydelta on 16. 8. 5.
  */
package object kryo {

  implicit class DictionaryIO(dict: CanUserDict) {
    def saveTo(stream: OutputStream): Unit = >>(stream)

    def saveTo(file: File): Unit = >>(file)

    def >>(file: File, append: Boolean = false): Unit = >>(new FileOutputStream(file, append))

    def >>(stream: OutputStream): Unit = {
      val kryo = KryoWrap.kryo
      val output = new Output(stream)

      val list = dict.items
      output.writeInt(list.size)

      list.foreach {
        case (surface, pos) =>
          output.writeString(surface)
          output.writeInt(pos.id)
      }

      output.close()
    }

    def readFrom(stream: InputStream): Unit = <<(stream)

    def <<(stream: InputStream): Unit = {
      val kryo = new ScalaKryoInstantiator().newKryo()
      val input = new Input(stream)

      val sz = input.readInt
      val items = (0 until sz).map {
        _ =>
          val surface = input.readString()
          val pos = POS(input.readInt)
          surface -> pos
      }

      input.close()
      dict.addUserDictionary(items: _*)
    }

    def readFrom(file: File): Unit = <<(file)

    def <<(file: File): Unit = <<(new FileInputStream(file))
  }

}
