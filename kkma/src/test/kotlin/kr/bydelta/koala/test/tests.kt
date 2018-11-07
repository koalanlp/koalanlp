package kr.bydelta.koala.test

import kr.bydelta.koala.*
import kr.bydelta.koala.data.DepEdge
import kr.bydelta.koala.kkma.*
import org.snu.ids.kkma.ma.MorphemeAnalyzer
import org.snu.ids.kkma.sp.ParseTreeEdge
import org.snu.ids.kkma.sp.ParseTreeNode
import org.spekframework.spek2.Spek

object KKMATagConversionTest : Spek(TagConversionSpek(
        to = { it.fromSejongPOS() },
        from = { it.toSejongPOS() },
        getMapping = {
            when (it) {
                POS.VX -> listOf(Conversion("VXV"),
                        Conversion("VXA", toTagger = false))
                POS.MM -> listOf(Conversion("MDT")
                        , Conversion("MDN", toTagger = false))
                POS.MAJ -> listOf(Conversion("MAC"))
                POS.JKB -> listOf(Conversion("JKM"))
                POS.JKV -> listOf(Conversion("JKI"))
                POS.EP ->
                    listOf(Conversion("EPT"),
                            Conversion("EPH", toTagger = false),
                            Conversion("EPP", toTagger = false))
                POS.EF ->
                    listOf(Conversion("EFN"),
                            Conversion("EFQ", toTagger = false),
                            Conversion("EFO", toTagger = false),
                            Conversion("EFA", toTagger = false),
                            Conversion("EFI", toTagger = false),
                            Conversion("EFR", toTagger = false))
                POS.EC ->
                    listOf(Conversion("ECE"),
                            Conversion("ECD", toTagger = false),
                            Conversion("ECS", toTagger = false))
                POS.ETM -> listOf(Conversion("ETD"))
                POS.SL -> listOf(Conversion("OL"))
                POS.SH -> listOf(Conversion("OH"))
                POS.SN -> listOf(Conversion("ON"))
                POS.NF -> listOf(Conversion("UN"))
                POS.NV -> listOf(Conversion("UV"))
                POS.NA -> listOf(Conversion("UE"))
                else -> listOf(Conversion(it.toString()))
            }
        }
))


object KKMADictTest : Spek(DictSpek(Dictionary, getTagger = { Tagger() }))

object KKMATaggerTest : Spek(TaggerSpek(
        getTagger = { Tagger() },
        tagSentByOrig = {
            val kkma = MorphemeAnalyzer()

            val original = kkma.divideToSentences(kkma.leaveJustBest(
                    kkma.postProcess(kkma.analyze(it)))).flatten()

            val tag = original.joinToString(" ") { s ->
                s.joinToString("+") { m -> "${m.string}/${m.tag}" }
            }

            val surface = original.joinToString(" ") { s -> s.exp }

            surface to tag
        },
        tagParaByOrig = {
            val kkma = MorphemeAnalyzer()

            val original = kkma.divideToSentences(kkma.leaveJustBest(
                    kkma.postProcess(kkma.analyze(it))))

            original.map { s ->
                s.joinToString(" ") { e ->
                    e.joinToString("+") { m -> m.string }
                }
            }
        },
        isSentenceSplitterImplemented = false
))

fun DepEdge.getOriginalString() = "${this.originalLabel}(${this.governor?.id ?: -1}, ${this.dependent.id})"

fun ParseTreeEdge.getOriginalString(nodes: List<ParseTreeNode>) =
        "${this.relation}(${nodes.getOrNull(this.fromId)?.eojeol?.firstMorp?.index
                ?: -1}, ${nodes[this.toId].eojeol?.firstMorp?.index})"

object KKMAParserTest : Spek(ParserSpek(
        getParser = { Parser() },
        parseSentByOrig = {
            val kkma = MorphemeAnalyzer()
            val kkpa = org.snu.ids.kkma.sp.Parser()

            val trees = kkma.divideToSentences(
                    kkma.leaveJustBest(kkma.postProcess(kkma.analyze(it)))
            ).map { x ->
                x.forEachIndexed { index, eojeol ->
                    eojeol.firstMorp.index = index
                }
                kkpa.parse(x)
            }

            trees.map {
                it.sentenec to it.edgeList.map { edge -> edge.getOriginalString(it.nodeList) }.sorted().joinToString()
            }
        },
        parseSentByKoala = { sent, parser ->
            val result = parser.analyze(sent)

            result.map { s ->
                val deps = s.getDependencies()
                val depStr = deps?.asSequence()?.map { it.getOriginalString() }?.sorted()?.joinToString() ?: ""

                s.surfaceString() to depStr
            }
        }
))