package kr.bydelta.koala.hnn

import org.specs2.mutable._

/**
  * Created by bydelta on 16. 7. 26.
  */
class SplitterSpec extends Specification {
  sequential

  "SentenceSplitter" should {
    "handle empty sentence" in {
      val sent = new SentenceSplitter().sentences("")
      sent must beEmpty
    }

    "split sentences" in {
      val sent = "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐다. 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다."
      val splits = new SentenceSplitter().sentences(sent)

      splits.length must_== 2
    }

    "be thread-safe" in {
      val sents = Seq(
        "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐다. 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다.",
        "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐다. 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다.",
        "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐다. 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다.",
        "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐다. 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다.",
        "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐다. 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다.",
        "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐다. 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다.",
        "포털의 '속초' 연관 검색어로 '포켓몬 고'가 올랐다. 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다."
      )

      val multithreaded = sents.par.map {
        sent =>
          new SentenceSplitter().sentences(sent)
      }.seq.mkString("\n")

      val splitter = new SentenceSplitter()
      val singlethreaded = sents.map {
        sent =>
          splitter.sentences(sent)
      }.mkString("\n")

      multithreaded must_== singlethreaded
    }
  }
}
