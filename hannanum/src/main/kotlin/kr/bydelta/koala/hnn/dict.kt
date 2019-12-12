@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.hnn

import kaist.cilab.jhannanum.common.Code
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.datastructure.TagSet
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.datastructure.Trie
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.resource.AnalyzedDic
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.resource.Connection
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.resource.NumberAutomata
import kr.bydelta.koala.POS
import kr.bydelta.koala.proc.CanCompileDict
import kr.bydelta.koala.proc.CanExtractResource
import kr.bydelta.koala.proc.DicEntry
import java.io.File
import java.io.FileOutputStream
import java.util.*


/**
 * 한나눔 사용자사전 인터페이스입니다.
 *
 * @since 1.x
 */
object Dictionary : CanCompileDict, CanExtractResource() {
    /** 품사표기 집합 */
    @JvmStatic
    internal val tagSet: TagSet by lazy {
        val tagSet = TagSet()
        val fileTagSet: String = extractResource() + File.separator + "data/kE/tag_set.txt"
        tagSet.init(fileTagSet, TagSet.TAG_SET_KAIST)
        tagSet
    }
    /** 연결가능 */
    @JvmStatic
    internal val connection: Connection by lazy {
        val connection = Connection()
        val fileConnections: String = extractResource() + File.separator + "data/kE/connections.txt"
        connection.init(fileConnections, tagSet.tagCount, tagSet)
        connection
    }
    /** 기 분석 사전 */
    @JvmStatic
    internal val analyzedDic: AnalyzedDic by lazy {
        val fileDicAnalyzed: String = extractResource() + File.separator + "data/kE/dic_analyzed.txt"
        val analyzedDic = AnalyzedDic()
        analyzedDic.readDic(fileDicAnalyzed)
        analyzedDic
    }
    /** 시스템 사전 */
    @JvmStatic
    internal val systemDic: Trie by lazy {
        val fileDicSystem: String = extractResource() + File.separator + "data/kE/dic_system.txt"
        val systemDic = Trie(Trie.DEFAULT_TRIE_BUF_SIZE_SYS)
        systemDic.read_dic(fileDicSystem, tagSet)
        systemDic
    }
    /** 사용자 사전 파일 */
    @JvmStatic
    private val usrDicPath: File by lazy {
        val f = File(extractResource() + File.separator + "data/kE/dic_user.txt")
        f.createNewFile()
        f.deleteOnExit()
        f
    }
    /** 사용자 사전 (한나눔) **/
    @JvmStatic
    internal val userDic: Trie by lazy { Trie(Trie.DEFAULT_TRIE_BUF_SIZE_USER) }
    /** 숫자 처리 */
    @JvmStatic
    internal val numAutomata: NumberAutomata by lazy { NumberAutomata() }
    /** 시스템 사전 (Koala) */
    @JvmStatic
    private val baseEntries = mutableListOf<Pair<String, List<POS>>>()
    /** 사용자 사전 (Koala) */
    @JvmStatic
    private val usrBuffer = mutableSetOf<Pair<String, POS>>()
    /** 마지막 사용자사전 업데이트 시각 */
    @JvmStatic
    private var dicLastUpdate = 0L

    /**
     * 사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가합니다.
     *
     * @since 1.x
     * @param dict 추가할 (표면형, 품사)의 순서쌍들 (가변인자). 즉, [Pair]<[String], [POS]>들
     */
    override fun addUserDictionary(vararg dict: DicEntry) {
        synchronized(usrDicPath) {
            val writer = FileOutputStream(usrDicPath, true).bufferedWriter()
            dict.forEach {
                val (morph, tag) = it

                writer.write("$morph\t${tag.fromSejongPOS().toLowerCase()}\n")
            }
            writer.close()
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
        val (_, system) =
                if (onlySystemDic) Pair(emptyList(), word.toList())
                else word.partition { getItems().contains(it) } // filter out existing morphemes!

        return system.groupBy { it.first }.flatMap { entry ->
            val (w, tags) = entry
            val morph = systemDic.fetch(Code.toTripleArray(w))

            // filter out existing morphemes!
            if (morph == null) tags // The case of not found.
            else {
                val found = morph.info_list.map { m ->
                    tagSet.getTagName(m.tag).toSejongPOS()
                }.toSet()
                tags.filterNot { m -> m.second in found }
            }
        }.toTypedArray()
    }

    /**
     * 사용자 사전에 등재된 모든 Item을 불러옵니다.
     *
     * @since 1.x
     * @return (형태소, 통합품사)의 Sequence.
     */
    override fun getItems(): Set<DicEntry> {
        usrBuffer += synchronized(usrDicPath){
            usrDicPath.readLines().asSequence().map {
                val segments = it.split('\t')
                segments[0] to segments[1].toSejongPOS()
            }
        }

        return usrBuffer
    }

    /**
     * 사전을 다시 불러옵니다.
     */
    @JvmStatic
    internal fun loadDictionary(): Unit =
            synchronized(userDic) {
                if (dicLastUpdate < usrDicPath.lastModified()) {
                    dicLastUpdate = usrDicPath.lastModified()
                    userDic.search_end = 0
                    userDic.read_dic(usrDicPath.absolutePath, tagSet)
                }
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
        }.listIterator()
    }

    /**
     * 시스템 사전의 내용을 불러옵니다.
     */
    @JvmStatic
    private fun extractBaseEntries(): List<Pair<String, List<POS>>> =
            if (baseEntries.isNotEmpty()) baseEntries
            else synchronized(this) {
                val stack = Stack<Pair<List<Char>, Trie.TNODE>>()
                stack.push(emptyList<Char>() to systemDic.node_head)

                while (stack.isNotEmpty()) {
                    val (prefix, top) = stack.pop()

                    val word = if (top.key.toInt() != 0) prefix + top.key else prefix
                    val value = top.info_list

                    if (value != null) {
                        val wordString = Code.toString(word.toCharArray())
                        baseEntries.add(wordString to value.map { tagSet.getTagName(it.tag).toSejongPOS() })
                    }

                    if (top.child_size > 0) {
                        for (id in top.child_idx until (top.child_idx + top.child_size)) {
                            stack.push(word to systemDic.get_node(id))
                        }
                    }
                }

                baseEntries
            }

    /**
     * 모델의 명칭입니다.
     */
    override val modelName: String
        get() = "hannanum"
}