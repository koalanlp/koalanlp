package kr.bydelta.koala

import opennlp.tools.parser.ParserModel
import opennlp.tools.postag.POSModel
import opennlp.tools.sentdetect.SentenceModel
import opennlp.tools.tokenize.TokenizerModel

import scala.annotation.tailrec

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

  /**
    * 자모문자열로 분해된 한글 문자열 재구성
    *
    * @param seq     재구성할 문자열.
    * @param builder (누적변수)
    * @return 재구성 된 문자열.
    */
  @tailrec
  final def assembleKorean(seq: Seq[Char], builder: StringBuilder = new StringBuilder): String =
  if (seq.isEmpty) builder.toString
  else {
    val head = seq.head
    if (seq.length > 2) {
      val second = seq(1)
      val third = seq(2)

      if (head.isChosungJamo && second.isJungsungJamo && third.isJongsungJamo) {
        builder.append(reconstructKorean(
          cho = head - 0x1100,
          jung = second - 0x1161,
          jong = third - 0x11A7
        ))
        assembleKorean(seq.drop(3), builder)
      } else {
        builder.append(head)
        assembleKorean(seq.tail, builder)
      }
    } else {
      builder.appendAll(seq)
      builder.toString
    }
  }
}
