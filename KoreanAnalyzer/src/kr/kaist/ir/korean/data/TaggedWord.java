package kr.kaist.ir.korean.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 어절을 나타내는 클래스. 꼬꼬마와 한나눔의 Eojeol 클래스에 해당함.
 * </p>
 * <p>
 * 직렬화 및 순회 가능 클래스(Serializable, Iterable&lt;Morpheme&gt;)
 * </p>
 * 
 * @author 김부근
 * @since 2014-07-31
 * @version 0.2.0
 */
public class TaggedWord implements Serializable, Iterable<TaggedMorpheme> {
	/**
	 * 문장 내 기능 표지 (세종계획 기능표지 지침을 따름)
	 * 
	 * @author 김부근
	 *
	 */
	public static enum FunctionalTag {
		/** 주어. 주격 체언구(NP_SBJ), 명사 전성 용언구(VP_SBJ), 명사절(S_SBJ) */
		Subject,
		/** 목적어. 목적격 체언구(NP_OBJ), 명사 전성 용언구(VP_OBJ), 명사절(S_OBJ) */
		Object,
		/** 보어. 보격 체언구(NP_CMP), 명사 전성 용언구(VP_CMP), 인용절(S_CMP) */
		Complement,
		/** 체언 수식어(관형격). 관형격 체언구(NP_MOD), 관형형 용언구(VP_MOD), 관형절(S_MOD) */
		Modifier,
		/** 용언 수식어(부사격). 부사격 체언구(NP_AJT), 부사격 용언구(VP_AJT) 문말어미+부사격 조사(S_AJT) */
		Adjunct,
		/** 접속어. 접속격 체언(NP_CNJ) */
		Conjunctive,
		/** 독립어. 체언(NP_INT) */
		Interjective,
		/** 삽입어구. 삽입된 성분의 기능표지 위치 (예: NP_PRN) */
		Parenthetical
	}

	/** Serialize ID */
	private static final long serialVersionUID = 6198162325200883107L;

	/** 형태소 객체(TaggedMorpheme)를 포함하는 LinkedList. */
	private final LinkedList<TaggedMorpheme> morphemes = new LinkedList<TaggedMorpheme>();
	/** 이 어절을 지배소로 하는 의존소(TaggedWord)의 목록. */
	private final LinkedList<TaggedWord> dependents = new LinkedList<TaggedWord>();

	/** 어절의 본래 형태 */
	String originalWord;

	/** 통합형 품사구분을 사용할 경우 문제의 소지가 있는가를 나타냄 */
	private boolean isProblematic = false;
	/** 본 어절이 의존소일 때, 지배소와 맺는 의존관계. 즉 본 어절의 기능 표지를 나타냄. */
	private FunctionalTag tag;
	/** 본래 표시된 기능표지 */
	private String rawTag;

	/** 어절 내 실질형태소의 갯수 */
	private int numOfMeaningful = 0;

	/**
	 * 어절 객체 생성자.
	 * 
	 * @param originalWord
	 *            어절의 본래 형태
	 */
	public TaggedWord(String originalWord) {
		this.originalWord = originalWord;
	}

	/**
	 * 어절 객체 생성자. (기본형)
	 */
	TaggedWord() {
	}

	/**
	 * 어절에 주어진 형태소를 마지막 형태소로 추가한다.
	 * 
	 * @param morpheme
	 *            추가할 형태소.
	 */
	public final void addMorpheme(TaggedMorpheme morpheme) {
		// 형태소를 추가한다.
		this.morphemes.add(morpheme);

		// 꼬꼬마와 한나눔 표기를 통합하는 과정에서 완벽히 통합되지 못하는 경우인지 확인한다.
		this.isProblematic = isProblematic() | morpheme.isProblematic();

		// 실질형태소 여부를 확인한다.
		if (morpheme.isNoun() || morpheme.isVerb() || morpheme.isModifier()) { // 체언,
																				// 용언,
																				// 수식언
			this.numOfMeaningful++;
		}
	}

	/**
	 * 주어진 어절을 본 어절을 지배소로 하는 의존소로 지정한다.
	 * 
	 * @param word
	 *            의존소로 지정할 어절
	 * @param tag
	 *            어절의 의존 관계. 즉 기능 표지.
	 */
	public final void addDependant(TaggedWord word, FunctionalTag tag,
			String rawTag) {
		word.setTag(tag, rawTag);
		this.dependents.add(word);
	}

	/**
	 * <p>
	 * 이 어절이 주어진 품사구분를 가진 어절인지 확인한다. 내부적으로는 형태소를 순회하면서 해당 품사구분에 포함되는 어절을 찾는다.
	 * </p>
	 * 
	 * <pre>
	 * TAG	: TAG에 부합하는 형태소(품사구분) 표기를 찾는다.
	 * TAG+TAG 와 같이 한 어절 내 여러 태그를 지정할 수 있다.
	 * </pre>
	 * 
	 * <p>
	 * 예를 들어 "V+ECE"는 "용언 및 대등연결어미"를 나타낸다.
	 * </p>
	 * 
	 * @param type
	 *            찾을 형태소 품사구분. 묶음 단위 또는 개별 단위 모두 가능하다.
	 * @return 해당 품사구분를 가진 형태소가 있을 경우 참(true).
	 */
	public final boolean contains(String type) {
		List<String> tags = Arrays.asList(type.split("\\+"));

		for (String tag : tags) {
			boolean find = false;
			for (TaggedMorpheme morph : morphemes) {
				if (morph.isTypeOf(tag)) {
					find = true;
					break;
				}
			}

			if (!find) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 주어진 위치의 형태소를 돌려준다. {@link #iterator()}를 참고하면 index를 사용하지 않을 수 있다.
	 * 
	 * @param index
	 *            받을 형태소의 어절 내 위치
	 * @return 해당 위치에 형태소가 있다면 해당 형태소 객체(Morpheme)를 반환한다.
	 * @exception IndexOutOfBoundsException
	 *                해당 위치가 어절 내 위치가 아닐 경우 발생한다.
	 */
	public final TaggedMorpheme getMorphemeAt(int index) {
		return this.morphemes.get(index);
	}

	/**
	 * 해당 품사구분와 일치하는 첫 형태소를 찾는다.
	 * 
	 * @param type
	 *            찾을 형태소 품사구분. 묶음 단위 또는 개별 단위 모두 가능하다.
	 * @return 형태소가 있다면 해당 형태소 객체(Morpheme)를, 아니면 Null을 반환한다.
	 */
	public final TaggedMorpheme getMorphemeOfType(String type) {
		for (int i = 0; i < morphemes.size(); i++) { // 형태소 목록을 순회하면서 해당 품사구분의
														// 형태소가 있는지 찾는다.
			TaggedMorpheme morph = morphemes.get(i);

			if (morph.isTypeOf(type)) {
				return morph;
			}
		}
		return null;
	}

	/**
	 * 가장 첫번째로 등장하는 형태소를 찾는다. 용언이나 체언의 어근 등을 확인할 때 사용할 수 있다.
	 * 
	 * @return 가장 첫번째로 등장하는 형태소.
	 */
	public final TaggedMorpheme getFirst() {
		return morphemes.getFirst();
	}

	/**
	 * 가장 마지막으로 등장하는 형태소를 찾는다. 조사나 어미, 문장부호 등을 확인할 때 사용할 수 있다.
	 * 
	 * @return 가장 마지막으로 등장하는 형태소.
	 */
	public final TaggedMorpheme getLast() {
		return morphemes.getLast();
	}

	/**
	 * 주어진 형태소의 다음 형태소를 찾는다. 어근에 붙은 어미, 접미사 등을 확인할 때 사용할 수 있다.
	 * 
	 * @param m
	 *            다음 형태소를 찾아낼 형태소.
	 * @return 형태소가 본 단어에 있고, 다음 형태소가 있을 경우는 다음 형태소를, 이외에는 Null을 돌려준다.
	 */
	public final TaggedMorpheme getNextOf(TaggedMorpheme m) {
		int index = morphemes.indexOf(m);
		return (index > -1 && index < morphemes.size()) ? morphemes
				.get(index + 1) : null;
	}

	/**
	 * 주어진 형태소의 이전 형태소를 찾는다. 어미나 조사가 붙은 대상을 확인할 때 사용할 수 있다.
	 * 
	 * @param m
	 *            이전 형태소를 찾아낼 형태소.
	 * @return 형태소가 본 단어에 있고 이전 형태소가 있는 경우는 이전 형태소를, 이외에는 Null을 돌려준다.
	 */
	public final TaggedMorpheme getPrevOf(TaggedMorpheme m) {
		int index = morphemes.indexOf(m);
		return (index > 0) ? morphemes.get(index - 1) : null;
	}

	/**
	 * <p>
	 * 형태소 목록을 돌려준다.
	 * </p>
	 * <p>
	 * <b>주의: 형태소 목록을 받아서 수정하지 않도록 주의한다.</b>
	 * </p>
	 * 
	 * @return 어절의 형태소 목록
	 */
	protected final LinkedList<TaggedMorpheme> getMorphemes() {
		return morphemes;
	}

	/**
	 * 이 어절을 지배소로 하는 어절의 목록을 돌려준다.
	 * 
	 * @return 이 어절이 지배소인 의존소 어절 목록.
	 */
	public final LinkedList<TaggedWord> getDependents() {
		return dependents;
	}

	/**
	 * 형태소 목록의 크기를 돌려준다. {@link #iterator()}를 참고하면 index를 사용하지 않을 수 있다.
	 * 
	 * @return 형태소 목록의 크기
	 */
	public final int size() {
		return morphemes.size();
	}

	/**
	 * 실질 형태소의 갯수를 돌려준다. {@link #iterator()}를 참고하면 index를 사용하지 않을 수 있다.
	 * 
	 * @return 형태소 목록의 크기
	 */
	public final int numOfMeaningful() {
		return numOfMeaningful;
	}

	/**
	 * 어절의 원래 형태를 돌려준다.
	 * 
	 * @return 어절의 원래 형태
	 */
	public final String getOriginalWord() {
		return originalWord;
	}

	/**
	 * 본 어절의 기능 표지를 돌려준다.
	 * 
	 * @return 기능 표지.
	 */
	public final FunctionalTag getTag() {
		return tag;
	}

	/**
	 * 본 어절의 기능 표지를 설정한다. 표지 설정은 기능표지가 Null일 때만 가능하다.
	 * 
	 * @param tag
	 *            설정할 기능표지
	 * @throws IllegalStateException
	 *             현재 어절의 기능표지가 Null이 아닐 때.
	 */
	private final void setTag(FunctionalTag tag, String rawTag)
			throws IllegalStateException {
		if (this.tag == null) {
			this.tag = tag;
			this.rawTag = rawTag;
		} else {
			throw new IllegalStateException(
					"Functional tag of this word is not null! Cannot overwrite the functional tag!");
		}
	}

	/**
	 * 본 어절의 기능 표지를, 원래 표기된 상태로 돌려준다.
	 * 
	 * @return 기능 표지 (원문).
	 */
	public final String getRawTag() {
		return rawTag;
	}

	/**
	 * 어절이 통합형 품사구분을 사용할 경우 문제의 소지가 있는지 확인한다.
	 * 
	 * @return 통합형 품사구분 내 완전 통합이 가능한 경우 참(true). 형태소 하나라도 누락되거나 구분이 불명확한 경우
	 *         거짓(false).
	 */
	public final boolean isProblematic() {
		return isProblematic;
	}

	/**
	 * <p>
	 * 어절의 형태소 목록을 초기화한다.
	 * </p>
	 * <p>
	 * <b>새로운 형태소 목록을 입력하기 위한 것이 아니라면, 초기화하지 않도록 한다</b>
	 * </p>
	 */
	protected final void clear() {
		this.morphemes.clear();
	}

	/**
	 * 두 어절이 형태소 표기 결과를 제외하고 같은지, 즉 어절의 문자열 자체가 같은지 확인한다.
	 * 
	 * @param another
	 *            비교할 어절
	 * @return 어절이 같다면 참(true).
	 */
	public boolean equalsWithoutTag(TaggedWord another) {
		return another.getOriginalWord().equals(this.getOriginalWord());
	}

	/**
	 * 어절의 상태를 문자열로 돌려준다. 예를 들면 다음과 같다.
	 * 
	 * <pre>
	 * 구별하는데 = 구별(NNGA/NNGA)+하(XSV/XSV)+ㄴ데(ECD/ECD)+\n
	 * 수식어:\n
	 * 체언포함\t용언포함\tPredicative Verb
	 * </pre>
	 * 
	 */
	@Override
	public String toString() {
		StringBuffer strbuf = new StringBuffer(getOriginalWord() + "\t= ");

		for (TaggedMorpheme m : this) {
			strbuf.append(m.toString());
		}

		if (dependents.size() > 0) {
			strbuf.append("\n\t의존관계:");

			for (TaggedWord w : dependents) {
				strbuf.append(w.getOriginalWord());
				strbuf.append('(');
				strbuf.append(w.getTag());
				strbuf.append('/');
				strbuf.append(w.getRawTag());
				strbuf.append(')');
				strbuf.append(' ');
			}
		}
		
		return strbuf.toString();
	}

	/**
	 * 형태소 순회 객체를 돌려준다.
	 */
	@Override
	public Iterator<TaggedMorpheme> iterator() {
		return morphemes.iterator();
	}
}