package kr.bydelta.koala.hnn

import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter}

import kaist.cilab.jhannanum.common.{Code, JSONReader}
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
  private lazy val json: JSONReader = new JSONReader(extractResource() + File.separator + "conf"
    + File.separator + "ChartMorphAnalyzer.json")
  override protected val modelName: String = "hannanum"
  /**
    * 사용자사전에 등재되기 전의 리스트.
    */
  private[koala] val userDict = mutable.HashMap[String, String]()
  /** 사용자사전. **/
  private[koala] val userDic: Trie = new Trie(Trie.DEFAULT_TRIE_BUF_SIZE_USER)
  private[koala] val tagSet: TagSet = new TagSet
  private[koala] val connection: Connection = new Connection
  private[koala] val connectionNot: ConnectionNot = new ConnectionNot
  private[koala] val numAutomata: NumberAutomata = new NumberAutomata
  private[koala] var analyzedDic: AnalyzedDic = _
  private[koala] var systemDic: Trie = _
  private var usrDicPath: String = _

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
    systemDic.fetch(Code.toTripleArray(word)).info_list.exists(x => oTag.contains(x.tag)) ||
      userDic.fetch(Code.toTripleArray(word)).info_list.exists(x => oTag.contains(x.tag))
  }

  def loadDictionary() =
    this synchronized {
      val baseDir = Dictionary.extractResource()
      usrDicPath = baseDir + File.separator + json.getValue("dic_user")

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

      if (systemDic == null) {
        val fileDicSystem: String = baseDir + File.separator + json.getValue("dic_system")
        val fileConnections: String = baseDir + File.separator + json.getValue("connections")
        val fileConnectionsNot: String = baseDir + File.separator + json.getValue("connections_not")
        val fileDicAnalyzed: String = baseDir + File.separator + json.getValue("dic_analyzed")
        val fileTagSet: String = baseDir + File.separator + json.getValue("tagset")
        tagSet.init(fileTagSet, TagSet.TAG_SET_KAIST)
        connection.init(fileConnections, tagSet.getTagCount, tagSet)
        connectionNot.init(fileConnectionsNot, tagSet)
        analyzedDic = new AnalyzedDic
        analyzedDic.readDic(fileDicAnalyzed)
        systemDic = new Trie(Trie.DEFAULT_TRIE_BUF_SIZE_SYS)
        systemDic.read_dic(fileDicSystem, tagSet)
      }
    }
}
