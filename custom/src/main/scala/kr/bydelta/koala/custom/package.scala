package kr.bydelta.koala

import opennlp.tools.parser.ParserModel
import opennlp.tools.postag.POSModel
import opennlp.tools.sentdetect.SentenceModel
import opennlp.tools.tokenize.TokenizerModel

/**
  * Created by bydelta on 17. 6. 24.
  */
package object custom {
  final lazy val SENT_MODEL = new SentenceModel(
    this.getClass.getClassLoader.getResourceAsStream(SENT_MODEL_PATH))
  final lazy val TOK_MODEL = new TokenizerModel(
    this.getClass.getClassLoader.getResourceAsStream(TOK_MODEL_PATH))
  final lazy val POS_MODEL = new POSModel(
    this.getClass.getClassLoader.getResourceAsStream(POS_MODEL_PATH))
  final lazy val DEP_MODEL = new ParserModel(
    this.getClass.getClassLoader.getResourceAsStream(DEP_MODEL_PATH))
  final val SENT_MODEL_PATH = "data/sent.model"
  final val TOK_MODEL_PATH = "data/tok.model"
  final val POS_MODEL_PATH = "data/pos.model"
  final val DEP_MODEL_PATH = "data/dep.model"
}
