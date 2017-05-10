package Model.LangSpec;

import Model.ITokenizer;
import Model.StringStream;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by cdzos on 2017/5/6.
 */
public class HTML implements ITokenizer {

    public static Pattern FirstLine = Pattern.compile("^<(?i:(!DOCTYPE\\\\s*)?html)");
    public static Pattern HTMLTagBegin = Pattern.compile("^(<)([a-zA-Z0-9:\\\\-]++)(?=[^>]*></\\\\2>)");
    public static Pattern HTMLTagEnd = Pattern.compile("^(>(<)/)(\\\\2)(>)");
    public static Pattern HTMLScriptBegin = Pattern.compile("^(?:^\\\\s+)?(<)((?i:script))\\\\b(?![^>]*/>)(?![^>]*(?i:type.?=.?text/((?!javascript).*)))");
    public static Pattern HTMLScriptEnd = Pattern.compile("^(?<=</(script|SCRIPT))(>)(?:\\\\s*\\\\n)?");
    public static Pattern CommentBegin = Pattern.compile("^<!--");
    public static Pattern CommentEnd = Pattern.compile("^--\\\\s*>");

    @Override
    public String[] tokenize(StringStream stream) {
        ArrayList<String> result = new ArrayList<>();
        return result.toArray(new String[result.size()]);
    }

}
