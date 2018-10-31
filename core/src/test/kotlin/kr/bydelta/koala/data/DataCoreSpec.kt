package kr.bydelta.koala.data

import kr.bydelta.koala.*
import org.amshove.kluent.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object DataCoreSpec : Spek({
    describe("kr/bydelta/koala/data/data.kt") {
        val sent by memoized {
            Sentence(
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
        }

        val sent2 by memoized {
            Sentence(
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
        }

        val sent3 by memoized {
            Sentence(
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
        }

        val sent4 by memoized {
            Sentence(
                    listOf(
                            Word("칠한",
                                    listOf(Morpheme("칠", POS.NNG, "NN"),
                                            Morpheme("하", POS.XSV, "XSV"),
                                            Morpheme("ㄴ", POS.ETM, "ETM"))
                            ),
                            Word("밥을",
                                    listOf(Morpheme("밥", POS.NNG, "NNG"),
                                            Morpheme("을", POS.JKO, "JKO"))
                            ),
                            Word("너는",
                                    listOf(Morpheme("너", POS.NP, "NP"),
                                            Morpheme("는", POS.JX, "JX"))
                            ),
                            Word("먹음",
                                    listOf(Morpheme("먹", POS.VV, "VV"),
                                            Morpheme("음", POS.ETN, "ETN"))
                            )
                    )
            )
        }

        describe("Morpheme") {
            val dummy1 by memoized { Morpheme("밥", POS.NNP, "NNP") }
            val dummy2 by memoized { Morpheme("밥", POS.NNG, "ncn") }
            val unknown by memoized { Morpheme("??", POS.NA) }

            // id, surface, tag, originalTag,
            it("should have correct ID") {
                dummy1.id `should be equal to` -1
                dummy2.id `should be equal to` -1

                // The ID can be set initially.
                { dummy1.id = 5 } `should not throw` AnyException
                dummy1.id `should be equal to` 5

                // After initializing id, it cannot be modified.
                { dummy1.id = 5 } `should throw` AlreadySetIDException::class
                { sent[0][1].id = 8 } `should throw` AlreadySetIDException::class

                for (word in sent2) {
                    for ((index, morpheme) in word.withIndex()) {
                        morpheme.id `should be equal to` index
                    }
                }
            }

            it("should have correct surface") {
                dummy1.surface `should be equal to` "밥"
                dummy2.surface `should be equal to` "밥"
            }

            it("should have correct tag") {
                dummy1.tag `should equal` POS.NNP
                dummy2.tag `should equal` POS.NNG
            }

            it("should have correct original tag") {
                dummy1.originalTag `should be` "NNP"
                dummy2.originalTag `should be` "ncn"
            }

            // getWordSense, setProperty(T), setProperty(K, T),
            it("can save WordSense property") {
                { dummy1.setWordSense(1) } `should not throw` AnyException
                { dummy2.setWordSense(2) } `should not throw` AnyException

                sent2[0][0].getWordSense() `should be` null
                dummy1.getWordSense() `should equal` 1
                dummy2.getWordSense() `should equal` 2

                dummy1.getWordSense() `should be` dummy1.getProperty<WordSense>(Key.WORD_SENSE)?.id
                dummy2.getWordSense() `should be` dummy2.getProperty<WordSense>(Key.WORD_SENSE)?.id
            }

            // isNoun, isPredicate, isModifier, isJosa,
            it("can check whether it is a noun") {
                dummy1.isNoun() `should be` true
                dummy2.isNoun() `should be` true
                unknown.isNoun() `should not be` true

                sent2.flatMap { s -> s.map { it.isNoun() } } `should equal` listOf(false, false, true, false, true, false, false, false, false)
            }

            it("can check whether it is a verb") {
                dummy1.isPredicate() `should not be` true
                dummy2.isPredicate() `should not be` true
                unknown.isPredicate() `should not be` true

                sent2.flatMap { s -> s.map { it.isPredicate() } } `should equal` listOf(true, false, false, false, false, false, true, false, false)
            }

            it("can check whether it is a modifier") {
                dummy1.isModifier() `should not be` true
                dummy2.isModifier() `should not be` true
                unknown.isModifier() `should not be` true

                sent2.flatMap { s -> s.map { it.isModifier() } }.any { it } `should be` false
            }

            it("can check whether it is a josa") {
                dummy1.isJosa() `should not be` true
                dummy2.isJosa() `should not be` true
                unknown.isJosa() `should not be` true

                sent2.flatMap { s -> s.map { it.isJosa() } } `should equal` listOf(false, false, false, true, false, true, false, false, false)
            }

            // hasTag, hasTagOneOf, hasOriginalTag,
            it("can verify whether it has a tag under the given category") {
                dummy1.hasTag("N") `should be` true
                dummy1.hasTag("V") `should be` false

                unknown.hasTag("N") `should be` false
                unknown.hasTag("NA") `should be` true

                sent2.flatMap { s -> s.map { it.hasTag("E") } } `should equal` listOf(false, true, false, false, false, false, false, true, true)
            }

            it("can verify whether it has a tag under one of given category") {
                dummy1.hasTagOneOf("N", "V") `should be` true
                dummy1.hasTagOneOf("V", "E") `should be` false

                unknown.hasTagOneOf("NN", "NP") `should be` false
                unknown.hasTagOneOf("NA", "NN") `should be` true
            }

            it("can verify whether its original tag was under the given category") {
                dummy1.hasOriginalTag("NN") `should be` true
                dummy1.hasOriginalTag("nn") `should be` true
                dummy1.hasOriginalTag("nc") `should be` false

                dummy2.hasOriginalTag("NC") `should be` true
                dummy2.hasOriginalTag("nc") `should be` true
                dummy2.hasOriginalTag("NN") `should be` false

                { unknown.hasOriginalTag("NA") `should be` false } `should not throw` AnyException
            }

            // equals, equalsWithoutTag, hashcode,
            it("can discriminate each other") {
                // Reflexive
                dummy1 `should equal` dummy1
                // Symmetry
                dummy1 `should not equal` dummy2
                dummy2 `should not equal` dummy1

                dummy2 `should equal` sent2[1][0]
                sent2[1][0] `should equal` dummy2
                // Transitivity
                dummy2 `should equal` sent[1][0]
                sent[1][0] `should equal` sent2[1][0]

                dummy1 `should not equal` unknown
                sent2.flatMap { s -> s.map { it == dummy2 } }.count { it } `should be equal to` 1

                dummy1 `should not equal` 1
            }

            it("can verify whether they have the same surface") {
                dummy1.equalsWithoutTag(dummy2) `should be` true
                dummy2.equalsWithoutTag(unknown) `should be` false
            }

            it("can provide correct hashcode") {
                dummy2.hashCode() `should be equal to` sent[1][0].hashCode()
                sent[1][0].hashCode() `should be equal to` sent2[1][0].hashCode()
            }

            // toString, component1, component2, toJSON
            it("should provide the correct string representation") {
                dummy1.toString() `should be equal to` "밥/NNP(NNP)"
                dummy2.toString() `should be equal to` "밥/NNG(ncn)"
                unknown.toString() `should be equal to` "??/NA"
            }

            it("should provide a way to unpack its values") {
                val (surface, tag) = dummy1
                surface `should be instance of` String::class
                tag `should be instance of` POS::class
                surface `should be equal to` "밥"
                tag `should be` POS.NNP
            }

            // Serializable
            it("can be serialized") {
                var outputBytes: ByteArray? = null

                {
                    val output = ByteArrayOutputStream(1024)
                    val serializer = ObjectOutputStream(output)
                    serializer.writeObject(dummy1)
                    serializer.writeObject(dummy2)
                    serializer.writeObject(unknown)
                    serializer.close()

                    outputBytes = output.toByteArray()
                    output.close()
                } `should not throw` AnyException

                {
                    val serializer = ObjectInputStream(ByteArrayInputStream(outputBytes))
                    serializer.readObject() as Morpheme `should equal` dummy1
                    serializer.readObject() as Morpheme `should equal` dummy2
                    serializer.readObject() as Morpheme `should equal` unknown
                    serializer.close()
                } `should not throw` AnyException
            }
        }

        describe("Word") {
            val dummy1 = Word("밥을",
                    listOf(
                            Morpheme("밥", POS.NNG, "NNG"),
                            Morpheme("을", POS.JKO, "JKO")
                    )
            )

            val dummy2 = Word("밥을",
                    listOf(
                            Morpheme("밥", POS.NNP),
                            Morpheme("을", POS.NNP)
                    )
            )

            // id, surface
            it("should have correct ID") {
                dummy1.id `should be equal to` -1

                // The ID can be set initially.
                { dummy1.id = 5 } `should not throw` AnyException
                dummy1.id `should be equal to` 5

                for ((index, word) in sent2.withIndex()) {
                    word.id `should be equal to` index
                }
            }

            it("should have correct surface") {
                dummy1.surface `should be equal to` "밥을"
                sent[0].surface `should be equal to` "나는"
            }

            // [Inherited] List<Morpheme>
            it("can access its morphemes using []") {
                dummy1[0] `should equal` Morpheme("밥", POS.NNG)
                dummy1[1] `should equal` Morpheme("을", POS.JKO)
            }

            it("provide iterator over its morphemes") {
                val it = dummy1.iterator()
                it.next() `should equal` dummy1[0]
                it.next() `should equal` dummy1[1]
                it.hasNext() `should be` false
            }

            it("should find index of given morpheme") {
                dummy1.indexOf(dummy1[0]) `should be equal to` 0
                dummy1.indexOf(dummy1[1]) `should be equal to` 1

                dummy1.indexOf(Morpheme("밥", POS.NNG)) `should be equal to` 0
                dummy1.indexOf(Morpheme("을", POS.JKO)) `should be equal to` 1

                dummy1.lastIndexOf(dummy1[0]) `should be equal to` 0
                dummy1.lastIndexOf(dummy1[1]) `should be equal to` 1

                dummy1.lastIndexOf(Morpheme("밥", POS.NNG)) `should be equal to` 0
                dummy1.lastIndexOf(Morpheme("을", POS.JKO)) `should be equal to` 1

                (Morpheme("밥", POS.NNG) in dummy1) `should be` true
                (Morpheme("밥", POS.NNP) in dummy1) `should be` false
            }

            // setProperty, getEntity, getPhrase, getDependency, getRole
            it("should provide proper way to set a property") {
                dummy1.getEntities().size `should be equal to` 0
                dummy1.getPhrase() `should be` null
                dummy1.getArgumentRoles() `should be` null
                dummy1.getPredicateRole() `should be` null
                dummy1.getDependentEdges() `should be` null
                dummy1.getGovernorEdge() `should be` null

                // All these trees automatically set pointers on the words.
                Entity("밥", CoarseEntityType.PS, "PS_OTHER", listOf(dummy1[0]))
                Entity("밥", CoarseEntityType.PS, "PS_SOME", listOf(dummy1[0]))
                Entity("밥", CoarseEntityType.PS, "PS_ANOTHER", listOf(dummy1[0]))
                val tree = SyntaxTree(PhraseTag.NP, dummy1)
                val dep = DepEdge(dummy1, dummy2, PhraseTag.NP, DependencyTag.SBJ)
                val role = RoleEdge(dummy1, dummy2, RoleType.ARG0)

                dummy1.getEntities().map { it.fineLabel } `should contain` "PS_OTHER"
                dummy1.getPhrase() `should equal` tree
                dummy1.getArgumentRoles()?.get(0) `should equal` role
                dummy2.getPredicateRole() `should equal` role
                dummy1.getDependentEdges()?.get(0) `should equal` dep
                dummy2.getGovernorEdge() `should equal` dep
            }

            // equals, hashcode, equalsWithoutTag
            it("can discriminate each other") {
                // Reflexive
                dummy1 `should equal` dummy1
                // Symmetry
                dummy1 `should not equal` dummy2
                dummy2 `should not equal` dummy1

                dummy1 `should equal` sent[1]
                sent[1] `should equal` dummy1

                dummy1[0].getWord() `should equal` dummy1
                dummy2[0].getWord() `should equal` dummy2

                dummy1 `should not equal` dummy1[0]
                sent.count { it == dummy1 } `should be equal to` 1
                sent2.count { it == dummy1 } `should be equal to` 1

                dummy1 `should not equal` dummy1[0]
            }

            it("can verify whether they have the same surface") {
                dummy1.equalsWithoutTag(dummy2) `should be` true
                dummy2.equalsWithoutTag(sent[0]) `should be` false
            }

            it("can provide correct hashcode") {
                dummy1.hashCode() `should be equal to` sent[1].hashCode()
                sent[1].hashCode() `should be equal to` sent2[1].hashCode()
            }

            // toString, singleLineString
            it("should provide the correct string representation") {
                dummy1.toString() `should be equal to` "밥을 = 밥/NNG+을/JKO"
                dummy2.toString() `should be equal to` "밥을 = 밥/NNP+을/NNP"
            }

            it("should provide proper string representing its morphemes") {
                dummy1.singleLineString() `should be equal to` "밥/NNG+을/JKO"
                dummy2.singleLineString() `should be equal to` "밥/NNP+을/NNP"
            }

            // Serializable
            it("can be serialized") {
                var outputBytes: ByteArray? = null

                {
                    val output = ByteArrayOutputStream(1024)
                    val serializer = ObjectOutputStream(output)
                    serializer.writeObject(dummy1)
                    serializer.writeObject(dummy2)
                    serializer.close()

                    outputBytes = output.toByteArray()
                    output.close()
                } `should not throw` AnyException

                {
                    val serializer = ObjectInputStream(ByteArrayInputStream(outputBytes))
                    serializer.readObject() as Word `should equal` dummy1
                    serializer.readObject() as Word `should equal` dummy2
                    serializer.close()
                } `should not throw` AnyException
            }
        }

        describe("Sentence") {
            // [Inherited] List<Word>
            it("can access its morphemes using []") {
                sent[0].surface `should equal` "나는"
                sent[1].surface `should equal` "밥을"
            }

            it("provide iterator over its morphemes") {
                val it = sent.iterator()
                it.next() `should equal` sent[0]
                it.next() `should equal` sent[1]
                it.hasNext() `should be` false
            }

            it("should find index of given morpheme") {
                sent.indexOf(sent[0]) `should be equal to` 0
                sent.indexOf(sent[1]) `should be equal to` 1

                sent2.indexOf(sent[0]) `should be equal to` 2
                sent2.indexOf(sent[1]) `should be equal to` 1

                sent.lastIndexOf(sent[0]) `should be equal to` 0
                sent.lastIndexOf(sent[1]) `should be equal to` 1

                sent2.lastIndexOf(sent[0]) `should be equal to` 2
                sent2.lastIndexOf(sent[1]) `should be equal to` 1

                (sent2[0] in sent) `should be` false
                (sent2[1] in sent) `should be` true
            }

            // setProperty, getSyntaxTree, getDependencyTree, getRoleTree, getEntities
            it("should provide proper way to set a property") {
                sent.getCorefGroups() `should be` null
                sent.getEntities() `should be` null
                sent.getSyntaxTree() `should be` null
                sent.getDependencies() `should be` null
                sent.getRoles() `should be` null

                // All these trees automatically set pointers on the words.
                {
                    sent.setEntities(listOf(Entity("나", CoarseEntityType.PS, "PS_OTHER", listOf(sent[0][0]))))
                    sent.setCorefGroups(listOf(CoreferenceGroup(listOf(sent.getEntities()!!.get(0)))))
                    sent.setSyntaxTree(SyntaxTree(PhraseTag.S, children = listOf(
                            SyntaxTree(PhraseTag.NP, sent[0]),
                            SyntaxTree(PhraseTag.NP, sent[1])
                    )))
                    sent.setDepEdges(listOf(
                            DepEdge(null, sent[1], PhraseTag.S, DependencyTag.ROOT),
                            DepEdge(sent[1], sent[0], PhraseTag.S, DependencyTag.ROOT)
                    ))
                    sent.setRoleEdges(listOf(
                            RoleEdge(sent[1], sent[0], RoleType.ARG0)
                    ))
                } `should not throw` AnyException

                sent.getCorefGroups()?.get(0)?.get(0) `should equal` sent.getEntities()?.get(0)
                sent.getEntities()?.get(0)?.fineLabel `should equal` "PS_OTHER"
                sent.getSyntaxTree()?.label `should equal` PhraseTag.S
                sent.getDependencies()?.get(0)?.depType `should equal` DependencyTag.ROOT
                sent.getRoles()?.get(0)?.label `should equal` RoleType.ARG0
            }

            // getNouns, getModifiers, getVerbs
            it("should provide proper list of nouns") {
                sent2.getNouns() `should contain all` listOf(sent2[1], sent2[2])
                sent4.getNouns() `should contain all` sent4.subList(1, 4)
            }

            it("should provide proper list of verbs") {
                sent2.getVerbs() `should contain all` listOf(sent2[3])
                sent4.getVerbs().size `should be equal to` 0
            }

            it("should provide proper list of modifiers") {
                sent2.getModifiers() `should contain all` listOf(sent2[0])
                sent4.getModifiers() `should contain all` listOf(sent4[0])
            }

            // toString, surfaceString, singleLineString
            it("should provide the correct string representation") {
                sent.toString() `should be equal to` "나는 밥을"
                sent2.toString() `should be equal to` "흰 밥을 나는 먹었다"

                sent.toString() `should be equal to` sent.surfaceString()
                sent2.toString() `should be equal to` sent2.surfaceString()

                sent.surfaceString("/") `should be equal to` "나는/밥을"
                sent2.surfaceString("/") `should be equal to` "흰/밥을/나는/먹었다"
            }

            it("should provide proper string representing its morphemes") {
                sent.singleLineString() `should be equal to` "나/NP+는/JX 밥/NNG+을/JKO"
                sent2.singleLineString() `should be equal to` "희/VA+ㄴ/ETM 밥/NNG+을/JKO 나/NP+는/JX 먹/VV+었/EP+다/EF"
            }

            // equal, hashcode
            it("can discriminate each other") {
                // Reflexive
                sent2 `should equal` sent2
                // Symmetry
                sent `should not equal` sent2
                sent2 `should not equal` sent4

                sent2 `should equal` sent3
                sent3 `should equal` sent2

                sent `should not equal` sent[0]
            }

            it("can provide correct hashcode") {
                sent.hashCode() `should be equal to` sent.hashCode()
                sent2.hashCode() `should be equal to` sent3.hashCode()
            }

            // Serializable
            it("can be serialized") {
                var outputBytes: ByteArray? = null

                {
                    val output = ByteArrayOutputStream(10240)
                    val serializer = ObjectOutputStream(output)
                    serializer.writeObject(sent)
                    serializer.writeObject(sent2)
                    serializer.close()

                    outputBytes = output.toByteArray()
                    output.close()
                } `should not throw` AnyException

                {
                    val serializer = ObjectInputStream(ByteArrayInputStream(outputBytes))
                    serializer.readObject() as Sentence `should equal` sent
                    serializer.readObject() as Sentence `should equal` sent2
                    serializer.close()
                } `should not throw` AnyException
            }

            // Sentence.empty
            it("should be an empty sentence when call .empty") {
                Sentence.empty.size `should be equal to` 0
            }
        }

        describe("WordSense") {
            // value
            it("should contain an integer value") {
                val value = WordSense(5)
                value.id `should be equal to` 5

                val value2 = WordSense(7)
                value2.id `should be equal to` 7
            }

            // data class
            it("should be a data class") {
                val value = WordSense(5)
                value.toString() `should be equal to` "WordSense(id=5)"

                val (comp) = value
                comp `should be equal to` 5

                value.copy(id = 6) `should equal` WordSense(6)
            }

            // Serializable
            it("can be serialized") {
                var outputBytes: ByteArray? = null
                val value = WordSense(5);

                {
                    val output = ByteArrayOutputStream(10240)
                    val serializer = ObjectOutputStream(output)
                    serializer.writeObject(value)
                    serializer.close()

                    outputBytes = output.toByteArray()
                    output.close()
                } `should not throw` AnyException

                {
                    val serializer = ObjectInputStream(ByteArrayInputStream(outputBytes))
                    serializer.readObject() as WordSense `should equal` value
                    serializer.close()
                } `should not throw` AnyException
            }
        }

        describe("ListProperty") {
            val dummy1 = ListProperty(mutableListOf(WordSense(1), WordSense(2), WordSense(3)))
            val dummy2 = ListProperty(mutableListOf(WordSense(1), WordSense(2), WordSense(3)))
            val dummy3 = ListProperty(mutableListOf(WordSense(1), WordSense(3), WordSense(2)))
            val dummy4 = ListProperty(mutableListOf(WordSense(1), WordSense(3)))

            // children
            it("should correctly handle children") {
                dummy1[0] `should equal` WordSense(1)
                dummy1[1] `should equal` WordSense(2)
                dummy1[2] `should equal` WordSense(3)
                dummy1[0] `should not equal` dummy1[1]
            }

            it("should find index of given list entry") {
                dummy1.indexOf(dummy1[0]) `should be equal to` 0
                dummy1.indexOf(dummy1[1]) `should be equal to` 1

                dummy1.indexOf(dummy3[0]) `should be equal to` 0
                dummy1.indexOf(dummy3[1]) `should be equal to` 2

                dummy1.lastIndexOf(dummy1[0]) `should be equal to` 0
                dummy1.lastIndexOf(dummy1[1]) `should be equal to` 1

                dummy1.lastIndexOf(dummy3[0]) `should be equal to` 0
                dummy1.lastIndexOf(dummy3[1]) `should be equal to` 2

                (dummy2[0] in dummy1) `should be` true
                (WordSense(7) in dummy1) `should be` false
            }

            // equal, hashcode
            it("can discriminate each other") {
                // Reflexive
                dummy1 `should equal` dummy1

                // Symmetry
                dummy1 `should equal` dummy2
                dummy2 `should equal` dummy1

                dummy1 `should not equal` dummy3
                dummy3 `should not equal` dummy1

                dummy1 `should not equal` dummy4
                dummy4 `should not equal` dummy1

                dummy1 `should not equal` dummy1[0]
            }

            it("can provide correct hashcode") {
                dummy1.hashCode() `should be equal to` dummy1.hashCode()
                dummy1.hashCode() `should be equal to` dummy2.hashCode()
            }

            // Serializable
            it("can be serialized") {
                var outputBytes: ByteArray? = null

                {
                    val output = ByteArrayOutputStream(1024)
                    val serializer = ObjectOutputStream(output)
                    serializer.writeObject(dummy1)
                    serializer.writeObject(dummy2)
                    serializer.close()

                    outputBytes = output.toByteArray()
                    output.close()
                } `should not throw` AnyException

                @Suppress("UNCHECKED_CAST")
                {
                    val serializer = ObjectInputStream(ByteArrayInputStream(outputBytes))
                    serializer.readObject() as ListProperty<WordSense> `should equal` dummy1
                    serializer.readObject() as ListProperty<WordSense> `should equal` dummy2
                    serializer.close()
                } `should not throw` AnyException
            }
        }

        describe("SyntaxTree") {
            val dummy1 = SyntaxTree(
                    PhraseTag.S,
                    null, listOf(
                    SyntaxTree(PhraseTag.NP, null, listOf(
                            SyntaxTree(PhraseTag.DP, sent2[0], originalLabel = "DP"),
                            SyntaxTree(PhraseTag.NP, sent2[1], originalLabel = "NP")
                    )),
                    SyntaxTree(PhraseTag.VP, null, listOf(
                            SyntaxTree(PhraseTag.NP, sent2[2]),
                            SyntaxTree(PhraseTag.VP, sent2[3])
                    )))
            )

            val dummy2 = SyntaxTree(
                    PhraseTag.S,
                    null, listOf(
                    SyntaxTree(PhraseTag.NP, null, listOf(
                            SyntaxTree(PhraseTag.DP, sent3[0], originalLabel = "dp"),
                            SyntaxTree(PhraseTag.NP, sent3[1], originalLabel = "np")
                    )),
                    SyntaxTree(PhraseTag.VP, null, listOf(
                            SyntaxTree(PhraseTag.NP, sent3[2]),
                            SyntaxTree(PhraseTag.VP, sent3[3])
                    )))
            )

            // getChildren, getParent, isRoot, hasNonTerminals, getTerminals
            it("should provide ways to access its children") {
                dummy1[0].label `should equal` PhraseTag.NP
                dummy1[1].label `should equal` PhraseTag.VP
                dummy1[0][0].label `should equal` PhraseTag.DP

                dummy1[0][0].originalLabel `should equal` "DP"
                dummy1[0][1].originalLabel `should equal` "NP"
                dummy2[0][0].originalLabel `should equal` "dp"
                dummy2[0][1].originalLabel `should equal` "np"
                dummy1[0].originalLabel `should be` null

                dummy1[0].hasNonTerminals() `should be` true
                dummy1[0][0].hasNonTerminals() `should be` false
            }

            it("should provide ways to access its parents") {
                dummy1.isRoot() `should be` true
                dummy1[0].isRoot() `should be` false
                dummy1[0][0].isRoot() `should be` false

                dummy1[0].getParent() `should equal` dummy1
                dummy1[0][0].getParent() `should equal` dummy1[0]
                dummy1[0][1].getParent() `should equal` dummy1[0]
            }

            it("provide ways to access terminal nodes, i.e. words") {
                dummy1.getTerminals() `should contain all` sent2
                dummy1[0].getTerminals() `should contain all` sent2.subList(0, 2)
                dummy1[1].getTerminals() `should contain all` sent2.subList(2, 4)
                dummy1[0][0].getTerminals() `should contain all` listOf(sent2[0])
            }

            // equal, hashcode
            it("can discriminate each other") {
                // Reflexive
                dummy1 `should equal` dummy1
                dummy1 shouldContainSame dummy1.getNonTerminals()

                // Symmetry
                dummy1 `should equal` dummy2
                dummy2 `should equal` dummy1

                dummy1 `should not equal` dummy1[0]
                dummy1[0] `should not equal` dummy1[1]
                dummy1 `should not equal` sent2[0]
            }

            it("can provide correct hashcode") {
                dummy1.hashCode() `should be equal to` dummy1.hashCode()
                dummy1.hashCode() `should be equal to` dummy2.hashCode()
            }

            // List<SyntaxTree>
            it("should find index of given tree") {
                dummy1.indexOf(dummy1[0]) `should be equal to` 0
                dummy1.indexOf(dummy1[1]) `should be equal to` 1

                dummy1.indexOf(dummy2[0]) `should be equal to` 0
                dummy1.indexOf(dummy2[1]) `should be equal to` 1

                dummy1.lastIndexOf(dummy1[0]) `should be equal to` 0
                dummy1.lastIndexOf(dummy1[1]) `should be equal to` 1

                dummy1.lastIndexOf(dummy2[0]) `should be equal to` 0
                dummy1.lastIndexOf(dummy2[1]) `should be equal to` 1

                (dummy2[0] in dummy1) `should be` true
                (dummy2[0][1] in dummy1) `should be` false
            }

            // toString, getTreeString
            it("should provide the correct string representation") {
                dummy1.toString() `should be equal to` "S-Node()"
                dummy1[0].toString() `should be equal to` "NP-Node()"
                dummy1[0][0].toString() `should be equal to` "DP-Node(흰 = 희/VA+ㄴ/ETM)"
            }

            it("should provide tree representation") {
                dummy1.getTreeString().toString() `should be equal to` """
                    >S-Node()
                    >| NP-Node()
                    >| | DP-Node(흰 = 희/VA+ㄴ/ETM)
                    >| | NP-Node(밥을 = 밥/NNG+을/JKO)
                    >| VP-Node()
                    >| | NP-Node(나는 = 나/NP+는/JX)
                    >| | VP-Node(먹었다 = 먹/VV+었/EP+다/EF)
                """.trimMargin(">").trim()

                dummy1[0].getTreeString().toString() `should be equal to` """
                    >NP-Node()
                    >| DP-Node(흰 = 희/VA+ㄴ/ETM)
                    >| NP-Node(밥을 = 밥/NNG+을/JKO)
                """.trimMargin(">").trim()
            }

            // type, leaf
            it("should have correct information") {
                dummy1.label `should equal` PhraseTag.S

                dummy1.terminal `should equal` null
                dummy1[0].terminal `should equal` null
                dummy1[0][0].terminal `should equal` sent2[0]
            }

            // Serializable
            it("can be serialized") {
                var outputBytes: ByteArray? = null

                {
                    val output = ByteArrayOutputStream(10240)
                    val serializer = ObjectOutputStream(output)
                    serializer.writeObject(dummy1)
                    serializer.writeObject(dummy2)
                    serializer.close()

                    outputBytes = output.toByteArray()
                    output.close()
                } `should not throw` AnyException

                @Suppress("UNCHECKED_CAST")
                {
                    val serializer = ObjectInputStream(ByteArrayInputStream(outputBytes))
                    serializer.readObject() as SyntaxTree `should equal` dummy1
                    serializer.readObject() as SyntaxTree `should equal` dummy2
                    serializer.close()
                } `should not throw` AnyException
            }
        }

        describe("DepEdge") {
            val dummy1 = DepEdge(sent2[3], sent2[1], PhraseTag.NP, DependencyTag.OBJ)
            val dummy2 = DepEdge(sent3[3], sent3[1], PhraseTag.NP, DependencyTag.OBJ, "NPobj")
            val dummy3 = DepEdge(sent3[3], sent3[2], PhraseTag.NP, DependencyTag.SBJ)
            val dummy4 = DepEdge(sent3[1], sent3[0], PhraseTag.DP)
            val dummy5 = DepEdge(null, sent3[3], PhraseTag.VP, DependencyTag.ROOT)

            // governor, dependent, type, depTag
            // label, src, dest
            it("should handle its property") {
                dummy1.src `should equal` sent2[3]
                dummy1.governor `should equal` dummy1.src

                dummy1.dest `should equal` sent2[1]
                dummy1.dependent `should equal` dummy1.dest

                dummy1.type `should equal` PhraseTag.NP
                dummy1.depType `should equal` DependencyTag.OBJ
                dummy1.label `should equal` DependencyTag.OBJ

                dummy1.originalLabel `should be` null
                dummy2.originalLabel `should equal` "NPobj"
            }

            // toString
            it("should provide the correct string representation") {
                dummy1.toString() `should be equal to` "NPOBJ('먹었다 = 먹/VV+었/EP+다/EF' → '밥을 = 밥/NNG+을/JKO')"
                dummy3.toString() `should be equal to` "NPSBJ('먹었다 = 먹/VV+었/EP+다/EF' → '나는 = 나/NP+는/JX')"
                dummy4.toString() `should be equal to` "DP('밥을 = 밥/NNG+을/JKO' → '흰 = 희/VA+ㄴ/ETM')"
                dummy5.toString() `should be equal to` "VPROOT('ROOT' → '먹었다 = 먹/VV+었/EP+다/EF')"
            }

            // equal, hashcode
            it("can discriminate each other") {
                // Reflexive
                dummy1 `should equal` dummy1

                // Symmetry
                dummy1 `should equal` dummy2
                dummy2 `should equal` dummy1

                dummy1 `should not equal` dummy3
                dummy1 `should not equal` dummy4
                dummy1 `should not equal` dummy1.src
            }

            it("can provide correct hashcode") {
                dummy1.hashCode() `should be equal to` dummy1.hashCode()
                dummy1.hashCode() `should be equal to` dummy2.hashCode()
            }

            // Serializable
            it("can be serialized") {
                var outputBytes: ByteArray? = null

                {
                    val output = ByteArrayOutputStream(10240)
                    val serializer = ObjectOutputStream(output)
                    serializer.writeObject(dummy1)
                    serializer.writeObject(dummy2)
                    serializer.close()

                    outputBytes = output.toByteArray()
                    output.close()
                } `should not throw` AnyException

                @Suppress("UNCHECKED_CAST")
                {
                    val serializer = ObjectInputStream(ByteArrayInputStream(outputBytes))
                    serializer.readObject() as DepEdge `should equal` dummy1
                    serializer.readObject() as DepEdge `should equal` dummy2
                    serializer.close()
                } `should not throw` AnyException
            }
        }

        describe("RoleEdge") {
            val dummy1 = RoleEdge(sent2[3], sent2[1], RoleType.ARG1, modifiers = listOf(sent2[0]))
            val dummy2 = RoleEdge(sent3[3], sent3[1], RoleType.ARG1, originalLabel = "ARG-1")
            val dummy3 = RoleEdge(sent3[3], sent3[2], RoleType.ARG0)
            val dummy4 = RoleEdge(sent3[1], sent3[0], RoleType.ARGM_PRD)

            // predicate, argument, label
            // label, src, dest
            it("should handle its property") {
                dummy1.src `should equal` sent2[3]
                dummy1.predicate `should equal` dummy1.src

                dummy1.dest `should equal` sent2[1]
                dummy1.argument `should equal` dummy1.dest

                dummy1.label `should equal` RoleType.ARG1

                dummy1.modifiers[0] `should equal` sent2[0]

                dummy1.originalLabel `should be` null
                dummy2.originalLabel `should equal` "ARG-1"
            }

            // toString
            it("should provide the correct string representation") {
                dummy1.toString() `should be equal to` "ARG1('먹었다' → '밥을/흰')"
                dummy3.toString() `should be equal to` "ARG0('먹었다' → '나는/')"
                dummy4.toString() `should be equal to` "ARGM_PRD('밥을' → '흰/')"
            }

            // equal, hashcode
            it("can discriminate each other") {
                // Reflexive
                dummy1 `should equal` dummy1

                // Symmetry
                dummy1 `should equal` dummy2
                dummy2 `should equal` dummy1

                dummy1 `should not equal` dummy3
                dummy1 `should not equal` dummy4
                dummy1 `should not equal` dummy1.src
            }

            it("can provide correct hashcode") {
                dummy1.hashCode() `should be equal to` dummy1.hashCode()
                dummy1.hashCode() `should be equal to` dummy2.hashCode()
            }

            // Serializable
            it("can be serialized") {
                var outputBytes: ByteArray? = null

                {
                    val output = ByteArrayOutputStream(10240)
                    val serializer = ObjectOutputStream(output)
                    serializer.writeObject(dummy1)
                    serializer.writeObject(dummy2)
                    serializer.close()

                    outputBytes = output.toByteArray()
                    output.close()
                } `should not throw` AnyException

                @Suppress("UNCHECKED_CAST")
                {
                    val serializer = ObjectInputStream(ByteArrayInputStream(outputBytes))
                    serializer.readObject() as RoleEdge `should equal` dummy1
                    serializer.readObject() as RoleEdge `should equal` dummy2
                    serializer.close()
                } `should not throw` AnyException
            }
        }

        describe("Entity") {
            val dummy1 = Entity("나", CoarseEntityType.PS, "PS_OTHER", listOf(sent3[2][0]))
            val dummy2 = Entity("나", CoarseEntityType.PS, "PS_OTHER", listOf(sent3[2][0]))
            val dummy3 = Entity("나", CoarseEntityType.PS, "PS_DIFF", listOf(sent3[2][0]))
            val dummy4 = Entity("흰 밥", CoarseEntityType.PS, "PS_OTHER", sent3[0] + sent3[1])

            //type, fineType
            //words
            //surface
            it("has correct property") {
                dummy1.label `should equal` CoarseEntityType.PS
                dummy1.fineLabel `should be equal to` "PS_OTHER"
                dummy1[0] `should equal` sent3[2][0]

                dummy1.surface `should equal` "나"
                dummy4.surface `should equal` "흰 밥"

                (sent3[2][0] in dummy1) `should be` true
                (sent3[1][0] in dummy1) `should be` false

                dummy1.getCorefGroup() `should equal` null
            }

            // equal, hashcode
            it("can discriminate each other") {
                // Reflexive
                dummy1 `should equal` dummy1

                // Symmetry
                dummy1 `should equal` dummy2
                dummy2 `should equal` dummy1

                dummy1 `should not equal` dummy3
                dummy1 `should not equal` dummy4
                dummy1 `should not equal` dummy1[0]
            }

            it("can provide correct hashcode") {
                dummy1.hashCode() `should be equal to` dummy1.hashCode()
                dummy1.hashCode() `should be equal to` dummy2.hashCode()
            }

            //getCorefGroup
            it("can be serialized") {
                var outputBytes: ByteArray? = null

                {
                    val output = ByteArrayOutputStream(10240)
                    val serializer = ObjectOutputStream(output)
                    serializer.writeObject(dummy1)
                    serializer.writeObject(dummy4)
                    serializer.close()

                    outputBytes = output.toByteArray()
                    output.close()
                } `should not throw` AnyException

                @Suppress("UNCHECKED_CAST")
                {
                    val serializer = ObjectInputStream(ByteArrayInputStream(outputBytes))
                    serializer.readObject() as Entity `should equal` dummy1
                    serializer.readObject() as Entity `should equal` dummy4
                    serializer.close()
                } `should not throw` AnyException
            }
        }

        describe("CorefrerenceGroup") {
            val dummy1 = CoreferenceGroup(listOf(Entity("나", CoarseEntityType.PS, "PS_OTHER", listOf(sent3[2][0]))))
            val dummy2 = CoreferenceGroup(listOf(Entity("나", CoarseEntityType.PS, "PS_OTHER", listOf(sent3[2][0]))))
            val dummy3 = CoreferenceGroup(listOf(Entity("나", CoarseEntityType.PS, "PS_DIFF", listOf(sent3[2][0]))))
            val dummy4 = CoreferenceGroup(listOf(Entity("흰 밥", CoarseEntityType.PS, "PS_OTHER", sent3[0] + sent3[1])))

            it("should inherit list") {
                (dummy1[0] in dummy1) `should be` true
                (dummy3[0] in dummy1) `should be` false
                dummy1.indexOf(dummy1[0]) `should equal` 0
                dummy1.lastIndexOf(dummy1[0]) `should equal` 0
            }

            // equal, hashcode
            it("can discriminate each other") {
                // Reflexive
                dummy1 `should equal` dummy1

                // Symmetry
                dummy1 `should equal` dummy2
                dummy2 `should equal` dummy1

                dummy1 `should not equal` dummy3
                dummy1 `should not equal` dummy4
                dummy1 `should not equal` dummy1[0]

                dummy1[0].getCorefGroup() `should equal` dummy1
                dummy1[0].getCorefGroup() `should equal` dummy2
            }

            it("can provide correct hashcode") {
                dummy1.hashCode() `should be equal to` dummy1.hashCode()
                dummy1.hashCode() `should be equal to` dummy2.hashCode()
            }

            //Serializable
            it("can be serialized") {
                var outputBytes: ByteArray? = null

                {
                    val output = ByteArrayOutputStream(10240)
                    val serializer = ObjectOutputStream(output)
                    serializer.writeObject(dummy1)
                    serializer.writeObject(dummy4)
                    serializer.close()

                    outputBytes = output.toByteArray()
                    output.close()
                } `should not throw` AnyException

                @Suppress("UNCHECKED_CAST")
                {
                    val serializer = ObjectInputStream(ByteArrayInputStream(outputBytes))
                    serializer.readObject() as CoreferenceGroup `should equal` dummy1
                    serializer.readObject() as CoreferenceGroup `should equal` dummy4
                    serializer.close()
                } `should not throw` AnyException
            }
        }
    }
})