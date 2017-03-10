package Model;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by duzhong on 17-3-11.
 *
 * Read data from `tmLanguage`, and to generate the tokenize information
 *
 */
public final class TmLangLoader {

    TmLangLoader(String rawData) throws ParseException {
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(rawData);
    }

}
