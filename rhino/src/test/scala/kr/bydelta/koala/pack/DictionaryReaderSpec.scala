package kr.bydelta.koala.pack

import kr.bydelta.koala.POS
import kr.bydelta.koala.helper.DictionaryReader
import kr.bydelta.koala.rhino.Dictionary
import org.specs2.execute.Result
import org.specs2.mutable.Specification
import rhino.FileAnalyzer

/**
  * Created by bydelta on 17. 8. 25.
  */
class DictionaryReaderSpec extends Specification {
  val fa = new FileAnalyzer("./rhino/src/main/resources/rhino/")

  "DictionaryReader" should {
    "read the same method" in {
      fa.MakeMethodsList("rhino.lexicon.combi.combi").toSeq must containTheSameElementsAs(DictionaryReader.combiMethods_List.toSeq)
      fa.MakeMethodsList("rhino.lexicon.ending.ending").toSeq must containTheSameElementsAs(DictionaryReader.endingMethods_List.toSeq)
    }

    "read complexStem_MethodDeleted" in {
      Result.unit {
        fa.MakeFileArray("complexStem_MethodDeleted.txt").zip(DictionaryReader.complexStem_MethodDeleted).foreach {
          case (a, b) =>
            a must be_==(b)
        }
      }
    }
    "read stem_MethodDeleted" in {
      Result.unit {
        fa.MakeFileArray("stem_MethodDeleted.txt").zip(DictionaryReader.stem_MethodDeleted.toSeq).foreach {
          case (a, b) =>
            a must be_==(b)
        }
      }
    }
    "read ending_MethodDeleted" in {
      Result.unit {
        fa.MakeFileArray("ending_MethodDeleted.txt").zip(DictionaryReader.ending_MethodDeleted.toSeq).foreach {
          case (a, b) =>
            a must be_==(b)
        }
      }
    }
    "read afterNumber_MethodDeleted" in {
      Result.unit {
        fa.MakeFileArray("afterNumber_MethodDeleted.txt").zip(DictionaryReader.afterNumber_MethodDeleted.toSeq).foreach {
          case (a, b) =>
            a must be_==(b)
        }
      }
    }
    "read stem_List" in {
      Result.unit {
        fa.MakeFile2DArray("stem_List.txt").zip(DictionaryReader.stem_List).foreach {
          case (a, b) =>
            a.zip(b).foreach {
              case (x, y) => x must be_==(y)
            }
        }
      }
    }
    "read endingList" in {
      Result.unit {
        fa.MakeFile2DArray("ending_List.txt").zip(DictionaryReader.ending_List).foreach {
          case (a, b) =>
            a.zip(b).foreach {
              case (x, y) => x must be_==(y)
            }
        }
      }
    }
    "read afterNumberList" in {
      Result.unit {
        fa.MakeFile2DArray("afterNumber_List.txt").zip(DictionaryReader.afterNumber_List).foreach {
          case (a, b) =>
            a.zip(b).foreach {
              case (x, y) => x must be_==(y)
            }
        }
      }
    }
    "read nonEnding" in {
      Result.unit {
        fa.MakeFile2DArray("_auto_managed_nonEndingList.txt").zip(DictionaryReader.nonEndingList).foreach {
          case (a, b) =>
            a.zip(b).foreach {
              case (x, y) => x must be_==(y)
            }
        }
      }
    }
    "read Num:stem_List" in {
      Result.unit {
        fa.GetAspNum("stem_List.txt").zip(DictionaryReader.aspgStem.toSeq).foreach {
          case (a, b) =>
            a must be_==(b)
        }
      }
    }
    "read Num:ending_List" in {
      Result.unit {
        fa.GetAspNum("ending_List.txt").zip(DictionaryReader.aspgEnding.toSeq).foreach {
          case (a, b) =>
            a must be_==(b)
        }
      }
    }
  }

  "Dictionary" should {
    "throw exceptions" in {
      Dictionary.items must throwA[UnsupportedOperationException]
      Dictionary.getNotExists(false, ("ABC", POS.VV)) must throwA[UnsupportedOperationException]
      Dictionary.addUserDictionary("ABC" -> POS.VV) must throwA[UnsupportedOperationException]
      Dictionary.addUserDictionary("ABC", POS.VV) must throwA[UnsupportedOperationException]
    }
  }
}
