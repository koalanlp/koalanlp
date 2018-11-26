@file:JvmName("Util")
@file:JvmMultifileClass

package kr.bydelta.koala.arirang

import kr.bydelta.koala.POS
import kr.bydelta.koala.data.Morpheme
import kr.bydelta.koala.data.Sentence
import kr.bydelta.koala.data.Word
import kr.bydelta.koala.proc.CanTagOnlyASentence
import org.apache.lucene.analysis.ko.morph.AnalysisOutput
import org.apache.lucene.analysis.ko.morph.PatternConstants
import org.apache.lucene.analysis.ko.morph.WordSegmentAnalyzer


/**
 * 아리랑 형태소 분석기입니다.
 *
 * @since 1.x
 * @see [CanTagOnlyASentence]
 */
class Tagger : CanTagOnlyASentence<List<AnalysisOutput>>() {

    /** 아리랑 형태소 분석기 원본 */
    val tagger = WordSegmentAnalyzer()

    /**
     * List<AnalysisOutput> 타입의 분석결과 [result]를 변환, [Sentence]를 구성합니다.
     *
     * @since 1.x
     * @param text 품사분석을 수행한 문단의 String입니다.
     * @param result 변환할 분석결과.
     * @return 변환된 [Sentence] 객체
     */
    override fun convertSentence(text: String, result: List<AnalysisOutput>): Sentence {
        var pos = 0
        val words = mutableListOf<Word>()
        val candidates = result.filter { it.source.trim().isNotEmpty() }

        for (word in candidates) {
            var surfaceCandidate = word.source.trim()
            val morphCandidates = mutableListOf<Morpheme>()

            val newPos = minOf(maxOf(pos, text.indexOf(surfaceCandidate, startIndex = pos)), text.length)
            val token = text.substring(pos, newPos).trim()

            // 혹시 이 문자열 앞에 다른 문자가 있었다면, 누락된 것이므로 복원함
            if (token.isNotEmpty()) {
                morphCandidates.add(0, Morpheme(token, POS.NA, " "))
                surfaceCandidate = token + surfaceCandidate //Note: Order changed!
            }
            morphCandidates.addAll(interpretOutput(word).map {
                Morpheme(it.first.trim(), it.second, it.third)
            })
            pos = newPos + surfaceCandidate.length

            // Find Special characters and separate them as morphemes
            var morphemes = morphCandidates.flatMap { m ->
                val s = m.surface
                if (SPRegex.containsMatchIn(s) || SFRegex.containsMatchIn(s) || SSRegex.containsMatchIn(s)) {
                    s.split(punctuationsSplit).map {
                        when {
                            it.matches(SPRegex) -> Morpheme(it, POS.SP, m.originalTag)
                            it.matches(SFRegex) -> Morpheme(it, POS.SF, m.originalTag)
                            it.matches(SSRegex) -> Morpheme(it, POS.SS, m.originalTag)
                            it.isBlank() -> Morpheme(it, POS.TEMP, m.originalTag)
                            else -> Morpheme(it.trim(), m.tag, m.originalTag)
                        }
                    }
                } else {
                    listOf(m)
                }
            }

            // Now separate by special characters or whitespace characters
            while (morphemes.any { it.tag in checkSet }) {
                val mIndex = morphemes.indexOfFirst { it.tag in checkSet }
                val morph = morphemes.take(mIndex)
                morphemes = morphemes.drop(mIndex)

                val symbol = morphemes.getOrNull(0)?.surface // 다음 morpheme의 표면형
                val sIndex =
                        if (symbol != null) maxOf(0, surfaceCandidate.indexOf(symbol))
                        else surfaceCandidate.length
                val surface = surfaceCandidate.take(sIndex).trim()

                // 현재 morpheme이 비어있는 단어가 아니라면
                if (surface.isNotEmpty()) {
                    words.add(Word(surface = surface, morphemes = morph))
                }

                if (symbol != null && symbol.trim().isNotEmpty()) {
                    words.add(Word(surface = symbol.trim(), morphemes = listOf(morphemes[0])))
                }

                morphemes = morphemes.drop(1)
                surfaceCandidate = surfaceCandidate.drop(sIndex + (symbol?.length ?: 0))
            }

            if (surfaceCandidate.trim().isNotEmpty()) {
                words.add(Word(surface = surfaceCandidate.trim(), morphemes = morphemes))
            }
        }

        val restOfText = text.drop(pos).trim()
        if (restOfText.isNotEmpty()) {
            words.add(Word(restOfText, morphemes = listOf(Morpheme(restOfText, POS.NA, " "))))
        }

        return Sentence(words.toList())
    }

    /**
     * 변환되지않은, [text]의 분석결과 List<AnalysisOutput>를 반환합니다.
     *
     * @since 1.x
     * @param text 분석할 String.
     * @return 원본 분석기의 결과인 문장 1개
     */
    override fun tagSentenceOriginal(text: String): List<AnalysisOutput> =
            if (text.trim().isEmpty()) emptyList()
            else {
                val list = mutableListOf<MutableList<AnalysisOutput>>()
                tagger.analyze(text.trim(), list, false)
                list.mapNotNull { out -> out.maxBy { it.score } }
            }

    /** 아리랑 분석기 분석 결과를 해석함
     *
     * **참고** 아리랑 분석기는 분석 결과를 형태소 형태로 들고 있지 않지만,
     * 내부에서는 형태소 분석을 수행하므로, 이를 복원하는 게 필요함.
     * */
    private fun interpretOutput(o: AnalysisOutput): List<Triple<String, POS, String>> {
        val morphs = mutableListOf<Triple<String, POS, String>>()

        morphs.add(Triple<String, POS, String>(o.stem, when (o.pos) {
            PatternConstants.POS_NPXM -> POS.NNG
            PatternConstants.POS_VJXV -> POS.VV
            PatternConstants.POS_AID -> POS.MAG
            else -> POS.SW
        }, o.pos.toString()))

        if (o.nsfx != null) {
            //NounSuffix
            morphs.add(Triple<String, POS, String>(o.nsfx, POS.XSN, "_s"))
        }

        when (o.patn) {
            2, 22 -> {
                morphs.add(Triple<String, POS, String>(o.josa, POS.JX, "_j"))
            }

            3 -> {
                //* 체언 + 용언화접미사 + 어미 */
                morphs.add(Triple<String, POS, String>(o.vsfx, POS.XSV, "_t"))
                if (o.pomi != null) {
                    morphs.add(Triple<String, POS, String>(o.pomi, POS.EP, "_f"))
                }

                morphs.add(Triple<String, POS, String>(o.eomi, POS.EF, "_e"))
            }

            4 -> {
                //* 체언 + 용언화접미사 + '음/기' + 조사 */
                morphs.add(Triple<String, POS, String>(o.vsfx, POS.XSV, "_t"))
                if (o.pomi != null) {
                    morphs.add(Triple<String, POS, String>(o.pomi, POS.EP, "_f"))
                }

                morphs.add(Triple<String, POS, String>(o.elist[0], POS.ETN, "_n"))
                morphs.add(Triple<String, POS, String>(o.josa, POS.JX, "_j"))
            }

            5 -> {
                //* 체언 + 용언화접미사 + '아/어' + 보조용언 + 어미 */
                morphs.add(Triple<String, POS, String>(o.vsfx, POS.XSV, "_t"))
                morphs.add(Triple<String, POS, String>(o.elist[0], POS.EC, "_c"))
                morphs.add(Triple<String, POS, String>(o.xverb, POS.VX, "_W"))
                if (o.pomi != null) {
                    morphs.add(Triple<String, POS, String>(o.pomi, POS.EP, "_f"))
                }

                morphs.add(Triple<String, POS, String>(o.eomi, POS.EF, "_e"))
            }

            6 -> {
                //* 체언 + '에서/부터/에서부터' + '이' + 어미 */
                morphs.add(Triple<String, POS, String>(o.josa, POS.JKB, "_j"))
                morphs.add(Triple<String, POS, String>(o.elist[0], POS.VCP, "_t"))
                if (o.pomi != null) {
                    morphs.add(Triple<String, POS, String>(o.pomi, POS.EP, "_f"))
                }

                morphs.add(Triple<String, POS, String>(o.eomi, POS.EF, "_e"))
            }

            7 -> {
                //* 체언 + 용언화접미사 + '아/어' + 보조용언 + '음/기' + 조사 */
                morphs.add(Triple<String, POS, String>(o.vsfx, POS.XSV, "_t"))
                morphs.add(Triple<String, POS, String>(o.elist[0], POS.EC, "_c"))
                morphs.add(Triple<String, POS, String>(o.xverb, POS.VX, "_W"))
                if (o.pomi != null) {
                    morphs.add(Triple<String, POS, String>(o.pomi, POS.EP, "_f"))
                }

                morphs.add(Triple<String, POS, String>(o.elist[0], POS.ETN, "_n"))
                morphs.add(Triple<String, POS, String>(o.josa, POS.JX, "_j"))
            }

            11 -> {
                //* 용언 + 어미 */
                if (o.pomi != null) {
                    morphs.add(Triple<String, POS, String>(o.pomi, POS.EP, "_f"))
                }

                morphs.add(Triple<String, POS, String>(o.eomi, POS.EF, "_e"))
            }

            12 -> {
                //* 용언 + '음/기' + 조사 */
                if (o.pomi != null) {
                    morphs.add(Triple<String, POS, String>(o.pomi, POS.EP, "_f"))
                }

                morphs.add(Triple<String, POS, String>(o.elist[0], POS.ETN, "_n"))
                morphs.add(Triple<String, POS, String>(o.josa, POS.JX, "_j"))
            }

            13 -> {
                //* 용언 + '음/기' + '이' + 어미 */
                morphs.add(Triple<String, POS, String>(o.elist[0], POS.ETN, "_n"))
                morphs.add(Triple<String, POS, String>(o.elist[1], POS.VCP, "_s"))
                if (o.pomi != null) {
                    morphs.add(Triple<String, POS, String>(o.pomi, POS.EP, "_f"))
                }

                morphs.add(Triple<String, POS, String>(o.eomi, POS.EF, "_e"))
            }

            14 -> {
                //* 용언 + '아/어' + 보조용언 + 어미 */
                morphs.add(Triple<String, POS, String>(o.elist[0], POS.EC, "_c"))
                morphs.add(Triple<String, POS, String>(o.xverb, POS.VX, "_W"))
                if (o.pomi != null) {
                    morphs.add(Triple<String, POS, String>(o.pomi, POS.EP, "_f"))
                }

                morphs.add(Triple<String, POS, String>(o.eomi, POS.EF, "_e"))
            }

            15 -> {
                //* 용언 + '아/어' + 보조용언 + '음/기' + 조사 */
                morphs.add(Triple<String, POS, String>(o.elist[1], POS.EC, "_c"))
                morphs.add(Triple<String, POS, String>(o.xverb, POS.VX, "_W"))
                if (o.pomi != null) {
                    morphs.add(Triple<String, POS, String>(o.pomi, POS.EP, "_f"))
                }

                morphs.add(Triple<String, POS, String>(o.elist[0], POS.ETN, "_n"))
                morphs.add(Triple<String, POS, String>(o.josa, POS.JX, "_j"))
            }

            else -> {
            }
        }

        return morphs.toList()
    }

    /** Static fields */
    companion object {
        /** 분리가 필요한 특수문자 */
        @JvmStatic
        private val checkSet = listOf(POS.SF, POS.SP, POS.SS, POS.TEMP)
        /** 종결부호 */
        @JvmStatic
        private val SFRegex = "(?U)[.?!]+".toRegex()
        /** 문장부호 */
        @JvmStatic
        private val SPRegex = "(?U)[,:;·/]+".toRegex()
        /** 종결부호, 문장부호, 괄호 등으로 segmentize하는 Regex */
        @JvmStatic
        private val punctuationsSplit =
                "(?U)((?<=[,.:;?!/·\\s\'\"(\\[{<〔〈《「『【‘“)\\]}>〕〉》」』】’”])|(?=[,.:;?!/·\\s\'\"(\\[{<〔〈《「『【‘“)\\]}>〕〉》」』】’”]+))".toRegex()
        /** 괄호 */
        @JvmStatic
        private val SSRegex = "(?U)[\'\"(\\[{<〔〈《「『【‘“)\\]}>〕〉》」』】’”]+".toRegex()
    }
}