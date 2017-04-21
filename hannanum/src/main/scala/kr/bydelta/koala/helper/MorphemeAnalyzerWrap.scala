package kr.bydelta.koala.helper

import java.util.StringTokenizer

import kaist.cilab.jhannanum.common.communication.Sentence
import kaist.cilab.parser.berkeleyadaptation.Eojeol

import scala.collection.mutable.ArrayBuffer

/**
  * 한나눔 HanNanumMorphAnalWrapper를 개량한 클래스.
  * - Scala에 맞게 수정.
  * - Parsing 과정에서, Tagging을 다시 실시하지 않도록 수정.
  *
  * 원본의 Copyright: KAIST 한나눔 개발팀.
  */
private[koala] object MorphemeAnalyzerWrap {
  @throws[Exception]
  def getSpacedresult(in: Sentence): Seq[String] =
    this.getAnalysisResult(in).flatMap(_.tokenList.map(_.word))

  @throws[Exception]
  def getAnalysisResult(raw: Sentence): Seq[Eojeol] = {
    val txt = raw.getPlainEojeols
    val result = this.modifySenLevelPOSResult(raw.toString)

    val tok = new StringTokenizer(result, "\n\r")
    val ret = ArrayBuffer[Eojeol]()

    txt.foldLeft(0) {
      case (i, word) =>
        val e = new Eojeol
        e.offset = i
        e.origEojeol = tok.nextToken

        val analResult =
          if (e.origEojeol.endsWith("에서")) {
            tok.nextToken
            e.origEojeol.substring(0, e.origEojeol.length - 2) + "/ncn+에서/jco"
          } else
            tok.nextToken.trim

        val eachAnal = new StringTokenizer(analResult, "+")
        var tokTmp = ArrayBuffer[Eojeol#Token]()
        while (eachAnal.hasMoreElements) {
          var tokIdx = eachAnal.nextToken
          while (tokIdx.indexOf('/') == -1) {
            tokIdx += eachAnal.nextToken
          }

          val t = new e.Token()
          tokIdx.splitAt(tokIdx.lastIndexOf('/')) match {
            case (h@"부터", _) =>
              t.word = h
              t.pos = "jca"
            case (head, tail) =>
              t.word = head
              t.pos = tail.substring(1)
          }

          tokTmp += t
        }

        e.tokenList = tokTmp.toArray

        this.integrateSamePOS(e, "n")
        this.integrateSamePOS(e, "j")
        e.eojeolIdx = ret.length
        ret += e

        i + word.length + 1
    }
    ret
  }

  private def integrateSamePOS(e: Eojeol, pos: String) {
    e.tokenList =
      e.tokenList.zipWithIndex.foldLeft((false, -1, ArrayBuffer[Boolean]())) {
        case ((prevOK, mergeIdx, merged), (tok, i)) =>
          if (tok.pos.startsWith(pos)) {
            if (prevOK) {
              e.tokenList(mergeIdx).word += tok.word
              merged += true
              (true, mergeIdx, merged)
            } else {
              merged += false
              (true, i, merged)
            }
          } else {
            merged += false
            (false, mergeIdx, merged)
          }
      }._3.zip(e.tokenList).collect {
        case (false, tok) => tok
      }.toArray
  }

  private def modifySenLevelPOSResult(result: String): String =
    result.replaceAll("가/pvg\\+아/ecx\\+지/px", "가지\\/pvg")
      .replaceAll("입/pvg\\+니다/ef", "이/jp\\+ㅂ니다/ef")
      .replaceAll("일/pvg\\+ㅂ니다/ef", "이/jp\\+ㅂ니다/ef")
      .replaceAll("에서/jca\\+는/jxc", "에서는/jca")
}
