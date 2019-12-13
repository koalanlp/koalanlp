@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.eunjeon

import kr.bydelta.koala.POS
import kr.bydelta.koala.data.Morpheme as KMorpheme
import kr.bydelta.koala.isHangulEnding
import kr.bydelta.koala.isJongsungEnding
import kr.bydelta.koala.proc.CanCompileDict
import kr.bydelta.koala.proc.DicEntry
import org.bitbucket.eunjeon.seunjeon.*
import scala.Enumeration
import scala.collection.Seq
import scala.collection.mutable.WrappedArray
import java.io.*
import java.nio.ByteBuffer
import java.nio.charset.Charset
import kotlin.system.exitProcess


/**
 * 은전한닢의 CompressedMorpheme 클래스가 Scala 2.12 이외의 환경에서 Serialization 이슈가 있어,
 * 이를 제거하기 위해 별도로 구성된 클래스입니다. 이 클래스는 [org.bitbucket.eunjeon.seunjeon.CompressedMorpheme]과 기본 구성이 같되,
 * Kotlin으로 재작성되었고, WrappedArray를 ObjectOutputStream에 dump하지 않고, Array로 변환하여 Dump하는 차이가 있습니다.
 * 내부 Method에 대한 상세한 설명은 SEunjeon을 참고하세요.
 */
class SafeCompressedMorpheme(private var _surface: ByteArray = byteArrayOf(),
                             private var _leftId: Short = 0, private var _rightId: Short = 0,
                             private var _cost: Int = 0, private var _feature: List<String> = emptyList(),
                             private var _mType: Byte = 0, private var _poses: ByteArray = byteArrayOf()): Morpheme, Serializable {
    override fun getSurface(): String = String(_surface, UTF_8)
    override fun getLeftId(): Short = _leftId
    override fun getRightId(): Short = _rightId
    override fun getCost(): Int = _cost
    override fun getFeature(): String = _feature.joinToString(",")
    override fun getFeatureHead(): String = _feature.first()
    override fun getMType() = MorphemeType.apply(_mType.toInt())
    override fun getPoses(): WrappedArray<Enumeration.Value> =
            WrappedArray.ofRef<Enumeration.Value>(_poses.map { Pos.apply(it.toInt()) }.toTypedArray())

    @Suppress("UNCHECKED_CAST")
    override fun deComposite(): Seq<Morpheme> = `BasicMorpheme$`.`MODULE$`.deComposite(_feature[7]) as Seq<Morpheme>
    fun uncompressed() = BasicMorpheme.apply(this)

    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.writeObject(_surface)
        out.writeShort(_leftId.toInt())
        out.writeShort(_rightId.toInt())
        out.writeInt(_cost)
        out.writeObject(_feature.toTypedArray())
        out.writeByte(_mType.toInt())
        out.writeObject(_poses)
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class)
    private fun readObject(input: ObjectInputStream) {
        _surface = input.readObject() as ByteArray
        _leftId = input.readShort()
        _rightId = input.readShort()
        _cost = input.readInt()

        _feature = (input.readObject() as Array<String>).toList()
        _mType = input.readByte()
        _poses = input.readObject() as ByteArray
    }

    /**
     * SafeCompressedMorpheme의 Companion object
     */
    companion object{
        /** Serialization Version ID: ver 2.1.4 (hexadecimal) */
        @Suppress("unused")
        @JvmStatic
        private val serialVersionUid: Long = 0x214L

        @JvmStatic
        private val UTF_8 = Charset.forName("UTF-8")

        @JvmStatic
        private fun deDupeFeatureArray(feature: List<String>): List<String> {
            return feature.map { CompressionHelper.getStrCached(it) }
        }

        /**
         * Scala 2.12의 WrappedArray가 포함되어 De-Serialization에 문제가 있던 TermDict를 다시 추출합니다.
         * 실행은 ./gradlew :koalanlp-eunjeon:run으로 간단히 수행되며,
         * 실행 결과로 저장되는 파일은 자동으로 필요한 resource path에 추가됩니다.
         * 배포되는 SEunjeon 패키지에는 이 파일이 포함되어 있으나, Github에는 업로드 크기 정책으로 인해 올라가지 않습니다.
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("src/main/resources/" + DictBuilder.TERM_DICT() + ".kt")
            if (!file.parentFile.exists())
                file.parentFile.mkdirs()

            if (!file.exists()) {
                val lexicon = LexiconDict().load(true)
                val termDict = lexicon.termDict()
                val objectOutputStream = ObjectOutputStream(File("./src/main/resources/" + DictBuilder.TERM_DICT() + ".koala")
                        .outputStream().buffered(16 * 1024))
                objectOutputStream.writeObject(termDict)
                objectOutputStream.close()
            }
        }
    }
}


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
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    internal val lexiconDict by lazy {
        // 아래의 loading 코드는 [LexiconDict.load] 함수 참고.
        synchronized(needCompress) {
            val lexicon = LexiconDict()

            val termDictIn = ObjectInputStream(this::class.java
                    .getResourceAsStream(DictBuilder.TERM_DICT() + ".koala")!!.buffered(16*1024))
            val compressedMorphemes = termDictIn.readObject() as Array<Morpheme>
            termDictIn.close()

            if (needCompress)
                lexicon.`termDict_$eq`(compressedMorphemes)
            else
                lexicon.`termDict_$eq`(compressedMorphemes.map{ BasicMorpheme.apply(it) as Morpheme }.toTypedArray())

            val dictMapperIn = ObjectInputStream(LexiconDict::class.java.classLoader
                    .getResourceAsStream(DictBuilder.DICT_MAPPER().drop(1))!!.buffered(16*1024))
            lexicon.`dictMapper_$eq`(dictMapperIn.readObject() as Array<out IntArray>)
            dictMapperIn.close()

            lexicon.`trie_$eq`(DoubleArrayTrie.apply(LexiconDict::class.java.classLoader
                    .getResourceAsStream(DictBuilder.TERM_TRIE().drop(1))))
            println("trie loading is completed.")

            lexicon
        }
    }

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
     * 은전한닢의 형태소 객체를 KoalaNLP의 [KMorpheme] 객체들로 변환합니다.
     *
     * @since 1.x
     * @param m: 은전한닢에서 생성한 형태소 객체
     * @return KoalaNLP의 [KMorpheme] 객체 목록. 만약 복합명사였을 경우 복합명사는 모두 분리됨.
     */
    @JvmStatic
    fun convertMorpheme(m: Morpheme): List<KMorpheme> {
        val array = m.feature.split(",")
        val compoundTag = array[0]
        val tokens = array.last()

        return if (array.size == 1 || tokens == "*") {
            listOf(KMorpheme(m.surface, compoundTag.toSejongPOS(), compoundTag))
        } else {
            tokens.split("+").map {
                val arr = it.split("/")
                KMorpheme(arr[0], arr[1].toSejongPOS(), arr[1])
            }
        }
    }
}
