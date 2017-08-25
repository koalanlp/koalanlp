package kr.bydelta.koala.pack

import kr.bydelta.koala.helper.{DictionaryReader, RHINOTagger}
import kr.bydelta.koala.rhino.Tagger
import kr.bydelta.koala.test.core.TaggerSpec
import kr.bydelta.koala.traits.CanTag
import org.specs2.execute.Result
import rhino.{MainClass, PreProcess}

/**
  * Created by bydelta on 16. 7. 26.
  */
class RhinoTaggerSpec extends TaggerSpec {
  override def tagSentByOrig(str: String): (String, String) = {
    val rhino =
      new MainClass(str, DictionaryReader.combiMethods_List,
        DictionaryReader.endingMethods_List, DictionaryReader.complexStem_MethodDeleted,
        DictionaryReader.stem_MethodDeleted, DictionaryReader.afterNumber_MethodDeleted,
        DictionaryReader.ending_MethodDeleted, DictionaryReader.stem_List,
        DictionaryReader.ending_List, DictionaryReader.afterNumber_List,
        DictionaryReader.nonEndingList, DictionaryReader.aspgStem, DictionaryReader.aspgEnding)
    val tagged = rhino.GetOutput().trim

    "" -> tagged
  }

  override def tagSentByKoala(str: String, tagger: CanTag): (String, String) = {
    val tagged = tagger.tagSentence(str)
    val tag = tagged.filterNot(_.exists(_.rawTag.isEmpty)).map { word =>
      word.surface + "\t" + word.map(m => m.surface + "/" + m.rawTag).mkString(" + ")
    }.mkString("\r\n")
    val surface = tagged.surfaceString()

    surface -> tag
  }

  override def tagParaByOrig(str: String): Seq[String] = Seq.empty

  override def getTagger: CanTag =
    new Tagger()

  override def isSentenceSplitterImplemented: Boolean = true

  "RHINOTagger" should {
    "split sentence as the same way" in {
      Result.unit {
        exampleSequence().foreach {
          case (_, sent) =>
            val preproc = new PreProcess(sent).GetOutput()
            val implemented = RHINOTagger.split(sent)

            implemented.filter(_._2).map(_._1).mkString("//") must be_==(preproc.mkString("//"))
        }
      }
    }
  }
}
