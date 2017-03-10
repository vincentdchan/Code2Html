package Model;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
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

    private class InnerHandler implements Runnable {

        private IEntry _entry;
        private Generator _generator;

        InnerHandler(Generator generator, IEntry entry) {
            _generator = generator;
            _entry = entry;
        }

        @Override
        public void run() {
            GenHandler _handler = new GenHandler(_generator, _config);
            _handlers.add(_handler);
            _handler.start(_entry);
        }

    }

    public Generator(Configuration config) {
        _config = config;
        _lock = new ReentrantLock();
        _blockingQueue = new LinkedBlockingQueue();

        _threadPool = new ThreadPoolExecutor(CoreThreadSize, MaxThreadSize, 1,
                TimeUnit.SECONDS, _blockingQueue);
    }

    public void startGeneration(IEntry[] files) {
        for (IEntry file : files) {
            _threadPool.execute(new InnerHandler(this, file));
        }
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

}
