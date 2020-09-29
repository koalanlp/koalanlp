package kr.bydelta.koala.rhino

import kr.bydelta.koala.Examples
import kr.bydelta.koala.TaggerSpek
import org.amshove.kluent.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import rhino.RHINO

object RhinoOriginTaggerTest : Spek(TaggerSpek(
        getTagger = { Tagger() },
        tagSentByOrig = {
            // Initialize resources
            RHINOTagger.tagger

            val rhino = RHINO()
            val tagged = rhino.ExternCall(it).trim().replace(" ", "")

            "" to tagged
        },
        tagSentByKoala = { str, tagger ->
            val tagged = tagger.tagSentence(str)
            val tag = tagged.filterNot { it.any { m -> m.originalTag.isNullOrEmpty() } }.joinToString("\r\n") { word ->
                word.surface + "\t" + word.joinToString("+") { m -> m.surface + "/" + m.originalTag }
            }
            val surface = tagged.surfaceString()

            surface to tag
        },
        tagParaByOrig = { emptyList() },
        isSentenceSplitterImplemented = true
))