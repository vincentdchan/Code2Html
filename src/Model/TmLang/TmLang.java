package Model.TmLang;

import java.util.Map;

/**
 * Created by duzhong on 17-3-11.
 */
public class TmLang {

    private String[] _fileTypes;
    private String _keyEquivalent;
    private String _name;
    private String _scopeName;
    private TmLangPattern[] _patterns;
    private Map<String, TmLangPattern> _repository;
    private String _uuid;
    private String _version;

    public TmLang() {

    }

    public String[] get_fileTypes() {
        return _fileTypes;
    }

    public void set_fileTypes(String[] _fileTypes) {
        this._fileTypes = _fileTypes;
    }

    public String get_keyEquivalent() {
        return _keyEquivalent;
    }

    public void set_keyEquivalent(String _keyEquivalent) {
        this._keyEquivalent = _keyEquivalent;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_uuid() {
        return _uuid;
    }

    public void set_uuid(String _uuid) {
        this._uuid = _uuid;
    }

    public String get_scopeName() {
        return _scopeName;
    }

    public void set_scopeName(String _scopeName) {
        this._scopeName = _scopeName;
    }

    public TmLangPattern[] get_patterns() {
        return _patterns;
    }

    public void set_patterns(TmLangPattern[] _patterns) {
        this._patterns = _patterns;
    }

    public Map<String, TmLangPattern> get_repository() {
        return _repository;
    }

    public void set_repository(Map<String, TmLangPattern> _repository) {
        this._repository = _repository;
    }

    public String get_version() {
        return _version;
    }

    public void set_version(String _version) {
        this._version = _version;
    }
}
