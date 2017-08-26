package kr.bydelta.koala.test.pack

import kr.bydelta.koala._
import kr.bydelta.koala.data.{Morpheme, Sentence, Word}
import org.specs2.execute.Result
import org.specs2.mutable.Specification

import scala.collection.JavaConverters._
import scala.util.Random

/**
  * Created by bydelta on 16. 7. 31.
  */
class DataSpecs extends Specification {
  "Morpheme" should {
    "provide correct equality" in {
      val morph1 = Morpheme("밥", "NNP", POS.NNP)
      val morph2 = Morpheme("밥", "ncn", POS.NNG)
      val morph3 = Morpheme("밥", "NNG", POS.NNG)

      (morph1 == morph2) must beFalse
      (morph2 == morph3) must beTrue
      morph2.hashCode must_== morph3.hashCode
      morph1.isModifier must beFalse
      morph1.isNoun must beTrue

      val morph4: Morpheme = Morpheme("은", "JX", POS.JX)
      morph4.isJosa must beTrue
      morph4.isPredicate must beFalse

      morph1.hasTag(Seq(POS.NNG, POS.NNB)) must beFalse
      morph2.hasRawTag("nc") must beTrue

      morph1.toString must_== "밥/NNP(NNP)"
      morph1.equalsWithoutTag(morph2) must beTrue

      val Morpheme(s, t) = morph4
      s must_== morph4.surface
      t must_== morph4.tag
    }
  }

  "Word" should {
    "provide correct equality" in {
      val word = Word(
        "밥을",
        Seq(
          Morpheme("밥", "NNG", POS.NNG),
          Morpheme("을", "JKO", POS.JKO)
        )
      )

      (word.iterator.next == word.jIterator.next()) must beTrue
      (word.iterator.next == word.jIterator.next()) must beTrue
      word.iterator.hasNext must_== word.jIterator.hasNext

      word(0).surface must_== "밥"
      word.head.id must_== 0
      word.last.surface must_== "을"
      word.last.id must_== 1
      word(3) must throwAn[IndexOutOfBoundsException]
      word.length must_== 2

      word.matches(Array("NNG", "JX")) must beFalse
      word.matches(Seq("NNG", "JK")) must beTrue

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
    "provide correct equality" in {
      val sent = Sentence(
        Seq(
          Word(
            "나는",
            Seq(
              Morpheme("나", "NP", POS.NP),
              Morpheme("는", "JX", POS.JX)
            )
          ),
          Word(
            "밥을",
            Seq(
              Morpheme("밥", "NNG", POS.NNG),
              Morpheme("을", "JKO", POS.JKO)
            )
          )
        )
      )

      sent.head.id must_== 0
      sent.last.id must_== 1
      sent(0).surface must_== "나는"
      sent.last.surface must_== "밥을"
      sent.nouns.map(_.surface) must containAllOf(Seq("나는", "밥을"))
      sent.nouns must containTheSameElementsAs(sent.jNouns.asScala)
      sent.jVerbs.size() must_== 0
      sent.jModifiers.size() must_== 0

      (sent canEqual sent.head) must beFalse
      (sent canEqual Vector[Word]()) must beFalse

      sent.matches(Seq(Seq("NN", "J"), Seq("N", "J"))) must beFalse
      sent.matches(Array(Array("NP", "J"), Array("N", "JKO"))) must beTrue

      val sent2 = Sentence(
        Seq(
          Word(
            "밥을",
            Seq(
              Morpheme("밥", "NNG", POS.NNG),
              Morpheme("을", "JKO", POS.JKO)
            )
          ),
          Word(
            "나는",
            Seq(
              Morpheme("나", "NP", POS.NP),
              Morpheme("는", "JX", POS.JX)
            )
          )
        )
      )

      sent must not(beEqualTo(sent2))
      sent2.root.addDependant(1, FunctionalTag.Object, "O")
      sent2(1).addDependant(0, FunctionalTag.Subject, "S")
      sent2.root.dependents.zip(sent2.root.jDependents.asScala).forall(x => x._1 == x._2) must beTrue

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
        }
      }
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

            str.dissembleHangul.toSeq.map(_.toInt) mustEqual code.flatMap(x => Seq(x._1 + 0x1100, x._2 + 0x1161, x._3 + 0x11A7))
            str.head.toDissembledSeq.map(_.toInt) mustEqual Seq(code.head._1 + 0x1100, code.head._2 + 0x1161, code.head._3 + 0x11A7)
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
          ("들", "아", true, "들어"),
          ("가", "아라", true, "가거라"),
          ("삼가", "아라", true, "삼가거라"),
          ("들어가", "아라", true, "들어가거라"),
          ("오", "아라", true, "오너라"),
          ("돌아오", "아라", true, "돌아오너라"),
          ("푸르", "어", false, "푸르러"),
          ("하", "았다", true, "하였다"),
          ("영원하", "아", true, "영원하여"),
          ("파랗", "으면", false, "파라면"),
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
}
