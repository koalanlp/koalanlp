package kr.bydelta.koala.helper

import org.snu.ids.ha.dic.SimpleDicReader

import scala.collection.mutable.ArrayBuffer

/**
  * 꼬꼬마 사용자사전을 실시간으로 반영하기 위한 Reader.
  */
private[koala] class UserDicReader extends SimpleDicReader with Iterator[String] {
  /**
    * 형태소 리스트.
    */
  private[koala] val morphemes = ArrayBuffer[String]()
  /**
    * 파일스트림 모사를 위한 현재위치 Marker.
    */
  private var iterator: Iterator[String] = morphemes.iterator

  /**
    * 사전에 (형태소,품사)리스트를 추가.
    *
    * @param map 추가할 (형태소,품사)리스트.
    */
  def ++=(map: Seq[(String, String)]) {
    morphemes ++= map.map {
      case (morph, tag) => s"$morph/$tag"
    }
  }

  /**
    * 사전에 형태소-품사를 추가.
    *
    * @param morph 추가할 형태소.
    * @param tag   형태소의 품사.
    */
  def +=(morph: String, tag: String) {
    morphemes += s"$morph/$tag"
  }

  override def cleanup() {}

  override def hasNext: Boolean = iterator.hasNext

  override def next(): String = readLine

  override def readLine: String = {
    if (iterator == null)
      reset()
    if (!iterator.hasNext) {
      null
    } else {
      iterator.next()
    }
  }

  /**
    * 위치 초기화. (반복읽기를 위함.)
    */
  def reset(): Unit = {
    iterator = morphemes.iterator
  }
}
