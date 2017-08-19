package kr.bydelta.koala.single;

import kr.bydelta.koala.POS;
import kr.bydelta.koala.POS$;
import kr.bydelta.koala.data.Sentence;
import kr.bydelta.koala.hnn.JavaDictionary;
import kr.bydelta.koala.hnn.Parser;
import kr.bydelta.koala.hnn.Tagger;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("WeakerAccess")
public class JHannanum {
    public static void main(String[] args) {
        /* 사용자정의사전 사용법 */
        // 1. 형태소 기본형 추가.
        List<String> list = new LinkedList<>();
        list.add("설빙");
        list.add("구글하");

        // 2. 기본형별 품사 지정.
        List<POS$.Value> pos = new LinkedList<>();
        pos.add(POS.NNG());
        pos.add(POS.VV());

        // 3. JavaDictionary에 추가.
        JavaDictionary.addUserDictionary(list, pos);

        /* Tagger & Parser 사용법 */
        Tagger tagger = new Tagger();
        Parser parser = new Parser();
        String line;
        Scanner scan = new Scanner(System.in);

        do {
            System.out.print("Input a sentence >> ");
            line = scan.nextLine();
            if (!line.isEmpty()) {
                // 문단 단위를 Tagging하는 경우:
                List<Sentence> paragraph = tagger.jTag(line);
                // 1개 문장인 경우는, 아래와 같습니다.
                // Sentence sent = tagger.tagSentence(line);
                // 문장이 1개라면,바로 의존관계분석을 할 수도 있습니다.
                // Sentence sent = parser.parse(line);

                for (Sentence sent : paragraph) {
                    // 아래와 같이, 의존관계분석을 진행합니다.
                    // Sentence 객체를 넣었으므로, 해당 객체에 변경사항이 기록됩니다.
                    parser.parse(sent);

                    System.out.println(sent.singleLineString());
                    System.out.println();
                }
            }
        } while (!line.isEmpty());
    }
}
