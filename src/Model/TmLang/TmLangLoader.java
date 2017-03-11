package Model.TmLang;
import org.json.simple.parser.*;
import org.json.simple.*;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by duzhong on 17-3-11.
 *
 * Read data from `tmLanguage`, and to generate the tokenize information
 *
 */
public final class TmLangLoader {

    private TmLang _langSpec;

    TmLangLoader(String rawData) throws ParseException, TmLangParseError {
        JSONParser parser = new JSONParser();

        _langSpec = new TmLang();
        Object obj = parser.parse(rawData);
        JSONObject jsonObj = (JSONObject)obj;

        // fileTypes --------------------------------------------------
        JSONArray fileTypesArr = (JSONArray)jsonObj.get("fileTypes");
        String[] fileTypes = new String[fileTypesArr.size()];
        int cnt = 0;
        for (Object child : fileTypesArr) {
            String value = (String)child;
            fileTypes[cnt++] = value;
        }
        _langSpec.set_fileTypes(fileTypes);

        // name -------------------------------------------------------
        String name = (String)jsonObj.get("name");
        if (name != null) {
            _langSpec.set_name(name);
        } else {
            throw new TmLangNameNotExists();
        }

        // repository -------------------------------------------------
        JSONObject repoObj = (JSONObject) jsonObj.get("repository");
        if (repoObj != null) {
            _langSpec.set_repository(parseRepository(repoObj));
        }

        // patterns ---------------------------------------------------
        // Notice: parse the `repository` first, then `patterns`
        JSONArray patternsSet = (JSONArray)jsonObj.get("patterns");
        if (patternsSet != null) {
            ArrayList<TmLangParseError> _patterns = new ArrayList<>();
            for (Object _obj : patternsSet) {
               JSONObject patternObj = (JSONObject) _obj;

            }
            _langSpec.set_patterns((TmLangPattern[]) _patterns.toArray());
        }

        // scopeName --------------------------------------------------
        String scopeName = (String)jsonObj.getOrDefault("scopeName", "");
        _langSpec.set_scopeName(scopeName);

        // uuid -------------------------------------------------------
        String uuid = (String)jsonObj.getOrDefault("uuid", "");
        _langSpec.set_uuid(uuid);

        // version ----------------------------------------------------
        String version = (String)jsonObj.getOrDefault("version", "");
        _langSpec.set_version(version);
    }

    private Map<String, TmLangPattern> parseRepository(JSONObject obj) {
        HashMap<String, TmLangPattern> result = new HashMap<>();
        Set keys = obj.keySet();
        for (Object _keyObj : keys) {
            JSONObject _patternObj = (JSONObject) obj.get(_keyObj);
            TmLangPattern _resultPattern = parsePatternObject(_patternObj);
            _resultPattern.set_name((String)_keyObj);
            result.put((String)_keyObj, _resultPattern);
        }
        return result;
    }

    private TmLangPattern parsePatternObject(JSONObject jsonObj) {
        TmLangPattern result = new TmLangPattern();

        // include -----------------------------------------------------
        String _include = (String)jsonObj.get("include");
        result.set_include(_include);

        // name --------------------------------------------------------
        String name = (String)jsonObj.get("name");
        result.set_name(name);

        // begin -------------------------------------------------------
        String beginStr = (String)jsonObj.get("begin");
        if (beginStr != null) {
            Pattern _regex = Pattern.compile(beginStr);
            result.set_begin(_regex);
            JSONObject _bcObj = (JSONObject)jsonObj.get("beginCaptures");
            if (_bcObj != null) {
                Set keys = _bcObj.keySet();
                for (Object _keyObj : keys) {
                    JSONObject _valObj = (JSONObject) _bcObj.get(_keyObj);
                    result.get_beginCaptures().put((String)_keyObj, parsePatternObject(_valObj));
                }
            }
        }

        // end ---------------------------------------------------------
        String endStr = (String)jsonObj.get("end");
        if (endStr != null) {
            Pattern _regex = Pattern.compile(endStr);
            result.set_end(_regex);
            JSONObject _ecObj = (JSONObject)jsonObj.get("endCaptures");
            if (_ecObj != null) {
                Set keys = _ecObj.keySet();
                for (Object _keyObj : keys) {
                    JSONObject _valObj = (JSONObject) _ecObj.get(_keyObj);
                    result.get_endCaptures().put((String)_keyObj, parsePatternObject(_valObj));
                }
            }
        }

        // patterns -----------------------------------------------------
        JSONArray _patternsObj = (JSONArray) jsonObj.get("patterns");
        if (_patternsObj != null) {
            ArrayList<TmLangPattern> _children = new ArrayList<>();
            for (Object _obj : _patternsObj) {
                _children.add(parsePatternObject((JSONObject)_obj));
            }
        }
        return result;
    }

    public class TmLangParseError extends Exception {

        @Override
        public String toString() {
            return "An parse error occurs.";
        }

    }

    public class TmLangNameNotExists extends TmLangParseError {

        @Override
        public String toString() {
            return "The name of the TmLanguage does not exists.";
        }

    }

    public class TmLangPatternNameNotExists extends TmLangParseError {

        @Override
        public String toString() {
            return "The name of the TmLanguage pattern does not exists.";
        }

    }

}
