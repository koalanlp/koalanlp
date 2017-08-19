package kr.bydelta.koala.custom.trainer

import java.io.{IOException, InputStream}

import kr.bydelta.koala.traits.CanSplitSentence
import opennlp.tools.sentdetect._
import opennlp.tools.util._

/**
  * Created by bydelta on 17. 8. 18.
  */
class SentenceSplitterTrainer {
  def train(stream: InputStream, splitter: CanSplitSentence = null) = {
    val isf = new InputStreamFactory() {
      override def createInputStream(): InputStream = stream
    }
    val lineStream = new PlainTextByLineStream(isf, "UTF-8")

    val sampleStream =
      if (splitter == null) new SentenceSampleStream(lineStream)
      else new RawSentenceSampleStream(lineStream, splitter)
    val sdf = new SentenceDetectorFactory("ko", true, null, null)

    SentenceDetectorME.train("ko", sampleStream, sdf, TrainingParameters.defaultParams())
  }
}

class RawSentenceSampleStream(sentences: ObjectStream[String], splitter: CanSplitSentence)
  extends FilterObjectStream[String, SentenceSample](new RawLinePreprocessorStream(sentences)) {

  @throws[IOException]
  def read: SentenceSample = {
    val string = replaceNewLineEscapeTags(samples.read)
    if (string != null && string != "") {
      val splitted = splitter.sentences(string)
      val sentencesString = new StringBuilder

      val spans = splitted.map {
        sentence =>
          val begin = sentence.length
          sentencesString append sentence
          val end = sentence.length
          sentencesString append ' '
          new Span(begin, end)
      }

      if (spans.nonEmpty)
        new SentenceSample(sentencesString, spans: _*)
      else
        null
    } else
      null
  }

  def replaceNewLineEscapeTags(s: String): String = s.replace("<LF>", "\n").replace("<CR>", "\r")
}

class RawLinePreprocessorStream(sentences: ObjectStream[String])
  extends FilterObjectStream[String, String](sentences) {
  @throws[IOException]
  def read: String = samples.read
}
