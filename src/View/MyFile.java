package View;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ZMYang on 2017/3/12.
 */
public class MyFile {
    File file;
    private String fileName;
    private String lastModified;
    private String length;
    private String type;
    private Button btChoose;
    private String choosedFile;
    private Button btCancel;

    MyFile(File file, ArrayList<File> arrayList) {
        this.file = file;
        fileName = file.getName();

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

        choosedFile = null;

        btCancel = new Button(" ", new ImageView(new Image("file:///../image/NO.png")));
        btCancel.setStyle("-fx-background-color:null");
        btCancel.setOnMouseEntered(e -> {
            btCancel.setStyle("-fx-background-color:lightblue");
        });
        btCancel.setOnMouseExited((e -> {
            btCancel.setStyle("-fx-background-color:null");
        }));
        btCancel.setOnAction(e -> {
            for(int i = 0 ; i < arrayList.size() ; i++){
                if(arrayList.get(i).equals(fileName)){
                    arrayList.remove(i);
                }
            }
            for (int i = 0; i < arrayList.size(); i++) {
                System.out.println(arrayList.get(i).getName());
            }
            System.out.println("finish!");
        });

        btChoose = new Button(" ", new ImageView(new Image("file:///../image/YES2.png")));
        btChoose.setStyle("-fx-background-color:null");
        btChoose.setOnMouseEntered(e -> {
            btChoose.setStyle("-fx-background-color:lightblue");
        });
        btChoose.setOnMouseExited((e -> {
            btChoose.setStyle("-fx-background-color:null");
        }));
        btChoose.setOnAction(e -> {
            boolean flag = true;
            for (int i = 0; i < arrayList.size(); i++) {
                if (file.getName().equals(arrayList.get(i).getName())) {
                    flag = false;
//                    System.out.println("exist!");
                }
            }
            if (flag) {
                choosedFile = fileName;
                arrayList.add(file);
            }
            for (int i = 0; i < arrayList.size(); i++) {
                System.out.println(arrayList.get(i).getName());
            }
            System.out.println("finish!");
        });
    }


    public Button getBtCancel() {
        return btCancel;
    }

    public void setBtCancel(Button btCancel) {
        this.btCancel = btCancel;
    }

    public String getChoosedFile() {
        return choosedFile;
    }

    public void setChoosedFile(String choosedFile) {
        this.choosedFile = choosedFile;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Button getBtChoose() {
        return btChoose;
    }

    public void setBtChoose(Button btChoose) {
        this.btChoose = btChoose;
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
