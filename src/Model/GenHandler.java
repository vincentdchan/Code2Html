package Model;


import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by duzhong on 17-3-4.
 */
public final class GenHandler implements Runnable{

    private Generator _generator;
    private Configuration _config;
    private String _srcCode;
    private String _styleCode;
    private ITokenizer _tokenizer;
    private List<IResultGetter> _getters;

    private final String HTMLBegin = "<html>\n" +
            "   <head>\n" +
            "       <meta charset=\"utf-8\">\n" +
            "       <title>Java Code</title>\n" +
            "       <style>\n";

    private final String HTMLMid =
            "       </style>\n" +
            "   </head>\n" +
            "   <body>\n";

    private final String HTMLEnd = "" +
            "   </body>\n" +
            "</html>\n";

    GenHandler(Generator generator,
               Configuration config,
               String srcCode,
               String styleCode,
               ITokenizer tokenizer,
               List<IResultGetter> getters) {
        _generator = generator;
        _config = config;
        _srcCode = srcCode;
        _styleCode = styleCode;
        _tokenizer = tokenizer;
        _getters = getters;
    }

    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();
        sb.append(HTMLBegin);
        sb.append(_styleCode);
        sb.append(HTMLMid);
        sb.append("<div id=\"code_area\">\n");

        sb.append(generateCodeHTML());

        sb.append("</div>\n");
        sb.append(HTMLEnd);

        dispatchGetter(sb.toString());
    }

    private String generateLineNumber(int i) {
        return "<span class=\"linenumber\">" + i + "</span>";
    }

    private String generateCodeHTML() {
        String[] lines = _srcCode.split("\r?\n");

        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (String line : lines) {
            sb.append("<p>");

            if (_config.is_showLineNumber()) {
                sb.append(generateLineNumber(count++));
            }

            List<Token> tokens = tokenize(line);

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

    private String escapeString(String code) {
        // String result = code.replaceAll("\n", "<br>\n");
        String result = code.replaceAll(" ", "&nbsp;");
        result = result.replaceAll("<", "&lt;");
        result = result.replaceAll(">", "&gt;");
        return result;
    }

    private List<Token> tokenize(String code) {
        ArrayList<Token> result = new ArrayList<>();
        StringStream ss = new StringStream(code);

        while(!ss.reachEnd()) {
            Token tok = new Token();
            tok.setSyntax(_tokenizer.tokenize(ss));
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

    private void dispatchGetter(String destCode) {
        for (IResultGetter getter : _getters) {
            getter.getResult(destCode);
        }
        finish();
    }

    private void finish() {
        _generator.removeHandler(this);
    }

    public Generator get_generator() {
        return _generator;
    }

    public Configuration get_config() {
        return _config;
    }

    public String get_srcCode() {
        return _srcCode;
    }

    public String get_styleCode() {
        return _styleCode;
    }

}
