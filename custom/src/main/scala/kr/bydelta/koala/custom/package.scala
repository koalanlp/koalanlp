package kr.bydelta.koala

import kr.bydelta.koala.KoreanCharacterExtension
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

  final lazy val RULE_JUNG = Map(
    'ㅏ' -> Seq('\u1161'),
    'ㅑ' -> Seq('\u1163'),
    'ㅓ' -> Seq('\u1165'),
    'ㅕ' -> Seq('\u1167'),
    'ㅗ' -> Seq('\u1169'),
    'ㅛ' -> Seq('\u116d'),
    'ㅜ' -> Seq('\u116e'),
    'ㅠ' -> Seq('\u1172'),
    'ㅡ' -> Seq('\u1173'),
    'ㅣ' -> Seq('\u1175'),
    'ㅐ' -> Seq('\u1161', '\u1175'),
    'ㅒ' -> Seq('\u1163', '\u1175'),
    'ㅔ' -> Seq('\u1165', '\u1175'),
    'ㅖ' -> Seq('\u1167', '\u1175'),
    'ㅢ' -> Seq('\u1173', '\u1175'),
    'ㅘ' -> Seq('\u1169', '\u1161'),
    'ㅚ' -> Seq('\u1169', '\u1175'),
    'ㅝ' -> Seq('\u116e', '\u1165'),
    'ㅟ' -> Seq('\u116e', '\u1175'),
    'ㅙ' -> Seq('\u1169', '\u1161', '\u1175'),
    'ㅞ' -> Seq('\u116e', '\u1165', '\u1175')
  ).map(t => (t._1 - 8174).toChar -> t._2)

  final lazy val RULE_CHO = (0x1100 to 0x1112).map(_.toChar).toSet
  final lazy val RULE_JONG = Map(
    0x11a7 -> Seq('\u11a7'),
    0x11a8 -> Seq('\u11a8'), //ㄱ/
    0x11bf -> Seq('\u11bf'), //ㅋ/
    0x11a9 -> Seq('\u11a9'), //ㄲ/
    0x11aa -> Seq('\u11a8', '\u11ba'), //ㄱㅅ
    0x11ab -> Seq('\u11ab'), //ㄴ
    0x11ac -> Seq('\u11ab', '\u11bd'), //ㄴㅈ
    0x11ad -> Seq('\u11ab', '\u11c2'), //ㄴㅎ
    0x11ae -> Seq('\u11ae'), //ㄷ
    0x11c0 -> Seq('\u11c0'), //ㅌ/
    0x11af -> Seq('\u11af'), //ㄹ
    0x11b0 -> Seq('\u11af', '\u11a8'), //ㄹㄱ
    0x11b1 -> Seq('\u11af', '\u11b7'), //ㄹㅁ
    0x11b2 -> Seq('\u11af', '\u11b8'), //ㄹㅂ/
    0x11b3 -> Seq('\u11af', '\u11ba'), //ㄹㅅ/
    0x11b4 -> Seq('\u11af', '\u1110'), //ㄹㅌ/
    0x11b5 -> Seq('\u11af', '\u11c1'), //ㄹㅍ/
    0x11b6 -> Seq('\u11af', '\u11c2'), //ㄹㅎ/
    0x11b7 -> Seq('\u11b7'), //ㅁ/
    0x11b8 -> Seq('\u11b8'), //ㅂ/
    0x11c1 -> Seq('\u11c1'), //ㅍ/
    0x11b9 -> Seq('\u11b8', '\u11ba'), //ㅂㅅ/
    0x11ba -> Seq('\u11ba'), //ㅅ/
    0x11bb -> Seq('\u11bb'), //ㅆ/
    0x11bd -> Seq('\u11bd'), //ㅈ/
    0x11be -> Seq('\u11be'), //ㅊ/
    0x11bc -> Seq('\u11bc'), //ㅇ/
    0x11c2 -> Seq('\u11c2') //ㅎ/
  ).map(t => t._1.toChar -> (t._2 :+ 7))

  final def toCodePoints(seq: Seq[Char]) = {
    seq.flatMap(_.toDissembledSeq.flatMap{
      c =>
        if (RULE_CHO(c)) Seq(c) // Range: 0 ~ 6
        else if (RULE_JUNG.contains(c)) RULE_JUNG(c) // Range: 8 ~ 17
        else if (RULE_JONG.contains(c)) RULE_JONG(c) // Range: 0 ~ 7
        else if (c.isDigit) Seq('0')
        else if (c.isLetter) Seq('A')
        else if (c.isWhitespace) Seq(' ')
        else Seq(c)
    })
  }
}
