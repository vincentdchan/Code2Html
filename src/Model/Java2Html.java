package Model;

import Model.TmLang.TmLang;
import Model.TmLang.TmLangLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by duzhong on 17-3-4.
 */

public final class Java2Html {

    private static ArrayList<TmLang> tmLangs;
    private static HashMap<String, LinkedList<TmLang>> _filetypesMap;

    private ArrayList<IResultGetter> _getters;

    private Configuration _config;

    public Java2Html() {
        _getters = new ArrayList<IResultGetter>();
    }

    public static boolean isLoadedTmLangs() {
        return tmLangs != null;
    }

    public static void loadTmLangs() throws IOException {
        tmLangs = new ArrayList<>();
        _filetypesMap = new HashMap<>();

        File directory = new File("./tmLanguages");
        if (!directory.exists()) throw new FileNotFoundException();
        File[] files = directory.listFiles();
        for(File _file : files) {
            if ( _file.getName().endsWith(".json")) {
                loadJson(_file);
            }
        }

        cachedFileTypes();
    }

    private static void loadJson(File file) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(file.getPath())));

        TmLangLoader loader;
        try {
            loader = new TmLangLoader(content);
        } catch (org.json.simple.parser.ParseException e) {
            System.out.print("Parse tmLanguage error: " + file.getName());
            e.printStackTrace();
            return;
        } catch (Model.TmLang.TmLangLoader.TmLangParseError e) {
            System.out.print("Parse tmLanguage error: " + file.getName());
            e.printStackTrace();
            return;
        }

        TmLang spec = loader.get_langSpec();
        tmLangs.add(spec);
    }

    private static void cachedFileTypes() {
        for (TmLang _tmLang : tmLangs) {
            String[] fileTypes = _tmLang.get_fileTypes();

            for (String _fileTypes : fileTypes) {
                LinkedList<TmLang> _ftList = _filetypesMap.get(_fileTypes);
                if (_ftList == null) {
                    _ftList = new LinkedList<>();
                    _filetypesMap.put(_fileTypes, _ftList);
                }
                _ftList.add(_tmLang);
            }
        }
    }

    /**
     *
     * @param filename identify the langurage of the code according to filename
     * @param srcCode the content of the code
     */
    public void convert(String filename, String srcCode) {
        String result = "";
        for (IResultGetter getter : _getters) {
            getter.getResult(result);
        }
    }

    public void addGetter(IResultGetter getter) {
        _getters.add(getter);
    }

    public void removeGetter(IResultGetter getter) {
        _getters.remove(getter);
    }

    public void set_config(Configuration _config) {
        this._config = _config;
    }

    public Configuration get_config() {
        return _config;
    }

}
