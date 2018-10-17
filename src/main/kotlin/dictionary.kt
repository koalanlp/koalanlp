@file:JvmName("DicUtils")

package kr.bydelta.koala

import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

/** Dictionary Entry 타입: 표면형을 나타내는 [String] 값과, 품사태그를 나타내는 [POS]값으로 구성.*/
typealias DicEntry = Pair<String, POS>

/**
 * 사용자 사전추가 기능을 위한 interface.
 */
abstract class CanCompileDict {
    /**
     * 사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가.
     *
     * @param dict 추가할 (표면형,품사)의 순서쌍.
     */
    abstract fun addUserDictionary(vararg dict: DicEntry)

    /**
     * 사용자 사전에, 표면형과 그 품사를 추가.
     *
     * @param morph 표면형.
     * @param tag   품사.
     */
    fun addUserDictionary(morph: String, tag: POS) = addUserDictionary(morph to tag)

    /**
     * 사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가.
     *
     * @param morphs 추가할 단어의 표면형의 목록.
     * @param tags   추가할 단어의 품사의 목록.
     */
    fun addUserDictionary(morphs: List<String>, tags: List<POS>) =
            addUserDictionary(*morphs.zip(tags).toTypedArray())

    /**
     * 사용자 사전에, (표면형,품사)의 여러 순서쌍을 추가.
     *
     * @param dict 추가할 (표면형,품사)의 순서쌍.
     */
    operator fun plusAssign(entry: DicEntry) = addUserDictionary(entry)

    /**
     * 사용자 사전에 등재된 모든 Item을 불러옵니다.
     *
     * @return (형태소, 통합품사)의 Sequence.
     */
    abstract fun getItems(): Set<DicEntry>

    /**
     * 원본 사전에 등재된 항목 중에서, 지정된 형태소의 항목만을 가져옵니다. (복합 품사 결합 형태는 제외)
     *
     * @param filter 가져올 품사인지 판단하는 함수.
     * @return (형태소, 품사)의 Iterator.
     */
    abstract fun getBaseEntries(filter: (POS) -> Boolean): Iterator<DicEntry>

    /**
     * 사전에 등재되어 있는지 확인합니다.
     *
     * @param word   확인할 형태소
     * @param posTag 품사들(기본값: NNP 고유명사, NNG 일반명사)
     */
    @JvmOverloads
    fun contains(word: String, posTag: Set<POS> = setOf(POS.NNP, POS.NNG)): Boolean =
            getNotExists(onlySystemDic = false,
                    word = *posTag.map { word to it }.toTypedArray()).size < posTag.size

    /**
     * 사전에 등재되어 있는지 확인하고, 사전에 없는단어만 반환합니다.
     *
     * @param onlySystemDic 시스템 사전에서만 검색할지 결정합니다.
     * @param word          확인할 (형태소, 품사)들.
     * @return 사전에 없는 단어들.
     */
    abstract fun getNotExists(onlySystemDic: Boolean, vararg word: DicEntry): Array<DicEntry>

    /**
     * 다른 사전을 참조하여, 선택된 사전에 없는 단어를 사용자사전으로 추가합니다.
     *
     * @param dict       참조할 사전
     * @param fastAppend 선택된 사전에 존재하는지를 검사하지 않고, 빠르게 추가하고자 할 때 (기본값 false)
     * @param filter     추가할 품사를 지정하는 함수. (기본값 isNoun)
     */
    @JvmOverloads
    fun importFrom(dict: CanCompileDict,
                   fastAppend: Boolean = false,
                   filter: (POS) -> Boolean = { it.isNoun() }) {
        val entries = dict.getBaseEntries(filter).asSequence()

        for (chunk in entries.chunked(10000)) {
            val seq = if (fastAppend) chunk.toTypedArray()
            else getNotExists(true, *chunk.toTypedArray())

            this.addUserDictionary(*seq)
        }
    }
}

/**
 * 파일단위 압축해제를 위한 재귀함수.
 *
 * @param zis 압축해제할 Zip 입력 스트림.
 */
private fun unzipStream(zis: ZipInputStream, outpath: File) {
    val entry = zis.nextEntry
    if (entry != null) {
        val targetFile = File(outpath, entry.name)
        targetFile.deleteOnExit()

        if (entry.isDirectory) {
            targetFile.mkdirs()
        } else {
            targetFile.parentFile.mkdirs()
            val fos = FileOutputStream(targetFile)
            try {
                val buffer = ByteArray(1024)
                var len: Int = zis.read(buffer)
                while (len > 0) {
                    fos.write(buffer, 0, len)
                    len = zis.read(buffer)
                }
            } finally {
                fos.close()
            }
        }

        unzipStream(zis, outpath)
    }
}


/**
 * Jar Resource에 포함된 모형을 임시디렉터리에 압축해제하기 위한 interface
 */
abstract class CanExtractResource {
    /**
     * 모델의 명칭.
     */
    protected abstract val modelName: String

    /**
     * 압축해제할 임시 디렉터리.
     */
    private val PATH by lazy {
        val path = File(System.getProperty("java.io.tmpdir"), "koalanlp-$modelName")
        path.mkdirs()

        val zis = ZipInputStream(this::class.java.getResourceAsStream("/$modelName.zip"))
        try {
            unzipStream(zis, path)
        } catch (e: Exception) {

        } finally {
            zis.close()
        }

        path.absolutePath
    }

    /**
     * 압축해제 작업
     *
     * @return 압축해제된 임시 디렉터리의 절대경로
     */
    fun extractResource(): String = PATH

    /**
     * 압축해제된 임시 디렉터리의 위치.
     *
     * @return 임시 디렉터리의 절대경로 String.
     */
    fun getExtractedPath(): String = PATH
}
