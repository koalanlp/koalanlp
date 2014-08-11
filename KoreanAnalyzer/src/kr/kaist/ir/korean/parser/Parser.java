package kr.kaist.ir.korean.parser;

import java.util.Map;

import kr.kaist.ir.korean.data.TaggedSentence;

/**
 * 구문 분석 작업을 수행하기 위해 정의한 인터페이스
 * 
 * @author 김부근
 * @since 2014-08-05
 * @version 0.2.2.4
 */
public interface Parser {
	/**
	 * 주어진 문장의 의존 구문 분석을 수행하여 그 결과를 돌려준다.
	 * 
	 * @param sentence
	 *            의존 분석을 수행할 문장
	 * @return 의존 분석 수행의 결과로 생성되는 구조를 포함한 문장(TaggedSentence).
	 * @throws Exception
	 *             구문 분석이 실패할 경우 발생한다.
	 */
	public TaggedSentence dependencyOf(String sentence) throws Exception;

	/**
	 * 사용자 정의 형태소 사전을 추가한다.
	 * 
	 * @param dict
	 *            사용자 정의 사전. "어절"이 키, "품사"가 값.
	 */
	public void addUserDictionary(Map<String, String> dict);

	/**
	 * 사용자 정의 형태소를 등록한다.
	 * 
	 * @param morph
	 *            등록할 형태소
	 * @param tag
	 *            형태소의 품사 구분 (통합형)
	 */
	public abstract void addUserDictionary(String morph, String tag);
}
