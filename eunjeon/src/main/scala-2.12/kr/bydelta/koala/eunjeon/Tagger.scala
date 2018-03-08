package kr.bydelta.koala.eunjeon

import kr.bydelta.koala.data.{Sentence, Word}
import kr.bydelta.koala.traits.CanTagOnlyASentence
import org.bitbucket.eunjeon.seunjeon._

/**
  * 은전한닢 통합분석기
  */
class Tagger extends CanTagOnlyASentence[Seq[Eojeol]] {
  /**
    * 은전한닢의 내장 Tokenizer.
    */
  lazy val tokenizer: Tokenizer = {
    val tok = new Tokenizer(Dictionary.lexiconDict, Dictionary.connectionCostDict, false)
    if (Dictionary.nonEmpty) {
      Dictionary.reloadDic()
      tok.setUserDict(Dictionary.userDict)
    }
    tok
  }

  /**
    * 변환되지않은, 분석결과를 반환.
    *
    * @param text 분석할 String.
    * @return 원본 문단객체의 Sequence
    */
  override def tagSentenceOriginal(text: String): Seq[Eojeol] = {
    Eojeoler.build(tokenizer.parseText(text, dePreAnalysis = false)).flatMap(_.eojeols).toSeq
  }

  /**
    * 분석결과를 변환함.
    *
    * @param text   품사분석을 시행할 문단 String
    * @param result 변환할 분석결과.
    * @return 변환된 Sentence 객체
    */
  override private[koala] def convertSentence(text: String, result: Seq[Eojeol]) =
    Sentence(
      result.map {
        eojeol =>
          Word(
            eojeol.surface,
            eojeol.nodes.flatMap(node => Dictionary.convertMorpheme(node.morpheme))
          )
      }
    )
}