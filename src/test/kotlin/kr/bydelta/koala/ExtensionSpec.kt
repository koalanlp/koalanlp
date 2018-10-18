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
                    Pair("斅學相長", "효학상장"))

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
        }
    }

    describe("HangulComposerExtension") {
        // Char.isCompleteHangul, Char.isIncompleteHangul, Char.isHangul,
        // Char.isChosungJamo, Char.isJungsungJamo, Char.isJongsungJamo, Char.isJongsungEnding,
        // CharSequence.isHangulEnding,
        // CharSequence.isJongsungEnding
        it("should correctly identify Hangul characters") {
            'k'.isCompleteHangul() `should be` false
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
                val code = (1..4).map {
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
        }
    }

    describe("VerbApplyCorrectorExtension") {
        // correctVerbApply
        it("should correct verb application") {

            val map =
                    listOf(Pair(Triple("깨닫", true, "아"), "깨달아"),
                            Pair(Triple("붇", true, "어나다"), "불어나다"),
                            Pair(Triple("눋", true, "어"), "눌어"),
                            Pair(Triple("믿", true, "어"), "믿어"),
                            Pair(Triple("묻", true, "어"), "물어"),
                            Pair(Triple("구르", true, "어"), "굴러"),
                            Pair(Triple("모르", true, "아"), "몰라"),
                            Pair(Triple("벼르", true, "어"), "별러"),
                            Pair(Triple("마르", true, "아"), "말라"),
                            Pair(Triple("무르", true, "어"), "물러"),
                            Pair(Triple("누르", true, "어"), "눌러"),
                            Pair(Triple("다르", true, "아"), "달라"),
                            Pair(Triple("사르", true, "아"), "살라"),
                            Pair(Triple("바르", true, "아"), "발라"),
                            Pair(Triple("가르", true, "아"), "갈라"),
                            Pair(Triple("나르", true, "아"), "날라"),
                            Pair(Triple("자르", true, "아"), "잘라"),
                            Pair(Triple("치르", true, "어"), "치러"),
                            Pair(Triple("따르", true, "아"), "따라"),
                            Pair(Triple("다다르", true, "아"), "다다라"),
                            Pair(Triple("우러르", true, "어"), "우러러"),
                            Pair(Triple("들르", true, "어"), "들러"),
                            Pair(Triple("아니꼽", false, "어"), "아니꼬워"),
                            Pair(Triple("무덥", false, "어"), "무더워"),
                            Pair(Triple("우습", false, "어"), "우스워"),
                            Pair(Triple("줍", true, "어"), "주워"),
                            Pair(Triple("더럽", false, "어"), "더러워"),
                            Pair(Triple("무섭", false, "어"), "무서워"),
                            Pair(Triple("귀엽", false, "어"), "귀여워"),
                            Pair(Triple("안쓰럽", false, "ㄴ"), "안쓰러운"),
                            Pair(Triple("아름답", false, "어"), "아름다워"),
                            Pair(Triple("잡", true, "아"), "잡아"),
                            Pair(Triple("뽑", true, "아"), "뽑아"),
                            Pair(Triple("곱", false, "아"), "고와"),
                            Pair(Triple("돕", true, "아"), "도와"),
                            Pair(Triple("뽑", true, "아"), "뽑아"),
                            Pair(Triple("씹", true, "어"), "씹어"),
                            Pair(Triple("업", true, "어"), "업어"),
                            Pair(Triple("입", true, "어"), "입어"),
                            Pair(Triple("잡", true, "아"), "잡아"),
                            Pair(Triple("접", true, "아"), "접어"),
                            Pair(Triple("좁", false, "아"), "좁아"),
                            Pair(Triple("낫", true, "아"), "나아"),
                            Pair(Triple("긋", true, "아"), "그어"),
                            Pair(Triple("벗", true, "아"), "벗어"),
                            Pair(Triple("솟", true, "아"), "솟아"),
                            Pair(Triple("씻", true, "아"), "씻어"),
                            Pair(Triple("뺏", true, "어"), "뺏어"),
                            Pair(Triple("푸", true, "어"), "퍼"),
                            Pair(Triple("끄", true, "아"), "꺼"),
                            Pair(Triple("끄", true, "어"), "꺼"),
                            Pair(Triple("들", true, "아"), "들어"),
                            Pair(Triple("가", true, "아라"), "가거라"),
                            Pair(Triple("삼가", true, "아라"), "삼가거라"),
                            Pair(Triple("들어가", true, "아라"), "들어가거라"),
                            Pair(Triple("오", true, "아라"), "오너라"),
                            Pair(Triple("돌아오", true, "아라"), "돌아오너라"),
                            Pair(Triple("푸르", false, "어"), "푸르러"),
                            Pair(Triple("하", true, "았다"), "하였다"),
                            Pair(Triple("하", true, "었다"), "하였다"),
                            Pair(Triple("영원하", true, "아"), "영원하여"),
                            Pair(Triple("파랗", false, "으면"), "파라면"),
                            Pair(Triple("파랗", false, "ㄴ"), "파란"),
                            Pair(Triple("동그랗", false, "은"), "동그란"),
                            Pair(Triple("파랗", false, "았다"), "파랬다"),
                            Pair(Triple("파랗", false, "을"), "파랄"),
                            Pair(Triple("그렇", false, "아"), "그래"),
                            Pair(Triple("시퍼렇", false, "었다"), "시퍼렜다"),
                            Pair(Triple("그렇", false, "네"), "그렇네"),
                            Pair(Triple("파랗", false, "네"), "파랗네"),
                            Pair(Triple("노랗", false, "네"), "노랗네"),
                            Pair(Triple("좋", false, "아"), "좋아"),
                            Pair(Triple("낳", true, "아"), "낳아"),
                            Pair(Triple("이", true, "라면서"), "이라면서"),
                            Pair(Triple("보", true, "면"), "보면"),
                            Pair(Triple("주장하", true, "았다"), "주장하였다"),
                            Pair(Triple("너그럽", false, "게"), "너그럽게"),
                            Pair(Triple("연결지", true, "었"), "연결졌"),
                            Pair(Triple("다", true, "아"), "다오"),
                            Pair(Triple("눕", true, "으니"), "누우니"),
                            Pair(Triple("눕", true, "자"), "눕자"),
                            Pair(Triple("돕", true, "으면"), "도우면"),
                            Pair(Triple("곱", false, "ㄴ"), "고운"),
                            Pair(Triple("곱", false, "어"), "고와"),
                            Pair(Triple("갑", true, "-"), "갑-"),
                            Pair(Triple("쌓", true, "자고"), "쌓자고"),
                            Pair(Triple("좇", true, "ㄴ"), "좇은"),
                            Pair(Triple("좇", true, "며"), "좇으며"),
                            Pair(Triple("갖", true, "ㄹ"), "갖을"),
                            Pair(Triple("붙", true, "며"), "붙으며"),
                            Pair(Triple("붙", true, "니"), "붙니"),
                            Pair(Triple("불", true, "나"), "부나"),
                            Pair(Triple("불", true, "오"), "부오"),
                            Pair(Triple("사가", true, "안"), "사간"),
                            Pair(Triple("끌", true, "오"), "끄오")
                    )

            map.forEach {
                val (input, result) = it
                val (verb, isVerb, rest) = input

                correctVerbApply(verb, isVerb, rest) `should be equal to` result
            }
        }
    }
})