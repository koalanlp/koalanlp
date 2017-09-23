package kr.bydelta.koala.test.pack

import kr.bydelta.koala.test.core.TaggerSpec
import kr.bydelta.koala.traits.CanTag
import kr.bydelta.koala.twt.Tagger
import org.openkoreantext.processor.OpenKoreanTextProcessor
import org.openkoreantext.processor.util.KoreanPos

/**
  * Created by bydelta on 16. 7. 26.
  */
class TwitterTaggerSpec extends TaggerSpec {

  override def tagSentByOrig(str: String): (String, String) = {
    val original = OpenKoreanTextProcessor.tokenize(OpenKoreanTextProcessor.normalize(str))

    val tag = original.map {
      w =>
        if (w.pos != KoreanPos.Space) w.text + "/" + w.pos
        else w.text
    }.mkString(" ").replaceAll("[ ]{2,}", "##").replaceAll(" ", "+").replaceAll("##", " ")
    "" -> tag
  }

  override def tagParaByOrig(str: String): Seq[String] = {
    val sentences = OpenKoreanTextProcessor.splitSentences(str).map(_.text)
    val original = sentences.map(s => OpenKoreanTextProcessor.tokenize(OpenKoreanTextProcessor.normalize(s)))

    original.map(_.map(_.text).mkString(" ").replaceAll("[ ]{2,}", "##").replaceAll(" ", "+").replaceAll("##", " "))
  }

  override def getTagger: CanTag =
    new Tagger()

  override def isSentenceSplitterImplemented: Boolean = false
}
