package kr.bydelta.koala.rhino

import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import kr.bydelta.koala.fromSejongTag
import kr.bydelta.koala.traits.CanTagOnlyASentence

/**
  * Created by bydelta on 17. 8. 23.
  */
class Tagger extends CanTagOnlyASentence[Array[String]]{
  override def tagSentenceOriginal(text: String): Array[String] = {
    val rhino = Dictionary.getRHINO(text)
    rhino.GetOutput()
    rhino.GetOutputArr()
  }

  override private[koala] def convertSentence(text: String, result: Array[String]): Sentence = {
    if (result != null) {
      Sentence(
        result.map{
          word =>
            val morphs =
              word.split(" \\+ ").map{
                morph =>
                  val (surf, rawTag) = morph.splitAt(morph.indexOf("/"))
                  Morpheme(surface = surf, rawTag = rawTag, tag = fromSejongTag(rawTag.substring(1)))
              }
            Word(surface = "", morphemes = morphs)
        }
      )
    } else
      Sentence(Seq.empty)
  }
}
