package kr.kaist.ir.korean.tagger;

import java.util.Iterator;
import java.util.LinkedList;

import kr.kaist.ir.korean.data.ConflictedWord;
import kr.kaist.ir.korean.data.TaggedMorpheme;
import kr.kaist.ir.korean.data.TaggedSentence;
import kr.kaist.ir.korean.data.TaggedWord;

/**
 * 한쪽 방법론의 품사부착기의 구조를 사용하고, 다른 방법론의 품사 부착 결과를 참고하여 최종 결과를 생성하는 품사부착기 클래스
 * 
 * @author 김부근
 * @since 2014-08-04
 * @version 0.1.1
 */
public class IntegratedTagger implements Tagger {
	/**
	 * 구조를 사용할 품사부착기 지정을 위한 구분
	 * 
	 * @author 김부근
	 *
	 */
	public static enum ParseStructure {
		/** 꼬꼬마 */
		KKMA,
		/** 한나눔 */
		HNN
	}

	/** 꼬꼬마 Tagger */
	private final Tagger kTagger;
	/** 한나눔 Tagger */
	private final Tagger hTagger;

	/** 구조를 취할 방법론 */
	private final ParseStructure priority;

	/**
	 * 생성자.
	 * 
	 * @param priority
	 *            구조로 사용할 품사부착기 구분
	 * @throws Exception
	 *             초기화 과정에서 문제가 생길 경우 예외 발생.
	 */
	public IntegratedTagger(ParseStructure priority) throws Exception {
		this.priority = priority;

		kTagger = new KkokkomaTagger();
		// 한나눔은 통일성을 위해 품사 부착기 사용
		hTagger = new HannanumTagger();
	}

	/**
	 * 기본 생성자. 구조로 사용할 결과는 KKMA를 따른다.
	 * 
	 * @throws Exception
	 *             초기화 과정에서 문제가 생길 경우 예외 발생.
	 */
	public IntegratedTagger() throws Exception {
		this(ParseStructure.KKMA);
	}

	/**
	 * 서로 다른 두 분석 결과를 통합한다. (어절 단위)
	 * 
	 * @param base
	 *            구조로 사용할 결과
	 * @param ref
	 *            참고자료로 사용할 결과
	 * @return 구조 사용 우선순위에 따라서 통합된 결과.
	 * @exception IndexOutOfBoundsException
	 *                두 결과가 서로 변환이 불가능할 때 발생.
	 */
	public static final TaggedSentence integrateSentence(TaggedSentence base,
			TaggedSentence ref) throws IndexOutOfBoundsException {
		// 새 문장
		TaggedSentence sentence = new TaggedSentence();

		Iterator<TaggedWord> itBase = base.iterator(), itRef = ref.iterator();
		StringBuffer strBase = new StringBuffer(), strRef = new StringBuffer();
		LinkedList<TaggedWord> baseWords = new LinkedList<TaggedWord>();
		LinkedList<TaggedWord> refWords = new LinkedList<TaggedWord>();
		TaggedWord wBase, wRef = null;

		// 두 문장을 앞에서부터 대응하여 양쪽을 포괄하는 형태소 묶음을 찾는다.
		while (itBase.hasNext()) {
			wBase = itBase.next();
			baseWords.add(wBase);
			strBase.append(wBase.getOriginalWord().replaceAll("\\s+", ""));

			while (itRef.hasNext() && strRef.length() < strBase.length()) {
				wRef = itRef.next();
				refWords.add(wRef);
				strRef.append(wRef.getOriginalWord().replaceAll("\\s+", ""));
			}

			// 양쪽을 포괄하는 형태소의 묶음 (어절 묶음)이 만들어졌다면, 그 어절들을 묶어 새로운 어절로 만든 다음 저장한다.
			if (strRef.toString().equals(strBase.toString())) {
				strBase = new StringBuffer();
				strRef = new StringBuffer();
				TaggedWord word;

				// 한 쪽에 비해서 다른 한 쪽이 긴 경우, 명백히 문제가 있는 상황이다.
				if (baseWords.size() == 0 || refWords.size() == 0) {
					throw new IndexOutOfBoundsException(
							"Character Mismatch between two sentences!");
				} else if (baseWords.size() == 1 && refWords.size() == 1) {
					// 각각 1개의 어절인 경우, 형태소 단위도 일치하는지 확인한다.
					Iterator<TaggedMorpheme> mitBase = wBase.iterator(), mitRef = wRef
							.iterator();
					boolean isSame = true;

					while (mitBase.hasNext() && mitRef.hasNext()) {
						if (!mitBase.next().equals(mitRef.next())) {
							isSame = false;
							break;
						}
					}

					// 일치하지 않으면 충돌을 해소하고, 일치한다면 그 단어를 복제한다.
					word = new ConflictedWord(wBase, wRef, !isSame);
				} else {
					// 여러 어절인 경우 충돌을 해소한다.
					word = new ConflictedWord(baseWords, refWords);
				}

				baseWords.clear();
				refWords.clear();

				sentence.addWord(word);
			}
		}

		return sentence;
	}

	/**
	 * 서로 다른 두 분석 결과를 통합한다. (문장 단위)
	 * 
	 * @param base
	 *            구조로 사용할 결과
	 * @param ref
	 *            참고자료로 사용할 결과
	 * @return 구조 사용 우선순위에 따라서 통합된 결과.
	 * @exception IndexOutOfBoundsException
	 *                두 결과가 서로 변환이 불가능할 때 발생.
	 */
	public static LinkedList<TaggedSentence> integrateParagraph(
			LinkedList<TaggedSentence> base, LinkedList<TaggedSentence> ref)
			throws IndexOutOfBoundsException {
		LinkedList<TaggedSentence> paragraph = new LinkedList<TaggedSentence>();

		Iterator<TaggedSentence> itBase = base.iterator(), itRef = ref
				.iterator();
		TaggedSentence sBase = new TaggedSentence(), sRef = new TaggedSentence();

		while (itBase.hasNext()) {
			sBase.concat(itBase.next());
			String strBase = sBase.getOriginalString("");
			String strRef = sRef.getOriginalString("");

			while (itRef.hasNext() && strBase.length() > strRef.length()) {
				sRef.concat(itRef.next());
				strRef = sRef.getOriginalString("");
			}

			if (strBase.equals(strRef)) {
				paragraph.add(integrateSentence(sBase, sRef));
				sBase.clear();
				sRef.clear();
			}
		}

		if (itBase.hasNext() || itRef.hasNext()) {
			throw new IndexOutOfBoundsException(
					"End-point mismatch between sentences!");
		}

		return paragraph;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.kaist.ir.korean.tagger.Tagger#analyzeSentence(java.lang.String)
	 */
	@Override
	public TaggedSentence analyzeSentence(String text) throws Exception {
		TaggedSentence kResult = kTagger.analyzeSentence(text);
		TaggedSentence hResult = hTagger.analyzeSentence(text);

		TaggedSentence base = (priority == ParseStructure.KKMA) ? kResult
				: hResult;
		TaggedSentence ref = (priority == ParseStructure.KKMA) ? hResult
				: kResult;

		// 통합 작업 수행
		return integrateSentence(base, ref);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.kaist.ir.korean.tagger.Tagger#analyzeParagraph(java.lang.String)
	 */
	@Override
	public LinkedList<TaggedSentence> analyzeParagraph(String text)
			throws Exception {
		LinkedList<TaggedSentence> base;
		LinkedList<TaggedSentence> ref;

		if (priority == ParseStructure.KKMA) {
			base = kTagger.analyzeParagraph(text);
			ref = hTagger.analyzeParagraph(text);
		} else {
			base = hTagger.analyzeParagraph(text);
			ref = kTagger.analyzeParagraph(text);
		}

		return integrateParagraph(base, ref);
	}
}
