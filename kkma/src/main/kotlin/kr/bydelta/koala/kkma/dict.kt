@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.kkma

import kr.bydelta.koala.POS
import kr.bydelta.koala.proc.CanCompileDict
import kr.bydelta.koala.proc.DicEntry
import org.snu.ids.kkma.dic.RawDicFileReader
import org.snu.ids.kkma.dic.SimpleDicFileReader
import org.snu.ids.kkma.dic.SimpleDicReader

/**
 * 꼬꼬마 사용자사전을 실시간으로 반영하기 위한 Reader class 입니다.
 */
internal class UserDicReader : SimpleDicReader, Iterator<String?> {
    /**
     * 형태소 리스트.
     */
    internal val morphemes = mutableListOf<String>()

    /**
     * 파일스트림 모사를 위한 현재위치 Marker.
     */
    private var iterator: Iterator<String>? = morphemes.iterator()

    /**
     * 사전에 (형태소,품사) 리스트를 추가.
     *
     * @param map 추가할 (형태소,품사)리스트.
     */
    operator fun plusAssign(map: List<Pair<String, String>>) {
        morphemes.addAll(map.map {
            "${it.first}/${it.second}"
        })
    }

    /**
     * 사전에 형태소-품사를 추가.
     *
     * @param morph 추가할 형태소.
     * @param tag   형태소의 품사.
     */
    operator fun plusAssign(pair: Pair<String, String>) {
        morphemes.add("${pair.first}/${pair.second}")
    }

    /**
     * 사전 초기화 함수
     */
    override fun cleanup() {}

    /**
     * Returns `true` if the iteration has more elements.
     */
    override fun hasNext(): Boolean = iterator?.hasNext() ?: false

    /**
     * Returns the next element in the iteration.
     */
    override fun next(): String? = readLine()

    /**
     * 사전 1행씩 읽기
     */
    override fun readLine(): String? {
        if (iterator == null)
            reset()

        return if (iterator?.hasNext() == false) {
            null
        } else {
            iterator?.next()
        }
    }

    /**
     * 위치 초기화. (반복읽기를 위함.)
     */
    fun reset() {
        iterator = morphemes.iterator()
    }
}


/**
 * 꼬꼬마 사용자사전을 제공하는 인터페이스입니다.
 *
 * @since 1.x
 */
object Dictionary : CanCompileDict {
    /** 원본사전의 어휘목록 **/
    private val systemDicByTag by lazy {
        org.snu.ids.kkma.dic.Dictionary.getInstance().asList.flatMap {
            it.filter { m -> m.tag != null }
                    .map { m -> m.tag.toSejongPOS() to m.exp } // (품사, 표현식)으로 변환.
        }.groupBy { it.first }.mapValues { entry -> entry.value.map { it.second to it.first } }
    }

    /** 사용자사전 Reader **/
    internal val userdic = UserDicReader()

    /** 사전 목록의 변화여부 **/
    internal var isDicChanged = false

    /**
     * 사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가합니다.
     *
     * @since 1.x
     * @param dict 추가할 (표면형, 품사)의 순서쌍들 (가변인자). 즉, [Pair]<[String], [POS]>들
     */
    override fun addUserDictionary(vararg dict: DicEntry) {
        if (dict.isNotEmpty()) {
            userdic += dict.mapNotNull {
                if (it.first.isNotEmpty()) it.first to it.second.fromSejongPOS()
                else null
            }
            isDicChanged = true
        }
    }

    /**
     * 사용자 사전에 등재된 모든 Item을 불러옵니다.
     *
     * @since 1.x
     * @return (형태소, 통합품사)의 Sequence.
     */
    override fun getItems(): Set<DicEntry> = synchronized(this) {
        userdic.reset()
        userdic.asSequence().mapNotNull {
            val segments = it?.split('/') ?: emptyList()
            if (segments.size < 2) null
            else segments[0] to segments[1].toSejongPOS()
        }.toSet()
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
        val converted = word.map {
            Triple(it.first, it, it.second.fromSejongPOS())
        }

        // Filter out existing morphemes!
        val (_, system) =
                if (onlySystemDic) Pair(emptyList(), converted)
                else converted.partition { userdic.morphemes.contains("${it.first}/${it.third}") }

        return system.groupBy { it.first }.flatMap { entry ->
            val (w, tags) = entry
            // Filter out existing morphemes!
            tags.filterNot { triple ->
                val mexp = org.snu.ids.kkma.dic.Dictionary.getInstance().getMExpression(w)
                mexp != null && triple.third in mexp.map { it.tag }
            }.map { it.second }
        }.toTypedArray()
    }

    /**
     * 원본 사전에 등재된 항목 중에서, 지정된 형태소의 항목만을 가져옵니다. (복합 품사 결합 형태는 제외)
     *
     * @since 1.x
     * @param filter 가져올 품사인지 판단하는 함수.
     * @return (형태소, 품사)의 Iterator.
     */
    override fun getBaseEntries(filter: (POS) -> Boolean): Iterator<DicEntry> {
        return systemDicByTag.filterKeys(filter).flatMap { it.value }.iterator()
    }

    /**
     * 사전 다시읽기.
     * @since 1.x
     */
    internal fun reloadDic() {
        synchronized(userdic) {
            if (isDicChanged) {
                userdic.reset()
                org.snu.ids.kkma.dic.Dictionary.reload(
                        listOf(
                                SimpleDicFileReader("/dic/00nng.dic"),
                                SimpleDicFileReader("/dic/01nnp.dic"),
                                SimpleDicFileReader("/dic/02nnb.dic"),
                                SimpleDicFileReader("/dic/03nr.dic"),
                                SimpleDicFileReader("/dic/04np.dic"),
                                SimpleDicFileReader("/dic/05comp.dic"),
                                SimpleDicFileReader("/dic/06slang.dic"),
                                SimpleDicFileReader("/dic/10verb.dic"),
                                SimpleDicFileReader("/dic/11vx.dic"),
                                SimpleDicFileReader("/dic/12xr.dic"),
                                SimpleDicFileReader("/dic/20md.dic"),
                                SimpleDicFileReader("/dic/21ma.dic"),
                                SimpleDicFileReader("/dic/30ic.dic"),
                                SimpleDicFileReader("/dic/40x.dic"),
                                RawDicFileReader("/dic/50josa.dic"),
                                RawDicFileReader("/dic/51eomi.dic"),
                                RawDicFileReader("/dic/52raw.dic"),
                                userdic
                        )
                )
                isDicChanged = false
            }
        }
    }
}
