package kr.bydelta.koala.helper

import java.io.InputStream

import scala.io.Source

/**
  * A static dictionary reader of RHINO. (To exclude "JFrame" from the source)
  * The code is a scala-version modification of rhino.FileAnalyzer.
  * The author of RHINO has the original copyright.
  */
private[koala] object DictionaryReader {
  val combiMethods_List: Array[String] = readMethodsList("rhino.lexicon.combi.combi")
  val endingMethods_List: Array[String] = readMethodsList("rhino.lexicon.ending.ending")
  val complexStem_MethodDeleted: Array[String] = readArray(getClass.getResourceAsStream("/rhino/complexStem_MethodDeleted.txt"))
  val stem_MethodDeleted: Array[String] = readArray(getClass.getResourceAsStream("/rhino/stem_MethodDeleted.txt"))
  val ending_MethodDeleted: Array[String] = readArray(getClass.getResourceAsStream("/rhino/ending_MethodDeleted.txt"))
  val afterNumber_MethodDeleted: Array[String] = readArray(getClass.getResourceAsStream("/rhino/afterNumber_MethodDeleted.txt"))
  val stem_List: Array[Array[String]] =
    read2DArray(getClass.getResourceAsStream("/rhino/stem_List.txt"), splitByTwo = false)
  val ending_List: Array[Array[String]] =
    read2DArray(getClass.getResourceAsStream("/rhino/ending_List.txt"), splitByTwo = false)
  val afterNumber_List: Array[Array[String]] =
    read2DArray(getClass.getResourceAsStream("/rhino/afterNumber_List.txt"), splitByTwo = false)
  val nonEndingList: Array[Array[String]] =
    read2DArray(getClass.getResourceAsStream("/rhino/_auto_managed_nonEndingList.txt"), splitByTwo = true)
  val aspgStem: Array[Int] = getMapOfLengths(stem_List)
  val aspgEnding: Array[Int] = getMapOfLengths(ending_List)

  private def readMethodsList(lexicon: String) =
    try {
      Class.forName(lexicon).getDeclaredMethods.map(_.getName)
    } catch {
      case _: Throwable => Array.empty[String]
    }

  private def read2DArray(stream: InputStream, splitByTwo: Boolean) =
    try {
      readArray(stream).map {
        line =>
          if (splitByTwo) {
            val splits = line.trim.split("\t", 2)
            if (splits.length > 1) Array(splits.head, splits(1), null)
            else Array(splits.head, null, "-1")
          } else {
            //stem_List.txt; ending_List.txt; afterNumber_List.txt; complexStem_List.txt; stem_short_List.txt
            val splits = line.trim.split("\t", 3)
            if (splits.length > 2) splits
            else Array(splits.head, splits(1), "-1")
          }
      }
    } catch {
      case _: Throwable => Array.empty[Array[String]]
    }

  private def readArray(stream: InputStream) =
    try {
      Source.fromInputStream(stream).getLines().map(_.trim).toArray
    } catch {
      case _: Throwable =>
        Array.empty[String]
    }

  private def getMapOfLengths(array: Array[Array[String]]) =
    try {
      val sizemap =
        array.map(_.head).zipWithIndex.map {
          case (word, lineNo) => (word.length, lineNo + 1)
        }.toStream.groupBy(_._1).map {
          case (wordlen, lines) if wordlen <= 7 => (wordlen, lines.minBy(_._2)._2)
          case (wordlen, _) => (wordlen, 0)
        }

      (0 until 10).map(k => sizemap.getOrElse(k, 0)).toArray
    } catch {
      case _: Throwable => Array.empty[Int]
    }
}