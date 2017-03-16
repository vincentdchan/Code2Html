import Model.Java2Html;
import sun.font.Script;

import java.io.IOException;
import javax.script.*;

/**
 * Created by duzhong on 17-3-15.
 */

public class Main {

    public static void main(String[] args) {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("nashorn");
        try {
            engine.eval("load('scripts/main.js');");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

}
