package kr.bydelta.koala.rhino

import java.io.File

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.traits.{CanCompileDict, CanExtractResource}
import rhino.{FileAnalyzer, MainClass}

/**
  * Created by bydelta on 17. 8. 23.
  */
object Dictionary extends CanCompileDict with CanExtractResource{
  @volatile private var combiMethods_List: Array[String] = _
  @volatile private var endingMethods_List: Array[String] = _
  @volatile private var complexStem_MethodDeleted: Array[String] = _
  @volatile private var stem_MethodDeleted: Array[String] = _
  @volatile private var ending_MethodDeleted: Array[String] = _
  @volatile private var afterNumber_MethodDeleted: Array[String] = _
  @volatile private var stem_List: Array[Array[String]] = _
  @volatile private var ending_List: Array[Array[String]] = _
  @volatile private var afterNumber_List: Array[Array[String]] = _
  @volatile private var aspgStem: Array[Int] = _
  @volatile private var aspgEnding: Array[Int] = _
  @volatile private var nonEndingList: Array[Array[String]] = _

  override protected def modelName: String = "rhino"

  private def load() = synchronized{
    if (aspgEnding == null) {
      val fa: FileAnalyzer = new FileAnalyzer(getExtractedPath + File.pathSeparator)
      combiMethods_List = fa.MakeMethodsList("rhino.lexicon.combi.combi")
      endingMethods_List = fa.MakeMethodsList("rhino.lexicon.ending.ending")
      complexStem_MethodDeleted = fa.MakeFileArray("complexStem_MethodDeleted.txt")
      stem_MethodDeleted = fa.MakeFileArray("stem_MethodDeleted.txt")
      ending_MethodDeleted = fa.MakeFileArray("ending_MethodDeleted.txt")
      afterNumber_MethodDeleted = fa.MakeFileArray("afterNumber_MethodDeleted.txt")
      stem_List = fa.MakeFile2DArray("stem_List.txt")
      ending_List = fa.MakeFile2DArray("ending_List.txt")
      afterNumber_List = fa.MakeFile2DArray("afterNumber_List.txt")
      nonEndingList = fa.MakeFile2DArray("_auto_managed_nonEndingList.txt")
      aspgStem = fa.GetAspNum("stem_List.txt")
      aspgEnding = fa.GetAspNum("ending_List.txt")
    }
  }

  def getRHINO(input: String) = {
    load()
    new MainClass(input, combiMethods_List, endingMethods_List, complexStem_MethodDeleted,
      stem_MethodDeleted, afterNumber_MethodDeleted, ending_MethodDeleted, stem_List,
      ending_List, afterNumber_List, nonEndingList, aspgStem, aspgEnding)
  }

  override def addUserDictionary(dict: (String, POSTag)*): Unit = {
    throw new UnsupportedOperationException("RHINO does not have method for adding user dictionary")
  }

  override def items: Set[(String, POSTag)] = {
    throw new UnsupportedOperationException("RHINO does not have method for adding user dictionary")
  }

  override def baseEntriesOf(filter: (POSTag) => Boolean): Iterator[(String, POSTag)] = {
    throw new UnsupportedOperationException("RHINO does not have method for handling a dictionary")
  }

  override def getNotExists(onlySystemDic: Boolean, word: (String, POSTag)*): Seq[(String, POSTag)] = {
    throw new UnsupportedOperationException("RHINO does not have method for handling a dictionary")
  }
}
