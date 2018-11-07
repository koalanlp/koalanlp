@file:JvmName("ProcUtil")
package kr.bydelta.koala.proc

import kr.bydelta.koala.*
import kr.bydelta.koala.data.*
import java.util.*

/**
 * 문장분리기 Interface
 *
 * 텍스트를 받아서 텍스트 문장들로 분리합니다.
 *
 * ## 사용법 예제
 * 문장분리기 `SentenceSplitter`가 `CanSplitSentence`를 상속받았다면,
 *
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
 * @since 1.x
 */
interface CanSplitSentence {
    /**
     * 주어진 문단 [text]를 문장단위로 분리합니다.
     *
     * @since 1.x
     * @param text 문장단위로 분리할 String.
     * @return 문장단위로 분리된 String의 [List].
     */
    fun sentences(text: String): List<String>

    /**
     * 주어진 문단 [text]를 문장단위로 분리합니다.
     *
     * @since 2.0.0
     * @param text 문장단위로 분리할 String.
     * @return 문장단위로 분리된 String의 [List].
     */
    operator fun invoke(text: String): List<String> = sentences(text)
}

/**
 * 세종 태그셋에 기반한 Heuristic 문장분리기
 *
 * 다음 조건에 따라 문장을 분리합니다:
 * 1. 열린 괄호나 인용부호가 없고,
 * 2. 숫자나 외국어로 둘러싸이지 않은 문장부호([POS.SF])가 어절의 마지막에 왔을 경우.
 *
 * ## 사용법 예제
 * `SentenceSplitter`는 이미 singleton object이므로 초기화가 필요하지 않습니다.
 *
 * ### Kotlin
 * ```kotlin
 * val sentence = ... //Tagged result
 * val splitt = SentenceSplitter.sentences(sentence)
 * // 또는
 * val splitt = SentenceSplitter(sentence)
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/scala-support/)
 * ```scala
 * import kr.bydelta.koala.Implicits._
 * val sentence = ... //Tagged result
 * val splitt = SentenceSplitter.sentences(sentence)
 * // 또는
 * val splitt = SentenceSplitter(sentence)
 * ```
 *
 * ### Java
 * ```java
 * Sentence sentence = ... //Tagged result
 * List<Sentence> splitt = SentenceSplitter.sentences(sentence);
 * // 또는
 * List<Sentence> splitt = SentenceSplitter.invoke(sentence);
 * ```
 *
 * @since 1.x
 */
object SentenceSplitter {
    private const val quoteRegex = "\'\"＇＂"
    private const val openParenRegex = "([{‘“（［｛<〔〈《「『【"
    private const val closeParenRegex = ")]}’”）］｝>〕〉》」』】"

    /**
     * 분석결과를 토대로 문장을 분리함.
     *
     * @since 2.0.0
     * @param para 분리할 문단.
     * @return 문장단위로 분리된 결과
     */
    operator fun invoke(para: Iterable<Word>): List<Sentence> {
        val sentences = mutableListOf<Sentence>()
        val parenStack = Stack<Char>()
        val it = para.filter { it.isNotEmpty() }.toList()
        var begin = 0

        for (pos in 0 until it.size) {
            val word = it[pos]

            for (ch in word.surface) {
                if (ch in openParenRegex) {
                    parenStack.push(ch)
                } else if (ch in closeParenRegex) {
                    if (parenStack.isNotEmpty() &&
                            closeParenRegex.indexOf(ch) == openParenRegex.indexOf(parenStack.peek()))
                        parenStack.pop()
                } else if (ch in quoteRegex) {
                    if (parenStack.isNotEmpty() && parenStack.peek() == ch)
                        parenStack.pop()
                    else
                        parenStack.push(ch)
                }
            }

            if (word.last().tag == POS.SF) {
                val prevSLN =
                        if (word.size == 1) {
                            if (pos > 0) it[pos - 1].last().hasTagOneOf("SL", "SN") else false
                        } else {
                            word[word.size - 2].hasTagOneOf("SL", "SN")
                        }
                val nextSLN =
                        if (pos + 1 < it.size) {
                            it[pos + 1].first().hasTagOneOf("SL", "SN")
                        } else false

                if (!(prevSLN && nextSLN) && parenStack.isEmpty()) {
                    //This is the end of the sentence!
                    sentences.add(Sentence(it.subList(begin, pos + 1)))
                    begin = pos + 1
                }
            }
        }

        if (begin < it.size) {
            sentences.add(Sentence(it.subList(begin, it.size)))
        }

        return sentences.filter { it.isNotEmpty() }
    }

    /**
     * 분석결과를 토대로 문장을 분리함.
     *
     * @since 2.0.0
     * @param para 분리할 문단.
     * @return 문장단위로 분리된 결과
     */
    fun sentences(para: Iterable<Word>) = invoke(para)
}

/**
 * 품사분석기의 최상위 인터페이스입니다.
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
 * 분석기 `Tagger`가 `CanTag`를 상속받았다면,
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
interface CanTag {
    /**
     * 주어진 문장 [text]을 분석하여 품사를 부착하고, 결과로 [Sentence] 객체를 돌려줍니다.
     *
     * @since 1.x
     * @param text 분석할 문장입니다.
     * @return 분석된 결과로 [Sentence] 객체 하나입니다.
     */
    fun tagSentence(text: String): Sentence

    /**
     * 주어진 문단 [text]을 분석하여 품사를 부착하고, 결과로 [List]<[Sentence]> 객체를 돌려줍니다.
     *
     * @since 1.x
     * @param text 분석할 문장입니다.
     * @return 분석된 결과로 [Sentence] 객체들의 목록입니다.
     */
    fun tag(text: String): List<Sentence>

    /**
     * 주어진 문단 [text]을 분석하여 품사를 부착하고, 결과로 [List]<[Sentence]> 객체를 돌려줍니다.
     *
     * @since 2.0.0
     * @param text 분석할 문장입니다.
     * @return 분석된 결과로 [Sentence] 객체들의 목록입니다.
     */
    operator fun invoke(text: String) = tag(text)
}

/**
 * 문장 1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S] 타입으로 돌려줍니다.
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
 * 분석기 `Tagger`가 `CanTagASentence`를 상속받았다면,
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
abstract class CanTagASentence<S> : CanTag {
    final override fun tagSentence(text: String): Sentence {
        val trim = text.trim()
        return if (trim.isNotEmpty()) convertSentence(tagSentenceOriginal(trim))
        else Sentence.empty
    }

    /**
     * 변환되지않은, [text]의 분석결과 [S]를 돌려줍니다.
     *
     * @since 1.x
     * @param text 분석할 String.
     * @return 원본 분석기의 분석 결과 (문장 1개)
     */
    abstract fun tagSentenceOriginal(text: String): S

    /**
     * [S] 타입의 분석결과 [result]를 변환, [Sentence]를 구성합니다.
     *
     * @since 1.x
     * @param result 변환할 분석결과.
     * @return 변환된 [Sentence] 객체
     */
    protected abstract fun convertSentence(result: S): Sentence
}

/**
 * 문단1개, 문장1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S] 타입으로 돌려줍니다.
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
 * 분석기 `Tagger`가 `CanTagAParagraph`를 상속받았다면,
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
abstract class CanTagAParagraph<S> : CanTagASentence<S>() {
    final override fun tag(text: String): List<Sentence> {
        val trim = text.trim()
        return if (trim.isNotEmpty()) tagParagraphOriginal(trim).map { convertSentence(it) }
        else emptyList()
    }

    /**
     * 변환되지않은, [text]의 분석결과 [List]<[S]>를 반환합니다
     *
     * @since 1.x
     * @param text 분석할 String.
     * @return 원본 분석기의 결과인 문장의 목록
     */
    abstract fun tagParagraphOriginal(text: String): List<S>
}

/**
 * 문단1개는 불가하지만, 문장1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S] 타입으로 돌려줍니다.
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
 * 분석기 `Tagger`가 `CanTagOnlyASentence`를 상속받았다면,
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
abstract class CanTagOnlyASentence<S> : CanTag {
    final override fun tagSentence(text: String): Sentence {
        val trim = text.trim()
        return if (trim.isNotEmpty()) convertSentence(trim, tagSentenceOriginal(trim))
        else Sentence.empty
    }

    final override fun tag(text: String): List<Sentence> {
        val trim = text.trim()
        return if (trim.isNotEmpty()) SentenceSplitter(convertSentence(trim, tagSentenceOriginal(trim)))
        else emptyList()
    }

    /**
     * 변환되지않은, [text]의 분석결과 [S]를 반환합니다.
     *
     * @since 1.x
     * @param text 분석할 String.
     * @return 원본 분석기의 결과인 문장 1개
     */
    abstract fun tagSentenceOriginal(text: String): S

    /**
     * [S] 타입의 분석결과 [result]를 변환, [Sentence]를 구성합니다.
     *
     * @since 1.x
     * @param text 품사분석을 수행한 문단의 String입니다.
     * @param result 변환할 분석결과.
     * @return 변환된 [Sentence] 객체
     */
    protected abstract fun convertSentence(text: String, result: S): Sentence
}

/**
 * 문장1개는 불가하지만, 문단1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S] 타입으로 돌려줍니다.
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
 * 분석기 `Tagger`가 `CanTagOnlyAParagraph`를 상속받았다면,
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
abstract class CanTagOnlyAParagraph<S> : CanTag {
    final override fun tagSentence(text: String): Sentence {
        val trim = text.trim()
        return if (trim.isNotEmpty()) Sentence(tag(trim).flatten())
        else Sentence.empty
    }

    final override fun tag(text: String): List<Sentence> {
        val trim = text.trim()
        return if (trim.isNotEmpty()) tagParagraphOriginal(trim).map { convertSentence(it) }
        else emptyList()
    }

    /**
     * 변환되지않은, [text]의 분석결과 [List]<[S]>를 반환합니다.
     *
     * @since 1.x
     * @param text 분석할 String.
     * @return 원본 분석기의 결과인 문장의 목록입니다.
     */
    abstract fun tagParagraphOriginal(text: String): List<S>

    /**
     * [S] 타입의 분석결과 [result]를 변환, [Sentence]를 구성합니다.
     *
     * @since 1.x
     * @param result 변환할 분석결과.
     * @return 변환된 [Sentence] 객체
     */
    protected abstract fun convertSentence(result: S): Sentence
}

/**
 * [Sentence] 객체에 property를 추가할 수 있는 interface입니다.
 * [INTERMEDIATE]는 각 분석기에서 문장을 분석한 결과물의 중간 형태, 즉 분석기의 입력 형태입니다.
 *
 * 다음 분석의 기본 틀로 사용됩니다.
 * - 구문구조 분석 [CanParseSyntax]
 * - 의존구문구조 분석 [CanParseDependency]
 * - 의미역 분석 [CanLabelSemanticRole]
 * - 개체명 인식 [CanRecognizeEntity]
 * - 다의어/동형이의어 분별 [CanDisambiguateSense]
 *
 * @since 2.0.0
 */
interface CanAnalyzeProperty<INTERMEDIATE> {
    /**
     * [item]을 분석하여 property 값을 반환합니다.
     *
     * @since 2.0.0
     * @param item 분석 단위 1개입니다.
     * @param sentence 원본 문장입니다.
     * @return 분석의 결과물입니다.
     */
    fun attachProperty(item: INTERMEDIATE, sentence: String): Sentence

    /**
     * String [sentence]를 품사 분석하여 분석기가 받아들이는 [List]<Pair<[INTERMEDIATE], String>>으로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 텍스트에서 변환할 문장입니다.
     * @return 분석기가 받아들일 수 있는 형태의 데이터입니다. 각 Pair의 [Pair.first] 값은 변환된 문장 객체이며, [Pair.second] 값은 해당 문장의 String 값입니다.
     */
    fun convert(sentence: String): List<Pair<INTERMEDIATE, String>>

    /**
     * Sentence [sentence]를 해체하여 분석기가 받아들이는 [INTERMEDIATE]로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 변환할 문장입니다.
     * @return 분석기가 받아들일 수 있는 형태의 데이터입니다.
     */
    fun convert(sentence: Sentence): INTERMEDIATE

    /**
     * 분석기의 중간 결과인 [sentence]를 조합하여 [Sentence] 객체로 변환합니다.
     *
     * @since 2.0.0
     * @param sentence 변환할 문장입니다.
     * @return [Sentence] 객체입니다.
     */
    fun convert(sentence: INTERMEDIATE): Sentence

    /**
     * String [sentence]를 분석함. 결과는 각 [Sentence]의 property로 저장합니다.
     *
     * @since 2.0.0
     * @param sentence 텍스트에서 변환할 문장입니다.
     * @return 결과가 부착된 문장입니다.
     */
    fun analyze(sentence: String): List<Sentence> {
        val paragraph = convert(sentence)
        return paragraph.map { attachProperty(it.first, it.second) }
    }

    /**
     * [sentence]를 분석함. 결과는 각 [Sentence]의 property로 저장됨.
     *
     * @since 2.0.0
     * @param sentence 분석 결과를 부착할 문장입니다.
     * @return 결과가 부착된 문장입니다.
     */
    fun analyze(sentence: Sentence): Sentence {
        return attachProperty(convert(sentence), sentence.surfaceString())
    }

    /**
     * [sentences]를 분석함. 결과는 각 [Sentence]의 property로 저장됨.
     *
     * @since 2.0.0
     * @param sentences 분석 결과를 부착할 문장들의 목록입니다.
     * @return 결과가 부착된 문장들의 목록입니다.
     */
    fun analyze(sentences: List<Sentence>): List<Sentence> = sentences.map { analyze(it) }

    /**
     * [sentence]를 분석함. 결과는 각 [Sentence]의 property로 저장됨.
     *
     * @since 2.0.0
     * @param sentence 텍스트에서 변환할 문장입니다.
     * @return 결과가 부착된 문장입니다.
     */
    operator fun invoke(sentence: String) = analyze(sentence)

    /**
     * [sentence]를 분석함. 결과는 각 [Sentence]의 property로 저장됨.
     *
     * @since 2.0.0
     * @param sentence 분석 결과를 부착할 문장입니다.
     * @return 결과가 부착된 문장입니다.
     */
    operator fun invoke(sentence: Sentence) = analyze(sentence)

    /**
     * [sentences]를 분석함. 결과는 각 [Sentence]의 property로 저장됨.
     *
     * @since 2.0.0
     * @param sentences 분석 결과를 부착할 문장들의 목록입니다.
     * @return 결과가 부착된 문장들의 목록입니다.
     */
    operator fun invoke(sentences: List<Sentence>) = analyze(sentences)
}


/**
 * 구문분석을 수행하는 Interface입니다.
 * 매개변수 [T]는 각 분석기에서 문장을 분석한 결과물의 중간 형태, 즉 분석기의 입력 형태입니다.
 *
 * ## 참고
 * **구문구조 분석**은 문장의 구성요소들(어절, 구, 절)이 이루는 문법적 구조를 분석하는 방법입니다.
 * 예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
 * 2개의 절이 있습니다
 * * 나는 밥을 먹었고
 * * 영희는 짐을 쌌다
 * 각 절은 3개의 구를 포함합니다
 * * 나는, 밥을, 영희는, 짐을: 체언구
 * * 먹었고, 쌌다: 용언구
 *
 * 아래를 참고해보세요.
 * * [Word.getPhrase] 어절이 직접 속하는 가장 작은 구구조 [SyntaxTree]를 가져오는 API
 * * [Sentence.getSyntaxTree] 전체 문장을 분석한 [SyntaxTree]를 가져오는 API
 * * [SyntaxTree] 구구조를 저장하는 형태
 * * [PhraseTag] 구구조의 형태 분류를 갖는 Enum 값
 *
 * ## 사용법 예제
 * 분석기 `Parser`가 `CanParseSyntax`를 상속받았다면,
 *
 * ### Kotlin
 * ```kotlin
 * // 문장에서 바로 분석할 때
 * val parser = Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: List<Sentence> = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/scala-support/)
 * ```scala
 * import kr.bydelta.koala.Implicits._
 * // 문장에서 바로 분석할 때
 * val parser = new Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: java.util.List[Sentence] = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Java
 * ```java
 * // 문장에서 바로 분석할 때
 * Parser parser = Parser()
 * List<Sentence> sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser.invoke("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * Sentence taggedSentence = ...
 * Sentence sentence = parser.analyze(taggedSentence) // 또는 parser.invoke(taggedSentence)
 *
 * List<Sentence> taggedSentList = ...
 * List<Sentence> sentences = parser.analyze(taggedSentList) // 또는 parser.invoke(taggedSentList)
 * ```
 *
 * @since 2.0.0
 */
interface CanParseSyntax<T> : CanAnalyzeProperty<T>

/**
 * 의존구문분석을 수행하는 Interface입니다.
 * 매개변수 [T]는 각 분석기에서 문장을 분석한 결과물의 중간 형태, 즉 분석기의 입력 형태입니다.
 *
 * ## 참고
 * **의존구조 분석**은 문장의 구성 어절들이 의존 또는 기능하는 관계를 분석하는 방법입니다.
 * 예) '나는 밥을 먹었고, 영희는 짐을 쌌다'라는 문장에는
 * 가장 마지막 단어인 '쌌다'가 핵심 어구가 되며,
 * * '먹었고'가 '쌌다'와 대등하게 연결되고
 * * '나는'은 '먹었고'의 주어로 기능하며
 * * '밥을'은 '먹었고'의 목적어로 기능합니다.
 * * '영희는'은 '쌌다'의 주어로 기능하고,
 * * '짐을'은 '쌌다'의 목적어로 기능합니다.
 *
 * 아래를 참고해보세요.
 * * [Word.getDependentEdges] 어절이 직접 지배하는 하위 의존구조 [DepEdge]의 목록을 가져오는 API
 * * [Word.getGovernorEdge] 어절이 지배당하는 상위 의존구조 [DepEdge]를 가져오는 API
 * * [Sentence.getDependencies] 전체 문장을 분석한 의존구조 [DepEdge]의 목록을 가져오는 API
 * * [DepEdge] 의존구조를 저장하는 형태
 * * [PhraseTag] 의존구조의 형태 분류를 갖는 Enum 값 (구구조 분류와 같음)
 * * [DependencyTag] 의존구조의 기능 분류를 갖는 Enum 값
 *
 * ## 사용법 예제
 * 분석기 `Parser`가 `CanParseDependency`를 상속받았다면,
 *
 * ### Kotlin
 * ```kotlin
 * // 문장에서 바로 분석할 때
 * val parser = Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: List<Sentence> = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/scala-support/)
 * ```scala
 * import kr.bydelta.koala.Implicits._
 * // 문장에서 바로 분석할 때
 * val parser = new Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: java.util.List[Sentence] = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Java
 * ```java
 * // 문장에서 바로 분석할 때
 * Parser parser = Parser()
 * List<Sentence> sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser.invoke("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * Sentence taggedSentence = ...
 * Sentence sentence = parser.analyze(taggedSentence) // 또는 parser.invoke(taggedSentence)
 *
 * List<Sentence> taggedSentList = ...
 * List<Sentence> sentences = parser.analyze(taggedSentList) // 또는 parser.invoke(taggedSentList)
 * ```
 *
 * @since 2.0.0
 */
interface CanParseDependency<T> : CanAnalyzeProperty<T>

/**
 * 의미역 분석(Semantic Role Labeling)을 수행하는 Interface입니다.
 * 매개변수 [T]는 각 분석기에서 문장을 분석한 결과물의 중간 형태, 즉 분석기의 입력 형태입니다.
 *
 * ## 참고
 * **의미역 결정**은 문장의 구성 어절들의 역할/기능을 분석하는 방법입니다.
 * 예) '나는 밥을 어제 집에서 먹었다'라는 문장에는
 * 동사 '먹었다'를 중심으로
 * * '나는'은 동작의 주체를,
 * * '밥을'은 동작의 대상을,
 * * '어제'는 동작의 시점을
 * * '집에서'는 동작의 장소를 나타냅니다.
 *
 * 아래를 참고해보세요.
 * * [Word.getArgumentRoles] 어절이 술어인 논항들의 [RoleEdge] 목록을 가져오는 API
 * * [Word.getPredicateRole] 어절이 논항인 [RoleEdge]의 술어를 가져오는 API
 * * [Sentence.getRoles] 전체 문장을 분석한 의미역 구조 [RoleEdge]를 가져오는 API
 * * [RoleEdge] 의미역 구조를 저장하는 형태
 * * [RoleType] 의미역 분류를 갖는 Enum 값
 *
 * ## 사용법 예제
 * 분석기 `Parser`가 `CanLabelSemanticRole`을 상속받았다면,
 *
 * ### Kotlin
 * ```kotlin
 * // 문장에서 바로 분석할 때
 * val parser = Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: List<Sentence> = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/scala-support/)
 * ```scala
 * import kr.bydelta.koala.Implicits._
 * // 문장에서 바로 분석할 때
 * val parser = new Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: java.util.List[Sentence] = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Java
 * ```java
 * // 문장에서 바로 분석할 때
 * Parser parser = Parser()
 * List<Sentence> sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser.invoke("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * Sentence taggedSentence = ...
 * Sentence sentence = parser.analyze(taggedSentence) // 또는 parser.invoke(taggedSentence)
 *
 * List<Sentence> taggedSentList = ...
 * List<Sentence> sentences = parser.analyze(taggedSentList) // 또는 parser.invoke(taggedSentList)
 * ```
 *
 * @since 2.0.0
 */
interface CanLabelSemanticRole<T> : CanAnalyzeProperty<T>

/**
 * 개체명 인식 (Named Entity Recognition)을 수행하는 Interface입니다.
 * 매개변수 [T]는 각 분석기에서 문장을 분석한 결과물의 중간 형태, 즉 분석기의 입력 형태입니다.
 *
 * ## 참고
 * **개체명 인식**은 문장에서 인물, 장소, 기관, 대상 등을 인식하는 기술입니다.
 * 예) '철저한 진상 조사를 촉구하는 국제사회의 목소리가 커지고 있는 가운데, 트럼프 미국 대통령은 되레 사우디를 감싸고 나섰습니다.'에서, 다음을 인식하는 기술입니다.
 * * '트럼프': 인물
 * * '미국' : 국가
 * * '대통령' : 직위
 * * '사우디' : 국가
 *
 * 아래를 참고해보세요.
 * * [Word.getEntities] 어절이 속하는 [Entity]를 가져오는 API
 * * [Sentence.getEntities] 문장에 포함된 모든 [Entity]를 가져오는 API
 * * [Entity] 개체명을 저장하는 형태
 * * [CoarseEntityType] [Entity]의 대분류 개체명 분류구조 Enum 값
 *
 * ## 사용법 예제
 * 분석기 `Parser`가 `CanRecognizeEntity`를 상속받았다면,
 *
 * ### Kotlin
 * ```kotlin
 * // 문장에서 바로 분석할 때
 * val parser = Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: List<Sentence> = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/scala-support/)
 * ```scala
 * import kr.bydelta.koala.Implicits._
 * // 문장에서 바로 분석할 때
 * val parser = new Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: java.util.List[Sentence] = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Java
 * ```java
 * // 문장에서 바로 분석할 때
 * Parser parser = Parser()
 * List<Sentence> sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser.invoke("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * Sentence taggedSentence = ...
 * Sentence sentence = parser.analyze(taggedSentence) // 또는 parser.invoke(taggedSentence)
 *
 * List<Sentence> taggedSentList = ...
 * List<Sentence> sentences = parser.analyze(taggedSentList) // 또는 parser.invoke(taggedSentList)
 * ```
 *
 * @since 2.0.0
 */
interface CanRecognizeEntity<T> : CanAnalyzeProperty<T>

/**
 * 다의어 분별 (Word sense disambiguation)을 수행하는 Interface입니다.
 * 매개변수 [T]는 각 분석기에서 문장을 분석한 결과물의 중간 형태, 즉 분석기의 입력 형태입니다.
 *
 * ## 참고
 * **다의어 분별**은 동일한 단어의 여러 의미를 구분하는 작업입니다.
 * 예) '말1'은 다음 의미를 갖는 다의어이며, 다의어 분별 작업은 이를 구분합니다.
 * 1. 사람의 생각이나 느낌 따위를 표현하고 전달하는 데 쓰는 음성 기호.
 * 2. 음성 기호로 생각이나 느낌을 표현하고 전달하는 행위. 또는 그런 결과물.
 * 3. 일정한 주제나 줄거리를 가진 이야기.
 *
 * **동형이의어 분별**은 동일한 형태지만 다른 의미를 갖는 어절을 구분하는 작업입니다.
 * 예) '말'은 다음과 같은 여러 동형이의어의 표면형입니다.
 * 1. '말1': 사람의 생각이나 느낌 따위를 표현하고 전달하는 데 쓰는 음성 기호
 * 2. '말2': 톱질을 하거나 먹줄을 그을 때 밑에 받치는 나무
 * 3. '말3': 곡식, 액체, 가루 따위의 분량을 되는 데 쓰는 그릇
 * 4. '말4': 말과의 포유류
 * ...
 *
 * 아래를 참고해보세요.
 * * [WordSense] 동형이의어/다의어의 의미번호를 저장하는 형태
 * * [Morpheme.getWordSense] 형태소의 어깨번호/의미번호를 가져오는 API
 *
 * ## 사용법 예제
 * 분석기 `Parser`가 `CanDisambiguateSense`를 상속받았다면,
 *
 * ### Kotlin
 * ```kotlin
 * // 문장에서 바로 분석할 때
 * val parser = Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: List<Sentence> = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/scala-support/)
 * ```scala
 * import kr.bydelta.koala.Implicits._
 * // 문장에서 바로 분석할 때
 * val parser = new Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: java.util.List[Sentence] = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Java
 * ```java
 * // 문장에서 바로 분석할 때
 * Parser parser = Parser()
 * List<Sentence> sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser.invoke("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * Sentence taggedSentence = ...
 * Sentence sentence = parser.analyze(taggedSentence) // 또는 parser.invoke(taggedSentence)
 *
 * List<Sentence> taggedSentList = ...
 * List<Sentence> sentences = parser.analyze(taggedSentList) // 또는 parser.invoke(taggedSentList)
 * ```
 *
 * @since 2.0.0
 */
interface CanDisambiguateSense<T> : CanAnalyzeProperty<T>


/**
 * 상호참조 해소, 공지시어 해소 (Coreference Resolution) 또는 대용어 분석 (Anaphora Resolution)을 수행하는 Interface입니다.
 *
 * ## 참고
 * **공지시어 해소**는 문장 내 또는 문장 간에 같은 대상을 지칭하는 어구를 찾아 묶는 분석과정입니다.
 * 예) '삼성그룹의 계열사인 삼성물산은 같은 그룹의 계열사인 삼성생명과 함께'라는 문장에서
 * * '삼성그룹'과 '같은 그룹'을 찾아 묶는 것을 말합니다.
 *
 * **영형대용어 분석**은 문장에서 생략된 기능어를 찾아 문장 내 또는 문장 간에 언급되어 있는 어구와 묶는 분석과정입니다.
 * 예) '나는 밥을 먹었고, 영희도 먹었다'라는 문장에서,
 * * '먹었다'의 목적어인 '밥을'이 생략되어 있음을 찾는 것을 말합니다.
 *
 * 아래를 참고해보세요.
 * * [Sentence.getCorefGroups] 문장 내에 포함된 개체명 묶음 [CoreferenceGroup]들의 목록을 반환하는 API
 * * [Entity.getCorefGroup] 각 개체명이 속하는 [CoreferenceGroup]을 가져오는 API
 * * [CoreferenceGroup] 동일한 대상을 지칭하는 개체명을 묶는 API
 *
 * ## 사용법 예제
 * 분석기 `Parser`가 `CanResolveCoref`를 상속받았다면,
 *
 * ### Kotlin
 * ```kotlin
 * // 문장에서 바로 분석할 때
 * val parser = Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: List<Sentence> = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Scala + [koalanlp-scala](https://koalanlp.github.io/scala-support/)
 * ```scala
 * import kr.bydelta.koala.Implicits._
 * // 문장에서 바로 분석할 때
 * val parser = new Parser()
 * val sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * val taggedSentence: Sentence = ...
 * val sentence = parser.analyze(taggedSentence) // 또는 parser(taggedSentence)
 *
 * val taggedSentList: java.util.List[Sentence] = ...
 * val sentences = parser.analyze(taggedSentList) // 또는 parser(taggedSentList)
 * ```
 *
 * ### Java
 * ```java
 * // 문장에서 바로 분석할 때
 * Parser parser = Parser()
 * List<Sentence> sentences = parser.analyze("문장 2개입니다. 결과는 목록이 됩니다.") // 또는 parser.invoke("문장 2개입니다. 결과는 목록이 됩니다.")
 *
 * // 타 분석기에서 분석한 다음 이어서 분석할 때
 * Sentence taggedSentence = ...
 * Sentence sentence = parser.analyze(taggedSentence) // 또는 parser.invoke(taggedSentence)
 *
 * List<Sentence> taggedSentList = ...
 * List<Sentence> sentences = parser.analyze(taggedSentList) // 또는 parser.invoke(taggedSentList)
 * ```
 *
 * @since 2.0.0
 */
interface CanResolveCoref<T> : CanAnalyzeProperty<T>