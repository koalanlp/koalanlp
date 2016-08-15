package kr.bydelta.koala.kkma;

import kr.bydelta.koala.POS$;
import kr.bydelta.koala.traits.CanCompileDict;

import java.util.List;

/**
 * 자바 사용자를 위한, 사용자정의사전.
 * <p>
 * POS$.Value 클래스는, POS.EC()와 같이 입력하면 생성됩니다.
 */
@SuppressWarnings("WeakerAccess")
public class JavaDictionary {
    /**
     * 사용자 정의 사전을 입력합니다.
     *
     * @param morphs 입력할 형태소의 목록.
     * @param pos    형태소별 품사의 목록. POS.NNG(), POS.VV() 등.
     */
    @SuppressWarnings("unused")
    public static void addUserDictionary(List<String> morphs, List<POS$.Value> pos) {
        get().jUserDictionary(morphs, pos);
    }

    /**
     * Wrapper method for Scala Dictionary object
     *
     * @return Dictionary object
     */
    @SuppressWarnings("SameReturnValue")
    public static CanCompileDict get() {
        return Dictionary$.MODULE$;
    }
}
