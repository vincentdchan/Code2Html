import Model.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by duzhong on 17-3-15.
 */

public class Main {

    public static void main(String[] args) throws IOException, SyncGenerator.NotSupportedFiletypes {

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
            SyncGenerator generator = new SyncGenerator();
            generator.setConfig(config);
            System.out.println(generator.convert(_name, _code));

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
        config.set_styleName("3024_Day");

        SyncGenerator generator = new SyncGenerator(config);
        System.out.println(generator.convert("test.c", CCode));

    }

}
