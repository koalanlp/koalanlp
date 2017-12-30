package kr.bydelta.koala.traits

import java.io.{File, FileOutputStream}
import java.util.zip.ZipInputStream

/**
  * Jar Resource에 포함된 모형을 임시디렉터리에 압축해제하기 위한 Trait
  */
trait CanExtractResource {
  /**
    * 압축해제할 임시 디렉터리.
    */
  private lazy val TMP = new File(System.getProperty("java.io.tmpdir"), s"koalanlp-$modelName")
  private[this] val logger = org.log4s.getLogger
  /**
    * 초기화(압축해제) 여부 Flag
    */
  private var initialized: Boolean = false

  /**
    * 모델의 명칭.
    */
  protected def modelName: String

  /**
    * 압축해제 작업
    *
    * @return 압축해제된 임시 디렉터리의 절대경로
    */
  protected[koala] def extractResource(): String = {
    TMP synchronized {
      if (!initialized) {
        initialized = true
        TMP.mkdirs()
        logger info s"Extracting dictionary resources to $TMP."

        val loader: ClassLoader = this.getClass.getClassLoader
        val zis = new ZipInputStream(loader.getResourceAsStream(s"$modelName.zip"))
        try {
          this synchronized {
            unzipStream(zis)
          }
        } catch {
          case e: Exception =>
            logger.error(e)("Extraction failed.")
        } finally {
          zis.close()
        }

        logger info s"Extraction finished."
      }
    }

    getExtractedPath
  }

  /**
    * 압축해제된 임시 디렉터리의 위치.
    *
    * @return 임시 디렉터리의 절대경로 String.
    */
  protected def getExtractedPath: String = {
    TMP.mkdirs()
    TMP.getAbsolutePath
  }

  /**
    * 파일단위 압축해제를 위한 재귀함수.
    *
    * @param zis 압축해제할 Zip 입력 스트림.
    */
  private def unzipStream(zis: ZipInputStream): Unit = {
    val entry = zis.getNextEntry
    if (entry != null) {
      val targetFile: File = new File(TMP, entry.getName)
      targetFile.deleteOnExit()

      if (entry.isDirectory) {
        targetFile.mkdirs
      } else {
        targetFile.getParentFile.mkdirs
        val fos: FileOutputStream = new FileOutputStream(targetFile)
        try {
          val buffer: Array[Byte] = new Array[Byte](1024)
          var len: Int = zis.read(buffer)
          while (len > 0) {
            fos.write(buffer, 0, len)
            len = zis.read(buffer)
          }
        } finally {
          fos.close()
        }
      }

      unzipStream(zis)
    }
  }

}
