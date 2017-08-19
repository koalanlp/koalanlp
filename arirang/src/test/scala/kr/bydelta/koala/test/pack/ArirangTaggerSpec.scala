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

    val tag = original.map(_.toString).mkString("").replaceAll("\\(N\\)", "").replaceAll("\\s+", "")
    val surface = original.map(_.getSource.trim).filter(_.nonEmpty).mkString(" ")

    surface -> tag
  }

  override def tagSentByKoala(str: String, tagger: CanTag[_]): (String, String) = {
    val tagged = tagger.tagSentence(str)
    val tag = tagged.map(_.map(m => m.surface + "(" + m.rawTag.last + ")").mkString(",")).mkString("").replaceAll("\\(N\\)", "").replaceAll("\\s+", "")
    val surface = tagged.surfaceString()
    surface -> tag
  }

  override def tagParaByOrig(str: String): Seq[String] = Seq.empty

  override def getTagger: CanTag[_] =
    new Tagger()

  override def getDict: CanCompileDict =
    Dictionary

  override def expectEmptyDict: Result = Result.unit(())

  override def expectNonEmptyDict: Result = Result.unit(())

  override def isSentenceSplitterImplemented: Boolean = false

  override def isParagraphImplemented: Boolean = false
}
