package kr.kaist.ir.korean.parser;

import kr.kaist.ir.korean.data.TaggedSentence;

/**
 * 구문 분석 작업을 수행하기 위해 정의한 인터페이스
 * 
 * @author 김부근
 * @since 2014-08-05
 * @version 0.2.0
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
}
