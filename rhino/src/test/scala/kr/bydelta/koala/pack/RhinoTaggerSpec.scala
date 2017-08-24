package kr.bydelta.koala.pack

import kr.bydelta.koala.rhino.{Dictionary, Tagger}
import kr.bydelta.koala.test.core.TaggerSpec
import kr.bydelta.koala.traits.CanTag

/**
  * Created by bydelta on 16. 7. 26.
  */
class RhinoTaggerSpec extends TaggerSpec {
  override def tagSentByOrig(str: String): (String, String) = {
    val rhino = Dictionary.getRHINO(str)
    val tagged = rhino.GetOutput().trim
    val original = tagged.split("[\r\n]+").map(_.split("\t").head).mkString(" ")

    original -> tagged
  }

  override def tagSentByKoala(str: String, tagger: CanTag): (String, String) = {
    val tagged = tagger.tagSentence(str)
    val tag = tagged.map { word =>
      word.surface + "\t" + word.map(m => m.surface + "/" + m.rawTag).mkString(" + ")
    }.mkString("\r\n")
    val surface = tagged.surfaceString()

    surface -> tag
  }

  override def tagParaByOrig(str: String): Seq[String] = Seq.empty

  override def getTagger: CanTag =
    new Tagger()

  override def isSentenceSplitterImplemented: Boolean = true
}
