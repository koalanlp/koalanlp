package kr.bydelta.koala.server

import kr.bydelta.koala.POS
import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.data.{Morpheme, Relationship, Sentence, Word}
import org.json.{JSONArray, JSONWriter}

/**
  * Json handling object
  */
object Json {

  /**
    * Build Success response
    *
    * @param data Sentences
    * @return JSON String (Success Response)
    */
  def success(data: Seq[Sentence]): String = {
    val sw = new StringBuffer()
    val writer = new JSONWriter(sw)
    apply(writer.`object`()
      .key("success").value(true)
      .key("data"), data)
      .endObject()
    sw.toString
  }

  /**
    * Build Success response
    *
    * @return JSON String (Empty Success Response)
    */
  def success(): String = {
    val sw = new StringBuffer()
    val writer = new JSONWriter(sw)
    writer.`object`()
      .key("success").value(true)
      .endObject()
    sw.toString
  }


  /**
    * Build Failure response
    *
    * @param data Sentences
    * @return JSON String (Failure Response)
    */
  def failure(msg: String): String = {
    val sw = new StringBuffer()
    val writer = new JSONWriter(sw)
    writer.`object`()
      .key("success").value(true)
      .key("message").value(msg)
      .endObject()
    sw.toString
  }

  /**
    * Parse JSON array for dictionary
    *
    * @param string JSON String to be parsed
    * @return Sequence of morphemes.
    */
  def parseDictJson(string: String): Seq[(String, POSTag)] = {
    val json = new JSONArray(string)
    val len = json.length()
    (0 until len).map {
      i =>
        val obj = json.optJSONObject(i)
        obj.getString("morph") -> POS.withName(obj.getString("tag"))
    }
  }

  /**
    * Write JSON String
    *
    * @param writer JSON Writer
    * @param x      Any object to be written
    * @return JSON Writer which is passed in.
    */
  private def apply(writer: JSONWriter, x: Any): JSONWriter =
    x match {
      case m: Morpheme =>
        writer.`object`()
          .key("surface").value(m.surface)
          .key("tag").value(m.tag.toString)
          .key("rawTag").value(m.rawTag.toString)
          .endObject()
      case r@Relationship(_, fTag, target) =>
        writer.`object`()
          .key("rel").value(fTag.toString)
          .key("rawRel").value(r.rawRel)
          .key("childID").value(target)
          .endObject()
      case w: Word =>
        apply(
          apply(writer.`object`()
            .key("surface").value(w.surface)
            .key("morphemes"), w.morphemes)
            .key("dependents"), w.dependents.toSeq
        ).endObject()
      case s: Sentence =>
        apply(
          apply(writer.`object`()
            .key("root"), s.root.dependents.toSeq)
            .key("words"), s.words
        ).endObject()
      case t: Seq[_] =>
        t.foldLeft(writer.array()) {
          case (w, item) => apply(w, item)
        }.endArray()
    }
}
