import Model.Configuration;
import Model.Generator;
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

    public static void main(String[] args) throws IOException, Generator.NotSupportedFiletypes {

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
            converter.convert(_name, _code);

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

        String CCode = "#include <stdio.h>\n" +
                "\n" +
                "int main() {\n" +
                "   printf(\"Hello world\");\n" +
                "   return 0;\n" +
                "}\n";

        Configuration config = new Configuration();
        Java2Html converter = new Java2Html();
        converter.set_config(config);
        converter.addGetter(new Main());
        // converter.convert("test.java", javaCode);
        converter.convert("test.c", CCode);

    }

    @Override
    public void getResult(String result) {
        System.out.println(result);
    }

}
