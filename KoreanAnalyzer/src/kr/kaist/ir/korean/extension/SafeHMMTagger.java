package kr.kaist.ir.korean.extension;

import java.util.ArrayList;
import java.util.LinkedList;

import kaist.cilab.jhannanum.common.Eojeol;
import kaist.cilab.jhannanum.common.JSONReader;
import kaist.cilab.jhannanum.common.communication.Sentence;
import kaist.cilab.jhannanum.common.communication.SetOfSentences;
import kaist.cilab.jhannanum.plugin.major.postagger.PosTagger;
import kaist.cilab.jhannanum.postagger.hmmpostagger.PhraseTag;
import kaist.cilab.jhannanum.postagger.hmmpostagger.ProbabilityDBM;
import kaist.cilab.parser.berkeleyadaptation.Configuration;

/**
 * <p>
 * 한나눔 0.8.4 버전의 HMMTagger 클래스를, 프로그래밍 가능한 사용자 정의 사전을 적용할 수 있도록 변형한 클래스.
 * </p>
 * <p>
 * 본 패키지의 코드 스타일에 맞게, 외부 메소드를 제외한 일부 명칭이 변경되었음.
 * </p>
 * <p>
 * <b>원 소스코드의 저작권은 한나눔에 있음.</b>
 * </p>
 * 
 * @author 김부근 (수정)
 * @since 2014-08-12
 * @version 0.2.2.4
 */
public class SafeHMMTagger implements PosTagger {
	/** log 0.01 - smoothing factor */
	private static double SF = -4.60517018598809136803598290936873;

	/** the list of nodes for each eojeol */
	private LinkedList<MarkovNode> wordPts = new LinkedList<MarkovNode>();

	/** the nodes for the markov model */
	private LinkedList<MarkovNode> markovNet = new LinkedList<MarkovNode>();

	/** for the probability P(W|T) */
	private ProbabilityDBM pwtPOS = null;

	/** for the probability P(T|T) */
	private ProbabilityDBM pttPOS = null;

	/** for the probability P(T|T) for eojeols */
	private ProbabilityDBM pttWP = null;

	/** the default probability */
	final static private double PCONSTANT = -20.0;

	@Override
	public Sentence tagPOS(SetOfSentences sos) {
		ArrayList<Eojeol[]> eojeolSetArray = sos.getEojeolSetArray();

		// initialization
		reset();

		for (Eojeol[] eojeolSet : eojeolSetArray) {
			MarkovNode prev = null;
			for (int i = 0; i < eojeolSet.length; i++) {
				Eojeol e = eojeolSet[i];
				String now_tag = PhraseTag.getPhraseTag(e.getTags());
				double probability = computeWT(e);

				MarkovNode node = new MarkovNode(e, now_tag, probability);
				markovNet.add(node);
				if (prev == null) {
					wordPts.add(node);
				} else {
					prev.setSibling(node);
				}
				prev = node;
			}
		}

		// gets the final result by running viterbi
		return endSentence(sos);
	}

	@Override
	public void initialize(String configFile, String baseDir) throws Exception {
		if (baseDir.length() == 0) {
			baseDir = ".";
		}

		wordPts.clear();
		markovNet.clear();

		JSONReader json = new JSONReader(configFile);
		String PWT_POS_TDBM_FILE = baseDir + "/" + json.getValue("pwt.pos");
		String PTT_POS_TDBM_FILE = baseDir + "/" + json.getValue("ptt.pos");
		String PTT_WP_TDBM_FILE = baseDir + "/" + json.getValue("ptt.wp");

		pwtPOS = new ProbabilityDBM(PWT_POS_TDBM_FILE);
		pttWP = new ProbabilityDBM(PTT_WP_TDBM_FILE);
		pttPOS = new ProbabilityDBM(PTT_POS_TDBM_FILE);
	}

	@Override
	public void shutdown() {

	}

	/**
	 * Computes P(T_i, W_i) of the specified eojeol.
	 * 
	 * @param eojeol
	 *            - the eojeol to compute the probability
	 * @return P(T_i, W_i) of the specified eojeol
	 */
	private double computeWT(Eojeol eojeol) {
		double current = 0.0, tbigram, tunigram, lexicon;

		String tag = eojeol.getTag(0);
		String bitag = "bnk-" + tag;
		String oldtag;

		/* the probability of P(t1|t0) */
		double[] prob = null;

		if ((prob = pttPOS.get(bitag)) != null) {
			/* current = P(t1|t0) */
			tbigram = prob[0];
		} else {
			/* current = P(t1|t0) = 0.01 */
			tbigram = PCONSTANT;
		}

		/* the probability of P(t1) */
		if ((prob = pttPOS.get(tag)) != null) {
			/* current = P(t1) */
			tunigram = prob[0];
		} else {
			/* current = P(t1) = 0.01 */
			tunigram = PCONSTANT;
		}

		/* the probability of P(w|t) */
		if ((prob = pwtPOS.get(eojeol.getMorpheme(0) + "/" + tag)) != null) {
			/* current *= P(w|t1) */
			lexicon = prob[0];
		} else {
			/* current = P(w|t1) = 0.01 */
			lexicon = PCONSTANT;
		}

		/*
		 * current = P(w|t1) * P(t1|t0) ~= P(w|t1) * (P(t1|t0))^Lambda1 *
		 * (P(t1))^Lambda2 (Lambda1 + Lambda2 = 1)
		 */
		// current = lexicon + Lambda1*tbigram + Lambda2*tunigram;

		/*
		 * current = P(w|t1)/P(t1) * P(t1|t0)/P(t1)
		 */
		// current = lexicon - tunigram + tbigram - tunigram;

		/*
		 * current = P(w|t1) * P(t1|t0)
		 */
		// current = lexicon + tbigram ;

		/*
		 * current = P(w|t1) * P(t1|t0) / P(t1)
		 */
		current = lexicon + tbigram - tunigram;
		oldtag = tag;

		for (int i = 1; i < eojeol.length; i++) {
			tag = eojeol.getTag(i);

			/* P(t_i|t_i-1) */
			bitag = oldtag + "-" + tag;

			if ((prob = pttPOS.get(bitag)) != null) {
				tbigram = prob[0];
			} else {
				tbigram = PCONSTANT;
			}

			/* P(w|t) */
			if ((prob = pwtPOS.get(eojeol.getMorpheme(i) + "/" + tag)) != null) {
				/* current *= P(w|t) */
				lexicon = prob[0];
			} else {
				lexicon = PCONSTANT;
			}

			/* P(t) */
			if ((prob = pttPOS.get(tag)) != null) {
				/* current = P(t) */
				tunigram = prob[0];
			} else {
				/* current = P(t)=0.01 */
				tunigram = PCONSTANT;
			}

			// current += lexicon - tunigram + tbigram - tunigram;
			// current += lexicon + tbigram;
			current += lexicon + tbigram - tunigram;

			oldtag = tag;
		}

		/* the blank at the end of eojeol */
		bitag = tag + "-bnk";

		/* P(bnk|t_last) */
		if ((prob = pttPOS.get(bitag)) != null) {
			tbigram = prob[0];
		} else {
			tbigram = PCONSTANT;
		}

		/* P(bnk) */
		if ((prob = pttPOS.get("bnk")) != null) {
			tunigram = prob[0];
		} else {
			tunigram = PCONSTANT;
		}

		/* P(w|bnk) = 1, and ln(1) = 0 */
		// current += 0 - tunigram + tbigram - tunigram;
		// current += 0 + tbigram;
		current += 0 + tbigram - tunigram;

		return current;
	}

	/**
	 * Runs viterbi to get the final morphological analysis result which has the
	 * highest probability.
	 * 
	 * @param sos
	 *            - all the candidates of morphological analysis
	 * @return the final morphological analysis result which has the highest
	 *         probability
	 */
	private Sentence endSentence(SetOfSentences sos) {
		MarkovNode prev, curr;

		/* Runs viterbi */
		for (int i = 0; i < wordPts.size() - 1; i++) {
			for (prev = wordPts.get(i); prev != null; prev = prev.getSibling()) {
				for (curr = wordPts.get(i + 1); curr != null; curr = curr
						.getSibling()) {
					updateProbability(prev, curr);
				}
			}
		}

		LinkedList<Eojeol> eojeols = new LinkedList<Eojeol>();
		for (curr = wordPts.getLast(); curr != null; curr = curr.getBackward()) {
			eojeols.addFirst(curr.getEojeol());
		}

		Sentence s = new Sentence(sos.getDocumentID(), sos.getSentenceID(),
				sos.isEndOfDocument(), sos.getPlainEojeolArray().toArray(
						new String[0]), eojeols.toArray(new Eojeol[0]));
		s.length = eojeols.size();

		return s;
	}

	/**
	 * Resets the model.
	 */
	private void reset() {
		wordPts.clear();
		markovNet.clear();
	}

	/**
	 * Updates the probability regarding the transition between two eojeols.
	 * 
	 * @param prev
	 *            - the previous eojeol
	 * @param curr
	 *            - the current eojeol
	 */
	private void updateProbability(MarkovNode prev, MarkovNode curr) {
		double PTT;
		double[] prob = null;
		double P;

		/* the traisition probability P(T_i,T_i-1) */
		prob = pttWP.get(prev.getTag() + "-" + curr.getTag());

		if (prob == null) {
			/* ln(0.01). Smoothing Factor */
			PTT = SF;
		} else {
			PTT = prob[0];
		}

		/* P(T_i,T_i-1) / P(T_i) */
		prob = pttWP.get(curr.getTag());

		if (prob != null) {
			PTT -= prob[0];
		}

		/* P(T_i,T_i-1) / (P(T_i) * P(T_i-1)) */
		// prob = ptt_wp_tf.get(mn[from].wp_tag);
		//
		// if (prob != null) {
		// PTT -= prob[0];
		// }

		if (prev.getBackward() == null) {
			prev.setCmProb(prev.getPtProb());
		}

		/*
		 * P = the accumulated probability to the previous eojeol * transition
		 * probability * the probability of current eojeol PTT = P(T_i|T_i-1) /
		 * P(T_i) mn[to].prob_wt = P(T_i, W_i)
		 */
		P = prev.getCmProb() + PTT + curr.getPtProb();

		// for debugging
		// System.out.format("P:%f\t%s(%d:%s):%f -> %f -> %s(%d:%s):%f\n", P,
		// mn[from].eojeol,
		// from, mn[from].wp_tag, mn[from].prob, PTT,
		// mn[to].eojeol, to, mn[to].wp_tag, mn[to].prob_wt );

		if (curr.getBackward() == null || P > curr.getCmProb()) {
			curr.setBackward(prev);
			curr.setCmProb(P);
		}
	}

	@Override
	public void initialize(String configFile) throws Exception {
		initialize(configFile, Configuration.hanBaseDir);
	}
}
