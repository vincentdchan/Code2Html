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
    private ITokenizer _tokenizer;
    private List<IResultGetter> _getters;

    private final String HTMLBegin = "<html>\n" +
            "   <head>\n" +
            "       <title>Java Code</title>\n" +
            "   </head>\n" +
            "   <body>\n";

    private final String HTMLEnd = "" +
            "   </body>\n" +
            "</html>\n";

    GenHandler(Generator generator,
               Configuration config,
               String srcCode,
               ITokenizer tokenizer,
               List<IResultGetter> getters) {
        _generator = generator;
        _config = config;
        _srcCode = srcCode;
        _tokenizer = tokenizer;
        _getters = getters;
    }

    @Override
    public void run() {
        _srcCode = _srcCode.replaceAll("<", "&lt;");
        _srcCode = _srcCode.replaceAll(">", "&gt;");

        StringBuilder sb = new StringBuilder();
        sb.append(HTMLBegin);
        sb.append("<div>\n");

        sb.append(generateCodeHTML());

        sb.append("</div>\n");
        sb.append(HTMLEnd);

        dispatchGetter(sb.toString());
    }

    private String generateCodeHTML() {
        List<Token> tokens = tokenize();

        StringBuilder sb = new StringBuilder();
        for (Token tok : tokens) {
            if (tok.getContent().contains("linebreak")) {
                sb.append("<br>");
            } else {
                String content = tok.getContent().replaceAll(" ", "&nbsp;");
                if (tok.getSyntaxs().length > 0) {
                    sb.append("<span class=\"");
                    for (String tokStr : tok.getSyntaxs()) {
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
        }

        return sb.toString();
    }

    private List<Token> tokenize() {
        ArrayList<Token> result = new ArrayList<>();
        StringStream ss = new StringStream(_srcCode);

        while(!ss.reachEnd()) {
            Token tok = new Token();
            tok.setSyntaxs(_tokenizer.tokenize(ss));
            tok.setContent(ss.popString());
            result.add(tok);
        }

        return result;
    }

    class Token {

        private String[] syntaxs;
        private String content;

        public String[] getSyntaxs() {
            return syntaxs;
        }

        public void setSyntaxs(String[] syntaxs) {
            this.syntaxs = syntaxs;
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
    }

    public Generator get_generator() {
        return _generator;
    }

    public Configuration get_config() {
        return _config;
    }

}
