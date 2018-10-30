@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.rhino

import kr.bydelta.koala.POS
import kr.bydelta.koala.data.Morpheme
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.data.Word
import kr.bydelta.koala.proc.CanTagOnlyASentence
import rhino.lexicon.LexiconInterface
import java.util.*


/**
 * RHINO에서 구현했던 Tagger의 Kotlin 버전입니다.
 * 불필요하게 참조되고 있는 Swing JFrame을 삭제하기 위해 재작성했습니다.
 *
 * 이 코드의 원본은 `rhino.MainClass`이며, 본 object 코드의 저작권은 RHINO의 저작자에게 귀속됩니다.
 */
internal object RHINOTagger {
    val SPECIALS = "([■▶◀◆▲◇◈☎【】]+)".toRegex()
    val TOKENIZE = " .?!。‘’“”`\'\"(){}[]─,ㆍ·:;/…_~∼∽□+-=±÷×*^><｜|％%&￦₩\\＄$¥￥£￡°㎞㎏@©ⓒ↑|#♥♡★☆♪♬■▶◀◆▲◇◈☎【】"
    val RETAGGING_CONTAIN = "(는|은|을|세|야|자라고)".toRegex()
    val RETAGGING_EQUAL = "^(는|은|을|세|야|자라고)$".toRegex()
    val MORPH_MATCH = "([^\\s]+)/([A-Z]{2,3})".toRegex()

    fun tag(input: String): List<Word> {
        val arr = split(input)
        val inputArr = arr.filter { it.second }.map { it.first }.toTypedArray()
        val outputArr = (1..arr.size).map { "" }.toTypedArray()

        val interf = LexiconInterface(
                DictionaryReader.complexStem_MethodDeleted,
                DictionaryReader.stem_MethodDeleted,
                DictionaryReader.afterNumber_MethodDeleted,
                DictionaryReader.ending_MethodDeleted,
                DictionaryReader.stem_List,
                DictionaryReader.ending_List,
                inputArr, outputArr,
                DictionaryReader.nonEndingList,
                DictionaryReader.aspgStem,
                DictionaryReader.aspgEnding)

        inputArr.indices.forEach { i ->
            if (interf.FindMethod(inputArr[i], DictionaryReader.combiMethods_List)) {
                outputArr[i] = interf.BindingWordInMethod(i, "combi", inputArr[i])
            } else if (interf.FindSentenceMark(inputArr[i])) {
                outputArr[i] = interf.GetSentenceMarkResult()
            } else if (interf.CheckNumber(i)) {
                try {
                    outputArr[i] = interf.FindArabicNumberPlus(i, DictionaryReader.afterNumber_MethodDeleted, DictionaryReader.afterNumber_List)
                } catch (_: Throwable) {
                    // RHINO Exception Handling 안되는 부분
                    outputArr[i] = inputArr[i] + "/NF"
                }
            } else {
                val (head, eogan, eomi) = interf.FindMorph(i, inputArr[i])
                val full = head?.replace("/NA \\+ /NA \\+ /NA$".toRegex(), "/NA")
                        ?.replace("/NNG \\+ ([하되])/(VV|VX|XSV)".toRegex(), "$1/VV") ?: "NA"

                outputArr[i] = full

                if (eogan != null && eomi != null) {
                    if (RETAGGING_CONTAIN.containsMatchIn(eomi)) {
                        val eomiForm =
                                eomi.split(" + ").joinToString("") { it.substring(0, it.indexOf("/")) }

                        if (eomiForm.matches(RETAGGING_EQUAL)) {
                            val newEomi =
                                    try {
                                        if (interf.FindMethod(eomiForm, DictionaryReader.endingMethods_List)) {
                                            interf.BindingWordInMethod(i, "ending", eomiForm, true).split("_".toRegex(), 3)[1]
                                        } else eomi
                                    } catch (_: Throwable) {
                                        eomi
                                    }

                            if (eogan != "") {
                                outputArr[i] = "$eogan + $newEomi"
                            } else {
                                outputArr[i] = newEomi
                            }
                        }
                    }
                }

                /***** 여기서부터는 RHINO에 없는 코드입니다: 어간/어미 분리 오류로 인한 해석 불가능 문제 해결 (2018.10.23) ******/
                if (outputArr[i] == "/NA") {
                    outputArr[i] = inputArr[i] + "/NA"
                }
                /***** 여기까지 추가 수정입니다. (2018.10.23) ******/
            }
        }

        /******* 여기서부터는 RHINO에 없는 코드입니다. *******/
        val sequence = mutableListOf<Word>()
        var indexOfInputArr = 0

        for ((word, isSymbol) in arr) {
            if (!isSymbol) {
                sequence.add(Word(word, listOf(Morpheme(word, POS.SW, ""))))
            } else {
                if (word == "." && indexOfInputArr > 0 && inputArr[indexOfInputArr - 1].all { it == '.' }) {
                    val lastWord = sequence.removeAt(sequence.size - 1)
                    val surf = lastWord.surface + "."
                    sequence.add(Word(surf, listOf(Morpheme(surf, POS.SE, "SE"))))
                } else if (word == "^" && indexOfInputArr > 0 && inputArr[indexOfInputArr - 1] == "^") {
                    sequence.removeAt(sequence.size - 1)
                    sequence.add(Word("^^", listOf(Morpheme("^^", POS.IC, "IC"))))
                } else {
                    val morphs = translateOutputMorpheme(outputArr[indexOfInputArr])
                    sequence.add(Word(word.trim(), morphs))
                }
                indexOfInputArr += 1
            }
        }

        return sequence.filter { it.surface.isNotBlank() }
        /***** 여기까지 추가수정이었습니다. ******/
    }

    private fun translateOutputMorpheme(morphStr: String) =
            MORPH_MATCH.findAll(morphStr).map {
                val surf = it.groupValues[1]
                val raw = it.groupValues[2]

                Morpheme(surf.trim(), POS.valueOf(raw), raw)
            }.toList()

    internal fun split(input: String): List<Pair<String, Boolean>> {
        val tokens = StringTokenizer(input.replace("\\s+".toRegex(), " "), TOKENIZE, true)
        val buffer = mutableListOf<Pair<String, Boolean>>()

        while (tokens.hasMoreTokens()) {
            val token = tokens.nextToken()

            if (token.isNotBlank()) {
                buffer.add(token to !SPECIALS.containsMatchIn(token))
            }
        }

        return buffer.toList()
    }
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