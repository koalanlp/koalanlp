package kr.bydelta.koala.kmr

import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter}

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala._
import kr.bydelta.koala.traits.{CanCompileDict, CanExtractResource}
import kr.co.shineware.ds.trie.model.TrieNode
import kr.co.shineware.nlp.komoran.modeler.model.{Observation, PosTable}
import kr.co.shineware.util.common.model.{Pair => KPair}

import scala.annotation.tailrec
import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.io.Source
import scala.util.Random

/**
  * 코모란 분석기 사용자사전
  */
object Dictionary extends CanCompileDict with CanExtractResource {
  /**
    * 사용자사전을 저장할 파일의 위치.
    */
  lazy val userDict = {
    val file = new File(getExtractedPath, "koala.dict")
    file.createNewFile()
    file.deleteOnExit()
    file
  }
  private lazy val dic = {
    val obs = new Observation
    obs.load(getExtractedPath + File.separator + "observation.model")
    obs.getTrieDictionary
  }
  private lazy val table = {
    val tbl = new PosTable
    tbl.load(getExtractedPath + File.separator + "pos.table")
    tbl
  }
  private var userLastUpdated = 0l

  private var userBuffer = Set[(String, POSTag)]()

  override def addUserDictionary(dict: (String, POSTag)*): Unit = Dictionary synchronized {
    userDict.getParentFile.mkdirs()
    val bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(userDict, true)))
    dict.foreach {
      case (str, pos) =>
        bw.write(str)
        bw.write('\t')
        bw.write(tagToKomoran(pos))
        bw.newLine()
    }
    bw.close()
  }

  override def addUserDictionary(morph: String, tag: POSTag): Unit = Dictionary synchronized {
    userDict.getParentFile.mkdirs()
    val bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(userDict, true)))
    bw.write(morph)
    bw.write('\t')
    bw.write(tagToKomoran(tag))
    bw.newLine()
    bw.close()
  }

  override def getNotExists(onlySystemDic: Boolean, word: (String, POSTag)*): Seq[(String, POSTag)] = {
    val (user, system) =
      if (onlySystemDic) (Seq.empty[(String, POSTag)], word)
      else word.partition(items.contains)
    user ++: system.groupBy(_._1).iterator.flatMap {
      case (w, tags) =>
        val searched = dic.get(w)
        if (searched == null) Seq.empty
        else {
          val found = searched.map(_.getFirst)
          tags.filter(t => found.contains(table.getId(tagToKomoran(t._2))))
        }
    }.toSeq
  }

  override def items: Set[(String, POSTag)] = userBuffer synchronized {
    if (userLastUpdated < userDict.lastModified()) {
      userLastUpdated = userDict.lastModified()
      userBuffer ++= Source.fromFile(userDict).getLines().map {
        line =>
          val segs = line.split('\t')
          segs(0) -> fromKomoranTag(segs(1))
      }
    }

    userBuffer
  }

  override def baseEntriesOf(f: (POSTag) => Boolean): Iterator[(String, POSTag)] = {
    type TNode = TrieNode[java.util.List[KPair[Integer, java.lang.Double]]]
    val targetIDs = POS.values.filter(f).map(p => table.getId(tagToKomoran(p)))

    @tailrec
    def iterate(stack: mutable.Stack[(String, TNode)],
                acc: Seq[(String, POSTag)] = Seq.empty): Seq[(String, POSTag)] =
      if (stack.isEmpty) acc
      else {
        val (prefix, top) = stack.pop()
        if (Random.nextDouble() < 0.01) println(prefix)
        val word = if (top.getKey == null) prefix else prefix + top.getKey.charValue()
        val value = top.getValue

        val newSeq = if (value != null && value.exists(x => targetIDs.contains(x.getFirst))) {
          value.filter(x => targetIDs.contains(x.getFirst))
            .map(x => word -> fromKomoranTag(table.getPos(x.getFirst))) ++: acc
        } else acc

        val children = top.getChildren
        if (children != null) {
          stack.pushAll(children.map(word -> _))
        }

        iterate(stack, newSeq)
      }

    iterate(mutable.Stack("" -> dic.getRoot)).toIterator
  }

  override protected def modelName: String = "komoran"
}
