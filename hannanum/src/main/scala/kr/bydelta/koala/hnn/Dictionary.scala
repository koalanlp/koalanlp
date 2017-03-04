package kr.bydelta.koala.hnn

import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter}

import kaist.cilab.jhannanum.common.Code
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.datastructure.{TagSet, Trie}
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.resource.{AnalyzedDic, Connection, ConnectionNot, NumberAutomata}
import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala._
import kr.bydelta.koala.traits.{CanCompileDict, CanExtractResource}

import scala.annotation.tailrec
import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.io.Source

/**
  * 한나눔 사용자사전
  */
object Dictionary extends CanCompileDict with CanExtractResource {
  extractResource()
  private[koala] lazy val tagSet: TagSet = {
    val tagSet = new TagSet
    val fileTagSet: String = getExtractedPath + File.separator + "data/kE/tag_set.txt"
    tagSet.init(fileTagSet, TagSet.TAG_SET_KAIST)
    tagSet
  }
  private[koala] lazy val connection: Connection = {
    val connection = new Connection
    val fileConnections: String = getExtractedPath + File.separator + "data/kE/connections.txt"
    connection.init(fileConnections, tagSet.getTagCount, tagSet)
    connection
  }
  private[koala] lazy val connectionNot: ConnectionNot = {
    val connectionNot = new ConnectionNot
    val fileConnectionsNot: String = getExtractedPath + File.separator + "data/kE/connections_not.txt"
    connectionNot.init(fileConnectionsNot, tagSet)
    connectionNot
  }
  private[koala] lazy val analyzedDic: AnalyzedDic = {
    val fileDicAnalyzed: String = getExtractedPath + File.separator + "data/kE/dic_analyzed.txt"
    val analyzedDic = new AnalyzedDic
    analyzedDic.readDic(fileDicAnalyzed)
    analyzedDic
  }
  private[koala] lazy val systemDic: Trie = {
    val fileDicSystem: String = getExtractedPath + File.separator + "data/kE/dic_system.txt"
    val systemDic = new Trie(Trie.DEFAULT_TRIE_BUF_SIZE_SYS)
    systemDic.read_dic(fileDicSystem, tagSet)
    systemDic
  }
  private lazy val usrDicPath: File = {
    val f = new File(getExtractedPath + File.separator + "data/kE/dic_user.txt")
    f.createNewFile()
    f.deleteOnExit()
    f
  }
  /** 사용자사전. **/
  private[koala] lazy val userDic: Trie = new Trie(Trie.DEFAULT_TRIE_BUF_SIZE_USER)
  private[koala] lazy val numAutomata: NumberAutomata = new NumberAutomata
  private[koala] var usrBuffer = Set[(String, POSTag)]()
  private var dicLastUpdate, mapLastUpdate = 0l

  override def addUserDictionary(morph: String, tag: POSTag) {
    addUserDictionary(morph -> tag)
  }

  override def addUserDictionary(dict: (String, POSTag)*) {
    val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(usrDicPath, true)))
    dict.foreach {
      case (morph, tag) =>
        writer.write(morph)
        writer.write('\t')
        writer.write(tagToHNN(tag))
        writer.newLine()
    }
    writer.close()
  }

  override def contains(word: String, posTag: Set[POSTag] = Set(POS.NNP, POS.NNG)): Boolean = {
    val oTag = posTag.map(x => tagSet.getTagID(tagToHNN(x)))
    fetchFrom(word, systemDic, oTag) || items.exists(x => x._1 == word && posTag.contains(x._2))
  }

  override def items: Set[(String, POSTag)] = {
    if (mapLastUpdate < usrDicPath.lastModified()) {
      mapLastUpdate = usrDicPath.lastModified()
      usrBuffer ++= Source.fromFile(usrDicPath).getLines().toStream.map {
        line =>
          val segs = line.split('\t')
          segs(0) -> fromHNNTag(segs(1))
      }
    }

    usrBuffer
  }

  private def fetchFrom(word: String, dict: Trie, oTag: Set[Int]) = {
    val morph = dict.fetch(Code.toTripleArray(word))
    if (morph == null) false
    else morph.info_list.exists(x => oTag.contains(x.tag))
  }

  def loadDictionary() =
    userDic synchronized {
      if (dicLastUpdate < usrDicPath.lastModified()) {
        dicLastUpdate = usrDicPath.lastModified()
        userDic.search_end = 0
        userDic.read_dic(usrDicPath.getAbsolutePath, tagSet)
      }
    }

  override def baseEntriesOf(f: (POSTag) => Boolean): Iterator[(String, POSTag)] = {
    val targetIDs = POS.values.filter(f).map(x => tagSet.getTagID(tagToHNN(x)))
    type TNode = Trie#TNODE

    @tailrec
    def iterate(stack: mutable.Stack[(Array[Char], TNode)],
                acc: Seq[(String, POSTag)] = Seq.empty): Seq[(String, POSTag)] =
      if (stack.isEmpty) acc
      else {
        val (prefix, top) = stack.pop()
        val word = prefix :+ top.key
        val value = top.info_list

        val newSeq = if (value != null && value.exists(x => targetIDs.contains(x.tag))) {
          val wordstr = Code.toString(word)
          value.filter(x => targetIDs.contains(x.tag))
            .map(x => wordstr -> fromHNNTag(tagSet.getTagName(x.tag))) ++: acc
        } else acc

        if (top.child_size > 0) {
          val children = top.child_idx until (top.child_idx + top.child_size)
          stack.pushAll(children.map(id => word -> systemDic.get_node(id)))
        }

        iterate(stack, newSeq)
      }

    iterate(mutable.Stack(Array.empty[Char] -> systemDic.node_head)).toIterator
  }

  override protected def modelName: String = "hannanum"
}
