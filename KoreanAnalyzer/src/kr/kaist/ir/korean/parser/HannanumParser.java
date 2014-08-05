package kr.kaist.ir.korean.parser;

import kaist.cilab.parser.berkeleyadaptation.BerkeleyParserWrapper;
import kaist.cilab.parser.berkeleyadaptation.Configuration;
import kaist.cilab.parser.corpusconverter.sejong2treebank.sejongtree.ParseTree;
import kaist.cilab.parser.dependency.DNode;
import kaist.cilab.parser.dependency.DTree;
import kaist.cilab.parser.psg2dg.Converter;
import kr.kaist.ir.korean.data.TaggedSentence;
import kr.kaist.ir.korean.data.TaggedWord;
import kr.kaist.ir.korean.data.TaggedWord.FunctionalTag;
import kr.kaist.ir.korean.tagger.HannanumTagger;
import kr.kaist.ir.korean.util.TagConverter;

/**
 * 한나눔 구문분석기를 사용한 의존관계 분석 클래스
 * 
 * @author 김부근
 * @version 0.2.0
 * @since 2014-08-05
 */
public class HannanumParser implements Parser {
	/** 한나눔에서 의존관계 분석에 사용하는 버클리 분석기 */
	private BerkeleyParserWrapper wrapper;
	/** 한나눔 형태소 분석기 */
	private HannanumTagger tagger;
	/** 한나눔 형태소 분석 결과 변환기 */
	private Converter conv;

	/**
	 * 한나눔 구문분석기를 생성한다.
	 * 
	 * @throws Exception
	 *             구문분석기 생성에 실패할 경우 발생한다.
	 */
	public HannanumParser() throws Exception {
		Configuration.hanBaseDir = "./";
		
		tagger = new HannanumTagger();
		wrapper = new BerkeleyParserWrapper(Configuration.parserModel);
		conv = new Converter();
	}

	/**
	 * 의존관계 분석 없이, 단지 구조 분석만을 실시한다.
	 * 
	 * @param sentence 구조 분석을 실시할 문장
	 * @return 구조 분석의 결과로 주어지는 ParseTree(한나눔 참고)
	 * @throws Exception 구조 분석이 실패할 경우 주어진다.
	 */
	public ParseTree parseTreeOf(String sentence) throws Exception{
		String parse = wrapper.parse(sentence);
		parse = Converter.functionTagReForm(parse);
		parse = conv.StringforDepformat(parse);
		
		return new ParseTree(sentence, parse, 0, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.kaist.ir.korean.parser.Parser#dependencyOf(java.lang.String)
	 */
	@Override
	public TaggedSentence dependencyOf(String sentence) throws Exception {
		// ()에 문제를 일으킬 수 있으므로, ()를 로 대체한다.
		sentence = sentence.replaceAll("\\(", "[").replaceAll("\\)", "]");

		// 한나눔 구문분석은 문장을 돌려주지 않으므로, 문장을 다시 분석한다.
		TaggedSentence value = tagger.analyzeSentence(sentence);
		
		// 의존관계 분석을 실시한다.
		DTree depTree = conv.convert(parseTreeOf(sentence));

		DNode[] nodeList = depTree.getNodeList();

		// 문장에 의존관계를 추가한다.
		for (DNode node : nodeList) {
			TaggedWord headWord = null, thisWord = null;
			try{
				thisWord = value.getWordAt(node.getWordIdx());
				headWord = value.getWordAt(node.getHead()
						.getWordIdx());

				String rawTag = node.getdType();
				FunctionalTag tag = TagConverter.getDependencyTag(
						rawTag, TagConverter.TaggerType.HNN);
				headWord.addDependant(thisWord, tag, rawTag);
			}catch(Exception e){
				// 단어가 목록에 없다는 것은 Root임을 말하는 것이므로, 아무 작업도 하지 않는다.
				if(node.getWordIdx() != -1 && node.getWordIdx() < value.size()){
					value.setRoot(node.getWordIdx());
				}
			}
		}

		return value;
	}
}
