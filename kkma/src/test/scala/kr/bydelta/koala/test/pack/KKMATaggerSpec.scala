package kr.bydelta.koala.test.pack

import kr.bydelta.koala.kkma.{Dictionary, Tagger}
import kr.bydelta.koala.test.core.TaggerSpec
import kr.bydelta.koala.traits.{CanCompileDict, CanTag}
import org.snu.ids.ha.ma.MorphemeAnalyzer
import org.specs2.execute.Result

import scala.collection.JavaConverters._

/**
  * Created by bydelta on 16. 7. 26.
  */
class KKMATaggerSpec extends TaggerSpec {
  override def tagSentByOrig(str: String): (String, String) = {
    val kkma = new MorphemeAnalyzer

    val original = kkma.divideToSentences(kkma.leaveJustBest(
      kkma.postProcess(kkma.analyze(str)))).asScala.flatMap(_.asScala)

    val tag = original.map(_.asScala.map(m => m.getString + "/" + m.getTag).mkString("+")).mkString(" ")
    val surface = original.map(_.getExp).mkString(" ")

    surface -> tag
  }

  override def tagParaByOrig(str: String): Seq[String] = {
    val kkma = new MorphemeAnalyzer

    val original = kkma.divideToSentences(kkma.leaveJustBest(
      kkma.postProcess(kkma.analyze(str)))).asScala

    original.map(_.asScala.map(_.asScala.map(_.getString).mkString("+")).mkString(" "))
  }

  override def getTagger: CanTag =
    new Tagger()

  override def isSentenceSplitterImplemented: Boolean = false
}
