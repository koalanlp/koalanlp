package kr.bydelta.koala.test.core

import kr.bydelta.koala.POS
import kr.bydelta.koala.arirang.{Dictionary => ADict}
import kr.bydelta.koala.eunjeon.{Dictionary => EDict}
import kr.bydelta.koala.hnn.{Dictionary => HDict}
import kr.bydelta.koala.kkma.{Dictionary => KDict}
import kr.bydelta.koala.kmr.{Dictionary => RDict}
import kr.bydelta.koala.twt.{Dictionary => TDict}
import org.specs2.execute.Result
import org.specs2.mutable.Specification

import scala.util.Try

/**
  * Created by bydelta on 16. 8. 5.
  */
object DictionaryImportSpec extends Specification {
  sequential

  "Dictionaries" should {
    "import other dictionary (nouns)" in {
      TDict.items.size must_== 0
      TDict.importFrom(KDict)
      TDict.items.size must be_>(0)
    }

    "import other dictionary (noun & verb)" in {
      val prevSize = KDict.userdic.morphemes.size
      KDict.importFrom(TDict, p => POS.isPredicate(p) || POS.isNoun(p), fastAppend = false)
      KDict.userdic.morphemes.size must be_>(prevSize)
    }

    "not throw exception during import" in {
      val dictionaries = Seq(EDict, HDict, RDict, KDict, TDict, ADict)
      Result.foreach(dictionaries.combinations(2).toSeq) {
        case Seq(head, last) =>
          head must not(beNull)
          last must not(beNull)
          Try(head.importFrom(last, _ == POS.NNB, fastAppend = true)) must beSuccessfulTry
          Try(last.importFrom(head, _ == POS.NNB, fastAppend = true)) must beSuccessfulTry
      }
    }
  }
}
