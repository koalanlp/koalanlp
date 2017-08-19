package kr.bydelta.koala.test.pack

import kr.bydelta.koala.POS.POSTag
import kr.bydelta.koala._
import org.specs2.execute.Result
import org.specs2.mutable.Specification

/**
  * Created by bydelta on 16. 7. 31.
  */
trait TagConversionSpec extends Specification {
  val tagMap: Map[String, String]

  def from(x: String): POSTag

  def to(x: POSTag): String

  "TagConversion" should {
    "convert tags correctly" in {
      Result.unit {
        tagMap.foreach {
          case (iTag, list) =>
            val iPOS = POS.withName(iTag)

            if (list.nonEmpty) {
              list.split("\n").foreach {
                case tag if tag startsWith "<" =>
                  from(tag.substring(1)) must_== iPOS
                case tag if tag startsWith ">" =>
                  to(iPOS) must_== tag.substring(1)
                case tag =>
                  from(tag) must_== iPOS
                  to(iPOS) must_== tag
              }
            }
        }
      }
    }
  }
}
