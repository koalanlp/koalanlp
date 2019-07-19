package kr.bydelta.koala.rhino

import kr.bydelta.koala.Examples
import kr.bydelta.koala.TaggerSpek
import org.amshove.kluent.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import rhino.FileAnalyzer
import rhino.MainClass
import rhino.PreProcess

object RhinoOriginTaggerTest : Spek(TaggerSpek(
        getTagger = { Tagger() },
        tagSentByOrig = {
            val rhino = MainClass(it, DictionaryReader.combiMethods_List,
                    DictionaryReader.endingMethods_List, DictionaryReader.complexStem_MethodDeleted,
                    DictionaryReader.stem_MethodDeleted, DictionaryReader.afterNumber_MethodDeleted,
                    DictionaryReader.ending_MethodDeleted, DictionaryReader.stem_List,
                    DictionaryReader.ending_List, DictionaryReader.afterNumber_List,
                    DictionaryReader.nonEndingList, DictionaryReader.aspgStem, DictionaryReader.aspgEnding)
            val tagged = rhino.GetOutput().trim()

            "" to tagged
        },
        tagSentByKoala = { str, tagger ->
            val tagged = tagger.tagSentence(str)
            val tag = tagged.filterNot { it.any { m -> m.originalTag.isNullOrEmpty() } }.joinToString("\r\n") { word ->
                word.surface + "\t" + word.joinToString(" + ") { m -> m.surface + "/" + m.originalTag }
            }
            val surface = tagged.surfaceString()

            surface to tag
        },
        tagParaByOrig = { emptyList() },
        isSentenceSplitterImplemented = true
))

object RhinoOriginDictionaryReaderTest : Spek({
    defaultTimeout = 300000L  // 5 min

    val fa = FileAnalyzer("./src/main/resources/rhino/")

    describe("DictionaryReader") {
        it("read the same method") {
            fa.MakeMethodsList("rhino.lexicon.combi.combi") shouldContainSame DictionaryReader.combiMethods_List
            fa.MakeMethodsList("rhino.lexicon.ending.ending") shouldContainSame DictionaryReader.endingMethods_List
        }

        it("read complexStem_MethodDeleted") {
            fa.MakeFileArray("complexStem_MethodDeleted.txt").zip(DictionaryReader.complexStem_MethodDeleted).forEach {
                it.first `should be equal to` it.second
            }
        }
        it("read stem_MethodDeleted") {
            fa.MakeFileArray("stem_MethodDeleted.txt").zip(DictionaryReader.stem_MethodDeleted).forEach {
                it.first `should be equal to` it.second
            }
        }
        it("read ending_MethodDeleted") {
            fa.MakeFileArray("ending_MethodDeleted.txt").zip(DictionaryReader.ending_MethodDeleted).forEach {
                it.first `should be equal to` it.second
            }
        }
        it("read afterNumber_MethodDeleted") {
            fa.MakeFileArray("afterNumber_MethodDeleted.txt").zip(DictionaryReader.afterNumber_MethodDeleted).forEach {
                it.first `should be equal to` it.second
            }
        }
        it("read stem_List") {
            fa.MakeFile2DArray("stem_List.txt").zip(DictionaryReader.stem_List).forEach { pair ->
                pair.first.zip(pair.second).forEach {
                    it.first `should equal` it.second
                }
            }
        }
        it("read endingList") {
            fa.MakeFile2DArray("ending_List.txt").zip(DictionaryReader.ending_List).forEach { pair ->
                pair.first.zip(pair.second).forEach {
                    it.first `should equal` it.second
                }
            }
        }
        it("read afterNumberList") {
            fa.MakeFile2DArray("afterNumber_List.txt").zip(DictionaryReader.afterNumber_List).forEach { pair ->
                pair.first.zip(pair.second).forEach {
                    it.first `should equal` it.second
                }
            }
        }
        it("read nonEnding") {
            fa.MakeFile2DArray("_auto_managed_nonEndingList.txt").zip(DictionaryReader.nonEndingList).forEach { pair ->
                pair.first.zip(pair.second).forEach {
                    it.first `should equal` it.second
                }
            }
        }
        it("read Num:stem_List") {
            fa.GetAspNum("stem_List.txt").zip(DictionaryReader.aspgStem).forEach {
                it.first `should be equal to` it.second
            }
        }
        it("read Num:ending_List") {
            fa.GetAspNum("ending_List.txt").zip(DictionaryReader.aspgEnding).forEach {
                it.first `should be equal to` it.second
            }
        }
    }
})


object RhinoOriginExtraTest : Spek({
    defaultTimeout = 300000L  // 5 min

    describe("RHINOTagger") {
        it("split sentence as the same way") {
            Examples.exampleSequence().forEach { pair ->
                val sent = pair.second
                val preproc = PreProcess(sent).GetOutput()
                val implemented = RHINOTagger.split(sent)

                implemented.filter { it.second }.joinToString("//") { it.first } `should be equal to` preproc.joinToString("//")
            }
        }
    }
})

object RhinoCLITest : Spek({
    defaultTimeout = 300000L  // 5 min

    describe("RhinoTagger") {
        it("tag sentences") {
            { Tagger() } `should not throw` AnyException

            val tagger = Tagger()

            Examples.exampleSequence().forEach {
                val sent = it.second
                { tagger.tag(sent) } `should not throw` AnyException

                if (it.first == 1) {
                    { tagger.tagSentence(sent) } `should not throw` AnyException
                }
            }
        }
    }
})
