package kr.bydelta.koala.test.pack

import kr.bydelta.koala.kmr.{Dictionary, Tagger}
import kr.bydelta.koala.test.core.TaggerSpec
import kr.bydelta.koala.traits.{CanCompileDict, CanTag}
import kr.co.shineware.nlp.komoran.core.analyzer.Komoran
import org.specs2.execute.Result

import scala.collection.JavaConverters._

/**
  * Created by bydelta on 16. 7. 26.
  */
class KomoranTaggerSpec extends TaggerSpec {
  override def tagSentByOrig(str: String): (String, String) = {
    val komoran = new Komoran(Dictionary.extractResource())
    val original = komoran.analyze(str).asScala

    val tag = original.map(_.asScala.map(_.getFirst).mkString("+")).mkString(" ")

    "" -> tag
  }

  override def tagSentByKoala(str: String, tagger: CanTag[_]): (String, String) = {
    val (surface, tag) = super.tagSentByKoala(str, tagger)
    str -> tag
  }

  override def tagParaByOrig(str: String): Seq[String] = Seq.empty

  override def getTagger: CanTag[_] =
    new Tagger()

  override def getDict: CanCompileDict =
    Dictionary

  override def expectEmptyDict: Result =
    Dictionary.userDict.length() must_== 0l

  override def expectNonEmptyDict: Result =
    Dictionary.userDict.length() must be_>(0l)

  override def isSentenceSplitterImplemented: Boolean = true
}
