package kr.bydelta.koala.test

import kaist.cilab.jhannanum.common.communication.PlainSentence
import kaist.cilab.jhannanum.common.communication.Sentence
import kaist.cilab.jhannanum.common.workflow.Workflow
import kaist.cilab.jhannanum.plugin.major.morphanalyzer.impl.ChartMorphAnalyzer
import kaist.cilab.jhannanum.plugin.major.postagger.impl.HMMTagger
import kaist.cilab.jhannanum.plugin.supplement.MorphemeProcessor.UnknownMorphProcessor.UnknownProcessor
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.InformalSentenceFilter.InformalSentenceFilter
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.SentenceSegmentor.SentenceSegmentor
import kaist.cilab.parser.berkeleyadaptation.BerkeleyParserWrapper
import kaist.cilab.parser.berkeleyadaptation.Configuration
import kaist.cilab.parser.berkeleyadaptation.HanNanumMorphAnalWrapper
import kaist.cilab.parser.corpusconverter.sejong2treebank.sejongtree.NonterminalNode
import kaist.cilab.parser.corpusconverter.sejong2treebank.sejongtree.ParseTree
import kaist.cilab.parser.dependency.DNode
import kaist.cilab.parser.psg2dg.Converter
import kr.bydelta.koala.*
import kr.bydelta.koala.data.DepEdge
import kr.bydelta.koala.data.SyntaxTree
import kr.bydelta.koala.hnn.*
import kr.bydelta.koala.proc.CanParseDependency
import kr.bydelta.koala.proc.CanParseSyntax
import org.amshove.kluent.`should be equal to`
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.File

object HNNDictTest : Spek(DictSpek(Dictionary, getTagger = { Tagger() }))

val splitterFlow by lazy {
    synchronized(Configuration.hanBaseDir) {
        Configuration.hanBaseDir = "./hnnModels"
        val workflow = Workflow()
        val basePath = "./hnnModels"

        workflow.appendPlainTextProcessor(SentenceSegmentor(),
                basePath + File.separator + "conf" + File.separator + "SentenceSegment.json")
        workflow.activateWorkflow(true)
        println("SplitterFlow initialized")
        workflow
    }
}

object HNNSpliterTest : Spek(SplitterSpek(
        getSplitter = { SentenceSplitter() },
        originalSplitCounter = {
            synchronized(splitterFlow) {
                splitterFlow.analyze(it)
                var count = 0
                var isEnded = false

                while (!isEnded) {
                    val sent = splitterFlow.getResultOfSentence(PlainSentence(0, 0, false))
                    isEnded = sent.isEndOfDocument
                    count += 1
                }

                count
            }
        }
))

object HNNTagConversionTest : Spek(TagConversionSpek(
        from = { it.toSejongPOS() },
        to = { it.fromSejongPOS() },
        getMapping = {
            when (it) {
                POS.NNG ->
                    listOf(Conversion("ncpa", toTagger = false),
                            Conversion("ncps", toTagger = false),
                            Conversion("ncn"),
                            Conversion("ncr", toTagger = false))
                POS.NNP ->
                    listOf(Conversion("nqpa", toTagger = false),
                            Conversion("nqpb", toTagger = false),
                            Conversion("nqpc", toTagger = false),
                            Conversion("nqq"))
                POS.NNB -> listOf(Conversion("nbn"), Conversion("nbs", toTagger = false))
                POS.NNM -> listOf(Conversion("nbu"))
                POS.NR -> listOf(Conversion("nnc"), Conversion("nno", toTagger = false))
                POS.NP -> listOf(Conversion("npd"), Conversion("npp", toTagger = false))
                POS.VV -> listOf(Conversion("pvg"), Conversion("pvd", toTagger = false))
                POS.VA -> listOf(Conversion("paa"), Conversion("pad", toTagger = false))
                POS.VX -> listOf(Conversion("px"))
                POS.VCP -> listOf(Conversion("jp"))
                POS.VCN -> listOf(Conversion("paa", toSejong = false))
                POS.MM -> listOf(Conversion("mma"), Conversion("mmd", toTagger = false))
                POS.MAG -> listOf(Conversion("mag"), Conversion("mad", toTagger = false))
                POS.IC -> listOf(Conversion("ii"))
                POS.JKS -> listOf(Conversion("jcs"))
                POS.JKC -> listOf(Conversion("jcc"))
                POS.JKG -> listOf(Conversion("jcm"))
                POS.JKO -> listOf(Conversion("jco"))
                POS.JKB -> listOf(Conversion("jca"))
                POS.JKV -> listOf(Conversion("jcv"))
                POS.JKQ -> listOf(Conversion("jcr"))
                POS.JC -> listOf(Conversion("jct"), Conversion("jcj", toTagger = false))
                POS.JX -> listOf(Conversion("jxf"), Conversion("jxc", toTagger = false))
                POS.EC ->
                    listOf(Conversion("ecc"),
                            Conversion("ecs", toTagger = false),
                            Conversion("ecx", toTagger = false))
                POS.XPN -> listOf(Conversion("xp"))
                POS.XPV -> listOf(Conversion("xp", toSejong = false))
                POS.XSN ->
                    listOf(Conversion("xsnx"),
                            Conversion("xsnu", toTagger = false),
                            Conversion("xsna", toTagger = false),
                            Conversion("xsnca", toTagger = false),
                            Conversion("xsncc", toTagger = false),
                            Conversion("xsns", toTagger = false),
                            Conversion("xsnp", toTagger = false))
                POS.XSV ->
                    listOf(Conversion("xsvn"),
                            Conversion("xsvv", toTagger = false),
                            Conversion("xsva", toTagger = false))
                POS.XSA ->
                    listOf(Conversion("xsmn"), Conversion("xsms", toTagger = false))
                POS.XSM ->
                    listOf(Conversion("xsas"), Conversion("xsam", toTagger = false))
                POS.XSO, POS.XR, POS.NF, POS.NV, POS.NA -> emptyList()
                POS.SS -> listOf(Conversion("sl"), Conversion("sr", toTagger = false))
                POS.SO -> listOf(Conversion("sd"))
                POS.SW -> listOf(Conversion("sy"), Conversion("su", toTagger = false))
                POS.SL -> listOf(Conversion("f"))
                POS.SH -> listOf(Conversion("f", toSejong = false))
                POS.SN -> listOf(Conversion("nnc", toSejong = false))
                else -> listOf(Conversion(it.toString().toLowerCase()))
            }
        }
))

val taggerWorkflow by lazy {
    synchronized(Configuration.hanBaseDir) {
        Configuration.hanBaseDir = "./hnnModels"
        val workflow = Workflow()
        val basePath = "./hnnModels"

        workflow.appendPlainTextProcessor(SentenceSegmentor(),
                basePath + File.separator + "conf" + File.separator + "SentenceSegment.json")
        workflow.appendPlainTextProcessor(InformalSentenceFilter(),
                basePath + File.separator + "conf" + File.separator + "InformalSentenceFilter.json")

        workflow.setMorphAnalyzer(ChartMorphAnalyzer(),
                basePath + File.separator + "conf" + File.separator + "ChartMorphAnalyzer.json", "./hnnModels/")
        workflow.appendMorphemeProcessor(UnknownProcessor(),
                basePath + File.separator + "conf" + File.separator + "UnknownMorphProcessor.json")

        workflow.setPosTagger(HMMTagger(),
                basePath + File.separator + "conf" + File.separator + "HmmPosTagger.json", "./hnnModels/")
        workflow.activateWorkflow(true)
        workflow
    }
}

object HNNTaggerTest : Spek(TaggerSpek(
        getTagger = { Tagger() },
        isSentenceSplitterImplemented = false,
        tagSentByOrig = {
            synchronized(taggerWorkflow) {
                taggerWorkflow.analyze(it)
                val original = mutableListOf<Sentence>()
                var isEnded = false

                while (!isEnded) {
                    val sent = taggerWorkflow.getResultOfSentence(Sentence(0, 0, false))
                    isEnded = sent.isEndOfDocument
                    original.add(sent)
                }

                val tag = original.flatMap { s -> s.eojeols.toList() }
                        .joinToString(" ") { e ->
                            e.morphemes.zip(e.tags).joinToString("+") { pair -> "${pair.first}/${pair.second}" }
                        }

                val surface = original.flatMap { s -> s.plainEojeols.toList() }.joinToString(" ")
                surface to tag
            }
        },
        tagParaByOrig = {
            synchronized(taggerWorkflow) {
                taggerWorkflow.analyze(it)
                val original = mutableListOf<Sentence>()
                var isEnded = false

                while (!isEnded) {
                    val sent = taggerWorkflow.getResultOfSentence(Sentence(0, 0, false))
                    isEnded = sent.isEndOfDocument
                    original.add(sent)
                }

                original.map { s -> s.eojeols.joinToString(" ") { m -> m.morphemes.joinToString("+") } }
            }
        }
))

val parser by lazy {
    Configuration.parserModel = "./hnnModels/models/parser/KorGrammar_BerkF_ORIG"
    Configuration.hanBaseDir = "./hnnModels/"
    BerkeleyParserWrapper(Configuration.parserModel)
}

fun SyntaxTree.getDFSString(buffer: StringBuffer, depth: Int = 0): StringBuffer {
    buffer.append("\n")
    buffer.append("| ".repeat(depth))
    buffer.append("${this.originalLabel}")
    if (this.terminal != null) {
        buffer.append("(")
        buffer.append(this.terminal!!.joinToString("+") { it.originalTag!! })
        buffer.append(")")
    } else {
        for (child in this) {
            child.getDFSString(buffer, depth + 1)
        }
    }
    return buffer
}

fun NonterminalNode.getDFSString(buffer: StringBuffer, depth: Int = 0): StringBuffer {
    buffer.append("\n")
    buffer.append("| ".repeat(depth))
    buffer.append(this.phraseTag.split("-")[0])
    if (this in this.tree.prePreTerminalNodes) {
        buffer.append("(")
        buffer.append(this.myTerminals!!.joinToString("+") { it.pos })
        buffer.append(")")
    } else {
        for (child in this.children) {
            (child as NonterminalNode).getDFSString(buffer, depth + 1)
        }
    }
    return buffer
}

object HNNSyntaxParserTest : Spek(ParserSpek<Sentence, CanParseSyntax<Sentence>>(
        getParser = { Parser() },
        parseSentByKoala = { sent, parser ->
            if (sent.any { it in "+()" }) {
                // '+()'가 포함된 어절의 분석결과는 한나눔이 오류를 발생시킴 (+가 누락되거나, ()가 제대로 처리되지 않음)
                // 따라서 비교 과정에서 무시함
                emptyList()
            } else {
                val result = parser.analyze(sent)

                result.map { s ->
                    // 한나눔 파서의 원본 문장 변형 정도가 심하므로, 원본 문장은 확인하지 않음
                    "" to s.getSyntaxTree()?.getDFSString(StringBuffer()).toString()
                }
            }
        },
        parseSentByOrig = {
            if (it.any { ch -> ch in "+()" }) {
                // '+()'가 포함된 어절의 분석결과는 한나눔이 오류를 발생시킴 (+가 누락되거나, ()가 제대로 처리되지 않음)
                // 따라서 비교 과정에서 무시함
                emptyList()
            } else {
                synchronized(taggerWorkflow) {
                    taggerWorkflow.analyze(it)
                    val tagged = mutableListOf<Sentence>()
                    var isEnded = false

                    while (!isEnded) {
                        val sent = taggerWorkflow.getResultOfSentence(Sentence(0, 0, false))
                        isEnded = sent.isEndOfDocument
                        tagged.add(sent)
                    }

                    val conv = Converter()
                    val trees =
                            tagged.map { s ->
                                val surface = s.plainEojeols.joinToString(" ")
                                        .replace("(", "-LRB-").replace(")", "-RRB-")
                                val exp = conv.StringforDepformat(Converter.functionTagReForm(parser.parse(surface)))
                                val tree = ParseTree(surface, exp, 0, true)

                                conv.convert(tree) // 한나눔 코드가 의존구문분석을 위한 분석 과정에서 Tree를 변경하고 있음
                                tree
                            }


                    trees.map {
                        // 한나눔 파서의 원본 문장 변형 정도가 심하므로, 원본 문장은 확인하지 않음
                        "" to (it.head as NonterminalNode).getDFSString(StringBuffer()).toString()
                    }
                }
            }
        }
))

fun DepEdge.getOriginalString() = "${this.originalLabel}(${this.governor?.id ?: -1}, ${this.dependent.id})"

fun DNode.getOriginalString() =
        "${this.correspondingPhrase.phraseTag.split("-")[0]}_${this.getdType()}(${this.head?.wordIdx
                ?: -1}, ${this.wordIdx})"

object HNNDepParserTest : Spek(ParserSpek<Sentence, CanParseDependency<Sentence>>(
        getParser = { Parser() },
        parseSentByKoala = { sent, parser ->
            if (sent.any { it in "+()" }) {
                // '+()'가 포함된 어절의 분석결과는 한나눔이 오류를 발생시킴 (+가 누락되거나, ()가 제대로 처리되지 않음)
                // 따라서 비교 과정에서 무시함
                emptyList()
            } else {
                val result = parser.analyze(sent)

                result.map { s ->
                    val deps = s.getDependencies()
                    val depString = deps?.asSequence()?.map { it.getOriginalString() }?.sorted()?.joinToString() ?: ""

                    // 한나눔 파서의 원본 문장 변형 정도가 심하므로, 원본 문장은 확인하지 않음
                    "" to depString
                }
            }
        },
        parseSentByOrig = { sent ->
            if (sent.any { it in "+()" }) {
                // '+()'가 포함된 어절의 분석결과는 한나눔이 오류를 발생시킴 (+가 누락되거나, ()가 제대로 처리되지 않음)
                // 따라서 비교 과정에서 무시함
                emptyList()
            } else {
                synchronized(taggerWorkflow) {
                    taggerWorkflow.analyze(sent)
                    val tagged = mutableListOf<Sentence>()
                    var isEnded = false

                    while (!isEnded) {
                        val s = taggerWorkflow.getResultOfSentence(Sentence(0, 0, false))
                        isEnded = s.isEndOfDocument
                        tagged.add(s)
                    }

                    val conv = Converter()
                    val trees =
                            tagged.map { s ->
                                val surface = s.plainEojeols.joinToString(" ")
                                        .replace("(", "-LRB-").replace(")", "-RRB-")
                                val exp = conv.StringforDepformat(Converter.functionTagReForm(parser.parse(surface)))
                                val tree = ParseTree(surface, exp, 0, true)
                                conv.convert(tree)
                            }

                    trees.map { t ->
                        // 한나눔 파서의 원본 문장 변형 정도가 심하므로, 원본 문장은 확인하지 않음
                        "" to t.nodeList.map { it.getOriginalString() }.sorted().joinToString()
                    }
                }
            }
        }
))


object HNNOriginalWrapperTest : Spek({
    defaultTimeout = 300000L  // 5 minutes

    val tagger = Tagger()

    describe("MorphemeAnalyzerWrap") {
        it("should provide exactly same result with HanNanumMorphemeAnalyzerWrap") {
            Examples.exampleSequence().forEach { pair ->
                val sent = pair.second
                tagger.tagParagraphOriginal(sent).forEach { s ->
                    Configuration.hanBaseDir = "./hnnModels/"
                    println("MorphAnalWrap ${s.plainEojeols.joinToString(" ")}")

                    val original = HanNanumMorphAnalWrapper.getInstance().getAnalysisResult(s.plainEojeols.joinToString(" "))
                    val reproduced = MorphemeAnalyzerWrap.getAnalysisResult(s)

                    reproduced.zip(original).forEach {
                        if ('+' !in it.first.origEojeol) {
                            // '+'가 포함된 어절의 분석결과는 한나눔이 오류임 (+가 누락됨)
                            it.first.toString() `should be equal to` it.second.toString()
                        }
                    }
                }
            }
        }
    }

    describe("BerkeleyParserWrap") {
        it("should provide exactly same result with BerkeleyParserWrapper") {
            Configuration.parserModel = "./hnnModels/models/parser/KorGrammar_BerkF_ORIG"
            Configuration.hanBaseDir = "./hnnModels/"
            val parser = BerkeleyParserWrapper(Configuration.parserModel)
            val wrap = BerkeleyParserWrap()
            Examples.exampleSequence().forEach { pair ->
                val sent = pair.second
                tagger.tagParagraphOriginal(sent).forEach { s ->
                    println("ParserWrap ${s.plainEojeols.joinToString(" ")}")
                    val original = parser.parse(s.plainEojeols.joinToString(" "))
                    val reproduced = wrap.parseForced(s)

                    if ('+' !in reproduced) {
                        // '+'가 포함된 어절의 분석결과는 한나눔이 오류임 (+가 누락됨)
                        original `should be equal to` reproduced
                    }
                }
            }
        }
    }
})
