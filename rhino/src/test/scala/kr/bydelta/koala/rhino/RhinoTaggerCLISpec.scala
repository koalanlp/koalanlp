package kr.bydelta.koala.rhino

import kr.bydelta.koala.test.core.Examples
import org.specs2.execute.Result
import org.specs2.mutable.Specification

import scala.util.Try

/**
  * Created by bydelta on 17. 8. 25.
  */
class RhinoTaggerCLISpec extends Specification with Examples {
  "RhinoTagger" should {
    "tag a sentence" in {
      Try(new Tagger) must beSuccessfulTry
      val tagger = new Tagger

      Result.unit {
        exampleSequence().foreach {
          case (_, line) =>
            Try(tagger.tag(line)) must beSuccessfulTry
        }
      }
    }
  }
}
