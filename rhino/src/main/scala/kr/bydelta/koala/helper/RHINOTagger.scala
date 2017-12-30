package kr.bydelta.koala.helper

import java.util.StringTokenizer

import _root_.rhino.lexicon.LexiconInterface
import kr.bydelta.koala._
import kr.bydelta.koala.data.{Morpheme, Word}

/**
  * A tagger of RHINO. (To exclude "JFrame" from the source)
  * The code is a scala-version modification of rhino.MainClass.
  * The author of RHINO has the original copyright.
  */
private[koala] object RHINOTagger {
  final val SPECIALS = "([■▶◀◆▲◇◈☎【】]+)".r
  final val TOKENIZE = " .?!。‘’“”`\'\"(){}[]─,ㆍ·:;/…_~∼∽□+-=±÷×*^><｜|％%&￦₩\\＄$¥￥£￡°㎞㎏@©ⓒ↑|#♥♡★☆♪♬■▶◀◆▲◇◈☎【】"
  final val RETAGGING_CONTAIN = "(는|은|을|세|야|자라고)".r
  final val RETAGGING_EQUAL = "^(는|은|을|세|야|자라고)$".r
  final val MORPH_MATCH = "([^\\s]+)/([A-Z]{2,3})".r

  def tag(input: String): Seq[Word] = {
    val arr = split(input)
    val inputArr = arr.filter(_._2).map(_._1).toArray
    val outputArr = Array.fill(inputArr.length)("")

    val interface = new LexiconInterface(
      DictionaryReader.complexStem_MethodDeleted,
      DictionaryReader.stem_MethodDeleted,
      DictionaryReader.afterNumber_MethodDeleted,
      DictionaryReader.ending_MethodDeleted,
      DictionaryReader.stem_List,
      DictionaryReader.ending_List,
      inputArr, outputArr,
      DictionaryReader.nonEndingList,
      DictionaryReader.aspgStem,
      DictionaryReader.aspgEnding)

    inputArr.indices.foreach {
      i =>
        if (interface.FindMethod(inputArr(i), DictionaryReader.combiMethods_List)) {
          outputArr(i) = interface.BindingWordInMethod(i, "combi", inputArr(i))
        } else if (interface.FindSentenceMark(inputArr(i))) {
          outputArr(i) = interface.GetSentenceMarkResult()
        } else if (interface.CheckNumber(i)) {
          try{
            outputArr(i) = interface.FindArabicNumberPlus(i,
              DictionaryReader.afterNumber_MethodDeleted, DictionaryReader.afterNumber_List)
          }catch{
            case _: Throwable  =>
              // RHINO Exception Handling 안되는 부분
              outputArr(i) = inputArr(i) + "/NF"
          }
        } else {
          val Array(head, eogan, eomi) = interface.FindMorph(i, inputArr(i))
          val full =
            if (head == null) "NA"
            else head.replaceAll("/NA \\+ /NA \\+ /NA$", "/NA")
              .replaceAll("/NNG \\+ (하|되)/(VV|VX|XSV)", "$1/VV")

          if (eogan != null && eomi != null) {
            outputArr(i) = full
            RETAGGING_CONTAIN.findFirstMatchIn(eomi) match {
              case Some(_) =>
                val eomiForm =
                  eomi.split(" \\+ ").map(w => w.substring(0, w.indexOf("/"))).mkString
                RETAGGING_EQUAL.findFirstMatchIn(eomiForm) match {
                  case Some(_) =>
                    val newEomi =
                      try {
                        if (interface.FindMethod(eomiForm, DictionaryReader.endingMethods_List)) {
                          interface.BindingWordInMethod(i, "ending", eomiForm, true).split("_", 3)(1)
                        } else eomi
                      } catch {
                        case _: Throwable => eomi
                      }

                    if (eogan != "") {
                      outputArr(i) = eogan + " + " + newEomi
                    } else {
                      outputArr(i) = newEomi
                    }
                  case None =>
                }
              case None =>
            }
          } else {
            outputArr(i) = full
          }
        }
    }

    /* 여기서부터는 RHINO에 없는 코드입니다. */
    arr.foldLeft((0, Seq.empty[Word])) {
      case ((idxOfInputArr, seq), (symbol, false)) =>
        (idxOfInputArr, Word(symbol, Seq(Morpheme(symbol, "", POS.SW))) +: seq)
      case ((idxOfInputArr, seq), (".", true)) if idxOfInputArr > 0 && inputArr(idxOfInputArr - 1).matches("^\\.+$") =>
        val surf = seq.head.surface + "."
        (idxOfInputArr + 1, Word(surf, Seq(Morpheme(surf, "SE", POS.SE))) +: seq.tail)
      case ((idxOfInputArr, seq), ("^", true)) if idxOfInputArr > 0 && inputArr(idxOfInputArr - 1) == "^" =>
        (idxOfInputArr + 1, Word("^^", Seq(Morpheme("^^", "IC", POS.IC))) +: seq.tail)
      case ((idxOfInputArr, seq), (word, true)) =>
        val morphs = translateOutputMorpheme(outputArr(idxOfInputArr))
        (idxOfInputArr + 1, Word(word.trim, morphs) +: seq)
    }._2.reverse.filter(_.surface.nonEmpty)
  }

  private def translateOutputMorpheme(morphStr: String) =
    MORPH_MATCH.findAllMatchIn(morphStr).map {
      case MORPH_MATCH(surf, raw) =>
        Morpheme(surface = surf.trim, rawTag = raw, tag = POS.withName(raw))
    }.toSeq

  private[koala] def split(input: String) = {
    val tok = new StringTokenizer(input.replaceAll("\\s+", " "), TOKENIZE, true)
    new Iterator[String] {
      override def hasNext: Boolean = tok.hasMoreTokens

      override def next(): String = tok.nextToken()
    }.filter(_.trim.nonEmpty).map {
      case SPECIALS(t) =>
        (t, false)
      case t =>
        (t, true)
    }.toSeq
  }
}
