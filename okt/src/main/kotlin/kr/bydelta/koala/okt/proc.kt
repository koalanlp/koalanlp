@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.okt

import kr.bydelta.koala.POS
import kr.bydelta.koala.data.Morpheme
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.data.Word
import kr.bydelta.koala.proc.CanSplitSentence
import kr.bydelta.koala.proc.CanTagAParagraph
import org.openkoreantext.processor.OpenKoreanTextProcessor
import org.openkoreantext.processor.tokenizer.KoreanTokenizer


/**
 * 트위터 품사분석기 인터페이스를 제공합니다.
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
class Tagger : CanTagAParagraph<List<KoreanTokenizer.KoreanToken>>() {
    /**
     * 변환되지않은, [text]의 분석결과 [List]<List<KoreanToken>>를 반환합니다
     *
     * @since 1.x
     * @param text 분석할 String.
     * @return 원본 분석기의 결과인 문장의 목록
     */
    override fun tagParagraphOriginal(text: String): List<List<KoreanTokenizer.KoreanToken>> =
            OpenKoreanTextProcessor.splitSentences(text).asJava().map { tagSentenceOriginal(it.text()) }

    /**
     * 변환되지않은, [text]의 분석결과 List<KoreanToken>를 돌려줍니다.
     *
     * @since 1.x
     * @param text 분석할 String.
     * @return 원본 분석기의 분석 결과 (문장 1개)
     */
    override fun tagSentenceOriginal(text: String): List<KoreanTokenizer.KoreanToken> =
            OpenKoreanTextProcessor.tokenize(
                    OpenKoreanTextProcessor.normalize(text)
            ).asJava()

    /**
     * List<KoreanToken> 타입의 분석결과 [result]를 변환, [Sentence]를 구성합니다.
     *
     * @since 1.x
     * @param result 변환할 분석결과.
     * @return 변환된 [Sentence] 객체
     */
    override fun convertSentence(result: List<KoreanTokenizer.KoreanToken>): Sentence {
        val words = mutableListOf<Word>()
        var morphemes = mutableListOf<Morpheme>()

        for (token in result) {
            if (token.text().matches("(?U)^\\s+$".toRegex())) {
                if (morphemes.isNotEmpty()) {
                    words.add(Word(morphemes.joinToString("") { it.surface }, morphemes))
                    morphemes = mutableListOf()
                }
            } else {
                val pos = token.pos().toString()
                morphemes.add(Morpheme(token.text(), pos.toSejongPOS(), pos))
            }
        }

        if (morphemes.isNotEmpty())
            words.add(Word(morphemes.joinToString("") { it.surface }, morphemes))

        return Sentence(words)
    }
}

/**
 * 트위터 문장분리기 인터페이스를 제공합니다.
 *
 * 텍스트를 받아서 텍스트 문장들로 분리합니다.
 *
 * ## 사용법 예제
 * ### Kotlin
 * ```kotlin
 * val splitter = SentenceSplitter()
 * val sentence = "분석할 문장을 적었습니다."
 * val split = splitter.sentences(sentence)
 * // 또는
 * val split = splitter(sentence)
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/scala-support/)
 * ```scala
 * import kr.bydelta.koala.Implicits._
 * val splitter = new SentenceSplitter()
 * val sentence = "분석할 문장을 적었습니다."
 * val split = splitter.sentences(sentence)
 * // 또는
 * val split = splitter(sentence)
 * ```
 *
 * ### Java
 * ```java
 * SentenceSplitter splitter = new SentenceSplitter();
 * String sentence = "분석할 문장을 적었습니다.";
 * List<String> split = splitter.sentences(sentence);
 * // 또는
 * List<String> split = splitter.invoke(sentence);
 * ```
 *
 */
class SentenceSplitter : CanSplitSentence {
    /**
     * 주어진 문단 [text]를 문장단위로 분리합니다.
     *
     * @since 1.x
     * @param text 문장단위로 분리할 String.
     * @return 문장단위로 분리된 String의 [List].
     */
    override fun sentences(text: String): List<String> =
            OpenKoreanTextProcessor.splitSentences(text).asJava().map { it.text() }
}


