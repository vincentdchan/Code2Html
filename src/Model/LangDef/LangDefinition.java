package Model.LangDef;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by duzhong on 17-3-22.
 */
public final class LangDefinition {

    private Map<String, Pattern> _presetSyntax;
    private Map<String, String> _presetRaw;
    private Map<String, Tokenizer> _tokenizerMap;
    private String _tokenPostfix;

    public LangDefinition (String rawData) throws ParseException, LangDefError {
        JSONParser parser = new JSONParser();

        JSONObject _object = (JSONObject)parser.parse(rawData);

        if (_object.containsKey("tokenPostfix")) {
            _tokenPostfix = (String)_object.get("tokenPostfix");
        }

        _presetSyntax = new HashMap<>();
        _presetRaw = new HashMap<>();
        for (Object _keyObj : _object.keySet()) {
            String _key = (String)_keyObj;
            if (_key.equals("tokenizer")) {
                continue;
            }

            Object _valueObj = _object.get(_keyObj);
            if (_valueObj instanceof String) {
                String _str = (String)_valueObj;
                _presetSyntax.put(_key, Pattern.compile(_str));
            } else if (_valueObj instanceof JSONArray) {
                JSONArray _arr = (JSONArray)_valueObj;

                // sort the string long to short
                _arr.sort((Object o1, Object o2) -> {
                    return ((String)o2).length() - ((String)o1).length();
                });

                StringBuilder sb = new StringBuilder();
                sb.append("(");
                for (Object _subObj : _arr) {
                    String _subStr = (String)_subObj;
                    sb.append(Pattern.quote(_subStr));
                    sb.append("|");
                }
                sb.deleteCharAt(sb.length() - 1);  // remove the laste '|'
                sb.append(")");
                String _str = sb.toString();
                sb = null; // release resource and make sure the follwing code not use sb anymore.
                _presetRaw.put(_key, _str);
                _presetSyntax.put(_key, Pattern.compile(_str));
            } else if (_valueObj instanceof JSONObject) {
                JSONObject _jsonObj = (JSONObject)_valueObj;
                throw new LangDefError("Unkown definition");
            }
        }

        JSONObject _tokenizerObj = (JSONObject)_object.get("tokenizer");
        if (_tokenizerObj == null) {
            throw new LangDefError("tokenizer field not found");
        }

        _tokenizerMap = new HashMap<>();

        for (Object _tokKey : _tokenizerObj.keySet()) {
            String _tokenizerKey = (String)_tokKey;
            Object _raw = _tokenizerObj.get(_tokKey);

            _tokenizerMap.put(_tokenizerKey, new Tokenizer(_tokenizerKey, _raw));
        }

    }

    public enum ItemType {
        Redirect,       // redirect another tokenizer
        Token,          // this is a token
        Stream,         // streaming case to many tokenizers
        Bridge,         // link to next tokenizer
    }

    public final class Tokenizer {

        private String name;
        private ArrayList<Item> items;

        public Tokenizer(String name, Object raw) throws LangDefError {
            this.name = name;

            if (!(raw instanceof JSONArray)) throw new LangDefError("Tokenizer must be a array.");
            JSONArray arr = (JSONArray)raw;

            items = new ArrayList<>();
            for (Object _val : arr) {
                if (_val instanceof JSONObject) {
                    JSONObject _subObj = (JSONObject)_val;
                    Item item = new Item();
                    if (_subObj.containsKey("include")) {
                        item.setType(ItemType.Redirect);
                        item.setInclude((String)_subObj.get("include"));
                    } else {
                        throw new LangDefError("Unknown definition");
                    }
                    items.add(item);
                } else if (_val instanceof JSONArray) {
                    JSONArray _subArr = (JSONArray)_val;
                    if (_subArr.size() < 2)
                        throw new LangDefError("The size of item array must bigger than 2.");
                    Item item = new Item();
                    item.setIntro((String)_subArr.get(0));

                    Object _secondObj = _subArr.get(1);
                    if (_secondObj instanceof JSONObject) {
                        JSONObject _prop = (JSONObject)_secondObj;
                        if (_prop.containsKey("token")) {
                            item.setTokenName((String)_prop.get("token"));
                            item.setType(ItemType.Token);
                        }
                        if (_prop.containsKey("log")) {
                            item.setLog((String)_prop.get("log"));
                        }
                        if (_prop.containsKey("cases")) {
                            JSONObject _casesObj = (JSONObject) _prop.get("cases");
                            item.setType(ItemType.Stream);
                            if (_casesObj == null)
                                throw new LangDefError("cases filed must be a object");
                            item.setCaseMap(_casesObj);
                        }
                    } else if (_secondObj instanceof String) {
                        String _str = (String)_secondObj;
                        if (_str.length() > 0 && _str.charAt(0) == '@') {
                            item.setType(ItemType.Stream.Redirect);
                            item.setNextTokenizerKey(_str.substring(1));
                        } else {
                            item.setType(ItemType.Token);
                            item.setTokenName(_str);
                        }
                    } else {
                        throw new LangDefError("Unknown type of second parameter of item.");
                    }
                } else {
                    throw new LangDefError("Unknown type.");
                }
            }
        }

        public Item[] getItems() {
            return items.toArray(new Item[items.size()]);
        }

        public final class Item {

            private ItemType type;
            private String include;
            private String tokenName;
            private String intro;
            private String nextTokenizerKey;
            private String log;
            private boolean push;
            private boolean pop;
            private Map<String, String> caseMap;

            public ItemType getType() {
                return type;
            }

            public void setType(ItemType type) {
                this.type = type;
            }

            public String getInclude() {
                return include;
            }

            public void setInclude(String include) {
                this.include = include;
            }

            public String getTokenName() {
                return tokenName;
            }

            public void setTokenName(String tokenName) {
                this.tokenName = tokenName;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public String getNextTokenizerKey() {
                return nextTokenizerKey;
            }

            public void setNextTokenizerKey(String nextTokenizerKey) {
                this.nextTokenizerKey = nextTokenizerKey;
            }

            public String getLog() {
                return log;
            }

            public void setLog(String log) {
                this.log = log;
            }

            public boolean isPush() {
                return push;
            }

            public void setPush(boolean push) {
                this.push = push;
            }

            public boolean isPop() {
                return pop;
            }

            public void setPop(boolean pop) {
                this.pop = pop;
            }

            public Map<String, String> getCaseMap() {
                return caseMap;
            }

            public void setCaseMap(Map<String, String> caseMap) {
                this.caseMap = caseMap;
            }

        }

        public String getName() {
            return name;
        }

    }

    public final class LangDefError extends Exception {

        private String message;

        public LangDefError(String msg) {
            this.message = msg;
        }

        @Override
        public String toString() {
            return super.toString() + " " + this.message;
        }

    }

    public Map<String, Pattern> get_presetSyntax() {
        return _presetSyntax;
    }

    public Map<String, String> get_presetRaw() {
        return _presetRaw;
    }

    public Map<String, Tokenizer> get_tokenizerMap() {
        return _tokenizerMap;
    }

    public String get_tokenPostfix() {
        return _tokenPostfix;
    }

}
