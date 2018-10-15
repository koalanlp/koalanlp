package kr.bydelta.koala

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.amshove.kluent.shouldContainSame
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.util.*

object TypeSpec : Spek({
    describe("POS") {
        // ALL, NOUNS, PREDICATES, MODIFIERS, POSTPOSITIONS, ENDINGS, AFFIXES, SUFFIXES, SYMBOLS, UNKNOWNS
        it("should discriminate tags") {
            val map = mapOf(
                    POS.NNG to setOf(POS.NOUNS),
                    POS.NNP to setOf(POS.NOUNS),
                    POS.NNB to setOf(POS.NOUNS),
                    POS.NNM to setOf(POS.NOUNS),
                    POS.NR to setOf(POS.NOUNS),
                    POS.NP to setOf(POS.NOUNS),
                    POS.VV to setOf(POS.PREDICATES),
                    POS.VA to setOf(POS.PREDICATES),
                    POS.VX to setOf(POS.PREDICATES),
                    POS.VCP to setOf(POS.PREDICATES),
                    POS.VCN to setOf(POS.PREDICATES),
                    POS.MM to setOf(POS.MODIFIERS),
                    POS.MAG to setOf(POS.MODIFIERS),
                    POS.MAJ to setOf(POS.MODIFIERS),
                    POS.IC to emptySet(),
                    POS.JKS to setOf(POS.POSTPOSITIONS),
                    POS.JKC to setOf(POS.POSTPOSITIONS),
                    POS.JKG to setOf(POS.POSTPOSITIONS),
                    POS.JKO to setOf(POS.POSTPOSITIONS),
                    POS.JKB to setOf(POS.POSTPOSITIONS),
                    POS.JKV to setOf(POS.POSTPOSITIONS),
                    POS.JKQ to setOf(POS.POSTPOSITIONS),
                    POS.JC to setOf(POS.POSTPOSITIONS),
                    POS.JX to setOf(POS.POSTPOSITIONS),
                    POS.EP to setOf(POS.ENDINGS),
                    POS.EF to setOf(POS.ENDINGS),
                    POS.EC to setOf(POS.ENDINGS),
                    POS.ETN to setOf(POS.ENDINGS),
                    POS.ETM to setOf(POS.ENDINGS),
                    POS.XPN to setOf(POS.AFFIXES),
                    POS.XPV to setOf(POS.AFFIXES),
                    POS.XSN to setOf(POS.AFFIXES, POS.SUFFIXES),
                    POS.XSV to setOf(POS.AFFIXES, POS.SUFFIXES),
                    POS.XSA to setOf(POS.AFFIXES, POS.SUFFIXES),
                    POS.XSM to setOf(POS.AFFIXES, POS.SUFFIXES),
                    POS.XSO to setOf(POS.AFFIXES, POS.SUFFIXES),
                    POS.XR to emptySet(),
                    POS.SF to setOf(POS.SYMBOLS),
                    POS.SP to setOf(POS.SYMBOLS),
                    POS.SS to setOf(POS.SYMBOLS),
                    POS.SE to setOf(POS.SYMBOLS),
                    POS.SO to setOf(POS.SYMBOLS),
                    POS.SW to setOf(POS.SYMBOLS),
                    POS.NF to setOf(POS.UNKNOWNS),
                    POS.NV to setOf(POS.UNKNOWNS),
                    POS.NA to setOf(POS.UNKNOWNS),
                    POS.SL to emptySet(),
                    POS.SH to emptySet(),
                    POS.SN to emptySet()
            )

            val tagset = listOf(
                    POS.UNKNOWNS,
                    POS.SYMBOLS,
                    POS.SUFFIXES,
                    POS.AFFIXES,
                    POS.ENDINGS,
                    POS.POSTPOSITIONS,
                    POS.MODIFIERS,
                    POS.PREDICATES,
                    POS.NOUNS
            )

            map.keys shouldContainSame POS.ALL.minus(POS.TEMP)

            map.forEach { (tag, set) ->
                for (target in tagset) {

                    (tag in target) `should be equal to` (target in set)

                    when (target) {
                        POS.UNKNOWNS -> tag.isUnknown()
                        POS.SYMBOLS -> tag.isSymbol()
                        POS.SUFFIXES -> tag.isSuffix()
                        POS.AFFIXES -> tag.isAffix()
                        POS.ENDINGS -> tag.isEnding()
                        POS.POSTPOSITIONS -> tag.isPostPosition()
                        POS.MODIFIERS -> tag.isModifier()
                        POS.PREDICATES -> tag.isPredicate()
                        POS.NOUNS -> tag.isNoun()
                        else -> false
                    } `should be equal to` (target in set)
                }
            }
        }

        // startsWith
        // CharSequence.contains, in
        it("should correctly recognize higher-level tags") {
            val partialCodes = POS.ALL.minus(POS.TEMP).flatMap {
                val name = it.name
                (1..name.length).flatMap { len ->
                    val code = name.substring(0, len)
                    listOf(code, code.toLowerCase())
                }
            }.toSet()

            POS.ALL.forEach {
                if (it.isUnknown()) {
                    for (code in partialCodes) {
                        if (code.toUpperCase() == "N") {
                            code.contains(it) `should be` false
                            (it in code) `should be` false
                            it.startsWith(code) `should be` false
                        } else {
                            code.contains(it) `should be` it.name.startsWith(code.toUpperCase())
                            (it in code) `should be` it.name.startsWith(code.toUpperCase())
                            it.startsWith(code) `should be` it.name.startsWith(code.toUpperCase())
                        }
                    }
                } else {
                    for (code in partialCodes) {
                        code.contains(it) `should be` it.name.startsWith(code.toUpperCase())
                        (it in code) `should be` it.name.startsWith(code.toUpperCase())
                        it.startsWith(code) `should be` it.name.startsWith(code.toUpperCase())
                    }
                }
            }
        }
    }

    describe("PhraseTag") {
        // Iterable<String>.contains, in
        it("should correctly recognize names of tags") {
            val values = PhraseTag.values()
            val codes = values.map { it.name }
            val random = Random()

            for (i in 0..100) {
                val filtered = codes.filter { random.nextBoolean() }

                for (tag in values) {
                    (tag in filtered) `should be equal to` (tag.name in filtered)
                    filtered.contains(tag) `should be equal to` (tag.name in filtered)
                }
            }
        }
    }

    describe("DependencyTag") {
        // Iterable<String>.contains, in
        it("should correctly recognize names of tags") {
            val values = DependencyTag.values()
            val codes = values.map { it.name }
            val random = Random()

            for (i in 0..100) {
                val filtered = codes.filter { random.nextBoolean() }

                for (tag in values) {
                    (tag in filtered) `should be equal to` (tag.name in filtered)
                    filtered.contains(tag) `should be equal to` (tag.name in filtered)
                }
            }
        }
    }

    describe("RoleType") {
        // Iterable<String>.contains, in
        it("should correctly recognize names of tags") {
            val values = RoleType.values()
            val codes = values.map { it.name }
            val random = Random()

            for (i in 0..100) {
                val filtered = codes.filter { random.nextBoolean() }

                for (tag in values) {
                    (tag in filtered) `should be equal to` (tag.name in filtered)
                    filtered.contains(tag) `should be equal to` (tag.name in filtered)
                }
            }
        }
    }

    describe("CoarseEntityType") {
        // Iterable<String>.contains, in
        it("should correctly recognize names of tags") {
            val values = CoarseEntityType.values()
            val codes = values.map { it.name }
            val random = Random()

            for (i in 0..100) {
                val filtered = codes.filter { random.nextBoolean() }

                for (tag in values) {
                    (tag in filtered) `should be equal to` (tag.name in filtered)
                    filtered.contains(tag) `should be equal to` (tag.name in filtered)
                }
            }
        }
    }
})