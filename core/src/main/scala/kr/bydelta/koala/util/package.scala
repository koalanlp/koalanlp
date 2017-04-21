package kr.bydelta.koala

/**
  * Created by bydelta on 17. 4. 8.
  */
package object util {
  val HanFirstList = Array('ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')
  val HanSecondList = Array('ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ')
  val HanLastList = Array('\u0000', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')

  def reunionKorean(seq: Seq[Char], acc: Seq[Char] = Seq.empty): String =
    if (seq.isEmpty) new String(acc.reverse.toArray)
    else {
      val char = seq.head
      val newAcc =
        if (!char.isHangul) {
          // 한글이 아닐 때.
          char +: acc
        } else if (HanSecondList.contains(char)) {
          // 지금 문자가 중성일때. 앞문자는 비었거나, 완성문자이거나, 초성이거나, 불완전한 글자임.
          if (acc.isEmpty || !HanFirstList.contains(acc.head)) {
            // 앞에 아무것도 없거나, 초성만 있지 않을때.
            if (acc.isEmpty || !acc.head.isCompleteHangul || !acc.head.endsWithJongsung) {
              // 앞에 아무것도 없거나, 완성문자가 아니거나(초성문자 제외), 종성으로 끝나지 않을 때.
              char +: acc
            } else {
              // 종성으로 끝나는 완전한 문자.
              val chosung = HanFirstList.indexOf(HanLastList(acc.head.getJongsungCode))
              val newFrontChar = (acc.head - acc.head.getJongsungCode).toChar
              val newChar: Char = ('가' + chosung * 588 + HanSecondList.indexOf(char) * 28 + 0).toChar
              newChar +: newFrontChar +: acc.tail
            }
          } else {
            // 앞에 초성만 있었을때.
            val chosung = HanFirstList.indexOf(acc.head)
            val newChar: Char = ('가' + chosung * 588 + HanSecondList.indexOf(char) * 28 + 0).toChar
            newChar +: acc.tail
          }
        } else {
          if (acc.isEmpty || !acc.head.isCompleteHangul || acc.head.endsWithJongsung) {
            // 앞에 아무것도 없거나, 불완전하거나, 종성으로 이미 끝났을때.
            char +: acc
          } else {
            // 종성으로 끝나지않은, 완전한 문자.
            val newChar: Char = (acc.head + HanLastList.indexOf(char)).toChar
            newChar +: acc.tail
          }
        }
      reunionKorean(seq.tail, newAcc)
    }
}
