package Model;
import Model.TmLang.*;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by duzhong on 17-3-15.
 */
public final class Tokenizer {

    public final class Token {

        private ArrayList<String> _types;
        private String _content;

        public Token() {
            _types = new ArrayList<>();
        }

        public void addTokenType(String type) {
            _types.add(type);
        }

        public ArrayList<String> getTokenTypes() {
            return _types;
        }

        public String get_content() {
            return _content;
        }

        public void set_content(String _content) {
            this._content = _content;
        }

    }

    TmLang _tmLang;
    String _content;
    StringStream _stream;

    private LinkedList<Token> _tokens;
    private ArrayList<String> _messages;

    private void beginTokenize() {
        _stream = new StringStream(_content);
    }

    public TmLang get_tmLang() {
        return _tmLang;
    }

    public void set_tmLang(TmLang _tmLang) {
        this._tmLang = _tmLang;
    }

    public String get_content() {
        return _content;
    }

    public void set_content(String _content) {
        this._content = _content;
    }

    public LinkedList<Token> get_tokens() {
        return _tokens;
    }

    public ArrayList<String> get_messages() {
        return _messages;
    }

}
