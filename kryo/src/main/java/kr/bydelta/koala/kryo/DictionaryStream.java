package kr.bydelta.koala.kryo;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import kr.bydelta.koala.POS;
import kr.bydelta.koala.POS$;
import kr.bydelta.koala.traits.CanCompileDict;
import scala.Tuple2;
import scala.collection.JavaConversions;

import java.io.*;
import java.util.LinkedList;
import java.util.Set;

/**
 * class for dictionary I/O function.
 */
@SuppressWarnings("CanBeFinal")
public class DictionaryStream {
    /**
     * Dictionary instance
     */
    private CanCompileDict dict;

    /**
     * Create IOStream from dictionary
     *
     * @param dict target dictionary
     */
    public DictionaryStream(CanCompileDict dict) {
        this.dict = dict;
    }

    /**
     * Save this dictionary to given file.
     *
     * @param file Target file
     */
    public void saveTo(File file) throws FileNotFoundException {
        saveTo(new FileOutputStream(file));
    }

    /**
     * Save this dictionary to given stream.
     *
     * @param stream Target stream
     */
    @SuppressWarnings("WeakerAccess")
    public void saveTo(OutputStream stream) {
        Output output = new Output(stream);

        Set<Tuple2<String, POS$.Value>> list = JavaConversions.setAsJavaSet(dict.items());
        output.writeInt(list.size());

        for (Tuple2<String, POS$.Value> item : list) {
            String surface = item._1();
            int id = item._2().id();

            output.writeString(surface);
            output.writeInt(id);
        }

        output.close();
    }

    /**
     * Read the dictionary from given stream
     *
     * @param stream Source stream
     */
    @SuppressWarnings("WeakerAccess")
    public void readFrom(InputStream stream) {
        Input input = new Input(stream);

        int sz = input.readInt();
        LinkedList<String> morphemes = new LinkedList<>();
        LinkedList<POS$.Value> poses = new LinkedList<>();

        for (int i = 0; i < sz; i++) {
            String surface = input.readString();
            POS$.Value pos = POS.apply(input.readInt());

            morphemes.add(surface);
            poses.add(pos);
        }

        input.close();
        dict.jUserDictionary(morphemes, poses);
    }

    /**
     * Read the dictionary from given file.
     *
     * @param file Source file
     */
    public void readFrom(File file) throws FileNotFoundException {
        readFrom(new FileInputStream(file));
    }
}
