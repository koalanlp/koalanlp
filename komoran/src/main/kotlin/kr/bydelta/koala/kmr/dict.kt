@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.kmr

import kr.bydelta.koala.POS
import kr.bydelta.koala.proc.CanCompileDict
import kr.bydelta.koala.proc.CanExtractResource
import kr.bydelta.koala.proc.DicEntry
import kr.co.shineware.ds.aho_corasick.model.AhoCorasickNode
import kr.co.shineware.nlp.komoran.constant.FILENAME
import kr.co.shineware.nlp.komoran.model.ScoredTag
import kr.co.shineware.nlp.komoran.modeler.model.Observation
import kr.co.shineware.nlp.komoran.parser.KoreanUnitParser
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * 코모란 분석기 사용자사전 인터페이스를 제공합니다.
 *
 * @since 1.x
 */
object Dictionary : CanExtractResource(), CanCompileDict {

    /**
     * 사용자사전을 저장할 파일의 위치.
     *
     * @since 1.x
     */
    @JvmStatic
    internal val userDict by lazy {
        val file = File(extractResource(), "koala.dict")
        file.createNewFile()
        file.deleteOnExit()
        file
    }

    /**
     * 시스템 사전
     *
     * @since 1.x
     */
    @JvmStatic
    private val systemdic by lazy {
        val o = Observation()
        o.load(o::class.java.classLoader.getResourceAsStream("${FILENAME.FULL_MODEL}/${FILENAME.OBSERVATION}"))
        o
    }

    /** 한글 분해/합성 */
    @JvmStatic
    private val unitparser by lazy { KoreanUnitParser() }
    /** 사용자 사전 버퍼 */
    @JvmStatic
    private val userBuffer = mutableListOf<DicEntry>()
    /** 사전 항목 버퍼 */
    @JvmStatic
    private var baseEntries = mutableListOf<Pair<String, List<POS>>>()

    /**
     * 사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가합니다.
     *
     * @since 1.x
     * @param dict 추가할 (표면형, 품사)의 순서쌍들 (가변인자). 즉, [Pair]<[String], [POS]>들
     */
    override fun addUserDictionary(vararg dict: DicEntry) = synchronized(userDict) {
        userDict.parentFile.mkdirs()
        FileOutputStream(userDict, true).bufferedWriter().use { bw ->
            dict.forEach {
                val (str, pos) = it
                bw.write("$str\t${pos.fromSejongPOS()}\n")
            }
        }
    }

    /**
     * 사전에 등재되어 있는지 확인하고, 사전에 없는단어만 반환합니다.
     *
     * @since 1.x
     * @param onlySystemDic 시스템 사전에서만 검색할지 결정합니다.
     * @param word          확인할 (형태소, 품사)들.
     * @return 사전에 없는 단어들, 즉, [Pair]<[String], [POS]>들.
     */
    override fun getNotExists(onlySystemDic: Boolean, vararg word: DicEntry): Array<DicEntry> {
        // Filter out existing morphemes!
        val (_, system) =
                if (onlySystemDic) Pair(emptyList(), word.toList())
                else word.partition { getItems().contains(it) }

        return system.groupBy { it.first }.flatMap { entry ->
            val (w, tags) = entry
            val searched =
                    try {
                        systemdic.trieDictionary.get(unitparser.parse(w))
                    } catch (_: NullPointerException) {
                        emptyMap<String, List<ScoredTag>>()
                    }

            // Filter out existing morphemes!
            if (searched.isEmpty()) tags // For the case of not found.
            else {
                val found = searched.map {
                    val (units, scoredtag) = it
                    val surface = unitparser.combine(units)
                    val tag = scoredtag.map { t -> t.tag }
                    surface to tag
                }.filter { it.first == w }.flatMap { it.second }.toList()
                tags.filterNot { found.contains(it.second.fromSejongPOS()) }
            }
        }.toTypedArray()
    }

    /**
     * 사용자 사전에 등재된 모든 Item을 불러옵니다.
     *
     * @since 1.x
     * @return (형태소, 통합품사)의 Sequence.
     */
    override fun getItems(): Set<DicEntry> = synchronized(userDict) {
        userBuffer.clear()
        userBuffer.addAll(userDict.bufferedReader().lines().iterator().asSequence().map {
            val segs = it.split('\t')
            segs[0] to segs[1].toSejongPOS()
        })

        userBuffer.toSet()
    }

    /**
     * 원본 사전에 등재된 항목 중에서, 지정된 형태소의 항목만을 가져옵니다. (복합 품사 결합 형태는 제외)
     *
     * @since 1.x
     * @param filter 가져올 품사인지 판단하는 함수.
     * @return (형태소, 품사)의 Iterator.
     */
    override fun getBaseEntries(filter: (POS) -> Boolean): Iterator<DicEntry> {
        return extractBaseEntries().flatMap { pair ->
            val (word, tags) = pair
            tags.filter(filter).map { word to it }
        }.iterator()
    }

    /** 사전 등재 항목 디코딩 */
    @JvmStatic
    private fun extractBaseEntries(): List<Pair<String, List<POS>>> =
            if (baseEntries.isNotEmpty()) baseEntries
            else synchronized(this) {
                val stack = Stack<Pair<List<Char>, AhoCorasickNode<List<ScoredTag?>>>>()
                stack.push(emptyList<Char>() to systemdic.trieDictionary.newFindContext().currentNode)

                while (stack.isNotEmpty()) {
                    val (prefix, top) = stack.pop()
                    val word = if (top.parent == null) prefix else prefix + top.key
                    val value = top.value ?: emptyList()

                    if (value.any { it != null }) {
                        val wordstr = unitparser.combine(word.joinToString(""))
                        baseEntries.add(wordstr to value.mapNotNull { it?.tag.toSejongPOS() })
                    }

                    top.children?.forEach {
                        stack.push(word to it)
                    }
                }

                baseEntries
            }

    /**
     * 모델의 명칭입니다.
     *
     * @since 1.x
     */
    override val modelName: String = "komoran"
}
