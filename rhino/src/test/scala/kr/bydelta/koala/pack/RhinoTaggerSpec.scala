package kr.bydelta.koala.pack

import kr.bydelta.koala.rhino.{Dictionary, Tagger}
import kr.bydelta.koala.test.core.TaggerSpec
import kr.bydelta.koala.traits.{CanCompileDict, CanTag}
import org.specs2.execute.Result
import rhino.RHINO

/**
  * Created by bydelta on 16. 7. 26.
  */
class RhinoTaggerSpec extends TaggerSpec {
  override def tagSentByOrig(str: String): (String, String) = {
    val rhino = new RHINO()
    val original = rhino.ExternCall()

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
