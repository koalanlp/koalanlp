package kr.bydelta.koala.custom

import java.io.{File, FileInputStream, InputStream}

import kr.bydelta.koala.traits.CanSplitSentence
import opennlp.tools.sentdetect.{SentenceDetectorME, SentenceModel}

/**
  * Created by bydelta on 17. 6. 24.
  */
class SentenceSplitter(model: SentenceModel) extends CanSplitSentence {
  private val detector = new SentenceDetectorME(model)

  def this(stream: InputStream) {
    this(new SentenceModel(stream))
  }

  def this(path: String) {
    this(new FileInputStream(new File(path)))
  }

  def this() {
    this(SENT_MODEL)
  }

  override def sentences(text: String): Seq[String] = {
    detector.sentDetect(text).toSeq
  }
}
