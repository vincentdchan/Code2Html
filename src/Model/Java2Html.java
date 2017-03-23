package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by duzhong on 17-3-4.
 */

public final class Java2Html {


    private ArrayList<IResultGetter> _getters;

    private Configuration _config;

    public Java2Html() {
        _getters = new ArrayList<IResultGetter>();
    }

    private static void loadJson(File file) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(file.getPath())));

    }

    private static void cachedFileTypes() {
    }

    /**
     *
     * @param filename identify the langurage of the code according to filename
     * @param srcCode the content of the code
     */
    public void convert(String filename, String srcCode) {
        String result = "";
        for (IResultGetter getter : _getters) {
            getter.getResult(result);
        }
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
