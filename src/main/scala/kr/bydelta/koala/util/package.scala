package kr.bydelta.koala

/**
  * 한국어 처리에 도움이 되는, Utility 모음입니다.
  */
package object util {
  private final val SecondPos = Seq('ㅏ', 'ㅑ')
  private final val SecondNeg = Seq('ㅓ', 'ㅕ')
  private lazy val endsWithL = charEndsWith('ㄹ')(_)
  private lazy val endsWithEu = charEndsWithMo('ㅡ')(_)
  private lazy val endsWithAh = charEndsWithMo('ㅏ')(_)
  private lazy val startsWithN = charStartsWith('ㄴ')(_)
  private lazy val startsWithB = charStartsWith('ㅂ')(_)
  private lazy val startsWithS = charStartsWith('ㅅ')(_)
  private lazy val startsWithOh = charStartsWithMo('ㅗ')(_)
  private lazy val startsWithAh = charStartsWithMo('ㅏ')(_)
  private lazy val startsWithUh = charStartsWithMo('ㅓ')(_)
  val HanFirstList = Array('ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')
  val HanSecondList = Array('ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ')
  val HanLastList = Array('\u0000', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')

  def reunionKorean(seq: Seq[Char], acc: Seq[Char] = Seq.empty): String =
    if (seq.isEmpty) new String(acc.reverse.toArray)
    else {
      val char = seq.head
      val newAcc =
        if (!char.isHangul || acc.isEmpty || !acc.head.isHangul) {
          // (앞 문자든, 현재 문자든) 한글이 아닐 때.
          char +: acc
        } else if (HanSecondList.contains(char)) {
          // 지금 문자가 중성일때. 앞문자는 완성문자이거나, 초성이거나, 불완전한 글자임.
          if (!HanFirstList.contains(acc.head)) {
            // 앞에 초성만 있지 않을때.
            if (!acc.head.isCompleteHangul || !acc.head.endsWithJongsung) {
              // 앞에 완성문자가 아니거나(초성문자 제외), 종성으로 끝나지 않을 때.
              char +: acc
            } else {
              // 종성으로 끝나는 완전한 문자.
              val chosung = HanFirstList.indexOf(HanLastList(acc.head.getJongsungCode))
              val newFrontChar = (acc.head - acc.head.getJongsungCode).toChar
              val newChar: Char = reconstructKorean(chosung, HanSecondList.indexOf(char))
              newChar +: newFrontChar +: acc.tail
            }
          } else {
            // 앞에 초성만 있었을때.
            val chosung = HanFirstList.indexOf(acc.head)
            val newChar: Char = reconstructKorean(chosung, HanSecondList.indexOf(char))
            newChar +: acc.tail
          }
        } else {
          if (!acc.head.isCompleteHangul || acc.head.endsWithJongsung) {
            // 앞에 아무것도 없거나, 불완전하거나, 종성으로 이미 끝났을때.
            char +: acc
          } else if (HanFirstList.contains(char)) {
            // 종성으로 끝나지않은, 완전한 문자이고, 따라오는 문자가 초성일때.
            val newChar: Char = (acc.head + HanLastList.indexOf(char)).toChar
            newChar +: acc.tail
          } else {
            // 따라오는 문자가 초성이 아닐때.
            char +: acc
          }
        }
      reunionKorean(seq.tail, newAcc)
    }

  def reduceVerbApply(verb: Seq[Char], isVerb: Boolean, rest: Seq[Char]): Seq[Char] =
  if (rest.isEmpty) verb
  else {
    val verbStr = verb.mkString
    val restStr = rest.mkString
    val verbRev = verb.reverse
    val char = verbRev.head
    val next = rest.head

    if (!next.isHangul) {
      verb ++ rest
    } else if (next.getChosungCode == 11 || next.isIncompleteHangul) {
      if ((verbStr.matches("^벗|솟|씻|뺏$") && isVerb) ||
        (char != '낫' && char.getJongsungCode == 19 && !isVerb))
        verb ++ harmony(verbRev, rest)
      else if (char.getJongsungCode == 19) // 종성: ㅅ
        (char - char.getJongsungCode).toChar +: harmony(verbRev, rest)
      else if (verbStr.matches("^듣|깨닫|붇|묻|눋$"))
        verbRev.tail.reverse ++: (char + 1).toChar +: harmony(verbRev, rest)
      else if (verbStr.matches("^돕|겁|곱$"))
        (char - char.getJongsungCode).toChar +: (addOh(rest.head) +: rest.tail)
      else if (verbStr.matches("^굽|뽑|씹|업|입|잡|접|좁|집$"))
        verb ++ harmony(verbRev, rest)
      else if (char.getJongsungCode == 17) // 종성: ㅂ
        verbRev.tail.reverse ++: ((char - 17).toChar +: (addWoo(rest.head) +: rest.tail))
      else if (verbStr.matches("^치르|따르|다다르|우러르|들르$") &&
        (startsWithAh(next) || startsWithUh(next)))
        verbRev.tail.reverse ++:
          harmony(verbRev.tail, reconstructKorean(char.getChosungCode, next.getJungsungCode, next.getJongsungCode) +: rest.tail)
      else if (verbStr == "푸르" && startsWithUh(next))
        verb ++:
          harmony(verbRev, (next - 6 * JUNGSUNG_RANGE).toChar +: rest.tail)
      else if (verbStr == "푸" && startsWithUh(next))
        (next + 6 * JUNGSUNG_RANGE).toChar +: rest.tail
      else if (char == '르' && (startsWithAh(next) || startsWithUh(next)) && verb.length > 1)
        verbRev.tail.tail.reverse ++: ((verbRev.tail.head + 8).toChar +:
          harmony(verbRev.tail, (next - 6 * JUNGSUNG_RANGE).toChar +: rest.tail))
      else if (char == '하' && (startsWithAh(next) || startsWithUh(next)))
        verb ++: harmony(Seq('어'), (next + 2 * JONGSUNG_RANGE).toChar +: rest.tail) //force "ㅕ"
      else if (isVerb && char == '가' && next == '아' && rest.length > 1 && (rest.tail.head - rest.tail.head.getJongsungCode) == '라')
        verb ++: ('거' +: rest.tail)
      else if (isVerb && char == '오' && next == '아' && rest.length > 1 && (rest.tail.head - rest.tail.head.getJongsungCode) == '라')
        verb ++: ('너' +: rest.tail)
      else if (verbStr == "다" && restStr == "아")
        "다오".toSeq
      else if (!isVerb && char.getJongsungCode == 27 && char != '좋') {
        // 종성: ㅎ
        if (next.isIncompleteHangul) {
          verbRev.tail.reverse ++ ((char - 27 + HanLastList.indexOf(next)).toChar +: rest.tail)
        } else if (next.getJungsungCode == 18) {
          // "ㅡ"
          verbRev.tail.reverse ++ ((char - 27 + next.getJongsungCode).toChar +: rest.tail)
        } else if (startsWithAh(next) || startsWithUh(next)) {
          verbRev.tail.reverse ++
            (reconstructKorean(char.getChosungCode, next.getJungsungCode + 1, next.getJongsungCode) +: rest.tail)
        } else
          verb ++ harmony(verbRev, rest)
      } else if (endsWithEu(char) &&
        (startsWithAh(next) || startsWithUh(next))) {
        // 규칙적탈락: 어간 'ㅡ'탈락. 'ㅡ'가 'ㅏ/ㅓ'앞에서 탈락.
        verbRev.tail.reverse ++:
          harmony(verbRev.tail, reconstructKorean(char.getChosungCode, next.getJungsungCode, next.getJongsungCode) +: rest.tail)
      } else if (endsWithL(char) && startsWithOh(next)) {
        // 규칙적탈락: 어간 'ㄹ'탈락. 'ㄹ'이 'ㄴㅂㅅ오'앞에서 탈락.
        verbRev.tail ++ ((char - char.getJongsungCode).toChar +: rest)
      } else if (char.endsWithJongsung && !endsWithL(char)) {
        // 규칙적첨가: ('ㄹ'이외의 종료 어간) + '-ㄴ,-ㄹ,-오,-시,-며.'
        if (next == 'ㄴ' || next == 'ㄹ') {
          verb ++ harmony(verbRev, reconstructKorean(jung = 18, jong = HanLastList.indexOf(next)) +: rest.tail)
        } else if (next == '오') {
          verb ++ harmony(verbRev, '으' +: rest)
        } else
          verb ++ harmony(verbRev, rest)
      } else if (char.getJungsungCode == HanSecondList.length - 1 && startsWithUh(next)) {
        // 불규칙: 'ㅎ'종성 + 'ㅓ' = 'ㅎ'탈락 + 어간 어미가 'ㅐ'로 변화
        verbRev.tail.reverse ++: (reconstructKorean(char.getChosungCode, 6, next.getJongsungCode) +: rest.tail)
      } else if (endsWithAh(char) && startsWithAh(next)) {
        verbRev.tail.reverse ++
          (reconstructKorean(char.getChosungCode, char.getJungsungCode, next.getJongsungCode) +: rest.tail)
      } else {
        verb ++ harmony(verbRev, rest)
      }
    } else if (endsWithL(char) &&
      (startsWithB(next) || startsWithN(next) || startsWithS(next))) {
      // 규칙적탈락: 어간 'ㄹ'탈락. 'ㄹ'이 'ㄴㅂㅅ오'앞에서 탈락.
      verbRev.tail.reverse ++: harmony(verbRev.tail, (char - char.getJongsungCode).toChar +: rest)
    } else if (char.endsWithJongsung && !endsWithL(char)) {
      // 규칙적첨가: ('ㄹ'이외의 종료 어간) + '-ㄴ,-ㄹ,-오,-시,-며.'
      if (next == '시' || next == '며') {
        verb ++ harmony(verbRev, '으' +: rest)
      } else
        verb ++ harmony(verbRev, rest)
    } else {
      verb ++ harmony(verbRev, rest)
    }
  }

  private def addOh(ch: Char): Char = {
    val jcode = ch.getJungsungCode
    if (ch.isIncompleteHangul)
      reconstructKorean(jong = HanLastList.indexOf(ch), jung = 13)
    else if (jcode == 18) // ㅡ->ㅜ
      (ch - JONGSUNG_RANGE * 5).toChar
    else if (jcode == 0) //ㅏ->ㅘ
      (ch + JONGSUNG_RANGE * 9).toChar
    else if (jcode == 4) //ㅓ->ㅘ
      (ch + JONGSUNG_RANGE * 5).toChar
    else
      ch
  }

  private def addWoo(ch: Char): Char = {
    val jcode = ch.getJungsungCode
    if (ch.isIncompleteHangul)
      reconstructKorean(jong = HanLastList.indexOf(ch), jung = 13)
    else if (jcode == 18) // ㅡ->ㅜ
      (ch - JONGSUNG_RANGE * 5).toChar
    else if (jcode == 0) //ㅏ->ㅝ
      (ch + JONGSUNG_RANGE * 14).toChar
    else if (jcode == 4) //ㅓ->ㅝ
      (ch + JONGSUNG_RANGE * 10).toChar
    else
      ch
  }

  private def harmony(front: Seq[Char], rest: Seq[Char]) =
    if (!rest.head.isCompleteHangul || (front.nonEmpty && !front.head.isCompleteHangul))
      rest
    else if (front.isEmpty) {
      val restJung = HanSecondList(rest.head.getJungsungCode)
      val ch = rest.head
      if (SecondPos.contains(restJung))
        reconstructKorean(ch.getChosungCode,
          HanSecondList.indexOf(SecondNeg(SecondPos.indexOf(restJung))), ch.getJongsungCode) +: rest.tail
      else
        rest
    } else {
      val frontJung = HanSecondList(front.head.getJungsungCode)
      val isTheCase = frontJung == 'ㅏ' || frontJung == 'ㅗ' || frontJung == 'ㅑ'
      val ch = rest.head
      val restJung = HanSecondList(ch.getJungsungCode)
      val restCho = ch.getChosungCode == 11
      if (isTheCase && SecondNeg.contains(restJung) && restCho) {
        reconstructKorean(ch.getChosungCode,
          HanSecondList.indexOf(SecondPos(SecondNeg.indexOf(restJung))), ch.getJongsungCode) +: rest.tail
      } else if (!isTheCase && SecondPos.contains(restJung) && restCho) {
        reconstructKorean(ch.getChosungCode,
          HanSecondList.indexOf(SecondNeg(SecondPos.indexOf(restJung))), ch.getJongsungCode) +: rest.tail
      } else
        rest
    }

  private def charStartsWith(jamo: Char)(ch: Char) =
    ch.getChosungCode == HanFirstList.indexOf(jamo)

  private def charEndsWith(jamo: Char)(ch: Char) =
    ch.getJongsungCode == HanLastList.indexOf(jamo)

  private def charStartsWithMo(mo: Char)(ch: Char) =
    (ch == mo) || ((ch.getChosungCode == HanFirstList.indexOf('ㅇ')) &&
      (ch.getJungsungCode == HanSecondList.indexOf(mo)))

  private def charEndsWithMo(mo: Char)(ch: Char) =
    !ch.endsWithJongsung &&
      (ch.getJungsungCode == HanSecondList.indexOf(mo))
}
