package kr.kaist.ir.korean.util;

import kaist.cilab.jhannanum.common.Code;
import kr.kaist.ir.korean.data.TaggedMorpheme;
import kr.kaist.ir.korean.util.TagConverter.TaggerType;

import org.snu.ids.ha.util.Hangul;

/**
 * 형태소 관련 연산을 처리한다.
 * 
 * @author 김부근
 * @version 0.2.2.4
 * @since 2014-08-09
 */
public class MorphemeOperator {

	/**
	 * 문자열에 주어진 형태소를 이어붙인다.
	 * 
	 * @param current
	 *            이어붙이기 전의 문자열
	 * @param morp
	 *            이어붙일 형태소
	 * @return 둘을 이어붙인 문자열을 돌려준다.
	 */
	public static String concat(String current, TaggedMorpheme morp) {
		return concat(current, morp.getMorpheme(), morp.getTag(),
				morp.getType());
	}

	/**
	 * 문자열에 주어진 형태소를 이어붙인다.
	 * 
	 * @param current
	 *            이어붙이기 전의 문자열
	 * @param morp
	 *            이어붙일 형태소
	 * @param morpType
	 *            형태소의 품사 구분
	 * @param type
	 *            형태소 분석기의 유형
	 * @return 둘을 이어붙인 문자열을 돌려준다.
	 */
	public static String concat(String current, String morp, String morpType,
			TaggerType type) {
		if (current == null) {
			return morp;
		} else if (morpType.startsWith("S") || morpType.startsWith("O")
				|| morpType.startsWith("NR") || morpType.startsWith("MDN")
				|| current.length() == 0) {
			return current + morp;
		} else if (type == TaggerType.HNN
				&& Hangul.hasJong(current.substring(current.length() - 1))) {
			char[] tplCurrent, tplN;
			switch (morp) {
			case "ㄴ":
				tplCurrent = Code.toTripleArray(current);
				tplN = Code.toTripleArray("은");

				tplCurrent[tplCurrent.length - 1] = tplN[tplN.length - 1];
				return Code.toString(tplCurrent);
			case "ㄹ":
				tplCurrent = Code.toTripleArray(current);
				tplN = Code.toTripleArray("을");

				tplCurrent[tplCurrent.length - 1] = tplN[tplN.length - 1];
				return Code.toString(tplCurrent);
			}

			return Hangul.append(current, morp);
		} else {
			return Hangul.append(current, morp);
		}
	}
}
