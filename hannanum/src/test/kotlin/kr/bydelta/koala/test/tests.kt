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
import kaist.cilab.parser.corpusconverter.sejong2treebank.sejongtree.NonterminalNode
import kaist.cilab.parser.corpusconverter.sejong2treebank.sejongtree.ParseTree
import kaist.cilab.parser.corpusconverter.sejong2treebank.sejongtree.TerminalNode
import kaist.cilab.parser.corpusconverter.sejong2treebank.sejongtree.TreeNode
import kaist.cilab.parser.dependency.DNode
import kaist.cilab.parser.psg2dg.Converter
import kr.bydelta.koala.*
import kr.bydelta.koala.data.DepEdge
import kr.bydelta.koala.data.SyntaxTree
import kr.bydelta.koala.hnn.*
import kr.bydelta.koala.proc.CanParseDependency
import kr.bydelta.koala.proc.CanParseSyntax
import org.spekframework.spek2.Spek
import java.io.File

object HNNDictTest : Spek(DictSpek(Dictionary))

val splitterFlow by lazy {
    synchronized(Configuration.hanBaseDir) {
        Configuration.hanBaseDir = "./"
        val workflow = Workflow()
        val basePath = "./"

        workflow.appendPlainTextProcessor(SentenceSegmentor(),
                basePath + File.separator + "conf" + File.separator + "SentenceSegment.json")
        workflow.activateWorkflow(true)
        print("SplitterFlow initialized")
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
        Configuration.hanBaseDir = "./"
        val workflow = Workflow()
        val basePath = "./"

        workflow.appendPlainTextProcessor(SentenceSegmentor(),
                basePath + File.separator + "conf" + File.separator + "SentenceSegment.json")
        workflow.appendPlainTextProcessor(InformalSentenceFilter(),
                basePath + File.separator + "conf" + File.separator + "InformalSentenceFilter.json")

        workflow.setMorphAnalyzer(ChartMorphAnalyzer(),
                basePath + File.separator + "conf" + File.separator + "ChartMorphAnalyzer.json")
        workflow.appendMorphemeProcessor(UnknownProcessor(),
                basePath + File.separator + "conf" + File.separator + "UnknownMorphProcessor.json")

        workflow.setPosTagger(HMMTagger(),
                basePath + File.separator + "conf" + File.separator + "HmmPosTagger.json")
        workflow.activateWorkflow(true)
        workflow
    }
}

val prevEnd by lazy { Dictionary.userDic.search_end }

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

val parser by lazy { BerkeleyParserWrapper(Configuration.parserModel) }

fun SyntaxTree.getDFSString(buffer: StringBuffer): StringBuffer {
    buffer.append("${this.originalLabel}{${this.terminal?.joinToString(",") {
        it.tag.toString()
    }?.plus(",") ?: ""}")
    for (child in this) {
        child.getDFSString(buffer)
        buffer.append(',')
    }
    buffer.append('}')
    return buffer
}

fun TreeNode.getDFSString(buffer: StringBuffer): StringBuffer {
    return if (this is NonterminalNode) {
        buffer.append("${this.phraseTag.split("-")[0]}{")
        for (child in this.children) {
            child.getDFSString(buffer)
            buffer.append(',')
        }
        buffer.append('}')
        buffer
    } else if (this is TerminalNode) {
        buffer.append(this.pos)
        buffer
    } else
        buffer
}

fun DepEdge.getOriginalString() = "${this.originalLabel}(${this.governor?.id ?: -1}, ${this.dependent.id})"

fun DNode.getOriginalString() =
        "${this.correspondingPhrase.phraseTag.split("-")[0]}_${this.getdType()}(${this.head?.wordIdx
                ?: -1}, ${this.wordIdx})"

object HNNSyntaxParserTest : Spek(ParserSpek<Sentence, CanParseSyntax<Sentence>>(
        getParser = { Parser() },
        parseSentByKoala = { sent, parser ->
            val result = parser.analyze(sent)

            val tree = result.joinToString("\n") { s ->
                s.getSyntaxTree()?.getDFSString(StringBuffer()).toString()
            }

            // 한나눔 파서의 원본 문장 변형 정도가 심하므로, 원본 문장은 확인하지 않음
            "" to tree
        },
        parseSentByOrig = {
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
                        ParseTree(surface, exp, 0, true)
                    }


            val tree = trees.joinToString("\n") { t ->
                t.head.getDFSString(StringBuffer()).toString()
            }

            // 한나눔 파서의 원본 문장 변형 정도가 심하므로, 원본 문장은 확인하지 않음
            "" to tree
        }
))

object HNNDepParserTest : Spek(ParserSpek<Sentence, CanParseDependency<Sentence>>(
        getParser = { Parser() },
        parseSentByKoala = { sent, parser ->
            val result = parser.analyze(sent)

            val dependencies = result.joinToString("\n") { s ->
                val deps = s.getDependencies()
                deps?.asSequence()?.map { it.getOriginalString() }?.sorted()?.joinToString() ?: ""
            }

            // 한나눔 파서의 원본 문장 변형 정도가 심하므로, 원본 문장은 확인하지 않음
            "" to dependencies
        },
        parseSentByOrig = { sent ->
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

            val tree = trees.joinToString("\n") { t ->
                t.nodeList.map { it.getOriginalString() }.sorted().joinToString()
            }

            // 한나눔 파서의 원본 문장 변형 정도가 심하므로, 원본 문장은 확인하지 않음
            "" to tree
        }
))