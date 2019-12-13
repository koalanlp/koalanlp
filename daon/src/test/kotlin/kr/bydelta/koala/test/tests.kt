package kr.bydelta.koala.test

import daon.core.Daon
import kr.bydelta.koala.Conversion
import kr.bydelta.koala.POS
import kr.bydelta.koala.TagConversionSpek
import kr.bydelta.koala.TaggerSpek
import kr.bydelta.koala.daon.Tagger
import kr.bydelta.koala.daon.fromSejongPOS
import kr.bydelta.koala.daon.toSejongPOS
import org.spekframework.spek2.Spek

object DaonTagConversionTest : Spek(TagConversionSpek(
        from = { it.toSejongPOS() },
        to = { it.fromSejongPOS() },
        getMapping = {
            when (it) {
                POS.NNM -> listOf(Conversion("NNB", toSejong = false, toTagger = true))
                POS.XSM -> listOf(Conversion("XSB"))
                POS.XSO -> emptyList()
                POS.NA -> listOf(Conversion("NA"), Conversion("UNKNOWN", toTagger = false))
                else -> listOf(Conversion(it.toString()))
            }
        }
))

object DaonTaggerTest : Spek(TaggerSpek(
        getTagger = { Tagger() },
        tagSentByOrig = {
            val daon = Daon()
            val analyzed = daon.analyze(it)

            val surface = analyzed.joinToString(" ") { s -> s.surface }
            val tagged = analyzed.joinToString(" ") { e ->
                e.morphemes.joinToString("+") { m ->
                    "${m.word}/${m.tag}"
                }
            }
            surface to tagged
        },
        tagParaByOrig = { emptyList() },
        isSentenceSplitterImplemented = true
))