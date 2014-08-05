package kr.kaist.ir.korean.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 통합형태소 분석 과정에서 생기는 충돌을 완화하기 위해, 가장 포괄적인 단위로 어절을 묶어버리는 클래스.
 * </p>
 * <p>
 * 예를 들어서, "생선물"에 대해서 한쪽은 "생, 선물"을, 다른 쪽은 "생선, 물"을 주장하면 "생선물"로 묶어버림.
 * </p>
 * 
 * @author 김부근
 * @since 2014-08-01
 * @version 0.1.0
 */
public final class ConflictedWord extends TaggedWord {
	/** Serial ID */
	private static final long serialVersionUID = 3995407752564911685L;

	/** 골격을 구성하는 방법론이 주장하는 어절 목록 */
	private LinkedList<TaggedWord> wordsOfBase = new LinkedList<TaggedWord>();
	/** 참고로 사용되는 방법론이 주장하는 어절 목록 */
	private LinkedList<TaggedWord> wordsOfRef = new LinkedList<TaggedWord>();

	/**
	 * 가장 불일치할 경우, 즉 한 문자열에 대해서 어절 구분이 판이하게 다른 경우 사용하는 생성자.
	 * <p>
	 * 예: 생일/선물 vs 생/일선/물
	 * </p>
	 * 
	 * @param base
	 *            골격 구성 방법론이 주장하는 어절 목록
	 * @param ref
	 *            참고 방법론이 주장하는 어절 목록
	 */
	public ConflictedWord(List<TaggedWord> base, List<TaggedWord> ref) {
		StringBuffer representation = new StringBuffer();
		for (TaggedWord word : base) {
			representation.append(word.getOriginalWord());

			for (TaggedMorpheme m : word) {
				if (!m.isProblematic()) {
					addMorpheme(m);
				} else {
					addMorpheme(findSafeMorpheme(m, ref));
				}
			}
		}
		this.originalWord = representation.toString();
		wordsOfBase.addAll(base);
		wordsOfRef.addAll(ref);
	}

	/**
	 * 골격 구성 방법론이 가장 포괄적인 어절로 묶어두었을 때 사용하는 생성자. 즉 참고 방법론의 문자열이 포함될때.
	 * <p>
	 * 예: 생일선물 vs 생일/선물
	 * </p>
	 * 
	 * @param base
	 *            골격 구성 방법론이 주장하는 어절
	 * @param ref
	 *            참고 방법론이 주장하는 어절 목록
	 */
	public ConflictedWord(TaggedWord base, List<TaggedWord> ref) {
		for (TaggedMorpheme m : base) {
			if (!m.isProblematic()) {
				addMorpheme(m);
			} else {
				addMorpheme(findSafeMorpheme(m, ref));
			}
		}
		this.originalWord = base.getOriginalWord();
		wordsOfBase.add(base);
		wordsOfRef.addAll(ref);
	}

	/**
	 * 참고 방법론이 가장 포괄적인 어절로 묶어두었을 때 사용하는 생성자. 즉 골격 구성 방법론의 문자열이 포함될때.
	 * <p>
	 * 예: 생일/선물 vs 생일선물
	 * </p>
	 * 
	 * @param base
	 *            골격 구성 방법론이 주장하는 어절 목록
	 * @param ref
	 *            참고 방법론이 주장하는 어절
	 */
	public ConflictedWord(List<TaggedWord> base, TaggedWord ref) {
		StringBuffer representation = new StringBuffer();
		for (TaggedWord word : base) {
			representation.append(word.getOriginalWord());

			for (TaggedMorpheme m : word) {
				if (!m.isProblematic()) {
					addMorpheme(m);
				} else {
					addMorpheme(findSafeMorpheme(m, ref));
				}
			}
		}
		this.originalWord = representation.toString();
		wordsOfBase.addAll(base);
		wordsOfRef.add(ref);
	}

	/**
	 * 어절 구분은 같으나 내부 형태소 구분이 다를 때 사용하는 생성자.
	 * <p>
	 * 예: 선물 (NNG) vs 선물=선(PS)+물(NNG)
	 * </p>
	 * 
	 * @param base
	 *            골격 구성 방법론이 주장하는 어절
	 * @param ref
	 *            참고 방법론이 주장하는 어절
	 */
	public ConflictedWord(TaggedWord base, TaggedWord ref) {
		this(base, ref, false);
	}

	/**
	 * 어절의 충돌이 전혀 일어나지 않은 경우의 생성자.
	 * 
	 * @param base
	 *            골격 구성 방법론이 주장하는 어절
	 * @param ref
	 *            참고 방법론이 주장하는 어절
	 * @param findSafeMorpheme
	 *            형태소 표기 변환을 시도한다면 참(true).
	 */
	public ConflictedWord(TaggedWord base, TaggedWord ref,
			boolean findSafeMorpheme) {
		for (TaggedMorpheme m : base) {
			if (!findSafeMorpheme || !m.isProblematic()) {
				addMorpheme(m);
			} else {
				addMorpheme(findSafeMorpheme(m, ref));
			}
		}

		this.originalWord = base.getOriginalWord();
		wordsOfBase.add(base);
		wordsOfRef.add(ref);
	}

	/**
	 * 골격 구성 방법론이 주장하는 어절 목록. 변경을 방지하기 위해 순회 가능 객체로 반환함.
	 * 
	 * @return 골격 구성 방법론의 어절 목록 순회 객체
	 */
	public Iterator<TaggedWord> getWordsOfBase() {
		return wordsOfBase.iterator();
	}

	/**
	 * 참고 방법론이 주장하는 어절 목록. 변경을 방지하기 위해 순회 가능 객체로 반환함.
	 * 
	 * @return 참고 방법론의 어절 목록 순회 객체
	 */
	public Iterator<TaggedWord> getWordsOfRef() {
		return wordsOfRef.iterator();
	}

	/**
	 * 충돌한 부분을 보여주도록 한다.
	 * 
	 */
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer(super.toString());

		result.append("\n\t[골격 구성]\n\t | ");
		for (TaggedWord w : wordsOfBase) {
			result.append(w.toString().replaceAll("\n", "\n\t | "))
					.append("\n\t | ");
		}
		result.append("\n\t[참조]\n\t | ");
		for (TaggedWord w : wordsOfRef) {
			result.append(w.toString().replaceAll("\n", "\n\t | "))
					.append('\n');
		}

		return result.toString();
	}

	/**
	 * 주어진 어절 집합에서 형태소의 통합 형태를 찾는다. 찾지 못할 경우 현재 형태소의 값을 넘긴다.
	 * 
	 * @param m
	 *            찾고자 하는 형태소
	 * @param ref
	 *            대상이 되는 어절 목록
	 * @return 형태소의 통합 형태를 찾은 경우 새로운 형태소가, 아닌 경우 원 형태소를 돌려준다.
	 */
	private static TaggedMorpheme findSafeMorpheme(TaggedMorpheme m,
			List<TaggedWord> ref) {
		for (TaggedWord w : ref) {
			TaggedMorpheme m2 = findSafeMorpheme(m, w);
			if (m != m2) {
				return m2;
			}
		}

		return m;
	}

	/**
	 * 주어진 단어에서 형태소의 통합 형태를 찾는다. 찾지 못할 경우 현재 형태소의 값을 넘긴다.
	 * 
	 * @param m
	 *            찾고자 하는 형태소
	 * @param ref
	 *            대상이 되는 어절
	 * @return 형태소의 통합 형태를 찾은 경우 새로운 형태소가, 아닌 경우 원 형태소를 돌려준다.
	 */
	private static TaggedMorpheme findSafeMorpheme(TaggedMorpheme m,
			TaggedWord ref) {
		/* 한나눔의 경우 통합태그 NR, VCP/jp, VA/paa, VX, EP, EF, XP, O에서 문제가 발생한다. */
		/* 꼬꼬마의 경우 통합태그 NNG, JX, U에서 발생한다. */

		String class1Tag = m.getTag().substring(0, 1);

		for (TaggedMorpheme morph : ref) {
			if (morph.equals(m.getMorpheme())) {
				if (morph.isTypeOf(class1Tag)) {
					if (class1Tag.equals("V") || class1Tag.equals("O")
							|| morph.isTypeOf(m.getTag())) {
						return morph;
					}
				} else if (m.isTypeOf("U")
						|| (m.isNumber() && morph.isNumber())) {
					return morph;
				}
			}
		}

		return m;
	}
}
