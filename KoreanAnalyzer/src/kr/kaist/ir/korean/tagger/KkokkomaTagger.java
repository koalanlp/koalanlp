package kr.kaist.ir.korean.tagger;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.kaist.ir.korean.data.TaggedMorpheme;
import kr.kaist.ir.korean.data.TaggedSentence;
import kr.kaist.ir.korean.data.TaggedWord;
import kr.kaist.ir.korean.extension.UserDicReader;
import kr.kaist.ir.korean.util.TagConverter;
import kr.kaist.ir.korean.util.TagConverter.TaggerType;

import org.snu.ids.ha.dic.DicReader;
import org.snu.ids.ha.dic.Dictionary;
import org.snu.ids.ha.dic.RawDicFileReader;
import org.snu.ids.ha.dic.SimpleDicFileReader;
import org.snu.ids.ha.ma.Eojeol;
import org.snu.ids.ha.ma.MExpression;
import org.snu.ids.ha.ma.MorphemeAnalyzer;
import org.snu.ids.ha.ma.Sentence;

/**
 * 꼬꼬마 형태소 분석기를 사용한 품사부착 클래스
 * 
 * @author 김부근
 * @since 2014-07-31
 * @version 0.2.2.4
 */
public final class KkokkomaTagger implements Tagger {

	/** Default Tagger */
	private static volatile KkokkomaTagger defaultTagger;
	/** 사용자 정의 Dictionary를 위한 리스트 */
	private static LinkedList<DicReader> readers = new LinkedList<DicReader>();
	/** 사용자 정의 Dictionary */
	private static volatile UserDicReader userdic;

	/**
	 * 꼬꼬마 형태소 분석기
	 */
	private MorphemeAnalyzer ma;

	/**
	 * <p>
	 * 꼬꼬마의 기본 형태소 분석기를 돌려준다.
	 * </p>
	 * <p>
	 * 리소스 절약을 위해서 꼬꼬마 분석기는 한 개만 사용하는 것이 좋다. 여러개 사용하고 싶다면 별도로 만들 수 있다.
	 * {@link #KkokkomaTagger()}참조.
	 * </p>
	 * 
	 * @return 꼬꼬마의 기본 형태소 분석기.
	 * @throws Exception
	 *             분석기 초기화에 실패할 경우.
	 */
	public static KkokkomaTagger getDefaultTagger() throws Exception {
		if (defaultTagger == null) {
			defaultTagger = new KkokkomaTagger();
		}

		return defaultTagger;
	}

	/**
	 * 생성자. 꼬꼬마 형태소 분석기를 새로 시동한다.
	 */
	public KkokkomaTagger() {
		ma = new MorphemeAnalyzer();
		ma.createLogger(null);
	}

	/**
	 * 꼬꼬마 형태소 분석기의 문장 분석 결과를 통합 형태로 변환한다.
	 * 
	 * @param result
	 *            변환할 꼬꼬마 형태소 분석기 결과
	 * @return 변환된 품사 부착 결과
	 */
	public TaggedSentence parseResult(Sentence result) {
		// 새로운 문장
		TaggedSentence sentence = new TaggedSentence();

		// 각 어절마다 변환작업 수행
		for (int i = 0; i < result.size(); i++) {
			Eojeol eojeol = result.get(i);

			// 새 어절
			TaggedWord word = new TaggedWord(eojeol.getExp());

			// 어절에 포함된 형태소를 변환
			for (int j = 0; j < eojeol.size(); j++) {
				org.snu.ids.ha.ma.Morpheme morp = eojeol.get(j);
				word.addMorpheme(new TaggedMorpheme(morp.getString(), morp
						.getTag(), TaggerType.KKMA));
			}

			// 문장에 단어를 저장함.
			sentence.addWord(word);
		}

		return sentence;
	}

	/**
	 * 사후 정리
	 * 
	 * @throws Throwable
	 *             문제 발생시
	 */
	@Override
	protected void finalize() throws Throwable {
		ma.closeLogger();
		super.finalize();
	}

	/**
	 * 꼬꼬마 분석기의 결과를 그대로 돌려줍니다.
	 * 
	 * @param text
	 *            형태소 분석을 진행할 문자열
	 * @return 꼬꼬마 분석기의 형태소 분석 결과 (org.snu.ids.ha.ma.Sentence)
	 * @throws Exception
	 *             분석 과정에서 오류가 발생할 경우 발생합니다.
	 */
	public List<Sentence> analyzeSentenceRaw(String text) throws Exception {
		//한글 사이 특수 기호가 붙어있을 경우 문제가 발생할 수 있으므로, 문장부호를 제하고 앞 뒤에 한칸씩 띄어쓰기를 더한다.
		text = text.replaceAll("\\s*([^ㄱ-힣0-9A-Za-z,\\.!\\?\'\"]+)\\s*", " $1 ");
		
		// 문장을 분석한다.
		List<MExpression> ret = ma.analyze(text);
		// 띄어쓰기를 정돈한다.
		ret = ma.postProcess(ret);
		// 가장 적합한 품사 부착 결과만을 남겨둔다.
		ret = ma.leaveJustBest(ret);
		// 문장단위로 분할하고 돌려준다.
		return ma.divideToSentences(ret);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.kaist.ir.korean.tagger.Tagger#analyzeSentence(java.lang.String)
	 */
	@Override
	public TaggedSentence analyzeSentence(String text) throws Exception {
		return parseResult(analyzeSentenceRaw(text).get(0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.kaist.ir.korean.tagger.Tagger#analyzeParagraph(java.lang.String)
	 */
	@Override
	public LinkedList<TaggedSentence> analyzeParagraph(String text)
			throws Exception {
		// 문장 단위로 분할하여 각 문장을 통합형태로 변환한다.
		List<Sentence> results = analyzeSentenceRaw(text);
		LinkedList<TaggedSentence> paragraph = new LinkedList<TaggedSentence>();

		for (org.snu.ids.ha.ma.Sentence result : results) {
			paragraph.add(parseResult(result));
		}

		return paragraph;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.kaist.ir.korean.tagger.Tagger#addUserDictionary(java.util.Map)
	 */
	@Override
	public void addUserDictionary(Map<String, String> dict) {
		if (dict != null && dict.size() > 0) {
			try {
				initReaders();
				Set<String> keySet = dict.keySet();
				for (String word : keySet) {
					if (word.trim().length() > 0) {
						userdic.addMorpheme(word, TagConverter.toOriginalTag(
								dict.get(word), TaggerType.KKMA));
					}
				}

				Dictionary.reload(readers);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.kaist.ir.korean.tagger.Tagger#addUserMorph(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addUserDictionary(String morph, String tag) {
		if (morph != null && morph.length() > 0 && tag != null) {
			try {
				initReaders();
				userdic.addMorpheme(morph,
						TagConverter.toOriginalTag(tag, TaggerType.KKMA));

				Dictionary.reload(readers);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 사전 목록의 초기화
	 * 
	 * @throws UnsupportedEncodingException
	 *             초기화 실패시 발생
	 */
	private static synchronized void initReaders()
			throws UnsupportedEncodingException {
		synchronized (readers) {
			if (userdic == null) {
				userdic = new UserDicReader();
				readers.add(new SimpleDicFileReader("/dic/kcc.dic"));
				readers.add(new SimpleDicFileReader("/dic/noun.dic"));
				readers.add(new SimpleDicFileReader("/dic/person.dic"));
				readers.add(new RawDicFileReader("/dic/raw.dic"));
				readers.add(new SimpleDicFileReader("/dic/simple.dic"));
				readers.add(new SimpleDicFileReader("/dic/verb.dic"));
				readers.add(userdic);
			}
		}
	}
}
