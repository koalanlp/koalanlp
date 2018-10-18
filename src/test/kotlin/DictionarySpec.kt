package kr.bydelta.koala

import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import kr.bydelta.koala.dic.CanCompileDict
import kr.bydelta.koala.dic.CanExtractResource
import kr.bydelta.koala.dic.DicEntry
import org.amshove.kluent.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.File
import kotlin.streams.toList

object DictionarySpec : Spek({
    describe("CanCompileDict Interface") {
        // addUserDictionary(3 types), plusAssign
        it("add user-defined words") {
            val dict = object : CanCompileDict() {
                val words = mutableSetOf<Pair<String, POS>>()

                override fun addUserDictionary(vararg dict: Pair<String, POS>) {
                    words.addAll(dict)
                }

                override fun getItems(): Set<DicEntry> {
                    return words.toSet()
                }

                override fun getBaseEntries(filter: (POS) -> Boolean): Iterator<DicEntry> {
                    return words.filter { filter(it.second) }.listIterator()
                }

                override fun getNotExists(onlySystemDic: Boolean, vararg word: DicEntry): Array<DicEntry> {
                    return word.toSet().subtract(this.words).toTypedArray()
                }
            }

            // single type
            { dict.addUserDictionary("테팔", POS.NNP) } `should not throw` AnyException
            dict.words.size `should be equal to` 1
            dict.words `should contain` ("테팔" to POS.NNP)

            // Var-args type
            { dict.addUserDictionary("김종국" to POS.NNP, "사랑" to POS.NNG) } `should not throw` AnyException
            dict.words.size `should be equal to` 3
            dict.words `should contain` ("김종국" to POS.NNP)
            dict.words `should contain` ("사랑" to POS.NNG)

            val wordList = mutableListOf<String>()
            val posList = mutableListOf<POS>()
            wordList.add("동병상련")
            wordList.add("지나다")
            posList.add(POS.NNG)
            posList.add(POS.VV)

            dict.addUserDictionary(wordList, posList)
            dict.words.size `should be equal to` 5
            dict.words `should contain` ("동병상련" to POS.NNG)
            dict.words `should contain` ("지나다" to POS.VV)

            dict += ("Aligator Sky" to POS.NNP)
            dict.words.size `should be equal to` 6
            dict.words `should contain` ("Aligator Sky" to POS.NNP)
        }

        val dict1 = object : CanCompileDict() {
            val words = mutableSetOf<Pair<String, POS>>("테팔" to POS.NNP,
                    "김종국" to POS.NNP, "사랑" to POS.NNG, "동병상련" to POS.NNG, "지나다" to POS.VV)

            override fun addUserDictionary(vararg dict: Pair<String, POS>) {
                words.addAll(dict)
            }

            override fun getItems(): Set<DicEntry> {
                return words.toSet()
            }

            override fun getBaseEntries(filter: (POS) -> Boolean): Iterator<DicEntry> {
                return words.filter { filter(it.second) }.listIterator()
            }

            override fun getNotExists(onlySystemDic: Boolean, vararg word: DicEntry): Array<DicEntry> {
                return word.toSet().subtract(this.words).toTypedArray()
            }
        }

        val dict2 = object : CanCompileDict() {
            val words = mutableSetOf<Pair<String, POS>>()

            override fun addUserDictionary(vararg dict: Pair<String, POS>) {
                words.addAll(dict)
            }

            override fun getItems(): Set<DicEntry> {
                return words.toSet()
            }

            override fun getBaseEntries(filter: (POS) -> Boolean): Iterator<DicEntry> {
                return words.filter { filter(it.second) }.listIterator()
            }

            override fun getNotExists(onlySystemDic: Boolean, vararg word: DicEntry): Array<DicEntry> {
                return word.toSet().subtract(this.words).toTypedArray()
            }
        }

        // getItems, getBaseEntries, getNotExists: Abstract (depends on the actual implementation)
        // contains
        it("check whether dictionary has a word") {
            dict1.contains("지나다", setOf(POS.VA)) `should be` false
            dict1.contains("지나다", setOf(POS.VV)) `should be` true
            dict1.contains("김종국", setOf(POS.NNB, POS.NNP, POS.NNG)) `should be` true
        }

        // importFrom
        it("import from other dictionary") {
            dict2.importFrom(dict1) { it.isNoun() }
            dict2.words.size `should be equal to` 4
            dict2.contains("테팔", setOf(POS.NNP, POS.NNG)) `should be` true
            dict2.contains("김종국", setOf(POS.NNP, POS.NNG)) `should be` true
            dict2.contains("동병상련", setOf(POS.NNP, POS.NNG)) `should be` true
            dict2.contains("지나다", setOf(POS.VV)) `should be` false
        }
    }

    describe("CanExtractResource Interface") {
        // extractResource, getExtractedPath
        it("can extract resource") {
            val sampleCER = object : CanExtractResource() {
                override val modelName: String = "test"
            }

            { sampleCER.extractResource() } `should not throw` AnyException

            val path = sampleCER.extractResource()
            val path2 = sampleCER.extractResource()

            path `should be equal to` path2

            val lines = File(path, "DataSpecs.scala").bufferedReader().lines().toList()
            lines.size `should be equal to` 565
        }

        it("should be thread safe") {
            val sampleCER2 = object : CanExtractResource() {
                override val modelName: String = "test-thread"
            }

            {
                val paths = runBlocking {
                    (0..5).map {
                        async(Dispatchers.Default) {
                            sampleCER2.extractResource()
                        }
                    }.map {
                        it.await()
                    }
                }
                paths.toSet().size `should be equal to` 1
            } `should not throw` AnyException
        }
    }
})