package kr.bydelta.koala

import org.amshove.kluent.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.*

object ExtensionSpec : Spek({
    describe("AlphabetReaderExtension") {
        // CharSequence.alphaToHangul,
        // CharSequence.hangulToAlpha,
        // CharSequence.isAlphaPronounced,
        it("should read alphabet") {
            val examples = listOf(
                    "A" to "에이",
                    "B" to "비",
                    "C" to "씨",
                    "D" to "디",
                    "E" to "이",
                    "F" to "에프",
                    "G" to "지",
                    "H" to "에이치",
                    "I" to "아이",
                    "J" to "제이",
                    "K" to "케이",
                    "L" to "엘",
                    "M" to "엠",
                    "N" to "엔",
                    "O" to "오",
                    "P" to "피",
                    "Q" to "큐",
                    "R" to "알",
                    "S" to "에스",
                    "T" to "티",
                    "U" to "유",
                    "V" to "브이",
                    "W" to "더블유",
                    "X" to "엑스",
                    "Y" to "와이",
                    "Z" to "제트"
            )

            val rand = Random()
            (1..50).forEach {
                val (original, korean) = (1..6).map { _ -> examples[rand.nextInt(examples.size)] }.unzip()

                val originalStr = original.joinToString("")
                val koreanStr = korean.joinToString("")

                originalStr.alphaToHangul().toString() `should equal` koreanStr
                koreanStr.hangulToAlpha().toString() `should equal` originalStr
                koreanStr.isAlphaPronounced() `should be` true
            }

            "삼성 갤럭시 S9".alphaToHangul().toString() `should equal` "삼성 갤럭시 에스9"
            "이마트".hangulToAlpha().toString() `should equal` "E마트"

            "이마트".isAlphaPronounced() `should be` false
        }
    }

    describe("HanjaReaderExtension") {
        // CharSequence.hanjaToHangul
        it("should translate Hanja correctly") {
            val sample = listOf(Pair("可高可下", "가고가하"),
                    Pair("家道和平", "가도화평"),
                    Pair("家傳忠孝", "가전충효"),
                    Pair("家和泰祥", "가화태상"),
                    Pair("竭力盡忠", "갈력진충"),
                    Pair("江深水靜", "강심수정"),
                    Pair("康和器務", "강화기무"),
                    Pair("居安思危", "거안사위"),
                    Pair("擧案齊眉", "거안제미"),
                    Pair("健和誠最", "건화성최"),
                    Pair("格物致知", "격물치지"),
                    Pair("見利思義", "견리사의"),
                    Pair("見月忘指", "견월망지"),
                    Pair("堅忍不拔", "견인불발"),
                    Pair("堅忍不敗", "견인불패"),
                    Pair("敬事而信", "경사이신"),
                    Pair("敬愛和樂", "경애화락"),
                    Pair("敬天乃孝", "경천내효"),
                    Pair("敬天愛人", "경천애인"),
                    Pair("苦盡甘來", "고진감래"),
                    Pair("恭謙克讓", "공겸극양"),
                    Pair("過猶不及", "과유불급"),
                    Pair("寬仁厚德", "관인후덕"),
                    Pair("光風霽月", "광풍제월"),
                    Pair("敎學相長", "교학상장"),
                    Pair("口傳心授", "구전심수"),
                    Pair("君子九思", "군자구사"),
                    Pair("君子不器", "군자불기"),
                    Pair("捲土重來", "권토중래"),
                    Pair("克己復禮", "극기부례"), //극기복례가 맞으나, 음가사전의 대표음 표기는 극기부례로 되어있음
                    Pair("克己常進", "극기상진"),
                    Pair("勤儉和順", "근검화순"),
                    Pair("根深枝茂", "근심지무"),
                    Pair("勤者必成", "근자필성"),
                    Pair("金玉滿堂", "금옥만당"),
                    Pair("氣山心海", "기산심해"),
                    Pair("樂在人和", "낙재인화"),
                    Pair("樂天知命", "낙천지명"),
                    Pair("囊中之錐", "낭중지추"),
                    Pair("駑馬十駕", "노마십가"),
                    Pair("農者之心", "농자지심"),
                    Pair("訥言敏行", "눌언민행"),
                    Pair("能忍自安", "능인자안"),
                    Pair("多情佛心", "다정불심"),
                    Pair("膽大心小", "담대심소"),
                    Pair("大器晩成", "대기만성"),
                    Pair("大道無門", "대도무문"),
                    Pair("大象無形", "대상무형"),
                    Pair("大人君子", "대인군자"),
                    Pair("大人無己", "대인무기"),
                    Pair("大志遠望", "대지원망"),
                    Pair("大中至正", "대중지정"),
                    Pair("達人大觀", "달인대관"),
                    Pair("德盛禮恭", "덕성예공"),
                    Pair("德在人先", "덕재인선"),
                    Pair("德知體技", "덕지체기"),
                    Pair("德必有隣", "덕필유린"),
                    Pair("韜光養晦", "도광양회"),
                    Pair("道法自然", "도법자연"),
                    Pair("道常無名", "도상무명"),
                    Pair("磨斧作針", "마부작침"),
                    Pair("萬古常靑", "만고상청"),
                    Pair("萬事亨通", "만사형통"),
                    Pair("梅經寒苦", "매경한고"),
                    Pair("每事盡善", "매사진선"),
                    Pair("明鏡止水", "명경지수"),
                    Pair("名實相符", "명실상부"),
                    Pair("無愧我心", "무괴아심"),
                    Pair("無愧於天", "무괴어천"),
                    Pair("無念積善", "무념적선"),
                    Pair("無信不立", "무신불립"),
                    Pair("務實力行", "무실력행"), // 무실역행이 맞는 발음이나 두음법칙 적용이 되지 않음
                    Pair("無言實踐", "무언실천"),
                    Pair("無忍不勝", "무인불승"),
                    Pair("無慾淸淨", "무욕청정"),
                    Pair("無爲而和", "무위이화"),
                    Pair("無爲自然", "무위자연"),
                    Pair("無意無技", "무의무기"),
                    Pair("無汗不得", "무한부득"),
                    Pair("無汗不成", "무한불성"),
                    Pair("美悳傳家", "미덕전가"),
                    Pair("敏事愼言", "민사신언"),
                    Pair("拍掌大笑", "박장대소"),
                    Pair("博學篤志", "박학독지"),
                    Pair("反哺之孝", "반포지효"),
                    Pair("百世淸風", "백세청풍"),
                    Pair("百忍三思", "백인삼사"),
                    Pair("報本之心", "보본지심"),
                    Pair("福緣善慶", "복연선경"),
                    Pair("本立道生", "본립도생"),
                    Pair("俯仰無愧", "부앙무괴"),
                    Pair("父慈子孝", "부자자효"),
                    Pair("不狂不及", "불광불급"),
                    Pair("不撓不屈", "불뇨불굴"), // 불요불굴이 맞는 발음이나 두음법칙 적용이 되지 않음
                    Pair("鵬夢蟻生", "붕몽의생"),
                    Pair("非禮勿動", "비례물동"),
                    Pair("邪不犯正", "사불범정"),
                    Pair("射石飮羽", "사석음우"),
                    Pair("思始觀終", "사시관종"),
                    Pair("事人如天", "사인여천"),
                    Pair("思判行省", "사판행성"),
                    Pair("事必歸正", "사필귀정"),
                    Pair("山高水長", "산고수장"),
                    Pair("殺身成仁", "살신성인"),
                    Pair("三思一言", "삼사일언"),
                    Pair("三省吾身", "삼성오신"),
                    Pair("三忍九思", "삼인구사"),
                    Pair("上敬下愛", "상경하애"),
                    Pair("塞翁之馬", "새옹지마"),
                    Pair("瑞氣雲集", "서기운집"),
                    Pair("瑞氣集門", "서기집문"),
                    Pair("先公後私", "선공후사"),
                    Pair("先事後得", "선사후득"),
                    Pair("先手必勝", "선수필승"),
                    Pair("善始善終", "선시선종"),
                    Pair("先憂後樂", "선우후락"),
                    Pair("先正其心", "선정기심"),
                    Pair("先行後言", "선행후언"),
                    Pair("誠勤是寶", "성근시보"),
                    Pair("成實在勤", "성실재근"),
                    Pair("誠心誠意", "성심성의"),
                    Pair("誠意正心", "성의정심"),
                    Pair("少言多行", "소언다행"),
                    Pair("少慾知足", "소욕지족"),
                    Pair("首邱初心", "수구초심"),
                    Pair("修己治人", "수기치인"),
                    Pair("壽福康寧", "수복강녕"),
                    Pair("壽山福海", "수산복해"),
                    Pair("修身齊家", "수신제가"),
                    Pair("水滴石穿", "수적석천"),
                    Pair("水滴成川", "수적성천"),
                    Pair("熟慮斷行", "숙려단행"),
                    Pair("夙興夜寐", "숙흥야매"),
                    Pair("崇德廣業", "숭덕광업"),
                    Pair("崇祖愛族", "숭조애족"),
                    Pair("習勤忘勞", "습근망로"),
                    Pair("始終如一", "시종여일"),
                    Pair("始終一貫", "시종일관"),
                    Pair("信心直行", "신심직행"),
                    Pair("信愛忍和", "신애인화"),
                    Pair("實事求是", "실사구시"),
                    Pair("心安如海", "심안여해"),
                    Pair("身言書判", "신언서판"),
                    Pair("深思高擧", "심사고거"),
                    Pair("深思敏行", "심사민행"),
                    Pair("心身健康", "심신건강"),
                    Pair("心淸高志", "심청고지"),
                    Pair("心淸事達", "심청사달"),
                    Pair("心平氣和", "심평기화"),
                    Pair("安分知足", "안분지족"),
                    Pair("安貧樂道", "안빈낙도"),
                    Pair("愛之實踐", "애지실천"),
                    Pair("養德遠害", "양덕원해"),
                    Pair("語愛顔慈", "어애안자"),
                    Pair("抑强扶弱", "억강부약"),
                    Pair("言辭安定", "언사안정"),
                    Pair("言行一致", "언행일치"),
                    Pair("易地思之", "역지사지"),
                    Pair("吾唯知足", "오유지족"),
                    Pair("溫故知新", "온고지신"),
                    Pair("外柔內剛", "외유내강"),
                    Pair("愚公移山", "우공이산"),
                    Pair("運數大通", "운수대통"),
                    Pair("元亨利貞", "원형이정"),
                    Pair("有備無患", "유비무환"),
                    Pair("流水不腐", "유수불부"),
                    Pair("有始有終", "유시유종"),
                    Pair("唯我獨尊", "유아독존"),
                    Pair("悠悠自適", "유유자적"),
                    Pair("有志竟成", "유지경성"),
                    Pair("殷鑑不遠", "은감불원"),
                    Pair("陰德陽報", "음덕양보"),
                    Pair("里仁爲美", "이인위미"),
                    Pair("仁德智交", "인덕지교"),
                    Pair("仁愛恭儉", "인애공검"),
                    Pair("仁義禮智", "인의예지"),
                    Pair("人一己百", "인일기백"),
                    Pair("仁者無敵", "인자무적"),
                    Pair("忍中有和", "인중유화"),
                    Pair("忍之爲德", "인지위덕"),
                    Pair("一刻千金", "일각천금"),
                    Pair("一諾千金", "일낙천금"),
                    Pair("一念通天", "일념통천"),
                    Pair("一忍百樂", "일인백락"),
                    Pair("一日三省", "일일삼성"),
                    Pair("一心同體", "일심동체"),
                    Pair("一生懸命", "일생현명"),
                    Pair("日善日創", "일선일창"),
                    Pair("一水四見", "일수사견"),
                    Pair("一心萬能", "일심만능"),
                    Pair("一心正念", "일심정념"),
                    Pair("一日一善", "일일일선"),
                    Pair("一以貫之", "일이관지"),
                    Pair("日進月步", "일진월보"),
                    Pair("日就月將", "일취월장"),
                    Pair("一行三昧", "일행삼매"),
                    Pair("立身揚名", "입신양명"),
                    Pair("自彊不息", "자강불식"),
                    Pair("自求多福", "자구다복"),
                    Pair("慈悲無敵", "자비무적"),
                    Pair("自勝者强", "자승자강"),
                    Pair("慈顔愛語", "자안애어"),
                    Pair("自重自愛", "자중자애"),
                    Pair("積德爲福", "적덕위복"),
                    Pair("積德種善", "적덕종선"),
                    Pair("積小成大", "적소성대"),
                    Pair("正道無憂", "정도무우"),
                    Pair("正道無敵", "정도무적"),
                    Pair("正道正行", "정도정행"),
                    Pair("正善如流", "정선여류"),
                    Pair("正心大道", "정심대도"),
                    Pair("正心修己", "정심수기"),
                    Pair("正心修德", "정심수덕"),
                    Pair("正心正行", "정심정행"),
                    Pair("正正堂堂", "정정당당"),
                    Pair("諸行無常", "제행무상"),
                    Pair("切磋琢磨", "절차탁마"),
                    Pair("中正仁義", "중정인의"),
                    Pair("志石心鏡", "지석심경"),
                    Pair("至誠感天", "지성감천"),
                    Pair("至誠無息", "지성무식"),
                    Pair("止於至善", "지어지선"),
                    Pair("至仁無親", "지인무친"),
                    Pair("知足不辱", "지족불욕"),
                    Pair("知足常樂", "지족상락"),
                    Pair("知足者富", "지족자부"),
                    Pair("知行合一", "지행합일"),
                    Pair("眞心盡力", "진심진력"),
                    Pair("眞心出死", "진심출사"),
                    Pair("責任完遂", "책임완수"),
                    Pair("處變不驚", "처변불경"),
                    Pair("處染常淨", "처염상정"),
                    Pair("天道無親", "천도무친"),
                    Pair("川流不息", "천류불식"),
                    Pair("天長地久", "천장지구"),
                    Pair("天天想新", "천천상신"),
                    Pair("天下泰平", "천하태평"),
                    Pair("淸廉潔白", "청렴결백"),
                    Pair("淸心正行", "청심정행"),
                    Pair("淸香滿堂", "청향만당"),
                    Pair("初志一貫", "초지일관"),
                    Pair("寵辱不驚", "총욕불경"),
                    Pair("春華秋實", "춘화추실"),
                    Pair("忠信篤敬", "충신독경"),
                    Pair("忠禮傳家", "충예전가"),
                    Pair("忠孝敬睦", "충효경목"),
                    Pair("他山之石", "타산지석"),
                    Pair("擇善固執", "택선고집"),
                    Pair("擇言篤志", "택언독지"),
                    Pair("破邪顯正", "파사현정"),
                    Pair("庖丁解牛", "포정해우"),
                    Pair("被褐懷玉", "피갈회옥"),
                    Pair("學行一致", "학행일치"),
                    Pair("恒産恒心", "항산항심"),
                    Pair("香遠益淸", "향원익청"),
                    Pair("向學立志", "향학립지"), // 향학입지가 맞는 발음이나, 두음법칙이 적용되지 않음.
                    Pair("虛心平意", "허심평의"),
                    Pair("虛心合道", "허심합도"),
                    Pair("螢雪之功", "형설지공"),
                    Pair("虎視牛行", "호시우행"),
                    Pair("浩然之氣", "호연지기"),
                    Pair("弘益人間", "홍익인간"),
                    Pair("和氣滿堂", "화기만당"),
                    Pair("禍福無門", "화복무문"),
                    Pair("和信家樂", "화신가락"),
                    Pair("和義淸正", "화의청정"),
                    Pair("和而不同", "화이부동"),
                    Pair("孝友文行", "효우문행"),
                    Pair("孝忍知愛", "효인지애"),
                    Pair("孝悌忠信", "효제충신"),
                    Pair("斅學相長", "효학상장"),
                    Pair("雷雨", "뇌우"),
                    Pair("老人恭敬", "노인공경"),
                    Pair("300 兩, 閑兩", "300 냥, 한량"),
                    Pair(" 兩班", " 양반"),
                    Pair("1492 年에", "1492 년에"),
                    Pair("年月日", "연월일"),
                    Pair("30 里를 운행한 2 輛의 기차", "30 리를 운행한 2 량의 기차"),
                    Pair("百分率 發付率 知刻率", "백분율 발부율 지각률"),
                    Pair("一列", "일렬"),
                    Pair("戰列", "전열"),
                    Pair("金吉東 金天動 入金額", "김길동 김천동 입금액"),
                    Pair("不實 不동의 不견", "부실 부동의 불견"))

            for ((hanja, hangul) in sample) {
                hanja.hanjaToHangul().toString() `should be equal to` hangul
            }

            "한 女人이 길을 건넜다".hanjaToHangul(false).toString() `should be equal to` "한 녀인이 길을 건넜다"
            "한 女人이 길을 건넜다".hanjaToHangul(true).toString() `should be equal to` "한 여인이 길을 건넜다"
        }

        // Char.isHanja
        it("should recognize Hanja") {
            '金'.isHanja() `should be` true
            '㹤'.isHanja() `should be` true

            '김'.isHanja() `should be` false

            // 부수 보충
            "⺀⺁⺂⻱⻲⻳".all { it.isHanja() } `should be` true
        }
    }

    describe("HangulComposerExtension") {
        // Char.isCompleteHangul, Char.isIncompleteHangul, Char.isHangul,
        // Char.isChosungJamo, Char.isJungsungJamo, Char.isJongsungJamo, Char.isJongsungEnding,
        // CharSequence.isHangulEnding,
        // CharSequence.isJongsungEnding
        it("should correctly identify Hangul characters") {
            'k'.isCompleteHangul() `should be` false
            '車'.isCompleteHangul() `should be` false
            '\u1161'.isCompleteHangul() `should be` false
            '\u1161'.isJungsungJamo() `should be` true

            val hangulSentence = "무조건 한글로 말하라니 이거 참 난감하군"
            for (ch in hangulSentence) {
                ch.isCompleteHangul() `should be` !ch.isWhitespace()
            }

            val mixedSentence = "SNS '인플루엔서' 쇼핑 피해 심각... 법적 안전장치 미비: ㄱ씨는 요즘 ㄴ SNS에서 갤럭시S"
            for (ch in mixedSentence) {
                if (ch in "SN '.:") {
                    ch.isCompleteHangul() `should be` false
                    ch.isHangul() `should be` false
                    ch.isIncompleteHangul() `should be` false
                    ch.isChosungJamo() `should be` false
                    ch.isJungsungJamo() `should be` false
                    ch.isJongsungJamo() `should be` false
                    ch.isJongsungEnding() `should be` false
                } else if (ch in "ㄱㄴ") {
                    ch.isCompleteHangul() `should be` false
                    ch.isHangul() `should be` true
                    ch.isIncompleteHangul() `should be` true
                    ch.isChosungJamo() `should be` false
                    ch.isJungsungJamo() `should be` false
                    ch.isJongsungJamo() `should be` false
                    ch.isJongsungEnding() `should be` false
                } else {
                    ch.isCompleteHangul() `should be` true
                    ch.isHangul() `should be` true
                    ch.isIncompleteHangul() `should be` false
                    ch.isChosungJamo() `should be` false
                    ch.isJungsungJamo() `should be` false
                    ch.isJongsungJamo() `should be` false
                    ch.isJongsungEnding() `should be` (ch in "인플엔핑심각법적안전장는즘갤럭")
                }
            }

            val fragments = mixedSentence.split(" ")
            for (fragment in fragments) {
                fragment.isHangulEnding() `should be` (fragment !in listOf("SNS", "'인플루엔서'", "심각...", "미비:", "갤럭시S"))
                fragment.isJongsungEnding() `should be` (fragment in listOf("쇼핑", "법적", "ㄱ씨는", "요즘"))
            }
        }

        // Char.getChosung, Char.getJungsung, Char.getJongsung
        it("correctly dissemble hangul character") {
            for (ch in "SNS '인플루엔서' 쇼핑 피해 심각... 법적 안전장치 미비: ㄱ씨는 요즘 ㄴ SNS에서 갤럭시S") {
                if (ch.isCompleteHangul()) {
                    val cho = ch.getChosung()
                    cho?.isChosungJamo() `should be` true
                    cho?.isJungsungJamo() `should be` false
                    cho?.isJongsungJamo() `should be` false
                    cho?.getChosung() `should equal` ch.getChosung()
                    cho?.getJungsung() `should be` null
                    cho?.getJongsung() `should be` null

                    val jung = ch.getJungsung()
                    jung?.isChosungJamo() `should be` false
                    jung?.isJungsungJamo() `should be` true
                    jung?.isJongsungJamo() `should be` false
                    jung?.getChosung() `should be` null
                    jung?.getJungsung() `should equal` ch.getJungsung()
                    jung?.getJongsung() `should be` null

                    val jong = ch.getJongsung()
                    if (jong != null) {
                        jong.isChosungJamo() `should be` false
                        jong.isJungsungJamo() `should be` false
                        jong.isJongsungJamo() `should be` true
                        jong.getChosung() `should be` null
                        jong.getJungsung() `should be` null
                        jong.getJongsung() `should equal` ch.getJongsung()
                    }
                } else {
                    ch.getChosung() `should be` null
                    ch.getJungsung() `should be` null
                    ch.getJongsung() `should be` null
                }
            }
        }

        // Char.dissembleHangul
        // CharSequence.dissembleHangul
        // assembleHangul
        // Triple.assembleHangul,
        // CharSequence.assembleHangul
        it("can assemble Korean jamo (sequence)") {
            val rand = Random()

            (1..100).forEach { _ ->
                val code = (1..4).map { _ ->
                    Triple(('\u1100' + rand.nextInt(18)),
                            ('\u1161' + rand.nextInt(20)),
                            rand.nextInt(28).let { if (it == 0) null else ('\u11A7' + it) })
                }
                val tupleStr = code.joinToString("") { it.assembleHangul().toString() }
                val seqStr = code.flatMap {
                    if (it.third == null) listOf(it.first, it.second) else it.toList()
                }.joinToString("").assembleHangul().toString()
                val str = code.joinToString("") {
                    assembleHangul(it.first, it.second, it.third).toString()
                }

                str.isJongsungEnding() `should be equal to` str.last().isJongsungEnding()
                str.last().isJongsungEnding() `should be equal to` (code.last().third != null)

                str `should be equal to` tupleStr
                str `should be equal to` seqStr

                str.first().getChosung()?.toInt() `should equal` code.first().first.toInt()
                str.first().getJungsung()?.toInt() `should equal` code.first().second.toInt()
                str.first().getJongsung()?.toInt() `should equal` code.first().third?.toInt()

                str.dissembleHangul().map { it.toInt() } shouldContainSame code.flatMap { if (it.third != null) it.toList() else listOf(it.first, it.second) }.map { it?.toInt() }
                str.first().dissembleHangul() `should equal` code[0]
            };

            { assembleHangul('a', 'b') } `should throw` IllegalArgumentException::class

            val sampleString = "SNS '인플루엔서' 쇼핑 피해 심각... 법적 안전장치 미비: ㄱ씨는 요즘 ㄴ SNS에서 갤럭시S \u1100\u1100 \u11A8\u11A8"
            sampleString.dissembleHangul().assembleHangul().toString() `should be equal to` sampleString
        }
    }

    describe("VerbApplyCorrectorExtension") {
        // correctVerbApply
        it("should correct verb application") {

            val map = """
                |V 벗 아/어/ㅏ/ㅓ 벗어 자 벗자
                |V 솟 아/어/ㅏ/ㅓ 솟아 니 솟니 자 솟자
                |V 씻 아/어/ㅏ/ㅓ 씻어 니 씻니 자 씻자
                |V 뺏 아/어/ㅏ/ㅓ 뺏어 니 뺏니 자 뺏자
                |A 낫 아/어/ㅏ/ㅓ 나아 니 낫니 자 낫자
                |V 젓 아/어/ㅏ/ㅓ 저어 는 젓는 자 젓자
                |V 긋 아/어/ㅏ/ㅓ 그어 기 긋기 니 긋니
                |V 앗 아/어/ㅏ/ㅓ 앗아 기 앗기 니 앗니 ㄴ 앗은
                |V 빼앗 아/어/ㅏ/ㅓ 빼앗아 기 빼앗기 으니 빼앗으니
                |V 잣 아/어/ㅏ/ㅓ 자아 으니 자으니 소 잣소
                |V 듣 아/어/ㅏ/ㅓ 들어 소 듣소 기 듣기 니 듣니
                |V 깨닫 아/어/ㅏ/ㅓ 깨달아 니 깨닫니
                |A 붇 아/어/ㅏ/ㅓ 불어 은 불은 ㅁ 불음 네 붇네
                |V 뜯 아/어/ㅏ/ㅓ 뜯어 은 뜯은 ㅁ 뜯음 네 뜯네
                |A 눋 아/어/ㅏ/ㅓ 눌어 은 눌은 ㅁ 눌음 네 눋네
                |V 겯 아/어/ㅏ/ㅓ 결어
                |V 싣 아/어/ㅏ/ㅓ 실어
                |V 일컫 아/어/ㅏ/ㅓ 일컬어
                |V 묻 아/어/ㅏ/ㅓ 묻어
                |V 걷 아/어/ㅏ/ㅓ 걸어
                |V 돕 아/어/ㅏ/ㅓ 도와 은 도운 ㅁ 도움
                |A 곱 아/어/ㅏ/ㅓ 고와 은 고운 ㅁ 고움
                |A 굽 아/어/ㅏ/ㅓ 굽어 은 굽은 ㅁ 굽음
                |V 뽑 아/어/ㅏ/ㅓ 뽑아 은 뽑은 ㅁ 뽑음
                |V 씹 아/어/ㅏ/ㅓ 씹어 은 씹은 ㅁ 씹음
                |V 업 아/어/ㅏ/ㅓ 업어 은 업은 ㅁ 업음
                |V 입 아/어/ㅏ/ㅓ 입어 은 입은 ㅁ 입음
                |V 잡 아/어/ㅏ/ㅓ 잡아 은 잡은 ㅁ 잡음
                |V 접 아/어/ㅏ/ㅓ 접어 은 접은 ㅁ 접음
                |A 좁 아/어/ㅏ/ㅓ 좁아 은 좁은 ㅁ 좁음
                |V 집 아/어/ㅏ/ㅓ 집어 은 집은 ㅁ 집음
                |A 덥 아/어/ㅏ/ㅓ 더워 은 더운 ㅁ 더움
                |A 우습 아/어/ㅏ/ㅓ 우스워 은 우스운 ㅁ 우스움
                |V 줍 아/어/ㅏ/ㅓ 주워 은 주운 ㅁ 주움
                |A 더럽 아/어/ㅏ/ㅓ 더러워 은 더러운 ㅁ 더러움
                |A 무섭 아/어/ㅏ/ㅓ 무서워 은 무서운 ㅁ 무서움
                |A 귀엽 아/어/ㅏ/ㅓ 귀여워 은 귀여운 ㅁ 귀여움
                |A 안쓰럽 아/어/ㅏ/ㅓ 안쓰러워 은 안쓰러운 ㅁ 안쓰러움
                |A 아름답 아/어/ㅏ/ㅓ 아름다워 은 아름다운
                |A 아니꼽 아/어/ㅏ/ㅓ 아니꼬워 은 아니꼬운
                |A 아깝 아/어/ㅏ/ㅓ 아까워 은 아까운
                |A 감미롭 아/어/ㅏ/ㅓ 감미로워 은 감미로운
                |V 구르 아/어/ㅏ/ㅓ 굴러 은 구른
                |V 모르 아/어/ㅏ/ㅓ 몰라 은 모른
                |V 벼르 아/어/ㅏ/ㅓ 별러 은 벼른
                |V 마르 아/어/ㅏ/ㅓ 말라 은 마른
                |V 무르 아/어/ㅏ/ㅓ 물러 은 무른
                |V 누르 아/어/ㅏ/ㅓ 눌러 은 누른
                |V 다르 아/어/ㅏ/ㅓ 달라 은 다른
                |V 사르 아/어/ㅏ/ㅓ 살라 은 사른
                |V 바르 아/어/ㅏ/ㅓ 발라 은 바른
                |V 가르 아/어/ㅏ/ㅓ 갈라 은 가른
                |V 나르 아/어/ㅏ/ㅓ 날라 은 나른
                |V 자르 아/어/ㅏ/ㅓ 잘라 은 자른
                |V 치르 아/어/ㅏ/ㅓ 치러 은 치른
                |V 따르 아/어/ㅏ/ㅓ 따라 은 따른
                |V 다다르 아/어/ㅏ/ㅓ 다다라 은 다다른
                |V 우러르 아/어/ㅏ/ㅓ 우러러 은 우러른
                |V 들르 아/어/ㅏ/ㅓ 들러
                |A 푸르 아/어/ㅏ/ㅓ 푸르러 은 푸른
                |V 이르 아/어/ㅏ/ㅓ 이르러 은 이른
                |A 노르 아/어/ㅏ/ㅓ 노르러 은 노른
                |V 푸 아/어/ㅏ/ㅓ 퍼 ㄴ 푼
                |V 끄 아/어/ㅏ/ㅓ 꺼 ㄴ 끈
                |V 부수 아/어/ㅏ/ㅓ 부숴
                |V 주 아/어/ㅏ/ㅓ 줘
                |V 누 아/어/ㅏ/ㅓ 눠
                |V 주 어지다 주어지다
                |V 붓 아/어/ㅏ/ㅓ 부어
                |V 따르 아/어/ㅏ/ㅓ 따라
                |V 모으 아/어/ㅏ/ㅓ 모아
                |V 쓰 이다 쓰이다
                |V 아니하 았다/었다 아니하였다
                |V 영원하 아/어 영원하여 았다/었다 영원하였다 ㄴ 영원한 ㄹ 영원할
                |V 달 아라/어라 다오 았네 달았네
                |V 다 아 다오
                |A 파랗 으면 파라면 은 파란 았/었 파랬 니 파랗니 네 파랗네 ㅂ니다 파랗습니다
                |A 동그랗 으면 동그라면 은 동그란 았/었 동그랬 니 동그랗니 네 동그랗네 ㅂ니다 동그랗습니다
                |A 그렇 으면 그러면 은 그런 았/었 그랬 아/어 그래 네 그렇네
                |A 시퍼렇 으면 시퍼러면 은 시퍼런 아/어 시퍼레 네 시퍼렇네
                |A 좋 으면 좋으면 아 좋아 네 좋네
                |V 낳 으면 낳으면 아 낳아 네 낳네
                |A 않 으면 않으면 아 않아 네 않네
                |V 불 으면 불으면 아 불어 래 불래
                |V 주장하 면 주장하면 었다/았다 주장하였다
                |V 연결지 었 연결졌
                |V 갚 - 갚- 다 갚다 은/ㄴ 갚은
                |V 쌓 으면 쌓으면 자고 쌓자고 오 쌓으오 시 쌓으시 며 쌓으며
                |V 좇 으니 좇으니 ㄴ 좇은 오 좇으오 시 좇으시 며 좇으며
                |V 갖 ㄴ 갖은 ㄹ 갖을 오 갖으오 시 갖으시 며 갖으며
                |V 불 니 부니 오 부오 시 부시
                |V 끌 오 끄오 니 끄니 ㅂ니다 끕니다 시 끄시 네 끄네
                |V 알 오 아오 니 아니 네 아네 ㅂ니다 압니다 시 아시
                |V 사 ㄴ 산
                |V 기 다 기다 어 겨 기 기기
                |V google ㅂ니다 google습니다 ㄹ google을 하 google하
                |V 하 ㅂ니다 합니다 다 하다 시 하시
                |V 박 았다 박았다
                |V 사 았다/었다 샀다 ㄴ 산 ㄹ 살
                |V 서 았다/었다 섰다 ㄴ 선 ㄹ 설
                |V 갚 ㄹ 갚을 았다 갚았다
                |V 울 ㅁ 울음
                |""".trimMargin().trim().split("\n").flatMap { verbs ->
                val splits = verbs.split(" ")
                val isVerb = splits[0] == "V"
                val root = splits[1]
                (2 until splits.size step 2).flatMap { i ->
                    val eomi = splits[i]
                    val result = splits[i + 1]

                    eomi.split("/").flatMap { str ->
                        if (str.first() in "ㄴㅂㄹㅁ") {
                            listOf(str, ChoToJong[str.first()]!! + str.drop(1)).map {
                                Pair(Triple(root, isVerb, it), result)
                            }
                        } else if (str.first() == 'ㅏ') {
                            listOf(assembleHangul(HanFirstList[11], HanSecondList[0]) + str.drop(1)).map {
                                Pair(Triple(root, isVerb, it), result)
                            }
                        } else if (str.first() == 'ㅓ') {
                            listOf(assembleHangul(HanFirstList[11], HanSecondList[4]) + str.drop(1)).map {
                                Pair(Triple(root, isVerb, it), result)
                            }
                        } else
                            listOf(Pair(Triple(root, isVerb, str), result))
                    }
                }
            }

            map.forEach {
                val (input, result) = it
                val (verb, isVerb, rest) = input

                correctVerbApply(verb, isVerb, rest) `should be equal to` result
            }
        }
    }
})