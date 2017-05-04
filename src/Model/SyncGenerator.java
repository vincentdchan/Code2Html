package Model;

import Model.LangSpec.CLang;
import Model.LangSpec.JavaLang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdzos on 2017/5/4.
 */
public final class SyncGenerator {

    private final String HTMLBegin = "<html>\n" +
            "   <head>\n" +
            "       <meta charset=\"utf-8\">\n" +
            "       <title>";

    private final String HTMLMid1 = "</title>\n" +
            "       <style>\n";

    private final String HTMLMid2 =
            "       </style>\n" +
                    "   </head>\n" +
                    "   <body>\n";

    private final String HTMLEnd = "" +
            "   </body>\n" +
            "</html>\n";

    private String _spaces = "";
    private String _styleCode;

    Configuration config;

    public SyncGenerator() {
    }

    public SyncGenerator(Configuration config) throws IOException {
        setConfig(config);
    }

    public String convert(String filename, String srcCode) throws NotSupportedFiletypes {
        ITokenizer tokenizer = null;
        if (filename.endsWith(".java")) {
            tokenizer = new JavaLang();
        } else if (filename.endsWith(".c") || filename.endsWith(".h")) {
            tokenizer = new CLang();
        } else {
            throw new NotSupportedFiletypes(filename);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(HTMLBegin);
        sb.append(filename);
        sb.append(HTMLMid1);
        sb.append(_styleCode);
        sb.append(HTMLMid2);
        sb.append("<div id=\"code_area\">\n");

        sb.append(generateCodeHTML(tokenizer, srcCode));

        sb.append("</div>\n");
        sb.append(HTMLEnd);

        return sb.toString();
    }

    private String generateLineNumber(int i) {
        return "<span class=\"linenumber\">" + i + "</span>";
    }

    private String generateCodeHTML(ITokenizer tokenizer, String _srcCode) {
        String[] lines = _srcCode.split("\r?\n");

        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (String line : lines) {
            sb.append("<p>");

            if (config.is_showLineNumber()) {
                sb.append(generateLineNumber(count++));
            }

            List<Token> tokens = tokenize(tokenizer, line);

            for (Token tok : tokens) {
                String[] _syntax = tok.getSyntax();
                String content = escapeString(tok.getContent());
                if (_syntax.length > 0) {
                    sb.append("<span class=\"");
                    for (String tokStr : tok.getSyntax()) {
                        sb.append("jc-");
                        sb.append(tokStr);
                        sb.append(" ");
                    }
                    sb.deleteCharAt(sb.length() - 1); // remove the last space
                    sb.append("\">");
                    sb.append(content);
                    sb.append("</span>");
                } else {
                    sb.append(content);
                }
            }

            sb.append("</p>\n");
        }

        return sb.toString();
    }

    /**
     * Turn the tab into spaces,
     * and then escape the string
     */
    private String escapeString(String code) {
        String result = code.replaceAll("\t", _spaces);
        result = result.replaceAll(" ", "&nbsp;");
        result = result.replaceAll("<", "&lt;");
        result = result.replaceAll(">", "&gt;");

        return result;
    }

    private List<Token> tokenize(ITokenizer tokenizer, String code) {
        ArrayList<Token> result = new ArrayList<>();
        StringStream ss = new StringStream(code);

        while(!ss.reachEnd()) {
            Token tok = new Token();
            tok.setSyntax(tokenizer.tokenize(ss));
            tok.setContent(ss.popString());

            if (result.size() > 0) {
                Token top = result.get(result.size() - 1);
                if (top.getSyntax().length == 1 &&
                        tok.getSyntax().length == 1 &&
                        top.getSyntax()[0].equals(tok.getSyntax()[0])) {
                    top.setContent(top.getContent() + tok.getContent());
                    continue;
                }
            }
            result.add(tok);
        }

        return result;
    }

    class Token {

        private String[] syntax;
        private String content;

        public String[] getSyntax() {
            return syntax;
        }

        public void setSyntax(String[] syntax) {
            this.syntax = syntax;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }

    private String inputStreamToString(InputStream input) throws IOException {
        String result = new String();
        BufferedReader bf = new BufferedReader(new InputStreamReader(input));
        for (String line; (line = bf.readLine()) != null; result += line);
        return result;
    }

    private String searchStyleFileByName(String name) {
        return "/resources/themeCSS/" + name + ".tmTheme.css";
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) throws IOException {
        this.config = config;


        int tab2SpaceCount = config.get_tab2spaceCount();
        for (int i = 0; i < tab2SpaceCount; ++i) {
            _spaces += " ";
        }

        String _stylePath = searchStyleFileByName(config.get_styleName());
        _styleCode = new String();

        _styleCode = "body {\n" +
                "   font-size: " + config.get_fontSize() + "px;\n" +
                "}\n";
        _styleCode += inputStreamToString(getClass().getResourceAsStream(_stylePath));
        _styleCode += inputStreamToString(getClass().getResourceAsStream("/resources/test.css"));
    }

    public class NotSupportedFiletypes extends Exception {

        private String filename;

        public NotSupportedFiletypes(String filenam) {
            super();
            this.filename = filename;
        }

        public String getFilename() {
            return filename;
        }

        @Override
        public String toString() {
            return this.filename;
        }

    }

}
