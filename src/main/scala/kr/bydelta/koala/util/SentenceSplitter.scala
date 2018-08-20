package kr.bydelta.koala.util

import kr.bydelta.koala.POS
import kr.bydelta.koala.data.{Morpheme, Sentence, Word}

import scala.annotation.tailrec

object SentenceSplitter {
  private val quoteRegex = "\'\"＇＂"
  private val openParenRegex = "([{‘“（［｛<〔〈《「『【"
  private val closeParenRegex = ")]}’”）］｝>〕〉》」』】"

  /**
    * 분석결과를 토대로 문장을 분리함.
    *
    * @param para 분리할 문단.
    * @return 문장단위로 분리된 결과
    */
  def apply(para: Seq[Word]): Seq[Sentence] = split(para)

  @tailrec
  private def rollupParenStack(morphemes: Seq[Morpheme],
                               stack: List[Char],
                               isPreviousSL: Boolean = false,
                               hasEndOfSentence: Boolean = false): (List[Char], Boolean) =
    morphemes match {
      case Seq() =>
        (stack, if (stack.isEmpty) hasEndOfSentence else false)
      case Seq(Morpheme(morph, POS.SF), tail@_*) if morph != "." || !isPreviousSL =>
        // 영문 약어 뒤에 따라붙는 마침표가 아니면.
        rollupParenStack(tail, stack, hasEndOfSentence = true)
      case Seq(Morpheme(_, POS.SN), tail@_*) =>
        // 숫자가 나타나면 종결부호 플래그 무시.
        rollupParenStack(tail, stack)
      case Seq(Morpheme(x, tag), tail@_*) =>
        val newStack = x.foldLeft(stack) {
          case (list, ch) if openParenRegex.contains(ch) =>
            ch +: list
          case (list, ch) if closeParenRegex.contains(ch) =>
            list.headOption match {
              case Some(stackHead) =>
                if (closeParenRegex.indexOf(ch) == openParenRegex.indexOf(stackHead))
                  list.tail
                else list
              case _ => list
            }
          case (list, ch) if quoteRegex.contains(ch) =>
            list.headOption match {
              case Some(stackHead) if stackHead == ch => list.tail
              case _ => ch +: list
            }
          case (list, _) => list
        }

        rollupParenStack(tail, newStack,
          isPreviousSL = tag == POS.SL,
          hasEndOfSentence = hasEndOfSentence)
    }

  /**
    * 분석결과를 토대로 문장을 분리함.
    *
    * @param para       분리할 문단.
    * @param parenStack 현재까지 열려있는 묶음기호 Stack.
    * @param acc        현재까지 분리된 문장들.
    * @return 문장단위로 분리된 결과
    */
  @tailrec
  private def split(para: Seq[Word],
                    parenStack: List[Char] = List(),
                    acc: Seq[Seq[Word]] = Seq(Seq.empty)): Seq[Sentence] =
    if (para.isEmpty) acc.filter(_.nonEmpty).map(x => Sentence(x.reverse)).reverse
  else {
    val Seq(head, tail@_*) = para
    val Seq(accHead, accTail@_*) = acc
    val (newParenStack, isEnding) = rollupParenStack(head, parenStack)
    val newAccHead = head +: accHead

    if (isEnding) split(tail, newParenStack, Seq.empty +: newAccHead +: accTail)
    else split(tail, newParenStack, newAccHead +: accTail)
  }
}