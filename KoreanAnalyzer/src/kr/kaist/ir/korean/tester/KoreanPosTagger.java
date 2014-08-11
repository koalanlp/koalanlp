package kr.kaist.ir.korean.tester;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import kr.kaist.ir.korean.data.TaggedSentence;
import kr.kaist.ir.korean.tagger.IntegratedTagger;
import kr.kaist.ir.korean.tagger.IntegratedTagger.ParseStructure;
import kr.kaist.ir.korean.tagger.Tagger;

/**
 * Tagger 시험용 클래스. 다음과 같이 코딩되어 있다.
 * 
 * <pre>
 * public final class KoreanPosTagger {
 * 	public static final void main(String[] args) {
 * 		// 각 Tagger 선언
 * 		Tagger iTagger = null;
 * 
 * 		// 각 Tagger 초기화
 * 		try {
 * 			iTagger = new IntegratedTagger(ParseStructure.KKMA);
 * 		} catch (Exception e) {
 * 			e.printStackTrace();
 * 		}
 * 
 * 		// 명령행 인자가 아닌, 터미널에서 직접 문장단위로 입력받음.
 * 		Scanner scanner = new Scanner(System.in);
 * 		String text;
 * 
 * 		System.out.println(&quot;Input dictionary! [Example : 세르비아/NNP, 불가리아/NNP]&quot;);
 * 		text = scanner.nextLine();
 * 		String[] words = text.split(&quot;,\\s+&quot;);
 * 		HashMap&lt;String, String&gt; dict = new HashMap&lt;String, String&gt;();
 * 
 * 		for (String word : words) {
 * 			int delimiter = word.lastIndexOf('/');
 * 			if (delimiter == -1) {
 * 				dict.put(word, &quot;NNP&quot;);
 * 			} else {
 * 				dict.put(word.substring(0, delimiter),
 * 						word.substring(delimiter + 1));
 * 			}
 * 		}
 * 		iTagger.addUserDictionary(dict);
 * 
 * 		System.out.println(&quot;Input a sentence to parse.&quot;);
 * 		while ((text = scanner.nextLine()) != null) {
 * 			// 각 Tagger가 분석한 다음 그 결과를 출력.
 * 			try {
 * 				System.out.println(&quot;대상 문장 : &quot; + text + &quot;\n&quot;);
 * 
 * 				// 실질적인 분석 과정은 한 문장.
 * 				// 사전을 사용할 경우
 * 				// TaggedSentence s = iTagger.analyzeSentence(text, dict);
 * 				// 사전을 사용하지 않을 경우
 * 				// TaggedSentence s = iTagger.analyzeSentence(text);
 * 				// show(s, &quot;통합&quot;);
 * 				// 문단 단위로 하고 싶은 경우,
 * 				LinkedList&lt;TaggedSentence&gt; p = iTagger.analyzeParagraph(text,
 * 						dict);
 * 				for (TaggedSentence s : p) {
 * 					show(s, &quot;통합&quot;);
 * 				}
 * 
 * 			} catch (Exception e) {
 * 				e.printStackTrace();
 * 			}
 * 
 * 			System.out.println(&quot;Input a sentence to parse.&quot;);
 * 		}
 * 
 * 		// 사용한 Tagger와 Scanner 정리.
 * 		scanner.close();
 * 	}
 * 
 * 	private static void show(Sentence s, String string) {
 * 		System.out
 * 				.println(&quot;\n\n==================================================\n\n&quot;);
 * 		System.out.println(string + &quot;의 분석 결과입니다.\n&quot; + s.toString());
 * 	}
 * }
 * </pre>
 * 
 * @author 김부근
 * @version 0.2.2.4
 * @since 2014-08-04
 */
public final class KoreanPosTagger {

	/**
	 * Tagger 시험을 위해 시작하는 메인 함수
	 * 
	 * @param args
	 *            명령행 인자 (사용하지 않음)
	 */
	public static final void main(String[] args) {
		// 각 Tagger 선언
		Tagger iTagger = null;

		// 각 Tagger 초기화
		try {
			iTagger = new IntegratedTagger(ParseStructure.KKMA);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 명령행 인자가 아닌, 터미널에서 직접 문장단위로 입력받음.
		Scanner scanner = new Scanner(System.in);
		String text;

		System.out.println("Input dictionary! [Example : 세르비아/NNP, 불가리아/NNP]");
		text = scanner.nextLine();
		String[] words = text.split("[,;]{1}\\s*");
		HashMap<String, String> dict = new HashMap<String, String>();

		for (String word : words) {
			int delimiter = word.lastIndexOf('/');
			if (delimiter == -1) {
				System.out.println(word + "=NNP");
				dict.put(word, "NNP");
			} else {
				dict.put(word.substring(0, delimiter),
						word.substring(delimiter + 1));
				System.out.println(word.substring(0, delimiter) + "="
						+ word.substring(delimiter + 1));
			}
		}
		iTagger.addUserDictionary(dict);

		System.out.println("Input a sentence to parse.");
		while ((text = scanner.nextLine()) != null) {
			// 각 Tagger가 분석한 다음 그 결과를 출력.
			try {
				System.out.println("대상 문장 : " + text + "\n");

				// 실질적인 분석 과정은 한 문장.
				// 사전을 사용할 경우
				// TaggedSentence s = iTagger.analyzeSentence(text, dict);
				// 사전을 사용하지 않을 경우
				// TaggedSentence s = iTagger.analyzeSentence(text);
				// show(s, "통합");
				// 문단 단위로 하고 싶은 경우,
				LinkedList<TaggedSentence> p = iTagger.analyzeParagraph(text);
				for (TaggedSentence s : p) {
					show(s, "통합");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("Input a sentence to parse.");
		}

		// 사용한 Tagger와 Scanner 정리.
		scanner.close();
	}

	/**
	 * 시험 결과 출력
	 * 
	 * @param s
	 *            분석된, 출력할 문장
	 * @param string
	 *            Tagger의 이름
	 */
	private static void show(TaggedSentence s, String string) {
		System.out
				.println("\n\n==================================================\n\n");
		System.out.println(string + "의 분석 결과입니다.\n" + s.toString());
	}
}
