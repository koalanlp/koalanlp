package kr.bydelta.koala.test.core

import kr.bydelta.koala.kkma.{Parser, Tagger}
import org.snu.ids.ha.sp.{Parser => KParser}
import org.specs2.mutable._

import scala.collection.JavaConverters._

/**
  * Created by bydelta on 16. 7. 26.
  */
class ImplicitSpec extends Specification {
  isolated

  "Implicit" should {
    implicit val tagger = new Tagger
    implicit val parser = new Parser
    import kr.bydelta.koala.Implicit._

    "handle empty sentence" in {
      "".toTagged must_== tagger.tag("")
    }

    "tag a sentence" in {
      "고급진 오므라이스를 원한다면, 데미글라스를 올려보아요.".toTagged must_== tagger.tag("고급진 오므라이스를 원한다면, 데미글라스를 올려보아요.")
    }

    "parse a sentence" in {
      val parsed1 = "고급진 오므라이스를 원한다면, 데미글라스를 올려보아요.".toParsed
      val parsed2 = parser.parse("고급진 오므라이스를 원한다면, 데미글라스를 올려보아요.")

      parsed1 must_== parsed2
      parsed1.head.root.dependents must containTheSameElementsAs(parsed2.head.root.dependents.toSeq)
    }

    "parse after tagging" in {
      val tagged = "고급진 오므라이스를 원한다면, 데미글라스를 올려보아요.".toTagged

      // Sequence-level
      tagged.toParsed.head.root.dependents must containTheSameElementsAs(parser.jParse(tagged.asJava).get(0).root.dependents.toSeq)

      // Sentence-level
      tagged.head.toParsed.root.dependents must containTheSameElementsAs(parser.parse(tagged.head).root.dependents.toSeq)
    }
  }
}
