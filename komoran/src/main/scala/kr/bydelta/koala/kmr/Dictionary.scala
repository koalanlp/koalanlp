package kr.bydelta.koala.kmr

import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter}

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala._
import kr.bydelta.koala.traits.{CanExtractResource, CanUserDict}

import scala.io.Source

/**
  * 코모란 분석기 사용자사전
  */
object Dictionary extends CanUserDict with CanExtractResource {
  override protected val modelName: String = "komoran"
  /**
    * 사용자사전을 저장할 파일의 위치.
    */
  val userDict = new File(getExtractedPath, "koala.dict")
  userDict.deleteOnExit()

  override def addUserDictionary(dict: (String, POSTag)*): Unit = Dictionary synchronized {
    userDict.getParentFile.mkdirs()
    val bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(userDict, true)))
    dict.foreach {
      case (str, pos) =>
        bw.write(str)
        bw.write('\t')
        bw.write(tagToKomoran(pos))
        bw.newLine()
    }
    bw.close()
  }

  override def addUserDictionary(morph: String, tag: POSTag): Unit = Dictionary synchronized {
    userDict.getParentFile.mkdirs()
    val bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(userDict, true)))
    bw.write(morph)
    bw.write('\t')
    bw.write(tagToKomoran(tag))
    bw.newLine()
    bw.close()
  }

  override def items: Seq[(String, POSTag)] =
    Source.fromFile(userDict).getLines().map {
      line =>
        val segs = line.split('\t')
        segs(0) -> fromKomoranTag(segs(1))
    }.toSeq
}
