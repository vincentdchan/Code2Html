package Model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by duzhong on 17-3-4.
 *
 * The Java2Html class must be multi-thread class.
 * This class is a wrapper of `Generator` class, it's friendly
 * to view part of the program.
 *
 * When you create a class and set a config, the object
 * will convert the code to html in the same way(config)
 * in different threads. You can call `convert` method
 * many times. It's OK and it won't block the thread.
 *
 * Java2Html use a inner ThreadPool which is in class `Generator`.
 */
public final class Java2Html {


    private ArrayList<IResultGetter> _getters;
    private Configuration _config;

    public static String[] getStylesNameList() {
        ArrayList<String> result = new ArrayList<>();
        File file = new File("resources/themeCSS");
        for (File child : file.listFiles()) {
            if (!child.isFile()) continue;
            String filename = child.getName();
            if (!filename.endsWith(".css")) continue;
            String[] slices = filename.split(".");
            result.add(slices[0]);
        }
        return result.toArray(new String[result.size()]);
    }

    public Java2Html() {
        _getters = new ArrayList<IResultGetter>();
    }

    /**
     * You can call this methods many times, the work will be dispatch
     * to different threads. Don't worry about the method will block
     * the thread.
     *
     * @param filename identify the langurage of the code according to filename
     * @param srcCode the content of the code
     */
    public void convert(String filename, String srcCode) throws IOException, Generator.NotSupportedFiletypes {
        Entry[] entries = new Entry[1];
        entries[0] = new Entry();
        entries[0].setFilename(filename);
        entries[0].setSourceCode(srcCode);
        convert(entries);
    }

    public void convert(Entry[] entries) throws IOException, Generator.NotSupportedFiletypes {
        Generator generator = new Generator(_config);
        generator.generate(entries, _getters);
    }

    public void addGetter(IResultGetter getter) {
        _getters.add(getter);
    }

    public void removeGetter(IResultGetter getter) {
        _getters.remove(getter);
    }

    public void set_config(Configuration _config) {
        this._config = _config;
    }

    public Configuration get_config() {
        return _config;
    }

}
