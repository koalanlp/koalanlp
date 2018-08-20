package kr.bydelta.koala.test.pack

import java.io.File

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala._
import kr.bydelta.koala.data._
import kr.bydelta.koala.traits._
import kr.bydelta.koala.Implicit._
import org.specs2.mutable.Specification

import scala.collection.mutable
import scala.collection.JavaConverters._
import scala.io.Source
import scala.language.reflectiveCalls

/**
  * Created by bydelta on 16. 7. 31.
  */
class TraitSpecs extends Specification {
  reflectiveCalls
  sequential

  "CanExtractResource" should {
    "extract resource" in {
      val sampleCER = new CanExtractResource {
        override protected def modelName: String = "test"
      }

      sampleCER.extractResource() must not(throwAn[Exception])
      val path = sampleCER.extractResource()
      val path2 = sampleCER.extractResource()

      path mustEqual path2

      val lines = Source.fromFile(new File(path, "DataSpecs.scala")).getLines()
      lines.hasNext must beTrue
    }

    "must be threadSafe" in {
      val sampleCER2 = new CanExtractResource {
        override protected def modelName: String = "test-thread"
      }

      val paths = (0 to 5).par.map(_ => sampleCER2.extractResource())
      paths.toSet must haveSize(1)
    }
  }

  "CanCompileDict" should {
    val dict1 = new CanCompileDict {
      val words = mutable.Set.empty[(String, POSTag)]
      override def addUserDictionary(dict: (String, POSTag)*): Unit =
        dict.foreach(words.add)

      override def items: Set[(String, POSTag)] = words.toSet

      override def baseEntriesOf(filter: POSTag => Boolean): Iterator[(String, POSTag)] =
        words.filter(x => filter(x._2)).iterator

      override def getNotExists(onlySystemDic: Boolean, word: (String, POSTag)*): Seq[(String, POSTag)] =
        word.diff(words.toSeq)
    }

    val dict2 = new CanCompileDict {
      val words = mutable.Set.empty[(String, POSTag)]
      override def addUserDictionary(dict: (String, POSTag)*): Unit =
        dict.foreach(words.add)

      override def items: Set[(String, POSTag)] = words.toSet

      override def baseEntriesOf(filter: POSTag => Boolean): Iterator[(String, POSTag)] =
        words.filter(x => filter(x._2)).iterator

      override def getNotExists(onlySystemDic: Boolean, word: (String, POSTag)*): Seq[(String, POSTag)] =
        word.diff(words.toSeq)
    }

    "add user-defined words" in {
      dict1.addUserDictionary("테팔", POS.NNP) must not(throwAn[Exception])
      dict1.words must haveLength(1)
      dict1.words must contain("테팔" -> POS.NNP)

      dict1.addUserDictionary("김종국" -> POS.NNP, "사랑" -> POS.NNG) must not(throwAn[Exception])
      dict1.words must haveLength(3)
      dict1.words must contain("김종국" -> POS.NNP)
      dict1.words must contain("사랑" -> POS.NNG)

      val wordList = new java.util.LinkedList[String]()
      val posList = new java.util.LinkedList[POS.Value]()
      wordList.add("동병상련")
      wordList.add("지나다")
      posList.add(POS.NNG)
      posList.add(POS.VV)

      dict1.jUserDictionary(wordList, posList)
      dict1.words must haveLength(5)
      dict1.words must contain("동병상련" -> POS.NNG)
      dict1.words must contain("지나다" -> POS.VV)
    }

    "check whether dictionary has a word" in {
      dict1.contains("지나다", Set(POS.VA)) must beFalse
      dict1.contains("지나다", Set(POS.VV)) must beTrue
      dict1.contains("김종국", Set(POS.NNB, POS.NNP, POS.NNG)) must beTrue
    }

    "import from other dictionary" in {
      dict2.importFrom(dict1, POS.isNoun)
      dict2.words must haveLength(4)
      dict2.contains("테팔", Set(POS.NNP, POS.NNG)) must beTrue
      dict2.contains("김종국", Set(POS.NNP, POS.NNG)) must beTrue
      dict2.contains("동병상련", Set(POS.NNP, POS.NNG)) must beTrue
      dict2.contains("지나다", Set(POS.VV)) must beFalse
    }
  }

  "CanTagOnlyAParagraph" should {
    val tagger = new CanTagOnlyAParagraph[Seq[String]] {
      override def tagParagraphOriginal(text: String): Seq[Seq[String]] =
        text.split("\n").map(_.split(" ").toSeq).toSeq

      override private[koala] def convertSentence(result: Seq[String]) =
        Sentence(result.zipWithIndex.map{
          case (w, wid) => Word(wid, w, Seq(Morpheme(0, w, "NNP", POS.NNP)))
        })
    }

    "tag a sentence" in {
      val sent = tagger.tagSentence("이것은 테스트 문장입니다.")
      sent.singleLineString mustEqual "이것은/NNP 테스트/NNP 문장입니다./NNP"
      sent must haveLength(3)

      tagger.tagSentence("") must beEmpty
    }

    "tag a paragraph" in {
      val sent = tagger.tag("이것은 테스트 문장입니다.\n저것은 설렘입니다.")
      sent.head.singleLineString mustEqual "이것은/NNP 테스트/NNP 문장입니다./NNP"
      sent.head must haveLength(3)
      sent(1).singleLineString mustEqual "저것은/NNP 설렘입니다./NNP"
      sent(1) must haveLength(2)

      tagger.tag("") must beEmpty
      tagger.jTag("행복하신가요?").asScala.toSeq mustEqual tagger.tag("행복하신가요?")
    }

    "support implicit tagging" in {
      implicit val tag = tagger
      "이것은 테스트 문장입니다.\n저것은 설렘입니다.".toTagged mustEqual tagger.tag("이것은 테스트 문장입니다.\n저것은 설렘입니다.")
    }
  }

  "CanTagAParagraph" should {
    val tagger = new CanTagAParagraph[Seq[String]] {
      override def tagSentenceOriginal(text: String): Seq[String] =
        text.split(" ").toSeq

      override def tagParagraphOriginal(text: String): Seq[Seq[String]] =
        text.split("\n").map(_.split(" ").toSeq).toSeq

      override private[koala] def convertSentence(result: Seq[String]) =
        Sentence(result.zipWithIndex.map{
          case (w, wid) => Word(wid, w, Seq(Morpheme(0, w, "NNP", POS.NNP)))
        })
    }

    "tag a sentence" in {
      val sent = tagger.tagSentence("이것은 테스트 문장입니다.")
      sent.singleLineString mustEqual "이것은/NNP 테스트/NNP 문장입니다./NNP"
      sent must haveLength(3)

      tagger.tagSentence("") must beEmpty
    }

    "tag a paragraph" in {
      val sent = tagger.tag("이것은 테스트 문장입니다.\n저것은 설렘입니다.")
      sent.head.singleLineString mustEqual "이것은/NNP 테스트/NNP 문장입니다./NNP"
      sent.head must haveLength(3)
      sent(1).singleLineString mustEqual "저것은/NNP 설렘입니다./NNP"
      sent(1) must haveLength(2)

      tagger.tag("") must beEmpty
      tagger.jTag("행복하신가요?").asScala.toSeq mustEqual tagger.tag("행복하신가요?")
    }

    "support implicit tagging" in {
      implicit val tag = tagger
      "이것은 테스트 문장입니다.\n저것은 설렘입니다.".toTagged mustEqual tagger.tag("이것은 테스트 문장입니다.\n저것은 설렘입니다.")
    }
  }

  "CanTagASentence" should {
    val tagger = new CanTagASentence[Seq[String]] {
      override def tagSentenceOriginal(text: String): Seq[String] =
        text.split(" ").toSeq

      override def tag(text: String): Seq[Sentence] =
        if(text.trim.isEmpty) Seq.empty
        else text.split("\n").map(x => convertSentence(tagSentenceOriginal(x)))

      override private[koala] def convertSentence(result: Seq[String]) =
        Sentence(result.zipWithIndex.map{
          case (w, wid) => Word(wid, w, Seq(Morpheme(0, w, "NNP", POS.NNP)))
        })
    }

    "tag a sentence" in {
      val sent = tagger.tagSentence("이것은 테스트 문장입니다.")
      sent.singleLineString mustEqual "이것은/NNP 테스트/NNP 문장입니다./NNP"
      sent must haveLength(3)

      tagger.tagSentence("") must beEmpty
    }

    "tag a paragraph" in {
      val sent = tagger.tag("이것은 테스트 문장입니다.\n저것은 설렘입니다.")
      sent.head.singleLineString mustEqual "이것은/NNP 테스트/NNP 문장입니다./NNP"
      sent.head must haveLength(3)
      sent(1).singleLineString mustEqual "저것은/NNP 설렘입니다./NNP"
      sent(1) must haveLength(2)

      tagger.tag("") must beEmpty
      tagger.jTag("행복하신가요?").asScala.toSeq mustEqual tagger.tag("행복하신가요?")
    }

    "support implicit tagging" in {
      implicit val tag = tagger
      "이것은 테스트 문장입니다.\n저것은 설렘입니다.".toTagged mustEqual tagger.tag("이것은 테스트 문장입니다.\n저것은 설렘입니다.")
    }
  }

  "CanTagOnlyASentence" should {
    val tagger = new CanTagOnlyASentence[Seq[String]] {
      override def tagSentenceOriginal(text: String): Seq[String] =
        text.split(" ").toSeq

      override private[koala] def convertSentence(text: String, result: Seq[String]) =
        Sentence(result.zipWithIndex.map{
          case (w, wid) => Word(wid, w, Seq(Morpheme(0, w, "NNP", POS.NNP)))
        })
    }

    "tag a sentence" in {
      val sent = tagger.tagSentence("이것은 테스트 문장입니다.")
      sent.singleLineString mustEqual "이것은/NNP 테스트/NNP 문장입니다./NNP"
      sent must haveLength(3)

      tagger.tagSentence("") must beEmpty
    }

    "tag a paragraph" in {
      val sent = tagger.tag("이것은 테스트 문장입니다.\n저것은 설렘입니다.")
      sent.head.singleLineString mustEqual "이것은/NNP 테스트/NNP 문장입니다.\n저것은/NNP 설렘입니다./NNP"
      sent.head must haveLength(4)

      tagger.tag("") must beEmpty
      tagger.jTag("행복하신가요?").asScala.toSeq mustEqual tagger.tag("행복하신가요?")
    }

    "support implicit tagging" in {
      implicit val tag = tagger
      "이것은 테스트 문장입니다.\n저것은 설렘입니다.".toTagged mustEqual tagger.tag("이것은 테스트 문장입니다.\n저것은 설렘입니다.")
    }
  }

  "CanParse" should {
    val parser = new CanParse {
      override def parse(sentence: String): Seq[Sentence] =
        sentence.split("\n").map { sent =>
          val s = Sentence(sent.split(" ").zipWithIndex.map(t => Word(t._2, t._1, Seq(Morpheme(0, t._1, "NNP", POS.NNP)))))
          s.withPhraseTree(Phrase(PhraseType.S, s.map(w => Phrase(PhraseType.NP, w)):_*))
            .withDependency(Dependency(PhraseType.NP, FunctionalTag.UNDEF, s.head, Seq.empty:_*))
        }

      override def parse(sentence: Sentence): Sentence =
        sentence.withPhraseTree(Phrase(PhraseType.S, sentence.map(w => Phrase(PhraseType.NP, w)):_*))
          .withDependency(Dependency(PhraseType.NP, FunctionalTag.UNDEF, sentence.head, Seq.empty:_*))
    }

    "parse correctly" in {
      parser.jParse("안녕하세요.\n졸린 일요일입니다.").asScala mustEqual parser.parse("안녕하세요.\n졸린 일요일입니다.")

      val sample = parser.parse("안녕하세요.\n졸린 일요일입니다.")
      sample.head.phrases must not(beEmpty)

      val singleSample = Sentence(sample.head.words)
      val parsed = parser.parse(singleSample)
      parsed.phrases mustEqual sample.head.phrases
      parsed.dependencies mustEqual sample.head.dependencies

      val javaParse = parser.jParse(sample.asJava).asScala
      javaParse mustEqual parser.parse(sample)
    }

    "support implicit parsing" in {
      implicit val par = parser
      "이것은 테스트 문장입니다.\n저것은 설렘입니다.".toParsed mustEqual parser.parse("이것은 테스트 문장입니다.\n저것은 설렘입니다.")

      val sample = parser.parse("안녕하세요.\n졸린 일요일입니다.")
      val singleSample = Sentence(sample.head.words)

      singleSample.toParsed.phrases mustEqual sample.head.phrases
      Seq(singleSample).toParsed.head.phrases mustEqual sample.head.phrases
    }
  }

  "CanSplitSentence" should {
    val splitter = new CanSplitSentence {
      override def sentences(text: String): Seq[String] =
        text.split("\n").toSeq
    }

    "split sentence" in {
      splitter.sentences("안녕하세요.\n졸린 일요일입니다.") must containTheSameElementsAs(Seq("안녕하세요.", "졸린 일요일입니다."))
      splitter.jSentences("안녕하세요.\n졸린 일요일입니다.").asScala must containTheSameElementsAs(Seq("안녕하세요.", "졸린 일요일입니다."))
    }

    "support implicit segmenting" in {
      implicit val par = splitter
      "이것은 테스트 문장입니다.\n저것은 설렘입니다.".sentences mustEqual splitter.sentences("이것은 테스트 문장입니다.\n저것은 설렘입니다.")
    }
  }
}
