package kr.bydelta.koala.custom.trainer

import java.io.{File, FileInputStream, InputStream}

import opennlp.tools.util.{InputStreamFactory, PlainTextByLineStream}

/**
  * Created by bydelta on 17. 9. 9.
  */
trait Trainer[T, M] {
  def train(file: File, references: T*) = train(new FileInputStream(file), references: _*)

  def train(stream: InputStream, references: T*) = {
    val isf = new InputStreamFactory() {
      override def createInputStream(): InputStream = stream
    }
    val lineStream = new RawLinePreprocessorStream(new PlainTextByLineStream(isf, "UTF-8"))
    train(lineStream, references)
  }

  protected def train(lineStream: RawLinePreprocessorStream, references: Seq[T]): M
}
