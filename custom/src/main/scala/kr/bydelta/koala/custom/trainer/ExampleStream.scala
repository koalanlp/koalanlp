package kr.bydelta.koala.custom.trainer

import java.io.IOException

import opennlp.tools.util.{FilterObjectStream, ObjectStream}

/**
  * Created by bydelta on 17. 9. 9.
  */
trait ExampleStream[SAMPLE, REF] extends FilterObjectStream[String, SAMPLE] {
  protected val references: Seq[REF]
  private var refId = -1
  private var sample = ""

  def preprocess(str: String): String

  def buildSample(str: String, tool: REF): SAMPLE

  @throws[IOException]
  def read: SAMPLE = {
    refId = (refId + 1) % references.length
    if (refId == 0) {
      sample = samples.read()
    }

    buildSample(preprocess(sample), references(refId))
  }
}

class RawLinePreprocessorStream(sentences: ObjectStream[String])
  extends FilterObjectStream[String, String](sentences) {
  @throws[IOException]
  def read: String = samples.read
}