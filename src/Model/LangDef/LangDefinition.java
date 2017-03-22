package Model.LangDef;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by duzhong on 17-3-22.
 */
public class LangDefinition {

    static LangDefinition LoadJson(String rawData) throws ParseException, LangDefError {
        LangDefinition result = new LangDefinition();
        JSONParser parser = new JSONParser();

        JSONObject _object = (JSONObject)parser.parse(rawData);

        result._presetSyntax = new HashMap<>();
        for (Object _keyObj : _object.keySet()) {
            String _key = (String)_keyObj;
            if (_key.equals("tokenizer")) {
                continue;
            }

            Object _valueObj = _object.get(_keyObj);
            if (_valueObj instanceof String) {
                String _str = (String)_valueObj;
                result._presetSyntax.put(_key, Pattern.compile(_str));

            } else if (_valueObj instanceof JSONArray) {
                JSONArray _arr = (JSONArray)_valueObj;
                StringBuilder sb = new StringBuilder();
                sb.append("(");
                for (Object _subObj : _arr) {
                    String _subStr = (String)_subObj;
                    sb.append(Pattern.quote(_subStr));
                    sb.append("|");
                }
                sb.deleteCharAt(sb.length() - 1);  // remove the laste '|'
                sb.append(")");
                result._presetSyntax.put(_key, Pattern.compile(sb.toString()));
            } else if (_valueObj instanceof JSONObject) {
                JSONObject _jsonObj = (JSONObject)_valueObj;
            }
        }

        JSONObject _tokenizerObj = (JSONObject)_object.get("tokenizer");
        if (_tokenizerObj == null) {
            throw new LangDefError("tokenizer field not found");
        }

        return result;
    }

    public static class LangDefError extends Exception {

        private String message;

        public LangDefError(String msg) {
            this.message = msg;
        }

        @Override
        public String toString() {
            return super.toString() + " " + this.message;
        }

    }

    public class Tokenizer {

    }

    private Map<String, Pattern> _presetSyntax;
    private Tokenizer[] _tokenizers;


}
