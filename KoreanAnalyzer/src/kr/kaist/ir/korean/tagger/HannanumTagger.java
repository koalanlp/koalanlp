package kr.kaist.ir.korean.tagger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import kaist.cilab.jhannanum.common.Eojeol;
import kaist.cilab.jhannanum.common.communication.Sentence;
import kaist.cilab.jhannanum.common.workflow.Workflow;
import kaist.cilab.jhannanum.plugin.supplement.MorphemeProcessor.UnknownMorphProcessor.UnknownProcessor;
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.InformalSentenceFilter.InformalSentenceFilter;
import kaist.cilab.jhannanum.plugin.supplement.PlainTextProcessor.SentenceSegmentor.SentenceSegmentor;
import kaist.cilab.jhannanum.plugin.supplement.PosProcessor.NounExtractor.NounExtractor;
import kr.kaist.ir.korean.data.TaggedMorpheme;
import kr.kaist.ir.korean.data.TaggedSentence;
import kr.kaist.ir.korean.data.TaggedWord;
import kr.kaist.ir.korean.extension.ExtendedChartMorphAnalyzer;
import kr.kaist.ir.korean.extension.ExtendedHMMTagger;
import kr.kaist.ir.korean.extension.UserDictProcessor;
import kr.kaist.ir.korean.util.TagConverter.TaggerType;

/**
 * 한나눔 형태소 분석기를 사용한 품사부착 클래스
 * 
 * @author 김부근
 * @since 2014-07-31
 * @version 0.2.2.4
 */
public final class HannanumTagger implements Tagger {
	/**
	 * 한나눔의 평문 변환 추가기능 구분.
	 * 
	 * @author 김부근
	 *
	 */
	public static enum PlainTextAddon {
		/** 문장 단위 자르기 작업 */
		SentenceSegment,
		/** 형식을 벗어난 구문 배제 작업 */
		InformalSentenceFilter
	}

	/** jar 리소스 압축 해제할 폴더 */
	private static final File TMP = new File(".");
	/** 전체 초기화 여부 */
	private static boolean initialized = false;
	/** Default Tagger */
	private static volatile HannanumTagger defaultTagger;

	/** 한나눔 형태소 분석기와 품사 부착기를 사용하기 위한 변수 */
	private Workflow workflow;
	/** 품사부착기 초기화 여부 */
	private boolean isWorkflowActivated = false;
	/** 프로세싱 대기 여부 */
	private Boolean isWorkflowOnline = false;
	/** 사용자 사전 */
	private UserDictProcessor userDictProcessor;

	/**
	 * <p>
	 * 한나눔의 기본 형태소 분석기를 돌려준다.
	 * </p>
	 * 
	 * @return 한나눔의 기본 형태소 분석기.
	 * @throws Exception
	 *             분석기 초기화에 실패할 경우.
	 */
	public static HannanumTagger getDefaultTagger() throws Exception {
		if (defaultTagger == null) {
			defaultTagger = new HannanumTagger();
		}

		return defaultTagger;
	}

	/**
	 * 한나눔 형태소 분석기까지 사용하는 Tagger 생성자.
	 * 
	 * @param useUnknownMorph
	 *            알 수 없는 형태소 분석 처리 여부
	 * @param addons
	 *            평문 변환 추가기능 구분(들)
	 * @throws Exception
	 *             형태소 분석기 초기화 과정에서 실패할 경우 발생.
	 */
	public HannanumTagger(boolean useUnknownMorph, PlainTextAddon... addons)
			throws Exception {
		init();
		workflow = new Workflow();

		// 평문 변환 추가기능 부착
		for (PlainTextAddon type : addons) {
			switch (type) {
			case SentenceSegment:
				workflow.appendPlainTextProcessor(new SentenceSegmentor(),
						"conf/plugin/SupplementPlugin/PlainTextProcessor/SentenceSegment.json");
				break;
			case InformalSentenceFilter:
				workflow.appendPlainTextProcessor(new InformalSentenceFilter(),
						"conf/plugin/SupplementPlugin/PlainTextProcessor/InformalSentenceFilter.json");
				break;
			}
		}

		// 형태소 분석기 할당
		workflow.setMorphAnalyzer(new ExtendedChartMorphAnalyzer(),
				"conf/plugin/MajorPlugin/MorphAnalyzer/ChartMorphAnalyzer.json");
		
		// 형태소 분석기 플러그인 추가
		if (useUnknownMorph) {
			workflow.appendMorphemeProcessor(new UnknownProcessor(),
					"conf/plugin/SupplementPlugin/MorphemeProcessor/UnknownMorphProcessor.json");
		}

		userDictProcessor = new UserDictProcessor();
		workflow.appendMorphemeProcessor(userDictProcessor, null);
	}

	/**
	 * 한나눔 품사 부착기까지 사용하는 Tagger 생성자.
	 * 
	 * @param useUnknownMorph
	 *            알 수 없는 형태소 분석 처리 여부
	 * @param useNounExtractor
	 *            명사 추출기 사용 구분
	 * @param addons
	 *            평문 변환 추가기능 구분(들)
	 * @throws Exception
	 *             분석기 초기화 과정에서 실패할 경우 발생.
	 */
	public HannanumTagger(boolean useUnknownMorph, boolean useNounExtractor,
			PlainTextAddon... addons) throws Exception {
		// 형태소 분석기까지 초기화
		this(useUnknownMorph, addons);

		// 기본 품사부착기 추가
		workflow.setPosTagger(new ExtendedHMMTagger(),
				"conf/plugin/MajorPlugin/PosTagger/HmmPosTagger.json");

		// 품사 부착기 플러그인 추가
		if (useNounExtractor) {
			workflow.appendPosProcessor(new NounExtractor(),
					"conf/plugin/SupplementPlugin/PosTagger/NounExtractor.json");
		}
	}

	/**
	 * 한나눔에서 미리 정의한 Tagger 생성자
	 * 
	 * @throws Exception
	 *             분석기 초기화 과정에서 실패할 경우 발생.
	 */
	protected HannanumTagger() throws Exception {
		this(true, false, PlainTextAddon.SentenceSegment,
				PlainTextAddon.InformalSentenceFilter);
	}

	/**
	 * 한나눔 형태소 분석기의 문장 분석 결과를 통합 형태로 변환한다.
	 * 
	 * @param result
	 *            변환할 꼬꼬마 형태소 분석기 결과
	 * @return 변환된 품사 부착 결과
	 */
	private TaggedSentence parseResult(Sentence result) {
		int len = result.length;
		Eojeol[] eojeols = result.getEojeols();
		String[] plainEojeols = result.getPlainEojeols();

		// 새 문장
		TaggedSentence sentence = new TaggedSentence();

		// 각 어절마다 변환작업 수행
		for (int i = 0; i < len; i++) {
			TaggedWord word = new TaggedWord(plainEojeols[i]);
			// 형태소는 있으나 파싱 결과가 없는 경우,분석 오류이므로 수동으로 형태소를 삽입한다.
			String[] morphemes = (eojeols[i] != null) ? eojeols[i]
					.getMorphemes() : new String[] { plainEojeols[i] };
			String[] tags = (eojeols[i] != null) ? eojeols[i].getTags()
					: new String[] { "ncn" };

			// 형태소를 변환한다.
			for (int m = 0; m < morphemes.length; m++) {
				TaggedMorpheme morpheme = new TaggedMorpheme(morphemes[m],
						tags[m], TaggerType.HNN);
				word.addMorpheme(morpheme);
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
		workflow.close();
		super.finalize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.kaist.ir.korean.tagger.Tagger#analyzeSentence(java.lang.String)
	 */
	@Override
	public synchronized TaggedSentence analyzeSentence(String text)
			throws Exception {
		// 초기화가 안되어있다면 분석기를 초기화한다
		if (!isWorkflowActivated) {
			isWorkflowActivated = true;
			workflow.activateWorkflow(true);
		}

		Sentence sentence;
		// Tagger를 공유할 경우 간혹 문장이 섞이는 경우가 있다. 분석이 진행중이면 0.1초간 현 스레드를 대기시킨다.
		while (isWorkflowOnline) {
			Thread.sleep(100);
		}

		synchronized (isWorkflowOnline) {
			// 분석한다
			isWorkflowOnline = true;
			workflow.analyze(text);

			isWorkflowOnline = false;
			sentence = workflow.getResultOfSentence(new Sentence(0, 0, false));
		}

		return parseResult(sentence);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.kaist.ir.korean.tagger.Tagger#analyzeParagraph(java.lang.String)
	 */
	@Override
	public synchronized LinkedList<TaggedSentence> analyzeParagraph(String text)
			throws Exception {
		// 초기화가 안되어있다면 분석기를 초기화한다
		if (!isWorkflowActivated) {
			isWorkflowActivated = true;
			workflow.activateWorkflow(true);
		}

		// Tagger를 공유할 경우 간혹 문장이 섞이는 경우가 있다. 분석이 진행중이면 0.1초간 현 스레드를 대기시킨다.
		while (isWorkflowOnline) {
			Thread.sleep(100);
		}

		LinkedList<TaggedSentence> paragraph = new LinkedList<TaggedSentence>();
		synchronized (isWorkflowOnline) {
			// 분석한다
			isWorkflowOnline = true;
			workflow.analyze(text);

			String needToParse = text.replaceAll("\\s+", "");

			// 문장단위로 변환한다
			// 한나눔의 API가 오류가 있어 직접 변환하도록 한다.
			workflow.analyze("ENDEND");

			while (true) {
				try {
					Sentence line = workflow.getResultOfSentence(new Sentence(
							0, 0, false));
					if (line.toString().contains("ENDEND/f")) {
						break;
					}

					TaggedSentence sentence = parseResult(line);
					String parsed = sentence.getOriginalString("");

					paragraph.add(sentence);
					if (needToParse.length() > parsed.length()) {
						needToParse = needToParse.substring(parsed.length());
					} else {
						needToParse = "";
					}
				} catch (NullPointerException e) {
					System.out
							.println("Hannanum Tagger has problem! At this point : "
									+ needToParse);
				}
			}
			isWorkflowOnline = false;
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
		userDictProcessor.setDict(dict);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.kaist.ir.korean.tagger.Tagger#addUserMorph(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addUserDictionary(String morph, String tag) {
		userDictProcessor.addDict(morph, tag);
	}

	/**
	 * 리소스 복사를 위한 초기화 함수
	 * 
	 * @return
	 * 
	 * @throws IOException
	 *             초기화 과정 실패시 발생
	 */
	private static void init() throws IOException {
		if (initialized) {
			return;
		} else {
			initialized = true;
		}

		ClassLoader loader = ClassLoader.getSystemClassLoader();
		ZipInputStream zis = null;
		try {
			File model = new File("models.zip");
			if (model.exists()) {
				zis = new ZipInputStream(new FileInputStream(model));
			} else {
				zis = new ZipInputStream(
						loader.getResourceAsStream("models.zip"));
			}
			ZipEntry entry;

			while ((entry = zis.getNextEntry()) != null) {
				String fileNameToUnzip = entry.getName();
				File targetFile = new File(TMP, fileNameToUnzip);

				if (entry.isDirectory()) {
					targetFile.mkdirs();
				} else {
					targetFile.getParentFile().mkdirs();

					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(targetFile);

						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = zis.read(buffer)) != -1) {
							fos.write(buffer, 0, len);
						}
					} finally {
						if (fos != null) {
							fos.close();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (zis != null) {
				zis.close();
			}
		}
	}
}
