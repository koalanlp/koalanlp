package kr.bydelta.koala.kkma

import org.snu.ids.ha.sp.{Parser => KParser}
import org.specs2.mutable._

/**
  * Created by bydelta on 16. 7. 26.
  */
class ImplicitSpec extends Specification {
  sequential

  "Implicit" should {
    implicit val tagger = new Tagger
    implicit val parser = new Parser
    import kr.bydelta.koala.Implicit._

    "handle empty sentence" in {
      "".toTagged must_== tagger.tagSentence("")
    }

    "tag a sentence" in {
      "고급진 오므라이스를 원한다면, 데미글라스를 올려보아요.".toTagged must_== tagger.tagSentence("")
    }

    "parse a sentence" in {
      val parsed1 = "고급진 오므라이스를 원한다면, 데미글라스를 올려보아요.".toParsed
      val parsed2 = parser.parse("고급진 오므라이스를 원한다면, 데미글라스를 올려보아요.")

      parsed1 must_== parsed2
      parsed1.root.dependents must containTheSameElementsAs(parsed2.root.dependents.toSeq)
    }

    "parse after tagging" in {
      val tagged = "고급진 오므라이스를 원한다면, 데미글라스를 올려보아요.".toTagged
      tagged.toParsed.root.dependents must containTheSameElementsAs(parser.parse(tagged).root.dependents.toSeq)
    }
  }
}
