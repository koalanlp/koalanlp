package kr.bydelta.koala.test

import kr.bydelta.koala.*
import kr.bydelta.koala.kmr.Dictionary
import kr.bydelta.koala.kmr.Tagger
import kr.bydelta.koala.kmr.fromSejongPOS
import kr.bydelta.koala.kmr.toSejongPOS
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran
import org.spekframework.spek2.Spek

object KomoranTaggerTest : Spek(TaggerSpek(getTagger = { Tagger() },
        tagSentByOrig = {
            val komoran = Komoran(DEFAULT_MODEL.FULL)
            val original = komoran.analyze(it)
            val tag = StringBuffer()
            var prev = 0

            for (token in original.tokenList) {
                if (token.beginIndex > prev) {
                    tag.append(" ")
                }
                tag.append("${token.morph}/${token.pos}")
                prev = token.endIndex
            }

            "" to tag.toString()
        }, tagParaByOrig = { emptyList() },
        tagSentByKoala = { str, tagger ->
            val tagged = tagger.tagSentence(str)
            val tag = tagged.joinToString(" ") { w -> w.joinToString("") { "${it.surface}/${it.originalTag}" } }

            str to tag
        }, isSentenceSplitterImplemented = true))

object KomoranDictTest : Spek(DictSpek(dict = Dictionary))

object KomoranTagConversionTest : Spek(TagConversionSpek(from = { it.toSejongPOS() },
        to = { it.fromSejongPOS() },
        getMapping = {
            when (it) {
                POS.NNM -> listOf(Conversion("NNB", toSejong = false))
                POS.XPV -> listOf(Conversion("XR", toSejong = false))
                POS.XSM, POS.XSO -> emptyList()
                POS.NF, POS.NV -> listOf(Conversion("NA", toSejong = false))
                else -> listOf(Conversion(it.toString()))
            }
        }))