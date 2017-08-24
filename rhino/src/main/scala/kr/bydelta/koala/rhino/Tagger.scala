package kr.bydelta.koala.rhino

import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.fromSejongTag
import kr.bydelta.koala.traits.CanTagOnlyASentence

/**
  * Created by bydelta on 17. 8. 23.
  */
class Tagger extends CanTagOnlyASentence[Array[(String, String)]] {
  override def tagSentenceOriginal(text: String): Array[(String, String)] = {
    val rhino = Dictionary.getRHINO(text)
    rhino.GetOutput().split("[\r\n]+").map {
      word =>
        val Array(head, next, _@_*) = word.split("\t")
        (head, next)
    }
  }

  override private[koala] def convertSentence(text: String, result: Array[(String, String)]): Sentence = {
    if (result != null) {
      Sentence(
        result.map{
          case (surface, word) =>
            val matcher = Tagger.MORPH_MATCH
            val morphs =
              matcher.findAllMatchIn(word).map {
                case matcher(surf, raw) =>
                  Morpheme(surface = surf.trim, rawTag = raw, tag = fromSejongTag(raw))
              }.toSeq
            Word(surface = surface, morphemes = morphs)
        }
      )
    } else
      Sentence(Seq.empty)
  }
}

object Tagger {
  private val MORPH_MATCH = "([^\\s]+)/([A-Z]{2,3})".r
}