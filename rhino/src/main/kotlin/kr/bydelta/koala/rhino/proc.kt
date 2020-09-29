@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.rhino

import kr.bydelta.koala.POS
import kr.bydelta.koala.data.Morpheme
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.data.Word
import kr.bydelta.koala.proc.CanExtractResource
import kr.bydelta.koala.proc.CanTagOnlyASentence
import rhino.FileAnalyzer
import rhino.RHINO
import rhino.datastructure.TST
import java.io.File


/**
 * RHINO Resource를 불러들이는 부분입니다.
 * 이 코드의 원본은 rhino.ExternInit 및 rhino.SetFileLists입니다.
 */
internal object RHINOTagger : CanExtractResource() {
    override val modelName: String = "rhino"

    @JvmStatic
    val MORPH_MATCH = "([^\\s]+)/\\s*([A-Z]{2,3})".toRegex()

    @JvmStatic
    internal val tagger by lazy {
        val fa = FileAnalyzer(RHINOTagger.getExtractedPath() + File.separator)

        val coM = fa.MakeMethodsList("rhino.lexicon.combi.combi")
        val ssM = fa.MakeMethodsList("rhino.lexicon.stem.stem")
        val eM = fa.MakeMethodsList("rhino.lexicon.ending.ending")
        val csN = fa.MakeFileAllContentsArray("complexStem_MethodDeleted.txt", 2)
        val ssN = fa.MakeFileAllContentsArray("stem_MethodDeleted.txt", 2)
        val eN = fa.MakeFileAllContentsArray("ending_MethodDeleted.txt", 3)
        val anN = fa.MakeFileAllContentsArray("afterNumber_MethodDeleted.txt", 2)
        val kN = fa.MakeFileAllContentsArray("koreanName.txt", 2)
        val alsoxr = fa.MakeFileAllContentsArray("alsoXR.txt", 2)

        // Initialize static variables in RHINO
        RHINO.combi = TST()
        RHINO.stem = TST()
        RHINO.stemR = TST()
        RHINO.stemM = TST()
        RHINO.endingR = TST()
        RHINO.endingM = TST()
        RHINO.afterNum = TST()
        RHINO.koreanName = TST()
        RHINO.alsoXR = TST()

        coM.forEach { RHINO.combi.insert(it, "coM") }
        ssM.forEach { RHINO.stem.insert(it, "ssM") }
        ssM.forEach { RHINO.stemR.insertR(it, "ssM", "N") }
        ssM.forEach { RHINO.stemM.insert(it, "ssM") }
        eM.forEach { RHINO.endingR.insertR(it, "eM", "N") }
        eM.forEach { RHINO.endingM.insertR(it, "eM", "N") }
        csN.forEach { RHINO.stem.insert(it[0], it[1]) }
        csN.forEach { RHINO.stemR.insertR(it[0], it[1], "N") }
        ssN.forEach { RHINO.stem.insert(it[0], it[1]) }
        ssN.forEach { RHINO.stemR.insertR(it[0], it[1], "N") }
        eN.forEach { RHINO.endingR.insertR(it[0], it[1], it[2]) }
        anN.forEach { RHINO.afterNum.insert(it[0], it[1]) }
        kN.forEach { RHINO.koreanName.insert(it[0], it[1]) }
        alsoxr.forEach { RHINO.stem.insert(it[0], it[1]) }
        alsoxr.forEach { RHINO.stemR.insertR(it[0], it[1], "N") }

        RHINO()
    }

    @JvmStatic
    internal fun tag(string: String): Sentence {
        val wordsWithoutSpecials = synchronized(tagger) {
            tagger.ExternCall(string).trim().split("\n").map { word ->
                val (surface, morphemesString) = word.trim().split("\t")
                val morphemes = translateOutputMorpheme(morphemesString)
                Word(surface, morphemes)
            }
        }

        // Rhino 특수문자 복원 전까지 수동 복원
        val words = mutableListOf<Word>()
        var remainingString = string
        var wordId = 0
        while (remainingString.isNotEmpty()) {
            if (remainingString.startsWith(wordsWithoutSpecials[wordId].surface)) {
                words.add(wordsWithoutSpecials[wordId])
                remainingString = remainingString.substring(wordsWithoutSpecials[wordId].surface.length)
                wordId += 1
            } else {
                val char = remainingString.substring(0, 1)
                words.add(Word(char, listOf(Morpheme(surface = char, tag = POS.SW, originalTag = ""))))
                remainingString = remainingString.substring(1)
            }
        }

        return Sentence(words.toList())
    }

    @JvmStatic
    private fun translateOutputMorpheme(morphStr: String) =
            MORPH_MATCH.findAll(morphStr).map {
                val surf = it.groupValues[1]
                val raw = it.groupValues[2]
                Morpheme(surf.trim(), POS.valueOf(raw), raw)
            }.toList()
}

/**
 * 라이노 형태소 분석기의 KoalaNLP Wrapper입니다.
 *
 * ## 참고
 * **형태소**는 의미를 가지는 요소로서는 더 이상 분석할 수 없는 가장 작은 말의 단위로 정의됩니다.
 *
 * **형태소 분석**은 문장을 형태소의 단위로 나누는 작업을 의미합니다.
 * 예) '문장을 형태소로 나눠봅시다'의 경우,
 * * 문장/일반명사, -을/조사,
 * * 형태소/일반명사, -로/조사,
 * * 나누-(다)/동사, -어-/어미, 보-(다)/동사, -ㅂ시다/어미
 * 로 대략 나눌 수 있습니다.
 *
 * 아래를 참고해보세요.
 * * [Morpheme] 형태소를 저장하는 클래스입니다.
 * * [POS] 형태소의 분류를 담은 Enum class
 *
 * ## 사용법 예제
 *
 * ### Kotlin
 * ```kotlin
 * val tagger = Tagger()
 * val sentence = tagger.tagSentence("문장 1개입니다.")
 * val sentences = tagger.tag("문장들입니다. 결과는 목록이 됩니다.")
 * // 또는
 * val sentences = tagger("문장들입니다. 결과는 목록이 됩니다.")
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/scala-support/)
 * ```scala
 * import kr.bydelta.koala.Implicits._
 * val tagger = new Tagger()
 * val sentence = tagger.tagSentence("문장 1개입니다.")
 * val sentences = tagger.tag("문장들입니다. 결과는 목록이 됩니다.")
 * // 또는
 * val sentences = tagger("문장들입니다. 결과는 목록이 됩니다.")
 * ```
 *
 * ### Java
 * ```java
 * Tagger tagger = new Tagger()
 * Sentence sentence = tagger.tagSentence("문장 1개입니다.")
 * List<Sentence> sentences = tagger.tag("문장들입니다. 결과는 목록이 됩니다.")
 * // 또는
 * List<Sentence> sentences = tagger.invoke("문장들입니다. 결과는 목록이 됩니다.")
 * ```
 *
 * @since 1.x
 */
class Tagger : CanTagOnlyASentence<List<Word>>() {
    /**
     * 변환되지않은, [text]의 분석결과 List<Word>를 반환합니다.
     *
     * @since 1.x
     * @param text 분석할 String.
     * @return 원본 분석기의 결과인 문장 1개
     */
    override fun tagSentenceOriginal(text: String): List<Word> {
        return RHINOTagger.tag(text)
    }

    /**
     * List<Word> 타입의 분석결과 [result]를 변환, [Sentence]를 구성합니다.
     *
     * @since 1.x
     * @param text 품사분석을 수행한 문단의 String입니다.
     * @param result 변환할 분석결과.
     * @return 변환된 [Sentence] 객체
     */
    override fun convertSentence(text: String, result: List<Word>): Sentence {
        return Sentence(result)
    }
}