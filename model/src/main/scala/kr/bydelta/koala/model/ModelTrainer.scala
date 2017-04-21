package kr.bydelta.koala.model

import kr.bydelta.koala
import kr.bydelta.koala.KoreanCharacterExtension
import org.tensorflow.Graph

/**
  * Created by bydelta on 17. 3. 31.
  */
object ModelTrainer {
  def unpackHangul(str: String): Seq[Char] =
    str.flatMap {
      ch =>
        if (ch.isCompleteHangul) {
          val cho = ch.getChosungCode + 0x1100
          val jung = ch.getJungsungCode + 0x1161
          val jong = ch.getJongsungCode + 0x11A7
          Seq(cho.toChar,
            jung.toChar,
            jong.toChar)
        } else {
          Seq(ch)
        }
    }

  def packHangul(seq: Seq[Char]): String =
    koala.util.reunionKorean(seq.map {
      ch =>
        if (ch >= 0x1100 && ch <= 0x1112) koala.util.HanFirstList(ch - 0x1100)
        else if (ch >= 0x1161 && ch <= 0x1175) koala.util.HanSecondList(ch - 0x1161)
        else if (ch >= 0x11A7 && ch <= 0x11C2) koala.util.HanLastList(ch - 0x11A7)
        else ch
    })

  def main(args: Array[String]): Unit = {
    val g = new Graph()


  }
}
