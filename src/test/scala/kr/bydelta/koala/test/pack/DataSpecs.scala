package kr.bydelta.koala.test.pack

import kr.bydelta.koala._
import kr.bydelta.koala.data._
import org.specs2.execute.Result
import org.specs2.mutable.Specification

import scala.collection.JavaConverters._
import scala.util.Random

/**
  * Created by bydelta on 16. 7. 31.
  */
class DataSpecs extends Specification {
  sequential

  val sent = Sentence(
    Seq(
      Word(0,
        "나는",
        Seq(
          Morpheme(0, "나", "NP", POS.NP),
          Morpheme(1, "는", "JX", POS.JX)
        )
      ),
      Word(1,
        "밥을",
        Seq(
          Morpheme(0, "밥", "NNG", POS.NNG),
          Morpheme(1, "을", "JKO", POS.JKO)
        )
      )
    )
  )

  val sent2 = Sentence(
    Seq(
      Word(0,
        "흰",
        Seq(Morpheme(0, "희", "VA", POS.VA), Morpheme(1, "ㄴ", "ETM", POS.ETM))
      ),
      Word(1,
        "밥을",
        Seq(Morpheme(0, "밥", "NNG", POS.NNG), Morpheme(1, "을", "JKO", POS.JKO))
      ),
      Word(2,
        "나는",
        Seq(Morpheme(0, "나", "NP", POS.NP), Morpheme(1, "는", "JX", POS.JX))
      ),
      Word(3,
        "먹었다",
        Seq(Morpheme(0, "먹", "VV", POS.VV), Morpheme(1, "었", "EP", POS.EP), Morpheme(2, "다", "EF", POS.EF))
      )
    )
  )

  "Morpheme" should {
    val morph1 = Morpheme(-1, "밥", "NNP", POS.NNP)
    val morph2 = Morpheme(-1, "밥", "ncn", POS.NNG)
    val morph3 = sent2(1).head

    "have id number" in {
      morph1.id mustEqual -1
      morph2.id mustEqual -1

      sent.head.head.id must_== 0
      sent.head(1).id must_== 1
      morph3.id must_== 0
    }

    "check its type" in {
      morph1.isModifier must beFalse
      morph1.isNoun must beTrue

      sent2.head.head.isPredicate must beTrue
      sent2.head.last.isModifier must beFalse

      sent2.last.head.isPredicate must beTrue
      sent2(1).last.isJosa must beTrue
      sent2(2).last.isJosa must beTrue
      sent2(3).last.isJosa must beFalse
    }

    "check whether it has specified type" in {
      morph1.hasTag(Seq(POS.NNG, POS.NNB)) must beFalse
      morph1.hasTag("NN") must beTrue
      morph2.hasRawTag("nc") must beTrue
    }

    "provide correct equality" in {
      morph1 must_!== morph2
      morph2 must_== morph3
      morph2 must_!= sent

      morph1.surface mustEqual "밥"

      morph2.hashCode must_== morph3.hashCode
      morph1.equalsWithoutTag(morph2) must beTrue
    }

    "be correctly unapplied" in {
      val Morpheme(s, t) = morph3
      s must_== morph3.surface
      t must_== morph3.tag
    }

    "generate correct string" in {
      morph1.toString must_== "밥/NNP(NNP)"
    }
  }

  "Word" should {
    val word = Word(-1,
      "밥을",
      Seq(
        Morpheme(0, "밥", "NNG", POS.NNG),
        Morpheme(1, "을", "JKO", POS.JKO)
      )
    )

    "have id number" in {
      sent.head.id must_== 0
      sent2(2).id must_== 2
      word.id must_== -1
    }

    "provide correct length" in {
      word.length must_== 2
      sent2.last.length must_== 3
    }

    "find correct morpheme with its index" in {
      word(0) must_== sent.last.head
      word(1) must_== sent.last.last
      word(3) must throwAn[IndexOutOfBoundsException]
    }

    "match morpheme by tags" in {
      word.matches(Array("NNG", "JX")) must beFalse
      word.matches(Seq("NNG", "JK")) must beTrue
    }

    "provide correct equality" in {
      word must_== Word(-1,
        "밥을",
        Seq(
          Morpheme(0, "밥", "NNG", POS.NNG),
          Morpheme(1, "을", "JKO", POS.JKO)
        )
      )
      word must not(beEqualTo(sent.last))
      word must not(beEqualTo(sent.head))
      word must not(beEqualTo(Word(-1, "밥을", Seq(Morpheme(0, "밥을", "NNP", POS.NNP)))))
      word must_!= sent

      sent.last must_== sent.last
      sent2.last must_!= sent.last

      word.equalsWithoutTag(sent.last) must beTrue
      word.equalsWithoutTag(Word(-1, "밥을", Seq(Morpheme(0, "밥", "NNP", POS.NNP),
        Morpheme(1, "을", "NNP", POS.NNP)))) must beTrue
      word.equalsWithoutTag(Word(-1, "밥을", Seq(Morpheme(0, "밥을", "NNP", POS.NNP)))) must beTrue
      word.equalsWithoutTag(Word(-1, "발을", Seq(Morpheme(0, "발을", "NNP", POS.NNP)))) must beFalse
    }

    "generate correct string" in {
      word.singleLineString mustEqual "밥/NNG+을/JKO"
      word.surface mustEqual "밥을"
      word.toString mustEqual "밥을 = 밥/NNG+을/JKO"
    }

    "provide correct iterator" in {
      val scalaIt = word.iterator
      val javaIt = word.jIterator

      scalaIt.next mustEqual word(0)
      javaIt.next() mustEqual word(0)
      scalaIt.next mustEqual word(1)
      javaIt.next() mustEqual word(1)
      scalaIt.hasNext mustEqual javaIt.hasNext
    }

    "provide other functionalities" in {
      import Implicit._
      val filtered = word.filter(POS.JKO)
      filtered must beAnInstanceOf[Word]

      (word ++ word) must beAnInstanceOf[Word]

      val Word(s, m, m2) = word
      s must_== word.surface
      m must_== word.head
      m2 must_== word(1)
    }
  }

  "Sentence" should {
    "know its length" in {
      sent must haveLength(2)
      sent2 must haveLength(4)
    }

    "find its word" in {
      sent(0).id mustEqual 0
      sent(1).id mustEqual 1
      sent2(3).id mustEqual 3

      sent(0).surface mustEqual "나는"
      sent(1).surface mustEqual "밥을"
      sent(3) must throwAn[IndexOutOfBoundsException]
    }

    "provide correct string representation" in {
      sent.surfaceString() mustEqual "나는 밥을"
      sent2.surfaceString("-") mustEqual "흰-밥을-나는-먹었다"

      sent.toString mustEqual sent.surfaceString()
      sent2.toString mustEqual sent2.surfaceString()

      sent.singleLineString mustEqual "나/NP+는/JX 밥/NNG+을/JKO"
    }

    "collect words by POS tags" in {
      sent.nouns.map(_.surface) must containAllOf(Seq("나는", "밥을"))
      sent.nouns must containTheSameElementsAs(sent.jNouns.asScala)

      sent2.verbs.map(_.surface) must containAllOf(Seq("흰", "먹었다"))
      sent2.modifiers must beEmpty

      sent2.verbs must containTheSameElementsAs(sent2.jVerbs.toArray())
      sent2.nouns must containTheSameElementsAs(sent2.jNouns.toArray())
      sent2.jModifiers.size() mustEqual 0


    }

    "match word by POS tags" in {
      sent.matches(Seq(Seq("NN", "J"), Seq("N", "J"))) must beFalse
      sent.matches(Array(Array("NP", "J"), Array("N", "JKO"))) must beTrue
    }

    "provide correct equality" in {
      (sent canEqual sent.head) must beFalse
      (sent canEqual Vector[Word]()) must beFalse

      sent must not(beEqualTo(sent2))
    }

    "provide correct iterator" in {
      val scalaIt = sent2.iterator
      val javaIt = sent2.jIterator

      scalaIt.next mustEqual sent2(0)
      javaIt.next() mustEqual sent2(0)
      scalaIt.next mustEqual sent2(1)
      javaIt.next() mustEqual sent2(1)
      scalaIt.next mustEqual sent2(2)
      javaIt.next() mustEqual sent2(2)
      scalaIt.next mustEqual sent2(3)
      javaIt.next() mustEqual sent2(3)
      scalaIt.hasNext mustEqual javaIt.hasNext
    }

    "provide handle of parse tree structure" in {
      sent withDependency Dependency(PhraseType.S, FunctionalTag.UNDEF, Word.ROOT,
        Dependency(PhraseType.NP, FunctionalTag.OBJ, sent(1),
          Dependency(PhraseType.NP, FunctionalTag.SBJ, sent(0))
        )
      )

      sent withPhraseTree Phrase(PhraseType.S,
        Phrase(PhraseType.NP, sent(0)),
        Phrase(PhraseType.NP, sent(1))
      )

      sent.dependencies must not(beEmpty)
      sent.phrases must not(beEmpty)

      sent.getDependencyOf(0) must beSome[DependencyRelationLike]
      sent.getPhraseOf(0) must beSome[SyntacticPhraseLike]

      sent.getDependencyOf(1) must_== sent.getDependencyOf(sent(1))
      sent.getPhraseOf(1) must_== sent.getPhraseOf(sent(1))

      sent.getDependencyOf(0).get.parent.surface mustEqual sent(1).surface
      sent.getPhraseOf(1).get.parent.surface mustEqual "나는 밥을"

      sent.getDependencyOf(3) must beNone
      sent.getPhraseOf(3) must beNone

      sent.getDependencyOf(Word.ROOT) must beEqualTo(sent.dependencies)
      sent.getDependencyOf(sent2(0)) must beNone

      sent.getPhraseOf(Word.ROOT) must beNone
      sent.getDependencyOf(sent2(0)) must beNone

      sent.dependencies.get.treeString() mustEqual
        """S-UNDEF( = ) @ -1
          ||  NP-OBJ(밥을 = 밥/NNG+을/JKO) @ 1
          ||  |  NP-SBJ(나는 = 나/NP+는/JX) @ 0""".stripMargin

      sent.phrases.get.treeString() mustEqual
        """S(나는 밥을)
          ||  NP(나는) @ 0
          ||  NP(밥을) @ 1""".stripMargin

      sent.dependencies.get(0) mustEqual sent.getDependencyOf(1).get
      sent.dependencies.get(0) mustNotEqual sent

      sent.phrases.get(1) mustEqual sent.getPhraseOf(1).get
      sent.phrases.get(1) mustNotEqual sent
    }

    "provide inherited/scala utilities" in {
      import Implicit._
      val filtered = sent.filter(Seq(POS.JX, POS.JC))
      filtered must beAnInstanceOf[Sentence]
      filtered.size must_== 1

      val filtered2 = sent.filter(POS.JX)
      filtered2 must beAnInstanceOf[Sentence]
      filtered2 must containTheSameElementsAs(filtered)

      val concated = sent ++ sent
      concated must beAnInstanceOf[Sentence]

      val Sentence(m, m2) = sent
      m must_== sent.head
      m2 must_== sent(1)
    }
  }

  "package" should {
    "convert alphabet" in {
      val examples = Seq(
        "A" -> "에이",
        "B" -> "비",
        "C" -> "씨",
        "D" -> "디",
        "E" -> "이",
        "F" -> "에프",
        "G" -> "지",
        "H" -> "에이치",
        "I" -> "아이",
        "J" -> "제이",
        "K" -> "케이",
        "L" -> "엘",
        "M" -> "엠",
        "N" -> "엔",
        "O" -> "오",
        "P" -> "피",
        "Q" -> "큐",
        "R" -> "알",
        "S" -> "에스",
        "T" -> "티",
        "U" -> "유",
        "V" -> "브이",
        "W" -> "더블유",
        "X" -> "엑스",
        "Y" -> "와이",
        "Z" -> "제트"
      )

      Result.unit {
        (1 to 50).foreach {
          _ =>
            val (original, korean) = (1 to 6).map(_ => examples(Random.nextInt(examples.length))).unzip

            pronounceAlphabet(original.mkString) must_== korean.mkString
            writeAlphabet(korean.mkString) must_== original.mkString
            isAlphabetPronounced(korean.mkString) must beTrue
        }
      }

      pronounceAlphabet("삼성 갤럭시 S9") mustEqual "삼성 갤럭시 에스9"
      writeAlphabet("이마트") mustEqual "E마트"

      isAlphabetPronounced("이마트") must beFalse
    }

    "give the same function in Extensions" in {
      reconstructKorean('한'.getChosungCode, '걱'.getJungsungCode, '물'.getJongsungCode) mustEqual '헐'
    }

    "construct or dissamble Korean jamo" in {
      Result.unit {
        (1 to 50).foreach {
          _ =>
            val code = (1 to 4).map(_ => (Random.nextInt(18),
              Random.nextInt(20), Random.nextInt(28)))
            val str = code.map(x => reconstructKorean(x._1, x._2, x._3)).mkString

            str.endsWithJongsung mustEqual str.last.endsWithJongsung
            str.last.endsWithJongsung mustEqual (code.last._3 > 0)

            str.head.getChosungCode mustEqual code.head._1
            str.head.getJungsungCode mustEqual code.head._2
            str.head.getJongsungCode mustEqual code.head._3

            str.dissembleHangul.toSeq.map(_.toInt) mustEqual code.flatMap(x => if (x._3 != 0) Seq(x._1 + 0x1100, x._2 + 0x1161, x._3 + 0x11A7) else Seq(x._1 + 0x1100, x._2 + 0x1161))
            str.head.toDissembledSeq.map(_.toInt) mustEqual (if (code.head._3 != 0) Seq(code.head._1 + 0x1100, code.head._2 + 0x1161, code.head._3 + 0x11A7) else Seq(code.head._1 + 0x1100, code.head._2 + 0x1161))
        }
      }
    }

    "know which is jamo" in {
      'k'.isCompleteHangul must beFalse
      0x1161.toChar.isCompleteHangul must beFalse
      0x1161.toChar.isJungsungJamo must beTrue

      val ch = '겁'
      val cho = (ch.getChosungCode + 0x1100).toChar
      cho.isChosungJamo must beTrue
      cho.getChosungCode must_== ch.getChosungCode
      cho.getJungsungCode must_== -1
      cho.getJongsungCode must_== 0

      val jung = (ch.getJungsungCode + 0x1161).toChar
      jung.isJungsungJamo must beTrue
      jung.getChosungCode must_== -1
      jung.getJungsungCode must_== ch.getJungsungCode
      jung.getJongsungCode must_== 0

      val jong = (ch.getJongsungCode + 0x11A7).toChar
      jong.isJongsungJamo must beTrue
      jong.getChosungCode must_== -1
      jong.getJungsungCode must_== -1
      jong.getJongsungCode must_== ch.getJongsungCode
    }

    "know hanja character" in {
      '金'.isHanja must beTrue
      '㹤'.isHanja must beTrue

      '김'.isHanja must beFalse
    }
  }

  "util package" should {
    "reduce verb application correctly" in {
      val map =
        Seq(("깨닫", "아", true, "깨달아"),
          ("붇", "어나다", true, "불어나다"),
          ("눋", "어", true, "눌어"),
          ("믿", "어", true, "믿어"),
          ("묻", "어", true, "물어"),
          ("구르", "어", true, "굴러"),
          ("모르", "아", true, "몰라"),
          ("벼르", "어", true, "별러"),
          ("마르", "아", true, "말라"),
          ("무르", "어", true, "물러"),
          ("누르", "어", true, "눌러"),
          ("다르", "아", true, "달라"),
          ("사르", "아", true, "살라"),
          ("바르", "아", true, "발라"),
          ("가르", "아", true, "갈라"),
          ("나르", "아", true, "날라"),
          ("자르", "아", true, "잘라"),
          ("치르", "어", true, "치러"),
          ("따르", "아", true, "따라"),
          ("다다르", "아", true, "다다라"),
          ("우러르", "어", true, "우러러"),
          ("들르", "어", true, "들러"),
          ("아니꼽", "어", false, "아니꼬워"),
          ("무덥", "어", false, "무더워"),
          ("우습", "어", false, "우스워"),
          ("줍", "어", true, "주워"),
          ("더럽", "어", false, "더러워"),
          ("무섭", "어", false, "무서워"),
          ("귀엽", "어", false, "귀여워"),
          ("안쓰럽", "ㄴ", false, "안쓰러운"),
          ("아름답", "어", false, "아름다워"),
          ("잡", "아", true, "잡아"),
          ("뽑", "아", true, "뽑아"),
          ("곱", "아", false, "고와"),
          ("돕", "아", true, "도와"),
          ("뽑", "아", true, "뽑아"),
          ("씹", "어", true, "씹어"),
          ("업", "어", true, "업어"),
          ("입", "어", true, "입어"),
          ("잡", "아", true, "잡아"),
          ("접", "아", true, "접어"),
          ("좁", "아", false, "좁아"),
          ("낫", "아", true, "나아"),
          ("긋", "아", true, "그어"),
          ("벗", "아", true, "벗어"),
          ("솟", "아", true, "솟아"),
          ("씻", "아", true, "씻어"),
          ("뺏", "어", true, "뺏어"),
          ("푸", "어", true, "퍼"),
          ("끄", "아", true, "꺼"),
          ("끄", "어", true, "꺼"),
          ("들", "아", true, "들어"),
          ("가", "아라", true, "가거라"),
          ("삼가", "아라", true, "삼가거라"),
          ("들어가", "아라", true, "들어가거라"),
          ("오", "아라", true, "오너라"),
          ("돌아오", "아라", true, "돌아오너라"),
          ("푸르", "어", false, "푸르러"),
          ("하", "았다", true, "하였다"),
          ("하", "었다", true, "하였다"),
          ("영원하", "아", true, "영원하여"),
          ("파랗", "으면", false, "파라면"),
          ("파랗", "ㄴ", false, "파란"),
          ("동그랗", "은", false, "동그란"),
          ("파랗", "았다", false, "파랬다"),
          ("파랗", "을", false, "파랄"),
          ("그렇", "아", false, "그래"),
          ("시퍼렇", "었다", false, "시퍼렜다"),
          ("그렇", "네", false, "그렇네"),
          ("파랗", "네", false, "파랗네"),
          ("노랗", "네", false, "노랗네"),
          ("좋", "아", false, "좋아"),
          ("낳", "아", true, "낳아"),
          ("이", "라면서", true, "이라면서"),
          ("보", "면", true, "보면"),
          ("주장하", "았다", true, "주장하였다"),
          ("너그럽", "게", false, "너그럽게"),
          ("연결지", "었", true, "연결졌"),
          ("다", "아", true, "다오"),
          ("눕", "으니", true, "누우니"),
          ("눕", "자", true, "눕자"),
          ("돕", "으면", true, "도우면"),
          ("곱", "ㄴ", false, "고운"),
          ("곱", "어", false, "고와"),
          ("갑", "-", true, "갑-"),
          ("쌓", "자고", true, "쌓자고"),
          ("좇", "ㄴ", true, "좇은"),
          ("좇", "며", true, "좇으며"),
          ("갖", "ㄹ", true, "갖을"),
          ("붙", "며", true, "붙으며"),
          ("붙", "니", true, "붙니"),
          ("불", "나", true, "부나"),
          ("불", "오", true, "부오"),
          ("사가", "안", true, "사간"),
          ("끌", "오", true, "끄오")
        )

      Result.unit {
        map.foreach {
          case (verb, rest, isVerb, result) =>
            util.reduceVerbApply(verb.toSeq, isVerb, rest.toSeq).mkString mustEqual result
        }
      }
    }

    "reunion korean" in {

      val map =
        Seq(
          Seq('ㄱ', 'ㅏ', 'ㄴ', 'ㄷ', 'ㅣ') -> "간디",
          Seq('ㄱ', 'ㅏ', 'ㄱ', 'ㅓ', 'ㄷ', 'ㅡ', 'ㄴ') -> "가거든",
          Seq('갑', 'ㅘ', 'ㅆ', 'ㅇ', 'ㅓ') -> "가봤어",
          Seq('가', 'ㅗ', 'ㄴ', 'ㅜ', 'ㄹ', 'ㅣ') -> "가ㅗ누리"
        )

      Result.unit {
        map.foreach {
          case (seq, result) =>
            util.reunionKorean(seq).mkString mustEqual result
        }
      }
    }
  }

  "POS tag" should {
    "identify POS tags" in {
      import POS._

      val map = Seq(
        NNG -> Set(isNoun),
        NNP -> Set(isNoun),

        NNB -> Set(isNoun),
        NNM -> Set(isNoun),
        NR -> Set(isNoun),
        NP -> Set(isNoun),

        VV -> Set(isPredicate),
        VA -> Set(isPredicate),
        VX -> Set(isPredicate),
        VCP -> Set(isPredicate),
        VCN -> Set(isPredicate),

        MM -> Set(isModifier),
        MAG -> Set(isModifier),
        MAJ -> Set(isModifier),

        IC -> Set.empty,

        JKS -> Set(isPostPosition),
        JKC -> Set(isPostPosition),
        JKG -> Set(isPostPosition),
        JKO -> Set(isPostPosition),
        JKB -> Set(isPostPosition),
        JKV -> Set(isPostPosition),
        JKQ -> Set(isPostPosition),
        JC -> Set(isPostPosition),
        JX -> Set(isPostPosition),

        EP -> Set(isEnding),
        EF -> Set(isEnding),
        EC -> Set(isEnding),
        ETN -> Set(isEnding),
        ETM -> Set(isEnding),

        XPN -> Set(isAffix),
        XPV -> Set(isAffix),
        XSN -> Set(isAffix, isSuffix),
        XSV -> Set(isAffix, isSuffix),
        XSA -> Set(isAffix, isSuffix),
        XSM -> Set(isAffix, isSuffix),
        XSO -> Set(isAffix, isSuffix),
        XR -> Set(isAffix),

        SF -> Set(isSymbol),
        SP -> Set(isSymbol),
        SS -> Set(isSymbol),
        SE -> Set(isSymbol),
        SO -> Set(isSymbol),
        SW -> Set(isSymbol),

        NF -> Set(isUnknown),
        NV -> Set(isUnknown),
        NA -> Set(isUnknown),

        SL -> Set.empty,
        SH -> Set.empty,
        SN -> Set.empty
      )

      val tagset = Seq(
        isUnknown,
        isSymbol,
        isSuffix,
        isAffix,
        isEnding,
        isPostPosition,
        isModifier,
        isPredicate,
        isNoun
      )

      Result.unit {
        map.foreach {
          case (tag, set) =>
            tagset.filter(f => f(tag)) must containTheSameElementsAs(set.toSeq)
        }
      }
    }
  }
}
