package Model.TmLang;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by duzhong on 17-3-11.
 */
public class TmLangPattern {

    protected String _name;
    protected Pattern _begin;
    protected HashMap<String, TmLangPattern> _beginCaptures;
    protected String _contentName;
    protected Pattern _end;
    protected HashMap<String, TmLangPattern> _endCaptures;
    protected HashMap<String, TmLangPattern> _captures;
    protected Pattern _match;
    protected String _include;
    protected Pattern[] _patterns;

    public TmLangPattern() {
        _beginCaptures = new HashMap<>();
        _endCaptures = new HashMap<>();
        _captures = new HashMap<>();
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_begin(Pattern _begin) {
        this._begin = _begin;
    }

    public void set_contentName(String _contentName) {
        this._contentName = _contentName;
    }

    public void set_end(Pattern _end) {
        this._end = _end;
    }

    public void set_match(Pattern _match) {
        this._match = _match;
    }

    public String get_name() {
        return _name;
    }

    public Pattern get_begin() {
        return _begin;
    }

    public HashMap<String, TmLangPattern> get_beginCaptures() {
        return _beginCaptures;
    }

    public String get_contentName() {
        return _contentName;
    }

    public Pattern get_end() {
        return _end;
    }

    public HashMap<String, TmLangPattern> get_endCaptures() {
        return _endCaptures;
    }

    public HashMap<String, TmLangPattern> get_captures() {
        return _captures;
    }

    public Pattern get_match() {
        return _match;
    }

    public String get_include() {
        return _include;
    }

    public void set_include(String _include) {
        this._include = _include;
    }

    public Pattern[] get_patterns() {
        return _patterns;
    }

    public void set_patterns(Pattern[] _patterns) {
        this._patterns = _patterns;
    }

}
