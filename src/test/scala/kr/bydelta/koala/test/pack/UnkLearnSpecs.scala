package kr.bydelta.koala.test.pack

import kr.bydelta.koala.traits.{CanCompileDict, CanLearnWord}
import org.specs2.mutable.Specification

import scala.language.reflectiveCalls

/**
  * Created by bydelta on 16. 7. 31.
  */
class UnkLearnSpecs extends Specification {
  isolated

  "CanLearnWord" should {
    "separate Josa correctly" in {
      val learner = new CanLearnWord[Null, Null] {
        override protected val converter: (Null) => Null = null
        override protected val targets: Seq[CanCompileDict] = null

        override def extractNouns(corpora: Null, minOccurrence: Int, minVariations: Int): Stream[String] =
          Stream.empty

        def splitWord(word: String) = extractJosa(word)
      }

      learner.splitWord("새정치민주연합은") must be_==(Some("새정치민주연합" -> "은"))
      learner.splitWord("특검후보추천위는") must be_==(Some("특검후보추천위" -> "는"))
      learner.splitWord("최순실을") must be_==(Some("최순실" -> "을"))
      learner.splitWord("최순실") must be_==(Some("최순실" -> ""))
      learner.splitWord("특검후보추천위를") must be_==(Some("특검후보추천위" -> "를"))
      learner.splitWord("새정치민주연합의") must be_==(Some("새정치민주연합" -> "의"))
      learner.splitWord("여야와") must be_==(Some("여야" -> "와"))
      learner.splitWord("세상과") must be_==(Some("세상" -> "과"))
      learner.splitWord("민주사회로부터") must be_==(Some("민주사회" -> "로"))
      learner.splitWord("민주시민으로써") must be_==(Some("민주시민" -> "으로써"))
      learner.splitWord("민주시민으로") must be_==(Some("민주시민" -> "으로"))
      learner.splitWord("국민난로로") must be_==(Some("국민난로" -> "로"))
      learner.splitWord("시민사회와가") must be_==(Some("시민사회와" -> "가"))
      learner.splitWord("원내대표는") must be_==(Some("원내대표" -> "는"))
      learner.splitWord("시대와의") must be_==(Some("시대" -> "와"))
      learner.splitWord("시대에서는") must be_==(Some("시대" -> "에서"))
      learner.splitWord("대표님은") must be_==(None)
      learner.splitWord("대표") must be_==(Some("대표" -> ""))
    }
  }
}
