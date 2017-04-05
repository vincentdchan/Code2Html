package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Created by ZMYang on 2017/4/1.
 */
public class Getter implements IResultGetter {
    String fileName;

    public Getter(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void getResult(String result) {
        File file = new File(fileName+".html");
        try {
            PrintStream out = new PrintStream(file);
            out.print(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
