package kr.kaist.ir.korean.data;

import java.io.Serializable;

import kr.kaist.ir.korean.util.TagConverter;
import kr.kaist.ir.korean.util.TagConverter.TaggerType;

/**
 * <p>
 * 형태소를 나타내는 클래스. 꼬꼬마의 Morpheme 클래스에 해당하며, 한나눔에서는 String으로 처리하고 있다.
 * </p>
 * <p>
 * 직렬화 가능(Serializable) 객체.
 * </p>
 * {@link TagConverter} 참조
 * 
 * @author 김부근
 * @since 2014-08-04
 * @version 0.2.0
 */
public class TaggedMorpheme implements Serializable {
	/** Serialize ID */
	private static final long serialVersionUID = 586158448235147684L;

	/** 형태소 문자열 */
	private final String morpheme;
	/** 통합형 형태소 품사구분. */
	private final String tag;
	/** 방법론별 품사 구분 */
	private final String rawTag;
	/** 형태소가 통합형 구분을 적용할 때 문제가 발생할 소지가 있는지 확인한다. */
	private boolean isProblematic = false;

	/**
	 * 생성자. 형태소와 품사구분을 받아 형태소 객체를 만든다.
	 * 
	 * @param morpheme
	 *            형태소 문자열
	 * @param tag
	 *            품사구분 문자열
	 */
	public TaggedMorpheme(String morpheme, String tag, TaggerType type) {
		super();
		this.morpheme = morpheme;
		this.rawTag = tag;
		this.tag = TagConverter.toIntegratedPOSTag(tag, type); // 품사구분을 통합형 구분으로 변환한다.
		this.isProblematic = TagConverter.isProblematicPOSTag(tag, type);
	}

	/**
	 * 형태소가 통합형 품사구분을 사용할 경우 문제의 소지가 있는지 확인한다.
	 * 
	 * @return 통합형 품사구분 내 통합이 가능한 경우 참(true). 누락되거나 구분이 불명확한 경우 거짓(false).
	 */
	public final boolean isProblematic() {
		return isProblematic;
	}

	/**
	 * 형태소 문자열을 돌려준다.
	 * 
	 * @return 형태소 문자열
	 */
	public final String getMorpheme() {
		return morpheme;
	}

	/**
	 * 통합형 품사 구분 문자열을 돌려준다.
	 * 
	 * @return 통합형 품사 구분 문자열
	 */
	public final String getTag() {
		return tag;
	}

	/**
	 * 원 방법론의 품사 구분을 돌려준다.
	 * 
	 * @return 원 방법론 품사 구분 문자열.
	 */
	public final String getRawTag() {
		return rawTag;
	}

	/**
	 * 품사가 체언인지 확인한다.
	 * 
	 * @return 체언일 경우 참(true).
	 */
	public final boolean isNoun() {
		return tag.startsWith("N");
	}

	/**
	 * 품사가 용언인지 확인한다.
	 * 
	 * @return 용언일 경우 참(true).
	 */
	public final boolean isVerb() {
		return tag.startsWith("V");
	}

	/**
	 * 품사가 수식언인지 확인한다.
	 * 
	 * @return 수식언일 경우 참(true).
	 */
	public final boolean isModifier() {
		return tag.startsWith("M");
	}

	/**
	 * 품사가 관계언인지 확인한다.
	 * 
	 * @return 관계언(조사)일 경우 참(true).
	 */
	public final boolean isJosa() {
		return tag.startsWith("J");
	}

	/**
	 * 본 형태소가 숫자인지 확인한다.
	 * 
	 * @return 숫자일 경우 참(true).
	 */
	public boolean isNumber() {
		return tag.equals("NR") || tag.equals("MDN") || tag.equals("ON");
	}

	/**
	 * 통합형 구분 품사가 인자로 전달된 품사구분에 포함되는지 확인한다.
	 * 
	 * @param tag
	 *            형태소가 포함되는지 확인하려는 품사구분
	 * @return 포함될 경우 참(true).
	 */
	public final boolean isTypeOf(String tag) {
		return this.tag.startsWith(tag);
	}

	/**
	 * 원 방법론의 품사가 인자로 전달된 품사 구분에 포함되는지 확인한다.
	 * 
	 * @param tag
	 *            형태소가 포함되는지 확인하려는 품사구분
	 * @return 포함될 경우 참(true).
	 */
	public final boolean isRawTypeOf(String tag) {
		return rawTag.startsWith(tag);
	}

	/**
	 * 두 형태소가 태그를 제외하고 같은지 확인한다.
	 * 
	 * @param another
	 *            비교할 다른 형태소.
	 */
	public boolean equalsWithoutTag(TaggedMorpheme another) {
		return another.getMorpheme().equals(this.morpheme);
	}

	/**
	 * 형태소를 통합형 구분, 원 방법론의 구분과 함께 문자열로 돌려준다. 예를 들면 다음과 같다.
	 * <p>
	 * 도움(NNGN/ncn)
	 * </p>
	 */
	@Override
	public String toString() {
		return morpheme + "(" + tag + "/" + rawTag + ")";
	}

	/**
	 * 두 형태소가 같은지 확인한다. 문자열이 전달될 경우, 형태소 문자열이 같은지 확인한다.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TaggedMorpheme) {
			return ((TaggedMorpheme) obj).getMorpheme().equals(this.morpheme)
					&& ((TaggedMorpheme) obj).getTag().equals(this.tag);
		} else if (obj instanceof String) {
			return ((String) obj).equals(this.morpheme);
		} else {
			return false;
		}
	}
}
