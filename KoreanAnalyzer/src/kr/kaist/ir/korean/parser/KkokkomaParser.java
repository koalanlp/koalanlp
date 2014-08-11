package kr.kaist.ir.korean.parser;

import java.util.List;
import java.util.Map;

import kr.kaist.ir.korean.data.TaggedSentence;
import kr.kaist.ir.korean.data.TaggedWord;
import kr.kaist.ir.korean.data.TaggedWord.FunctionalTag;
import kr.kaist.ir.korean.tagger.KkokkomaTagger;
import kr.kaist.ir.korean.util.TagConverter;
import kr.kaist.ir.korean.util.TagConverter.TaggerType;

import org.snu.ids.ha.ma.Sentence;
import org.snu.ids.ha.sp.ParseTree;
import org.snu.ids.ha.sp.ParseTreeEdge;
import org.snu.ids.ha.sp.ParseTreeNode;

/**
 * 꾜꼬마 구문 분석기를 사용하는 구문 분석 클래스
 * 
 * @author 김부근
 * @since 2014-08-05
 * @version 0.2.2.4
 */
public class KkokkomaParser implements Parser {
	/** 꼬꼬마 구문분석기 */
	private org.snu.ids.ha.sp.Parser parser;
	/** 꼬꼬마 형태소 분석기 */
	private KkokkomaTagger tagger;

	/**
	 * 꼬꼬마 형태소 분석기와 구문분석기를 초기화하는 생성자
	 */
	public KkokkomaParser() {
		parser = org.snu.ids.ha.sp.Parser.getInstance();
		tagger = new KkokkomaTagger();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.kaist.ir.korean.parser.Parser#dependencyOf(java.lang.String)
	 */
	@Override
	public TaggedSentence dependencyOf(String sentence) throws Exception {
		// 형태소 분석을 실시하되, 양쪽 결과가 모두 필요하므로 꼬꼬마의 결과를 받고, 받은 결과를 토대로 TaggedSentence를
		// 생성한다.
		Sentence rawSentence = tagger.analyzeSentenceRaw(sentence).get(0);
		TaggedSentence value = tagger.parseResult(rawSentence);

		// 구문 분석을 실시한다.
		ParseTree parse = parser.parse(rawSentence);

		// 구문 분석 결과의 각 변의 양 끝점을 찾아 문장에 추가한다.
		List<ParseTreeEdge> edgeList = parse.getEdgeList();
		List<ParseTreeNode> nodeList = parse.getNodeList();
		for (ParseTreeEdge e : edgeList) {
			int from = rawSentence.indexOf(nodeList.get(e.getFromId())
					.getEojeol());
			int to = rawSentence.indexOf(nodeList.get(e.getToId()).getEojeol());
			if (from != -1 && to != -1) {
				TaggedWord thisWord = value.getWordAt(to);
				TaggedWord headWord = value.getWordAt(from);
				String rawTag = e.getRelation();
				FunctionalTag tag = TagConverter.getDependencyTag(rawTag,
						TaggerType.KKMA);

				headWord.addDependant(thisWord, tag, rawTag);
			} else if (to != -1) {
				// Root를 지정하고 있으므로 표기해준다.
				value.setRoot(to);
			}
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
		tagger.addUserDictionary(dict);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.kaist.ir.korean.parser.Parser#addUserDictionary(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addUserDictionary(String morph, String tag) {
		tagger.addUserDictionary(morph, tag);
	}
}
