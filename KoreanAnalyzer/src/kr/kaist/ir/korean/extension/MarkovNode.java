package kr.kaist.ir.korean.extension;

import kaist.cilab.jhannanum.common.Eojeol;

/**
 * Node for the markov model.(Markov 모델의 꼭짓점)
 * 
 * @author Sangwon Park (hudoni@world.kaist.ac.kr), CILab, SWRC, KAIST
 */
class MarkovNode {
	/**
	 * Markov Node 생성자
	 * 
	 * @param eojeol
	 *            어절
	 * @param tag
	 *            어절표지
	 * @param probability
	 *            확률값
	 */
	public MarkovNode(Eojeol eojeol, String tag, double probability) {
		super();
		this.eojeol = eojeol;
		this.tag = tag;
		this.ptProb = probability;
		this.cmProb = 0;
		this.backward = null;
		this.sibling = null;
	}

	/** eojeol (어절) */
	private Eojeol eojeol;

	/** eojeol tag (어절 표지) */
	private String tag;

	/** the probability of this node - P(T, W) (이 꼭짓점의 확률값) */
	private double ptProb;

	/**
	 * the accumulated probability from start to this node (시작 꼭짓점에서부터 여기까지의
	 * 누적 확률값)
	 */
	private double cmProb;

	/** back pointer for viterbi algorithm (Viterbi 알고리즘을 위한 회귀 포인터) */
	private MarkovNode backward;

	/** the index for the next sibling (다음 형제의 index) */
	private MarkovNode sibling;

	/**
	 * @return the eojeol
	 */
	public Eojeol getEojeol() {
		return eojeol;
	}

	/**
	 * @param eojeol the eojeol to set
	 */
	public void setEojeol(Eojeol eojeol) {
		this.eojeol = eojeol;
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return the ptProb
	 */
	public double getPtProb() {
		return ptProb;
	}

	/**
	 * @param ptProb the ptProb to set
	 */
	public void setPtProb(double ptProb) {
		this.ptProb = ptProb;
	}

	/**
	 * @return the cmProb
	 */
	public double getCmProb() {
		return cmProb;
	}

	/**
	 * @param cmProb the cmProb to set
	 */
	public void setCmProb(double cmProb) {
		this.cmProb = cmProb;
	}

	/**
	 * @return the backward
	 */
	public MarkovNode getBackward() {
		return backward;
	}

	/**
	 * @param backward the backward to set
	 */
	public void setBackward(MarkovNode backward) {
		this.backward = backward;
	}

	/**
	 * @return the sibling
	 */
	public MarkovNode getSibling() {
		return sibling;
	}

	/**
	 * @param sibling the sibling to set
	 */
	public void setSibling(MarkovNode sibling) {
		this.sibling = sibling;
	}
}