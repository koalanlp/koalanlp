@file:JvmName("Utils")
@file:JvmMultifileClass

package kr.bydelta.koala

import java.lang.IllegalArgumentException
import java.io.File
import kotlin.streams.toList

/** '가' 위치 **/
const val HANGUL_START = '가'
/** '힣' 위치 **/
const val HANGUL_END = '힣'
/** 종성 범위 **/
const val JONGSUNG_RANGE = 0x001C
/** 중성 범위 **/
const val JUNGSUNG_RANGE = 0x024C


/*********************
 ***** 알파벳 읽기 *****
 *********************/

/**
 * 알파벳의 독음방법
 */
val ALPHABET_READING = mapOf(
        'H' to "에이치", 'W' to "더블유",
        'A' to "에이", 'F' to "에프", 'I' to "아이", 'J' to "제이", 'K' to "케이",
        'S' to "에스", 'V' to "브이", 'X' to "엑스", 'Y' to "와이", 'Z' to "제트",
        'B' to "비", 'C' to "씨", 'D' to "디", 'E' to "이", 'G' to "지",
        'L' to "엘", 'M' to "엠", 'N' to "엔", 'O' to "오", 'P' to "피",
        'Q' to "큐", 'R' to "알", 'T' to "티", 'U' to "유"
).toList().sortedBy { -it.second.length }

/**
 * 주어진 문자열 [this] 에서 알파벳이 발음되는 대로 국문 문자열로 표기하여 값으로 돌려줌.
 */
fun CharSequence.alphaToHangul(): CharSequence {
    val buffer = StringBuffer()
    for (ch in this) {
        val replacement = ALPHABET_READING.find {
            it.first == ch.toUpperCase()
        }?.second ?: ch

        buffer.append(replacement)
    }

    return buffer
}

/**
 * 주어진 문자열 [this]에 적힌 알파벳 발음을 알파벳으로 변환하여 문자열로 반환.
 */
fun CharSequence.hangulToAlpha(): CharSequence {
    val buffer = StringBuffer()
    var index = 0

    while (index < this.length) {
        val found = ALPHABET_READING.find {
            this.startsWith(it.second, startIndex = index)
        }
        when (found) {
            is Pair<Char, String> -> {
                buffer.append(found.first)
                index += found.second.length
            }
            else -> {
                buffer.append(this[index])
                index += 1
            }
        }
    }

    return buffer
}

/**
 * 주어진 문자열 [this]가 알파벳이 발음되는 대로 표기된 문자열인지 확인.
 */
fun CharSequence.isAlphaPronounced(): Boolean {
    var substr = this
    while (substr.isNotEmpty()) {
        val found = ALPHABET_READING.find { substr.startsWith(it.second) }

        if (found == null)
            return false

        substr = substr.drop(found.second.length)
    }

    return true
}

/*********************
 ***** 한자 읽기 *****
 *********************/

/** 현재 문자 [this]가 한자 범위인지 확인. */
fun Char.isHanja(): Boolean {
    val code = this.toInt()
    return this.isCJKHanja() ||
            (code in 0x2E80..0x2EFF) || //   한중일 부수 보충 	 2E80 	 2EFF
            (code in 0x20000..0x2A6DF) || //한중일 통합 한자 확장 	 20000 	 2A6DF
            (code in 0x2F800..0x2FA1F)  //  한중일 호환용 한자 보충 	 2F800 	 2FA1F
}

/** 현재 문자 [this]가 한중일 통합한자, 통합한자 확장 - A, 호환용 한자 범위인지 확인
 * (국사편찬위원회 한자음가사전은 해당 범위에서만 정의되어 있음)
 * */
fun Char.isCJKHanja(): Boolean {
    val code = this.toInt()
    return (code in 0x3400..0x4DBF) ||  //  한중일 통합 한자 확장 - A 	 3400 	 4DBF
            (code in 0x4E00..0x9FBF) || //  한중일 통합 한자 	 4E00 	 9FBF
            (code in 0xF900..0xFAFF)    //  한중일 호환용 한자 	 F900 	 FAFF
}

private val HANJA_READ_TABLE by lazy {
    Object::class.java
            .getResourceAsStream("/hanjaToHangul.csv").bufferedReader()
            .lines()
            .filter { !it.startsWith('#') && !it.isEmpty() }
            .map {
                val (hanja, sound) = it.split(',')
                if (sound.length > 1)
                    hanja[0] to sound.replace(Regex("[\\(\\[\\{\\}\\]\\)]+"), "")[0]
                else hanja[0] to sound[0]
            }.toList().toMap()
}

/** 두음법칙 예외조항이 적용되는 한자 */
private val HEAD_CORRECTION_EXCLUSION = listOf('兩', '年', '里', '理', '輛')

/** 두음법칙이 적용될 초성 */
private val HEAD_CORRECTION_TARGET_CHO = listOf('\u1102', '\u1105')

/** 두음법칙 적용 대상인 모음: ㅑ,ㅒ,ㅕ,ㅖ,ㅛ,ㅠ,ㅣ*/
private val HEAD_CORRECTION_DOUBLE_TARGET = listOf('\u1163', '\u1164', '\u1167', '\u1168', '\u116d', '\u1172', '\u1175')
/** 두음법칙 적용 대상인 모음: ㅏ,ㅐ,ㅗ,ㅚ,ㅜ,ㅡ */
private val HEAD_CORRECTION_SINGLE_TARGET = listOf('\u1161', '\u1162', '\u1169', '\u116c', '\u116e', '\u1173')
/** 두음법칙 적용 대상 */
private val HEAD_CORRECTION_IL = listOf('렬', '률')
/** 不 발음 교정: ㄷ, ㅈ*/
private val HANJA_BU_FIX = listOf('\u1103', '\u110C')

/**
 * 국사편찬위원회 한자음가사전에 따라 한자 표기된 내용을 국문 표기로 전환함.
 *
 * [headCorrection] 값이 true인 경우, whitespace에 따라오는 문자에 두음법칙을 자동 적용함. (기본값 true)
 * 단, 다음 의존명사는 예외: 냥(兩), 년(年), 리(里), 리(理), 량(輛)
 *
 * 다음 두음법칙은 사전을 조회하지 않기 때문에 적용되지 않음에 유의:
 * * 한자 파생어나 합성어에서 원 단어의 두음법칙: 예) "신여성"이 옳은 표기이나 "신녀성"으로 표기됨
 * * 외자가 아닌 이름: 예) "허난설헌"이 옳은 표기이나 "허란설헌"으로 표기됨
 */
@JvmOverloads
fun CharSequence.hanjaToHangul(headCorrection: Boolean = true): CharSequence {
    val buffer = StringBuffer()

    this.forEachIndexed { index, ch ->
        if (ch.isCJKHanja()) {
            var newChar = HANJA_READ_TABLE.getOrDefault(ch, ch)
            val next = if (this.length > index + 1) this[index + 1] else null

            // 不 발음 교정
            if ((ch == '不' || ch == '不') && // 부 문자가 2개 있음
                    next?.isCJKHanja() == true &&
                    (next == '實' || HANJA_READ_TABLE.getOrDefault(next, next).getChosung() in HANJA_BU_FIX)) {
                newChar = '부'
            }

            // 金 발음 교정 (앞에 한자가 있는 경우 성씨가 아닐 확률이 높으므로 '금'으로 표기)
            if (ch == '金' && buffer.isNotEmpty() && this[index - 1].isCJKHanja()) {
                newChar = '금'
            }

            if (!headCorrection) {
                buffer.append(newChar)
            } else if (ch in HEAD_CORRECTION_EXCLUSION && // 냥,년,리,리,량 문자이면서
                    next != null &&
                    next.isHangul()) { //문장의 끝이 아니고 뒤에 한글이 붙는 경우 (보통 의존명사+조사)
                buffer.append(newChar)
            } else if (newChar.getChosung() in HEAD_CORRECTION_TARGET_CHO) {
                if (buffer.isEmpty() || !this[index - 1].isCJKHanja()) {
                    // 첫머리 문자 또는 한자 외의 다른 문자에 이은 한자
                    if (newChar.getJungsung() in HEAD_CORRECTION_DOUBLE_TARGET) {
                        // ㄴ이나 ㄹ이 ㅇ으로 바뀌는 경우:
                        // 한자음 '녀, 뇨, 뉴, 니, 랴, 려, 례, 료, 류, 리' 등 ㄴ 또는 ㄹ+ㅣ나 ㅣ로 시작하는 이중모음이 단어 첫머리에 올 때
                        buffer.append(Triple('\u110B', newChar.getJungsung()!!, newChar.getJongsung())
                                .assembleHangul())
                    } else if (newChar.getJungsung() in HEAD_CORRECTION_SINGLE_TARGET) {
                        // ㄹ이 ㄴ으로 바뀌는 경우:
                        // 한자음 '라, 래, 로, 뢰, 루, 르' 등 ㄹ+ㅣ를 제외한 단모음이 단어 첫머리에 올 때
                        buffer.append(Triple('\u1102', newChar.getJungsung()!!, newChar.getJongsung())
                                .assembleHangul())
                    } else {
                        buffer.append(newChar)
                    }
                } else if (newChar in HEAD_CORRECTION_IL &&
                        (!buffer.isJongsungEnding() || buffer.last().getJongsung() == '\u11AB')) {
                    //    모음이나 ㄴ 받침 뒤에 이어지는 '렬, 률'은 '열, 율'로 발음한다.
                    buffer.append(Triple('\u110B', newChar.getJungsung()!!, newChar.getJongsung())
                            .assembleHangul())
                } else {
                    buffer.append(newChar)
                }
            } else {
                buffer.append(newChar)
            }
        } else
            buffer.append(ch)
    }

    return buffer
}

/*********************
 ***** 한글 재구성 *****
 *********************/

/** 현재 문자 [this]가 초성, 중성, 종성(선택적)을 다 갖춘 문자인지 확인. */
fun Char.isCompleteHangul(): Boolean = this in HANGUL_START..HANGUL_END

/** 현재 문자 [this]가 불완전한 한글 문자인지 확인. */
fun Char.isIncompleteHangul(): Boolean = this.toInt() in 0x1100..0x11FF || this.toInt() in 0x3130..0x318F

/** 현재 문자 [this]가 한글 완성형 또는 조합용 문자인지 확인. */
fun Char.isHangul(): Boolean = this.isCompleteHangul() || this.isIncompleteHangul()

/** 현재 문자열 [this]가 한글로 끝나면 true */
fun CharSequence.isHangulEnding(): Boolean = this.last().isHangul()

/** 현재 문자 [this]가 현대 한글 초성 자음 문자인지 확인. */
fun Char.isChosungJamo(): Boolean = this.toInt() in 0x1100..0x1112

/** 현재 문자 [this]가 현대 한글 중성 모음 문자인지 확인. */
fun Char.isJungsungJamo(): Boolean = this.toInt() in 0x1161..0x1175

/** 현재 문자 [this]가 한글 종성 자음 문자인지 확인. */
fun Char.isJongsungJamo(): Boolean = this.toInt() in 0x11A8..0x11C2

/** 현재 문자 [this]가 종성으로 끝나면 true. */
fun Char.isJongsungEnding(): Boolean = this.isCompleteHangul() && (this - HANGUL_START) % JONGSUNG_RANGE != 0

/** 현재 문자열 [this]가 종성으로 끝나면 true */
fun CharSequence.isJongsungEnding(): Boolean = this.last().isJongsungEnding()

/** 현재 문자 [this]에서 초성 자음문자 분리. 초성이 없으면 null. */
fun Char.getChosung(): Char? =
        if (this.isCompleteHangul()) ((this - HANGUL_START) / JUNGSUNG_RANGE + 0x1100).toChar()
        else if (this.isChosungJamo()) this
        else null

/** 현재 문자 [this]에서 중성 모음문자 분리. 중성이 없으면 null. */
fun Char.getJungsung(): Char? =
        if (this.isCompleteHangul()) ((this - HANGUL_START) % JUNGSUNG_RANGE / JONGSUNG_RANGE + 0x1161).toChar()
        else if (this.isJungsungJamo()) this
        else null

/** 현재 문자 [this]에서 종성 자음문자 분리. 종성이 없으면 null. */
fun Char.getJongsung(): Char? =
        if (this.isJongsungEnding())
            ((this - HANGUL_START) % JONGSUNG_RANGE + 0x11A7).toChar()
        else if (this.isJongsungJamo()) this
        else null

/** 현재 문자 [this]를 초성, 중성, 종성 자음문자로 분리. 종성이 없으면 null. */
fun Char.dissembleHangul(): Triple<Char, Char, Char?>? =
        if (this.isCompleteHangul()) Triple(this.getChosung()!!, this.getJungsung()!!, this.getJongsung())
        else null

/** 현재 문자열 [this]를 초성, 중성, 종성 자음문자로 분리하여 새 문자열을 만듭니다. 종성이 없으면 종성은 쓰지 않습니다. */
fun CharSequence.dissembleHangul(): CharSequence {
    val buffer = StringBuffer()
    for (ch in this) {
        if (ch.isCompleteHangul()) {
            val (cho, jung, jong) = ch.dissembleHangul()!!
            buffer.append(cho)
            buffer.append(jung)
            jong?.let { buffer.append(it) }
        } else {
            buffer.append(ch)
        }
    }

    return buffer
}

/**
 * 초성을 [cho] 문자로, 중성을 [jung] 문자로, 종성을 [jong] 문자로 갖는 한글 문자를 재구성합니다.
 */
@JvmOverloads
fun assembleHangul(cho: Char, jung: Char, jong: Char? = null): Char {
    if (cho.isChosungJamo() && jung.isJungsungJamo()) {
        if (jong != null && jong.isJongsungJamo()) {
            return HANGUL_START + (cho.toInt() - 0x1100) * JUNGSUNG_RANGE +
                    (jung.toInt() - 0x1161) * JONGSUNG_RANGE + (jong.toInt() - 0x11A7)
        } else if (jong == null) {
            return HANGUL_START + (cho.toInt() - 0x1100) * JUNGSUNG_RANGE +
                    (jung.toInt() - 0x1161) * JONGSUNG_RANGE
        }
    }

    throw IllegalArgumentException("($cho, $jung, $jong) cannot construct a hangul character!")
}

/**
 * 초성을 [this.first] 문자로, 중성을 [this.second] 문자로, 종성을 [this.third] 문자로 갖는 한글 문자를 재구성합니다.
 */
fun Triple<Char, Char, Char?>.assembleHangul(): Char =
        assembleHangul(this.first, this.second, this.third)

/**
 * 주어진 문자열에서 초성, 중성, 종성이 연달아 나오는 경우 이를 조합하여 한글 문자를 재구성합니다.
 */
fun CharSequence.assembleHangul(): CharSequence {
    val buffer = StringBuffer()

    for (ch in this) {
        if (buffer.isEmpty() || !buffer.last().isHangul() || !ch.isIncompleteHangul()) {
            buffer.append(ch)
        } else if (ch.isChosungJamo()) {
            buffer.append(ch)
        } else if (ch.isJungsungJamo() && buffer.last().isChosungJamo()) {
            val last = buffer.last()
            buffer.deleteCharAt(buffer.length - 1)
            buffer.append(kr.bydelta.koala.assembleHangul(last, ch))
        } else if (ch.isJongsungJamo() && !buffer.last().isJongsungEnding()) {
            val last = buffer.last()
            buffer.deleteCharAt(buffer.length - 1)
            buffer.append(last + (ch.toInt() - 0x11A7))
        } else {
            buffer.append(ch)
        }
    }

    return buffer
}

/**************************
 ***** 동사 활용 재구성 *****
 **************************/
/** 초성 조합형 문자열 리스트 (UNICODE 순서) */
val HanFirstList by lazy { (0x1100..0x1112).map { it.toChar() }.toTypedArray() }
//'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
/** 중성 조합형 문자열 리스트 (UNICODE 순서) */
val HanSecondList by lazy { (0x1161..0x1175).map { it.toChar() }.toTypedArray() }
//'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
/** 종성 조합형 문자열 리스트 (UNICODE 순서). 가장 첫번째는 null (받침 없음) */
val HanLastList by lazy { (listOf<Char?>(null) + (0x11A8..0x11C2).map { it.toChar() }.toList<Char?>()).toTypedArray() }
//null, 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
/** 초성 문자를 종성 조합형 문자로 변경 */
val ChoToJong by lazy {
    mapOf(
            '\u1100' to '\u11A8', //ㄱ
            '\u1101' to '\u11A9', //ㄲ
            '\u1102' to '\u11AB', //ㄴ
            '\u1103' to '\u11AE', //ㄷ
            '\u1105' to '\u11AF', // ㄹ
            '\u1106' to '\u11B7', // ㅁ
            '\u1107' to '\u11B8', // ㅂ
            '\u1109' to '\u11BA', // ㅅ
            '\u110A' to '\u11BB', // ㅆ
            '\u110B' to '\u11BC', // ㅇ
            '\u110C' to '\u11BD', // ㅈ
            '\u110E' to '\u11BE', // ㅊ
            '\u110F' to '\u11BF', // ㅋ
            '\u1110' to '\u11C0', // ㅌ
            '\u1111' to '\u11C1', // ㅍ
            '\u1112' to '\u11C2', // ㅎ
            // 아래는 완성형 문자
            'ㄱ' to '\u11A8', //ㄱ
            'ㄲ' to '\u11A9', //ㄲ
            'ㄴ' to '\u11AB', //ㄴ
            'ㄷ' to '\u11AE', //ㄷ
            'ㄹ' to '\u11AF', // ㄹ
            'ㅁ' to '\u11B7', // ㅁ
            'ㅂ' to '\u11B8', // ㅂ
            'ㅅ' to '\u11BA', // ㅅ
            'ㅆ' to '\u11BB', // ㅆ
            'ㅇ' to '\u11BC', // ㅇ
            'ㅈ' to '\u11BD', // ㅈ
            'ㅊ' to '\u11BE', // ㅊ
            'ㅋ' to '\u11BF', // ㅋ
            'ㅌ' to '\u11C0', // ㅌ
            'ㅍ' to '\u11C1', // ㅍ
            'ㅎ' to '\u11C2'// ㅎ
    )
}

/** 모음조화용 양성모음 */
private val SecondPos by lazy { listOf(HanSecondList[0], HanSecondList[2]) }
/** 모음조화용 음성모음 */
private val SecondNeg by lazy { listOf(HanSecondList[4], HanSecondList[6]) }
/** 종성 ㄹ로 종료 */
private val endsWithL by lazy { charEndsWith(HanLastList[8]!!) }
/** 모음 ㅡ로 종료 */
private val endsWithEu by lazy { charEndsWithMo(HanSecondList[18]) }
/** 모음 ㅏ로 종료 */
private val endsWithAh by lazy { charEndsWithMo(HanSecondList[0]) }
/** ㄴ으로 시작 */
private val startsWithN by lazy { charStartsWith(HanFirstList[2]) }
/** ㅂ으로 시작 */
private val startsWithB by lazy { charStartsWith(HanFirstList[7]) }
/** ㅅ으로 시작 */
private val startsWithS by lazy { charStartsWith(HanFirstList[9]) }
/** '오'로 시작 */
private val startsWithOh by lazy { charStartsWithMo(HanSecondList[8]) }
/** '아'로 시작 */
private val startsWithAh by lazy { charStartsWithMo(HanSecondList[0]) }
/** '어'로 시작 */
private val startsWithUh by lazy { charStartsWithMo(HanSecondList[4]) }

/**
 * 주어진 용언의 원형 [verb]이 뒷 부분 [rest]와 같이 어미가 붙어 활용될 때, 불규칙 활용 용언과 모음조화를 교정함.
 *
 * 동사인 경우 [isVerb] = true이어야 하며, 형용사인 경우 false이어야 함.
 */
fun correctVerbApply(verb: String, isVerb: Boolean, rest: String): String =
        if (rest.isEmpty()) verb
        else {
            val char = verb.last()
            val next = rest[0]
            val front = verb.dropLast(1)
            val tail = rest.drop(1)

            if (!next.isHangul()) {
                verb + rest
            } else if (next.getChosung() == HanFirstList[11] || next.isIncompleteHangul()) {
                if ((verb.matches(Regex("^벗|솟|씻|뺏$")) && isVerb) ||
                        (char != '낫' && char.getJongsung() == HanLastList[19] && !isVerb))
                    verb + harmony(verb, rest)
                else if (char.getJongsung() == HanLastList[19]) // 종성: ㅅ
                    (char - 19) + harmony(verb, rest)
                else if (verb.matches(Regex("^듣|깨닫|붇|묻|눋$")))
                    front + (char + 1) + harmony(verb, rest)
                else if (verb.matches(Regex("^돕|겁|곱$")))
                    (char - 17) + (addOh(next) + tail)
                else if (verb.matches(Regex("^굽|뽑|씹|업|입|잡|접|좁|집$")))
                    verb + harmony(verb, rest)
                else if (char.getJongsung() == HanLastList[17]) // 종성: ㅂ
                    front + ((char - 17) + (addWoo(next) + tail))
                else if (verb.matches(Regex("^치르|따르|다다르|우러르|들르$")) &&
                        (startsWithAh(next) || startsWithUh(next)))
                    front + harmony(front,
                            assembleHangul(char.getChosung()!!, next.getJungsung()!!, next.getJongsung()) +
                                    tail)
                else if (verb == "푸르" && startsWithUh(next))
                    verb + harmony(verb, (next - 6 * JUNGSUNG_RANGE) + rest.drop(1))
                else if (verb == "푸" && startsWithUh(next))
                    (next + 6 * JUNGSUNG_RANGE) + tail
                else if (char == '르' && (startsWithAh(next) || startsWithUh(next)) && verb.length > 1)
                    front.dropLast(1) + ((front.last() + 8) +
                            harmony(front, (next - 6 * JUNGSUNG_RANGE) + tail))
                else if (char == '하' && (startsWithAh(next) || startsWithUh(next)))
                    verb + harmony("어", (next + 2 * JONGSUNG_RANGE) + tail) //force "ㅕ"
                else if (isVerb && char == '가' && next == '아' && rest.length > 1 &&
                        (tail.first() - HanLastList.indexOf(tail.first().getJongsung())) == '라')
                    verb + ('거' + tail)
                else if (isVerb && char == '오' && next == '아' && rest.length > 1 &&
                        (tail.first() - HanLastList.indexOf(tail.first().getJongsung())) == '라')
                    verb + ('너' + tail)
                else if (verb == "다" && rest == "아")
                    "다오"
                else if (!isVerb && char.getJongsung() == HanLastList.last() && char != '좋') {
                    // 종성: ㅎ
                    if (next.isIncompleteHangul()) {
                        front + ((char - 27 + HanLastList.indexOf(ChoToJong.getOrDefault(next, next))) + tail)
                    } else if (next.getJungsung() == HanSecondList[18]) {
                        // "ㅡ"
                        front + ((char - 27 + HanLastList.indexOf(next.getJongsung())) + tail)
                    } else if (startsWithAh(next) || startsWithUh(next)) {
                        front + (assembleHangul(char.getChosung()!!,
                                next.getJungsung()!! + 1, next.getJongsung()) + tail)
                    } else
                        verb + harmony(verb, rest)
                } else if (endsWithEu(char) &&
                        (startsWithAh(next) || startsWithUh(next))) {
                    // 규칙적탈락: 어간 'ㅡ'탈락. 'ㅡ'가 'ㅏ/ㅓ'앞에서 탈락.
                    front + harmony(front,
                            assembleHangul(char.getChosung()!!,
                                    next.getJungsung()!!, next.getJongsung()) + tail)
                } else if (endsWithL(char) && startsWithOh(next)) {
                    // 규칙적탈락: 어간 'ㄹ'탈락. 'ㄹ'이 'ㄴㅂㅅ오'앞에서 탈락.
                    front + ((char - HanLastList.indexOf(char.getJongsung())) + rest)
                } else if (char.isJongsungEnding() && !endsWithL(char)) {
                    // 규칙적첨가: ('ㄹ'이외의 종료 어간) + '-ㄴ,-ㄹ,-오,-시,-며.'
                    if (next == 'ㄴ' || next == HanLastList[4]) {
                        verb + harmony(verb,
                                assembleHangul(cho = HanFirstList[11],
                                        jung = HanSecondList[18],
                                        jong = HanLastList[4]) + tail)
                    } else if (next == 'ㄹ' || next == HanLastList[8]) {
                        verb + harmony(verb,
                                assembleHangul(cho = HanFirstList[11],
                                        jung = HanSecondList[18],
                                        jong = HanLastList[8]) + tail)
                    } else if (next == '오') {
                        verb + harmony(verb, '으' + rest)
                    } else
                        verb + harmony(verb, rest)
                } else if (char.getJungsung() == HanSecondList.last() && startsWithUh(next)) {
                    // 불규칙: 'ㅎ'종성 + 'ㅓ' = 'ㅎ'탈락 + 어간 어미가 'ㅐ'로 변화
                    front + (assembleHangul(char.getChosung()!!, HanSecondList[6], next.getJongsung()) + tail)
                } else if (endsWithAh(char) && startsWithAh(next)) {
                    front + (assembleHangul(char.getChosung()!!, char.getJungsung()!!, next.getJongsung()) + tail)
                } else {
                    verb + harmony(verb, rest)
                }
            } else if (endsWithL(char) &&
                    (startsWithB(next) || startsWithN(next) || startsWithS(next))) {
                // 규칙적탈락: 어간 'ㄹ'탈락. 'ㄹ'이 'ㄴㅂㅅ오'앞에서 탈락.
                front + harmony(front, (char - HanLastList.indexOf(char.getJongsung())) + rest)
            } else if (char.isJongsungEnding() && !endsWithL(char)) {
                // 규칙적첨가: ('ㄹ'이외의 종료 어간) + '-ㄴ,-ㄹ,-오,-시,-며.'
                if (next == '시' || next == '며') {
                    verb + harmony(verb, '으' + rest)
                } else
                    verb + harmony(verb, rest)
            } else {
                verb + harmony(verb, rest)
            }
        }

/** ㅗ 모음 첨가 */
private fun addOh(ch: Char): Char {
    val jcode = ch.getJungsung()?.let { HanSecondList.indexOf(it) } ?: -1
    return if (ch.isIncompleteHangul()) {
        assembleHangul(cho = HanFirstList[11], jung = HanSecondList[13], jong = ChoToJong.getOrDefault(ch, ch))
    } else if (jcode == 18) // ㅡ->ㅜ
        (ch - JONGSUNG_RANGE * 5).toChar()
    else if (jcode == 0) //ㅏ->ㅘ
        (ch + JONGSUNG_RANGE * 9).toChar()
    else if (jcode == 4) //ㅓ->ㅘ
        (ch + JONGSUNG_RANGE * 5).toChar()
    else
        ch
}

/** ㅜ 모음 첨가 */
private fun addWoo(ch: Char): Char {
    val jcode = ch.getJungsung()?.let { HanSecondList.indexOf(it) } ?: -1
    return if (ch.isIncompleteHangul())
        assembleHangul(cho = HanFirstList[11], jung = HanSecondList[13], jong = ChoToJong.getOrDefault(ch, ch))
    else if (jcode == 18) // ㅡ->ㅜ
        (ch - JONGSUNG_RANGE * 5).toChar()
    else if (jcode == 0) //ㅏ->ㅝ
        (ch + JONGSUNG_RANGE * 14).toChar()
    else if (jcode == 4) //ㅓ->ㅝ
        (ch + JONGSUNG_RANGE * 10).toChar()
    else
        ch
}

/** 모음조화 계산 */
private fun harmony(front: String, rest: String): String =
        if (!rest.first().isCompleteHangul() || (front.isNotEmpty() && !front.first().isCompleteHangul()))
            rest
        else if (front.isEmpty()) {
            val restJung = rest.first().getJungsung()!!
            val ch = rest.first()
            if (SecondPos.contains(restJung))
                assembleHangul(ch.getChosung()!!,
                        SecondNeg[SecondPos.indexOf(restJung)],
                        ch.getJongsung()) + rest.drop(1)
            else
                rest
        } else {
            val frontJung = front.last().getJungsung()
            val isTheCase = frontJung == HanSecondList[0] || frontJung == HanSecondList[8] || frontJung == HanSecondList[2]
            val ch = rest.first()
            val restJung = ch.getJungsung()
            val restCho = ch.getChosung() == HanFirstList[11]
            if (isTheCase && SecondNeg.contains(restJung) && restCho) {
                assembleHangul(ch.getChosung()!!,
                        SecondPos[SecondNeg.indexOf(restJung)],
                        ch.getJongsung()) + rest.substring(1)
            } else if (!isTheCase && SecondPos.contains(restJung) && restCho) {
                assembleHangul(ch.getChosung()!!,
                        SecondNeg[SecondPos.indexOf(restJung)],
                        ch.getJongsung()) + rest.drop(1)
            } else
                rest
        }

/** 초성 자모로 시작하는지 확인 */
private fun charStartsWith(jamo: Char): (Char) -> Boolean = { it.getChosung() == jamo }

/** 종성 자모로 종료하는지 확인 */
private fun charEndsWith(jamo: Char): (Char) -> Boolean = { it.getJongsung() == jamo }

/** 초성 음가 없이 모음으로 시작하는지 확인 */
private fun charStartsWithMo(mo: Char): (Char) -> Boolean =
        { it == mo || (it.getChosung() == HanFirstList[11] && it.getJungsung() == mo) }

/** 받침 없이 지정된 모음으로 종료하는지 확인 */
private fun charEndsWithMo(mo: Char): (Char) -> Boolean =
        { !it.isJongsungEnding() && it.getJungsung() == mo }
