@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.khaiii

import com.beust.klaxon.Klaxon
import com.sun.jna.*
import kr.bydelta.koala.POS
import kr.bydelta.koala.data.Morpheme
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.data.Word
import kr.bydelta.koala.proc.CanTagOnlyASentence
import java.io.File
import java.util.logging.Level


/**
 * Khaiii Shared object와 소통하기 위한 JNA Wrapper
 */
interface KhaiiiLibrary : Library {
    /**
     * Khaiii 버전
     */
    fun khaiii_version(): String

    /**
     * Khaiii 연결
     *
     * **[참고]** Khaiii를 사용한 이후에는 반드시 [close] 함수를 불러 연결을 종료해주셔야 합니다.
     *
     * @param rscDir 리소스 디렉터리
     * @param optStr 옵션 (JSON 형식의 String)
     * @return Khaiii handle
     */
    fun khaiii_open(rscDir: String, optStr: String): Int

    /**
     * 형태소 분석 수행
     * @param handle Khaiii handle
     * @param inStr 입력 문자열 포인터
     * @param optStr 문자열 분석시에 override할 옵션
     * @return 분석 결과의 첫 어절
     */
    fun khaiii_analyze(handle: Int, inStr: Pointer, optStr: String): KhaiiiWord?
    // [참고] String으로 할 경우 인코딩 문제가 있으니 inStr는 Pointer로 유지해야 함

    /**
     * 분석 결과를 deallocate함
     * @param handle Khaiii handle
     * @param results 분석 결과 포인터 (KhaiiiWord의 pointer)
     */
    fun khaiii_free_results(handle: Int, results: Pointer)

    /**
     * Khaiii 연결 종료
     * @param handle Khaiii handle
     */
    fun khaiii_close(handle: Int)

    /**
     * 가장 마지막 오류 조회
     * @param handle Khaiii handle
     * @return 오류 문자열
     */
    fun khaiii_last_error(handle: Int): String

    /**
     * 음절별로 지정된 태그값의 목록을 반환합니다. (Khaiii_dev API)
     * @param handle Khaiii handle
     * @param inStr 입력 문자열 포인터
     * @param optStr 문자열 분석시에 override할 옵션
     * @param output 음절별 분석 결과가 반환될 intArray
     * @return array에서 분석 결과가 저장된 크기
     */
    fun khaiii_analyze_bfr_errorpatch(handle: Int, inStr: Pointer, optStr: String, output: IntArray?): Int

    /**
     * Khaiii 로그 레벨 지정
     * @param name Logger 이름
     * @param level spdlog level
     */
    fun khaiii_set_log_level(name: String, level: String): Int

    /**
     * Khaiii 로그 레벨 지정
     * @param nameLevelPairs 각 Logger별로 지정할 spdlog level들. ${Logger}:${level},${Logger}:${level}... 형식
     */
    fun khaiii_set_log_levels(nameLevelPairs: String): Int
}

/**
 * Khaiii 분석기 Wrapper
 *
 * 코드는 Khaiii Python package의 코드를 참조하여 재작성했습니다.
 *
 * @constructor Wrapper를 생성합니다.
 * @param resourceDirectory 리소스가 저장된 위치. 기본값 사용시 "".
 * @param opt Khaiii option
 */
class Khaiii @JvmOverloads constructor(resourceDirectory: String = "", val opt: KhaiiiConfig = KhaiiiConfig()) {
    /**
     * JSON 변환을 위한 Klaxon 객체
     */
    private val klaxon by lazy { Klaxon() }
    /**
     * 리소스가 저장된 위치
     */
    private val rscDir: String by lazy {
        val dir =
                if (resourceDirectory.isBlank()) {
                    if (Platform.isWindows()) throw Exception("Windows에서의 설치 위치는 수동 지정이 필요합니다.")
                    "/usr/local/share/khaiii"
                } else resourceDirectory

        if (File(dir).isDirectory) dir
        else throw Exception("${dir}가 비어있습니다! Khaiii의 Resource 위치를 다시 확인해주세요.")
    }

    /**
     * 현재 Khaiii API의 handle 값
     */
    private var handle: Int = -1

    /**
     * Khaiii version
     */
    fun version() = LIB.khaiii_version()

    /**
     * Khaiii를 엽니다. (finalize시 자동으로 Khaiii가 닫힙니다.)
     *
     * - 이미 열려있는 경우는 다시 열지 않습니다.
     * - 연결을 닫은 후에 다시 연결을 열 수 있습니다.
     */
    fun open() {
        synchronized(this) {
            if (this.handle < 0) {
                this.handle = LIB.khaiii_open(this.rscDir, klaxon.toJsonString(opt))
                if (this.handle < 0)
                    throw getLastError()
            }
        }
    }

    /**
     * Khaiii 연결을 닫습니다.
     *
     * - 이미 닫혀있는 경우는 다시 닫지 않습니다.
     * - 연결을 닫은 후에라도 필요하면 다시 [open] 함수를 호출해서 열 수 있습니다.
     */
    fun close() {
        synchronized(this) {
            if (this.handle >= 0) {
                LIB.khaiii_close(this.handle)
            }

            this.handle = -1
        }
    }

    /**
     * 형태소 분석을 수행합니다.
     * @param inStr 입력 문자열
     * @param opt Override할 설정 값. 그런 설정값이 없는 경우 null로 두거나 비워두시면 됩니다. (기본값 null)
     */
    @JvmOverloads
    fun analyze(inStr: String, opt: KhaiiiConfig? = null): KhaiiiWord? {
        open()

        // 인코딩 문제로 인해 UTF-8 byte array로 강제 전환 후 이 pointer를 전달합니다.
        val inByteArray = Native.toByteArray(inStr, "UTF-8")
        val memory = Memory(inByteArray.size.toLong())
        memory.write(0, inByteArray, 0, inByteArray.size)

        val optStr = if (opt == null) "" else klaxon.toJsonString(opt)
        val results = LIB.khaiii_analyze(this.handle, memory, optStr) ?: throw getLastError()
        LIB.khaiii_free_results(this.handle, results.pointer)

        return results
    }

    /**
     * 음절별로 지정된 태그값의 목록을 반환합니다. (Khaiii_dev API)
     * @param inStr 입력 문자열 포인터
     * @param optStr 문자열 분석시에 override할 옵션
     * @return array에서 분석 결과가 저장된 크기
     */
    @JvmOverloads
    fun analyzeBeforeErrorPatch(inStr: String, optStr: String = ""): List<String> {
        open()

        // 인코딩 문제로 인해 UTF-8 byte array로 강제 전환 후 이 pointer를 전달합니다.
        val inByteArray = Native.toByteArray(inStr, "UTF-8")
        val memory = Memory(inByteArray.size.toLong())
        memory.write(0, inByteArray, 0, inByteArray.size)

        val output = IntArray(inStr.length)
        val outNum = LIB.khaiii_analyze_bfr_errorpatch(this.handle, memory, optStr, output)

        if (outNum < 2)
            throw getLastError()

        // 음절별 분석 결과를 복원합니다.
        val results = mutableListOf<String>()
        for (idx in 1..outNum) {
            results.add(posTagsInKhaiii[output[idx]])
        }

        return results.toList()
    }

    /**
     * Khaiii 로그 레벨 지정
     * @param name Logger 이름
     * @param level Java Log Level
     */
    @JvmOverloads
    fun setLogLevel(name: KhaiiiLoggerType = KhaiiiLoggerType.all, level: Level = Level.INFO) {
        val cLevel = level.getSpdlogLevel()

        val ret = LIB.khaiii_set_log_level(name.toString(), cLevel)
        if (ret < 0)
            throw getLastError()
    }

    /**
     * Khaiii 로그 레벨을 여러개 지정합니다.
     * @param levelPairs (Logger 이름, Java Log Level)의 순서쌍들 (가변인자)
     */
    fun setLogLevels(vararg levelPairs: Pair<KhaiiiLoggerType, Level>) {
        val ret = LIB.khaiii_set_log_levels(levelPairs.joinToString(",") { "${it.first}:${it.second.getSpdlogLevel()}" })
        if (ret < 0)
            throw getLastError()
    }

    /**
     * 마지막 오류를 Exception으로 감싸서 반환
     */
    private fun getLastError() = synchronized(this) {
        Exception(LIB.khaiii_last_error(this.handle))
    }

    /**
     * Finalize시 Close하도록 처리
     */
    @Suppress("ProtectedInFinal", "Unused")
    protected fun finalize() {
        close()
    }

    /**
     * Khaiii의 Companion object
     */
    companion object {
        /**
         * Khaiii Shared Object link (JNA)
         */
        private val LIB: KhaiiiLibrary by lazy {
            synchronized(Khaiii) {
                // 인코딩 지정
                System.setProperty("jna.encoding", "UTF-8")
                try {
                    Native.load("libkhaiii", KhaiiiLibrary::class.java) as KhaiiiLibrary
                } catch (e: Exception) {
                    throw UnsatisfiedLinkError(
                            """Khaiii 라이브러리 파일 (libkhaiii.so/libkhaiii.dylib)를 시스템에서 찾을 수 없습니다.
                            |Khaiii는 C++로 구현되어 KoalaNLP는 이를 자동으로 설치하지 않습니다.
                            |Khaiii가 설치되어 있는지, LD_LIBRARY_PATH 등의 환경 변수가 제대로 설정되었는지 확인해주세요. \" +\n" +
                            |설치가 필요한 경우, 설치법은 https://github.com/kakao/khaiii/blob/v0.1/doc/setup.md 에서 확인해주세요.""".trimMargin())
                }
            }
        }
    }
}

/**
 * Khaiii 품사 분석 API의 Wrapper입니다.
 *
 * * 이 Wrapper는 아직 실험 단계입니다.
 *
 * * Khaiii Shared Object가 이미 설치되어 있어야 합니다. (자동설치 하지 않으며, 설치 방법은
 *   [Khaiii:설치](https://github.com/kakao/khaiii/blob/v0.1/doc/setup.md)를 참조하세요.)
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
 * @param resourceDirectory 리소스가 저장된 위치입니다. 플랫폼의 기본값을 사용할 경우 "". (기본값 "")
 * @param option Khaiii 분석 과정에서 사용할 설정입니다.
 */
class Tagger(resourceDirectory: String = "", option: KhaiiiConfig = KhaiiiConfig()) : CanTagOnlyASentence<KhaiiiWord?>() {

    val khaiii by lazy {
        val api = Khaiii(resourceDirectory, option)
        assert(api.version() == "0.1", {
            "API가 변화하고 있어, 0.1버전만 지원합니다. ${api.version()} 버전은 아직 모르니 issue를 등록해주세요!"
        })
        api
    }


    /**
     * [KhaiiiWord] 타입의 분석결과 [result]를 변환, [Sentence]를 구성합니다.
     *
     * @since 2.0.0
     * @param text 품사분석을 수행한 문단의 String입니다.
     * @param result 변환할 분석결과.
     * @return 변환된 [Sentence] 객체
     */
    override fun convertSentence(text: String, result: KhaiiiWord?): Sentence {
        val alignment = text.byteAlignment()

        val words = mutableListOf<Word>()
        var wordPt: KhaiiiWord? = result

        while (wordPt != null) {
            val alignBegin = wordPt.begin ?: 0
            val alignLength = wordPt.length ?: 1
            val beginAt = alignment[alignBegin]
            val endAt = alignment[maxOf(alignBegin + alignLength - 1, 0)] + 1
            val surface = text.substring(beginAt, endAt)
            val morphemes = mutableListOf<Morpheme>()

            var morphPt = wordPt.morphs
            while (morphPt != null) {
                val lex = morphPt.lex ?: ""
                val tag = morphPt.tag

                morphemes.add(Morpheme(lex, tag.toSejongPOS(), tag))
                morphPt = morphPt.next
            }

            words.add(Word(surface, morphemes.toList()))
            wordPt = wordPt.next
        }

        return Sentence(words.toList())
    }

    /**
     * 변환되지않은, [text]의 분석결과 [KhaiiiWord]를 반환합니다.
     *
     * @since 2.0.0
     * @param text 분석할 String.
     * @return 원본 분석기의 결과인 문장 1개의 첫 어절.
     */
    override fun tagSentenceOriginal(text: String): KhaiiiWord? = if (text.isBlank()) null else khaiii.analyze(text)

    /**
     * Khaiii를 닫을 경우, finalize 단계에서 close하여 handle을 너무 많이 열지 않도록 관리합니다.
     */
    @Suppress("ProtectedInFinal", "Unused")
    protected fun finalize() {
        khaiii.close()
    }
}
