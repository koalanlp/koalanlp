package kr.bydelta.koala.single

import kr.bydelta.koala.POS
import kr.bydelta.koala.kkma.{Dictionary, Parser, Tagger}

import scala.io.StdIn

object KKMA {
  def main(args: Array[String]) {
    Dictionary.addUserDictionary("은전한닢" -> POS.NNG, "꼬꼬마" -> POS.NNG, "구글하" -> POS.VV)
    val tagger = new Tagger
    val parser = new Parser

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

            parser.parse(sent)
            println(sent.treeString)
            println()
        }
      }
    } while (line.nonEmpty)
  }
}
