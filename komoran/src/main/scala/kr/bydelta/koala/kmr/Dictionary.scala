package kr.bydelta.koala.kmr

import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter}

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.traits.{CanCompileDict, CanExtractResource}
import kr.co.shineware.ds.aho_corasick.model.AhoCorasickNode
import kr.co.shineware.nlp.komoran.model.ScoredTag
import kr.co.shineware.nlp.komoran.modeler.model.Observation
import kr.co.shineware.nlp.komoran.parser.KoreanUnitParser

import scala.annotation.tailrec
import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
  * 코모란 분석기 사용자사전
  */
object Dictionary extends CanCompileDict with CanExtractResource {


  /**
    * 사용자사전을 저장할 파일의 위치.
    */
  lazy val userDict: File = {
    val file = new File(extractResource(), "koala.dict")
    file.createNewFile()
    file.deleteOnExit()
    file
  }
  private lazy val systemdic = {
    val o = new Observation
    o.load(o.getClass.getClassLoader.getResourceAsStream("models_full" + File.separator + "observation.model"))
    o
  }
  private lazy val unitparser = new KoreanUnitParser()
  private val userBuffer = ArrayBuffer[(String, POSTag)]()
  private var baseEntries = Seq[(String, Seq[POSTag])]()

  override def addUserDictionary(dict: (String, POSTag)*): Unit = userDict synchronized {
    userDict.getParentFile.mkdirs()
    val bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(userDict, true)))
    dict.foreach {
      case (str, pos) =>
        bw.write(str)
        bw.write('\t')
        bw.write(fromSejongPOS(pos))
        bw.newLine()
    }
    bw.close()
  }

  override def addUserDictionary(morph: String, tag: POSTag): Unit = userDict synchronized {
    userDict.getParentFile.mkdirs()
    val bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(userDict, true)))
    bw.write(morph)
    bw.write('\t')
    bw.write(fromSejongPOS(tag))
    bw.newLine()
    bw.close()
  }

  override def getNotExists(onlySystemDic: Boolean, word: (String, POSTag)*): Seq[(String, POSTag)] = {
    // Filter out existing morphemes!
    val (_, system) =
      if (onlySystemDic) (Seq.empty[(String, POSTag)], word)
      else word.partition(items.contains)

    system.groupBy(_._1).iterator.flatMap {
      case (w, tags) =>
        val searched =
          try {
            systemdic.getTrieDictionary.get(unitparser.parse(w)).asScala
          } catch {
            case _: NullPointerException =>
              Map.empty
            case e: Throwable =>
              e.printStackTrace()
              Map.empty
          }

        // Filter out existing morphemes!
        if (searched.isEmpty) tags // For the case of not found.
        else {
          val found = searched.map {
            case (units, scoredtag) =>
              val word = unitparser.combine(units)
              val tag = scoredtag.asScala.map(_.getTag)
              word -> tag
          }.filter(_._1 == w).flatMap(_._2).toSeq
          tags.filterNot(t => found.contains(fromSejongPOS(t._2)))
        }
    }.toSeq
  }

  override def items: Set[(String, POSTag)] = userDict synchronized {
    userBuffer.clear()
    userBuffer appendAll Source.fromFile(userDict).getLines().map {
      line =>
        val segs = line.split('\t')
        segs(0) -> toSejongPOS(segs(1))
    }

    userBuffer.toSet
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
      @tailrec
      def iterate(stack: List[(Seq[Char], AhoCorasickNode[java.util.List[ScoredTag]])]): Unit =
        if (stack.nonEmpty) {
          val (prefix, top) = stack.head
          var nStack = stack.tail

          val word = if (top.getParent == null) prefix else prefix :+ top.getKey
          val value = if (top.getValue != null) top.getValue.asScala else Seq()

          if (value != null && value.exists(_ != null)) {
            val wordstr = unitparser.combine(word.mkString)
            baseEntries +:= wordstr -> value.map(x => toSejongPOS(x.getTag))
          }

          val children = top.getChildren
          if (children != null) {
            nStack ++:= children.map(word -> _)
          }

          iterate(nStack)
        }

      iterate(List(Seq.empty[Char] -> systemdic.getTrieDictionary.newFindContext().getCurrentNode))

      baseEntries
    }

  /**
    * 압축해제 작업없음. 임시폴더만 생성
    */
  override protected[koala] def extractResource(): String = this.getExtractedPath

  override protected def modelName: String = "komoran"
}
