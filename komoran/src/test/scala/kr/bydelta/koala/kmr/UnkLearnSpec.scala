package kr.bydelta.koala.kmr

import kr.bydelta.koala.util.UnknownWordLearner
import org.specs2.mutable.Specification

/**
  * Created by bydelta on 16. 7. 30.
  */
class UnkLearnSpec extends Specification {
  sequential

  protected val text =
    """부정청탁 및 금품 등 수수의 금지에 관한 법률(김영란법) 시행이 두 달 앞으로 다가오면서 한 해 10조원에 달하는 접대비를 쓰는 재계에 ‘비상’이 걸렸다. 기업별로 전담팀을 꾸려 불법 범주에 드는 접대행위를 구별하기 위한 자체 ‘가이드라인’을 준비하는 등 대응책 마련에 분주히 움직이고 있다.
      |정부 부처와 지자체 등은 이미 공직자윤리법 적용을 받지만 '시범 케이스' 사례가 발생하지 않도록 내부 단속에 나섰다. 언론사들은 법 시행 이전에 구체적 ‘행동규정’ 정비에 나섰다.
      |29일 삼성, 현대자동차, LG, SK 등 주요 대기업들의 경우 내부 법무팀을 통해 법안을 면밀하게 분석하는 등 대응책 마련에 착수한 상태다. 지난해 기업 등 법인 59만여곳이 법인카드로 결제한 접대비는 9조9685억원으로 집계됐다. 재계에서는 기타 업무추진비 등의 명목으로 지출되는 비용까지 합하면 실제 접대비 규모는 이보다 훨씬 클 것으로 추정하고 있다. 이에 따라 업무 추진과 대관 업무 등에서 기존의 관행들을 대부분 개선해야 한다.
      |현대차그룹 관계자는 “법무팀을 중심으로 홍보와 대관팀 관계자들이 모여 지금까지 해온 일에 대해 되는 것, 안되는 것들을 하나하나 정리하고 있다”고 말했다.
      |기업들은 법무팀 관계자들을 대형 로펌 등에 파견해 외부 법률자문도 받고 있다. 기업 임직원이 이를 위반했을 때 법인도 양벌규정에 따라 처벌받아 전사적으로 대비할 필요가 있기 때문이다. 이에 맞춰 국내 대형 로펌들은 잇달아 기업들을 위한 김영란법 자문서비스에 나서고 있다. 현재 대형 법무법인 중에서는 세종, 태평양이 ‘반부패 컴플라이언스팀’이란 이름으로 본격 자문서비스에 나섰다. 이 같은 서비스는 향후 더 늘어날 것으로 보인다.""".stripMargin.split("\n").toSeq

  def getTagger = new Tagger

  "UnknownWordLearner" should {
    Dictionary.extractResource()
    val learner = new UnknownWordLearner(getTagger, Dictionary)

    "extract all nouns" in {
      val level0 = learner.extractNouns(text, minOccurrence = 0, minVariations = 0)
      val level2 = learner.extractNouns(text, minOccurrence = 1, minVariations = 1)

      level0.size must be_>(level2.size)
      level0 must not(containAnyOf(Seq("관계자들", "위반했")))
      level0 must containAllOf(Seq("현대차그룹", "법무팀"))
      level2 must contain("법무팀")
    }

    "learn all nouns" in {
      val tagger1 = getTagger
      val beforeLearn = text.map(s => tagger1.tagSentence(s).singleLineString).mkString("\n")

      learner.learn(text, minOccurrence = 0, minVariations = 0)

      val tagger2 = getTagger
      val afterLearn = text.map(s => tagger2.tagSentence(s).singleLineString).mkString("\n")

      beforeLearn must_!= afterLearn
    }
  }
}
