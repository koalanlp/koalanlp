package kr.kaist.ir.korean.tagger;

import java.util.LinkedList;

import kr.kaist.ir.korean.data.TaggedSentence;

/**
 * 품사 부착 작업을 수행하기 위한 기능을 정의한 인터페이스
 * 
 * @author 김부근
 * @version 0.1.0
 * @since 2014-07-31
 */
public interface Tagger {
	/**
	 * 주어진 문장에 품사를 부착한다.
	 * 
	 * @param text 품사를 부착할 문장
	 * @return 품사가 부착된 문장, 즉 Sentence 객체.
	 * @throws Exception 품사 부착 과정에서 문제가 발생할 경우 발생한다.
	 */
	public abstract TaggedSentence analyzeSentence(String text) throws Exception;

	/**
	 * 주어진 문단에 품사를 부착한다.
	 * 
	 * @param text 품사를 부착할 문단
	 * @return 품사가 부착된 문장의 목록.
	 * @throws Exception 품사 부착 과정에서 문제가 발생할 경우 발생한다.
	 */
	public abstract LinkedList<TaggedSentence> analyzeParagraph(String text)
			throws Exception;

}