package Model.LangSpec;

import Model.ITokenizer;
import Model.StringStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by duzhong on 17-3-30.
 */
public class CLang implements ITokenizer {

    public enum State {
        Normal,
        Comment,
        StringConstant,
        Macro,
    }

    public static Pattern NumberPattern = Pattern.compile("^\\b((0(x|X)[0-9a-fA-F]*)|(0(b|B)[01]*)|(([0-9]+\\.?[0-9]*)|(\\.[0-9]+))((e|E)(\\+|-)?[0-9]+)?)(L|l|UL|ul|u|U|F|f|ll|LL|ull|ULL)?\\b");
    public static Object RawOperatorsPatterns[][] = {
            {Pattern.compile("^(?<![\\w$])(sizeof)(?![\\w$])"), "sizeof"},
            {Pattern.compile("^--"), "decrement"},
            {Pattern.compile("^\\+\\+"), "increment"},
            {Pattern.compile("^(%=|\\+=|-=|\\*=|(?<!\\()/=)"), "assignment"},
            {Pattern.compile("^(&=|\\^=|<<=|>>=|\\|=)"), "assignment-compound-bitwise"},
            {Pattern.compile("^(<<|>>)"), "bitwise-shift"},
            {Pattern.compile("^(!=|<=|>=|==|<|>)"), "comparision"},
            {Pattern.compile("^(&&|!|\\|\\|)"), "logic"},
            {Pattern.compile("^(&|\\||\\^|~)"), ""},
            {Pattern.compile("^="), "assignment"},
            // {Pattern.compile("^%|\\*|/|-|\\+"), ""},
    };

    public static Pattern MacroKeyword = Pattern.compile("^\\b(defined|define|ifdef|endif|ifndef|elif|else|if|include)\\b");
    public static Pattern MacroConstant = Pattern.compile("^(_|[A-Z])+");
    public static Pattern Word = Pattern.compile("^(_|[a-zA-Z])(_|[0-9a-zA-Z])*");

    public static Object PreservedPatterns[][] = {{
            Pattern.compile(
                    "^\\b(break|case|continue|default|do|else|for|goto|if|_Pragma|return|switch|while)\\b"),
            "keyword",
    }, {
        Pattern.compile("^\\b(asm|__asm__|auto|bool|_Bool|char|_Complex|double|enum|float|_Imaginary|int|long|short|signed|struct|typedef|union|unsigned|void)\\b"),
            "constant-other",
    }, {
        Pattern.compile("^\\b(const|extern|register|restrict|static|volatile|inline)\\b"),
            "constant-language",
    }, {
        Pattern.compile("^\\b(NULL|true|false|TRUE|FALSE)\\b"),
            "constant-character",
    }, {
        Pattern.compile("^\\b(u_char|u_short|u_int|u_long|ushort|uint|u_quad_t|quad_t|qaddr_t|caddr_t|daddr_t|dev_t|fixpt_t|blkcnt_t|blksize_t|gid_t|in_addr_t|in_port_t|ino_t|key_t|mode_t|nlink_t|id_t|pid_t|off_t|segsz_t|swblk_t|uid_t|id_t|clock_t|size_t|ssize_t|time_t|useconds_t|suseconds_t)\\b"),
            "constant-other",
    }, {
        Pattern.compile("^(?x) \\b\n(int8_t|int16_t|int32_t|int64_t|uint8_t|uint16_t|uint32_t|uint64_t|int_least8_t\n|int_least16_t|int_least32_t|int_least64_t|uint_least8_t|uint_least16_t|uint_least32_t\n|uint_least64_t|int_fast8_t|int_fast16_t|int_fast32_t|int_fast64_t|uint_fast8_t\n|uint_fast16_t|uint_fast32_t|uint_fast64_t|intptr_t|uintptr_t|intmax_t|intmax_t\n|uintmax_t|uintmax_t)\n\\b"),
            "constant-other",
    }};

    private State state = State.Normal;
    private Stack<State> stateStack = new Stack<>();

    @Override
    public List<String> tokenize(StringStream ss) {
        ArrayList<String> result = new ArrayList<>();
        switch (state) {
            case Normal:
                if (ss.isFirstChar() && ss.getChar() == '#') {
                    state = State.Macro;
                    result.add("macro");
                    ss.moveForward();
                } else if (ss.swallow("//")) {
                    result.add("comment");
                    ss.goToEnd();
                } else if (ss.swallow("/*")) {
                    result.add("comment");
                    state = State.Comment;
                } else if (ss.swallow("\"")) {
                    stateStack.push(state);
                    state = State.StringConstant;
                    result.add("string");
                } else if (swallowOperators(ss, result) ||
                        swallowPreserved(ss, result)) {
                    // do nothing
                } else if (ss.swallow(NumberPattern)) {
                    result.add("constant-numeric");
                } else if (ss.swallow("#")) {
                    state = State.Macro;
                    result.add("macro");
                } else if (ss.swallow(Word)) {
                } else {
                    ss.moveForward();
                }
                break;
            case Macro:
                result.add("macro");
                if (ss.isLastChar()) {
                    if (ss.getChar() != '\\') {
                        state = State.Normal;
                    }
                    ss.moveForward();
                } else if (ss.swallow("\"")) {
                    stateStack.push(state);
                    state = State.StringConstant;
                    result.add("string");
                } else if (ss.swallow(MacroKeyword)) {
                    result.add("keyword");
                } else if (ss.swallow(NumberPattern)) {
                    result.add("constant-numeric");
                } else if (ss.swallow(Word)) {
                    String word = ss.getTopString();
                    Matcher matcher = MacroConstant.matcher(word);
                    if (matcher.find()) {
                        result.add("constant-character");
                    }
                } else {
                    ss.moveForward();
                }
                break;
            case Comment:
                result.add("comment");
                if (ss.swallow("*/")) {
                    state = State.Normal;
                } else {
                    ss.moveForward();
                }
                break;
            case StringConstant:
                result.add("string");
                if (ss.getChar() == '\\') { // escape
                    ss.moveForward(2);
                } else if (ss.getChar() == '"') {
                    state = stateStack.pop();
                    ss.moveForward();
                } else {
                    ss.moveForward();
                }
                break;
        }
        return result;
    }

    private boolean swallowOperators(StringStream ss, ArrayList<String> tokens) {
        return swallowPattern(ss, tokens, RawOperatorsPatterns);
    }

    private boolean swallowPreserved(StringStream ss, ArrayList<String> tokens) {
        return swallowPattern(ss, tokens, PreservedPatterns);
    }

    private boolean swallowPattern(StringStream ss, ArrayList<String> tokens, Object[][] pat) {
        for (Object[] pair : pat) {
            Pattern pattern = (Pattern)pair[0];
            String token = (String)pair[1];
            if (ss.swallow(pattern)) {
                tokens.add(token);
                return true;
            }
        }
        return false;
    }

}
