package kr.bydelta.koala

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object ProcessorSpec : Spek({
    describe("CanSplitSentence Interface") {
        // sentences
    }

    describe("SentenceSplitter") {
        // invoke
    }

    describe("CanTagASentence Interface") {
        // tagSentence
        // tag
        // invoke
    }

    describe("CanTagOnlyASentence Interface") {
        // tagSentence
        // tag
        // invoke
    }

    describe("CanTagAParagraph Interface") {
        // tagSentence
        // tag
        // invoke
    }

    describe("CanTagOnlyAParagraph Interface") {
        val tagger = object : CanTagOnlyAParagraph<List<String>>() {
            override fun tagParagraphOriginal(text: String): List<List<String>> {
                return text.split("\n").map { it.split(" ").toList() }.toList()
            }

            override fun convertSentence(result: List<String>): Sentence {
                return Sentence(result.map {
                    Word(it, listOf(Morpheme(it, POS.NNP, "NNP")))
                })
            }
        }

        // tagSentence
        it("tag a sentence") {
            val sent = tagger.tagSentence("이것은 테스트 문장입니다.")
            sent.singleLineString() `should be equal to` "이것은/NNP 테스트/NNP 문장입니다./NNP"
            sent.size `should be equal to` 3

            tagger.tagSentence("").isEmpty() `should be` true
        }

        // tag
        // invoke
        it("tag a paragraph") {
            val sent = tagger.tag("이것은 테스트 문장입니다.\n저것은 설렘입니다.")
            sent[0].singleLineString() `should be equal to` "이것은/NNP 테스트/NNP 문장입니다./NNP"
            sent[0].size `should be equal to` 3
            sent[1].singleLineString() `should be equal to` "저것은/NNP 설렘입니다./NNP"
            sent[1].size `should be equal to` 2

            tagger.tag("").isEmpty() `should be` true
            tagger("행복하신가요?") `should equal` tagger.tag("행복하신가요?")
        }
    }

    describe("CanAnalyzeSentenceProperty Interface") {
        // parse, invoke, attachProperty
    }

    describe("CanDisambiguateSense Interface") {
        // parse, invoke, attachProperty
    }
})