package kr.bydelta.koala.cross

import kr.bydelta.koala.eunjeon.Tagger
import kr.bydelta.koala.kkma.Parser
import kr.bydelta.koala.twt.SentenceSplitter

import scala.io.StdIn

/**
  * Created by bydelta on 16. 7. 24.
  */
object TwtEunjeonKKMA {
  def main(args: Array[String]) {
    val splitter = new SentenceSplitter
    val tagger = new Tagger
    val parser = new Parser

    var line = ""
    do {
      print("Input a sentence >> ")
      line = StdIn.readLine()
      if (line.nonEmpty) {
        println("문장 분리...")
        splitter.sentences(line).foreach {
          sent =>
            println("품사 부착...")
            tagger.tag(sent).foreach {
              tagged =>
                println(tagged.singleLineString)
                println("의존 구문 분석...")
                println(parser.parse(tagged).treeString)
            }
        }
      }
    } while (line.nonEmpty)
  }
}
