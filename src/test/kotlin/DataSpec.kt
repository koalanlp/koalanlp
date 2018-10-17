package kr.bydelta.koala

import org.amshove.kluent.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object DataSpec : Spek({
    describe("data.kt") {
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
                { dummy1.setProperty(IntProperty(1)) } `should not throw` AnyException
                { dummy2.setProperty(IntProperty(2)) } `should not throw` AnyException

                { sent2[0][0].getWordSense() } `should throw` UninitializedPropertyAccessException::class
                dummy1.getWordSense() `should be equal to` 1
                dummy2.getWordSense() `should be equal to` 2

                dummy1.getWordSense() `should be` dummy1.getProperty<IntProperty>(KEY_WORDSENSE)?.value
                dummy2.getWordSense() `should be` dummy2.getProperty<IntProperty>(KEY_WORDSENSE)?.value
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
                { dummy1.getEntities() } `should throw` UninitializedPropertyAccessException::class
                { dummy1.getPhrase() } `should throw` UninitializedPropertyAccessException::class
                { dummy1.getDependency() } `should throw` UninitializedPropertyAccessException::class
                { dummy1.getRole() } `should throw` UninitializedPropertyAccessException::class

                // All these trees automatically set pointers on the words.
                Entity(CoarseEntityType.PS, "PS_OTHER", listOf(dummy1))
                SyntaxTree(PhraseTag.NP, dummy1)
                DepTree(dummy1, PhraseTag.NP, DependencyTag.SBJ)
                RoleTree(dummy1, RoleType.ARG0)

                dummy1.getEntities()[0].fineType `should be equal to` "PS_OTHER"
                dummy1.getPhrase().type `should equal` PhraseTag.NP
                dummy1.getDependency().depType `should equal` DependencyTag.SBJ
                dummy1.getRole().type `should equal` RoleType.ARG0
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

            // setProperty, getSyntaxTree, getDependencyTree, getRoleTree, getNamedEntities
            it("should provide proper way to set a property") {
                { sent.getNamedEntities() } `should throw` UninitializedPropertyAccessException::class
                { sent.getSyntaxTree() } `should throw` UninitializedPropertyAccessException::class
                { sent.getDependencyTree() } `should throw` UninitializedPropertyAccessException::class
                { sent.getSemRoleTree() } `should throw` UninitializedPropertyAccessException::class

                // All these trees automatically set pointers on the words.
                sent.setListProperty(Entity(CoarseEntityType.PS, "PS_OTHER", listOf(sent[0])))
                sent.setProperty(SyntaxTree(PhraseTag.S, children = listOf(
                        SyntaxTree(PhraseTag.NP, sent[0]),
                        SyntaxTree(PhraseTag.NP, sent[1])
                )))
                sent.setProperty(DepTree(
                        sent[1], PhraseTag.S, DependencyTag.ROOT,
                        DepTree(sent[0], PhraseTag.NP, DependencyTag.SBJ)
                ))
                sent.setProperty(RoleTree(
                        sent[1], RoleType.HEAD,
                        RoleTree(sent[0], RoleType.ARG0)
                ))

                sent.getNamedEntities()[0].fineType `should be equal to` "PS_OTHER"
                sent.getSyntaxTree().type `should equal` PhraseTag.S
                sent.getDependencyTree().depType `should equal` DependencyTag.ROOT
                sent.getSemRoleTree().type `should equal` RoleType.HEAD
            }

            // getNouns, getModifiers, getVerbs
            it("should provide proper list of nouns") {
                sent2.getNouns() `should contain all` listOf(sent2[1], sent2[2])
            }

            it("should provide proper list of verbs") {
                sent2.getVerbs() `should contain all` listOf(sent2[0], sent2[3])
            }

            it("should provide proper list of modifiers") {
                sent2.getModifiers() `should contain all` listOf(sent2[0])
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
                sent2 `should not equal` sent

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

        describe("IntProperty") {
            // value
            it("should contain an integer value") {
                val value = IntProperty(5)
                value.value `should be equal to` 5

                val value2 = IntProperty(7)
                value2.value `should be equal to` 7
            }

            // data class
            it("should be a data class") {
                val value = IntProperty(5)
                value.toString() `should be equal to` "IntProperty(value=5)"

                val (comp) = value
                comp `should be equal to` 5

                value.copy(value = 6) `should equal` IntProperty(6)
            }

            // Serializable
            it("can be serialized") {
                var outputBytes: ByteArray? = null
                val value = IntProperty(5);

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
                    serializer.readObject() as IntProperty `should equal` value
                    serializer.close()
                } `should not throw` AnyException
            }
        }

        describe("ListProperty") {
            val dummy1 = ListProperty(listOf(IntProperty(1), IntProperty(2), IntProperty(3)))
            val dummy2 = ListProperty(listOf(IntProperty(1), IntProperty(2), IntProperty(3)))
            val dummy3 = ListProperty(listOf(IntProperty(1), IntProperty(3), IntProperty(2)))

            // children
            it("should correctly handle children") {
                dummy1[0] `should equal` IntProperty(1)
                dummy1[1] `should equal` IntProperty(2)
                dummy1[2] `should equal` IntProperty(3)
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
                (IntProperty(7) in dummy1) `should be` false
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
                    serializer.readObject() as ListProperty<IntProperty> `should equal` dummy1
                    serializer.readObject() as ListProperty<IntProperty> `should equal` dummy2
                    serializer.close()
                } `should not throw` AnyException
            }
        }

        describe("Entity") {
            val dummy1 = Entity(CoarseEntityType.PS, "PS_OTHER", listOf(sent2[2]))
            val dummy2 = Entity(CoarseEntityType.PS, "PS_OTHER", listOf(sent2[2]))
            val dummy3 = Entity(CoarseEntityType.PL, "PL_OTHER", listOf(sent2[0], sent2[1]))

            // List<Word>
            it("should find index of given word") {
                dummy3.indexOf(dummy3[0]) `should be equal to` 0
                dummy3.indexOf(dummy3[1]) `should be equal to` 1

                dummy1.indexOf(sent[0]) `should be equal to` 0
                dummy1.indexOf(dummy2[0]) `should be equal to` 0

                dummy3.lastIndexOf(dummy3[0]) `should be equal to` 0
                dummy3.lastIndexOf(dummy3[1]) `should be equal to` 1

                dummy1.lastIndexOf(sent[0]) `should be equal to` 0
                dummy1.lastIndexOf(dummy2[0]) `should be equal to` 0

                (dummy2[0] in dummy1) `should be` true
                (sent2[0] in dummy1) `should be` false
            }

            // type, fineType, words, surface
            it("should have correct type") {
                dummy1.type `should equal` CoarseEntityType.PS
                dummy2.type `should equal` CoarseEntityType.PS
                dummy3.type `should equal` CoarseEntityType.PL

                dummy1.fineType `should be equal to` "PS_OTHER"
                dummy2.fineType `should be equal to` "PS_OTHER"
                dummy3.fineType `should be equal to` "PL_OTHER"
            }

            it("should provide list of words forming it") {
                dummy1.words `should equal` listOf(sent2[2])
                dummy3.words `should equal` listOf(sent2[0], sent2[1])

                dummy1[0] `should equal` sent2[2]
                dummy3[1] `should equal` sent2[1]
            }

            it("should provide correct surface form") {
                dummy1.surface `should be equal to` "나는"
                dummy2.surface `should be equal to` "나는"
                dummy3.surface `should be equal to` "흰 밥을"
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

                dummy1 `should not equal` dummy1[0]
            }

            it("can provide correct hashcode") {
                dummy1.hashCode() `should be equal to` dummy1.hashCode()
                dummy1.hashCode() `should be equal to` dummy2.hashCode()
            }

            // toString
            it("should provide the correct string representation") {
                dummy1.toString() `should be equal to` "PS(PS_OTHER; '나는')"
                dummy2.toString() `should be equal to` "PS(PS_OTHER; '나는')"
                dummy3.toString() `should be equal to` "PL(PL_OTHER; '흰 밥을')"
            }

            // Serializable
            it("can be serialized") {
                var outputBytes: ByteArray? = null

                {
                    val output = ByteArrayOutputStream(1024)
                    val serializer = ObjectOutputStream(output)
                    serializer.writeObject(dummy1)
                    serializer.writeObject(dummy2)
                    serializer.writeObject(dummy3)
                    serializer.close()

                    outputBytes = output.toByteArray()
                    output.close()
                } `should not throw` AnyException

                @Suppress("UNCHECKED_CAST")
                {
                    val serializer = ObjectInputStream(ByteArrayInputStream(outputBytes))
                    serializer.readObject() as Entity `should equal` dummy1
                    serializer.readObject() as Entity `should equal` dummy2
                    serializer.readObject() as Entity `should equal` dummy3
                    serializer.close()
                } `should not throw` AnyException
            }
        }

        describe("SyntaxTree") {
            val dummy1 = SyntaxTree(
                    PhraseTag.S,
                    null,
                    SyntaxTree(PhraseTag.NP, null,
                            SyntaxTree(PhraseTag.DP, sent2[0]),
                            SyntaxTree(PhraseTag.NP, sent2[1])
                    ),
                    SyntaxTree(PhraseTag.VP, null,
                            SyntaxTree(PhraseTag.NP, sent2[2]),
                            SyntaxTree(PhraseTag.VP, sent2[3])
                    )
            )

            val dummy2 = SyntaxTree(
                    PhraseTag.S,
                    null,
                    SyntaxTree(PhraseTag.NP, null,
                            SyntaxTree(PhraseTag.DP, sent3[0]),
                            SyntaxTree(PhraseTag.NP, sent3[1])
                    ),
                    SyntaxTree(PhraseTag.VP, null,
                            SyntaxTree(PhraseTag.NP, sent3[2]),
                            SyntaxTree(PhraseTag.VP, sent3[3])
                    )
            )

            // getChildren, getParent, isRoot, hasNonTerminals, getTerminals
            it("should provide ways to access its children") {
                dummy1[0].type `should equal` PhraseTag.NP
                dummy1[1].type `should equal` PhraseTag.VP
                dummy1[0][0].type `should equal` PhraseTag.DP

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

                // Symmetry
                dummy1 `should equal` dummy2
                dummy2 `should equal` dummy1

                dummy1 `should not equal` dummy1[0]
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
                dummy1.type `should equal` PhraseTag.S

                dummy1.leaf `should equal` null
                dummy1[0].leaf `should equal` null
                dummy1[0][0].leaf `should equal` sent2[0]
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

        describe("DepTree") {
            val dummy1 = DepTree(
                    sent2[3],
                    PhraseTag.S,
                    DependencyTag.ROOT,
                    DepTree(sent2[1], PhraseTag.NP, DependencyTag.OBJ,
                            DepTree(sent2[0], PhraseTag.DP)
                    ),
                    DepTree(sent2[2], PhraseTag.NP, DependencyTag.SBJ)
            )

            val dummy2 = DepTree(
                    sent3[3],
                    PhraseTag.S,
                    DependencyTag.ROOT,
                    DepTree(sent3[1], PhraseTag.NP, DependencyTag.OBJ,
                            DepTree(sent3[0], PhraseTag.DP)
                    ),
                    DepTree(sent3[2], PhraseTag.NP, DependencyTag.SBJ)
            )

            // getChildren, getParent, isRoot, hasNonTerminals, getTerminals
            it("should provide ways to access its children") {
                dummy1[0].type `should equal` PhraseTag.NP
                dummy1[1].type `should equal` PhraseTag.NP
                dummy1[0][0].type `should equal` PhraseTag.DP

                dummy1[0].hasNonTerminals() `should be` true
                dummy1[0][0].hasNonTerminals() `should be` false
            }

            it("should provide ways to access its parents") {
                dummy1.isRoot() `should be` true
                dummy1[0].isRoot() `should be` false
                dummy1[0][0].isRoot() `should be` false

                dummy1[0].getParent() `should equal` dummy1
                dummy1[0][0].getParent() `should equal` dummy1[0]
                dummy1[1].getParent() `should equal` dummy1
            }

            it("provide ways to access terminal nodes, i.e. words") {
                dummy1.getTerminals() `should contain all` sent2
                dummy1[0].getTerminals() `should contain all` sent2.subList(0, 2)
                dummy1[1].getTerminals() `should contain all` listOf(sent2[2])
                dummy1[0][0].getTerminals() `should contain all` listOf(sent2[0])
            }

            // equal, hashcode
            it("can discriminate each other") {
                // Reflexive
                dummy1 `should equal` dummy1

                // Symmetry
                dummy1 `should equal` dummy2
                dummy2 `should equal` dummy1

                dummy1 `should not equal` dummy1[0]
                dummy1 `should not equal` dummy1.leaf

                dummy1[0][0] `should equal` dummy2[0][0]
            }

            it("can provide correct hashcode") {
                dummy1.hashCode() `should be equal to` dummy1.hashCode()
                dummy1.hashCode() `should be equal to` dummy2.hashCode()
            }

            // toString, getTreeString
            it("should provide the correct string representation") {
                dummy1.toString() `should be equal to` "S-ROOT-Node(먹었다 = 먹/VV+었/EP+다/EF)"
                dummy1[0].toString() `should be equal to` "NP-OBJ-Node(밥을 = 밥/NNG+을/JKO)"
                dummy1[0][0].toString() `should be equal to` "DP-Node(흰 = 희/VA+ㄴ/ETM)"
            }

            it("should provide tree representation") {
                dummy1.getTreeString().toString() `should be equal to` """
                    >S-ROOT-Node(먹었다 = 먹/VV+었/EP+다/EF)
                    >| NP-OBJ-Node(밥을 = 밥/NNG+을/JKO)
                    >| | DP-Node(흰 = 희/VA+ㄴ/ETM)
                    >| NP-SBJ-Node(나는 = 나/NP+는/JX)
                """.trimMargin(">").trim()

                dummy1[0].getTreeString().toString() `should be equal to` """
                    >NP-OBJ-Node(밥을 = 밥/NNG+을/JKO)
                    >| DP-Node(흰 = 희/VA+ㄴ/ETM)
                """.trimMargin(">").trim()
            }

            // type, depType, leaf, head
            it("should have correct information") {
                dummy1.type `should equal` PhraseTag.S
                dummy1.depType `should equal` DependencyTag.ROOT
                dummy1[0].depType `should equal` DependencyTag.OBJ
                dummy1[0][0].depType `should equal` null

                dummy1.leaf `should equal` sent2[3]
                dummy1[0].leaf `should equal` sent2[1]
                dummy1[0][0].leaf `should equal` sent2[0]

                dummy1.leaf `should equal` dummy1.head
                dummy1[0].leaf `should equal` dummy1[0].head
                dummy1[0][0].leaf `should equal` dummy1[0][0].head
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
                    serializer.readObject() as DepTree `should equal` dummy1
                    serializer.readObject() as DepTree `should equal` dummy2
                    serializer.close()
                } `should not throw` AnyException
            }
        }

        describe("RoleTree") {
            val dummy1 = RoleTree(
                    sent2[3],
                    RoleType.HEAD,
                    RoleTree(sent2[1], RoleType.ARG1,
                            RoleTree(sent2[0], RoleType.ARGM_PRD)
                    ),
                    RoleTree(sent2[2], RoleType.ARG0)
            )

            val dummy2 = RoleTree(
                    sent3[3],
                    RoleType.HEAD,
                    RoleTree(sent3[1], RoleType.ARG1,
                            RoleTree(sent3[0], RoleType.ARGM_PRD)
                    ),
                    RoleTree(sent3[2], RoleType.ARG0)
            )

            // getChildren, getParent, isRoot, hasNonTerminals, getTerminals
            it("should provide ways to access its children") {
                dummy1[0].type `should equal` RoleType.ARG1
                dummy1[1].type `should equal` RoleType.ARG0
                dummy1[0][0].type `should equal` RoleType.ARGM_PRD

                dummy1[0].hasNonTerminals() `should be` true
                dummy1[0][0].hasNonTerminals() `should be` false
            }

            it("should provide ways to access its parents") {
                dummy1.isRoot() `should be` true
                dummy1[0].isRoot() `should be` false
                dummy1[0][0].isRoot() `should be` false

                dummy1[0].getParent() `should equal` dummy1
                dummy1[0][0].getParent() `should equal` dummy1[0]
                dummy1[1].getParent() `should equal` dummy1
            }

            it("provide ways to access terminal nodes, i.e. words") {
                dummy1.getTerminals() `should contain all` sent2
                dummy1[0].getTerminals() `should contain all` sent2.subList(0, 2)
                dummy1[1].getTerminals() `should contain all` listOf(sent2[2])
                dummy1[0][0].getTerminals() `should contain all` listOf(sent2[0])
            }

            // equal, hashcode
            it("can discriminate each other") {
                // Reflexive
                dummy1 `should equal` dummy1

                // Symmetry
                dummy1 `should equal` dummy2
                dummy2 `should equal` dummy1

                dummy1 `should not equal` dummy1[0]
                dummy1 `should not equal` dummy1.leaf
            }

            it("can provide correct hashcode") {
                dummy1.hashCode() `should be equal to` dummy1.hashCode()
                dummy1.hashCode() `should be equal to` dummy2.hashCode()
            }

            // toString, getTreeString
            it("should provide the correct string representation") {
                dummy1.toString() `should be equal to` "HEAD-Node(먹었다 = 먹/VV+었/EP+다/EF)"
                dummy1[0].toString() `should be equal to` "ARG1-Node(밥을 = 밥/NNG+을/JKO)"
                dummy1[0][0].toString() `should be equal to` "ARGM_PRD-Node(흰 = 희/VA+ㄴ/ETM)"
            }

            it("should provide tree representation") {
                dummy1.getTreeString().toString() `should be equal to` """
                    >HEAD-Node(먹었다 = 먹/VV+었/EP+다/EF)
                    >| ARG1-Node(밥을 = 밥/NNG+을/JKO)
                    >| | ARGM_PRD-Node(흰 = 희/VA+ㄴ/ETM)
                    >| ARG0-Node(나는 = 나/NP+는/JX)
                >""".trimMargin(">").trim()

                dummy1[0].getTreeString().toString() `should be equal to` """
                    >ARG1-Node(밥을 = 밥/NNG+을/JKO)
                    >| ARGM_PRD-Node(흰 = 희/VA+ㄴ/ETM)
                """.trimMargin(">").trim()
            }

            // type, leaf
            it("should have correct information") {
                dummy1.type `should equal` RoleType.HEAD

                dummy1.leaf `should equal` sent2[3]
                dummy1[0].leaf `should equal` sent2[1]
                dummy1[0][0].leaf `should equal` sent2[0]
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
                    serializer.readObject() as RoleTree `should equal` dummy1
                    serializer.readObject() as RoleTree `should equal` dummy2
                    serializer.close()
                } `should not throw` AnyException
            }
        }
    }

    describe("Property") {
        it("should set proper property") {
            val prop = Word();

            {
                prop.setProperty(Entity(CoarseEntityType.EV, "EV_OTHER", listOf(prop)))
                prop.setProperty(ListProperty(emptyList<Entity>()))
            } `should not throw` IllegalArgumentException::class

            {
                prop.setProperty(prop)
            } `should throw` IllegalArgumentException::class

            {
                prop.getProperty<ListProperty<*>>(KEY_CHILD)
            } `should not throw` IllegalStateException::class

        }
    }
})