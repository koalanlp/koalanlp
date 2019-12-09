@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.utagger

import com.beust.klaxon.Klaxon
import com.sun.jna.*
import kr.bydelta.koala.POS
import kr.bydelta.koala.data.Morpheme
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.proc.CanTagOnlyASentence
import java.io.File


/**
 * UTagger Shared object와 소통하기 위한 JNA Wrapper
 */
interface UTaggerLibrary : Library {
    /**
     * UTagger 설정파일 불러오기
     *
     * **(참고)** UTagger 설정과 값을 불러온 경우 반드시 [Global_release] 함수를 불러 연결을 종료해주셔야 합니다.
     *
     * @param configPath UTagger 설정파일(Hlxcfg.txt)의 위치. Character pointer 유형.
     * @param v2 문서에 지정되지 않은 알 수 없는 옵션. 0으로 입력함
     * @return 오류 메시지
     */
    fun Global_init2(configPath: Pointer, v2: Int): WString

    /**
     * 새로운 UTagger Thread 생성
     *
     * **(참고)** UTagger Thread를 사용한 이후에는 반드시 [deleteUCMA] 함수를 불러 연결을 종료해주셔야 합니다.
     *
     * @param analyzerIndex 사용할 Thread의 index. 0부터 99까지 가능.
     * @return 오류 메시지
     */
    fun newUCMA2(analyzerIndex: Int): WString

    /**
     * UTagger의 출력에서 carriage return(\r)을 삭제합니다.
     *
     * @param analyzerIndex 사용할 Thread의 index. 0부터 99까지 가능. (사용 전 반드시 [newUCMA2]를 사용해 생성해야 함)
     */
    fun cmaSetNewlineN(analyzerIndex: Int)

    /**
     * UTagger를 사용하여 형태소 분석을 수행합니다.
     *
     * @param analyzerIndex 사용할 Thread의 index. 0부터 99까지 가능. (사용 전 반드시 [newUCMA2]를 사용해 생성해야 함)
     * @param inStr 입력할 문자열 WChar 포인터.
     * @param styleOption 출력 형태 옵션. JSON 출력은 영향이 없는 것으로 보임. 문서에서 3으로 입력함.
     * @return JSON 형태의 분석 결과.
     */
    fun cma_tag_line_json2(analyzerIndex: Int, inStr: WString, styleOption: Int): WString

    /**
     * UTagger를 사용하여 대역어를 확인합니다.
     *
     * @param analyzerIndex 사용할 Thread의 index. 0부터 99까지 가능. (사용 전 반드시 [newUCMA2]를 사용해 생성해야 함)
     * @param parsedJsonStr 대역어 분석 결과 JSON WChar 포인터.
     * @param translateTo 뜻 풀이의 대상이 되는 언어.
    -      0=없음
    -      1=영어
    -      2=일어
    -      3=프랑스어(불어)
    -      4=스페인어
    -      5=아랍어
    -      6=몽골어
    -      7=베트남어
    -      8=태국어
    -      9=인도네시아어
    -      10=러시아어
    -      11=중국어
     */
    fun cma_tag_target_word_json2(analyzerIndex: Int, inStr: WString, translateTo: Int): WString

    /**
     * UTagger thread를 닫습니다.
     *
     * @param analyzerIndex 닫을 Thread의 index. 0부터 99까지 가능.
     */
    fun deleteUCMA(analyzerIndex: Int)

    /**
     * UTagger 연결 종료
     */
    fun Global_release()
}

/**
 * UTagger 분석기 Wrapper
 *
 * 코드는 UTagger 2018/2019 Python 예시 코드를 참조하여 재작성했습니다.
 */
class UTagger {
    /**
     * JSON 변환을 위한 Klaxon 객체
     */
    private val klaxon by lazy { Klaxon() }

    /**
     * 현재 UTagger thread의 번호
     */
    private var threadId: Int = -1

    /**
     * UTagger를 엽니다. (finalize시 자동으로 UTagger가 닫힙니다.)
     *
     * - 이미 열려있는 경우는 다시 열지 않습니다.
     * - 연결을 닫은 후에 다시 연결을 열 수 있습니다.
     */
    fun open() {
        synchronized(this) {
            if (this.threadId < 0) {
                this.threadId = UTagger.open()
                LIB?.cmaSetNewlineN(this.threadId)
            }
        }
    }

    /**
     * UTagger 연결을 닫습니다.
     *
     * - 이미 닫혀있는 경우는 다시 닫지 않습니다.
     * - 연결을 닫은 후에라도 필요하면 다시 [open] 함수를 호출해서 열 수 있습니다.
     */
    fun close() {
        synchronized(this) {
            if (this.threadId >= 0) {
                close(this.threadId)
            }

            this.threadId = -1
        }
    }

    /**
     * 형태소 분석을 수행합니다.
     *
     * @param inStr 입력 문자열
     * @param tagWithMeaning 뜻 풀이의 대상이 되는 언어를 지정합니다.
    -      0=없음
    -      1=영어
    -      2=일어
    -      3=프랑스어(불어)
    -      4=스페인어
    -      5=아랍어
    -      6=몽골어
    -      7=베트남어
    -      8=태국어
    -      9=인도네시아어
    -      10=러시아어
    -      11=중국어
     * @return 형태소 분석 결과 JSON을 해석한 [UWord]의 목록.
     */
    @JvmOverloads
    fun analyze(inStr: String, tagWithMeaning: Int = 0): List<UWord>? {
        open()

        val result: String = synchronized(LOCK) {
            analyzeAsJSON(inStr, tagWithMeaning)
        }

        if (result.isNotBlank())
            return klaxon.parseArray(result)
        else
            return emptyList()
    }


    /**
     * 형태소 분석을 수행하고, 원본 JSON 문자열 그대로를 반환합니다.
     *
     * @param inStr 입력 문자열
     * @param tagWithMeaning 뜻 풀이의 대상이 되는 언어를 지정합니다.
    -      0=없음
    -      1=영어
    -      2=일어
    -      3=프랑스어(불어)
    -      4=스페인어
    -      5=아랍어
    -      6=몽골어
    -      7=베트남어
    -      8=태국어
    -      9=인도네시아어
    -      10=러시아어
    -      11=중국어
     * @return 형태소 분석 결과 JSON 문자열
     */
    @JvmOverloads
    fun analyzeAsJSON(inStr: String, tagWithMeaning: Int = 0): String {
        // UTF-8 WideString으로 변환 후 이 pointer를 전달합니다.
        val wString = WString(inStr)
        return if (tagWithMeaning > 0) LIB!!.cma_tag_target_word_json2(this.threadId, wString, 3).toString()
        else LIB!!.cma_tag_line_json2(this.threadId, wString, tagWithMeaning).toString()
    }

    /**
     * Finalize시 Close하도록 처리
     */
    @Suppress("ProtectedInFinal", "Unused")
    protected fun finalize() {
        close()
    }

    /**
     * UTagger의 Companion object
     */
    companion object {
        /** Thread lock **/
        private const val LOCK = 1
        /** 라이브러리 위치 **/
        private var libPath: String? = null
        /** 설정파일 위치 **/
        private var confPath: String? = null
        /** 라이브러리 인스턴스 **/
        private var LIB: UTaggerLibrary? = null
        /** 사용중인 UTagger thread 목록 **/
        private var ASSIGNED_HANDLES: MutableList<Int> = mutableListOf()

        /**
         * UTagger의 라이브러리와 설정파일의 위치를 지정합니다.
         *
         * @param libraryPath 라이브러리의 위치입니다.
         * @param configPath 설정 파일의 위치입니다.
         */
        @JvmStatic
        fun setPath(libraryPath: String, configPath: String) {
            if (LIB != null && (libPath != libraryPath || confPath != configPath)) {
                LIB?.Global_release()
            }

            // Lazy loading
            libPath = File(libraryPath).takeIf { it.isFile }?.absolutePath
            confPath = File(configPath).takeIf { it.isFile }?.absolutePath
        }

        /**
         * UTagger thread를 엽니다.
         */
        private fun open(): Int {
            synchronized(LOCK) {
                if (LIB == null) {
                    // 인코딩 지정
                    System.setProperty("jna.encoding", "UTF-8")

                    if (libPath.isNullOrBlank() || confPath.isNullOrBlank()) {
                        throw IllegalStateException(
                                """UTagger 라이브러리 파일과 설정 파일의 정보가 지정되지 않았습니다.
                        |UTagger.setPath(libraryPath, configPath)를 사용하여 지정해주세요.""".trimMargin())
                    }

                    LIB = try {
                        Native.load(libPath, UTaggerLibrary::class.java) as UTaggerLibrary
                    } catch (e: Exception) {
                        throw UnsatisfiedLinkError(
                                """UTagger 라이브러리 파일을 ${libPath}에서 찾을 수 없습니다.
                        |UTagger는 C/C++로 구현되어 KoalaNLP는 이를 자동으로 설치하지 않습니다. UTagger가 설치되어 있는지 확인해주세요.
                        |설치가 필요한 경우, 설치법은 https://koalanlp.github.io/usage/Install-UTagger.md 에서 확인해주세요.""".trimMargin())
                    }

                    // 인코딩 문제로 인해 CP949 byte array로 강제 전환 후 이 pointer를 전달합니다.
                    val confPathArray = Native.toByteArray(confPath, "euc-kr")
                    val confMemory = Memory(confPathArray.size.toLong())
                    confMemory.write(0, confPathArray, 0, confPathArray.size)

                    val msg = LIB!!.Global_init2(confMemory, 0)
                    ASSIGNED_HANDLES.clear()

                    if (msg.isNotBlank()) {
                        throw Error("""UTagger를 초기화하는 데 실패했습니다. 다음 오류를 참고하세요.
                    |${msg}""".trimMargin())
                    }
                }
            }

            val handle = (0..99).firstOrNull { it !in ASSIGNED_HANDLES }
            val msg = handle?.let { synchronized(LOCK) { LIB!!.newUCMA2(it) } }

            when {
                handle == null -> {
                    throw IllegalStateException("""가능한 최대 UTagger thread의 수를 초과했습니다.
                              |UTagger는 최대 100개까지 만들 수 있으므로, 이미 열려있는 tagger를 메모리에서 삭제하십시오.""".trimMargin())
                }
                msg == null -> {
                    throw NullPointerException("""UTagger 라이브러리가 초기화 되지 않았습니다.  
                              |라이브러리 초기화는 UTagger.setPath(libraryPath, configPath) 함수로 수행할 수 있습니다.""".trimMargin())
                }
                msg.isNotBlank() -> {
                    throw Error("""UTagger thread 초기화 과정에서 다음과 같은 오류가 발생했습니다.
                              |${msg}""".trimMargin())
                }
                else -> {
                    return handle
                }
            }
        }

        /**
         * UTagger thread를 닫습니다.
         */
        private fun close(handle: Int) {
            if (handle !in ASSIGNED_HANDLES) {
                throw IllegalStateException("지정된 실제로 열려있지 않은 UTagger를 닫으려고 하고 있습니다.")
            }

            synchronized(LOCK) {
                LIB?.deleteUCMA(handle)
                ASSIGNED_HANDLES.remove(handle)

                if (ASSIGNED_HANDLES.isEmpty()) {
                    LIB?.Global_release()
                    LIB = null
                }
            }
        }
    }
}

/**
 * UTagger 품사 분석 API의 Wrapper입니다.
 *
 * * 이 Wrapper는 아직 실험 단계입니다.
 *
 * * UTagger 2018이 지원하는 기능 중에서, Dependency parsing은 Segmentation fault가 많이 발생하여 지원되지 않습니다.
 *
 * * UTagger Shared Object가 이미 설치되어 있어야 합니다. (자동설치 하지 않으며, 설치 방법은
 *   [UTagger 설치 방법](https://koalanlp.github.io/usage/Install-UTagger.md)를 참조하세요.)
 *
 * * Shared object와 JNI를 통한 송수신 과정에서 Exception 또는 Segmentation fault 등이 발생할 수 있습니다.
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
 * @since 2.0.0
 * @constructor 분석기를 생성합니다.
 */
class Tagger : CanTagOnlyASentence<List<UWord>?>() {

    /**
     * UTagger Library의 JNA Wrapper
     */
    val uTagger by lazy {
        val api = UTagger()
        api.open()
        api
    }


    /**
     * List<[UWord]> 타입의 분석결과 [result]를 변환, [Sentence]를 구성합니다.
     *
     * @since 2.0.0
     * @param text 품사분석을 수행한 문단의 String입니다.
     * @param result 변환할 분석결과.
     * @return 변환된 [Sentence] 객체
     */
    override fun convertSentence(text: String, result: List<UWord>?): Sentence {
        val words = result?.map { it.toWord() }

        if (words == null)
            return Sentence.empty
        else
            return Sentence(words.toList())
    }

    /**
     * 변환되지않은, [text]의 분석결과 List<[UWord]>를 반환합니다.
     *
     * @since 2.0.0
     * @param text 분석할 String.
     * @return 원본 분석기 분석 결과인 [UWord] 객체
     */
    override fun tagSentenceOriginal(text: String): List<UWord>? = if (text.isBlank()) null else uTagger.analyze(text)

    /**
     * Khaiii를 닫을 경우, finalize 단계에서 close하여 handle을 너무 많이 열지 않도록 관리합니다.
     */
    @Suppress("ProtectedInFinal", "Unused")
    protected fun finalize() {
        uTagger.close()
    }
}
