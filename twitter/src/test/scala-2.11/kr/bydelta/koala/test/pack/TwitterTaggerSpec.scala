package kr.bydelta.koala.test.pack

import com.twitter.penguin.korean.TwitterKoreanProcessor
import com.twitter.penguin.korean.util.KoreanPos
import kr.bydelta.koala.test.core.TaggerSpec
import kr.bydelta.koala.traits.CanTag
import kr.bydelta.koala.twt.Tagger

/**
  * Created by bydelta on 16. 7. 26.
  */
class TwitterTaggerSpec extends TaggerSpec {

  override def tagSentByOrig(str: String): (String, String) = {
    val original = TwitterKoreanProcessor.tokenize(TwitterKoreanProcessor.normalize(str))

    val tag = original.map {
      w =>
        if (w.pos != KoreanPos.Space) w.text + "/" + w.pos
        else w.text
    }.mkString(" ").replaceAll("[ ]{2,}", "##").replaceAll(" ", "+").replaceAll("##", " ")
    "" -> tag
  }

  override def tagParaByOrig(str: String): Seq[String] = {
    val sentences = TwitterKoreanProcessor.splitSentences(str).map(_.text)
    val original = sentences.map(s => TwitterKoreanProcessor.tokenize(TwitterKoreanProcessor.normalize(s)))

    original.map(_.map(_.text).mkString(" ").replaceAll("[ ]{2,}", "##").replaceAll(" ", "+").replaceAll("##", " "))
  }

  override def getTagger: CanTag =
    new Tagger()

  override def isSentenceSplitterImplemented: Boolean = false
}
