package kr.bydelta.koala.test.pack

import kr.bydelta.koala.POS
import kr.bydelta.koala.POS.POSTag
import org.specs2.execute.Result
import org.specs2.mutable.Specification

/**
  * Created by bydelta on 16. 7. 31.
  */
case class Conversion(tag: String,
                      toTagger: Boolean = true,
                      toSejong: Boolean = true)

trait TagConversionSpec extends Specification {
  def tagMap: PartialFunction[POSTag, Seq[Conversion]]

  def from(x: String): POSTag

  def to(x: POSTag): String

  "TagConversion" should {
    "convert tags correctly" in {
      Result.unit {
        POS.values.foreach { iPOS =>
          val list = tagMap(iPOS)
          list.foreach {
            case Conversion(tag, toTagger, toSejong) =>
              if (toTagger)
                to(iPOS) must_== tag
              if (toSejong)
                from(tag) must_== iPOS
          }
        }
      }
    }
  }
}
