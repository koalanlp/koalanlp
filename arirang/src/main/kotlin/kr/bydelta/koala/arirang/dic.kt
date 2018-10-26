@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.arirang

import kr.bydelta.koala.POS
import kr.bydelta.koala.proc.CanCompileDict
import kr.bydelta.koala.proc.DicEntry
import org.apache.lucene.analysis.ko.morph.WordEntry
import org.apache.lucene.analysis.ko.utils.DictionaryUtil
import org.apache.lucene.analysis.ko.utils.FileUtil
import org.apache.lucene.analysis.ko.utils.KoreanEnv

/**
 * Arirang 분석기의 사전 인터페이스입니다.
 *
 * @since 1.x
 * @see CanCompileDict
 */
object Dictionary : CanCompileDict {
    internal val userItems = mutableListOf<DicEntry>()
    internal val systemdic = mutableMapOf<POS, Set<String>>()

    /**
     * 사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가합니다.
     *
     * @since 1.x
     * @param dict 추가할 (표면형, 품사)의 순서쌍들 (가변인자). 즉, [Pair]<[String], [POS]>들
     */

    override fun addUserDictionary(vararg dict: DicEntry) {
        userItems.addAll(dict)
        dict.forEach {
            val (word, pos) = it

            val features = //NVZDBIPSCC 순서로 코딩됨
                    if (pos.isNoun()) "100000000X"
                    else if (pos.isPredicate()) "010000000X"
                    else if (pos.isModifier() || pos == POS.IC) "001000000X"
                    else "000000000X"
            DictionaryUtil.addEntry(WordEntry(word, features.toCharArray()))
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
        return systemdic.filterKeys(filter).flatMap { entry ->
            val (key, item) = entry
            item.map { it to key }
        }.listIterator()
    }

    /**
     * 사용자 사전에 등재된 모든 Item을 불러옵니다.
     *
     * @since 1.x
     * @return (형태소, 통합품사)의 Sequence.
     */
    override fun getItems(): Set<DicEntry> = userItems.toSet()

    /**
     * 사전에 등재되어 있는지 확인하고, 사전에 없는단어만 반환합니다.
     *
     * @since 1.x
     * @param onlySystemDic 시스템 사전에서만 검색할지 결정합니다.
     * @param word          확인할 (형태소, 품사)들.
     * @return 사전에 없는 단어들, 즉, [Pair]<[String], [POS]>들.
     */
    override fun getNotExists(onlySystemDic: Boolean, vararg word: DicEntry): Array<DicEntry> {
        if (systemdic.isEmpty()) load()

        // Filter out existing morphemes!
        val (_, system) =
                if (onlySystemDic) Pair(emptyList(), word.toList())
                else word.partition { getItems().contains(it) }

        return system.groupBy { it.second }.flatMap {
            val (pos, words) = it

            val dict = systemdic[mapToTag(pos)] ?: emptySet()
            words.filterNot { it.first in dict }
        }.toTypedArray()
    }

    /** 사전 불러오기 **/
    private fun load(): Unit {
        synchronized(this) {
            readAuxDict("josa.dic", POS.JX)
            readAuxDict("eomi.dic", POS.EP)
            readAuxDict("prefix.dic", POS.XPN)
            readAuxDict("suffix.dic", POS.XSN)
            readDict()
        }
    }

    /** 보조 사전 불러오기 */
    private fun readAuxDict(dic: String, tag: POS) {
        val path = KoreanEnv.getInstance().getValue(dic)

        try {
            systemdic[tag] = FileUtil.readLines(path, "UTF-8").map { it.trim() }.toSet()
        } catch (_: Throwable) {
        }
    }

    /** 사전 읽기 */
    private fun readDict() {
        try {
            val list = FileUtil.readLines(KoreanEnv.getInstance().getValue("dictionary.dic"), "UTF-8")
            list.addAll(FileUtil.readLines(KoreanEnv.getInstance().getValue("extension.dic"), "UTF-8"))

            list.flatMap { item ->
                val segs = item.split("[,]+".toRegex())
                if (segs.size == 2) {
                    val buffer = mutableListOf<DicEntry>()

                    val word = segs[0].trim()

                    if (segs.last()[0] != '0') buffer.add(word to POS.NNG)
                    if (segs.last()[1] != '0') buffer.add(word to POS.VV)
                    if (segs.last()[2] != '0') buffer.add(word to POS.MAG)
                    if (segs.last().take(3) == "000") buffer.add(word to POS.NA)

                    buffer.toList()
                } else emptyList()
            }.groupBy { it.second }.forEach {
                systemdic[it.key] = it.value.map { v -> v.first }.toSet()
            }
        } catch (_: Throwable) {
        }
    }

    /** 아리랑 태그 변환 */
    private fun mapToTag(tag: POS): POS =
            when {
                tag.isPostPosition() -> POS.JX
                tag == POS.XPN || tag == POS.XPV -> POS.XPN
                tag.isSuffix() -> POS.XSN
                tag.isEnding() -> POS.EP
                tag.isNoun() -> POS.NNG
                tag.isPredicate() -> POS.VV
                tag == POS.IC || tag.isModifier() -> POS.MAG
                else -> POS.NA
            }
}
