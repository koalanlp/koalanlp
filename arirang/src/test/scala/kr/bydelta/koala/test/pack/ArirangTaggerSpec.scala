package kr.bydelta.koala.test.pack

import java.util

import kr.bydelta.koala.arirang._
import kr.bydelta.koala.test.core.TaggerSpec
import kr.bydelta.koala.traits.{CanCompileDict, CanTag}
import org.apache.lucene.analysis.ko.morph.{AnalysisOutput, WordSegmentAnalyzer}
import org.specs2.execute.Result

import scala.collection.JavaConverters._

/**
  * Created by bydelta on 16. 7. 26.
  */
class ArirangTaggerSpec extends TaggerSpec {
  val tagger = new WordSegmentAnalyzer

  override def tagSentByOrig(str: String): (String, String) = {
    val list = new util.LinkedList[util.List[AnalysisOutput]]()
    tagger.analyze(str, list, false)
    val original = list.asScala.map(_.asScala.maxBy(_.getScore))

    val tag = original.map(_.toString).mkString.replaceAll("[NVZ\\s\\.,\\(\\)]+", "")

    "" -> tag
  }

  override def tagSentByKoala(str: String, tagger: CanTag): (String, String) = {
    val tagged = tagger.tagSentence(str)
    val tag = tagged.map(_.map(m => m.surface + "(" + m.rawTag.last + ")").mkString(","))
      .mkString.replaceAll("[NVZ\\s\\.,\\(\\)]+", "")
    val surface = tagged.surfaceString()

    surface -> tag
  }

  override def tagParaByOrig(str: String): Seq[String] = Seq.empty

  override def getTagger: CanTag =
    new Tagger()

  override def isSentenceSplitterImplemented: Boolean = true
}
