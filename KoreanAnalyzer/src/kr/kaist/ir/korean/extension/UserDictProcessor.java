package kr.kaist.ir.korean.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import kaist.cilab.jhannanum.common.Eojeol;
import kaist.cilab.jhannanum.common.communication.SetOfSentences;
import kaist.cilab.jhannanum.plugin.supplement.MorphemeProcessor.MorphemeProcessor;
import kr.kaist.ir.korean.util.MorphemeOperator;
import kr.kaist.ir.korean.util.TagConverter;
import kr.kaist.ir.korean.util.TagConverter.TaggerType;

/**
 * 프로그램 실행 과정 중에 사용자 정의 사전을 추가할 수 있도록 하는 형태소 분석기(Plug-in) 어절의 시작부분부터 일치하는 지점까지만
 * 변환한다.
 * 
 * @author 김부근
 * @since 2014-08-08
 * @version 0.2.2.4
 */
public class UserDictProcessor implements MorphemeProcessor {

	/** 사용자 정의 사전 */
	private Map<String, String> dict = new HashMap<String, String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see kaist.cilab.jhannanum.plugin.Plugin#initialize(java.lang.String)
	 */
	@Override
	public void initialize(String arg0) throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kaist.cilab.jhannanum.plugin.Plugin#initialize(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void initialize(String arg0, String arg1) throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kaist.cilab.jhannanum.plugin.Plugin#shutdown()
	 */
	@Override
	public void shutdown() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kaist.cilab.jhannanum.plugin.supplement.MorphemeProcessor.MorphemeProcessor
	 * #doProcess(kaist.cilab.jhannanum.common.communication.SetOfSentences)
	 */
	@Override
	public SetOfSentences doProcess(SetOfSentences sentence) {
		if (dict.size() > 0) {
			ArrayList<Eojeol[]> eojeol = sentence.getEojeolSetArray();

			for (int i = sentence.length - 1; i >= 0; i--) {
				Eojeol[] eojeols = eojeol.get(i), newEojeols = new Eojeol[eojeols.length];

				// 모든 어절 분석 결과에 대해서 같은 작업을 반복한
				for (int k = 0; k < eojeols.length; k++) {
					Eojeol e = eojeols[k];

					String[] origMorp = e.getMorphemes();
					String[] origTags = e.getTags();

					LinkedList<String> morp = new LinkedList<String>();
					LinkedList<String> tags = new LinkedList<String>();

					String morpheme = "";
					boolean findMorp = false;

					// 어절의 시작부분부터 덧붙여 일치하는 어절이 나타날 경우 교체하고, 이후는 무시한다.
					for (int j = 0; j < origMorp.length; j++) {
						if (!findMorp) {
							String m = origMorp[j];
							String tag = TagConverter.toIntegratedPOSTag(
									origTags[j], TaggerType.HNN);

							morpheme = MorphemeOperator.concat(morpheme, m,
									tag, TaggerType.HNN);

							if (dict.containsValue(morpheme)) {
								morp.add(morpheme);
								tags.add(TagConverter.toOriginalTag(
										dict.get(morpheme), TaggerType.HNN));

								findMorp = true;
							}
						} else {
							morp.add(origMorp[j]);
							tags.add(origTags[j]);
						}
					}

					if (findMorp) {
						newEojeols[k] = new Eojeol(morp.toArray(new String[0]),
								tags.toArray(new String[0]));
					}else{
						newEojeols[k] = e;
					}
				}

				eojeol.set(i, newEojeols);
			}
		}

		return sentence;
	}

	/**
	 * 처리 과정에서 사용할 사용자 사전을 설정한다.
	 * 
	 * @param dict
	 *            사용자 사전. "어절"을 키 값으로, "표기"를 값으로 한다.
	 */
	public void setDict(Map<String, String> dict) {
		if (dict != null) {
			this.dict.putAll(dict);
		}
	}

	/**
	 * 처리 과정에서 사용할 사용자 사전에 추가한다.
	 * 
	 * @param morph
	 *            추가할 형태소
	 * @param tag
	 *            형태소의 품사 구분
	 */
	public void addDict(String morph, String tag) {
		this.dict.put(morph, tag);
	}
}
