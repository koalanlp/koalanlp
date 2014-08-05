package kr.kaist.ir.korean.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 문장을 나타내는 클래스. 꼬꼬마와 한나눔의 Sentence에 해당한다.
 * </p>
 * <p>
 * 직렬화 및 순회 가능(Serializable, Iterable&lt;Word&gt;) 객체이며, 각 방법론(한나눔, 꼬꼬마, 통합형)에
 * 맞게 구현할 수 있도록 추상 클래스로 선언되었다.
 * </p>
 * 
 * @author 김부근
 * @since 2014-08-04
 * @version 0.1.2
 */
public class TaggedSentence implements Serializable, Iterable<TaggedWord> {
	/** Serial ID */
	private static final long serialVersionUID = 4683667063955634926L;

	/** 문장에 포함된 어절의 목록 */
	private final LinkedList<TaggedWord> words = new LinkedList<TaggedWord>();
	/** 구문 분석 결과를 수형도(Tree)형태로 보고자 할 때, 뿌리(Root)가 되는 어절을 지정한다. */
	private int root = -1;

	/**
	 * 문장에 어절을 추가한다.
	 * 
	 * @param word
	 *            문장의 마지막에 추가할 어절.
	 */
	public void addWord(TaggedWord word) {
		words.add(word);
	}

	/**
	 * 문장에 문장을 이어붙인다.
	 * 
	 * @param s
	 *            이 문장에 이어붙일 문장
	 */
	public void concat(TaggedSentence s) {
		this.words.addAll(s.words);
	}

	/**
	 * 인자로 전달된 형태소가 포함되어있는지 확인한다.
	 * 
	 * @param type
	 *            찾고자 하는 형태소
	 * @return 해당 형태소 포함 여부
	 */
	public final boolean hasMorphemeType(String type) {
		for (TaggedWord word : words) {
			if (word.contains(type)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * <p>
	 * 문장의 형태가 제시된 문장 형태에 부합하는지 확인한다.
	 * </p>
	 * <p>
	 * 문장의 형태는 다음의 조합이다.
	 * </p>
	 * 
	 * <pre>
	 * 어절과 어절은 띄어쓰기로 구분한다.
	 * TAG	: TAG에 부합하는 형태소 표기를 찾는다.
	 * TAG+TAG 와 같이 한 어절 내 여러 태그를 지정할 수 있다.
	 * </pre>
	 * 
	 * <p>
	 * 예를 들어 "NN NNGA+JKS VV+ECE"는 "명사 - 동작성명사 및 주격조사 - 동사 및 대등연결어미"를 나타낸다.
	 * </p>
	 * 
	 * @return 해당 문장 형태에 부합할 경우 참(true).
	 */
	public final boolean isTypeOf(String type) {
		List<String> args = Arrays.asList(type.split(" "));

		int wordindx = words.size() - 1, argsindx = args.size() - 1;

		while (wordindx > -1 && argsindx > -1) {
			TaggedWord word = words.get(wordindx);

			if (word.contains(args.get(argsindx))) {
				wordindx--;
				argsindx--;
			} else {
				wordindx = wordindx + (args.size() - argsindx) - 2;
				argsindx = args.size() - 1;
			}
		}

		return (argsindx == -1);
	}

	/**
	 * 문장의 길이를 돌려준다.
	 * 
	 * @return 문장 내 어절의 갯수를 세어 돌려준다.
	 */
	public final int size() {
		return words.size();
	}

	/**
	 * 문장의 첫번째 어절을 찾아 돌려준다.
	 * 
	 * @return 문장의 첫번째 어절
	 */
	public final TaggedWord getFirst() {
		return words.getFirst();
	}

	/**
	 * 문장의 마지막 어절을 찾아 돌려준다.
	 * 
	 * @return 문장의 마지막 어절
	 */
	public final TaggedWord getLast() {
		return words.getLast();
	}

	/**
	 * 문장에서 지정된 위치에 있는 어절을 찾아 돌려준다.
	 * 
	 * @param index
	 *            찾을 어절의 위치
	 * @return 해당 위치의 어절.
	 * @throws IndexOutOfBoundsException
	 *             어절의 위치가 0보다 작거나 문장 길이보다 길면 발생한다.
	 */
	public final TaggedWord getWordAt(int index) {
		return words.get(index);
	}

	/**
	 * 문장의 어절을 구분자로 연결하여 돌려준다.
	 * 
	 * @param delimiter
	 *            구분자. Null을 입력하면 공백으로 처리한다.
	 * @return 구분자로 연결한 원 문장.
	 */
	public final String getOriginalString(String delimiter) {
		delimiter = (delimiter == null) ? " " : delimiter;

		StringBuffer strbuf = new StringBuffer();
		for (TaggedWord w : this) {
			strbuf.append(w.getOriginalWord());
			strbuf.append(delimiter);
		}

		return strbuf.toString();
	}

	/**
	 * 단어 목록을 초기화한다.
	 */
	public final void clear() {
		words.clear();
	}

	/**
	 * 문장의 단어를 순회하는 순회 객체를 반환한다.
	 */
	@Override
	public Iterator<TaggedWord> iterator() {
		return words.iterator();
	}

	/**
	 * 문장을 문자열 형태로 만들어 반환한다.
	 */
	@Override
	public String toString() {
		StringBuffer strbuf = new StringBuffer();
		for (int i = 0; i < words.size(); i++) {
			TaggedWord w = words.get(i);
			strbuf.append(w.toString());
			if(i == root){
				strbuf.append("[ROOT]");
			}
			strbuf.append('\n');
		}

		return strbuf.toString();
	}

	/**
	 * 구문 분석 결과의 뿌리를 돌려준다.
	 * 
	 * @return 구문 분석 결과의 뿌리(root)
	 */
	public TaggedWord getRoot() {
		return words.get(root);
	}

	/**
	 * 구문 분석 결과의 뿌리를 설정한다.
	 * 
	 * @param index
	 *            구문 분석 결과의 뿌리(root)로 설정할 어절의 위치
	 */
	public void setRoot(int index) {
		this.root = index;
	}
}
