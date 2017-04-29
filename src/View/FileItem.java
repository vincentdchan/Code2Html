package View;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by cdzos on 2017/4/29.
 */
public class FileItem {
    File file;
    private String fileName;
    private String lastModified;
    private String length;
    private String type;
    private BooleanProperty check;
    private String path;
    private File basePath;

    FileItem(File file, File basePath) {
        this.file = file;
        this.basePath = basePath;
        fileName = file.getName();

        path = file.getAbsolutePath();
        check = new SimpleBooleanProperty();
        check.setValue(false);

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(file.lastModified());
        lastModified = fmt.format(date);

        long tempLength = file.length();
        if (tempLength > 1024 * 1024) {
            tempLength /= (1024 * 1024);
            length = (tempLength + 1) + " MB";
        } else if (tempLength > 1024) {
            tempLength /= 1024;
            length = (tempLength + 1) + " KB";
        } else {
            length = (tempLength + 1) + " B";
        }
        if (fileName.matches(".*\\..*")) {
            type = fileName.substring(fileName.lastIndexOf("."));
        } else {
            type = null;
        }


    }

    public boolean isChecked() {
        return check.get();
    }

    public BooleanProperty isCheckedProperty() {
        return check;
    }

    public void setChecked(boolean checked) {
        check.setValue(checked);
    }

    public String getPath() {
        String baseStr = basePath.getAbsolutePath();
        return path.replaceFirst(Pattern.quote(baseStr), "");
    }

    public void setPath(String path) {
        throw new NotImplementedException();
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModified() {
        return lastModified;
    }


    public String getType() {
        return type;
    }
}
