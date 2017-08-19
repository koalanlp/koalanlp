package kr.bydelta.koala.cross;

import kr.bydelta.koala.data.Sentence;
import kr.bydelta.koala.kkma.Parser;
import kr.bydelta.koala.kmr.Tagger;
import kr.bydelta.koala.twt.SentenceSplitter;

import java.util.List;
import java.util.Scanner;

@SuppressWarnings("WeakerAccess")
public class JTwtKomoranKKMA {

    public static void main(String[] args) {
        /* Tagger & Parser 복합사용법 */
        SentenceSplitter splitter = new SentenceSplitter();
        Tagger tagger = new Tagger();
        Parser parser = new Parser();
        String line;
        Scanner scan = new Scanner(System.in);

        do {
            System.out.print("Input a sentence >> ");
            line = scan.nextLine();
            if (!line.isEmpty()) {
                // 문장을 구분합니다.
                List<String> paragraph = splitter.jSentences(line);

                for (String sent : paragraph) {
                    // 이제, 아래와 같이 품사분석을 진행합니다.
                    List<Sentence> sentences = tagger.jTag(sent);

                    for (Sentence tagged : sentences) {
                        // 아래와 같이, 의존관계분석을 진행합니다.
                        // Sentence 객체를 넣었으므로, 해당 객체에 변경사항이 기록됩니다.
                        parser.parse(tagged);

                        System.out.println(tagged.treeString());
                        System.out.println();
                    }
                }
            }
        } while (!line.isEmpty());
    }
}
