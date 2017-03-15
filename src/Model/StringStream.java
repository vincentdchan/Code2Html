package Model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by duzhong on 17-3-15.
 */
public final class StringStream {

    private String _content;
    private int ptr;
    private int tmp_ptr;

    public StringStream(String content) {
        _content = content;
        ptr = 0;
        tmp_ptr = 0;
    }

    public String popString() {
        if (tmp_ptr < ptr) {
            StringBuilder sb = new StringBuilder();
            for (int i = tmp_ptr; i < ptr; i++) {
                sb.append(_content.charAt(i));
            }
            tmp_ptr = ptr;
            return sb.toString();
        } else {
            return "";
        }
    }

    public boolean swallow(Pattern pattern) {
        Matcher matcher = pattern.matcher(_content);
        if (matcher.find(ptr)) {
            ptr = matcher.end();
            return true;
        }
        return false;
    }

    public boolean test(Pattern pattern) {
        Matcher matcher = pattern.matcher(_content);
        return matcher.find(ptr);
    }

    public char getChar() {
        if (ptr < _content.length()) {
            return _content.charAt(ptr);
        } else {
            return '\0';
        }
    }

    public char getChar(int index) {
        if (index < _content.length()) {
            return _content.charAt(ptr);
        } else {
            return '\0';
        }
    }

    public void moveForward() {
        ++ptr;
    }

    public void moveForward(int cnt) {
        ptr += cnt;
    }

    public int currentOffset() {
        return ptr;
    }

    /**
     * Check if the current char is White;
     */
    public boolean isWhite() {
        char ch = getChar();
        return ch == ' ' ||
                ch == '\t' ||
                ch == '\r' ||
                ch == '\n';
    }

    /**
     * Similar to `eatSpace`, but this method
     * also eats something else like '\r\n', '\t'
     */
    public int eatWhite() {
        int tmp = ptr;
        char ch = getChar(tmp);
        while (ch == ' ' ||
                ch == '\t' ||
                ch == '\r' ||
                ch == '\n') {
            tmp++;
            ch = getChar(tmp);
        }
        if (tmp > ptr) {
            int count = tmp - ptr;
            return count;
        } else {
            return 0;
        }
    }

    public boolean isSpace() {
        return getChar() == ' ';
    }

    public int eatSpace() {
        int tmp = ptr;
        while (getChar(tmp) == ' ') {
            tmp++;
        }
        if (tmp > ptr) {
            int count = tmp - ptr;
            return count;
        } else {
            return 0;
        }
    }

    /**
     * Check if the current char is digit.
     */
    public boolean isDigit() {
        return Character.isDigit(getChar());
    }

    public boolean isAlphabetic() {
        return Character.isAlphabetic(getChar());
    }

    public boolean reachEnd() {
        return ptr < _content.length();
    }

}
