package kr.bydelta.koala.hnn

import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter}

import kaist.cilab.jhannanum.common.Code
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.datastructure.{TagSet, Trie}
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.resource.{AnalyzedDic, Connection, ConnectionNot, NumberAutomata}
import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala._
import kr.bydelta.koala.traits.{CanCompileDict, CanExtractResource}

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.io.Source

/**
  * 한나눔 사용자사전
  */
object Dictionary extends CanCompileDict with CanExtractResource {
  extractResource()
  private[koala] val tagSet: TagSet = {
    val tagSet = new TagSet
    val fileTagSet: String = getExtractedPath + File.separator + "data/kE/tag_set.txt"
    tagSet.init(fileTagSet, TagSet.TAG_SET_KAIST)
    tagSet
  }
  private[koala] val connection: Connection = {
    val connection = new Connection
    val fileConnections: String = getExtractedPath + File.separator + "data/kE/connections.txt"
    connection.init(fileConnections, tagSet.getTagCount, tagSet)
    connection
  }
  private[koala] val connectionNot: ConnectionNot = {
    val connectionNot = new ConnectionNot
    val fileConnectionsNot: String = getExtractedPath + File.separator + "data/kE/connections_not.txt"
    connectionNot.init(fileConnectionsNot, tagSet)
    connectionNot
  }
  private[koala] val analyzedDic: AnalyzedDic = {
    val fileDicAnalyzed: String = getExtractedPath + File.separator + "data/kE/dic_analyzed.txt"
    val analyzedDic = new AnalyzedDic
    analyzedDic.readDic(fileDicAnalyzed)
    analyzedDic
  }
  private[koala] val systemDic: Trie = {
    val fileDicSystem: String = getExtractedPath + File.separator + "data/kE/dic_system.txt"
    val systemDic = new Trie(Trie.DEFAULT_TRIE_BUF_SIZE_SYS)
    systemDic.read_dic(fileDicSystem, tagSet)
    systemDic
  }
  private val usrDicPath: String = getExtractedPath + File.separator + "data/kE/dic_user.txt"
  /**
    * 사용자사전에 등재되기 전의 리스트.
    */
  private[koala] val userDict = mutable.HashMap[String, String]()
  /** 사용자사전. **/
  private[koala] val userDic: Trie = new Trie(Trie.DEFAULT_TRIE_BUF_SIZE_USER)
  private[koala] val numAutomata: NumberAutomata = new NumberAutomata

  override def addUserDictionary(dict: (String, POSTag)*) {
    userDict ++= dict.map {
      case (word, tag) => (word, tagToHNN(tag))
    }
  }

  override def addUserDictionary(morph: String, tag: POSTag) {
    userDict += morph -> tagToHNN(tag)
  }

  override def items: Seq[(String, POSTag)] = {
    val fileDict =
      if (usrDicPath != null)
        Source.fromFile(usrDicPath).getLines().map {
          line =>
            val segs = line.split('\t')
            segs(0) -> fromHNNTag(segs(1))
        }.toSeq
      else Seq()
    fileDict ++ userDict.map {
      case (surf, tag) => surf -> fromHNNTag(tag)
    }.toSeq
  }

  override def contains(word: String, posTag: Set[POSTag] = Set(POS.NNP, POS.NNG)): Boolean = {
    loadDictionary()

    val oTag = posTag.map(x => tagSet.getTagID(tagToHNN(x)))
    fetchFrom(word, systemDic, oTag) || fetchFrom(word, userDic, oTag)
  }

  private def fetchFrom(word: String, dict: Trie, oTag: Set[Int]) = {
    val morph = dict.fetch(Code.toTripleArray(word))
    if (morph == null) false
    else morph.info_list.exists(x => oTag.contains(x.tag))
  }

  def loadDictionary() =
    userDict synchronized {
      if (userDict.nonEmpty) {
        val file = new File(usrDicPath)
        file.deleteOnExit()

        val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)))
        userDict.foreach {
          case (morph, tag) =>
            writer.write(morph)
            writer.write('\t')
            writer.write(tag)
            writer.newLine()
        }
        writer.close()
        userDict.clear()

        userDic.search_end = 0
        userDic.read_dic(usrDicPath, tagSet)
      }
    }

  override protected def modelName: String = "hannanum"
}
