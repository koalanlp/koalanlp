package kr.bydelta.koala.test

import kr.bydelta.koala.*
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.eunjeon.*
import org.bitbucket.eunjeon.seunjeon.Analyzer
import org.spekframework.spek2.Spek

object EunjeonTagConversionTest : Spek(TagConversionSpek(from = { it.toSejongPOS() },
        to = { it.fromSejongPOS() },
        getMapping = {
            when (it) {
                POS.NNM -> listOf(Conversion("NNBC"))
                POS.XPV -> listOf(Conversion("XR", toSejong = false))
                POS.XSM, POS.XSO -> emptyList()
                POS.SS -> listOf(Conversion("SSO"), Conversion("SSC", toTagger = false))
                POS.SW -> listOf(Conversion("SY"))
                POS.SO -> listOf(Conversion("SY", toSejong = false))
                POS.NF, POS.NV -> listOf(Conversion("UNKNOWN", toSejong = false))
                POS.NA -> listOf(Conversion("UNKNOWN"))
                POS.SP -> listOf(Conversion("SC"))
                else -> listOf(Conversion(it.toString()))
            }
        }))

object EunjeonDictionaryTest : Spek(DictSpek(Dictionary, getTagger = { Tagger() }))

object EunjeonTaggerTest : Spek(TaggerSpek(getTagger = { Tagger() },
        isSentenceSplitterImplemented = true,
        tagSentByOrig = { str ->
            val original = Analyzer.parseEojeol(str).asJava()
            val tag = original.joinToString(" ") {
                it.nodesJava().joinToString("+") { e ->
                    val arr = e.morpheme().feature.split(",").last()
                    if (arr == "*") e.morpheme().surface
                    else arr.replace("([^/]+)/[^+]+".toRegex(), "$1")
                }
            }

            val surface = original.joinToString(" ") { it.surface() }

            surface to tag
        },
        tagSentByKoala = { str, tagger ->
            val tagged = Sentence(tagger.tag(str).flatten())
            val tag = tagged.joinToString(" ") { it.joinToString("+") { m -> m.surface } }
            val surface = tagged.surfaceString()

            surface to tag
        },
        tagParaByOrig = { emptyList() }
))