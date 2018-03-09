package kr.bydelta.koala.eunjeon.helper

import java.io.{BufferedInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}
import java.nio.charset.Charset

import org.bitbucket.eunjeon.seunjeon._

import scala.reflect.io.{File, Path}

/**
  * Dictionary Extractor Object
  *
  * Execution of this object is required before system builds, because there is a deserialization issue in SEunjeon,
  * caused by various scala versions. We need to extract and save dictionary objects differently.
  */
object DictionaryExtractor {
  def main(args: Array[String]): Unit = {
    val resources = Path("eunjeon") / "src" / "main" / "resources-2.12"
    println(resources.toAbsolute.toString())

    val termDictStream = new ObjectInputStream(new BufferedInputStream(
      new BufferedInputStream(classOf[LexiconDict].getResourceAsStream(DictBuilder.TERM_DICT), 32*1024), 16*1024))
    val termDict = termDictStream.readObject().asInstanceOf[Array[CompressedMorpheme]]
    val termOut = new ObjectOutputStream(new FileOutputStream((resources / "term.dict").jfile))
    val len = termDict.length
    termOut.writeInt(len)
    termDict.foreach{
      morph =>
        writeMorpheme(termOut, morph)
    }
    termOut.close()
    termDictStream.close()

    val dictMapperStream = new ObjectInputStream(new BufferedInputStream(
      new BufferedInputStream(classOf[LexiconDict].getResourceAsStream(DictBuilder.DICT_MAPPER), 32*1024), 16*1024))
    val dictMapper = dictMapperStream.readObject().asInstanceOf[Array[Array[Int]]]
    val dictMapOut = new ObjectOutputStream(new FileOutputStream((resources / "dict.map").jfile))
    dictMapOut.writeInt(dictMapper.length)
    dictMapper.foreach{
      array =>
        dictMapOut.writeInt(array.length)
        array.foreach(dictMapOut.writeInt)
    }
    dictMapOut.close()
    dictMapperStream.close()
  }

  private def writeMorpheme(termOut: ObjectOutputStream, morph: CompressedMorpheme): Unit = {
    termOut.writeObject(morph.getSurface)
    termOut.writeShort(morph.getLeftId)
    termOut.writeShort(morph.getRightId)
    termOut.writeInt(morph.getCost)
    termOut.writeObject(morph.getFeature)
    termOut.writeByte(morph.getMType.id.toByte)
    val poses = morph.getPoses
    termOut.writeInt(poses.length)
    poses.foreach {
      pos =>
        termOut.writeByte(pos.id.toByte)
    }
  }

  private[koala] def readMorpheme(in: ObjectInputStream, needCompress: Boolean): Morpheme = {
    val surface = in.readObject().asInstanceOf[String]
    val leftId = in.readShort()
    val rightId = in.readShort()
    val cost = in.readInt()
    val features = in.readObject().asInstanceOf[String]
    val mType = MorphemeType(Byte.byte2int(in.readByte()))
    val posSz = in.readInt()
    val poses = (0 until posSz).map{
      _ => Pos(Byte.byte2int(in.readByte()))
    }.toArray

    val morph = BasicMorpheme(surface, leftId, rightId, cost, features, mType, poses)
    if(needCompress) new CompressedMorpheme(morph)
    else morph
  }
}
