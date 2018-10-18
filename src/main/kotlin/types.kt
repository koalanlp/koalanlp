@file:JvmName("Utils")

/**
 * # Package kr.bydelta.koala
 *
 * KoalaNLP의 공통 부분을 담은 패키지입니다.
 *
 * ## 자바 및 스칼라 개발자를 위한 노트
 * 1. 여기 수록된 항목 중에서 Types는 Java의 Class를 의미합니다.
 *
 * 2. 여기 수록된 항목 중에서 Extensions, Properties, Functions 항목에 있는 내용들은 (만약 있다면),
 *    Java와 Scala에서 `kr.bydelta.koala.Utils`의 Static Member로 참조됩니다.
 *
 * 3. 만약, Scala의 경우 [koalanlp-scala](https://koalanlp.github.io/wrapper-scala/) 패키지를 사용한다면,
 *    Extensions에 수록된 Implicit 변환을 Kotlin에서와 동일하게 사용할 수 있습니다.
 */
package kr.bydelta.koala

/**
 * 세종 품사표기 표준안을 Enum Class로 담았습니다.
 *
 * @since 1.x
 */
enum class POS {
    /** ([NOUNS]: 체언) ''일반명사'': 일반 개념을 표시하는 명사. **/
    NNG,
    /** ([NOUNS]: 체언) ''고유명사'' : 낱낱의 특정한 사물이나 사람을 다른 것들과 구별하여 부르기 위하여 고유의 기호를 붙인 이름. **/
    NNP,

    /** ([NOUNS]: 체언) ''일반 의존명사'' : 의미가 형식적이어서 다른 말 아래에 기대어 쓰이는 명사.
     *
     * ‘것1’, ‘따름1’, ‘뿐1’, ‘데1’ 따위가 있다.
     * */
    NNB,
    /** ([NOUNS]: 체언) ''단위성 의존명사'' : 수효나 분량 따위의 단위를 나타내는 의존 명사.
     *
     * ‘쌀 한 말, 쇠고기 한 근, 굴비 한 두름, 북어 한 쾌, 고무신 한 켤레, 광목 한 필’에서 ‘말3’, ‘근3’, ‘두름1’, ‘쾌1’, ‘켤레2’, ‘필2’
     * */
    NNM,
    /** ([NOUNS]: 체언) ''수사'' : 사물의 수량이나 순서를 나타내는 품사. 양수사와 서수사가 있다. **/
    NR,
    /** ([NOUNS]: 체언) ''대명사'' : 사람이나 사물의 이름을 대신 나타내는 말. 또는 그런 말들을 지칭하는 품사 **/
    NP,

    /** ([PREDICATES]: 용언) ''동사'' : 사물의 동작이나 작용을 나타내는 품사. **/
    VV,
    /** ([PREDICATES]: 용언) ''형용사'' : 사물의 성질이나 상태를 나타내는 품사. **/
    VA,
    /** ([PREDICATES]: 용언) ''보조용언'' : 본용언과 연결되어 그것의 뜻을 보충하는 역할을 하는 용언. 보조 동사, 보조 형용사가 있다.
     *
     * ‘ 가지고 싶다’의 ‘싶다’, ‘먹어 보다’의 ‘보다1’ 따위이다.
     * */
    VX,
    /** ([PREDICATES]: 용언) ''긍정지정사(이다)'': 무엇이 무엇이라고 지정하는 단어. (이다) **/
    VCP,
    /** ([PREDICATES]: 용언) ''부정지정사(아니다)'': 무엇이 무엇이 아니라고 지정하는 단어. (아니다) **/
    VCN,

    /** ([MODIFIERS]: 수식언) ''관형사'' : 체언 앞에 놓여서, 그 체언의 내용을 자세히 꾸며 주는 품사. **/
    MM,
    /** ([MODIFIERS]: 수식언) ''부사'' : 용언 또는 다른 말 앞에 놓여 그 뜻을 분명하게 하는 품사. **/
    MAG,
    /** ([MODIFIERS]: 수식언) ''접속부사'' : 앞의 체언이나 문장의 뜻을 뒤의 체언이나 문장에 이어 주면서 뒤의 말을 꾸며 주는 부사.
     *
     * ‘ 그러나’, ‘그런데’, ‘그리고’, ‘하지만’ 따위가 있다.
     * */
    MAJ,

    /** (독립언) ''감탄사'' : 말하는 이의 본능적인 놀람이나 느낌, 부름, 응답 따위를 나타내는 말의 부류이다. **/
    IC,

    /** ([POSTPOSITIONS]: 관계언) ''주격 조사'' : 문장 안에서, 체언이 서술어의 주어임을 표시하는 격 조사.
     *
     * ‘이/가’, ‘께서’, ‘에서2’ 따위가 있다.
     * */
    JKS,
    /** ([POSTPOSITIONS]: 관계언) ''보격 조사'' : 문장 안에서, 체언이 보어임을 표시하는 격 조사.
     *
     * ‘철수는 위대한 학자가 되었다.’에서의 ‘가11’, ‘그는 보통 인물이 아니다.’에서의 ‘이27’ 따위이다.
     * */
    JKC,
    /** ([POSTPOSITIONS]: 관계언) ''관형격 조사'' : 문장 안에서, 앞에 오는 체언이 뒤에 오는 체언의 관형어임을 보이는 조사. **/
    JKG,
    /** ([POSTPOSITIONS]: 관계언) ''목적격 조사'' : 문장 안에서, 체언이 서술어의 목적어임을 표시하는 격 조사. ‘을/를’이 있다. **/
    JKO,
    /** ([POSTPOSITIONS]: 관계언) ''부사격 조사'' : 문장 안에서, 체언이 부사어임을 보이는 조사.
     *
     * ‘ 에4’, ‘에서2’, ‘(으)로’, ‘와/과’, ‘보다4’ 따위가 있다. **/
    JKB,
    /** ([POSTPOSITIONS]: 관계언) ''호격 조사'' :
     * 문장 안에서, 체언이 부름의 자리에 놓이게 하여 독립어가 되게 하는 조사.
     *
     * ‘영숙아’의 ‘아9’, ‘철수야’의 ‘야12’ 따위가 있다. **/
    JKV,
    /** ([POSTPOSITIONS]: 관계언) ''인용격 조사'': 앞의 말이 인용됨을 나타내는 조사. **/
    JKQ,
    /** ([POSTPOSITIONS]: 관계언) ''접속 조사'' : 둘 이상의 단어나 구 따위를 같은 자격으로 이어 주는 구실을 하는 조사.
     *
     * ‘와4’, ‘과12’, ‘하고5’, ‘(이)나’, ‘(이)랑’ 따위가 있다. **/
    JC,
    /** ([POSTPOSITIONS]: 관계언) ''보조사'' : 체언, 부사, 활용 어미 따위에 붙어서 어떤 특별한 의미를 더해 주는 조사.
     *
     * ‘은5’, ‘는1’, ‘도15’, ‘만14’, ‘까지3’, ‘마저’, ‘조차8’, ‘부터’ 따위가 있다.
     * */
    JX,

    /** ([ENDINGS]: 어미) ''선어말 어미'' : 어말 어미 앞에 나타나는 어미.
     *
     * * ‘-시-23’, ‘-옵-1’ 따위와 같이 높임법에 관한 것과
     * * ‘-았-’, ‘-는-2’, ‘-더-2’, ‘-겠-’ 따위와 같이 시상(時相)에 관한 것이 있다.
     * */
    EP,
    /** ([ENDINGS]: 어미) ''종결 어미'' : 한 문장을 종결되게 하는 어말 어미.
     *
     * * 동사에는 평서형ㆍ감탄형ㆍ의문형ㆍ명령형ㆍ청유형이 있고,
     * * 형용사에는 평서형ㆍ감탄형ㆍ의문형이 있다.
     * */
    EF,
    /** ([ENDINGS]: 어미) ''연결 어미'' 어간에 붙어 다음 말에 연결하는 구실을 하는 어미.
     *
     * ‘-게10’, ‘-고25’, ‘-(으)며’, ‘-(으)면’, ‘-(으)니’, ‘-아/어’, ‘-지23’ 따위가 있다.
     * */
    EC,
    /** ([ENDINGS]: 어미) ''명사형 전성어미'': 용언의 어간에 붙어 명사의 기능을 수행하게 하는 어미. **/
    ETN,
    /** ([ENDINGS]: 어미) ''관형형 전성어미'': 용언의 어간에 붙어 관형사의 기능을 수행하게 하는 어미. **/
    ETM,

    /** ([AFFIXES]: 접사) ''체언 접두사'' : 파생어를 만드는 접사로, 어근이나 단어의 앞에 붙어 새로운 단어가 되게 하는 말. **/
    XPN,
    /** ([AFFIXES]: 접사) ''용언 접두사'' : 파생어를 만드는 접사로, 어근이나 단어의 앞에 붙어 새로운 단어가 되게 하는 말. **/
    XPV,
    /** ([AFFIXES]: 접사) ''명사 파생 접미사'':
     * 파생어를 만드는 접사로, 어근이나 단어의 뒤에 붙어 새로운 명사가 되게 하는 말.
     * */
    XSN,
    /** ([AFFIXES]: 접사) ''동사 파생 접미사'':
     * 파생어를 만드는 접사로, 어근이나 단어의 뒤에 붙어 새로운 동사가 되게 하는 말.
     * */
    XSV,
    /** ([AFFIXES]: 접사) ''형용사 파생 접미사'':
     * 파생어를 만드는 접사로, 어근이나 단어의 뒤에 붙어 새로운 형용사가 되게 하는 말.
     * */
    XSA,
    /** ([AFFIXES]: 접사) ''부사 파생 접미사'':
     * 파생어를 만드는 접사로, 어근이나 단어의 뒤에 붙어 새로운 부사가 되게 하는 말.
     * */
    XSM,
    /** ([AFFIXES]: 접사) ''기타 접미사'':
     * 파생어를 만드는 접사로, 어근이나 단어의 뒤에 붙어 새로운 단어가 되게 하는 말.
     * */
    XSO,
    /** ''어근'' :
     * 단어를 분석할 때, 실질적 의미를 나타내는 중심이 되는 부분.
     *
     * ‘덮개’의 ‘덮-’, ‘어른스럽다’의 ‘어른1’ 따위이다.
     * */
    XR,

    /** ([SYMBOLS]: 기호) 종결기호: 마침/물음/느낌표 **/
    SF,
    /** ([SYMBOLS]: 기호) 연결기호: 쉼표/가운뎃점/빗금 **/
    SP,
    /** ([SYMBOLS]: 기호) 묶음기호: 괄호/묶음표/따옴표 **/
    SS,
    /** ([SYMBOLS]: 기호) 생략기호: 줄임표 **/
    SE,
    /** ([SYMBOLS]: 기호) 붙임기호: 물결표/줄임표/빠짐표 **/
    SO,
    /** ([SYMBOLS]: 기호) 기타기호 **/
    SW,
    /** 명사 추정 범주 **/
    NF,
    /** 동사 추정 범주 **/
    NV,
    /** 분석 불능 범주 **/
    NA,

    /** 외국어 **/
    SL,
    /** 한자 **/
    SH,
    /** 숫자 **/
    SN,

    /** 임시기호(내부처리용) **/
    TEMP;

    /**
     * Static variables
     */
    companion object {
        /** 전체 **/
        val ALL = values().toSet()
        /** 체언 **/
        val NOUNS = setOf(NNG, NNP, NNB, NNM, NR, NP)
        /** 용언 **/
        val PREDICATES = setOf(VV, VA, VX, VCP, VCN)
        /** 수식언 **/
        val MODIFIERS = setOf(MM, MAG, MAJ)
        /** 관계언(조사) **/
        val POSTPOSITIONS = setOf(JKS, JKC, JKG, JKO, JKB, JKV, JKQ, JC, JX)
        /** 어미 **/
        val ENDINGS = setOf(EP, EF, EC, ETN, ETM)
        /** 접사 **/
        val AFFIXES = setOf(XPN, XPV, XSN, XSV, XSM, XSA, XSO)
        /** 접미사 **/
        val SUFFIXES = setOf(XSN, XSV, XSM, XSA, XSO)
        /** 기호 **/
        val SYMBOLS = setOf(SF, SP, SS, SE, SW, SO)
        /** 미확인 단어 **/
        val UNKNOWNS = setOf(NF, NV, NA)
    }

    /**
     * 이 값이 체언([NOUNS])인지 확인합니다.
     *
     * @since 1.x
     * @return 체언인 경우 True
     */
    fun isNoun(): Boolean = this in NOUNS

    /**
     * 이 값이 용언([PREDICATES])인지 확인합니다.
     *
     * @since 1.x
     * @return 용언인 경우 True
     */
    fun isPredicate(): Boolean = this in PREDICATES

    /**
     * 이 값이 수식언([MODIFIERS])인지 확인합니다.
     *
     * @since 1.x
     * @return 수식언인 경우 True
     */
    fun isModifier(): Boolean = this in MODIFIERS

    /**
     * 이 값이 관계언(조사; [POSTPOSITIONS])인지 확인합니다.
     *
     * @since 1.x
     * @return 관계언인 경우 True
     */
    fun isPostPosition(): Boolean = this in POSTPOSITIONS

    /**
     * 이 값이 어미([ENDINGS])인지 확인합니다.
     *
     * @since 1.x
     * @return 어미인 경우 True
     */
    fun isEnding(): Boolean = this in ENDINGS

    /**
     * 이 값이 접사([AFFIXES])인지 확인합니다.
     *
     * @since 1.x
     * @return 접사인 경우 True
     */
    fun isAffix(): Boolean = this in AFFIXES

    /**
     * 이 값이 접미사([SUFFIXES])인지 확인합니다.
     *
     * @since 1.x
     * @return 접미사인 경우 True
     */
    fun isSuffix(): Boolean = this in SUFFIXES

    /**
     * 이 값이 기호([SYMBOLS])인지 확인합니다.
     *
     * @since 1.x
     * @return 기호인 경우 True
     */
    fun isSymbol(): Boolean = this in SYMBOLS

    /**
     * 이 값이 미확인 단어([UNKNOWNS])인지 확인합니다.
     *
     * @since 1.x
     * @return 미확인 단어인 경우 True
     */
    fun isUnknown(): Boolean = this in UNKNOWNS

    /**
     * 이 값이 주어진 [tag]로 시작하는지 확인합니다.
     *
     * @since 2.0.0
     * @return 그러한 경우 True
     */
    fun startsWith(tag: CharSequence): Boolean {
        val xtag = tag.toString().toUpperCase()
        return if (xtag == "N") name.startsWith(xtag) && !isUnknown() else name.startsWith(xtag)
    }
}

/**
 * 전체 형태소의 개수
 */
internal inline val POS_SIZE get() = POS.values().size

/**
 * (Extension) 이 String 값에 주어진 [tag]가 포함되는지 확인합니다.
 *
 * @since 2.0.0
 * @param[tag] 하위 분류인지 확인할 형태소 품사표기 값
 * @return 하위 분류에 해당한다면 true
 */
operator fun CharSequence.contains(tag: POS): Boolean = tag.startsWith(this)

/*************************************************************/

/**
 * 세종 구문구조 표지자를 Enum class로 담았습니다.
 *
 * @since 2.0.0
 */
enum class PhraseTag {
    /**
     * 구문구조 표지자: 문장
     */
    S,
    /**
     * 구문구조 표지자: 체언 구
     *
     * 문장에서 주어 따위의 기능을 하는 명사, 대명사, 수사 또는 이 역할을 하는 구
     */
    NP,
    /**
     * 구문구조 표지자: 용언 구
     *
     * 문장에서 서술어의 기능을 하는 동사, 형용사 또는 이 역할을 하는 구
     */
    VP,
    /**
     * 구문구조 표지자: 긍정지정사구
     *
     * 무엇이 무엇이라고 지정하는 단어(이다) 또는 이 역할을 하는 구
     */
    VNP,
    /**
     * 구문구조 표지자: 부사구
     *
     * 용언구 또는 다른 말 앞에 놓여 그 뜻을 분명하게 하는 단어 또는 이 역할을 하는 구
     */
    AP,
    /**
     * 구문구조 표지자: 관형사구
     *
     * 체언구 앞에 놓여서, 그 체언구의 내용을 자세히 꾸며 주는 단어 또는 이 역할을 하는 구
     */
    DP,
    /**
     * 구문구조 표지자: 감탄사구
     *
     * 말하는 이의 본능적인 놀람이나 느낌, 부름, 응답 따위를 나타내는 단어 또는 이 역할을 하는 구
     */
    IP,
    /**
     * 구문구조 표지자: 의사(Pseudo) 구
     *
     * 인용부호와 괄호를 제외한 나머지 부호나, 조사, 어미가 단독으로 어절을 이룰 때 (즉, 구를 보통 이루지 않는 것이 구를 이루는 경우)
     */
    X,
    /**
     * 구문구조 표지자: 왼쪽 인용부호
     *
     * 열림 인용부호
     */
    L,
    /**
     * 구문구조 표지자: 오른쪽 인용부호
     *
     * 닫힘 인용부호
     */
    R,
    /**
     * 구문구조 표지자: 인용절
     *
     * 인용 부호 내부에 있는 인용된 절. 세종 표지에서는 Q, U, W, Y, Z가 사용되나 KoalaNLP에서는 하나로 통일함.
     */
    Q
}

/**
 * (Extension) 주어진 목록에 주어진 구문구조 표지 [tag]가 포함되는지 확인합니다.
 *
 * @since 2.0.0
 * @param[tag] 속하는지 확인할 구문구조 표지 값
 * @return 목록 중 하나라도 일치한다면 true
 */
operator fun Iterable<String>.contains(tag: PhraseTag): Boolean = this.any { it == tag.name }

/********************************************************/

/**
 * 의존구문구조 기능표지자를 담은 Enum class입니다.
 * (ETRI 표준안)
 *
 * [참고](http://aiopen.etri.re.kr/data/1.%20%EC%9D%98%EC%A1%B4%20%EA%B5%AC%EB%AC%B8%EB%B6%84%EC%84%9D%EC%9D%84%20%EC%9C%84%ED%95%9C%20%ED%95%9C%EA%B5%AD%EC%96%B4%20%EC%9D%98%EC%A1%B4%EA%B4%80%EA%B3%84%20%EA%B0%80%EC%9D%B4%EB%93%9C%EB%9D%BC%EC%9D%B8%20%EB%B0%8F%20%EC%97%91%EC%86%8C%EB%B8%8C%EB%A0%88%EC%9D%B8%20%EC%96%B8%EC%96%B4%EB%B6%84%EC%84%9D%20%EB%A7%90%EB%AD%89%EC%B9%98.pdf)
 *
 * @since 1.x
 */
enum class DependencyTag {
    /** ''주어'': 술어가 나타내는 동작이나 상태의 주체가 되는 말
     *
     * 주격 체언구(NP_SBJ), 명사 전성 용언구(VP_SBJ), 명사절(S_SBJ) */
    SBJ,

    /** ''목적어'': 타동사가 쓰인 문장에서 동작의 대상이 되는 말
     *
     * 목적격 체언구(NP_OBJ), 명사 전성 용언구(VP_OBJ), 명사절(S_OBJ) */
    OBJ,

    /** ''보어'': 주어와 서술어만으로는 뜻이 완전하지 못한 문장에서, 그 불완전한 곳을 보충하여 뜻을 완전하게 하는 수식어.
     *
     * 보격 체언구(NP_CMP), 명사 전성 용언구(VP_CMP), 인용절(S_CMP) */
    CMP,

    /** 체언 수식어(관형격). 관형격 체언구(NP_MOD), 관형형 용언구(VP_MOD), 관형절(S_MOD) */
    MOD,

    /** 용언 수식어(부사격). 부사격 체언구(NP_AJT), 부사격 용언구(VP_AJT) 문말어미+부사격 조사(S_AJT) */
    AJT,

    /** ''접속어'': 단어와 단어, 구절과 구절, 문장과 문장을 이어 주는 구실을 하는 문장 성분.
     *
     * 접속격 체언(NP_CNJ) */
    CNJ,

    /** 정의되지 않음: 기존 PRN(삽입어구) 포함 */
    UNDEF,

    /** ROOT 지시자 */
    ROOT
}

/**
 * (Extension) 주어진 목록에 주어진 의존구문 표지 [tag]가 포함되는지 확인.
 */
operator fun Iterable<String>.contains(tag: DependencyTag): Boolean = this.any { it == tag.name }

/************************************************************/

/**
 * 의미역(Semantic Role) 분석 표지를 담은 Enum class입니다.
 * (ETRI 표준안)
 *
 * @since 2.0.0
 */
enum class RoleType {
    /** 주 동사 */
    HEAD,
    /**
     * 동작주/행위자. 예: {"진흥왕은" 화랑도를 개편했다.}에서 __<진흥왕은>__.
     */
    ARG0,
    /**
     * 피동작주/대상. 예: {진흥왕은 "화랑도를" 개편했다.}에서 __<화랄도를>__, {"범인은" 사거리에서 발견되었다.}에서 __<범인은>__.
     */
    ARG1,
    /**
     * 시작점/수혜자. 예: {영희가 "철수에게서" 그 선물을 받았다.}에서 __<철수에게서>__.
     */
    ARG2,
    /**
     * 착점. 예: {연이가 "동창회에" 참석했다.}에서 __<동창회에>__.
     */
    ARG3,

    /**
     * 장소. 예: {친구들이 "서울에" 많이 산다.}에서 __<서울에>__.
     */
    ARGM_LOC,
    /**
     * 방향. 예: {달이 "서쪽으로" 기울었다.}에서 __<서쪽으로>__. */
    ARGM_DIR,
    /**
     * 조건. 예: {"과세 대상 금액이 많을수록" 높은 세율을 적용한다.}에서 __<많을수록>__. */
    ARGM_CND,
    /**
     * 방법. 예: {그는 "큰 소리로" 떠들었다.}에서 __<소리로>__. */
    ARGM_MNR,
    /**
     * 시간. 예: {진달래는 "이른 봄에" 핀다.}에서 __<봄에>__. */
    ARGM_TMP,
    /**
     * 범위. 예: {그 악기는 "4개의" 현을 가진다.}에서 __<4개의>__. */
    ARGM_EXT,
    /**
     * 보조서술. 예: {석회암 지대에서 "깔대기 모양으로" 파인 웅덩이가 생겼다.}에서 __<모양으로>__. */
    ARGM_PRD,
    /**
     * 목적. 예: {주나라의 백이와 숙제는 "절개를 지키고자" 수양산에 거처했다.}에서 __<지키고자>__. */
    ARGM_PRP,
    /**
     * 발생 이유. 예: {지난 밤 "강픙으로" 가로수가 넘어졌다.}에서 __<강픙으로>__. */
    ARGM_CAU,
    /**
     * 담화 연결. 예: {"하지만" 여기서 동, 서는 중국과 유럽을 뜻한다.}에서 __<하지만>__. */
    ARGM_DIS,
    /**
     * 부사적 어구. 예: {산의 능선이 "마치" 닭벼슬을 쓴 용의 형상을 닮았다.}에서 __<마치>__. */
    ARGM_ADV,
    /**
     * 부정. 예: {산은 불에 타지 "않았다."}에서 __<않았다.>__. */
    ARGM_NEG,
    /**
     * 도구. 예: {"하얀 천으로" 상자를 덮었다.}에서 __<천으로>__. */
    ARGM_INS
}

/**
 * (Extension) 주어진 목록에 주어진 의미역 표지 [tag]가 포함되는지 확인합니다.
 *
 * @since 2.0.0
 * @param[tag] 속하는지 확인할 구문구조 표지 값
 * @return 목록 중 하나라도 일치한다면 true
 */
operator fun Iterable<String>.contains(tag: RoleType): Boolean = this.any { it == tag.name }

/************************************************************/

/**
 * 대분류 개체명(Named Entity) 유형을 담은 Enum class입니다.
 * (ETRI 표준안)
 *
 * @since 2.0.0
 */
enum class CoarseEntityType {
    /**
     * 사람의 이름
     */
    PS,

    /**
     * 장소의 이름
     */
    LC,

    /**
     * 단체의 이름
     */
    OG,

    /**
     * 작품/물품의 이름
     */
    AF,

    /**
     * 기간/날짜의 이름
     */
    DT,

    /**
     * 시간/간격의 이름
     */
    TI,

    /**
     * 문명/문화활동에 사용되는 명칭
     */
    CV,

    /**
     * 동물의 이름
     */
    AM,

    /**
     * 식물의 이름
     */
    PL,

    /**
     * 수량의 값, 서수 또는 이름
     */
    QT,

    /**
     * 학문분야 또는 학파, 예술사조 등의 이름
     */
    FD,

    /**
     * 이론, 법칙, 원리 등의 이름
     */
    TR,

    /**
     * 사회적 활동, 운동, 사건 등의 이름
     */
    EV,

    /**
     * 화학적 구성물의 이름
     */
    MT,

    /**
     * 용어
     */
    TM
}

/**
 * (Extension) 주어진 목록에 주어진 개체명 유형 [tag]가 포함되는지 확인합니다.
 *
 * @since 2.0.0
 * @param[tag] 속하는지 확인할 구문구조 표지 값
 * @return 목록 중 하나라도 일치한다면 true
 */
operator fun Iterable<String>.contains(tag: CoarseEntityType): Boolean = this.any { it == tag.name }
