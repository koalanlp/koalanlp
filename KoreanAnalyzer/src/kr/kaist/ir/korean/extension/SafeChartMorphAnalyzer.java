package kr.kaist.ir.korean.extension;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import kaist.cilab.jhannanum.common.Code;
import kaist.cilab.jhannanum.common.Eojeol;
import kaist.cilab.jhannanum.common.JSONReader;
import kaist.cilab.jhannanum.common.communication.PlainSentence;
import kaist.cilab.jhannanum.common.communication.SetOfSentences;
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.MorphemeChart;
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.PostProcessor;
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.Simti;
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.datastructure.TagSet;
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.datastructure.Trie;
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.datastructure.Trie.INFO;
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.resource.AnalyzedDic;
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.resource.Connection;
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.resource.ConnectionNot;
import kaist.cilab.jhannanum.morphanalyzer.chartmorphanalyzer.resource.NumberAutomata;
import kaist.cilab.jhannanum.plugin.major.morphanalyzer.MorphAnalyzer;
import kaist.cilab.parser.berkeleyadaptation.Configuration;
import kr.kaist.ir.korean.util.TagConverter;
import kr.kaist.ir.korean.util.TagConverter.TaggerType;

/**
 * <p>
 * 한나눔 0.8.4 버전의 ChartMorphAnalyzer 클래스를, 프로그래밍 가능한 사용자 정의 사전을 적용할 수 있도록 변형한
 * 클래스.
 * </p>
 * <p>
 * 본 패키지의 코드 스타일에 맞게, 외부 메소드를 제외한 일부 명칭이 변경되었음.
 * </p>
 * <p>
 * <b>원 소스코드의 저작권은 한나눔에 있음.</b>
 * </p>
 * 
 * @author 김부근 (수정)
 * @since 2014-08-11
 * @version 0.2.2.4
 */
public class SafeChartMorphAnalyzer implements MorphAnalyzer {
	/** 이 플러그인의 명칭 */
	final static private String PLUG_IN_NAME = "MorphAnalyzer";

	/** 예외 처리를 위한 LOGGER */
	final private static Logger LOGGER = Logger.getAnonymousLogger();

	/** 기 분석된 사전 */
	private AnalyzedDic analyzedDic = null;

	/** 사용자 정의 사전 */
	private Trie userDic = new Trie(Trie.DEFAULT_TRIE_BUF_SIZE_USER);

	/** 형태소 표기 집합 */
	private TagSet tagSet = new TagSet();

	/** 격자(Lattice) 형태의 형태소 표(Chart) */
	private MorphemeChart chart = null;

	/** 결과로 돌려받을 어절 목록 */
	private LinkedList<Eojeol> eojeolList = null;

	/** 예외규칙 처리를 위한 후처리기 */
	private PostProcessor postProc = null;

	/**
	 * 이 플러그인의 명칭을 넘겨줌.
	 * 
	 * @return 이 플러그인의 명칭.
	 */
	public String getName() {
		return PLUG_IN_NAME;
	}

	/**
	 * 평문 어절을 분석하거나 기 분석 사전에서 찾아서 형태소 분석을 실시한다. It processes the input plain
	 * eojeol by analyzing it or searching the pre-analyzed dictionary.
	 * 
	 * @param plainEojeol
	 *            - plain eojeol to analyze (분석할 어절)
	 * @return the morphologically analyzed eojeol list (형태소 분석된 형태의 어절)
	 */
	private Eojeol[] processEojeol(String plainEojeol) {
		String analysis = analyzedDic.get(plainEojeol);

		eojeolList.clear();

		if (analysis != null) {
			// the eojeol was registered in the pre-analyzed dictionary
			StringTokenizer st = new StringTokenizer(analysis, "^");
			while (st.hasMoreTokens()) {
				String analyzed = st.nextToken();
				String[] tokens = analyzed.split("\\+|/");

				String[] morphemes = new String[tokens.length / 2];
				String[] tags = new String[tokens.length / 2];

				for (int i = 0, j = 0; i < morphemes.length; i++) {
					if (j < tokens.length - 1) {
						// 처리하지 않은 예외 처리
						morphemes[i] = tokens[j++];
						tags[i] = tokens[j++];
					} else {
						LOGGER.warning("Mismatch between the length of tokens and the number of morphemes and tags.");
						break;
					}
				}
				Eojeol eojeol = new Eojeol(morphemes, tags);
				eojeolList.add(eojeol);
			}
		} else {
			// (추가) 분석과정에서 발생하는 오류를 방지하기 위해서, chart.analyze에 오류가 발생할 경우, 분석하려는
			// 어절을 nqq로 설정한다.
			try {
				// analyze the input plain eojeol
				chart.init(plainEojeol);
				chart.analyze();
				chart.getResult();
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Exception from word " + plainEojeol,
						e);
				eojeolList.clear();
				eojeolList.add(new Eojeol(new String[] { plainEojeol },
						new String[] { "nqq" }));
			}
		}

		return eojeolList.toArray(new Eojeol[0]);
	}

	/**
	 * 지정된 평문을 분석하고, 가능한 분석 결과를 돌려준다. Analyzes the specified plain sentence, and
	 * returns all the possible analysis results.
	 * 
	 * @param ps
	 *            평문 분석 결과
	 * @return all the possible morphological analysis results (가능한 모든 형태소 분석
	 *         결과)
	 */
	@Override
	public SetOfSentences morphAnalyze(PlainSentence ps) {
		StringTokenizer st = new StringTokenizer(ps.getSentence(), " \t");

		String plainEojeol = null;
		int eojeolNum = st.countTokens();

		ArrayList<String> plainEojeolArray = new ArrayList<String>(eojeolNum);
		ArrayList<Eojeol[]> eojeolSetArray = new ArrayList<Eojeol[]>(eojeolNum);

		while (st.hasMoreTokens()) {
			plainEojeol = st.nextToken();

			plainEojeolArray.add(plainEojeol);
			eojeolSetArray.add(processEojeol(plainEojeol));
		}

		SetOfSentences sos = new SetOfSentences(ps.getDocumentID(),
				ps.getSentenceID(), ps.isEndOfDocument(), plainEojeolArray,
				eojeolSetArray);

		sos = postProc.doPostProcessing(sos);

		return sos;
	}

	/**
	 * 형태소 분석기를 초기화한다. Initializes the Chart-based Morphological Analyzer
	 * plug-in.
	 * 
	 * @param configFile
	 *            - the path for the configuration file (relative path to the
	 *            base directory) (base 디렉터리에서, 설정 파일의 상대적 위치)
	 * @param baseDir
	 *            - the path for base directory, which should have the 'conf'
	 *            and 'data' directory (conf와 data 디렉터리가 있는 위치)
	 * @throws Exception
	 *             설정 실패시 발생
	 */
	@Override
	public void initialize(String configFile, String baseDir) throws Exception {
		if (baseDir.length() == 0) {
			baseDir = ".";
		}

		JSONReader json = new JSONReader(configFile);

		String fileDicSystem = baseDir + "/" + json.getValue("dic_system");
		String fileConnections = baseDir + "/" + json.getValue("connections");
		String fileConnectionsNot = baseDir + "/"
				+ json.getValue("connections_not");
		String fileDicAnalyzed = baseDir + "/" + json.getValue("dic_analyzed");
		String fileTagSet = baseDir + "/" + json.getValue("tagset");

		tagSet.init(fileTagSet, TagSet.TAG_SET_KAIST);

		Connection connection = new Connection();
		connection.init(fileConnections, tagSet.getTagCount(), tagSet);

		ConnectionNot connectionNot = new ConnectionNot();
		connectionNot.init(fileConnectionsNot, tagSet);

		analyzedDic = new AnalyzedDic();
		analyzedDic.readDic(fileDicAnalyzed);

		Trie systemDic = new Trie(Trie.DEFAULT_TRIE_BUF_SIZE_SYS);
		systemDic.read_dic(fileDicSystem, tagSet);

		NumberAutomata numAutomata = new NumberAutomata();
		Simti simti = new Simti();
		simti.init();
		eojeolList = new LinkedList<Eojeol>();

		chart = new MorphemeChart(tagSet, connection, systemDic, userDic,
				numAutomata, simti, eojeolList);

		postProc = new PostProcessor();
	}

	/**
	 * 작업흐름(Workflow)이 닫히기 전에 수행할 작업.
	 */
	@Override
	public void shutdown() {
	}

	/**
	 * 초기화 작업
	 * 
	 * @param configFile
	 *            설정 파일의 위치
	 * @throws Exception
	 *             설정 실패시 발생
	 */
	@Override
	public void initialize(String configFile) throws Exception {
		initialize(configFile, Configuration.hanBaseDir);
	}

	/**
	 * (프로그래밍 가능한 사용자 정의 사전을 위해 추가된 메소드) 형태소를 사용자 정의 사전에 추가한다.
	 * 
	 * @param morph
	 *            추가할 형태소
	 * @param tag
	 *            형태소의 품사 구분
	 */
	public void addMorpheme(String morph, String tag) {
		char[] codes = Code.toTripleArray(morph);
		INFO info = userDic.new INFO();
		info.tag = tagSet.getTagID(TagConverter.toOriginalTag(tag,
				TaggerType.HNN));
		info.phoneme = TagSet.PHONEME_TYPE_ALL;

		userDic.store(codes, info);
	}
}
