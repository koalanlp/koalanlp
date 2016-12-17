package kr.bydelta.koala.traits

import kr.bydelta.koala.{KoreanStringExtension, POS}

/**
  * Case Class of morpheme particle (For CanLearnWord)
  *
  * @param morpheme      Morpheme string
  * @param posType       POS tag for this morpheme
  * @param allowJongsung Does this allow jongsung on the last character of the word?
  * @param allowJungsung Does this allow jungsung on the last character of the word?
  * @param allowCall     Does this allow NNG word?
  * @param allowETN      Does this allow ETN pos tag?
  */
protected[koala] case class Particle(morpheme: String, posType: POS.POSTag,
                                     allowJongsung: Boolean = false, allowJungsung: Boolean = false,
                                     allowCall: Boolean = true, allowETN: Boolean = true)

/**
  * 품사분석기가 분석하지 못한 신조어, 전문용어 등을 확인하여 추가하는 작업을 할 수 있는 Trait.
  *
  * @tparam S Sequence Type for corpora in Scala
  * @tparam J List Type for corpora in Java
  */
trait CanLearnWord[S, J] {
  /**
    * 고려하는 조사 목록의 최대 수량. (minVariation의 최댓값)
    */
  lazy final val JOSA_COUNT_MAX = Math.max(
    JOSA_LIST.filter(x => x.allowJungsung && !JOSA_FAKE.contains(x.posType)).map(_.morpheme).distinct.size,
    JOSA_LIST.filter(x => x.allowJongsung && !JOSA_FAKE.contains(x.posType)).map(_.morpheme).distinct.size
  )
  /**
    * 주요 조사의 최소숫자.
    *
    * 이/가/께서/에서(주격), 의(관형격), 을/를(목적격), 이/가(보격), 와/과(접속격), 은/는/도(보조사)
    */
  lazy final val JOSA_COUNT_MAJOR = Math.min(
    JOSA_LIST.filter(x => x.allowJungsung && JOSA_MAJOR.contains(x.posType)).map(_.morpheme).distinct.size,
    JOSA_LIST.filter(x => x.allowJongsung && JOSA_MAJOR.contains(x.posType)).map(_.morpheme).distinct.size
  )
  /** 인식되지 않은 단어의 품사 **/
  protected final val UNKNOWN_TAGS = Seq(POS.UE, POS.UN)
  /** 고려하는 조사 목록 (단음절, 앞단어 중성 종결) **/
  protected final val JOSA_LIST = {
    val allow = Seq(
      Particle("으로서", allowJongsung = true, posType = POS.JKB, allowCall = false),
      Particle("으로써", allowJongsung = true, posType = POS.JKB, allowCall = false),
      Particle("께서", allowJungsung = true, allowJongsung = true, posType = POS.JKS, allowETN = false),
      Particle("에서", allowJungsung = true, allowJongsung = true, posType = POS.JKS, allowCall = false, allowETN = false),
      Particle("에서", allowJungsung = true, allowJongsung = true, posType = POS.JKB, allowCall = false, allowETN = false),
      Particle("에게", allowJungsung = true, allowJongsung = true, posType = POS.JKB, allowETN = false),
      Particle("로서", allowJungsung = true, posType = POS.JKB, allowCall = false, allowETN = false),
      Particle("로써", allowJungsung = true, posType = POS.JKB, allowCall = false, allowETN = false),
      Particle("으로", allowJongsung = true, posType = POS.JKB, allowCall = false, allowETN = false),
      Particle("보다", allowJungsung = true, allowJongsung = true, posType = POS.JKB),
      Particle("라고", allowJungsung = true, allowJongsung = true, posType = POS.JKB, allowETN = false),
      Particle("부터", allowJungsung = true, allowJongsung = true, posType = POS.JX),
      Particle("이", allowJongsung = true, posType = POS.JKS, allowETN = false),
      Particle("가", allowJungsung = true, posType = POS.JKS, allowETN = false),
      Particle("의", allowJungsung = true, allowJongsung = true, posType = POS.JKG),
      Particle("을", allowJongsung = true, posType = POS.JKO),
      Particle("를", allowJungsung = true, posType = POS.JKO),
      Particle("이", allowJongsung = true, posType = POS.JKC),
      Particle("가", allowJungsung = true, posType = POS.JKC),
      Particle("에", allowJungsung = true, allowJongsung = true, posType = POS.JKB, allowCall = false),
      Particle("로", allowJungsung = true, posType = POS.JKB, allowCall = false),
      Particle("와", allowJungsung = true, posType = POS.JKB),
      Particle("과", allowJongsung = true, posType = POS.JKB),
      Particle("라", allowJungsung = true, allowJongsung = true, posType = POS.JKB),
      Particle("와", allowJungsung = true, posType = POS.JC),
      Particle("과", allowJongsung = true, posType = POS.JC),
      Particle("은", allowJongsung = true, posType = POS.JX),
      Particle("는", allowJungsung = true, posType = POS.JX),
      Particle("도", allowJungsung = true, allowJongsung = true, posType = POS.JX)
    )

    val call = Seq(
      Particle("씨", allowJungsung = true, allowJongsung = true, posType = POS.NNB),
      Particle("님", allowJungsung = true, allowJongsung = true, posType = POS.NNB)
    )

    val verbToNoun = Seq(
      Particle("임", allowJungsung = true, allowJongsung = true, posType = POS.ETN),
      Particle("함", allowJungsung = true, allowJongsung = true, posType = POS.ETN),
      Particle("됨", allowJungsung = true, allowJongsung = true, posType = POS.ETN),
      Particle("하기", allowJungsung = true, allowJongsung = true, posType = POS.ETN),
      Particle("되기", allowJungsung = true, allowJongsung = true, posType = POS.ETN)
    )

    allow ++ call.flatMap {
      case t@Particle(x, _, _, _, _, _) =>
        val isEndJong = x.endsWithJongsung
        allow.filter(_.allowCall).collect {
          case Particle(p, _, ajo, aju, _, _) if (ajo && isEndJong) || (aju && !isEndJong) =>
            t.copy(morpheme = x + p)
        }
    } ++ verbToNoun.flatMap {
      case t@Particle(x, _, _, _, _, _) =>
        val isEndJong = x.endsWithJongsung
        allow.filter(_.allowETN).collect {
          case Particle(p, _, ajo, aju, _, _) if (ajo && isEndJong) || (aju && !isEndJong) =>
            t.copy(morpheme = x + p)
        }
    }
  }
  /**
    * 뒷 품사에 따라, 그 앞에 올 수 없는 품사들
    */
  protected final val JOSA_IMPOSSIBLE = Map(
    POS.JC -> Set(POS.JX, POS.JKS, POS.JKO, POS.JKB, POS.JKC, POS.JKG),
    POS.JX -> Set.empty[POS.POSTag],
    POS.JKS -> Set(POS.JKO, POS.JKB, POS.JKC, POS.JKG, POS.JC),
    POS.JKO -> Set(POS.JKS, POS.JKB, POS.JKC, POS.JKG, POS.JC),
    POS.JKB -> Set(POS.JKO, POS.JKS, POS.JKC, POS.JKG, POS.JC),
    POS.JKC -> Set(POS.JKO, POS.JKB, POS.JKS, POS.JKG, POS.JC),
    POS.JKG -> Set(POS.JKO, POS.JKB, POS.JKS, POS.JKC),
    POS.NNB -> Set(POS.ETN, POS.JC, POS.JX, POS.JKS, POS.JKO, POS.JKB, POS.JKC, POS.JKG),
    POS.ETN -> Set(POS.NNB, POS.JC, POS.JX, POS.JKS, POS.JKO, POS.JKB, POS.JKC, POS.JKG)
  )
  /** 호칭에 붙는 의존 명사 및 명사형 전성어미 목록 (단음절) **/
  protected final val DEPS_CALL = Seq('씨', '님', '임', '들')
  /** 호칭에 붙는 의존 명사 및 명사형 전성어미 목록 (다음절) **/
  protected final val DEPS_CALL_LONG = Seq("하기", "되기")
  private final val JOSA_MAJOR = Set(POS.JKS, POS.JKG, POS.JKO, POS.JKC, POS.JC, POS.JX)
  private final val JOSA_FAKE = Set(POS.NNB, POS.ETN)
  /** Type conversion from J to S **/
  protected val converter: J => S
  /**
    * 신조어 등을 등록할 사용자사전들.
    */
  protected val targets: Seq[CanCompileDict]

  /**
    * 품사분석기가 분석하지 못한 신조어, 전문용어 등을 파악.
    *
    * @param corpora       새로운 단어를 발굴할 말뭉치.
    * @param minOccurrence 단어 등록을 위한, 최소 출현 횟수. (기본값 10회)
    * @param minVariations 단어 등록을 위한, 최소 활용(변형) 횟수. (기본값 2회) 용언의 경우는 활용형의 변화를, 체언의 경우는 조사의 변화를 파악함.
    * @return 새로운 단어와 그 품사의 Sequence.
    */
  def extractNouns(corpora: S, minOccurrence: Int = 10, minVariations: Int = 2): Stream[String]

  /**
    * (Java) 품사분석기가 분석하지 못한 신조어, 전문용어 등을 확인하여 추가
    *
    * @param corpora       새로운 단어를 발굴할 말뭉치.
    * @param minOccurrence 단어 등록을 위한, 최소 출현 횟수. (기본값 10회)
    * @param minVariations 단어 등록을 위한, 최소 활용(변형) 횟수. (기본값 2회) 용언의 경우는 활용형의 변화를, 체언의 경우는 조사의 변화를 파악함.
    */
  def jLearn(corpora: J, minOccurrence: Int = 10, minVariations: Int = 2): Unit =
  learn(converter(corpora), minOccurrence, minVariations)

  /**
    * 품사분석기가 분석하지 못한 신조어, 전문용어 등을 확인하여 추가
    *
    * @param corpora       새로운 단어를 발굴할 말뭉치.
    * @param minOccurrence 단어 등록을 위한, 최소 출현 횟수. (기본값 10회, 반드시 [[JOSA_COUNT_MAX]] 이하이어야 함.)
    * @param minVariations 단어 등록을 위한, 최소 활용(변형) 횟수. (기본값 2회) 용언의 경우는 활용형의 변화를, 체언의 경우는 조사의 변화를 파악함.
    */
  def learn(corpora: S, minOccurrence: Int = 10, minVariations: Int = 2): Unit = {
    val minVar: Int = if (minVariations > JOSA_COUNT_MAX) JOSA_COUNT_MAX else minVariations

    extractNouns(corpora, minOccurrence, minVar).map(_ -> POS.NNP).sliding(100, 100).foreach {
      set =>
        targets.par.foreach(_.addUserDictionary(set: _*))
    }
  }

  /**
    * 단어의 원형과 조사를 Heuristic으로 분리.
    *
    * @param word 분리할 어절.
    * @return (단어 원형, 조사)
    */
  protected def extractJosa(word: String): Option[(String, String)] =
  getStructure(word) match {
    case (w, Some(Particle(josa, p, _, _, _, _))) if p != POS.ETN && p != POS.NNB => Some(w -> josa)
    case (w, None) => Some(w -> "")
    case _ => None
  }

  /**
    * 조사가 가장 길게 연결 될 수 있는 구조를 찾는다.
    *
    * @param word 구조를 찾을 단어.
    * @param prev 이 단어에 붙었던 조사
    * @return Option(단어, 조사)
    */
  private def getStructure(word: String, prev: Option[Particle] = None): (String, Option[Particle]) =
  if (word.isEmpty) (word, None)
  else {
    if (word.endsWithHangul) {
      val candidates = JOSA_LIST.filter {
        case Particle(m, j, _, _, _, _) if word.length > m.length && word.endsWith(m) =>
          if (prev.isDefined && j != POS.JX && prev.get.posType != j) {
            !JOSA_IMPOSSIBLE(prev.get.posType).contains(j)
          } else if (prev.isEmpty)
            true
          else
            false
        case _ => false
      }.map {
        case j@Particle(m, _, _, _, _, _) =>
          val subword = word.dropRight(m.length)
          val endsWithJongsung = subword.endsWithJongsung

          if ((endsWithJongsung && j.allowJongsung) || (!endsWithJongsung && j.allowJungsung)) {
            getStructure(subword, Some(j)) match {
              case (_, None) => word -> Some(j)
              case s@(_, _) => s
            }
          } else (word, None)
        case _ => (word, None)
      }.filter(_._2.isDefined)

      if (candidates.isEmpty) {
        word -> prev
      } else {
        candidates.minBy(_._1.length)
      }
    } else {
      word -> prev
    }
  }
}


