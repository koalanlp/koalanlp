package kr.bydelta.koala.helper

import java.io.IOException

import org.snu.ids.ha.dic.SimpleDicReader

import scala.collection.mutable.ArrayBuffer

class UserDicReader extends SimpleDicReader {
  private val morphemes = ArrayBuffer[String]()
  private var seek: Int = 0

  def ++=(map: Seq[(String, String)]) {
    morphemes ++= map.map {
      case (morph, tag) => s"$morph/$tag"
    }
  }

  def +=(morph: String, tag: String) = {
    morphemes += s"$morph/$tag"
  }

  @throws[IOException]
  override def cleanup() {}

  def reset() = {
    seek = 0
  }

  @throws[IOException]
  def readLine: String = {
    if (seek == morphemes.size) {
      seek = 0
      null
    } else {
      seek += 1
      morphemes(seek - 1)
    }
  }
}
