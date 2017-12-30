package kr.bydelta.koala.wiki

import java.io.{BufferedWriter, FileOutputStream, OutputStreamWriter}
import java.net.URL
import java.util.GregorianCalendar

import kr.bydelta.koala
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.traits.CanTag
import org.jsoup.Jsoup

import scala.collection.JavaConverters._
import scala.util.{Random, Success, Try}

/**
  * Created by bydelta on 17. 9. 22.
  */
object ComparisonGenerator {
  final val example =
    """북한이 도발을 멈추지 않으면 미국이 북핵 시설을 타격해도 군사개입을 하지 않겠다. 중국 관영 환구시보가 밝힌 내용인데요. 중국이 여태껏 제시한 북한에 대한 압박 수단 가운데 가장 이례적이고, 수위가 높은 것으로 보입니다.
      |이한주 기자입니다.
      |중국 관영매체 환구시보가 북핵문제에 대해 제시한 중국의 마지노선입니다. 북핵 억제를 위해 외교적 노력이 우선해야 하지만 북한이 도발을 지속하면 핵시설 타격은 용인할 수 있다는 뜻을 내비친 겁니다.
      |그러나 한국과 미국이 38선을 넘어 북한 정권 전복에 나서면 중국이 즉각 군사개입에 나서야 한다는 점을 분명히 하였습니다. 북한에 대한 압박수위도 한층 높였습니다.
      |핵실험을 강행하면 트럼프 대통령이 북한의 생명줄로 지칭한 중국의 원유공급을 대폭 축소할 거라고 경고하였습니다. 축소 규모에 대해서도 '인도주의적 재앙이 일어나지 않는 수준'이라는 기준까지 제시하며 안보리 결정을 따르겠다고 못 박았습니다.
      |중국 관영매체가 그동안 북한에 자제를 요구한 적은 있지만, 군사지원 의무제공 포기 가능성과 함께 유엔 안보리 제재안을 먼저 제시한 것은 이례적입니다. 미·중 빅딜에 따른 대북압박 공조 가능성이 제기되는 가운데 북한이 어떤 반응을 보일지 관심이 쏠립니다.""".stripMargin
      .split("\n").toSeq

  def main(args: Array[String]): Unit = {
    // Fetch random article
    implicit val testset: Seq[String] = Random.shuffle(
      Try(Jsoup.parse(new URL("http://media.daum.net/politics/"), 10000)) match {
        case Success(doc) =>
          val articles = doc.select("a.link_txt").iterator()
            .asScala.filter(_.attr("href").startsWith("http://v.media.daum.net/v/"))
            .map(_.attr("href")).toSeq
          val seq = Random.shuffle(articles).take(3).flatMap {
            href =>
              Try(Jsoup.parse(new URL(href), 10000)) match {
                case Success(news) =>
                  val paragraphs = news.select(".article_view section p")
                  val seq = paragraphs.iterator.asScala.map(_.text.trim).filter(_.length > 100).toSeq

                  if (seq.nonEmpty) seq
                  else Seq.empty
                case _ =>
                  Seq.empty
              }
          }

          if (seq.nonEmpty) seq
          else Seq.empty
        case _ =>
          example
      }
    ).take(10)

    val (kkmaLoading, kkmaSeq) = results[koala.kkma.Tagger]
    val (eunjeonLoading, eunjeonSeq) = results[koala.eunjeon.Tagger]
    val (kmrLoading, kmrSeq) = results[koala.kmr.Tagger]
    val (rhinoLoading, rhinoSeq) = results[koala.rhino.Tagger]
    val (arirangLoading, arirangSeq) = results[koala.arirang.Tagger]
    val (hnnLoading, hnnSeq) = results[koala.hnn.Tagger]
    val (twtLoading, twtSeq) = results[koala.twt.Tagger]

    val loadings = Seq(kkmaLoading, twtLoading, eunjeonLoading,
      kmrLoading, rhinoLoading, arirangLoading, hnnLoading)
    val resultSeq = Seq(kkmaSeq, twtSeq, eunjeonSeq, kmrSeq, rhinoSeq, arirangSeq, hnnSeq)
    val names = Seq("KKMA", "Twitter", "Eunjeon", "KOMORAN", "Rhino", "Arirang", "Hannanum")
    val meanProcessing = resultSeq.map {
      s => s.tail.map(_._1).sum / s.length
    }

    val (timeSequence, taggedParagraphs) = kkmaSeq.indices.map {
      i =>
        resultSeq.map(_ (i)).unzip
    }.unzip

    val lazyLoading = loadings.zip(timeSequence.head).map {
      case (x, y) => x + y
    }

    val procs = Runtime.getRuntime.availableProcessors()

    implicit val bw: BufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args.head)))
    text("실제 사용 성능을 보여드리기 위해서, 카카오 뉴스에서 임의로 뉴스를 선택하고 10개 문단을 표본추출하였습니다.")
    text(s"실험환경: (Travis CI) **3GB** MaxHeap, **$procs**-core(s), Scala **${args(1)}**")
    text(s"실험일시: ${new GregorianCalendar().getTime}")
    heading("시간 성능 개관")
    table(
      "." +: names,
      ("Initializing" +: loadings.map(_ + "s")) +:
        timeSequence.zipWithIndex.map {
          case (seq, 0) => s"Sentence #0 + __Lazy Loading__" +: seq.map(_ + "s")
          case (seq, i) => s"Sentence #$i" +: seq.map(_ + "s")
        }
    )
    text(s"가장 __빠르게 초기화된__ 것은 `${names(loadings.zipWithIndex.minBy(_._1)._2)}`이며, " +
      s"가장 느리게 초기화된 패키지는 `${names(loadings.zipWithIndex.maxBy(_._1)._2)}`입니다.")
    text(s"가장 __빠르게 사전을 불러온__ 것은 `${names(lazyLoading.zipWithIndex.minBy(_._1)._2)}`이며, " +
      s"가장 느리게 사전을 불러온 패키지는 `${names(lazyLoading.zipWithIndex.maxBy(_._1)._2)}`입니다.")
    text(s"첫 문장을 빼면, 평균적으로 __가장 빠르게 분석한__ 것은 `${names(meanProcessing.zipWithIndex.minBy(_._1)._2)}`이며, " +
      s"가장 느리게 분석한 패키지는 `${names(meanProcessing.zipWithIndex.maxBy(_._1)._2)}`입니다.")

    heading("Tagging 결과")
    text("이제부터, 각 문장별로 품사분석 결과를 보여드립니다. " +
      "정답이 없으므로, 바르게 분석했는지의 여부는 여러분께서 판단하셔야 합니다. " +
      "다음과 같은 기준으로 평가해보시는 것을 권장합니다.")
    list(
      "띄어쓰기나 어절구분은 정확한가? (바르게 분석했다면, 읽는 데 __어색함이 없어야__ 합니다)",
      "고유명사나 신조어를 어떻게 분석했는가? (바르게 분석했다면, __고유명사/NNP__ 품사가 붙고, 적절히 띄어쓰기 되어야 합니다)",
      "체언(명사)이 바르게 분석되었는가? (바르게 분석했다면, __N으로 시작하는 품사__ 가 붙어야 합니다)",
      "용언(동사, 형용사)가 바르게 분석되었는가? (바르게 분석했다면, __V으로 시작하는 품사__ 가 붙고, 이후에, 어미(__E로 시작하는 품사__) 가 붙어야 합니다)" +
        "   * 명사로도 쓰이는 동사는 N(명사)+XSV(용언화 접미사) 형태를 띄기도 합니다."
    )

    testset.zipWithIndex.zip(taggedParagraphs).foreach {
      case ((original, i), seq) =>
        heading(s"문장 번호 #$i", depth = 3)
        text("원본 문장:")
        quote(original)

        heading("어절 구분", depth = 4)
        names.zip(seq).foreach {
          case (pack, res) =>
            heading(pack, depth = 5)
            quote(res.map(_.surfaceString("⎵")): _*)
        }

        heading("품사 분석", depth = 4)
        names.zip(seq).foreach {
          case (pack, res) =>
            heading(pack, depth = 5)
            codeQuote(res.map(_.singleLineString))
        }
    }

    bw.close()
  }

  def results[T <: CanTag](implicit m: scala.reflect.Manifest[T], samples: Seq[String]): (Double, Seq[(Double, Seq[Sentence])]) =
    this.synchronized {
      val (loading, tagger) = loadingSandbox[T]
      val resultSeq = samples.map(taggingSandbox(tagger, _))
      (loading, resultSeq)
    }

  def loadingSandbox[T <: CanTag](implicit m: scala.reflect.Manifest[T]): (Double, CanTag) = {
    val start = System.currentTimeMillis()
    val tagger = m.runtimeClass.newInstance.asInstanceOf[T]
    val end = System.currentTimeMillis()
    val delta = (end - start) / 1000.0

    println(s"\t\tLoading... $delta s [${m.runtimeClass.getCanonicalName}]")
    (delta, tagger)
  }

  def taggingSandbox(tagger: CanTag, text: String): (Double, Seq[Sentence]) = {
    val start = System.currentTimeMillis()
    val para = tagger.tag(text)
    val end = System.currentTimeMillis()
    val delta = (end - start) / 1000.0

    println(s"\t\tTagging... $delta s [${tagger.getClass.getCanonicalName}]")
    (delta, para)
  }

  def heading(text: String, depth: Int = 2)(implicit bw: BufferedWriter): Unit = {
    bw.write("#" * depth + s" $text")
    bw.newLine()
  }

  def quote(text: String*)(implicit bw: BufferedWriter): Unit = {
    bw.write(text.map("> " + _).mkString("\n>\n"))
    bw.newLine()
  }

  def codeQuote(text: Seq[String])(implicit bw: BufferedWriter): Unit = {
    bw.write("```text\n" + text.mkString("\n") + "\n```\n")
    bw.newLine()
  }

  def table(headers: Seq[String], values: Seq[Seq[String]])(implicit bw: BufferedWriter): Unit = {
    bw.write(headers.mkString(" | ") + "\n" +
      headers.map(_ => "---").mkString(" | ") + "\n" +
      values.map(_.mkString(" | ")).mkString("\n"))
    bw.newLine()
  }

  def text(text: String)(implicit bw: BufferedWriter): Unit = {
    bw.newLine()
    bw.write(text)
    bw.newLine()
  }

  def list(text: String*)(implicit bw: BufferedWriter): Unit = {
    bw.newLine()
    bw.write(text.map("* " + _).mkString("\n"))
    bw.newLine()
  }
}
