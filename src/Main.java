import Model.Configuration;
import Model.IResultGetter;
import Model.Java2Html;

/**
 * Created by duzhong on 17-3-15.
 */

public class Main implements IResultGetter {

    public static void main(String[] args) {

        String javaCode  = "/**\n" +
                " * Created by duzhong on 17-3-15.\n" +
                " */\n" +
                "\n" +
                "public class Main {\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "       System.out.println(\"Hello world.\");\n" +
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
