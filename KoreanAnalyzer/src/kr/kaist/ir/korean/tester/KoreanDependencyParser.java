package kr.kaist.ir.korean.tester;

import java.util.Scanner;

import kr.kaist.ir.korean.data.TaggedSentence;
import kr.kaist.ir.korean.parser.IntegratedParser;
import kr.kaist.ir.korean.parser.Parser;

/**
 * Parser 시험용 클래스. 다음과 같이 코딩되어 있다.
 * 
 * <pre>
 * public class KoreanDependencyParser {
 * 
 * 	public static void main(String[] args) {
 * 		Parser iParser = null;
 * 		try {
 * 			iParser = new IntegratedParser();
 * 		} catch (Exception e) {
 * 			e.printStackTrace();
 * 		}
 * 
 * 		// 명령행 인자가 아닌, 터미널에서 직접 문장단위로 입력받음.
 * 		Scanner scanner = new Scanner(System.in);
 * 		String text;
 * 
 * 		System.out.println(&quot;Input a sentence to parse.&quot;);
 * 		while ((text = scanner.nextLine()) != null) {
 * 			// 각 Tagger가 분석한 다음 그 결과를 출력.
 * 			try {
 * 				System.out.println(&quot;대상 문장 : &quot; + text + &quot;\n&quot;);
 * 
 * 				// 실질적인 분석 과정은 한 문장.
 * 				TaggedSentence s = iParser.dependencyOf(text);
 * 				show(s, &quot;통합&quot;);
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
 * 	private static void show(TaggedSentence s, String string) {
 * 		System.out
 * 				.println(&quot;\n\n==================================================\n\n&quot;);
 * 		System.out.println(string + &quot;의 분석 결과입니다.\n&quot; + s.toString());
 * 	}
 * }
 * </pre>
 * 
 * @author 김부근
 * @version 0.2.0
 * @since 2014-08-05
 */
public class KoreanDependencyParser {
	
	/**
	 * Parser 시험을 위해 시작하는 메인 함수
	 * 
	 * @param args
	 *            명령행 인자 (사용하지 않음)
	 */
	public static void main(String[] args) {
		Parser iParser = null;
		try {
			iParser = new IntegratedParser();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 명령행 인자가 아닌, 터미널에서 직접 문장단위로 입력받음.
		Scanner scanner = new Scanner(System.in);
		String text;

		System.out.println("Input a sentence to parse.");
		while ((text = scanner.nextLine()) != null) {
			// 각 Tagger가 분석한 다음 그 결과를 출력.
			try {
				System.out.println("대상 문장 : " + text + "\n");

				// 실질적인 분석 과정은 한 문장.
				TaggedSentence s = iParser.dependencyOf(text);
				show(s, "통합");
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
