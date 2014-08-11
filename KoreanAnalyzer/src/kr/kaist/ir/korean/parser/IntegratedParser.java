package kr.kaist.ir.korean.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import kr.kaist.ir.korean.data.ConflictedWord;
import kr.kaist.ir.korean.data.TaggedSentence;
import kr.kaist.ir.korean.data.TaggedWord;
import kr.kaist.ir.korean.data.TaggedWord.FunctionalTag;
import kr.kaist.ir.korean.tagger.IntegratedTagger;
import kr.kaist.ir.korean.util.TagConverter;
import kr.kaist.ir.korean.util.TagConverter.TaggerType;

/**
 * 양쪽 방법론 모두의 결과에서 공통된 정보만을 취합. 이 과정에서 어절 분석 구조가 다를 경우, KKMA를 따르도록 한다. 이 과정에서
 * Root는 없어진다.
 * 
 * @author 김부근
 * @since 2014-08-05
 * @version 0.2.2.4
 */
public class IntegratedParser implements Parser {
	private Parser hParser, kParser;

	/**
	 * 사용할 두 종류의 구문 분석기를 정의하는 생성자.
	 * 
	 * @throws Exception
	 *             초기화가 실패할 경우 발생한다.
	 */
	public IntegratedParser() throws Exception {
		hParser = new HannanumParser();
		kParser = new KkokkomaParser();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.kaist.ir.korean.parser.Parser#dependencyOf(java.lang.String)
	 */
	@Override
	public TaggedSentence dependencyOf(String sentence) throws Exception {
		// 구문을 분석한다.
		TaggedSentence kValue = kParser.dependencyOf(sentence);
		TaggedSentence hValue = hParser.dependencyOf(sentence);
		TaggedSentence value = IntegratedTagger.integrateSentence(kValue,
				hValue);

		HashMap<TaggedWord, Integer> wordmap = new HashMap<TaggedWord, Integer>();
		int index = 0;

		for (TaggedWord w : value) {
			HashMap<Integer, String> kdep = new HashMap<Integer, String>();

			// 꼬꼬마의 분석 결과를 추가한다.
			Iterator<TaggedWord> it = ((ConflictedWord) w).getWordsOfBase();
			while (it.hasNext()) {
				TaggedWord word = it.next();
				wordmap.put(word, index);
				LinkedList<TaggedWord> dependents = word.getDependents();
				for (TaggedWord dep : dependents) {
					Integer i = wordmap.get(dep);
					if (i != null && i != index) {
						// 공통된 결과만을 넣기 위해서 우선은 저장하지 않고 모아둔다.
						kdep.put(i, dep.getRawTag());
					}
				}
			}

			// 한나눔의 분석 결과를 추가한다.
			it = ((ConflictedWord) w).getWordsOfRef();
			while (it.hasNext()) {
				TaggedWord word = it.next();
				wordmap.put(word, index);
				LinkedList<TaggedWord> dependents = word.getDependents();
				for (TaggedWord dep : dependents) {
					Integer i = wordmap.get(dep);
					// 정상적인 index이고 공통 결과라면 저장한다.
					if (i != null && i != index && kdep.containsKey(i)) {
						String kkmaRawTag = kdep.get(i);
						FunctionalTag kkmaTag = TagConverter.getDependencyTag(
								kkmaRawTag, TaggerType.KKMA);
						if (dep.getTag() == kkmaTag
								|| kkmaRawTag.contains("대상")) {
							w.addDependant(value.getWordAt(i), dep.getTag(),
									dep.getRawTag() + "/" + kkmaRawTag);
						}
					}
				}
			}

			index++;
		}

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.kaist.ir.korean.parser.Parser#addUserDictionary(java.util.Map)
	 */
	@Override
	public void addUserDictionary(Map<String, String> dict) {
		kParser.addUserDictionary(dict);
		hParser.addUserDictionary(dict);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.kaist.ir.korean.parser.Parser#addUserDictionary(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addUserDictionary(String morph, String tag) {
		kParser.addUserDictionary(morph, tag);
		hParser.addUserDictionary(morph, tag);
	}
}
