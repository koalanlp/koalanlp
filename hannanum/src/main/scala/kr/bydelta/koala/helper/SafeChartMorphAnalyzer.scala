package kr.bydelta.koala.helper

import java.io.File
import java.util
import java.util.StringTokenizer

import kaist.cilab.jhannanum.common.communication.{PlainSentence, SetOfSentences}
import kaist.cilab.jhannanum.common.{Code, Eojeol, JSONReader}
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.datastructure.{TagSet, Trie}
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.resource.{AnalyzedDic, Connection, ConnectionNot, NumberAutomata}
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.{MorphemeChart, PostProcessor, Simti}
import kaist.cilab.jhannanum.plugin.major.morphanalyzer.MorphAnalyzer
import kaist.cilab.parser.berkeleyadaptation.Configuration
import kr.bydelta.koala.hnn.Dictionary

/**
  * SafeChartMorphAnalyzer의 Companion object
  */
private[koala] object SafeChartMorphAnalyzer {
  /** 사용자사전. **/
  val userDic: Trie = new Trie(Trie.DEFAULT_TRIE_BUF_SIZE_USER)
  private val tagSet: TagSet = new TagSet
  private val connection: Connection = new Connection
  private val connectionNot: ConnectionNot = new ConnectionNot
  private var analyzedDic: AnalyzedDic = _
  private var systemDic: Trie = _
}

/**
  * 한나눔 ChartMorphAnalyzer를 개량한 클래스.
  * - Scala에 맞게 수정.
  * - 사용자사전을 동적으로 불러올 수 있게 수정.
  * - 모델의 Path를 임시 폴더로 수정.
  */
private[koala] class SafeChartMorphAnalyzer extends MorphAnalyzer {
  private var eojeolList = new util.LinkedList[Eojeol]()
  private var postProc: PostProcessor = _
  private var chart: MorphemeChart = _

  def getName: String = "MorphAnalyzer"

  def morphAnalyze(ps: PlainSentence): SetOfSentences = {
    val st = new Iterator[String] {
      val tokenizer = new StringTokenizer(ps.getSentence, " \t")

      override def hasNext: Boolean = tokenizer.hasMoreTokens

      override def next(): String = tokenizer.nextToken()

      override def size: Int = tokenizer.countTokens()
    }
    val (plainEojeolArray, eojeolSetArray) =
      st.foldLeft((new util.ArrayList[String](st.size), new util.ArrayList[Array[Eojeol]](st.size))) {
        case (t@(pE, eS), tok) =>
          pE.add(tok)
          eS.add(processEojeol(tok))
          t
      }
    postProc.doPostProcessing(
      new SetOfSentences(ps.getDocumentID, ps.getSentenceID, ps.isEndOfDocument, plainEojeolArray, eojeolSetArray)
    )
  }

  private def processEojeol(plainEojeol: String): Array[Eojeol] = {
    eojeolList.clear()
    SafeChartMorphAnalyzer.analyzedDic.get(plainEojeol) match {
      case analysis: String =>
        new Iterator[Eojeol] {
          val tokenizer = new StringTokenizer(analysis, "^")

          override def hasNext: Boolean = tokenizer.hasMoreTokens

          override def next(): Eojeol = {
            val (morph, tag) = tokenizer.nextToken.split("\\+|/").sliding(2, 2).map {
              slice => (slice.head, slice.last)
            }.toSeq.unzip
            new Eojeol(morph.toArray, tag.toArray)
          }
        }.foreach(eojeolList.add(_))
      case null =>
        try {
          chart.init(plainEojeol)
          chart.analyze
          chart.getResult()
        } catch {
          case e: Exception =>
            eojeolList.clear()
            eojeolList.add(new Eojeol(Array[String](plainEojeol), Array[String]("nqq")))
        }
    }
    eojeolList.toArray(new Array[Eojeol](0))
  }

  def shutdown() {
  }

  @throws[Exception]
  def initialize(configFile: String) {
    initialize(configFile, Configuration.hanBaseDir)
  }

  @throws[Exception]
  def initialize(configFile: String, dummy: String) {
    val baseDir = Dictionary.getExtractedPath
    if (SafeChartMorphAnalyzer.systemDic == null) {
      val json: JSONReader = new JSONReader(configFile)
      val fileDicSystem: String = baseDir + File.separator + json.getValue("dic_system")
      val fileConnections: String = baseDir + File.separator + json.getValue("connections")
      val fileConnectionsNot: String = baseDir + File.separator + json.getValue("connections_not")
      val fileDicAnalyzed: String = baseDir + File.separator + json.getValue("dic_analyzed")
      val fileTagSet: String = baseDir + File.separator + json.getValue("tagset")
      SafeChartMorphAnalyzer.tagSet.init(fileTagSet, TagSet.TAG_SET_KAIST)
      SafeChartMorphAnalyzer.connection.init(fileConnections, SafeChartMorphAnalyzer.tagSet.getTagCount,
        SafeChartMorphAnalyzer.tagSet)
      SafeChartMorphAnalyzer.connectionNot.init(fileConnectionsNot, SafeChartMorphAnalyzer.tagSet)
      SafeChartMorphAnalyzer.analyzedDic = new AnalyzedDic
      SafeChartMorphAnalyzer.analyzedDic.readDic(fileDicAnalyzed)
      SafeChartMorphAnalyzer.systemDic = new Trie(Trie.DEFAULT_TRIE_BUF_SIZE_SYS)
      SafeChartMorphAnalyzer.systemDic.read_dic(fileDicSystem, SafeChartMorphAnalyzer.tagSet)
    }

    val numAutomata: NumberAutomata = new NumberAutomata
    val simti: Simti = new Simti
    simti.init()
    eojeolList = new util.LinkedList[Eojeol]()
    chart = new MorphemeChart(SafeChartMorphAnalyzer.tagSet, SafeChartMorphAnalyzer.connection,
      SafeChartMorphAnalyzer.systemDic,
      SafeChartMorphAnalyzer.userDic, numAutomata, simti, eojeolList)
    postProc = new PostProcessor
  }

  def addMorphemes(pairs: collection.Map[String, String]) {
    pairs.foreach {
      case (morph, tag) =>
        val codes: Array[Char] = Code.toTripleArray(morph)
        val info = new SafeChartMorphAnalyzer.userDic.INFO
        info.tag = SafeChartMorphAnalyzer.tagSet.getTagID(tag)
        info.phoneme = TagSet.PHONEME_TYPE_ALL
        SafeChartMorphAnalyzer.userDic.store(codes, info)
    }
  }
}