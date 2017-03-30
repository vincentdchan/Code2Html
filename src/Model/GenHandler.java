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

    }

    void start(String srcCode, ITokenizer tokenizer, IResultGetter[] _getters) {

    }

    public Generator get_generator() {
        return _generator;
    }

    public Configuration get_config() {
        return _config;
    }

}
