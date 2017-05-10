package Model.LangSpec;

import Model.ITokenizer;
import Model.StringStream;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by duzhong on 17-3-23.
 */
public class JavaLang implements ITokenizer {

    public enum State {
        Normal,
        Comment,
        String,
    }

    public final static String[] JavaKeywords = {
        "abstract", "continue", "for", "new", "switch", "assert", "default",
        "goto", "package", "synchronized", "boolean", "do", "if", "private",
        "this", "break", "double", "implements", "protected", "throw", "byte",
        "else", "import", "public", "throws", "case", "enum", "instanceof", "return",
        "transient", "catch", "extends", "int", "short", "try", "char", "final",
        "interface", "static", "void", "class", "finally", "long", "strictfp",
        "volatile", "const", "float", "native", "super", "while", "true", "false"
    };

    public final static String[] JavaTypeKeywords = {
        "boolean", "double", "byte", "int", "short", "char", "void", "long", "float"
    };

    public final static String[] JavaOperators = {
        "=", ">", "<", "!", "~", "?", ":",
        "==", "<=", ">=", "!=", "&&", "||", "++", "--",
        "+", "-", "*", "/", "&", "|", "^", "%", "<<",
        ">>", ">>>", "+=", "-=", "*=", "/=", "&=", "|=",
        "^=", "%=", "<<=", ">>=", ">>>="
    };

    private State currentState = State.Normal;
    private Pattern keywordsPattern;
    private Pattern typeKeywordsPattern;
    private Pattern operatorsPattern;
    private Pattern symbolsPattern = Pattern.compile("^[=><!~?:&|+\\-*\\/\\^%]+");
    private Pattern variablePattern = Pattern.compile("^([a-zA-Z][a-zA-Z0-9]*|_[a-zA-Z0-9]*|_)");
    private Pattern numberPattern = Pattern.compile("^(0x[0-9]+|[0-9]+)");

    public JavaLang() {
        buildKeywordsPattern();
        buildTypekeywordsPattern();
        buildOperatorsPattern();
    }

    public List<String> tokenize(StringStream stream) {
        List<String> result = new ArrayList<>();
        int variableLen;
        if (stream.getChar() == '\n') {
            stream.moveForward();
        } else {
            switch (currentState) {
                case Normal:
                    if (stream.getChar() == ' ') {
                        stream.eatSpace();
                        return result;
                    } else if (stream.swallow("/*")) {
                        currentState = State.Comment;
                        result.add("comment");
                    } else if (stream.swallow("//")) {
                        stream.goToEnd();
                        result.add("comment");
                    } else if (stream.swallow("\"")) {
                        currentState = State.String;
                        result.add("string");
                    } else if (stream.swallow(numberPattern)) {
                        result.add("constant-numeric");
                    } else if (stream.getChar() == '@') {
                        stream.moveForward();
                        stream.swallow(variablePattern);
                        result.add("tag");
                    } else if ((variableLen = stream.match(variablePattern)) > 0) {
                        // maybe a variable or a keyword.
                        int keywordLen = stream.match(keywordsPattern);
                        if (variableLen == keywordLen) { // it's a keyword
                            result.add("keyword");
                        } else { // it's a variable
                            result.add("variable");
                        }
                        stream.moveForward(variableLen);
                    } else if (stream.swallow(operatorsPattern)) {
                        result.add("operators");
                        String topString = stream.getTopString();
                        if (topString.equals(";")) {
                            result.add("semicolon");
                        } else if (topString.equals("(")) {
                            result.add("paren");
                            result.add("left-paren");
                        } else if (topString.equals(")")) {
                            result.add("paren");
                            result.add("right-paren");
                        } else if (topString.equals("{")) {
                            result.add("brace");
                            result.add("left-brace");
                        } else if (topString.equals("}")) {
                            result.add("brace");
                            result.add("right-brace");
                        } else if (topString.equals("[")) {
                            result.add("bracket");
                            result.add("left-bracket");
                        } else if (topString.equals("]")) {
                            result.add("bracket");
                            result.add("right-bracket");
                        }
                    } else {
                        stream.moveForward();
                    }
                    break;
                case String:
                    result.add("string");
                    if (stream.getChar() == '\\') { // escape
                        stream.moveForward(2);
                    } else if (stream.swallow("\"")) {
                        currentState = State.Normal;
                    } else {
                        stream.moveForward();
                    }
                    break;
                case Comment:
                    result.add("comment");
                    if (stream.swallow("*/")) {
                        currentState = State.Normal;
                    } else {
                        stream.moveForward();
                    }
                    break;
            }
        }

        return result;
    }

    /**
     * sort keywords from long to short
     */
    private void sortPrePatternStr(String[] strs) {
        Arrays.sort(strs, (Object o1, Object o2) -> {
            return ((String)o2).length() - ((String)o1).length();
        });
    }

    private Pattern buildArrayPattern(String[] strs) {
        sortPrePatternStr(strs);

        StringBuilder sb = new StringBuilder();
        sb.append("^(");
        for (int i = 0; i < strs.length; i++) {
            sb.append(Pattern.quote(strs[i]));
            sb.append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return Pattern.compile(sb.toString());
    }

    private void buildKeywordsPattern() {
        keywordsPattern = buildArrayPattern(JavaKeywords);
    }

    private void buildTypekeywordsPattern() {
        typeKeywordsPattern = buildArrayPattern(JavaTypeKeywords);
    }

    private void buildOperatorsPattern() {
        operatorsPattern = buildArrayPattern(JavaOperators);
    }

    public State getCurrentState() {
        return currentState;
    }

    public Pattern getKeywordsPattern() {
        return keywordsPattern;
    }

    public Pattern getTypeKeywordsPattern() {
        return typeKeywordsPattern;
    }

    public Pattern getOperatorsPattern() {
        return operatorsPattern;
    }

    public Pattern getSymbolsPattern() {
        return symbolsPattern;
    }

}
