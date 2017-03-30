package Model;

import Model.LangSpec.CLang;
import Model.LangSpec.JavaLang;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by duzhong on 17-3-4.
 */
public class Generator {

    public final int CoreThreadSize = 2;
    public final int MaxThreadSize = 8;

    private Configuration _config;
    private LinkedList<GenHandler> _handlers;
    private ThreadPoolExecutor _threadPool;
    private BlockingQueue _blockingQueue;
    private Lock _lock;

    private List<IResultGetter> getters;

    public Generator(Configuration config) {
        _config = config;
        _lock = new ReentrantLock();
        _blockingQueue = new LinkedBlockingQueue();

        _threadPool = new ThreadPoolExecutor(CoreThreadSize, MaxThreadSize, 1,
                TimeUnit.SECONDS, _blockingQueue);
    }

    public void generate(String filename, String srcCode, List<IResultGetter> getters) {
        this.getters = getters;

        ITokenizer tokenizer = null;
        if (filename.endsWith(".java")) {
            tokenizer = new JavaLang();
        } else if (filename.endsWith(".c") || filename.endsWith(".h")) {
            tokenizer = new CLang();
        }
        GenHandler _handler = new GenHandler(this, _config, srcCode, tokenizer, getters);
        addHandler(_handler);
        _threadPool.execute(_handler);

    }

    private void convertByTokenizer(String srcCode, ITokenizer tokenizer) {
        StringStream ss = new StringStream(srcCode);

        ArrayList<List<String>> tokens = new ArrayList<>();

        while (!ss.reachEnd()) {
            tokens.add(tokenizer.tokenize(ss));
        }
    }

    private void addHandler(GenHandler handler) {
        _lock.lock();
        try {
            _handlers.add(handler);
        } finally {
            _lock.unlock();
        }
    }

    private void removeHandler(GenHandler handler) {
        _lock.lock();
        try {
            _handlers.remove(handler);
        } finally {
            _lock.unlock();
        }
    }

    public Configuration get_config() {
        return _config;
    }

    public void set_config(Configuration _config) {
        this._config = _config;
    }

}
