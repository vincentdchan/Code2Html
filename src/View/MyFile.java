package View;

import javafx.scene.control.Button;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by ZMYang on 2017/3/12.
 */
public class MyFile {
    private String fileName;
    private String lastModified;
    private long length;
    private String type;
    private Button btChoose;

    MyFile(File file){
        fileName = file.getName();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(file.lastModified());
        lastModified = fmt.format(date);
        length = file.length() / 1024;
        if(fileName.matches(".*\\..*")){
            type = fileName.substring(fileName.lastIndexOf("."));
        }else{
            type = null;
        }
//        btChoose = new Button(" ","Image/Yes1.png");
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public void setLength(long length) {
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

    public long getLength() {
        return length;
    }

    public String getType() {
        return type;
    }
}
