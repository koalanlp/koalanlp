package kr.bydelta.koala

import java.io._

import com.twitter.chill.{Input => In, Output => Out}
import kr.bydelta.koala.traits.CanCompileDict

/**
  * package for Kryo serialization support
  */
package object kryo {

  /**
    * Implicit class for dictionary I/O function.
    *
    * @param dict Dictionary object for I/O
    */
  implicit class DictionaryIO(dict: CanCompileDict) {
    /**
      * Save this dictionary to given stream.
      *
      * @param stream Target stream
      */
    def saveTo(stream: OutputStream): Unit = >>(stream)

    /**
      * Save this dictionary to given stream
      *
      * @param stream Target stream
      */
    def >>(stream: OutputStream): Unit = {
      val output = new Out(stream)

      val list = dict.items
      output.writeInt(list.size)

      list.foreach {
        case (surface, pos) =>
          output.writeString(surface)
          output.writeInt(pos.id)
      }

      output.close()
    }

    /**
      * Save this dictionary to given file.
      *
      * @param file Target file
      */
    def saveTo(file: File): Unit = >>(file)

    /**
      * Save this dictionary to given file.
      *
      * @param file   Target file.
      * @param append True if append dictionary onto that file. (default: false)
      */
    def >>(file: File, append: Boolean = false): Unit = >>(new FileOutputStream(file, append))

    /**
      * Read the dictionary from given stream
      *
      * @param stream Source stream
      */
    def readFrom(stream: InputStream): Unit = <<(stream)

    /**
      * Read the dictionary from given file
      *
      * @param file Source file.
      */
    def readFrom(file: File): Unit = <<(file)

    /**
      * Read the dictionary from given file
      *
      * @param file Source file.
      */
    def <<(file: File): Unit = <<(new FileInputStream(file))

    /**
      * Read the dictionary from given stream
      *
      * @param stream Source stream
      */
    def <<(stream: InputStream): Unit = {
      val input = new In(stream)

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
  }

}
