package kr.kaist.ir.korean.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import kr.kaist.ir.korean.util.MorphemeOperator;

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
 * @version 0.2.2
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
		LinkedList<TaggedMorpheme> mBase = new LinkedList<TaggedMorpheme>(), mRef = new LinkedList<TaggedMorpheme>();
		StringBuffer strbuf = new StringBuffer();

		for (TaggedWord word : base) {
			mBase.addAll(word.getMorphemes());
			strbuf.append(word.getOriginalWord());
		}

		for (TaggedWord word : ref) {
			mRef.addAll(word.getMorphemes());
		}

		setLongestMorpheme(mBase, mRef);
		this.originalWord = strbuf.toString();

		wordsOfBase.addAll(base);
		wordsOfRef.addAll(ref);
	}

	/**
	 * 어절과 어절 목록을 통합하는 내부 생성자.
	 * 
	 * @param a
	 *            어절
	 * @param b
	 *            어절 목록
	 * @param isABase
	 *            A가 골격 방법론인지의 여부
	 */
	private ConflictedWord(TaggedWord a, List<TaggedWord> b, boolean isABase) {
		LinkedList<TaggedMorpheme> mB = new LinkedList<TaggedMorpheme>();
		StringBuffer strbuf = new StringBuffer();

		for (TaggedWord word : b) {
			mB.addAll(word.getMorphemes());
			strbuf.append(word.getOriginalWord());
		}

		if (isABase) {
			setLongestMorpheme(a.getMorphemes(), mB);
			this.originalWord = a.getOriginalWord();

			wordsOfBase.add(a);
			wordsOfRef.addAll(b);
		} else {
			setLongestMorpheme(mB, a.getMorphemes());
			this.originalWord = strbuf.toString();

			wordsOfBase.addAll(b);
			wordsOfRef.add(a);
		}
	}

	/**
	 * 골격 구성 방법론이 가장 포괄적인 어절로 묶어두었을 때 사용하는 생성자. 즉 참고 방법론의 문자열이 포함될때.
	 * <p>
	 * 예: 생일선물 vs 생일/선물
	 * </p>
	 * 
	 * @param base
	 *            골격 구성 방법론이 주장하는 어절 목록
	 * @param ref
	 *            참고 방법론이 주장하는 어절
	 */
	public ConflictedWord(TaggedWord base, List<TaggedWord> ref) {
		this(base, ref, true);
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
		this(ref, base, false);
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
		setLongestMorpheme(base.getMorphemes(), ref.getMorphemes());
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
			result.append(w.toString().replaceAll("\n", "\n\t | ")).append(
					"\n\t | ");
		}
		result.append("\n\t[참조]\n\t | ");
		for (TaggedWord w : wordsOfRef) {
			result.append(w.toString().replaceAll("\n", "\n\t | ")).append(
					"\n\t | ");
		}

		return result.toString();
	}

	/**
	 * 주어진 어절 집합에서 형태소의 통합 형태를 찾는다. 이 과정에서 골격 구성 방법론이 항상 우선하지만, 참고 방법론이 골격 구성
	 * 방법론의 여러 형태소를 합친 단일 형태소를 가지고 있거나, 골격 방법론과 동일한 형태소가 통합 문제가 없는 경우 참고 방법론의
	 * 형태소를 취한다.
	 * 
	 * @param base
	 *            골격 구성 방법론의 형태소 목록
	 * @param ref
	 *            참고 방법론의 형태소 목록
	 * @return 형태소의 통합 형태를 찾은 경우 새로운 형태소가, 아닌 경우 원 형태소를 돌려준다.
	 */
	private void setLongestMorpheme(List<TaggedMorpheme> base,
			List<TaggedMorpheme> ref) {
		String strBase = base.get(0).getMorpheme();
		String strRef = ref.get(0).getMorpheme();

		TaggedMorpheme mRef = ref.get(0);
		int iBase = 0, iRef = 0, lBase = 1, lRef = 1;

		while (true) {
			if (strRef.equals(strBase)) {
				if (lBase == 1 && lRef == 1) {
					TaggedMorpheme mBase = base.get(iBase);
					String class1Tag = mBase.getTag().substring(0, 1);
					boolean useBase = true;

					if (mBase.isProblematic()) {
						if (mRef.isTypeOf(class1Tag)) {
							if (class1Tag.equals("V") || class1Tag.equals("O")
									|| mRef.isTypeOf(mBase.getTag())) {
								useBase = false;
							}
						} else if (mBase.isTypeOf("U")
								|| (mBase.isNumber() && mRef.isNumber())) {
							useBase = false;
						}
					}

					addMorpheme((useBase) ? mBase : mRef);
				} else if (lRef == 1) {
					addMorpheme(mRef);
				} else {
					for (int i = iBase; i < iBase + lBase; i++) {
						addMorpheme(base.get(i));
					}
				}

				iRef += lRef;
				lRef = 1;
				iBase += lBase;
				lBase = 1;

				if (iRef < ref.size()) {
					mRef = ref.get(iRef);
					strRef = ref.get(iRef).getMorpheme();
				} else {
					break;
				}

				if (iBase < base.size()) {
					strBase = base.get(iBase).getMorpheme();
				} else {
					break;
				}
			} else if (strRef.length() > strBase.length()) {
				if (iBase + lBase < base.size()) {
					strBase = MorphemeOperator.concat(strBase, base.get(iBase + lBase));
					lBase++;
				} else {
					break;
				}
			} else {
				if (iRef + lRef < ref.size()) {
					mRef = ref.get(iRef + lRef);
					strRef = MorphemeOperator.concat(strRef, ref.get(iRef + lRef));
					lRef++;
				} else {
					break;
				}
			}
		}

		for (int i = iBase; i < base.size(); i++) {
			addMorpheme(base.get(i));
		}
	}
}
