package kr.bydelta.koala.traits

import java.io.{File, FileOutputStream, IOException}
import java.util.zip.ZipInputStream

/**
  * Created by bydelta on 16. 7. 22.
  */
trait CanExtractResource {
  private lazy val TMP = new File(System.getProperty("java.io.tmpdir"), modelName)
  protected val modelName: String
  private var initialized: Boolean = false

  def getExtractedPath = TMP.getAbsolutePath

  @throws[IOException]
  protected[koala] def extractResource() = {
    if (!initialized) {
      initialized = true
      TMP.mkdirs()

      val loader: ClassLoader = this.getClass.getClassLoader
      val zis = new ZipInputStream(loader.getResourceAsStream(s"$modelName.zip"))
      try {
        unzipStream(zis)
      } catch {
        case e: Exception => e.printStackTrace()
      } finally {
        zis.close()
      }
    }

    getExtractedPath
  }

  private def unzipStream(zis: ZipInputStream): Unit = {
    val entry = zis.getNextEntry
    if (entry != null) {
      val targetFile: File = new File(TMP, entry.getName)
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
