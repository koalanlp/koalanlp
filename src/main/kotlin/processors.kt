package kr.bydelta.koala

import java.util.*

/**
 * 문장분리기 Interface
 */
interface CanSplitSentence {
    /**
     * 주어진 문단 [text]를 문장단위로 분리함.
     *
     * @param text 문장단위로 분리할 String.
     * @return 문장단위로 분리된 String의 [List].
     */
    fun sentences(text: String): List<String>

    /**
     * 주어진 문단 [text]를 문장단위로 분리함.
     *
     * @param text 문장단위로 분리할 String.
     * @return 문장단위로 분리된 String의 [List].
     */
    operator fun invoke(text: String): List<String> = sentences(text)
}

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
        val sentences = mutableListOf<MutableList<Word>>(mutableListOf())
        val parenStack = Stack<Char>()
        val it = para.iterator()

        while (it.hasNext()) {
            val head = it.next()
            sentences.last().add(head)

            if (isEnding(head, parenStack)) sentences.add(mutableListOf())
        }

        return sentences.filter { it.isNotEmpty() }.map { Sentence(it.toList()) }
    }

    private fun isEnding(morphemes: Iterable<Morpheme>, stack: Stack<Char>): Boolean {
        var isPreviousSL = false
        var hasEndOfSentence = false
        val it = morphemes.iterator()

        while (it.hasNext()) {
            val (surface: String, tag: POS) = it.next()

            if (tag == POS.SF && (surface != "." || !isPreviousSL)) {
                hasEndOfSentence = true
            } else if (tag == POS.SN) {
                hasEndOfSentence = false
            } else {
                for (ch in surface) {
                    if (ch in openParenRegex) {
                        stack.push(ch)
                    } else if (ch in closeParenRegex) {
                        if (stack.isNotEmpty() && closeParenRegex.indexOf(ch) == openParenRegex.indexOf(stack.peek()))
                            stack.pop()
                    } else if (ch in quoteRegex) {
                        if (stack.isNotEmpty() && stack.peek() == ch)
                            stack.pop()
                        else
                            stack.push(ch)
                    }
                }
            }

            isPreviousSL = tag == POS.SL
        }

        return stack.isEmpty() && hasEndOfSentence
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
     * [item]을 분석하여 [OUT] 유형의 property 값을 반환함
     */
    fun getProperty(item: IN): OUT

    /**
     * [sentence]를 분석하여 [OUT] 유형의 property 값을 추가함
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