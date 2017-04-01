import Model.Configuration;
import Model.IResultGetter;
import Model.Java2Html;

import java.io.IOException;

/**
 * Created by duzhong on 17-3-15.
 */

public class Main implements IResultGetter {

    public static void main(String[] args) throws IOException {

        String javaCode  = "/**\n" +
                " * Created by duzhong on 17-3-15.\n" +
                " */\n" +
                "\n" +
                "public class Main {\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "       System.out.println(\"Hello world.\");   // this is an comment\n" +
                "    }\n" +
                "\n" +
                "}";

        Configuration config = new Configuration();
        Java2Html converter = new Java2Html();
        converter.set_config(config);
        converter.addGetter(new Main());
        converter.convert("test.java", javaCode);

    }

    @Override
    public void getResult(String result) {
        System.out.println(result);
    }

}
