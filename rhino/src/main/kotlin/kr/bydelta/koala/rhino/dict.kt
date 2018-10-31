@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.rhino

import java.io.InputStream

/**
 * RHINO가 구현해 둔 Dictionary의 Kotlin 버전입니다.
 * 불필요하게 참조되고 있는 Swing JFrame 삭제를 위해 재작성합니다.
 *
 * 원본은 `rhino.FileAnalyzer` 클래스이며,
 * 이 코드의 저작권은 RHINO의 저작자에게 있습니다.
 *
 * @since 1.x
 */
internal object DictionaryReader {
    val combiMethods_List = readMethodsList("rhino.lexicon.combi.combi")
    val endingMethods_List = readMethodsList("rhino.lexicon.ending.ending")
    val complexStem_MethodDeleted = readArray(javaClass.getResourceAsStream("/rhino/complexStem_MethodDeleted.txt"))
    val stem_MethodDeleted = readArray(javaClass.getResourceAsStream("/rhino/stem_MethodDeleted.txt"))
    val ending_MethodDeleted = readArray(javaClass.getResourceAsStream("/rhino/ending_MethodDeleted.txt"))
    val afterNumber_MethodDeleted = readArray(javaClass.getResourceAsStream("/rhino/afterNumber_MethodDeleted.txt"))
    val stem_List = read2DArray(javaClass.getResourceAsStream("/rhino/stem_List.txt"), splitByTwo = false)
    val ending_List = read2DArray(javaClass.getResourceAsStream("/rhino/ending_List.txt"), splitByTwo = false)
    val afterNumber_List = read2DArray(javaClass.getResourceAsStream("/rhino/afterNumber_List.txt"), splitByTwo = false)
    val nonEndingList = read2DArray(javaClass.getResourceAsStream("/rhino/_auto_managed_nonEndingList.txt"), splitByTwo = true)
    val aspgStem = getMapOfLengths(stem_List)
    val aspgEnding = getMapOfLengths(ending_List)

    private fun readMethodsList(lexicon: String) =
            try {
                Class.forName(lexicon).declaredMethods.map { it.name }.toTypedArray()
            } catch (_: Throwable) {
                emptyArray<String>()
            }

    private fun read2DArray(stream: InputStream, splitByTwo: Boolean): Array<Array<String?>> =
            readArray(stream).map { line ->
                if (splitByTwo) {
                    val splits = line.trim().split("\t", limit = 2)
                    arrayOf(splits[0], splits.getOrNull(1), null)
                } else {
                    //stem_List.txt; ending_List.txt; afterNumber_List.txt; complexStem_List.txt; stem_short_List.txt
                    val splits = line.trim().split("\t", limit = 3)
                    arrayOf<String?>(splits[0], splits[1], splits.getOrNull(2) ?: "-1")
                }
            }.toTypedArray()

    private fun readArray(stream: InputStream) =
            try {
                stream.bufferedReader().lines().iterator().asSequence()
                        .map { it.trim() }.toList().toTypedArray()
            } catch (_: Throwable) {
                emptyArray<String>()
            }

    private fun getMapOfLengths(array: Array<Array<String?>>) =
            try {
                val sizemap =
                        array.mapNotNull { it[0] }.mapIndexed { index, s -> s.length to index + 1 }
                                .groupBy { it.first }.asSequence().associate {
                                    val (wordlen, lines) = it

                                    if (wordlen <= 7) wordlen to (lines.minBy { w -> w.second }?.second ?: 0)
                                    else wordlen to 0
                                }

                (0 until 10).map { k -> sizemap[k] ?: 0 }.toIntArray()
            } catch (_: Throwable) {
                intArrayOf()
            }
}