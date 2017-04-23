package kr.bydelta.koala.test.pack

import kr.bydelta.koala.test.core.TaggerSpec
import kr.bydelta.koala.traits.{CanCompileDict, CanTag}
import kr.bydelta.koala.twt.{Dictionary, Tagger}
import org.openkoreantext.processor.OpenKoreanTextProcessor
import org.specs2.execute.Result

/**
  * Created by bydelta on 16. 7. 26.
  */
class TwitterTaggerSpec extends TaggerSpec {

  override def tagSentByOrig(str: String): (String, String) = {
    val original = OpenKoreanTextProcessor.tokenize(OpenKoreanTextProcessor.normalize(str))

    val tag = original.map(_.text).mkString(" ").replaceAll("[ ]{2,}", "##").replaceAll(" ", "+").replaceAll("##", " ")
    "" -> tag
  }

  override def tagParaByOrig(str: String): Seq[String] = {
    val sentences = OpenKoreanTextProcessor.splitSentences(str).map(_.text)
    val original = sentences.map(s => OpenKoreanTextProcessor.tokenize(OpenKoreanTextProcessor.normalize(s)))

    original.map(_.map(_.text).mkString(" ").replaceAll("[ ]{2,}", "##").replaceAll(" ", "+").replaceAll("##", " "))
  }

  override def getTagger: CanTag[_] =
    new Tagger()

  override def getDict: CanCompileDict =
    Dictionary

  override def expectEmptyDict: Result = Result.unit(())

  override def expectNonEmptyDict: Result = Result.unit(())

  override def isSentenceSplitterImplemented: Boolean = false
}
