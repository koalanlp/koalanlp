package kr.bydelta.koala.kmr

import kr.bydelta.koala.POS
import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.traits.{CanTag, CanTagOnlyASentence}
import kr.bydelta.koala.util.{SentenceSplitter, reduceVerbApply, reunionKorean}
import kr.co.shineware.nlp.komoran.core.Komoran
import kr.co.shineware.nlp.komoran.model.KomoranResult

import scala.collection.mutable.ArrayBuffer

/**
  * 코모란 형태소분석기.
  */
class Tagger extends CanTagOnlyASentence[KomoranResult] {
  /**
    * 코모란 분석기 객체.
    */
  lazy val komoran = {
    val komoran = Tagger.komoran
    if (Dictionary.userDict.exists())
      komoran.setUserDic(Dictionary.userDict.getAbsolutePath)
    komoran
  }

  override def tagSentenceOriginal(text: String): KomoranResult =
    komoran.analyze(text)

  override private[koala] def convertSentence(text: String, result: KomoranResult): Sentence = {
    if (result != null) {
      val words = ArrayBuffer[Word]()
      var morphs = ArrayBuffer[Morpheme]()
      var lastIdx = 0
      val tokenIt = result.getTokenList.iterator()

      while (tokenIt.hasNext) {
        val token = tokenIt.next()
        val tag = token.getPos
        if (token.getBeginIndex > lastIdx) {
          words += Word(surface = constructWordSurface(morphs), morphemes = morphs)
          morphs = ArrayBuffer[Morpheme]()
        }

        morphs += Morpheme(surface = token.getMorph, rawTag = token.getPos, fromKomoranTag(tag))
        lastIdx = token.getEndIndex
      }

      if (morphs.nonEmpty) {
        words += Word(surface = constructWordSurface(morphs), morphemes = morphs)
      }

      Sentence(words)
    } else
      Sentence(Seq.empty)
  }

  def constructWordSurface(wAsScala: Seq[Morpheme]) = {
    reunionKorean(wAsScala.foldLeft((Seq.empty[Char], false))({
      case ((prevSeq, wasVerb), curr) =>
        val tag = curr.rawTag.toUpperCase
        if (tag.startsWith("E")) {
          val newSeq = reduceVerbApply(prevSeq, wasVerb, curr.surface.toSeq)
          (newSeq, false)
        } else {
          val isVerb = (tag.startsWith("V") && tag != "VA") || tag == "XSV"
          (prevSeq ++ curr.surface.toSeq, isVerb)
        }
    })._1)
  }
}

/**
  * 코모란 분석기의 Companion object.
  */
private[koala] object Tagger {
  /**
    * 코모란 분석기 객체.
    */
  private lazy val komoran = {
    Dictionary.extractResource()
    val komoran = new Komoran()
    komoran
  }
}

