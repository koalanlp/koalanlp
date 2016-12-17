package kr.bydelta.koala.hnn

import kr.bydelta.koala.util.BasicWordLearner
import org.specs2.mutable.Specification

/**
  * Created by bydelta on 16. 7. 30.
  */
class UnkLearnSpec extends Specification {
  sequential

  protected val text =
    """세월호 참사(4월 16일)가 발생한 지 167일 만인 30일 세월호 특별법이 극적으로 타결됐다.
      |새누리당 이완구, 새정치민주연합 박영선 원내대표는 이날 국회 운영위원장실에서 5개항을 담은 합의문을 작성했다.
      |최대 쟁점이었던 세월호 특별검사 추천권은 특검 후보군 4명을 여야 합의로 추천하되 유족이 추천 과정에 참여할지는 추후 논의하기로 했다.
      |'2차 합의안'을 기본틀로 해 일부 조항을 추가한 것이다.
      |2차 합의안에선 7명으로 구성된 특검후보추천위원 중 여당 쪽에서 2명을 추천하는 과정에서 유족의 사전 동의를 받도록 했다.
      |6월 발효된 특검법은 특검후보추천위가 2명의 후보를 추천하면 대통령이 이 중 1명을 임명하도록 했다.
      |새정치민주연합은 전날 '여야와 유가족이 특검 후보 4명을 추천한다'는 수정안을 제시했지만 새누리당은 "피해자(유가족)가 특검 후보를 추천하는 것은 법체계에 어긋난다"며 강력 반대했다.
      |이에 따라 '유가족 참여는 추후 논의한다'는 문안을 넣는 것으로 절충점을 찾았다.
      |유가족의 범위에는 단원고 희생자 유가족뿐만 아니라 일반인 희생자 유가족을 포함시키기로 한 것으로 알려졌다.
      |여야는 세월호 특별법을 비롯해 정부조직법 개정안과 이른바 '유병언법(범죄수익은닉규제처벌법)'을 10월 말까지 일괄 처리키로 합의했다.
      |국정감사는 10월 7일부터 27일까지 20일간 실시하기로 합의했다.
      |대정부질문 및 교섭단체대표연설, 예산안 심의 등 정기국회 세부 의사 일정은 조만간 협의하기로 했다.
      |세월호 특별법 협상 타결 직후인 오후 7시 36분 여야는 본회의를 열어 자녀를 학대한 부모의 친권을 최대 4년간 정지하는 내용의 민법 개정안 등 85개 법안과 '일본 정부의 고노 담화 검증 결과 발표 규탄 결의안'을 비롯한 일반 안건 5건 등 90개 안건을 처리했다.
      |이로써 5월 2일 이후 151일 만에 '식물 국회'는 정상화됐다.
      |법을 만드는 '입법부'가 아니라 '무(無)법부'라는 따가운 시선과 '차라리 국회를 해산하라'는 비판 여론이 커지자 위기의식을 느낀 여야가 결단을 내린 것으로 보인다.
      |세월호 가족대책위는 이날 밤 국회에서 기자회견을 열고 여야 합의안에 반대한다고 밝혔다.
      |새정치민주연합의 박범계 원내대변인은 "유족의 심정을 충분히 이해한다.
      |앞으로 여당과의 협상에서 유족의 뜻을 전면적으로 담은 특검 후보를 추천하도록 하겠다"고 말했다.""".stripMargin.split("\n").toSeq

  def getTagger = new Tagger

  "BasicWordLearner" should {
    Dictionary.extractResource()
    lazy val learner = new BasicWordLearner(getTagger, Dictionary)

    "extract all nouns" in {
      val level0 = learner.extractNouns(text.toIterator, minOccurrence = 1, minVariations = 1)
      val level2 = learner.extractNouns(text.toIterator, minOccurrence = 2, minVariations = 2)

      level0.size must be_>(level2.size)
      level0 must not(containAnyOf(Seq("알려졌다", "협의하기")))
      level0 must containAllOf(Seq("새정치민주연합", "특검후보추천위원"))
      level2 must contain("새정치민주연합")
    }

    "learn all nouns" in {
      val prevEnd = Dictionary.items.size
      learner.learn(text.toIterator, minOccurrence = 1, minVariations = 1)
      Dictionary.items.size must be_>(prevEnd)
    }
  }
}
