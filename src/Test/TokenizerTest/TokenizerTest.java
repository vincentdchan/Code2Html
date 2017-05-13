package Test.TokenizerTest;

import Model.ITokenizer;
import Model.LangSpec.CLang;
import Model.LangSpec.HTML;
import Test.Test;
import Model.LangSpec.JavaLang;
import Model.StringStream;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by cdzos on 2017/5/13.
 */
public class TokenizerTest extends Test  {

    public static String testJava1 = "package Test.TokenizerTest;\n" +
            "\n" +
            "import Test.Test;\n" +
            "\n" +
            "/**\n" +
            " * Created by cdzos on 2017/5/13.\n" +
            " */\n" +
            "public class TokenizerTest extends Test  {\n" +
            "\n" +
            "    public static void main(String args[]) {\n" +
            "        System.out.println(\"Test\");\n" +
            "    }\n" +
            "\n" +
            "}";

    public static Object[][] testJava1Result  = {
            {"package", "keyword", },
            {" ", },
            {"Test", "variable", },
            {".", },
            {"TokenizerTest", "variable", },
            {";", },
            {"import", "keyword", },
            {" ", },
            {"Test", "variable", },
            {".", },
            {"Test", "variable", },
            {";", },
            {"/** * Created by cdzos on 2017/5/13. */", "comment", },
            {"public", "keyword", },
            {" ", },
            {"class", "keyword", },
            {" ", },
            {"TokenizerTest", "variable", },
            {" ", },
            {"extends", "keyword", },
            {" ", },
            {"Test", "variable", },
            {"  {    ", },
            {"public", "keyword", },
            {" ", },
            {"static", "keyword", },
            {" ", },
            {"void", "keyword", },
            {" ", },
            {"main", "variable", },
            {"(", },
            {"String", "variable", },
            {" ", },
            {"args", "variable", },
            {"[]) {        ", },
            {"System", "variable", },
            {".", },
            {"out", "variable", },
            {".", },
            {"println", "variable", },
            {"(", },
            {"\"Test\"", "string", },
            {");    }}", },
    };

    public String testC1 = "/* Copyright StrongLoop, Inc. All rights reserved.\n" +
            " *\n" +
            " * Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
            " * of this software and associated documentation files (the \"Software\"), to\n" +
            " * deal in the Software without restriction, including without limitation the\n" +
            " * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or\n" +
            " * sell copies of the Software, and to permit persons to whom the Software is\n" +
            " * furnished to do so, subject to the following conditions:\n" +
            " *\n" +
            " * The above copyright notice and this permission notice shall be included in\n" +
            " * all copies or substantial portions of the Software.\n" +
            " *\n" +
            " * THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
            " * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
            " * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
            " * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
            " * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING\n" +
            " * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS\n" +
            " * IN THE SOFTWARE.\n" +
            " */\n" +
            "\n" +
            "#include \"defs.h\"\n" +
            "#include <stdarg.h>\n" +
            "#include <stdio.h>\n" +
            "#include <stdlib.h>\n" +
            "\n" +
            "static void pr_do(FILE *stream,\n" +
            "                  const char *label,\n" +
            "                  const char *fmt,\n" +
            "                  va_list ap);\n" +
            "\n" +
            "void *xmalloc(size_t size) {\n" +
            "  void *ptr;\n" +
            "\n" +
            "  ptr = malloc(size);\n" +
            "  if (ptr == NULL) {\n" +
            "    pr_err(\"out of memory, need %lu bytes\", (unsigned long) size);\n" +
            "    exit(1);\n" +
            "  }\n" +
            "\n" +
            "  return ptr;\n" +
            "}\n" +
            "\n" +
            "void pr_info(const char *fmt, ...) {\n" +
            "  va_list ap;\n" +
            "  va_start(ap, fmt);\n" +
            "  pr_do(stdout, \"info\", fmt, ap);\n" +
            "  va_end(ap);\n" +
            "}\n" +
            "\n" +
            "void pr_warn(const char *fmt, ...) {\n" +
            "  va_list ap;\n" +
            "  va_start(ap, fmt);\n" +
            "  pr_do(stderr, \"warn\", fmt, ap);\n" +
            "  va_end(ap);\n" +
            "}\n" +
            "\n" +
            "void pr_err(const char *fmt, ...) {\n" +
            "  va_list ap;\n" +
            "  va_start(ap, fmt);\n" +
            "  pr_do(stderr, \"error\", fmt, ap);\n" +
            "  va_end(ap);\n" +
            "}\n" +
            "\n" +
            "static void pr_do(FILE *stream,\n" +
            "                  const char *label,\n" +
            "                  const char *fmt,\n" +
            "                  va_list ap) {\n" +
            "  char fmtbuf[1024];\n" +
            "  vsnprintf(fmtbuf, sizeof(fmtbuf), fmt, ap);\n" +
            "  fprintf(stream, \"%s:%s: %s\\n\", _getprogname(), label, fmtbuf);\n" +
            "}";

    public static Object[][] testC1Result = {
            {"/* Copyright StrongLoop, Inc. All rights reserved. * * Permission is hereby granted, free of charge, to any person obtaining a copy * of this software and associated documentation files (the \"Software\"), to * deal in the Software without restriction, including without limitation the * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or * sell copies of the Software, and to permit persons to whom the Software is * furnished to do so, subject to the following conditions: * * The above copyright notice and this permission notice shall be included in * all copies or substantial portions of the Software. * * THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS * IN THE SOFTWARE. */", "comment", },
            {"#", "macro", },
            {"include ", "macro", "keyword", },
            {"\"defs.h\"#", "macro", "string", },
            {"include <stdarg.h>#include <stdio.h>#include <stdlib.h>", "macro", "keyword", },
            {"static", "constant-language", },
            {" ", },
            {"void", "constant-other", },
            {" pr_do(FILE *stream,                  ", },
            {"const", "constant-language", },
            {" ", },
            {"char", "constant-other", },
            {" *label,                  ", },
            {"const", "constant-language", },
            {" ", },
            {"char", "constant-other", },
            {" *fmt,                  va_list ap);", },
            {"void", "constant-other", },
            {" *xmalloc(", },
            {"size_t", "constant-other", },
            {" size) {  ", },
            {"void", "constant-other", },
            {" *ptr;  ptr ", },
            {"=", "assignment", },
            {" malloc(size);  ", },
            {"if", "keyword", },
            {" (ptr ", },
            {"==", "comparision", },
            {" ", },
            {"NULL", "constant-character", },
            {") {    pr_err(", },
            {"\"out of memory, need %lu bytes\"", "string", },
            {", (", },
            {"unsigned", "constant-other", },
            {" ", },
            {"long", "constant-other", },
            {") size);    exit(", },
            {"1", "constant-numeric", },
            {");  }  ", },
            {"return", "keyword", },
            {" ptr;}", },
            {"void", "constant-other", },
            {" pr_info(", },
            {"const", "constant-language", },
            {" ", },
            {"char", "constant-other", },
            {" *fmt, ...) {  va_list ap;  va_start(ap, fmt);  pr_do(stdout, ", },
            {"\"info\"", "string", },
            {", fmt, ap);  va_end(ap);}", },
            {"void", "constant-other", },
            {" pr_warn(", },
            {"const", "constant-language", },
            {" ", },
            {"char", "constant-other", },
            {" *fmt, ...) {  va_list ap;  va_start(ap, fmt);  pr_do(stderr, ", },
            {"\"warn\"", "string", },
            {", fmt, ap);  va_end(ap);}", },
            {"void", "constant-other", },
            {" pr_err(", },
            {"const", "constant-language", },
            {" ", },
            {"char", "constant-other", },
            {" *fmt, ...) {  va_list ap;  va_start(ap, fmt);  pr_do(stderr, ", },
            {"\"error\"", "string", },
            {", fmt, ap);  va_end(ap);}", },
            {"static", "constant-language", },
            {" ", },
            {"void", "constant-other", },
            {" pr_do(FILE *stream,                  ", },
            {"const", "constant-language", },
            {" ", },
            {"char", "constant-other", },
            {" *label,                  ", },
            {"const", "constant-language", },
            {" ", },
            {"char", "constant-other", },
            {" *fmt,                  va_list ap) {  ", },
            {"char", "constant-other", },
            {" fmtbuf[", },
            {"1024", "constant-numeric", },
            {"];  vsnprintf(fmtbuf, ", },
            {"sizeof", "sizeof", },
            {"(fmtbuf), fmt, ap);  fprintf(stream, ", },
            {"\"%s:%s: %s\\n\"", "string", },
            {", _getprogname(), label, fmtbuf);}", },
    };

    public static String testHtml1 = "<!doctype html>\n" +
            "<html class=\"no-js\" lang=\"\">\n" +
            "    <head>\n" +
            "        <meta charset=\"utf-8\">\n" +
            "        <meta http-equiv=\"x-ua-compatible\" content=\"ie=edge\">\n" +
            "        <title></title>\n" +
            "        <meta name=\"description\" content=\"\">\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "\n" +
            "        <link rel=\"apple-touch-icon\" href=\"apple-touch-icon.png\">\n" +
            "        <!-- Place favicon.ico in the root directory -->\n" +
            "\n" +
            "        <link rel=\"stylesheet\" href=\"css/normalize.css\">\n" +
            "        <link rel=\"stylesheet\" href=\"css/main.css\">\n" +
            "        <script src=\"js/vendor/modernizr-2.8.3.min.js\"></script>\n" +
            "    </head>\n" +
            "    <body>\n" +
            "        <!--[if lte IE 9]>\n" +
            "            <p class=\"browserupgrade\">You are using an <strong>outdated</strong> browser. Please <a href=\"http://browsehappy.com/\">upgrade your browser</a> to improve your experience and security.</p>\n" +
            "        <![endif]-->\n" +
            "\n" +
            "        <!-- Add your site or application content here -->\n" +
            "        <p>Hello world! This is HTML5 Boilerplate.</p>\n" +
            "        <script src=\"https://code.jquery.com/jquery-{{JQUERY_VERSION}}.min.js\" integrity=\"{{JQUERY_SRI_HASH}}\" crossorigin=\"anonymous\"></script>\n" +
            "        <script>window.jQuery || document.write('<script src=\"js/vendor/jquery-{{JQUERY_VERSION}}.min.js\"><\\/script>')</script>\n" +
            "        <script src=\"js/plugins.js\"></script>\n" +
            "        <script src=\"js/main.js\"></script>\n" +
            "\n" +
            "        <!-- Google Analytics: change UA-XXXXX-Y to be your site's ID. -->\n" +
            "        <script>\n" +
            "            window.ga=function(){ga.q.push(arguments)};ga.q=[];ga.l=+new Date;\n" +
            "            ga('create','UA-XXXXX-Y','auto');ga('send','pageview')\n" +
            "        </script>\n" +
            "        <script src=\"https://www.google-analytics.com/analytics.js\" async defer></script>\n" +
            "    </body>\n" +
            "</html>";

    public Object[][] testHtml1Result = {
            {"<!doctype html>", },
            {"<", "punctuation-definition-tag-begin", },
            {"html", "constant-character", },
            {" ", },
            {"class", "variable", },
            {"=", },
            {"\"no-js\"", "string", },
            {" ", },
            {"lang", "variable", },
            {"=\"\"", },
            {">", "punctuation-definition-tag-end", },
            {"    ", },
            {"<", "punctuation-definition-tag-begin", },
            {"head", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"meta", "constant-character", },
            {" ", },
            {"charset", "variable", },
            {"=", },
            {"\"utf-8\"", "string", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"meta", "constant-character", },
            {" ", },
            {"http-equiv", "variable", },
            {"=", },
            {"\"x-ua-compatible\"", "string", },
            {" ", },
            {"content", "variable", },
            {"=", },
            {"\"ie=edge\"", "string", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"title", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"</", "punctuation-definition-tag-begin", },
            {"title", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"meta", "constant-character", },
            {" ", },
            {"name", "variable", },
            {"=", },
            {"\"description\"", "string", },
            {" ", },
            {"content", "variable", },
            {"=\"\"", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"meta", "constant-character", },
            {" ", },
            {"name", "variable", },
            {"=", },
            {"\"viewport\"", "string", },
            {" ", },
            {"content", "variable", },
            {"=", },
            {"\"width=device-width, initial-scale=1\"", "string", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"link", "constant-character", },
            {" ", },
            {"rel", "variable", },
            {"=", },
            {"\"apple-touch-icon\"", "string", },
            {" ", },
            {"href", "variable", },
            {"=", },
            {"\"apple-touch-icon.png\"", "string", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<!-- Place favicon.ico in the root directory -->", "comment", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"link", "constant-character", },
            {" ", },
            {"rel", "variable", },
            {"=", },
            {"\"stylesheet\"", "string", },
            {" ", },
            {"href", "variable", },
            {"=", },
            {"\"css/normalize.css\"", "string", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"link", "constant-character", },
            {" ", },
            {"rel", "variable", },
            {"=", },
            {"\"stylesheet\"", "string", },
            {" ", },
            {"href", "variable", },
            {"=", },
            {"\"css/main.css\"", "string", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"script", "constant-character", },
            {" ", },
            {"src", "variable", },
            {"=", },
            {"\"js/vendor/modernizr-2.8.3.min.js\"", "string", },
            {">", "punctuation-definition-tag-end", },
            {"</", "punctuation-definition-tag-begin", },
            {"script", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"    ", },
            {"</", "punctuation-definition-tag-begin", },
            {"head", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"    ", },
            {"<", "punctuation-definition-tag-begin", },
            {"body", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<!--[if lte IE 9]>            <p class=\"browserupgrade\">You are using an <strong>outdated</strong> browser. Please <a href=\"http://browsehappy.com/\">upgrade your browser</a> to improve your experience and security.</p>        <![endif]-->", "comment", },
            {"        ", },
            {"<!-- Add your site or application content here -->", "comment", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"p", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"Hello world! This is HTML5 Boilerplate.", },
            {"</", "punctuation-definition-tag-begin", },
            {"p", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"script", "constant-character", },
            {" ", },
            {"src", "variable", },
            {"=", },
            {"\"https://code.jquery.com/jquery-{{JQUERY_VERSION}}.min.js\"", "string", },
            {" ", },
            {"integrity", "variable", },
            {"=", },
            {"\"{{JQUERY_SRI_HASH}}\"", "string", },
            {" ", },
            {"crossorigin", "variable", },
            {"=", },
            {"\"anonymous\"", "string", },
            {">", "punctuation-definition-tag-end", },
            {"</", "punctuation-definition-tag-begin", },
            {"script", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"script", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"window.jQuery || document.write('", },
            {"<", "punctuation-definition-tag-begin", },
            {"script", "constant-character", },
            {" ", },
            {"src", "variable", },
            {"=", },
            {"\"js/vendor/jquery-{{JQUERY_VERSION}}.min.js\"", "string", },
            {">", "punctuation-definition-tag-end", },
            {"<", "punctuation-definition-tag-begin", },
            {"\\/", },
            {"script", "variable", },
            {">", "punctuation-definition-tag-end", },
            {"')", },
            {"</", "punctuation-definition-tag-begin", },
            {"script", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"script", "constant-character", },
            {" ", },
            {"src", "variable", },
            {"=", },
            {"\"js/plugins.js\"", "string", },
            {">", "punctuation-definition-tag-end", },
            {"</", "punctuation-definition-tag-begin", },
            {"script", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"script", "constant-character", },
            {" ", },
            {"src", "variable", },
            {"=", },
            {"\"js/main.js\"", "string", },
            {">", "punctuation-definition-tag-end", },
            {"</", "punctuation-definition-tag-begin", },
            {"script", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<!-- Google Analytics: change UA-XXXXX-Y to be your site's ID. -->", "comment", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"script", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"            window.ga=function(){ga.q.push(arguments)};ga.q=[];ga.l=+new Date;            ga('create','UA-XXXXX-Y','auto');ga('send','pageview')        ", },
            {"</", "punctuation-definition-tag-begin", },
            {"script", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"        ", },
            {"<", "punctuation-definition-tag-begin", },
            {"script", "constant-character", },
            {" ", },
            {"src", "variable", },
            {"=", },
            {"\"https://www.google-analytics.com/analytics.js\"", "string", },
            {" ", },
            {"async", "variable", },
            {" ", },
            {"defer", "variable", },
            {">", "punctuation-definition-tag-end", },
            {"</", "punctuation-definition-tag-begin", },
            {"script", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"    ", },
            {"</", "punctuation-definition-tag-begin", },
            {"body", "constant-character", },
            {">", "punctuation-definition-tag-end", },
            {"</", "punctuation-definition-tag-begin", },
            {"html", "constant-character", },
            {">", "punctuation-definition-tag-end", },
    };

    public void run() {
        setTotalInstances(3);
        setPassInstances(0);

        try {
            System.out.print("Testing Java tokenizer: ");
            if (testTokenizer(new JavaLang(), testJava1, testJava1Result)) {
                setPassInstances(getPassInstances() + 1);
                System.out.println("pass");
            } else {
                System.out.println("failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.print("Testing C tokenizer: ");
            if (testTokenizer(new CLang(), testC1, testC1Result)) {
                setPassInstances(getPassInstances() + 1);
                System.out.println("pass");
            } else {
                System.out.println("failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.print("Testing HTML tokenizer: ");
            if (testTokenizer(new HTML(), testHtml1, testHtml1Result)) {
                setPassInstances(getPassInstances() + 1);
                System.out.println("pass");
            } else {
                System.out.println("failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Tokenizer test: " + getPassInstances() + "/" + getTotalInstances());
    }

    boolean testTokenizer(ITokenizer tokenizer, String srcCode, Object[][] result) {
        List<Object> tmpResult = getTokenizerResult(tokenizer, srcCode, false);
        int i = 0;
        for (Object tmpObj : tmpResult) {
            List<Object> resultItem = (List<Object>)tmpObj;
            Object[] objectObject = result[i++];
            if (!((String)resultItem.get(0)).equals((String)objectObject[0])) {
                System.out.println("this: " + resultItem.get(0));
                System.out.println("that: " + objectObject[0]);
                return false;
            }
        }
        return true;
    }

    List<Object> getTokenizerResult(ITokenizer tokenizer, String srcCode, boolean escape) {
        String[] lines = srcCode.split("\n");

        ArrayList<Object> result = new ArrayList<>();
        for (String line : lines) {
            StringStream ss = new StringStream(line);
            while (!ss.reachEnd()) {
                List<String> tokens = tokenizer.tokenize(ss);
                StringBuilder sb = new StringBuilder();
                ArrayList<String> toks = new ArrayList<>();

                sb.append("{");
                for (String tok : tokens) {
                    sb.append("\"" + tok + "\", ");
                    toks.add(tok);
                }
                sb.append("}");

                String ps = ss.popString();
                if (escape) {
                    ps = ps.replaceAll("\"", "\\\\\"");
                }

                ArrayList<Object> resultItem = new ArrayList<>();
                resultItem.add(ps);
                resultItem.add(toks);
                if (result.size() > 0) {
                    ArrayList<Object> previousItem = (ArrayList<Object>)result.get(result.size() - 1);
                    ArrayList<String> previousTokenList = (ArrayList<String>) previousItem.get(1);

                    if (compareList(toks, previousTokenList)) {
                        previousItem.set(0, previousItem.get(0) + ps );
                    } else {
                        result.add(resultItem);
                    }
                } else {
                    result.add(resultItem);
                }
            }
        }
        return result;
    }

    void printTokenizeResult(ITokenizer tokenizer, String srcCode) {
        List<Object> result = getTokenizerResult(tokenizer, srcCode, true);
        printResult(result);
    }

    boolean compareList(List<String> a, List<String> b) {
        if (a.size() == 0 && b.size() == 0) return true;
        if (a.size() == 0 || b.size() == 0) return false;
        for (String s1 : a) {
            boolean found = false;
            for (String s2 : b) {
                if (s2.equals(s1)) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    void printResult(List<Object> lst) {
        for (Object obj : lst) {
            ArrayList<Object> arr = (ArrayList<Object>)obj;
            String str = (String)arr.get(0);
            ArrayList<String> toks = (ArrayList<String>)arr.get(1);
            String output = "{\"" + str + "\", ";
            for (String tok : toks) {
                output += "\"" + tok + "\", ";
            }

            output += "},";
            System.out.println(output);
        }
    }

}
