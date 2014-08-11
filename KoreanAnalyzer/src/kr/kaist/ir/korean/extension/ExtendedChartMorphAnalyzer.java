package kr.kaist.ir.korean.extension;

import kaist.cilab.jhannanum.common.Eojeol;
import kaist.cilab.jhannanum.common.communication.PlainSentence;
import kaist.cilab.jhannanum.common.communication.SetOfSentences;
import kaist.cilab.jhannanum.plugin.major.morphanalyzer.impl.ChartMorphAnalyzer;

/**
 * 한나눔의 형태소 분석기의 예외 처리를 위해 확장한다.
 * 
 * @author 김부근
 *
 */
public class ExtendedChartMorphAnalyzer extends ChartMorphAnalyzer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kaist.cilab.jhannanum.plugin.major.morphanalyzer.impl.ChartMorphAnalyzer
	 * #morphAnalyze(kaist.cilab.jhannanum.common.communication.PlainSentence)
	 */
	@Override
	public SetOfSentences morphAnalyze(PlainSentence ps) {
		try {
			return super.morphAnalyze(ps);
		} catch (Exception e) {
			String sentence = ps.getSentence();
			System.out.println("Exception occurred at " + sentence);
			e.printStackTrace();

			SetOfSentences sos = new SetOfSentences(0, 0, false);
			String[] strings = sentence.split("\\s+");

			for (String str : strings) {
				sos.addEojeolSet(new Eojeol[] { new Eojeol(
						new String[] { str }, new String[] { "nqq" }) });
				sos.addPlainEojeol(str);
			}
			
			return sos;
		}
	}

}
