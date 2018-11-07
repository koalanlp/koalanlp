package kr.bydelta.koala.test

import kr.bydelta.koala.DictSpek
import kr.bydelta.koala.TaggerSpek
import kr.bydelta.koala.arirang.Dictionary
import kr.bydelta.koala.arirang.Tagger
import org.apache.lucene.analysis.ko.morph.AnalysisOutput
import org.apache.lucene.analysis.ko.morph.WordSegmentAnalyzer
import org.spekframework.spek2.Spek

class ArirangDictTest : Spek(DictSpek(Dictionary, getTagger = { Tagger() }))

class ArirangTaggerTest : Spek(TaggerSpek(getTagger = { Tagger() },
        tagParaByOrig = { emptyList() },
        tagSentByOrig = {
            val list = mutableListOf<MutableList<AnalysisOutput>>()
            WordSegmentAnalyzer().analyze(it, list, false)
            val original = list.map { s -> s.maxBy { w -> w.score } }

            val tag = original.joinToString("") { s -> s.toString() }
                    .replace("[NVZ\\s.,()]+".toRegex(), "")

            "" to tag
        },
        tagSentByKoala = { str, tagger ->
            val tagged = tagger.tagSentence(str)
            val tag = tagged.joinToString("") { w ->
                w.joinToString(",") { m -> m.surface + "(" + m.originalTag!!.last() + ")" }
            }.replace("[NVZ\\s.,()]+".toRegex(), "")
            val surface = tagged.surfaceString()

            surface to tag
        },
        isParagraphImplemented = false,
        isSentenceSplitterImplemented = true
))