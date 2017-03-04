package kr.bydelta.koala.kryo

import kr.bydelta.koala.POS
import kr.bydelta.koala.kkma.{Dictionary => KDict}
import kr.bydelta.koala.twt.{Dictionary => TDict}
import org.specs2.mutable.Specification

/**
  * Created by bydelta on 16. 8. 5.
  */
object DictionaryImportSpec extends Specification {
  sequential

  "Dictionaries" should {
    "import other dictionary (nouns)" in {
      TDict.items.size must_== 0
      TDict.importFrom(KDict, fastAppend = true)
      TDict.items.size must be_>(0)
    }
    "import other dictionary (verbs)" in {
      val prevSize = KDict.userdic.morphemes.size
      KDict.importFrom(TDict, POS.isPredicate)
      KDict.userdic.morphemes.size must be_>(prevSize)
    }
  }
}
