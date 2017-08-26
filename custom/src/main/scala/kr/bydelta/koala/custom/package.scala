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

  final def dissembleKorean(seq: Seq[Char]) = {
    seq.flatMap(_.toDissembledSeq.flatMap{
      c =>
        c match {
          // ㅏ ㅐ  ㅑ  ㅒ ㅓ  ㅔ ㅕ  ㅖ ㅗ  ㅘ ㅙ  ㅚ ㅛ ㅜ ㅝ  ㅞ  ㅟ ㅠ  ㅡ ㅢ  |
          // 61 62 63 64 65 66 67 68 69 6a 6b 6c 6d 6e 6f 70 71 72 73 74 75
          case '\u1163' => Seq('\u1175', '\u1161')
          case '\u1164' => Seq('\u1175', '\u1162')

          case '\u1167' => Seq('\u1175', '\u1165')
          case '\u1168' => Seq('\u1175', '\u1166')

          case '\u116a' => Seq('\u1169', '\u1161')
          case '\u116b' => Seq('\u1169', '\u1162')
          case '\u116c' => Seq('\u1169', '\u1175')
          case '\u116d' => Seq('\u1175', '\u1169')

          case '\u116f' => Seq('\u116e', '\u1165')
          case '\u1170' => Seq('\u116e', '\u1166')
          case '\u1171' => Seq('\u116e', '\u1175')
          case '\u1172' => Seq('\u1175', '\u116e')

          case '\u1174' => Seq('\u1173', '\u1175')

          // ㄹㅁ
          case '\u11b1' => Seq('\u11af', '\u11b7')
          case _ => Seq(c)
        }
    })
  }

  @tailrec
  final def assembleKorean(seq: Seq[Char], acc: Seq[Char] = Seq.empty): String =
    if (seq.isEmpty) acc.reverse.mkString
    else if (acc.isEmpty || (!seq.head.isJungsungJamo && !seq.head.isJongsungJamo))
      assembleKorean(seq.tail, seq.head +: acc)
    else {
      val head = seq.head
      val last = acc.head
      if (head.isJungsungJamo && last.isChosungJamo)
        assembleKorean(seq.tail, reconstructKorean(last - '\u1100', head - '\u1161') +: acc.tail)
      else
        assembleKorean(seq.tail, assemble(head, last) ++: acc.tail)
    }

  private def assemble(pre: Char, post: Char): Seq[Char] =
    (pre, post) match {
      case ('\u1175', '\u1161') => Seq('\u1163')
      case ('\u1175', '\u1162') => Seq('\u1164')
      case (ch, '\u1161') if ch.getJungsungCode == 20 && !ch.endsWithJongsung =>
        Seq(reconstructKorean(cho = ch.getChosungCode, jung = 2))
      case (ch, '\u1162') if ch.getJungsungCode == 20 && !ch.endsWithJongsung =>
        Seq(reconstructKorean(cho = ch.getChosungCode, jung = 3))

      case ('\u1175', '\u1165') => Seq('\u1167')
      case ('\u1175', '\u1166') => Seq('\u1168')
      case (ch, '\u1165') if ch.getJungsungCode == 20 && !ch.endsWithJongsung =>
        Seq(reconstructKorean(cho = ch.getChosungCode, jung = 6))
      case (ch, '\u1166') if ch.getJungsungCode == 20 && !ch.endsWithJongsung =>
        Seq(reconstructKorean(cho = ch.getChosungCode, jung = 7))

      case ('\u1169', '\u1161') => Seq('\u116a')
      case ('\u1169', '\u1162') => Seq('\u116b')
      case ('\u1169', '\u1175') => Seq('\u116c')
      case ('\u1175', '\u1169') => Seq('\u116d')
      case (ch, '\u1161') if ch.getJungsungCode == 8 && !ch.endsWithJongsung =>
        Seq(reconstructKorean(cho = ch.getChosungCode, jung = 9))
      case (ch, '\u1162') if ch.getJungsungCode == 8 && !ch.endsWithJongsung =>
        Seq(reconstructKorean(cho = ch.getChosungCode, jung = 10))
      case (ch, '\u1175') if ch.getJungsungCode == 8 && !ch.endsWithJongsung =>
        Seq(reconstructKorean(cho = ch.getChosungCode, jung = 11))
      case (ch, '\u1169') if ch.getJungsungCode == 20 && !ch.endsWithJongsung =>
        Seq(reconstructKorean(cho = ch.getChosungCode, jung = 12))

      case ('\u116e', '\u1165') => Seq('\u116f')
      case ('\u116e', '\u1166') => Seq('\u1170')
      case ('\u116e', '\u1175') => Seq('\u1171')
      case ('\u1175', '\u116e') => Seq('\u1172')
      case (ch, '\u1165') if ch.getJungsungCode == 13 && !ch.endsWithJongsung =>
        Seq(reconstructKorean(cho = ch.getChosungCode, jung = 14))
      case (ch, '\u1166') if ch.getJungsungCode == 13 && !ch.endsWithJongsung =>
        Seq(reconstructKorean(cho = ch.getChosungCode, jung = 15))
      case (ch, '\u1175') if ch.getJungsungCode == 13 && !ch.endsWithJongsung =>
        Seq(reconstructKorean(cho = ch.getChosungCode, jung = 16))
      case (ch, '\u116e') if ch.getJungsungCode == 20 && !ch.endsWithJongsung =>
        Seq(reconstructKorean(cho = ch.getChosungCode, jung = 17))

      case ('\u1173', '\u1175') => Seq('\u1174')
      case (ch, '\u1175') if ch.getJungsungCode == 18 && !ch.endsWithJongsung =>
        Seq(reconstructKorean(cho = ch.getChosungCode, jung = 19))

      // ㄹㅁ
      case ('\u11af', '\u11b7') => Seq('\u11b1')
      case (ch, '\u11b7') if ch.getJongsungCode == 8 =>
        Seq(reconstructKorean(cho = ch.getChosungCode, jung = ch.getJungsungCode, jong = 10))
      case _ => Seq(post, pre)
    }
}
