package kr.bydelta.koala.server

import kr.bydelta.koala.POS
import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala.data.{Morpheme, Relationship, Sentence, Word}
import org.json.{JSONArray, JSONWriter}

object Json {

  def success(data: Seq[Sentence]): String = {
    val sw = new StringBuffer()
    val writer = new JSONWriter(sw)
    apply(writer.`object`()
      .key("success").value(true)
      .key("data"), data)
      .endObject()
    sw.toString
  }

  def success(): String = {
    val sw = new StringBuffer()
    val writer = new JSONWriter(sw)
    writer.`object`()
      .key("success").value(true)
      .endObject()
    sw.toString
  }

  def failure(msg: String): String = {
    val sw = new StringBuffer()
    val writer = new JSONWriter(sw)
    writer.`object`()
      .key("success").value(true)
      .key("message").value(msg)
      .endObject()
    sw.toString
  }

  def parseDictJson(string: String): Seq[(String, POSTag)] = {
    val json = new JSONArray(string)
    val len = json.length()
    (0 until len).map {
      i =>
        val obj = json.optJSONObject(i)
        obj.getString("morph") -> POS.withName(obj.getString("tag"))
    }
  }

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
