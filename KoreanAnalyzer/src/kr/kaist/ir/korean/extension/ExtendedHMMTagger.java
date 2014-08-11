package kr.kaist.ir.korean.extension;

import kaist.cilab.jhannanum.common.Eojeol;
import kaist.cilab.jhannanum.common.communication.Sentence;
import kaist.cilab.jhannanum.common.communication.SetOfSentences;
import kaist.cilab.jhannanum.plugin.major.postagger.impl.HMMTagger;

/**
 * 한나눔 POSTagger의 예외 처리를 위해 확장한다.
 * 
 * @author 김부근
 *
 */
public class ExtendedHMMTagger extends HMMTagger {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kaist.cilab.jhannanum.plugin.major.postagger.impl.HMMTagger#tagPOS(kaist
	 * .cilab.jhannanum.common.communication.SetOfSentences)
	 */
	@Override
	public Sentence tagPOS(SetOfSentences sos) {
		try {
			return super.tagPOS(sos);
		} catch (Exception e) {
			String sentence = sos.toString().replaceAll("\t[^\n]+\n", "")
					.replaceAll("\\s+", " ");
			System.out.println("Exception occurred at " + sentence);
			e.printStackTrace();

			Sentence s = new Sentence(0, 0, false);
			String[] strings = sentence.split("\\s+");
			Eojeol[] eojeols = new Eojeol[strings.length];

			for (int i = 0; i < strings.length; i++) {
				String str = strings[i];
				eojeols[i] = new Eojeol(new String[] { str },
						new String[] { "nqq" });
			}
			
			s.setEojeols(eojeols);
			s.setPlainEojeols(strings);

			return s;
		}
	}

}
