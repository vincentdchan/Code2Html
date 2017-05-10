package Model;

/**
 * Created by duzhong on 17-3-4.
 */
public class Configuration {

    private boolean _showLineNumber = true;
    private String _styleName = "1337";
    private int _tab2spaceCount = 4;
    private boolean _showJavaHint = true;
    private boolean _enableNav = true;
    private int _fontSize = 14;
    private String _fontFamily = "";

    @Override
    public Object clone() {
        Configuration that = new Configuration();
        that._showLineNumber = _showLineNumber;
        that._styleName = _styleName;
        that._tab2spaceCount = _tab2spaceCount;
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

    public String get_styleName() {
        return _styleName;
    }

    public void set_styleName(String _styleName) {
        this._styleName = _styleName;
    }

    public int get_tab2spaceCount() {
        return _tab2spaceCount;
    }

    public void set_tab2spaceCount(int _tab2spaceCount) {
        this._tab2spaceCount = _tab2spaceCount;
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

    public int get_fontSize() {
        return _fontSize;
    }

    public void set_fontSize(int _fontSize) {
        this._fontSize = _fontSize;
    }

    public String get_fontFamily() {
        return _fontFamily;
    }

    public void set_fontFamily(String _fontFamily) {
        this._fontFamily = _fontFamily;
    }

}
