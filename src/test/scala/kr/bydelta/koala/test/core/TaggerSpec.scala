package kr.bydelta.koala.test.core

import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.traits.{CanCompileDict, CanTag}
import org.specs2.execute.Result
import org.specs2.mutable.Specification

/**
  * Created by bydelta on 17. 4. 22.
  */
trait TaggerSpec extends Specification with Examples {
  sequential

  def tagSentByOrig(str: String): (String, String)

  def tagParaByOrig(str: String): Seq[String]

  def getTagger: CanTag

  def expectCorrectParse(str: String) = {
    print("S")
    val tagger = getTagger
    val (oSurface, oTag) = tagSentByOrig(str)
    val (tSurface, tTag) = tagSentByKoala(str, tagger)

    if (oTag.nonEmpty)
      tTag must_== oTag
    if (oSurface.nonEmpty)
      tSurface must_== oSurface

    tSurface.replaceAll("\\s+", "") must_== str.replaceAll("\\s+", "")
  }

  def tagSentByKoala(str: String, tagger: CanTag): (String, String) = {
    val tagged = Sentence(tagger.tag(str).flatten)
    val tag = tagged.map(_.map(m => m.surface + "/" + m.rawTag).mkString("+")).mkString(" ")
    val surface = tagged.surfaceString()
    surface -> tag
  }

  def expectCorrectParses(str: Seq[String]) = {
    val tagger = getTagger
    val single = str.map(s => tagSentByKoala(s, tagger))
    val multi = str.par.map {
      s =>
        print("T")
        val t = getTagger
        tagSentByKoala(s, t)
    }

    val matched = single.zip(multi)

    Result.unit {
      matched.foreach {
        case ((sS, sT), (tS, tT)) =>
          tS must_== sS
          tT must_== sT
      }
    }
  }

  def isSentenceSplitterImplemented: Boolean

  def isParagraphImplemented: Boolean = true

  "Tagger" should {
    "handle empty sentence" in {
      val sent = getTagger.tag("")
      sent must beEmpty
    }

    "tag a sentence" in {
      Result.foreach(exampleSequence().filter(_._1 == 1)) {
        case (_, sent) =>
          expectCorrectParse(sent)
      }
    }

    "be thread-safe" in {
      val sents = exampleSequence().filter(_._1 == 1).map(_._2)
      expectCorrectParses(sents)
    }

    if (isSentenceSplitterImplemented) {
      "match sentence split spec" in {
        val tagger = getTagger
        Result.foreach(exampleSequence(requireMultiLine = true)) {
          case (n, sent) =>
            print("L")
            val splits = tagger.tag(sent)
            if (splits.length != n) {
              println(" NOTMATCHED " + splits.map(_.singleLineString))
            }
            splits.length must_== n
        }
      }
    } else if (isParagraphImplemented) {
      "tag paragraph" in {
        print("P")
        val tagger = getTagger
        Result.foreach(exampleSequence()) {
          case (_, sent) =>
            val splits = tagger.tag(sent)
            val orig = tagParaByOrig(sent)

            splits.length must_== orig.length
            splits.map(_.map(_.map(_.surface).mkString("+"))
              .mkString(" ")).mkString("\n") must_== orig.mkString("\n")
        }
      }
    } else {
      "tag pargraph" in {
        Result.unit(())
      }
    }
  }
}

