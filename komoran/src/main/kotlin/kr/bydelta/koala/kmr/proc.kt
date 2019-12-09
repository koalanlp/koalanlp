@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.kmr

import kr.bydelta.koala.POS
import kr.bydelta.koala.correctVerbApply
import kr.bydelta.koala.data.Morpheme
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.data.Word
import kr.bydelta.koala.proc.CanTagOnlyASentence
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran
import kr.co.shineware.nlp.komoran.model.KomoranResult

/**
 * 코모란 형태소분석기의 KoalaNLP Wrapper입니다.
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
 * @constructor
 * @param useLightTagger KOMORAN Light Tagger를 사용할 것인지 확인합니다. 기본값 false. 사용시 true.
 */
class Tagger @JvmOverloads constructor(useLightTagger: Boolean = false) : CanTagOnlyASentence<KomoranResult>() {
    /**
     * 코모란 분석기 객체.
     * @since 1.x
     */
    val komoran: Komoran by lazy {
        val komoran =
                if (useLightTagger) taggerLight()
                else taggerFull()
        if (Dictionary.userDict.exists())
            komoran.setUserDic(Dictionary.userDict.absolutePath)
        komoran
    }

    /**
     * 변환되지않은, [text]의 분석결과 KomoranResult를 반환합니다.
     *
     * @since 1.x
     * @param text 분석할 String.
     * @return 원본 분석기의 결과인 문장 1개
     */
    override fun tagSentenceOriginal(text: String): KomoranResult = komoran.analyze(text)

    /**
     * KomoranResult 타입의 분석결과 [result]를 변환, [Sentence]를 구성합니다.
     *
     * @since 1.x
     * @param text 품사분석을 수행한 문단의 String입니다.
     * @param result 변환할 분석결과.
     * @return 변환된 [Sentence] 객체
     */
    override fun convertSentence(text: String, result: KomoranResult): Sentence =
            if (result.tokenList.isNotEmpty()) {
                val words = mutableListOf<Word>()
                var morphs = mutableListOf<Morpheme>()
                var lastIdx = 0

                for (token in result.tokenList) {
                    if (token.beginIndex > lastIdx) {
                        words.add(Word(constructWordSurface(morphs), morphs))
                        morphs = mutableListOf()
                    }

                    morphs.add(Morpheme(token.morph, token.pos.toSejongPOS(), token.pos))
                    lastIdx = token.endIndex
                }

                if (morphs.isNotEmpty()) {
                    words += Word(constructWordSurface(morphs), morphs)
                }

                Sentence(words)
            } else
                Sentence.empty

    /**
     * 코모란 분석기 처리 과정에서 잃어버린 단어 원형을 근사하여 복원합니다.
     * @since 1.x
     */
    private fun constructWordSurface(word: List<Morpheme>): String {
        var head = ""
        var wasVerb = false

        for (curr in word) {
            val tag = curr.originalTag?.toUpperCase()
            if (curr.hasOriginalTag("E") && head.isNotEmpty()) {
                head = correctVerbApply(head, wasVerb, curr.surface)
                wasVerb = false
            } else {
                wasVerb = (curr.hasOriginalTag("V") && tag != "VA") || tag == "XSV"
                head += curr.surface
            }
        }

        return head
    }

    /**
     * 코모란 분석기의 Companion object.
     * @since 1.x
     */
    internal companion object {
        /**
         * 코모란 분석기 객체.
         * @since 1.x
         */
        @JvmStatic
        private fun taggerFull(): Komoran {
            Dictionary.extractResource()
            return Komoran(DEFAULT_MODEL.FULL)
        }

        /**
         * 코모란 분석기 객체.
         * @since 1.x
         */
        @JvmStatic
        private fun taggerLight(): Komoran {
            Dictionary.extractResource()
            return Komoran(DEFAULT_MODEL.LIGHT)
        }
    }
}