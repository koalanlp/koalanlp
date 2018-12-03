package kr.bydelta.koala.khaiii

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Structure
import kr.bydelta.koala.data.Morpheme
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.data.Word
import kr.bydelta.koala.proc.CanTagOnlyASentence
import toSejongPOS
import java.util.logging.Logger

@Structure.FieldOrder("lex", "tag", "begin", "length", "reserved", "next")
class KhaiiiMorph : Structure() {
    /** Lexical (형태소 원형) */
    var lex: String? = null;
    /** POS Tag */
    var tag: String? = null;
    /** 형태소 시작 위치 */
    var begin: Int? = null;
    /** 형태소 길이 */
    var length: Int? = null;
    /** (예약) */
    var reserved: CharArray? = null;
    /** 다음 형태소 포인터 */
    var next: KhaiiiMorph? = null;
}

@Structure.FieldOrder("begin", "length", "reserved", "morphs", "next")
class KhaiiiWord : Structure() {
    /** 어절 시작 위치 */
    var begin: Int? = null;
    /** 어절 길이 **/
    var length: Int? = null;
    /** (예약) */
    var reserved: CharArray? = null;
    /** 형태소 목록 (첫 형태소 포인터) */
    var morphs: KhaiiiMorph? = null;
    /** 다음 어절 포인터 */
    var next: KhaiiiWord? = null;
}

interface KhaiiiLibrary : Library {
    fun khaiii_version(): String
    fun khaiii_open(rscDir: String = "", optStr: String = ""): Int
    fun khaiii_analyze(handle: Int, inStr: String = "", optStr: String = ""): KhaiiiWord?
    fun khaiii_free_results(handle: Int, results: KhaiiiWord)
    fun khaiii_close(handle: Int)
    fun khaiii_last_error(handle: Int): String
    fun khaiii_analyze_bfr_errorpatch(handle: Int, inStr: String = "", optStr: String = "", output: IntArray? = null): Int
    fun khaiii_set_log_level(name: String = "", level: String = ""): Int
    fun khaiii_set_log_levels(nameLevelPairs: String = ""): Int
}

/**
 * 원본코드: Khaiii Python package
 */
class Khaiii(val rscDir: String = "/usr/local/share/khaiii", val optStr: String = "") {
    val logger = Logger.getLogger("KhaiiiLibrary");
    var handle: Int = -1;

    fun version() = LIB.khaiii_version()

    fun open() {
        this.handle = LIB.khaiii_open(this.rscDir, this.optStr);
        if (this.handle < 0)
            throw getLastError()

        logger.info("Khaiii 연결 성공: 자료는 ${rscDir}에서, '$optStr' 설정으로 연결")
    }

    fun close() {
        if (this.handle >= 0) {
            LIB.khaiii_close(this.handle)
            logger.fine("Khaiii 연결 닫힘")
        }

        this.handle = -1;
    }

    fun analyze(inStr: String = "", optStr: String = ""): KhaiiiWord {
        if (this.handle < 0) open()

        val results = LIB.khaiii_analyze(this.handle, inStr, optStr) ?: throw getLastError()
        LIB.khaiii_free_results(this.handle, results)

        return results
    }

    fun analyzeBeforeErrorPatch(inStr: String = "", optStr: String = ""): List<Int> {
        if (this.handle < 0) open()

        val output = IntArray(inStr.length)
        val outNum = LIB.khaiii_analyze_bfr_errorpatch(this.handle, inStr, optStr, output)

        if (outNum < 2)
            throw getLastError()

        val results = mutableListOf<Int>()
        for (idx in 1..outNum) {
            results.add(output[idx]);
        }

        return results.toList()
    }

    fun setLogLevel(name: String = "", level: String = "") {
        val ret = LIB.khaiii_set_log_level(name, level)
        if (ret < 0)
            throw getLastError()
    }

    fun setLogLevels(vararg levelPairs: Pair<String, String>) {
        val ret = LIB.khaiii_set_log_levels(levelPairs.joinToString(",") { "${it.first}:${it.second}" })
        if (ret < 0)
            throw getLastError()
    }

    private fun getLastError() = Exception(LIB.khaiii_last_error(this.handle))

    private fun finalize() {
        close()
    }

    companion object {
        private val LIB: KhaiiiLibrary by lazy { Native.load("libkhaiii", KhaiiiLibrary::class.java) as KhaiiiLibrary }
    }
}

class Tagger(resourceDirectory: String = "/usr/local/share/khaiii",
             option: Map<String, String> = emptyMap()) : CanTagOnlyASentence<KhaiiiWord>() {

    val khaiii by lazy {
        Khaiii(resourceDirectory,
                "{" + option.asSequence().joinToString(",") { "\"${it.key}\":\"${it.value}\"" } + "}")
    }

    /**
     * [S] 타입의 분석결과 [result]를 변환, [Sentence]를 구성합니다.
     *
     * @since 1.x
     * @param text 품사분석을 수행한 문단의 String입니다.
     * @param result 변환할 분석결과.
     * @return 변환된 [Sentence] 객체
     */
    override fun convertSentence(text: String, result: KhaiiiWord): Sentence {
        val words = mutableListOf<Word>()
        var wordPt: KhaiiiWord? = result

        while (wordPt != null) {
            val beginAt = wordPt.begin ?: 0
            val endAt = beginAt + (wordPt.length ?: 0)
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
     * 변환되지않은, [text]의 분석결과 [S]를 반환합니다.
     *
     * @since 1.x
     * @param text 분석할 String.
     * @return 원본 분석기의 결과인 문장 1개
     */
    override fun tagSentenceOriginal(text: String): KhaiiiWord = khaiii.analyze(text)
}