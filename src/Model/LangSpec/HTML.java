package Model.LangSpec;

import Model.ITokenizer;
import Model.StringStream;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by cdzos on 2017/5/6.
 */
public class HTML implements ITokenizer {

    public static Pattern FirstLine = Pattern.compile("^<(?i:(!DOCTYPE\\\\s*)?html)");
    public static Pattern HTMLTagBegin = Pattern.compile("^(</?)([a-zA-Z0-9:\\\\-]++)(?=[^>]*)");
    public static Pattern SwallowHTMLTagBegin = Pattern.compile("^</?");
    public static Pattern HTMLStructureTag = Pattern.compile("^(body|head|html)");
    public static Pattern HTMLInlineTag = Pattern.compile("^(abbr|acronym|area|base|basefont|bdo|big|br|button|caption|cite|code|col|colgroup|del|dfn|em|font|head|html|img|input|ins|isindex|kbd|label|legend|link|li|map|meta|noscript|optgroup|option|param|samp|script|select|small|span|strike|strong|style|sub|sup|table|tbody|td|textarea|tfoot|th|thead|title|tr|tt|u|var|s|q|a|b|i)");
    public static Pattern HTMLAnyTag = Pattern.compile("^(address|blockquote|dd|div|section|article|aside|header|footer|nav|menu|dl|dt|fieldset|form|frame|frameset|h1|h2|h3|h4|h5|h6|iframe|noframes|object|ol|p|ul|applet|center|dir|hr|pre)");
    public static Pattern HTMLTagEnd = Pattern.compile("^/?>");
    public static Pattern HTMLScriptBegin = Pattern.compile("^(?:^\\\\s+)?(<)((?i:script))\\\\b(?![^>]*/>)(?![^>]*(?i:type.?=.?text/((?!javascript).*)))");
    public static Pattern HTMLScriptEnd = Pattern.compile("^(?<=</(script|SCRIPT))(>)(?:\\\\s*\\\\n)?");
    public static Pattern HTMLString = Pattern.compile("^\"(\\.|[^\"])+\"");
    public static Pattern CommentBegin = Pattern.compile("^<!--");
    public static Pattern CommentEnd = Pattern.compile("^-->");
    public static Pattern Word = Pattern.compile("^[a-zA-Z](-|_|[a-zA-Z0-9])*");

    private enum State {
        Normal,
        Comment,
        InTag,
    }

    private State state = State.Normal;
    private boolean findTag = false;

    @Override
    public String[] tokenize(StringStream ss) {
        ArrayList<String> result = new ArrayList<>();
        switch (state) {
            case Normal:
                if (ss.test(HTMLTagBegin)) {
                    ss.swallow(SwallowHTMLTagBegin);
                    result.add("punctuation-definition-tag-begin");
                    state = State.InTag;
                    findTag = false;
                } else if (ss.swallow(CommentBegin)) {
                    result.add("comment");
                    state = State.Comment;
                } else {
                    ss.moveForward();
                }
                break;
            case InTag:
                if (!findTag) {
                    if (ss.test(Word)) {
                        if (ss.swallow(HTMLStructureTag) || ss.swallow(HTMLInlineTag) || ss.swallow(HTMLAnyTag)) {
                            result.add("constant-character");
                            findTag = true;
                        } else {
                            ss.swallow(Word);
                        }
                    } else {
                        ss.moveForward();
                    }
                    findTag = true;
                } else if (ss.swallow(Word)) {
                    result.add("variable");
                } else if (ss.swallow(HTMLTagEnd)) {
                    result.add("punctuation-definition-tag-end");
                    state = State.Normal;
                } else if (ss.swallow(HTMLString)) {
                    result.add("string");
                } else {
                    ss.moveForward();
                }
                break;
            case Comment:
                result.add("comment");
                if (ss.swallow(CommentEnd)) {
                    state = State.Normal;
                } else {
                    ss.moveForward();
                }
                break;
        }
        return result.toArray(new String[result.size()]);
    }

}
