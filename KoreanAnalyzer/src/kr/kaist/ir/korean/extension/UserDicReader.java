package kr.kaist.ir.korean.extension;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.snu.ids.ha.dic.SimpleDicReader;

/**
 * 꼬꼬마 형태소 분석기에서 사용자 정의 사전을 실시간으로 불러오기 위한 Reader
 * 
 * @author 김부근
 *
 */
public class UserDicReader implements SimpleDicReader {
	/** 입력할 형태소 목록 */
	private List<String> morphemes = new LinkedList<String>();
	/** 현재까지 입력된 위치. 반복적으로 불러올 때, 이전 형태소가 사라지면 안되기 때문에 seek을 사용함 */
	private int seek = 0;

	/**
	 * 형태소 추가
	 * @param morph 추가할 형태소
	 * @param tag 형태소의 품사 구분
	 */
	public void addMorpheme(String morph, String tag) {
		morphemes.add(morph + "/" + tag);
		seek = 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.snu.ids.ha.dic.DicReader#cleanup()
	 */
	@Override
	public void cleanup() throws IOException {
	}

	/*
	 * (non-Javadoc)
	 * @see org.snu.ids.ha.dic.DicReader#readLine()
	 */
	@Override
	public String readLine() throws IOException {
		if (seek == morphemes.size()) {
			seek = 0;
			return null;
		} else {
			return morphemes.get(seek++);
		}
	}

}
