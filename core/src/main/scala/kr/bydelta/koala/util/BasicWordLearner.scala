package kr.bydelta.koala.util

import kr.bydelta.koala.KoreanStringExtension
import kr.bydelta.koala.traits.{CanCompileDict, CanLearnWord, CanTag}

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * 품사분석기가 분석하지 못한 신조어, 전문용어 등을 확인하여 추가하는 작업을 돕는 Class.
  *
  * @param tagger  품사분석의 기준이 되는 Tagger
  * @param targets 신조어 등을 등록할 사용자사전들.
  */
class BasicWordLearner(protected val tagger: CanTag[_], override protected val targets: CanCompileDict*)
  extends CanLearnWord[Iterator[String], java.util.Iterator[String]] {
  override protected val converter: (java.util.Iterator[String]) => Iterator[String] =
    x => x.asScala

  override def extractNouns(corpora: Iterator[String],
                            minOccurrence: Int = 100, minVariations: Int = 3): Stream[String] = {
    corpora.toStream.foldLeft(mutable.HashMap[String, mutable.HashMap[String, Int]]()) {
      case (map, para) =>
        val tagged = tagger.tagParagraph(para).flatMap(_.words)
        val words = tagged.filter { w =>
          w.morphemes.forall(m => m.isNoun || m.isJosa)
        }.map(w => w.surface.replaceAll("[^가-힣]+", "") -> w.count(_.isNoun))

        para.filterNonHangul
          .filter(word => words.exists(w => word.endsWith(w._1) && (word.length > w._1.length || w._2 > 1)))
          .foreach { word =>
            extractJosa(word) match {
              case Some((root, josa)) if root.nonEmpty =>
                val rootWord =
                  if (DEPS_CALL contains root.last) root.dropRight(1)
                  else if (DEPS_CALL_LONG contains root.takeRight(2)) root.dropRight(2)
                  else root
                if (rootWord.length > 1) {
                  val josamap = map.getOrElseUpdate(rootWord, mutable.HashMap())
                  josamap(josa) = josamap.getOrElse(josa, 0) + 1
                }
              case _ =>
            }
          }
        map
    }.toStream.collect {
      case (word, josaMap) if josaMap.contains("") && josaMap.values.sum >= minOccurrence &&
        josaMap.size >= minVariations =>
        word
    }
  }
}
