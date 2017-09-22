package kr.bydelta.koala.test.pack

import kr.bydelta.koala.kmr.Tagger
import kr.bydelta.koala.test.core.TaggerSpec
import kr.bydelta.koala.traits.CanTag
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran

import scala.collection.JavaConverters._

/**
  * Created by bydelta on 16. 7. 26.
  */
class KomoranTaggerSpec extends TaggerSpec {
  override def tagSentByOrig(str: String): (String, String) = {
    val komoran = new Komoran(DEFAULT_MODEL.FULL)
    val original = komoran.analyze(str)

    val (tag, _) = original.getTokenList.asScala.foldLeft((new StringBuilder, 0)) {
      case ((builder, prev), token) if token.getBeginIndex > prev =>
        builder.append(" ")
        builder.append(token.getMorph + "/" + token.getPos)
        (builder, token.getEndIndex)
      case ((builder, prev), token) =>
        builder.append(token.getMorph + "/" + token.getPos)
        (builder, token.getEndIndex)
    }

    "" -> tag.toString()
  }

  override def tagSentByKoala(str: String, tagger: CanTag): (String, String) = {
    val tagged = tagger.tagSentence(str)
    val tag = tagged.map(_.map(m => m.surface + "/" + m.rawTag).mkString).mkString(" ")

    str -> tag
  }

  override def tagParaByOrig(str: String): Seq[String] = Seq.empty

  override def getTagger: CanTag =
    new Tagger()

  override def isSentenceSplitterImplemented: Boolean = true
}
