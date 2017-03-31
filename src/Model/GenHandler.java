package Model;


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

    private final String HTMLBegin = "<html>" +
            "   <head>" +
            "       <title>Java Code>" +
            "   </head>" +
            "   <body>";

    private final String HTMLEnd = "" +
            "   </body>" +
            "</html>";

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

        String[] lines = _srcCode.split("\n");

        StringBuilder sb = new StringBuilder();
        sb.append(HTMLBegin);
        sb.append("<div>");

        for (String line : lines) {
            sb.append("<p>");
            sb.append(line);
            sb.append("</p>");
        }

        sb.append("</div>");
        sb.append(HTMLEnd);

        dispatchGetter(sb.toString());
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
