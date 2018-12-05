package kr.bydelta.koala.test

import kr.bydelta.koala.Conversion
import kr.bydelta.koala.POS
import kr.bydelta.koala.TagConversionSpek
import kr.bydelta.koala.TaggerSpek
import kr.bydelta.koala.khaiii.*
import org.spekframework.spek2.Spek

val KHAIII_RSC: String by lazy {
    System.setProperty("jna.library.path", System.getenv("KHAIII_LIB") ?: "")
    println("Set jna.library.path = ${System.getProperty("jna.library.path")}")

    val rsc = System.getenv("KHAIII_RSC") ?: ""
    println("Set KHAIII_RSC = $rsc")

    rsc
}

object KhaiiiTaggerTest : Spek(TaggerSpek(getTagger = { Tagger(KHAIII_RSC) },
        tagSentByOrig = {
            val khaiii = Khaiii(KHAIII_RSC)
            val original = khaiii.analyze(it)

            getOriginalTaggerOutput(original, it)
        }, tagParaByOrig = { emptyList() },
        tagSentByKoala = { str, tagger ->
            val tagged = tagger.tagSentence(str)
            val tag = tagged.joinToString(" ") { w -> w.joinToString("+") { "${it.surface}/${it.originalTag}" } }

            tagged.surfaceString() to tag
        }, isSentenceSplitterImplemented = true))


private fun getOriginalTaggerOutput(original: KhaiiiWord?, it: String): Pair<String, String> {
    val alignments = it.byteAlignment()
    val tag = StringBuffer()
    val surface = StringBuffer()
    var word = original

    while (word != null) {
        val alignBegin = word.begin ?: 0
        val alignLength = word.length ?: 1
        val begin = alignments[alignBegin]
        val end = alignments[maxOf(alignBegin + alignLength - 1, 0)] + 1
        surface.append(it.substring(begin, end))

        var morph = word.morphs
        while (morph != null) {
            val morphSurface = morph.lex
            val oTag = morph.tag
            tag.append("$morphSurface/$oTag")

            if (morph.next != null) {
                tag.append('+')
            }
            morph = morph.next
        }

        if (word.next != null) {
            surface.append(' ')
            tag.append(' ')
        }
        word = word.next
    }
    return Pair(surface.toString(), tag.toString())
}


object KhaiiiTagConversionTest : Spek(TagConversionSpek(from = { it.toSejongPOS() },
        to = { it.fromSejongPOS() },
        getMapping = {
            when (it) {
                POS.NA -> listOf(Conversion("ZZ"))
                POS.NV -> listOf(Conversion("ZV"))
                POS.NF -> listOf(Conversion("ZN"))
                POS.SW -> listOf(Conversion("SW"), Conversion("SWK", toTagger = false))
                else -> listOf(Conversion(it.toString()))
            }
        }))
