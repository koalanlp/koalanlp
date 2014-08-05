package kr.kaist.ir.korean.tagger;

import java.util.LinkedList;
import java.util.List;

import kr.kaist.ir.korean.data.TaggedMorpheme;
import kr.kaist.ir.korean.data.TaggedSentence;
import kr.kaist.ir.korean.data.TaggedWord;
import kr.kaist.ir.korean.util.TagConverter.TaggerType;

import org.snu.ids.ha.ma.Eojeol;
import org.snu.ids.ha.ma.MExpression;
import org.snu.ids.ha.ma.MorphemeAnalyzer;

/**
 * 꼬꼬마 형태소 분석기를 사용한 품사부착 클래스
 * 
 * @author 김부근
 * @since 2014-07-31
 * @version 0.1.0
 */
public final class KkokkomaTagger implements Tagger {

	/**
	 * 꼬꼬마 형태소 분석기
	 */
	private MorphemeAnalyzer ma;

	/**
	 * 생성자. 꼬꼬마 형태소 분석기를 새로 시동한다.
	 */
	public KkokkomaTagger() {
		ma = new MorphemeAnalyzer();
		ma.createLogger(null);
	}

	/**
	 * 꼬꼬마 형태소 분석기의 문장 분석 결과를 통합 형태로 변환한다.
	 * 
	 * @param result
	 *            변환할 꼬꼬마 형태소 분석기 결과
	 * @return 변환된 품사 부착 결과
	 */
	public TaggedSentence parseResult(org.snu.ids.ha.ma.Sentence result) {
		// 새로운 문장
		TaggedSentence sentence = new TaggedSentence();

		// 각 어절마다 변환작업 수행
		for (int i = 0; i < result.size(); i++) {
			Eojeol eojeol = result.get(i);

			// 새 어절
			TaggedWord word = new TaggedWord(eojeol.getExp());

			// 어절에 포함된 형태소를 변환
			for (int j = 0; j < eojeol.size(); j++) {
				org.snu.ids.ha.ma.Morpheme morp = eojeol.get(j);
				word.addMorpheme(new TaggedMorpheme(morp.getString(), morp
						.getTag(), TaggerType.KKMA));
			}

			// 문장에 단어를 저장함.
			sentence.addWord(word);
		}

		return sentence;
	}

	/**
	 * 사후 정리
	 * 
	 * @throws Throwable
	 *             문제 발생시
	 */
	@Override
	protected void finalize() throws Throwable {
		ma.closeLogger();
		super.finalize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.kaist.ir.korean.tagger.Tagger#analyzeSentence(java.lang.String)
	 */
	@Override
	public TaggedSentence analyzeSentence(String text) throws Exception {
		return parseResult(analyzeSentenceRaw(text));
	}

	/**
	 * 꼬꼬마 분석기의 결과를 그대로 돌려줍니다.
	 * 
	 * @param text
	 *            형태소 분석을 진행할 문자열
	 * @return 꼬꼬마 분석기의 형태소 분석 결과 (org.snu.ids.ha.ma.Sentence)
	 * @throws Exception
	 *             분석 과정에서 오류가 발생할 경우 발생합니다.
	 */
	public org.snu.ids.ha.ma.Sentence analyzeSentenceRaw(String text)
			throws Exception {
		// 문장을 분석한다.
		List<MExpression> ret = ma.analyze(text);
		// 띄어쓰기를 정돈한다.
		ret = ma.postProcess(ret);
		// 가장 적합한 품사 부착 결과만을 남겨둔다.
		ret = ma.leaveJustBest(ret);
		// 문장단위로 분할한다.
		List<org.snu.ids.ha.ma.Sentence> stl = ma.divideToSentences(ret);

		// 1개 문장이었으므로, 첫번째 문장 결과만을 취한다.
		return stl.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.kaist.ir.korean.tagger.Tagger#analyzeParagraph(java.lang.String)
	 */
	@Override
	public LinkedList<TaggedSentence> analyzeParagraph(String text)
			throws Exception {
		// 문장을 분석한다.
		List<MExpression> ret = ma.analyze(text);
		// 띄어쓰기를 정돈한다.
		ret = ma.postProcess(ret);
		// 가장 적합한 품사 부착 결과만을 남겨둔다.
		ret = ma.leaveJustBest(ret);

		// 문장 단위로 분할하여 각 문장을 통합형태로 변환한다.
		List<org.snu.ids.ha.ma.Sentence> results = ma.divideToSentences(ret);
		LinkedList<TaggedSentence> paragraph = new LinkedList<TaggedSentence>();

		for (org.snu.ids.ha.ma.Sentence result : results) {
			paragraph.add(parseResult(result));
		}

		return paragraph;
	}
}
