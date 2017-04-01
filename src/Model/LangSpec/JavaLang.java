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
    private Pattern SymbolsPattern = Pattern.compile("[=><!~?:&|+\\-*\\/\\^%]+");

    public JavaLang() {
        buildKeywordsPattern();
        buildTypekeywordsPattern();
        buildOperatorsPattern();
    }

    public String[] tokenize(StringStream stream) {
        List<String> result = new ArrayList<>();
        if (stream.getChar() == '\n') {
            stream.moveForward();
        } else {
            switch (currentState) {
                case Normal:
                    if (stream.getChar() == ' ') {
                        stream.eatSpace();
                        return new String[0];
                    } else if (stream.swallow(keywordsPattern)) {
                        result.add("keyword");
                    } else if (stream.swallow(operatorsPattern)) {
                        result.add("operators");
                    } else {
                        stream.moveForward();
                    }
                    break;
                case String:
                    stream.moveForward();
                    break;
                case Comment:
                    stream.moveForward();
                    break;
            }
        }

        return result.toArray(new String[result.size()]);
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
        sb.append("(");
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
        return SymbolsPattern;
    }

}
