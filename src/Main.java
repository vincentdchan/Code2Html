import Model.Configuration;
import Model.IResultGetter;
import Model.Java2Html;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by duzhong on 17-3-15.
 */

public class Main implements IResultGetter {

    public static void main(String[] args) throws IOException {

        if (args.length > 0) {
            String filename = args[0];

            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("File " + filename + " not found.");
                System.exit(1);
            }

            String _name = file.getName();
            String _code = new String(
                    Files.readAllBytes(
                            Paths.get(file.getAbsolutePath())));
            Configuration config = new Configuration();
            Java2Html converter = new Java2Html();
            converter.set_config(config);
            converter.addGetter(new Main());
            converter.convert(filename, _code);

            return;
        }

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
