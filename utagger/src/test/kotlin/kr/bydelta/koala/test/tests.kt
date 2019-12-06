package kr.bydelta.koala.test

import com.sun.jna.Platform
import kr.bydelta.koala.TaggerSpek
import kr.bydelta.koala.utagger.Tagger
import kr.bydelta.koala.utagger.UTagger
import org.spekframework.spek2.Spek
import java.io.File
import java.nio.charset.Charset
import kotlin.streams.toList

val DISTRO_IS_UBUNTU: Boolean by lazy {
    val process = Runtime.getRuntime().exec("uname -a")
    val output = process.inputStream.bufferedReader().lines().toList().joinToString()

    "ubuntu" in output.toLowerCase()
}

val UTAGGER_INIT: UTagger by lazy {
    val utaggerPath = File(System.getenv("HOME"), "utagger")
    val binPath = File(utaggerPath, "bin")

    val libPath = if (Platform.isWindows() && Platform.is64Bit()) "UTaggerR64.dll"
    else if (Platform.isLinux()) {
        if (DISTRO_IS_UBUNTU) "UTagger.so"
        else "UTagger centos.so"
    } else throw IllegalStateException("Windows 64bit, Ubuntu, CentOS 계열 외에는 UTagger API가 제공되지 않습니다.")

    // UTagger 라이브러리 위치 지정
    val libraryPath = File(binPath, libPath)
    val configPath = File(utaggerPath, "Hlxcfg.txt")
    UTagger.setPath(libraryPath.toString(), configPath.toString())

    // 라이브러리 설정파일 수정
    val charset = Charset.forName("euc-kr")

    val lines = configPath.inputStream().bufferedReader(charset).lines().toList()
    val writer = configPath.outputStream().bufferedWriter(charset)

    lines.forEach {
        if (it.startsWith("HLX_DIR"))
            writer.write(it.replace("HLX_DIR ../", "HLX_DIR ${utaggerPath.absolutePath}/"))
        else
            writer.write(it)
        writer.newLine()
    }

    writer.close()

    // UTagger 생성
    UTagger()
}


object UTaggerTaggerTest : Spek(TaggerSpek(getTagger = { Tagger() },
        tagSentByOrig = { str ->
            val tagger = UTAGGER_INIT
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
