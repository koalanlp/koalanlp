package kr.bydelta.koala.test.core

import kr.bydelta.koala._
import kr.bydelta.koala.traits.{CanCompileDict, CanLearnWord, CanTag}
import kr.bydelta.koala.util.BasicWordLearner
import org.specs2.execute.Result
import org.specs2.mutable.Specification

import scala.collection.JavaConverters._
import scala.util.Try

/**
  * Created by bydelta on 16. 7. 31.
  */
trait BasicWordLearnerSpecs extends Specification {
  lazy final val INC_1 = INCLUDED_SET.partition(word => {
    val words = tagger.tagSentence(word)
    val morphs = words.flatMap(_.morphemes)
    words.length > 1 && CanLearnWord.ALLOWED_ENDING(morphs.last.tag) &&
      !morphs.exists(CanLearnWord.DENIED_MORPS) && !isAlphabetPronounced(word)
  })
  lazy final val INC_2 = INCLUDED_SET_2.partition(word => {
    val words = tagger.tagSentence(word)
    val morphs = words.flatMap(_.morphemes)
    words.length > 1 && CanLearnWord.ALLOWED_ENDING(morphs.last.tag) &&
      !morphs.exists(CanLearnWord.DENIED_MORPS) && !isAlphabetPronounced(word)
  })
  final val EXCLUDED_SET: Seq[String] = Seq("등장하는", "요격하는")
  final val INCLUDED_SET: Seq[String] = Seq("속초시청", "미사일방어", "엠디", "국방부", "와이파이존", "월스트리트", "사드")
    .filterNot(x => getDict.contains(x))
  final val INCLUDED_SET_2: Seq[String] = Seq("국방부", "월스트리트", "와이파이존", "사드")
    .filterNot(x => getDict.contains(x))
  lazy val tagger = getTagger
  lazy val learner = new BasicWordLearner(tagger, getDict)
  /** 아래 기사의 저작권은 한겨레에 있습니다. (한겨례, 2016/07/13, 2014/05/28일자)
    * */
  protected val text =
  """평일 불구하고 속초행 버스 거의 매진
    |한국 ‘포켓몬고’ 실행 제한…규제 아닌 미출시 탓
    |닌텐도와 증강현실(AR) 콘텐츠 전문 회사 나이앤틱의 증강현실 게임 ‘포켓몬 고’가 세계적인 열풍을 일으키고 있는 가운데, 아직 게임이 출시되지 않은 한국에서도 강원도 속초 등 일부 지역에서 게임을 할 수 있다는 사실이 알려지면서 ‘포덕(포켓몬 마니아)’들이 이 지역으로 몰리고 있다. 평일임에도 불구하고 속초행 고속버스는 대부분 매진됐다.
    |포켓몬 고는 현실 세계를 돌아다니며 인기 게임에 등장하는 작은 몬스터를 잡고, 이를 키우는 방식의 증강현실 게임이다. 지난 6일 호주와 뉴질랜드를 시작으로 미국 등지에서 출시됐다.
    |12일 정도부터 게임 커뮤니티 사이에서 ‘강원도 속초와 고성, 양양, 경북 울릉도 등의 지역에서 게임이 된다’며 자신이 포획한 포켓몬과 주변에 표시된 포켓몬 체육관(포켓몬을 훈련시키는 곳), 포케스톱(게임에 필요한 아이템을 얻는 곳)을 인증하는 게시물들이 여러 개 올라왔다. 이에 다른 지역에 사는 포켓몬 마니아들이 고속버스 등을 타고 속초에 몰려들어 인증샷 릴레이를 펼치고 있다.
    |실제 13일 오전 서울 동서울터미널 속초행 고속버스는 대부분 매진 행렬이었다. 동서울터미널 관계자는 “12일 오전엔 2대 매진이었지만, 오늘 오전엔 속초행 버스가 7대 매진됐고 1~2자리만 남은 버스도 많았다”며 “오늘 도대체 무슨 일이 있는 것이냐”고 되물었다.
    |현재 속초시외버스터미널 근처에 서 있는 증강현실 속 ‘포켓몬 체육관’은 몰려든 마니아들에 의해 치열한 쟁탈전이 벌어지고 있다. 속초 도심 편의점 등 일부 장소에서도 꾸준히 포켓몬이 등장하고 있다는 증언이 속속 올라왔다. 누리꾼들은 원작 게임 주인공들의 고향 마을인 ‘태초마을’을 패러디해 ‘속초마을’이라는 별명도 붙였다.
    |포털의 ‘속초’ 연관 검색어로 ‘포켓몬 고’가 올랐고, 속초시청이 관광객의 편의를 위해 예전에 만들었던 무료 와이파이존 지도는 순식간에 인기 게시물이 됐다. 이에 속초시청은 공식 페이스북에 무료 와이파이존 지도를 다시 올리며 “(게임 덕분에) 속초시청도 홍보가 팍팍 되네요. 의외성이 주는 오늘의 즐거움입니다”라고 썼다.
    |속초시청 관계자는 <한겨레>와의 통화에서 “여름휴가 시즌이어도 평일 낮 시간대 속초행 고속버스까지 매진되는 일은 흔치 않다”며 “하루 사이에 관광객 유입 효과를 측정하는 것은 불가능하다. 숙박업소 사용 현황 등을 분석해야 경제 효과를 알 수 있을 것 같다”고 말했다.
    |일각에서 한국에 포켓몬 고가 출시되지 못하는 까닭이 증강현실 구현을 가로막는 정부의 지도 국외 반출 규제라는 지적이 나오고 있지만, 속초 등 일부 지역을 제외한 한국 전역에서 포켓몬 고를 즐길 수 없는 이유는 규제 때문이 아니라 게임 미출시 상태이기 때문이다.
    |나이앤틱은 2012년 증강현실 게임 ‘인그레스’를 출시하면서 전 세계를 마름모꼴로 나눈 ‘구획 지도(Cell Map)’를 그려 특정 구획에서 수신되는 GPS 신호를 차단하는 방식으로 정식 출시국가를 관리한 적이 있다. 이 지도는 휴전선 이남 대부분의 지역을 자체 지도 구획 기준으로 ‘AS16 구획’으로 분류하고 있으나 속초를 비롯해 강원 영동 북부와 울릉도 등은 ‘NR15’ 또는 ‘NR16’으로 분류하고 있다. 이 때문에 누리꾼들은 포켓몬 고의 출시국가 관리에도 ‘인그레스’의 구획 지도가 사용된 것으로 추정하고 있다. 실제로 ‘AS16’과 ‘NR15’ 구획이 맞닿은 강원도 양구에서 단시간 내에 게임 가능 지역과 불가능 지역을 오가며 포켓몬을 잡아 이 가설을 입증한 누리꾼도 있다.
    |추가 문장. 와이파이존은 전 세계에. 와이파이존을 국회로. 와이파이존보다 중요한 건 없다.
    |“부지 조사 실시…최종 결정만 남아”
    |WSJ, 국방부 관리 발언 인용 보도
    |미, 아시아안보회의서 한국 압박할 듯
    |중 “지역 안정·전략적 균형 해쳐” 반발
    |미국 국방부가 미국 미사일방어망(MD)의 핵심 무기체계인 ‘중고도 요격체계’(사드·THAAD)를 한국에 배치하는 방안을 검토하고 있다고 <월스트리트 저널>이 28일(현지시각) 보도했다.
    |이 신문은 미 국방부 관리들의 말을 인용해 “미 국방부가 북한 미사일 위협에 대응하고자 아시아에서 협력 강화를 위한 새로운 압박에 나섰다”며 “미국은 이미 사드를 한국에 배치하기 위해 부지 조사도 실시했다”고 전했다. 다만, 신문은 “아직 최종 결정은 내려지지 않았다”고 덧붙였다.
    |미국은 사드를 일시적으로 주한미군에 배치한 뒤 한국이 이를 구입하도록 하거나, 아니면 한국이 이를 곧바로 구입하는 방안을 추진 중이라고 신문은 전했다. 사드의 비용은 9억5000만달러(약 1조원)에 이른다.
    |사드는 중단거리 미사일을 요격하는 무기체계로, 사드의 한국 배치는 한국이 미·일의 엠디 체계에 본격적으로 편입되는 것을 뜻한다. 중국은 한·미·일의 엠디 통합을 대중국 견제용으로 인식하고 있는 만큼 사드가 한국에 배치될 경우 중국이 크게 반발할 것으로 예상된다. 중국 외교부의 친강 대변인은 28일 정례브리핑에서 “이곳(한반도)에 엠디를 배치하는 것은 지역의 안정과 전략적 균형에 이롭지 않다”며 “미국은 이 지역에서 관련 국가(중국)의 합리적 우려를 충분히 고려하기를 희망한다”고 말했다.
    |우리 국방부는 한국의 미사일방어 체계(KAMD)는 한반도의 종심이 짧은 점을 고려해 단거리 미사일에 대한 40㎞ 이하의 종말단계 하층 방어용으로 구축되고 있다고 설명하고 있다. 그래서 요격 고도 40~150㎞인 사드를 구입할 의사가 없다고 밝혀 왔다.
    |그러나 미국은 이번 주말 싱가포르에서 열리는 아시아안보회의(샹그릴라 대화)에서 우리 정부를 압박할 것으로 예상된다. <월스트리트 저널>은 “미국은 이번 회의에서 한·미·일 미사일 방어 협력 방안을 주요 의제로 삼아 논의할 예정”이라며 “이 회의에는 록히드 마틴의 고위 간부들도 참석할 예정”이라고 전했다. 록히드 마틴은 사드를 개발하는 방산업체다.
    |신문은 제임스 위너펠드 미 합참 부의장이 28일 워싱턴에서 열리는 한 세미나에서 “미국은 지역 미사일방어망 구축을 계속해서 강조할 것이다. 이것이 매우 정치적으로 민감한 문제이긴 하지만 북한의 도발에 대한 우리의 자신감을 증진시킬 것”이라고 밝힐 예정이라고 발언록을 미리 입수해 보도했다. 위너펠드 부의장을 포함한 국방부 관리들은 미국이 군비를 축소하고 있는 현실도 동맹국들과의 엠디 협력이 더 중요해진 이유라고 말한다고 신문은 전했다.
    |이에 대해 우리 국방부 관계자는 “사드가 주한미군에 배치되면 한반도 방어에 도움이 된다고 보지만, 현재로서 한국은 이 무기체계를 구매할 계획이 없다. 이 무기체계의 배치와 관련해 미국과 협의한 바도 없는 것으로 안다”고 말했다. 이 관계자는 “미사일방어 종말단계 중층방어용인 이 무기체계가 주한미군에 배치된다고 해도 우리와 무관한 일이며 우리가 미국의 미사일방어에 편입되는 것은 아니다”라고 덧붙였다.
    |추가 문장. 월스트리트는 뉴욕에. 월스트리트를 점령하라. 월스트리트와 함께.
    | """.stripMargin.split("\n").toSeq

  def getTagger:CanTag

  def getDict:CanCompileDict

  "BasicWordLearner" should {
    var lv0Empty = false

    "extract all nouns" in {
      val level0 = learner.extractNouns(text.toIterator, minOccurrence = 2, minVariations = 2)
      val level2 = learner.extractNouns(text.toIterator, minOccurrence = 5, minVariations = 3)

      lv0Empty = level0.isEmpty
      level0.size must be_>=(level2.size)
      level0 must not(containAnyOf(EXCLUDED_SET))
      level0 must containAllOf(INC_1._1)
      level0 must not(containAnyOf(INC_1._2))
      level2 must containAllOf(INC_2._1)
      level2 must not(containAnyOf(INC_2._2))
    }

    "learn all nouns" in {
      Result.unit {
        if (!lv0Empty) {
          val tagger1 = getTagger
          val beforeLearn = text.map(s => tagger1.tagSentence(s).singleLineString).mkString("\n")

          Try(learner.jLearn(text.toIterator.asJava, minOccurrence = 1, minVariations = 1)) must beSuccessfulTry

          val tagger2 = getTagger

          Try(text.map(s => tagger2.tagSentence(s).singleLineString).mkString("\n")) must beSuccessfulTry
        }
      }
    }
  }

  "Dictionary" should {
    "add a noun" in {
      getDict.addUserDictionary("갑질", POS.NNG) must not(throwA[Exception])
      getDict.getNotExists(false, "갑질" -> POS.NNG) must beEmpty
      getDict.getNotExists(false, "갑질" -> POS.VX) must not(beEmpty)
    }
    "add a verb" in {
      getDict.addUserDictionary("구글링" -> POS.VV) must not(throwA[Exception])
      getDict.getNotExists(false, "구글링" -> POS.VV) must beEmpty
      getDict.getNotExists(false, "구글링" -> POS.MM) must not(beEmpty)
    }
    "add a modifier" in {
      getDict.addUserDictionary("대애박" -> POS.MM) must not(throwA[Exception])
      getDict.getNotExists(false, "대애박" -> POS.MM) must beEmpty
      getDict.getNotExists(false, "대애박" -> POS.NNP) must not(beEmpty)
    }
  }
}
