package Model;

import Model.LangSpec.CLang;
import Model.LangSpec.JavaLang;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private String _styleCode;

    private List<IResultGetter> getters;

    public Generator(Configuration config) throws java.io.IOException {
        _config = config;
        _lock = new ReentrantLock();
        _blockingQueue = new LinkedBlockingQueue();
        _handlers = new LinkedList<>();

        String _stylePath = searchStyleFileByName(config.get_styleName());
        _styleCode = new String(
                Files.readAllBytes(
                        Paths.get(_stylePath)));

        _styleCode += new String(Files.readAllBytes(Paths.get("resources/test.css")));
    }

    private String searchStyleFileByName(String name) {
        File path = new File("resources/themeCSS");
        for (File child : path.listFiles()) {
            if (!child.isFile()) continue;
            if (child.getName().startsWith(name)) {
                return child.getAbsolutePath();
            }
        }
        return null;
    }


    /**
     * Generate the code in another thread.
     * If then length of entries is less than or equal three,
     * generate in another thread. Otherwise,
     * using the ThreadPool.
     *
     * @param entries
     * @param getters
     */
    public void generate(Entry[] entries, List<IResultGetter> getters) throws NotSupportedFiletypes {
        this.getters = getters;

        if (entries.length <= 3) {
            for (Entry entry : entries) {
                GenHandler _handler = generate(entry.getFilename(), entry.getSourceCode());
                executeTaskInAnotherThread(_handler);
            }
        } else {
            _threadPool = new ThreadPoolExecutor(CoreThreadSize, MaxThreadSize, 1,
                    TimeUnit.SECONDS, _blockingQueue);

            for (Entry entry : entries) {
                GenHandler _handler = generate(entry.getFilename(), entry.getSourceCode());
                _threadPool.execute(_handler);
            }

            _threadPool.shutdown();
        }

    }

    private GenHandler generate(String filename, String srcCode) throws NotSupportedFiletypes {

        ITokenizer tokenizer = null;
        if (filename.endsWith(".java")) {
            tokenizer = new JavaLang();
        } else if (filename.endsWith(".c") || filename.endsWith(".h")) {
            tokenizer = new CLang();
        } else {
            throw new NotSupportedFiletypes(filename);
        }
        GenHandler _handler = new GenHandler(this,
                _config,
                srcCode,
                _styleCode,
                tokenizer,
                getters);
        addHandler(_handler);
        // _threadPool.execute(_handler);
        return _handler;
    }

    private void executeTaskInAnotherThread(Runnable task) {
        new Thread(task).run();
    }

    public void addHandler(GenHandler handler) {
        _lock.lock();
        try {
            _handlers.add(handler);
        } finally {
            _lock.unlock();
        }
    }

    public void removeHandler(GenHandler handler) {
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
