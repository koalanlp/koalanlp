package kr.bydelta.koala.kryo

import java.io.{File, FileInputStream, FileOutputStream}

import com.twitter.chill.{Input, Output}
import kr.bydelta.koala.POS
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.kkma.{Dictionary, Parser}
import org.specs2.execute.Result
import org.specs2.mutable.Specification

/**
  * Created by bydelta on 16. 8. 5.
  */
object KryoSpec extends Specification {
  "SentenceSerializer" should {
    "save a sentence" in {
      val text = "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐고, 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다."
      val parser = new Parser
      val kryo = KryoWrap.kryo

      val sent = parser.parse(text)

      val tmpFile = new File(System.getProperty("java.io.tmpdir"), "test.dic")
      val output = new Output(new FileOutputStream(tmpFile))
      kryo.writeObject(output, sent)
      output.close()

      val input = new Input(new FileInputStream(tmpFile))
      val sent2 = kryo.readObject(input, classOf[Sentence])
      input.close()

      sent.singleLineString must_== sent2.singleLineString
      sent.treeString must_== sent2.treeString

      sent.indexOf(sent.topLevels.head) must_== sent2.indexOf(sent2.topLevels.head)
      Result.unit {
        sent.topLevels.zip(sent2.topLevels).foreach {
          case (left, right) =>
            left must_== right
            if (left.dependents.isEmpty)
              right.dependents must beEmpty
            else
              left.dependents.zip(right.dependents).foreach {
                case (l, r) =>
                  l must_== r
                  sent.indexOf(l) must_== sent2.indexOf(r)
              }
        }
      }
    }
  }

  "KryoSerializer" should {
    "save and load dictionary" in {
      Dictionary.addUserDictionary("힐스테이트", POS.NNP)

      val tmpFile = new File(System.getProperty("java.io.tmpdir"), "test.dic")
      Dictionary >> tmpFile

      Dictionary.userdic.morphemes.clear()
      Dictionary.userdic.reset()

      Dictionary.userdic.morphemes.size must_== 0
      Dictionary << tmpFile

      Dictionary.userdic.morphemes must contain("힐스테이트/NNP")
    }
  }
}
