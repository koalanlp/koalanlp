package kr.bydelta.koala.kkma

import kr.bydelta.koala.util.UnknownWordLearner
import org.specs2.mutable.Specification

/**
  * Created by bydelta on 16. 7. 30.
  */
class UnkLearnSpec extends Specification {
  sequential

  protected val text =
    """중국에서 국가안전위해죄 위반 혐의로 지난 3월 붙잡혀 구금됐던 북한 인권 운동가 김영환씨(49·사진) 일행 4명이 구금 114일 만인 20일 강제 추방 형식으로 귀국했다.
      |정부는 오후 5시15분(한국시간) 선양에서 중국 당국으로부터 구금 중이던 김씨 일행을 인수받았고, 이들은 선양발 오후 5시30분 대한항공편으로 7시30분쯤 인천공항에 도착했다.
      |김씨는 도착 직후 기자들에게 "저희를 구출하기 위해 노력해준 정부와 국민, 동료 등에게 감사드린다"며 "북한 민주화 노력은 우리가 해야 할 임무인 만큼 앞으로도 해 나가야 한다"고 말했다.
      |건강 상태를 묻는 말에는 "좋다"고 말했고, 체포 경위에는 "앞으로 말할 기회가 있을 것"이라면서 즉답을 피했다.
      |김씨와 일행인 유모(44)·강모(42)·이모(32)씨 건강상태도 비교적 양호했다.
      |외교통상부 당국자는 "김씨 일행은 정보당국에 체포 경위와 국내법 위반 여부 등을 조사받은 뒤 귀가할 예정"이라고 전했다.
      |중국 당국은 전날 이유를 알리지 않은 채 한국 측에 김씨 일행에 대한 강제추방 방침을 통보한 것으로 알려졌다.
      |중국은 지난달 김씨 일행에 대한 조사를 마쳤으며 기소 여부를 놓고 고심하다 최근 불기소 방침을 정한 것으로 알려졌다.
      |이런 결정은 기소 시 북한 인권 운동을 해온 김씨의 민감한 활동 내용이 공개되고 북한이 이에 대해 반발할 가능성을 고려한 조치로 분석된다.
      |중국은 그동안 우리 측과의 협의에서 한·중 관계를 고려해 김씨 일행 문제를 처리하겠다고 했으며 최근 방한한 멍젠주 중국 공안부장도 김씨 일행의 석방 문제를 진지하게 검토하고 있다며 사실상 추방형식으로 한국에 보낼 방침임을 밝혔다.
      |김영환씨는 1980년대 주사파 운동권 대부이자 당시 대학가 주체사상 교범이었던 '강철서신'의 저자로, 1990년대 말 우파로 전향해 북 민주화운동을 해왔다.
      |김씨와 일행 3명은 3월29일 중국 랴오닝성 다롄에서 탈북자 관련 회의를 하다 중국 공안에 국가안전위해죄로 체포돼 단둥시 국가안전청에 구금됐다.""".stripMargin.split("\n").toSeq

  def getTagger = new Tagger

  "UnknownWordLearner" should {
    val learner = new UnknownWordLearner(getTagger, Dictionary)

    "extract all nouns" in {
      val level0 = learner.extractNouns(text, minOccurrence = 0, minVariations = 0)
      val level2 = learner.extractNouns(text, minOccurrence = 1, minVariations = 1)

      level0.size must be_>(level2.size)
      level0 must not(containAnyOf(Seq("귀국했다", "구금됐다")))
      level0 must containAllOf(Seq("국가안전위해죄", "국가안전청"))
      level2 must contain("국가안전위해죄")
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
