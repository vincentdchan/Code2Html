package Model;

import java.util.ArrayList;

/**
 * Created by duzhong on 17-3-4.
 */

public final class Java2Html {

    private ArrayList<IResultGetter> _getters;

    private Configuration _config;

    public Java2Html() {
        _getters = new ArrayList<IResultGetter>();
    }

    public void convert(String javaCode) {
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
