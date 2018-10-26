@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.okt

import kr.bydelta.koala.POS
import kr.bydelta.koala.proc.CanCompileDict
import kr.bydelta.koala.proc.DicEntry
import org.openkoreantext.processor.util.CharArraySet
import org.openkoreantext.processor.util.KoreanDictionaryProvider
import org.openkoreantext.processor.util.KoreanPos
import scala.Enumeration


/**
 * 트위터 분석기 사용자사전 인터페이스를 제공합니다.
 *
 * @since 1.x
 */
object Dictionary : CanCompileDict {
    private val userDict = mutableSetOf<DicEntry>()

    /**
     * 사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가합니다.
     *
     * @since 1.x
     * @param dict 추가할 (표면형, 품사)의 순서쌍들 (가변인자). 즉, [Pair]<[String], [POS]>들
     */
    override fun addUserDictionary(vararg dict: DicEntry) {
        userDict.addAll(dict)
        dict.groupBy { KoreanPos.withName(it.second.fromSejongPOS()) }.forEach { twtTag, seq ->
            add(twtTag, seq.map { it.first })
        }
    }

    /**
     * 사용자 사전에 형태소를 실제로 더합니다.
     *
     * @since 1.x
     * @param tag   POS Tag
     * @param morph Morpheme sequence.
     */
    private fun add(tag: Enumeration.Value, morph: List<String>) {
        when (tag) {
            KoreanPos.ProperNoun() ->
                morph.forEach { KoreanDictionaryProvider.properNouns().add(it) }
            KoreanPos.Verb(), KoreanPos.Adjective() -> {
                // * Verb/Adjective dictionary cannot be modified in OKT.
                // KoreanDictionaryProvider.predicateStems.get(tag)
            }
            else ->
                if (dictContainsKey(tag)) KoreanDictionaryProvider.addWordsToDictionary(tag, morph.asScala().toSeq())

            // Otherwise,
            // * No proper dictionary.
            // KoreanDictionaryProvider.addWordsToDictionary(KoreanPos.Noun, morph)
        }
    }

    private fun dictContainsKey(tag: Enumeration.Value): Boolean =
            KoreanDictionaryProvider.koreanDictionary().containsKey(tag)

    /**
     * 사용자 사전에 등재된 모든 Item을 불러옵니다.
     *
     * @since 1.x
     * @return (형태소, 통합품사)의 Sequence.
     */
    override fun getItems(): Set<DicEntry> = userDict

    /**
     * 사전에 등재되어 있는지 확인하고, 사전에 없는단어만 반환합니다.
     *
     * @since 1.x
     * @param onlySystemDic 시스템 사전에서만 검색할지 결정합니다.
     * @param word          확인할 (형태소, 품사)들.
     * @return 사전에 없는 단어들, 즉, [Pair]<[String], [POS]>들.
     */
    override fun getNotExists(onlySystemDic: Boolean, vararg word: DicEntry): Array<DicEntry> =
            word.groupBy { w -> KoreanPos.withName(w.second.fromSejongPOS()) }.flatMap {
                val (tag, words) = it

                val tagDic =
                        if (tag == KoreanPos.ProperNoun()) KoreanDictionaryProvider.properNouns()
                        else dictGet(tag)

                // Filter out existing morphemes!
                if (tagDic != null)
                    words.filterNot { w -> tagDic.contains(w.first) }
                else
                    words // for the case of not found!
            }.toTypedArray()

    /**
     * 품사 [tag]에 맞는 시스템 사전을 가져옵니다.
     *
     * @since 1.x
     * @param tag 사전을 조회할 품사; [POS] Enum 값
     */
    private fun dictGet(tag: Enumeration.Value): CharArraySet? = KoreanDictionaryProvider.koreanDictionary()[tag]

    /**
     * 원본 사전에 등재된 항목 중에서, 지정된 형태소의 항목만을 가져옵니다. (복합 품사 결합 형태는 제외)
     *
     * @since 1.x
     * @param filter 가져올 품사인지 판단하는 함수.
     * @return (형태소, 품사)의 Iterator.
     */
    override fun getBaseEntries(filter: (POS) -> Boolean): Iterator<DicEntry> =
            KoreanPos.values().asJava().filter { filter(it.toString().toSejongPOS()) }.flatMap {
                val key = it.toString().toSejongPOS()

                when (it) {
                    KoreanPos.ProperNoun() -> {
                        KoreanDictionaryProvider.properNouns().map { x ->
                            when (x) {
                                is String -> x to key
                                is CharArray -> String(x) to key
                                else -> x.toString() to key
                            }
                        }
                    }

                    KoreanPos.Verb(), KoreanPos.Adjective() -> {
                        KoreanDictionaryProvider.predicateStems().apply(it).keys().asJava().map { m -> m to key }
                    }

                    else -> {
                        dictGet(it).orEmpty().map { x ->
                            when (x) {
                                is String -> x to key
                                is CharArray -> String(x) to key
                                else -> x.toString() to key
                            }
                        }
                    }
                }
            }.iterator()
}
