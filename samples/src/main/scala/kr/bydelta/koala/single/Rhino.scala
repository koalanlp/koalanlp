package kr.bydelta.koala.single

import kr.bydelta.koala.rhino.Tagger

import scala.io.StdIn

/**
  * Created by bydelta on 17. 8. 19.
  */
object Rhino {
  def main(args: Array[String]) {
    // RHINO는 사용자 정의 사전 지원 안됨.
    // Dictionary.addUserDictionary("은전한닢" -> POS.NNG, "꼬꼬마" -> POS.NNG, "구글하" -> POS.VV)
    val tagger = new Tagger

    var line = ""
    do {
      print("Input a sentence >> ")
      line = StdIn.readLine()
      if (line.nonEmpty) {
        println("품사 부착...")
        tagger.tag(line).foreach {
          sent =>
            println(sent.singleLineString)
            println()
        }
      }
    } while (line.nonEmpty)
  }
}
