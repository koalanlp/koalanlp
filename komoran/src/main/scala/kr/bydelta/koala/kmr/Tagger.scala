package kr.bydelta.koala.kmr

import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.traits.CanTagOnlyASentence
import kr.bydelta.koala.util.{reduceVerbApply, reunionKorean}
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran
import kr.co.shineware.nlp.komoran.model.KomoranResult

import scala.collection.mutable.ArrayBuffer

/**
  * 코모란 형태소분석기.
  *
  * @param useLightTagger KOMORAN Light Tagger 사용시 true.
  */
class Tagger(useLightTagger: Boolean) extends CanTagOnlyASentence[KomoranResult] {

  /**
    * 코모란 분석기 객체.
    */
  lazy val komoran: Komoran = {
    val komoran =
      if (useLightTagger) Tagger.komoran_light
      else Tagger.komoran_full
    if (Dictionary.userDict.exists())
      komoran.setUserDic(Dictionary.userDict.getAbsolutePath)
    komoran
  }

  /**
    * 코모란 형태소분석기. (Full Tagger)
    */
  def this() = this(false)

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

        morphs += Morpheme(surface = token.getMorph, rawTag = token.getPos, toSejongPOS(tag))
        lastIdx = token.getEndIndex
      }

      if (morphs.nonEmpty) {
        words += Word(surface = constructWordSurface(morphs), morphemes = morphs)
      }

      Sentence(words)
    } else
      Sentence.empty
  }

  def constructWordSurface(wAsScala: Seq[Morpheme]): String = {
    reunionKorean(wAsScala.foldLeft((Seq.empty[Char], false))({
      case ((prevSeq, wasVerb), curr) =>
        val tag = curr.rawTag.toUpperCase
        if (tag.startsWith("E") && prevSeq.nonEmpty) {
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
  private lazy val komoran_full = {
    Dictionary.extractResource()
    val komoran = new Komoran(DEFAULT_MODEL.FULL)
    komoran
  }

  /**
    * 코모란 분석기 객체.
    */
  private lazy val komoran_light = {
    Dictionary.extractResource()
    val komoran = new Komoran(DEFAULT_MODEL.LIGHT)
    komoran
  }
}

