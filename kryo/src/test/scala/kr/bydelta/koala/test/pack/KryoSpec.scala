package kr.bydelta.koala.test.pack

import java.io.{File, FileInputStream, FileOutputStream}

import com.twitter.chill.{Input, Output}
import kr.bydelta.koala.POS
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.kkma.{Dictionary, JavaDictionary, Parser}
import kr.bydelta.koala.kryo.{DictionaryIO, DictionaryStream, KryoWrap}
import org.specs2.mutable.Specification

/**
  * Created by bydelta on 16. 8. 5.
  */
object KryoSpec extends Specification {
  sequential

  "SentenceSerializer" should {
    "save a sentence" in {
      val text = "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐고, 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다."
      val parser = new Parser
      val kryo = KryoWrap.kryo

      val sent = parser.parse(text).head

      val tmpFile = new File(System.getProperty("java.io.tmpdir"), "test.sent")
      val output = new Output(new FileOutputStream(tmpFile))
      kryo.writeObject(output, sent)
      output.close()

      val input = new Input(new FileInputStream(tmpFile))
      val sent2 = kryo.readObject(input, classOf[Sentence])
      input.close()

      sent.singleLineString must_== sent2.singleLineString
      sent.treeString must_== sent2.treeString

      val sorted1 = sent.root.dependents.toSeq
      val sorted2 = sent2.root.dependents.toSeq
      sent.indexOf(sorted1.map(_.toString).sorted.mkString("\n")) must_==
        sent2.indexOf(sorted2.map(_.toString).sorted.mkString("\n"))
    }
  }

  "KryoSerializer" should {
    "save and load dictionary" in {
      // Clean up
      Dictionary.userdic.morphemes.clear()
      Dictionary.userdic.reset()

      Dictionary.addUserDictionary("힐스테이트1", POS.NNP)

      val tmpFile = new File(System.getProperty("java.io.tmpdir"), "test.dic")
      Dictionary >> tmpFile

      Dictionary.userdic.morphemes.clear()
      Dictionary.userdic.reset()

      Dictionary.userdic.morphemes.size must_== 0
      Dictionary << tmpFile

      Dictionary.userdic.morphemes must contain("힐스테이트1/NNP")
    }
  }

  "DictionaryStream" should {
    "save and load dictionary" in {
      Dictionary.userdic.morphemes.clear()
      Dictionary.userdic.reset()

      Dictionary.addUserDictionary("힐스테이트2", POS.NNP)

      val tmpFile = new File(System.getProperty("java.io.tmpdir"), "test-java.dic")
      val dicStream = new DictionaryStream(JavaDictionary.get)
      dicStream saveTo tmpFile

      Dictionary.userdic.morphemes.clear()
      Dictionary.userdic.reset()

      Dictionary.userdic.morphemes.size must_== 0
      dicStream readFrom tmpFile

      Dictionary.items must contain("힐스테이트2" -> POS.NNP)
    }
  }
}
