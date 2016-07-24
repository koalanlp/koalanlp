package kr.bydelta.koala.kmr

import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter}

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.Processor
import kr.bydelta.koala.traits.{CanExtractResource, CanUserDict}

/**
  * Created by bydelta on 16. 7. 24.
  */
object Dictionary extends CanUserDict with CanExtractResource {
  override protected val modelName: String = "komoran"
  val userDict = new File(getExtractedPath, "koala.dict")

  override def addUserDictionary(dict: (String, POSTag)*): Unit = {
    userDict.getParentFile.mkdirs()
    val bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(userDict, true)))
    dict.foreach {
      case (str, pos) =>
        bw.write(str)
        bw.write('\t')
        bw.write(Processor.Komoran originalPOSOf pos)
        bw.newLine()
    }
    bw.close()
  }

  override def addUserDictionary(morph: String, tag: POSTag): Unit = {
    userDict.getParentFile.mkdirs()
    val bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(userDict, true)))
    bw.write(morph)
    bw.write('\t')
    bw.write(Processor.Komoran originalPOSOf tag)
    bw.newLine()
    bw.close()
  }
}
