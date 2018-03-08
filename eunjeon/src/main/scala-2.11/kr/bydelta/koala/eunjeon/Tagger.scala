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
    val tok = new Tokenizer(Dictionary.lexiconDict, Dictionary.connectionCostDict)
    if (Dictionary.nonEmpty) {
      Dictionary.reloadDic()
      tok.setUserDict(Dictionary.userDict)
    }
    tok
  }

  override private[koala] def convertSentence(text: String, result: Seq[Eojeol]): Sentence =
    Sentence(
      result.map {
        eojeol =>
          Word(
            eojeol.surface,
            eojeol.nodes.flatMap(node => Dictionary.convertMorpheme(node.morpheme))
          )
      }
    )

  override def tagSentenceOriginal(text: String): Seq[Eojeol] =
    Eojeoler.build(tokenizer.parseText(text, dePreAnalysis = false))
}