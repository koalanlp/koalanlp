@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.eunjeon

import kr.bydelta.koala.POS
import kr.bydelta.koala.data.Morpheme
import kr.bydelta.koala.isHangulEnding
import kr.bydelta.koala.isJongsungEnding
import kr.bydelta.koala.proc.CanCompileDict
import kr.bydelta.koala.proc.DicEntry
import org.bitbucket.eunjeon.seunjeon.ConnectionCostDict
import org.bitbucket.eunjeon.seunjeon.DictBuilder
import org.bitbucket.eunjeon.seunjeon.LexiconDict
import org.bitbucket.eunjeon.seunjeon.NngUtil


/**
 * 은전한닢 사용자사전 인터페이스입니다.
 *
 * @since 1.x
 */
object Dictionary : CanCompileDict {
    @JvmStatic
    private val leftIDMap: Map<String, Short> by lazy {
        NngUtil::class.java.classLoader.getResourceAsStream(DictBuilder.LEFT_ID_DEF().drop(1)).bufferedReader().lines()
                .iterator().asSequence()
                .map {
                    val splits = it.split(" ")
                    splits[1].replace("^([^,]+,[^,]+),.*".toRegex(), "$1") to splits[0].toShort()
                }.groupBy { it.first }
                .filterValues { it.isNotEmpty() }
                .mapValues { it.value.maxBy { x -> x.second }!!.second }
    }

    @JvmStatic
    private val rightIDMap: Map<String, Short> by lazy {
        NngUtil::class.java.classLoader.getResourceAsStream(DictBuilder.RIGHT_ID_DEF().drop(1)).bufferedReader().lines()
                .iterator().asSequence()
                .map {
                    val splits = it.split(" ")
                    splits[1].replace("^([^,]+,[^,]+,[^,]+),.*".toRegex(), "$1") to splits[0].toShort()
                }.groupBy { it.first }
                .filterValues { it.isNotEmpty() }
                .mapValues { it.value.maxBy { x -> x.second }!!.second }
    }

    /**
     * Check whether compression required
     * @since 1.x
     */
    @JvmStatic
    internal val needCompress = Runtime.getRuntime().freeMemory() / 1024.0 / 1024.0 / 1024.0 < 1.0

    /**
     * 은전한닢 어휘사전.
     * @since 1.x
     */
    @JvmStatic
    internal val lexiconDict by lazy { LexiconDict().load(needCompress) }

    /**
     * 은전한닢 연결성 사전.
     * @since 1.x
     */
    @JvmStatic
    internal val connectionCostDict = ConnectionCostDict().load()

    /**
     * 은전한닢 사용자사전 객체
     * @since 1.x
     */
    @JvmStatic
    internal val userDict = LexiconDict().loadFromIterator(emptyList<String>().asScala(), false)

    /**
     * 사용자사전에 등재되기 전의 리스트.
     * @since 1.x
     */
    @JvmStatic
    private val rawDict = mutableSetOf<String>()

    /**
     * 사용자사전 변경여부.
     * @since 1.x
     */
    @JvmStatic
    private var isDicChanged = false

    /**
     * 사전에 항목이 있는지 확인.
     *
     * @since 1.x
     * @return True: 항목이 있을 때.
     */
    @JvmStatic
    internal fun isNotEmpty(): Boolean = synchronized(rawDict) { rawDict.isNotEmpty() }

    /**
     * 사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가합니다.
     *
     * @since 1.x
     * @param dict 추가할 (표면형, 품사)의 순서쌍들 (가변인자). 즉, [Pair]<[String], [POS]>들
     */
    override fun addUserDictionary(vararg dict: DicEntry) {
        synchronized(rawDict) {
            rawDict.addAll(dict.map { entry ->
                val (word, tag) = entry
                val oTag = tag.fromSejongPOS()

                if (word.isHangulEnding()) {
                    val jong = if (word.isJongsungEnding()) "T" else "F"
                    "$word,${getLeftId(oTag)},${getRightId(oTag, jong)},0,$oTag,*,$jong,$word,*,*,*,*"
                } else {
                    "$word,${getLeftId(oTag)},${getRightId(oTag)},0,$oTag,*,*,$word,*,*,*,*"
                }
            })

            isDicChanged = true
        }
    }

    /**
     * (Code modified from Seunjeon package)
     * Left ID 부여
     *
     * @since 1.x
     * @param tag Left ID를 찾을 Tag
     * @return Left ID
     */
    @JvmStatic
    private fun getLeftId(tag: String = "NNG"): Short {
        return leftIDMap["$tag,*"] ?: leftIDMap.values.max()!!
    }

    /**
     * (Code modified from Seunjeon package)
     * Right ID 부여
     *
     * @since 1.x
     * @param tag         Right ID를 찾을 Tag
     * @param hasJongsung 종성이 있으면 "T", 없으면 "F", 해당없는 문자는 "*"
     * @return Right ID
     */
    @JvmStatic
    private fun getRightId(tag: String = "NNG", hasJongsung: String = "*"): Short {
        return rightIDMap["$tag,*,$hasJongsung"] ?: leftIDMap.values.max()!!
    }

    /**
     * 사용자 사전에 등재된 모든 Item을 불러옵니다.
     *
     * @since 1.x
     * @return (형태소, 통합품사)의 Sequence.
     */
    override fun getItems(): Set<DicEntry> =
            rawDict.map {
                val segs = it.split(",")
                segs[0] to segs[4].toSejongPOS()
            }.toSet()

    /**
     * 사전에 등재되어 있는지 확인하고, 사전에 없는단어만 반환합니다.
     *
     * @since 1.x
     * @param onlySystemDic 시스템 사전에서만 검색할지 결정합니다.
     * @param word          확인할 (형태소, 품사)들.
     * @return 사전에 없는 단어들, 즉, [Pair]<[String], [POS]>들.
     */
    override fun getNotExists(onlySystemDic: Boolean, vararg word: DicEntry): Array<DicEntry> {
        reloadDic()
        return word.groupBy { it.first }.flatMap { entryset ->
            val (w, tags) = entryset

            val searched = (
                    if (onlySystemDic) lexiconDict.commonPrefixSearch(w).asJava()
                    else lexiconDict.commonPrefixSearch(w).asJava() + userDict.commonPrefixSearch(w).asJava()
                    ).filter { it.surface == w && it.feature.last() == '*' }

            // Filter out existing morphemes!
            if (searched.isNotEmpty()) {
                val found = searched.mapNotNull { it.featureHead }

                tags.filterNot {
                    found.contains(it.second.fromSejongPOS())
                }
            } else tags // The case of not found
        }.toTypedArray()
    }

    /**
     * 사용자사전을 다시 불러옴.
     *
     * @since 1.x
     */
    @JvmStatic
    internal fun reloadDic(): Unit = synchronized(rawDict) {
        if (isDicChanged) {
            userDict.loadFromIterator(rawDict.asScala(), false)
            isDicChanged = false
        }
    }

    /**
     * 원본 사전에 등재된 항목 중에서, 지정된 형태소의 항목만을 가져옵니다. (복합 품사 결합 형태는 제외)
     *
     * @since 1.x
     * @param filter 가져올 품사인지 판단하는 함수.
     * @return (형태소, 품사)의 Iterator.
     */
    override fun getBaseEntries(filter: (POS) -> Boolean): Iterator<DicEntry> =
            lexiconDict.termDict().asSequence().mapNotNull {
                val converted = convertMorpheme(it)
                if (converted.size == 1 && filter(converted[0].tag)) converted[0].surface to converted[0].tag
                else null
            }.iterator()

    /**
     * 은전한닢의 형태소 객체를 KoalaNLP의 [Morpheme] 객체들로 변환합니다.
     *
     * @since 1.x
     * @param m: 은전한닢에서 생성한 형태소 객체
     * @return KoalaNLP의 [Morpheme] 객체 목록. 만약 복합명사였을 경우 복합명사는 모두 분리됨.
     */
    @JvmStatic
    fun convertMorpheme(m: org.bitbucket.eunjeon.seunjeon.Morpheme): List<Morpheme> {
        val array = m.feature.split(",")
        val compoundTag = array[0]
        val tokens = array.last()

        return if (array.size == 1 || tokens == "*") {
            listOf(Morpheme(m.surface, compoundTag.toSejongPOS(), compoundTag))
        } else {
            tokens.split("+").map {
                val arr = it.split("/")
                Morpheme(arr[0], arr[1].toSejongPOS(), arr[1])
            }
        }
    }
}