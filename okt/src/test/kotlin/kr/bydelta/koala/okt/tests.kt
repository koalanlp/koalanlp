package kr.bydelta.koala.okt

import kr.bydelta.koala.*
import org.openkoreantext.processor.OpenKoreanTextProcessor
import org.openkoreantext.processor.util.KoreanPos
import org.spekframework.spek2.Spek

object OKTSplitterTest : Spek(SplitterSpek(getSplitter = { SentenceSplitter() },
        originalSplitCounter = { para ->
            OpenKoreanTextProcessor.splitSentences(para).length()
        }))

object OKTDictionaryTest : Spek(DictSpek(Dictionary, getTagger = { Tagger() },
        verbOn = false, importFilter = { !it.isPredicate() }))

object OKTTagConversionTest : Spek(TagConversionSpek(from = { it.toSejongPOS() },
        to = { it.fromSejongPOS() },
        getMapping = {
            when (it) {
                POS.NNG -> listOf(Conversion("Noun"))
                POS.NNP -> listOf(Conversion("ProperNoun"))
                POS.NNB, POS.NNM, POS.NP -> listOf(Conversion("Noun", toSejong = false))
                POS.NR -> listOf(Conversion("Number"))
                POS.VV -> listOf(Conversion("Verb"))
                POS.VA -> listOf(Conversion("Adjective"))
                POS.VX, POS.VCP, POS.VCN -> listOf(Conversion("Verb", toSejong = false))
                POS.MM -> listOf(Conversion("Modifier"), Conversion("Determiner", toTagger = false))
                POS.MAG -> listOf(Conversion("Adverb"))
                POS.MAJ -> listOf(Conversion("Adverb", toSejong = false))
                POS.IC -> listOf(Conversion("Exclamation"))
                POS.JKS, POS.JKC, POS.JKG, POS.JKO, POS.JKB, POS.JKV, POS.JKQ -> listOf(Conversion("Josa", toSejong = false))
                POS.JC -> listOf(Conversion("Conjunction"))
                POS.JX -> listOf(Conversion("Josa"))
                POS.EP -> listOf(Conversion("PreEomi"))
                POS.EF -> listOf(Conversion("Eomi"))
                POS.EC, POS.ETN, POS.ETM -> listOf(Conversion("Eomi", toSejong = false))
                POS.NF, POS.NV, POS.XPN -> listOf(Conversion("Unknown", toSejong = false))
                POS.XPV -> listOf(Conversion("VerbPrefix"))
                POS.XSN, POS.XSV, POS.XSA, POS.XSM -> listOf(Conversion("Suffix", toSejong = false))
                POS.XSO -> listOf(Conversion("Suffix"))
                POS.XR -> emptyList()
                POS.SF -> listOf(Conversion("Punctuation"))
                POS.SP, POS.SS, POS.SE, POS.SO -> listOf(Conversion("Others", toSejong = false))
                POS.SW -> listOf(Conversion("Others"), Conversion("CashTag", toTagger = false))
                POS.NA -> listOf(Conversion("Unknown"))
                POS.SL -> listOf(Conversion("Foreign"), Conversion("Alpha", toTagger = false))
                POS.SH -> listOf(Conversion("Foreign", toSejong = false))
                POS.SN -> listOf(Conversion("Number", toSejong = false))
                else -> emptyList()
            }
        }))

object OKTTaggerTest : Spek(TaggerSpek(
        getTagger = { Tagger() },
        tagParaByOrig = {
            val sentences = OpenKoreanTextProcessor.splitSentences(it).asJava().map { it.text() }
            val original = sentences.map { s -> OpenKoreanTextProcessor.tokenize(OpenKoreanTextProcessor.normalize(s)) }

            original.map { s ->
                s.asJava().joinToString(" ") { it.text() }
                        .replace("[ ]{2,}".toRegex(), "##").replace(" ", "+").replace("##", " ")
            }
        },
        tagSentByOrig = {
            val original = OpenKoreanTextProcessor.tokenize(OpenKoreanTextProcessor.normalize(it)).asJava()

            val tag = original.joinToString(" ") { w ->
                if (w.pos() != KoreanPos.Space()) w.text() + "/" + w.pos()
                else w.text()
            }.replace("[ ]{2,}".toRegex(), "##").replace(" ", "+").replace("##", " ")

            "" to tag
        },
        isSentenceSplitterImplemented = false
))