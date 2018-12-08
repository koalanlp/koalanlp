package kr.bydelta.koala.test

import kr.bydelta.koala.DictSpek
import kr.bydelta.koala.TaggerSpek
import kr.bydelta.koala.arirang.Dictionary
import kr.bydelta.koala.arirang.Tagger
import org.apache.lucene.analysis.ko.morph.AnalysisOutput
import org.apache.lucene.analysis.ko.morph.WordSegmentAnalyzer
import org.spekframework.spek2.Spek

"""Q. 정수정(크리스탈)은 어땠나.
이시언: 수정이 별명이 '얼음공주'이고 외모에서 풍기는 느낌 때문에 쌀쌀맞을 거라 생각했다. 그런데 실제로 겪어보니 성격이 정말 좋고, 가리는 음식도 없고, 예의도 바른 친구였다. 어린 친구가 오빠들을 많이 챙겼다. 해외 스케줄을 다녀오면 꼭 기념품을 사 와 선물했다. 그런 마음 씀씀이가 얼마나 예쁜가. 얼음공주 같은 외모 뒤에 정말 좋은 성격을 지녔다. 그래서 제가 '작은 달심'이라 했다. (한)혜진이가 그렇다. 사람 잘 챙기고 엄청 '츤데레'다. 혜진이랑 수정이는 그런 느낌이 닮았다.

출처 : SBS 뉴스 

원본 링크 : https://news.sbs.co.kr/news/endPage.do?news_id=N1005045233&plink=NEW&cooper=SBSNEWSSECTION&plink=COPYPASTE&cooper=SBSNEWSEND"""

// TODO

class ArirangDictTest : Spek(DictSpek(Dictionary, getTagger = { Tagger() }))

class ArirangTaggerTest : Spek(TaggerSpek(getTagger = { Tagger() },
        tagParaByOrig = { emptyList() },
        tagSentByOrig = {
            val list = mutableListOf<MutableList<AnalysisOutput>>()
            WordSegmentAnalyzer().analyze(it, list, false)
            val original = list.map { s -> s.maxBy { w -> w.score } }

            val tag = original.joinToString("") { s -> s.toString() }
                    .replace("[NVZ\\s.,()]+".toRegex(), "")

            "" to tag
        },
        tagSentByKoala = { str, tagger ->
            val tagged = tagger.tagSentence(str)
            val tag = tagged.joinToString("") { w ->
                w.joinToString(",") { m -> m.surface + "(" + m.originalTag!!.last() + ")" }
            }.replace("[NVZ\\s.,()]+".toRegex(), "")
            val surface = tagged.surfaceString()

            surface to tag
        },
        isParagraphImplemented = false,
        isSentenceSplitterImplemented = true
))
