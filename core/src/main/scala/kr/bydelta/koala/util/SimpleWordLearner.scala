package kr.bydelta.koala.util

import java.util

import kr.bydelta.koala.traits.{CanCompileDict, CanLearnWord}

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * Simple implementation of word learner
  */
class SimpleWordLearner(override val targets: CanCompileDict*)
  extends CanLearnWord[Iterator[String], java.util.Iterator[String]] {
  override protected val converter: (util.Iterator[String]) => Iterator[String] =
    x => x.asScala

  override def extractNouns(corpora: Iterator[String],
                            minOccurrence: Int = 100, minVariations: Int = 3): Stream[String] = {
    corpora.toStream.foldLeft(mutable.HashMap[String, mutable.HashMap[String, Int]]()) {
      case (map, para) =>
        para.replaceAll("(?U)[^가-힣\\s]+", " ").split("(?U)\\s+").toSeq
          .foreach { word =>
            extractJosa(word) match {
              case Some((root, josa)) if root.nonEmpty =>
                val rootWord =
                  if (DEPS_CALL contains root.last) root.drop(1)
                  else root
                if (rootWord.length > 1) {
                  val josamap = map.getOrElseUpdate(rootWord, mutable.HashMap())
                  josamap(josa) = josamap.getOrElse(josa, 0) + 1
                }
              case _ =>
            }
          }
        map
    }.toStream.filter {
      case (word, josaMap) if josaMap.size >= minVariations && josaMap.values.sum >= minOccurrence =>
        !targets.head.contains(word)
      case _ => false
    }.map(_._1)
  }
}
