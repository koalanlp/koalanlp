package kr.bydelta.koala

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.shouldContainSame
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object ProcessorSpec : Spek({
    describe("CanSplitSentence Interface") {

        val splitter = object : CanSplitSentence {
            override fun sentences(text: String): List<String> {
                return text.split("\n").toList()
            }
        }

        // sentences, invoke
        it("splits sentence") {
            splitter.sentences("안녕하세요.\n졸린 일요일입니다.") shouldContainSame listOf("안녕하세요.", "졸린 일요일입니다.")
            splitter("안녕하세요.\n졸린 일요일입니다.") shouldContainSame listOf("안녕하세요.", "졸린 일요일입니다.")
        }
    }

    describe("SentenceSplitter") {
        val sent1 =
                """집/NNG 앞/NNG+에서/JKB 고추/NNG+를/JKO 말리/VV+던/ETM 이숙희/NNP (/SS 가명/NNG ·/SP 75/SN )/SS 할머니/NNG+의/JKG 얼굴/NNG+에/JKB+는/JX 웃음/NNG+기/NNG+가/JKS 없/VA+었/EP+다/EF ./SF
                    |"/SW 나라/NNG+가/JKS 취로/NNG 사업/NNG+이/VCP 라도/EC 만들/VV+어/EC 주/VX+지/EC 않/VX+으면/EC 일/NNG+이/JKS 없/VA+어/EF ./SF 섬/NNG+이/VCP 라서/EC 어디/NP 다른/MM 데/NNB 나가/VV+서/EC 일/NNG+하/XSV+ᆯ/ETM 수/NNB+도/JX 없/VA+고/EC ./SW "/SW 가난/NNG+에/JKB 익숙/XR+하/XSA+아/EC+지/VX+ᆫ/ETM 연평/NNG+도/NNG 사람/NNG+들/XSN+은/JX '/SW 정당/NNG '/SW 과/JC '/SW 은혜/NNG '/SW 이/VCP+라는/ETM 말/NNG+을/JKO 즐기/VV+어/EC 쓰/VV+었/EP+다/EF ./SF""".trimMargin()
                        .split("\n").map { s ->
                            Sentence(s.split(" ").map { w ->
                                val morphemes = w.split("\\+").map { m ->
                                    val segments = m.split("/")
                                    Morpheme(segments[0], POS.valueOf(segments[1]), segments[1])
                                }
                                Word(morphemes.joinToString("") { it.surface }, morphemes)
                            })
                        }.toList()

        val sent2 =
                """집/NNG 앞/NNG+에서/JKB 고추/NNG+를/JKO 말리/VV+던/ETM 이숙희/NNP (/SS 가명/NNG ·/SP 75/SN )/SS 할머니/NNG+의/JKG 얼굴/NNG+에/JKB+는/JX 웃음/NNG+기/NNG+가/JKS 없/VA+었/EP+다/EF ./SF
                |"/SW 나라/NNG+가/JKS 취로/NNG 사업/NNG+이/VCP 라도/EC 만들/VV+어/EC 주/VX+지/EC 않/VX+으면/EC 일/NNG+이/JKS 없/VA+어/EF ./SF 섬/NNG+이/VCP 라서/EC 어디/NP 다른/MM 데/NNB 나가/VV+서/EC 일/NNG+하/XSV+ᆯ/ETM 수/NNB+도/JX 없/VA+고/EC ./SW "/SW+라고/JKB 하/VV+시/EP+었/EP+다/EF ./SF
                |가난/NNG+에/JKB 익숙/XR+하/XSA+아/EC+지/VX+ᆫ/ETM 연평/NNG+도/NNG 사람/NNG+들/XSN+은/JX '/SW 정당/NNG '/SW 과/JC '/SW 은혜/NNG '/SW 이/VCP+라는/ETM 말/NNG+을/JKO 즐기/VV+어/EC 쓰/VV+었/EP+다/EF ./SF""".trimMargin()
                        .split("\n").map { s ->
                            Sentence(s.split(" ").map { w ->
                                val morphemes = w.split("\\+").map { m ->
                                    val segments = m.split("/")
                                    Morpheme(segments[0], POS.valueOf(segments[1]), segments[1])
                                }
                                Word(morphemes.joinToString("") { it.surface }, morphemes)
                            })
                        }.toList()

        val sent3 =
                """오늘/NNG 환율/NNG+은/JX 137/SN ./SF 75/SN+원/NNM+에/JKB 거래/NNG+되/XSV+었/EP+다/EF+./SF
                    |미스터/NNP 션샤인/NNP+은/JX 해외/NNG+에서/JKB Mr/SL ./SF Sunshine/SL+으로/JKB 부르/VV+ㄴ/EP+다/EF+./SF""".trimMargin()
                        .split("\n").map { s ->
                            Sentence(s.split(" ").map { w ->
                                val morphemes = w.split("\\+").map { m ->
                                    val segments = m.split("/")
                                    Morpheme(segments[0], POS.valueOf(segments[1]), segments[1])
                                }
                                Word(morphemes.joinToString("") { it.surface }, morphemes)
                            })
                        }.toList()

        // invoke
        it("handle empty sentence") {
            val sent = SentenceSplitter(Sentence(emptyList()))
            sent.isEmpty() `should be` true
        }

        it("split sentences") {
            val split1 = SentenceSplitter(sent1.flatten())
            split1.size `should be equal to` 2
            split1 `should equal` sent1

            val split2 = SentenceSplitter(sent2.flatten())
            split2.size `should be equal to` 3
            split2 `should equal` sent2

            val split3 = SentenceSplitter(sent3.flatten())
            split3.size `should be equal to` 2
            split3 `should equal` sent3
        }
    }

    describe("CanTagASentence Interface") {
        val tagger = object : CanTagASentence<List<String>>() {
            override fun tag(text: String): List<Sentence> {
                return if (text.trim().isEmpty()) emptyList()
                else text.split("\n").map { convertSentence(tagSentenceOriginal(it)) }
            }

            override fun tagSentenceOriginal(text: String): List<String> {
                return text.split(" ").toList()
            }

            override fun convertSentence(result: List<String>): Sentence {
                return Sentence(result.map { Word(it, listOf(Morpheme(it, POS.NNP, "NNP"))) })
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

    describe("CanTagOnlyASentence Interface") {
        val tagger = object : CanTagOnlyASentence<List<String>>() {
            override fun tagSentenceOriginal(text: String): List<String> {
                return text.split(" ").toList()
            }

            override fun convertSentence(text: String, result: List<String>): Sentence {
                return Sentence(result.map { Word(it, listOf(Morpheme(it, POS.NNP, "NNP"))) })
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
            sent[0].singleLineString() `should be equal to` "이것은/NNP 테스트/NNP 문장입니다.\n저것은/NNP 설렘입니다./NNP"
            sent[0].size `should be equal to` 4

            tagger.tag("").isEmpty() `should be` true
            tagger("행복하신가요?") `should equal` tagger.tag("행복하신가요?")
        }
    }

    describe("CanTagAParagraph Interface") {
        val tagger = object : CanTagAParagraph<List<String>>() {
            override fun tagSentenceOriginal(text: String): List<String> {
                return text.split(" ").toList()
            }

            override fun convertSentence(result: List<String>): Sentence {
                return Sentence(result.map { Word(it, listOf(Morpheme(it, POS.NNP, "NNP"))) })
            }

            override fun tagParagraphOriginal(text: String): List<List<String>> {
                return text.split("\n").map { it.split(" ").toList() }.toList()
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
        val propertyAttacher = object : CanAnalyzeSentenceProperty<IntProperty> {
            override fun getProperty(item: Sentence): IntProperty {
                return IntProperty(1)
            }

            override fun convert(sentence: String): List<Sentence> {
                return listOf(Sentence(sentence.split(" ").map {
                    Word(it, listOf(Morpheme(it, POS.NNP, "NNP")))
                }))
            }
        }

        // parse, invoke, attachProperty
        it("should parse a sentence, a paragraph, and a Sentence instance") {
            val sent = "안녕하세요.\n졸린 일요일입니다."

            propertyAttacher.parse(sent) `should equal` propertyAttacher(sent)
            propertyAttacher.parse(sent)[0].getProperty<IntProperty>(KEY_WORDSENSE)?.value `should equal` 1

            val sentInst = Sentence(
                    listOf(
                            Word("흰",
                                    listOf(Morpheme("희", POS.VA, "VA"),
                                            Morpheme("ㄴ", POS.ETM, "ETM"))
                            ),
                            Word("밥을",
                                    listOf(Morpheme("밥", POS.NNG, "NNG"),
                                            Morpheme("을", POS.JKO, "JKO"))
                            ),
                            Word("나는",
                                    listOf(Morpheme("나", POS.NP, "NP"),
                                            Morpheme("는", POS.JX, "JX"))
                            ),
                            Word("먹었다",
                                    listOf(Morpheme("먹", POS.VV, "VV"),
                                            Morpheme("었", POS.EP, "EP"),
                                            Morpheme("다", POS.EF, "EF"))
                            )
                    )
            )

            propertyAttacher.parse(sentInst).getProperty<IntProperty>(KEY_WORDSENSE)?.value `should equal` 1
            sentInst.removeProperty(KEY_WORDSENSE)

            propertyAttacher(sentInst).getProperty<IntProperty>(KEY_WORDSENSE)?.value `should equal` 1
            sentInst.removeProperty(KEY_WORDSENSE)

            propertyAttacher.attachProperty(sentInst)
            sentInst.getProperty<IntProperty>(KEY_WORDSENSE)?.value `should equal` 1
            sentInst.removeProperty(KEY_WORDSENSE)

            val sentInst2 = Sentence(
                    listOf(
                            Word("나는",
                                    listOf(
                                            Morpheme("나", POS.NP, "NP"),
                                            Morpheme("는", POS.JX, "JX")
                                    )
                            ),
                            Word("밥을",
                                    listOf(
                                            Morpheme("밥", POS.NNG, "NNG"),
                                            Morpheme("을", POS.JKO, "JKO")
                                    )
                            )
                    )
            )

            propertyAttacher.parse(listOf(sentInst, sentInst2))
            sentInst.getProperty<IntProperty>(KEY_WORDSENSE)?.value `should equal` 1
            sentInst2.getProperty<IntProperty>(KEY_WORDSENSE)?.value `should equal` 1
            sentInst.removeProperty(KEY_WORDSENSE)
            sentInst2.removeProperty(KEY_WORDSENSE)

            propertyAttacher(listOf(sentInst, sentInst2))
            sentInst.getProperty<IntProperty>(KEY_WORDSENSE)?.value `should equal` 1
            sentInst2.getProperty<IntProperty>(KEY_WORDSENSE)?.value `should equal` 1
            sentInst.removeProperty(KEY_WORDSENSE)
            sentInst2.removeProperty(KEY_WORDSENSE)
        }
    }

    describe("CanDisambiguateSense Interface") {
        val propertyAttacher = object : CanDisambiguateSense {
            override fun getProperty(item: Sentence): Sentence {
                item.forEach { w ->
                    w.forEach {
                        it.setProperty(IntProperty(1))
                    }
                }
                return item
            }

            override fun convert(sentence: String): List<Sentence> {
                return listOf(Sentence(sentence.split(" ").map {
                    Word(it, listOf(Morpheme(it, POS.NNP, "NNP")))
                }))
            }
        }

        // parse, invoke, attachProperty
        it("should parse a sentence, a paragraph, and a Sentence instance") {
            val sent = "안녕하세요.\n졸린 일요일입니다."

            propertyAttacher.parse(sent) `should equal` propertyAttacher(sent)
            propertyAttacher.parse(sent)[0].all { w -> w.all { it.getWordSense() == 1 } } `should be` true

            val sentInst = Sentence(
                    listOf(
                            Word("흰",
                                    listOf(Morpheme("희", POS.VA, "VA"),
                                            Morpheme("ㄴ", POS.ETM, "ETM"))
                            ),
                            Word("밥을",
                                    listOf(Morpheme("밥", POS.NNG, "NNG"),
                                            Morpheme("을", POS.JKO, "JKO"))
                            ),
                            Word("나는",
                                    listOf(Morpheme("나", POS.NP, "NP"),
                                            Morpheme("는", POS.JX, "JX"))
                            ),
                            Word("먹었다",
                                    listOf(Morpheme("먹", POS.VV, "VV"),
                                            Morpheme("었", POS.EP, "EP"),
                                            Morpheme("다", POS.EF, "EF"))
                            )
                    )
            )

            propertyAttacher.parse(sentInst).all { w -> w.all { it.getWordSense() == 1 } } `should be` true
            sentInst.forEach { w -> w.forEach { it.removeProperty(KEY_WORDSENSE) } }

            propertyAttacher(sentInst).all { w -> w.all { it.getWordSense() == 1 } } `should be` true
            sentInst.forEach { w -> w.forEach { it.removeProperty(KEY_WORDSENSE) } }

            propertyAttacher.attachProperty(sentInst)
            sentInst.all { w -> w.all { it.getWordSense() == 1 } } `should be` true
            sentInst.forEach { w -> w.forEach { it.removeProperty(KEY_WORDSENSE) } }

            val sentInst2 = Sentence(
                    listOf(
                            Word("나는",
                                    listOf(
                                            Morpheme("나", POS.NP, "NP"),
                                            Morpheme("는", POS.JX, "JX")
                                    )
                            ),
                            Word("밥을",
                                    listOf(
                                            Morpheme("밥", POS.NNG, "NNG"),
                                            Morpheme("을", POS.JKO, "JKO")
                                    )
                            )
                    )
            )

            propertyAttacher.parse(listOf(sentInst, sentInst2))
            sentInst.all { w -> w.all { it.getWordSense() == 1 } } `should be` true
            sentInst2.all { w -> w.all { it.getWordSense() == 1 } } `should be` true
            sentInst.forEach { w -> w.forEach { it.removeProperty(KEY_WORDSENSE) } }
            sentInst2.forEach { w -> w.forEach { it.removeProperty(KEY_WORDSENSE) } }

            propertyAttacher(listOf(sentInst, sentInst2))
            sentInst.all { w -> w.all { it.getWordSense() == 1 } } `should be` true
            sentInst2.all { w -> w.all { it.getWordSense() == 1 } } `should be` true
            sentInst.forEach { w -> w.forEach { it.removeProperty(KEY_WORDSENSE) } }
            sentInst2.forEach { w -> w.forEach { it.removeProperty(KEY_WORDSENSE) } }
        }
    }
})