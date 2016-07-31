package kr.bydelta.koala.data

import kr.bydelta.koala.{POS, Processor}
import org.specs2.mutable.Specification

/**
  * Created by bydelta on 16. 7. 31.
  */
class DataSpecs extends Specification {
  "Morpheme" should {
    "provide correct equality" in {
      val morph1 = new Morpheme(surface = "밥", rawTag = "NNP", processor = Processor.KKMA)
      val morph2 = new Morpheme(surface = "밥", rawTag = "ncn", processor = Processor.Hannanum)

      morph1 must_!= morph2
      morph1 must_== "밥"
      morph1.isModifier must beFalse
      morph1.isNoun must beTrue
      morph1.hasTag(Seq(POS.NNG, POS.NNB)) must beFalse
      morph1.toString must_== "밥/NNP(NNP)"
      morph1.equalsWithoutTag(morph2) must beTrue
    }
  }

  "Word" should {
    "provide correct equality" in {
      val word = new Word(
        surface = "밥을",
        morphemes = Seq(
          new Morpheme(surface = "밥", rawTag = "NNG", processor = Processor.KKMA),
          new Morpheme(surface = "을", rawTag = "JKO", processor = Processor.KKMA)
        )
      )

      word.iterator.next must_== word.jIterator.next()
      word.iterator.next must_== word.jIterator.next()
      word.iterator.hasNext must_== word.jIterator.hasNext

      word.getPrevOf(word.get(1)).get must_== word.head
      word.getNextOf(word.get(0)).get must_== word.last
      word(3).isDefined must beFalse
      word.size must_== 2

      word.existsMorpheme(Seq(POS.NNP, POS.NNG)) must beTrue
      word.matches(Array("NNG", "JX")) must beFalse
      word.matches(Seq("NNG", "JK")) must beTrue
    }
  }

  "Sentence" should {
    "provide correct equality" in {
      val sent = new Sentence(
        words = Seq(
          new Word(
            surface = "나는",
            morphemes = Seq(
              new Morpheme(surface = "나", rawTag = "NP", processor = Processor.KKMA),
              new Morpheme(surface = "는", rawTag = "JX", processor = Processor.KKMA)
            )
          ),
          new Word(
            surface = "밥을",
            morphemes = Seq(
              new Morpheme(surface = "밥", rawTag = "NNG", processor = Processor.KKMA),
              new Morpheme(surface = "을", rawTag = "JKO", processor = Processor.KKMA)
            )
          )
        )
      )

      sent.head.surface must_== "나는"
      sent.last.surface must_== "밥을"
      sent.nouns.map(_.surface) must containAllOf(Seq("나는", "밥을"))
      sent.jVerbs.size() must_== 0
      sent.matches(Seq(Seq("NN", "J"), Seq("N", "J"))) must beFalse
      sent.matches(Array(Array("NP", "J"), Array("N", "JKO"))) must beTrue
    }
  }
}
