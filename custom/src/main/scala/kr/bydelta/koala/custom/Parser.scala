package kr.bydelta.koala.custom

import java.io.{File, FileInputStream, InputStream}

import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.traits.CanDepParse
import opennlp.tools.cmdline.parser.ParserTool
import opennlp.tools.parser.{ParserFactory, ParserModel}

/**
  * Created by bydelta on 17. 6. 24.
  */
class Parser(model: ParserModel) extends CanDepParse {
  private val detector = ParserFactory.create(model)

  def this(stream: InputStream) {
    this(new ParserModel(stream))
  }

  def this(path: String) {
    this(new FileInputStream(new File(path)))
  }

  def this() {
    this(DEP_MODEL)
  }

  override def parse(sentence: String): Sentence = {
    ParserTool.
  }

  /**
    * Sentence 객체의 의존구문을 분석함.
    *
    * @param sentence 의존구문분석을 할 Sentence 객체.
    * @return 의존구문분석 결과가 포함된 Sentence 객체. (입력값과 동일한 객체)
    */
  override def parse(sentence: Sentence): Sentence = ???
}
