package Model;

/**
 * Created by duzhong on 17-3-4.
 */
public final class GenHandler {

    private Generator _generator;

    private Configuration _config;

    GenHandler(Generator generator, Configuration config) {
        _generator = generator;
        _config = config;
    }

    void start(IEntry entry) {

    }

    public Generator get_generator() {
        return _generator;
    }

    public Configuration get_config() {
        return _config;
    }

}
