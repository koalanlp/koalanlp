/**
 * # Package kr.bydelta.koala.proc
 *
 * KoalaNLP가 지원하는, 또는 지원할 자연어처리 분석기 interface들을 모은 패키지입니다.
 */
package kr.bydelta.koala.proc

import kr.bydelta.koala.POS
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
 * val splitted = splitter.sentences(sentence)
 * // 또는
 * val splitted = splitter(sentence)
 * ```
 *
 * ### Scala
 * ```scala
 * import kr.bydelta.koala.Implicits.*
 * val splitter = new SentenceSplitter()
 * val sentence = "분석할 문장을 적었습니다."
 * val splitted = splitter.sentences(sentence)
 * // 또는
 * val splitted = splitter(sentence)
 * ```
 *
 * ### Java
 * ```java
 * SentenceSplitter splitter = new SentenceSplitter();
 * String sentence = "분석할 문장을 적었습니다.";
 * List<String> splitted = splitter.sentences(sentence);
 * // 또는
 * List<String> splitted = splitter.invoke(sentence);
 * ```
 */
interface CanSplitSentence {
    /**
     * 주어진 문단 [text]를 문장단위로 분리합니다.
     *
     * @param text 문장단위로 분리할 String.
     * @return 문장단위로 분리된 String의 [List].
     */
    fun sentences(text: String): List<String>

    /**
     * 주어진 문단 [text]를 문장단위로 분리합니다.
     *
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
 * val splitted = SentenceSplitter.sentences(sentence)
 * // 또는
 * val splitted = SentenceSplitter(sentence)
 * ```
 *
 * ### Scala
 * ```scala
 * import kr.bydelta.koala.Implicits.*
 * val sentence = ... //Tagged result
 * val splitted = SentenceSplitter.sentences(sentence)
 * // 또는
 * val splitted = SentenceSplitter(sentence)
 * ```
 *
 * ### Java
 * ```java
 * Sentence sentence = ... //Tagged result
 * List<Sentence> splitted = SentenceSplitter.sentences(sentence);
 * // 또는
 * List<Sentence> splitted = SentenceSplitter.invoke(sentence);
 * ```
 */
object SentenceSplitter {
    private val quoteRegex = "\'\"＇＂"
    private val openParenRegex = "([{‘“（［｛<〔〈《「『【"
    private val closeParenRegex = ")]}’”）］｝>〕〉》」』】"

    /**
     * 분석결과를 토대로 문장을 분리함.
     *
     * @param para 분리할 문단.
     * @return 문장단위로 분리된 결과
     */
    operator fun invoke(para: Iterable<Word>): List<Sentence> {
        val sentences = mutableListOf<Sentence>()
        val parenStack = Stack<Char>()
        val it = para.toList()
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
}

/**
 * 품사분석기 interface
 */
interface CanTag {
    /**
     * 주어진 문장 [text]을 분석하여 품사를 부착하고, 결과로 [Sentence] 객체를 돌려줌.
     */
    fun tagSentence(text: String): Sentence

    /**
     * 주어진 문단 [text]을 분석하여 품사를 부착하고, 결과로 [List]<[Sentence]> 객체를 돌려줌.
     */
    fun tag(text: String): List<Sentence>

    /**
     * 주어진 문단 [text]을 분석하여 품사를 부착하고, 결과로 [List]<[Sentence]> 객체를 돌려줌.
     */
    operator fun invoke(text: String) = tag(text)
}

/**
 * 문장 1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S] 타입으로 돌려줌.
 */
abstract class CanTagASentence<S> : CanTag {
    final override fun tagSentence(text: String): Sentence {
        val trim = text.trim()
        return if (trim.isNotEmpty()) convertSentence(tagSentenceOriginal(trim))
        else Sentence.empty
    }

    /**
     * 변환되지않은, [text]의 분석결과 [S]를 반환.
     *
     * @param text 분석할 String.
     * @return 원본 문단객체의 Sequence
     */
    abstract fun tagSentenceOriginal(text: String): S

    /**
     * [S] 타입의 분석결과 [result]를 변환, [Sentence]를 구성함.
     *
     * @param result 변환할 분석결과.
     * @return 변환된 Sentence 객체
     */
    protected abstract fun convertSentence(result: S): Sentence
}

/**
 * 문단1개, 문장1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S] 타입으로 돌려줌.
 */
abstract class CanTagAParagraph<S> : CanTagASentence<S>() {
    final override fun tag(text: String): List<Sentence> {
        val trim = text.trim()
        return if (trim.isNotEmpty()) tagParagraphOriginal(trim).map { convertSentence(it) }
        else emptyList()
    }

    /**
     * 변환되지않은, [text]의 분석결과 [List]<[S]>를 반환.
     *
     * @param text 분석할 String.
     * @return 원본 문단객체의 List
     */
    abstract fun tagParagraphOriginal(text: String): List<S>
}

/**
 * 문단1개는 불가하지만, 문장1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S] 타입으로 돌려줌.
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
     * 변환되지않은, [text]의 분석결과 [S]를 반환.
     *
     * @param text 분석할 String.
     * @return 원본 문단객체의 Sequence
     */
    abstract fun tagSentenceOriginal(text: String): S

    /**
     * [S] 타입의 분석결과 [result]를 변환, [Sentence]를 구성함.
     *
     * @param text 품사분석을 시행할 문단 String
     * @param result 변환할 분석결과.
     * @return 변환된 Sentence 객체
     */
    protected abstract fun convertSentence(text: String, result: S): Sentence
}

/**
 * 문장1개는 불가하지만, 문단1개가 분석가능한 품사분석기 interface. 원본 분석기는 문장 분석 결과를 [S] 타입으로 돌려줌.
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
     * 변환되지않은, [text]의 분석결과 [List]<[S]>를 반환.
     *
     * @param text 분석할 String.
     * @return 원본 문단객체의 List
     */
    abstract fun tagParagraphOriginal(text: String): List<S>

    /**
     * [S] 타입의 분석결과 [result]를 변환, [Sentence]를 구성함.
     *
     * @param result 변환할 분석결과.
     * @return 변환된 Sentence 객체
     */
    protected abstract fun convertSentence(result: S): Sentence
}

/**
 * [Sentence] 객체에 property를 추가할 수 있는 interface
 */
interface CanAnalyzeProperty<IN : Property, OUT> {
    /**
     * [item]을 분석하여 property 값을 반환함
     */
    fun getProperty(item: IN): OUT

    /**
     * [sentence]를 분석하여 property 값을 추가함
     */
    fun attachProperty(sentence: Sentence)

    /**
     * String [sentence]를 [List]<[Sentence]>로 변환함.
     */
    fun convert(sentence: String): List<Sentence>

    /**
     * String [sentence]를 분석함. 결과는 각 [Sentence]의 property로 저장됨.
     */
    fun parse(sentence: String): List<Sentence> {
        val paragraph = convert(sentence)
        return parse(paragraph)
    }

    /**
     * [sentence]를 분석함. 결과는 각 [Sentence]의 property로 저장됨.
     */
    fun parse(sentence: Sentence): Sentence {
        attachProperty(sentence)
        return sentence
    }

    /**
     * [sentences]를 분석함. 결과는 각 [Sentence]의 property로 저장됨.
     */
    fun parse(sentences: List<Sentence>): List<Sentence> = sentences.map { parse(it) }

    /**
     * [sentence]를 분석함. 결과는 각 [Sentence]의 property로 저장됨.
     */
    operator fun invoke(sentence: String) = parse(sentence)

    /**
     * [sentence]를 분석함. 결과는 각 [Sentence]의 property로 저장됨.
     */
    operator fun invoke(sentence: Sentence) = parse(sentence)

    /**
     * [sentences]를 분석함. 결과는 각 [Sentence]의 property로 저장됨.
     */
    operator fun invoke(sentences: List<Sentence>) = parse(sentences)
}


/**
 * [Sentence] 객체에 property를 추가할 수 있는 interface
 */
interface CanAnalyzeSentenceProperty<P : Property> : CanAnalyzeProperty<Sentence, P> {
    override fun attachProperty(sentence: Sentence) {
        sentence.setProperty(getProperty(sentence))
    }
}

/**
 * 구문분석 Interface
 */
interface CanSyntaxParse : CanAnalyzeSentenceProperty<SyntaxTree>

/**
 * 의존구문분석 Interface
 */
interface CanDepParse : CanAnalyzeSentenceProperty<DepTree>

/**
 * 의미역 분석(Semantic Role Labeling) Interface
 */
interface CanLabelSemanticRole : CanAnalyzeSentenceProperty<RoleTree>

/**
 * 개체명 인식 (Named Entity Recognition) Interface
 */
interface CanRecognizeEntity : CanAnalyzeSentenceProperty<ListProperty<Entity>>

/**
 * 다의어 분별 (Word sense disambiguation) Interface
 */
interface CanDisambiguateSense : CanAnalyzeProperty<Sentence, Sentence> {
    override fun attachProperty(sentence: Sentence) {
        getProperty(sentence)
    }
}