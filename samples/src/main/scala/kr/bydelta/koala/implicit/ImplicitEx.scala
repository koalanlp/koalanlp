package kr.bydelta.koala.`implicit`

import kr.bydelta.koala.eunjeon.Tagger
import kr.bydelta.koala.kkma.Parser
import kr.bydelta.koala.twt.SentenceSplitter

import scala.io.StdIn

/**
  * Created by bydelta on 16. 9. 10.
  */
object ImplicitEx {
  def main(args: Array[String]) {
    import kr.bydelta.koala.Implicit._
    implicit val splitter: SentenceSplitter = new SentenceSplitter
    implicit val tagger: Tagger = new Tagger
    implicit val parser: Parser = new Parser

    var line = ""
    do {
      print("Input a sentence >> ")
      line = StdIn.readLine()
      if (line.nonEmpty) {
        println("문장 분리...")
        line.sentences.foreach {
          sent =>
            println("품사 부착...")
            val tagged = sent.toTagged
            tagged.foreach {
              line =>
                println(line.singleLineString)
                println("의존 구문 분석...")
                println(line.toParsed.treeString)
            }
        }
      }
    } while (line.nonEmpty)
  }
}
