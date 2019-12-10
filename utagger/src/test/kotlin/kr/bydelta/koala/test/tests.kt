package kr.bydelta.koala.test

import com.sun.jna.Platform
import kr.bydelta.koala.POS
import kr.bydelta.koala.Conversion
import kr.bydelta.koala.TaggerSpek
import kr.bydelta.koala.TagConversionSpek
import kr.bydelta.koala.utagger.Tagger
import kr.bydelta.koala.utagger.UTagger
import org.spekframework.spek2.Spek
import java.io.File
import java.nio.charset.Charset

private var UTAGGER_INIT: UTagger? = null

private fun initUTagger(): UTagger {
    if (UTAGGER_INIT == null) {
        val utaggerPath = File(System.getenv("HOME"), "utagger")
        val binPath = File(utaggerPath, "bin")
        val DISTRO_IS_UBUNTU: Boolean by lazy {
            val process = Runtime.getRuntime().exec("uname -a")
            val output = process.inputStream.bufferedReader().lineSequence().joinToString()

            "ubuntu" in output.toLowerCase()
        }

        val libPath = if (Platform.isWindows() && Platform.is64Bit()) "utagger-win64.dll"
        else if (Platform.isLinux()) {
            if (DISTRO_IS_UBUNTU) "utagger-ubuntu1804.so"
            else "utagger-centos7.so"
        } else throw IllegalStateException("Windows 64bit, Ubuntu, CentOS 계열 외에는 UTagger API가 제공되지 않습니다.")

        // UTagger 라이브러리 위치 지정
        val libraryPath = File(binPath, libPath)
        val configPath = File(utaggerPath, "Hlxcfg.txt")
        UTagger.setPath(libraryPath.toString(), configPath.toString())

        // 라이브러리 설정파일 수정
        val charset = Charset.forName("euc-kr")

        val linesToWrite = configPath.inputStream().bufferedReader(charset).useLines { lines ->
            lines.map {
                if (it.startsWith("HLX_DIR"))
                    it.replace("HLX_DIR ../", "HLX_DIR ${utaggerPath.absolutePath}/")
                else
                    it
            }.toList()
        }

        val writer = configPath.outputStream().bufferedWriter(charset)
        writer.write(linesToWrite.joinToString("\n"))
        writer.close()

        // UTagger 생성
        UTAGGER_INIT = UTagger()
    }

    return UTAGGER_INIT!!
}


object UTaggerTest : Spek(TaggerSpek(
        getTagger = {
            initUTagger()
            Tagger()
        },
        tagSentByOrig = { str ->
            val tagger = initUTagger()
            val original = tagger.analyze(str)

            val surface = original!!.joinToString(" ") { it.surface }
            val tag = original.joinToString(" ") { w -> w.morphemes.joinToString("+") { "${it.surface}/${it.tag}" } }
            surface to tag
        }, tagParaByOrig = { emptyList() },
        tagSentByKoala = { str, tagger ->
            val tagged = tagger.tagSentence(str)
            val tag = tagged.joinToString(" ") { w -> w.joinToString("+") { "${it.surface}/${it.originalTag}" } }

            tagged.surfaceString() to tag
        }, isSentenceSplitterImplemented = true))


// Dummy test for test filtering.
object UTaggerTagConversionTest : Spek(TagConversionSpek(
        from = {
            if (it != null) POS.valueOf(it)
            else POS.NA
        }, to = { it.toString() },
        getMapping = { listOf(Conversion(it.toString())) }))
