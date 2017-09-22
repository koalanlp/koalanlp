package kr.bydelta.koala.custom.trainer

import kr.bydelta.koala.traits.CanSplitSentence
import opennlp.tools.sentdetect._
import opennlp.tools.util._

/**
  * Created by bydelta on 17. 8. 18.
  */
class SentenceSplitterTrainer extends Trainer[CanSplitSentence, SentenceModel] {
  override def train(stream: RawLinePreprocessorStream, references: Seq[CanSplitSentence]) = {
    val sampleStream = new SentenceSampleStream(stream, references)
    val sdf = new SentenceDetectorFactory("ko", true, null, null)
    SentenceDetectorME.train("ko", sampleStream, sdf, TrainingParameters.defaultParams())
  }
}

class SentenceSampleStream(sents: RawLinePreprocessorStream, override protected val references: Seq[CanSplitSentence])
  extends ExampleStream[SentenceSample, CanSplitSentence](sents) {
  override def preprocess(str: String): String =
    str.replace("<LF>", "\n").replace("<CR>", "\r")

  override def buildSample(str: String, tool: CanSplitSentence): SentenceSample = {
    if (str != null && str != "") {
      val splitted = tool.sentences(str)
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
}
