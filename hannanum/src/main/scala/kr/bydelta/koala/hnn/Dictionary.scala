package kr.bydelta.koala.hnn

import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter}

import kaist.cilab.jhannanum.common.Code
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.datastructure.{TagSet, Trie}
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.resource.{AnalyzedDic, Connection, ConnectionNot, NumberAutomata}
import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.traits.{CanCompileDict, CanExtractResource}

import scala.annotation.tailrec
import scala.collection.JavaConverters._
import scala.io.Source

/**
  * 한나눔 사용자사전
  */
object Dictionary extends CanCompileDict with CanExtractResource {
  private[koala] lazy val tagSet: TagSet = {
    val tagSet = new TagSet
    val fileTagSet: String = extractResource() + File.separator + "data/kE/tag_set.txt"
    tagSet.init(fileTagSet, TagSet.TAG_SET_KAIST)
    tagSet
  }
  private[koala] lazy val connection: Connection = {
    val connection = new Connection
    val fileConnections: String = extractResource() + File.separator + "data/kE/connections.txt"
    connection.init(fileConnections, tagSet.getTagCount, tagSet)
    connection
  }
  private[koala] lazy val connectionNot: ConnectionNot = {
    val connectionNot = new ConnectionNot
    val fileConnectionsNot: String = extractResource() + File.separator + "data/kE/connections_not.txt"
    connectionNot.init(fileConnectionsNot, tagSet)
    connectionNot
  }
  private[koala] lazy val analyzedDic: AnalyzedDic = {
    val fileDicAnalyzed: String = extractResource() + File.separator + "data/kE/dic_analyzed.txt"
    val analyzedDic = new AnalyzedDic
    analyzedDic.readDic(fileDicAnalyzed)
    analyzedDic
  }
  private[koala] lazy val systemDic: Trie = {
    val fileDicSystem: String = extractResource() + File.separator + "data/kE/dic_system.txt"
    val systemDic = new Trie(Trie.DEFAULT_TRIE_BUF_SIZE_SYS)
    systemDic.read_dic(fileDicSystem, tagSet)
    systemDic
  }
  private lazy val usrDicPath: File = {
    val f = new File(extractResource() + File.separator + "data/kE/dic_user.txt")
    f.createNewFile()
    f.deleteOnExit()
    f
  }
  /** 사용자사전. **/
  private[koala] lazy val userDic: Trie = new Trie(Trie.DEFAULT_TRIE_BUF_SIZE_USER)
  private[koala] lazy val numAutomata: NumberAutomata = new NumberAutomata
  private var baseEntries = Seq[(String, Seq[POSTag])]()
  private[koala] var usrBuffer = Set[(String, POSTag)]()
  private var dicLastUpdate = 0l

  override def addUserDictionary(morph: String, tag: POSTag) {
    addUserDictionary(morph -> tag)
  }

  override def addUserDictionary(dict: (String, POSTag)*) {
    val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(usrDicPath, true)))
    dict.foreach {
      case (morph, tag) =>
        writer.write(morph)
        writer.write('\t')
        writer.write(fromSejongPOS(tag).toLowerCase)
        writer.newLine()
    }
    writer.close()
  }

  override def getNotExists(onlySystemDic: Boolean, word: (String, POSTag)*): Seq[(String, POSTag)] = {
    val (_, system) =
      if (onlySystemDic) (Seq.empty[(String, POSTag)], word)
      else word.partition(items.contains) // filter out existing morphemes!

    system.groupBy(_._1).iterator.flatMap {
      case (w, tags) =>
        val morph = systemDic.fetch(Code.toTripleArray(w))

        // filter out existing morphemes!
        if (morph == null) tags // The case of not found.
        else {
          val found = morph.info_list.asScala.map(_.tag)
          tags.filterNot {
            case (_, t) => found.contains(tagSet.getTagID(fromSejongPOS(t).toLowerCase))
          }
        }
    }.toSeq
  }

  override def items: Set[(String, POSTag)] = {
    usrBuffer ++= Source.fromFile(usrDicPath).getLines().toStream.map {
      line =>
        val segments = line.split('\t')
        segments(0) -> toSejongPOS(segments(1))
    }

    usrBuffer
  }

  def loadDictionary(): Unit =
    userDic synchronized {
      if (dicLastUpdate < usrDicPath.lastModified()) {
        dicLastUpdate = usrDicPath.lastModified()
        userDic.search_end = 0
        userDic.read_dic(usrDicPath.getAbsolutePath, tagSet)
      }
    }

  override def baseEntriesOf(f: (POSTag) => Boolean): Iterator[(String, POSTag)] = {
    extractBaseEntries().iterator.collect {
      case (word, tags) if tags.exists(f) =>
        tags.filter(f).map(x => word -> x)
    }.flatten
  }

  private def extractBaseEntries(): Seq[(String, Seq[POSTag])] =
    if (baseEntries.nonEmpty) baseEntries
    else this.synchronized {
      type TNode = Trie#TNODE

      @tailrec
      def iterate(stack: List[(Array[Char], TNode)]): Unit =
        if (stack.nonEmpty) {
          val (prefix, top) = stack.head
          var nStack = stack.tail

          val word = prefix :+ top.key
          val value = top.info_list

          if (value != null) {
            val wordString = Code.toString(word)
            baseEntries +:= (wordString -> value.asScala.map(x => toSejongPOS(tagSet.getTagName(x.tag))))
          }

          if (top.child_size > 0) {
            val children = top.child_idx until (top.child_idx + top.child_size)
            nStack ++:= children.map(id => word -> systemDic.get_node(id))
          }

          iterate(nStack)
        }

      iterate(List(Array.empty[Char] -> systemDic.node_head))

      baseEntries
    }

  override protected def modelName: String = "hannanum"
}
