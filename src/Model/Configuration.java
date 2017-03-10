package Model;

/**
 * Created by duzhong on 17-3-4.
 */
public class Configuration {

    private boolean _showLineNumber = true;
    private String _styleFileName = "default.css";
    private int _spaceCount = 4;
    private boolean _showJavaHint = true;
    private boolean _enableNav = true;

    @Override
    public Object clone() {
        Configuration that = new Configuration();
        that._showLineNumber = _showLineNumber;
        that._styleFileName = _styleFileName;
        that._spaceCount = _spaceCount;
        that._showJavaHint = _showJavaHint;
        that._enableNav = _enableNav;
        return that;
    }

    public boolean is_showLineNumber() {
        return _showLineNumber;
    }

    public void set_showLineNumber(boolean _showLineNumber) {
        this._showLineNumber = _showLineNumber;
    }

    public String get_styleFileName() {
        return _styleFileName;
    }

    public void set_styleFileName(String _styleFileName) {
        this._styleFileName = _styleFileName;
    }

    public int get_spaceCount() {
        return _spaceCount;
    }

    public void set_spaceCount(int _spaceCount) {
        this._spaceCount = _spaceCount;
    }

    public boolean is_showJavaHint() {
        return _showJavaHint;
    }

    public void set_showJavaHint(boolean _showJavaHint) {
        this._showJavaHint = _showJavaHint;
    }

    public boolean is_enableNav() {
        return _enableNav;
    }

    public void set_enableNav(boolean _enableNav) {
        this._enableNav = _enableNav;
    }

}
