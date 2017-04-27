package View;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ZMYang on 2017/3/23.
 */
public class RightTable {
    private String choosedFile;
    private String path;
    private Button btCancel;

    public RightTable(File fileName) {
        choosedFile = fileName.getName();

        path = fileName.getAbsolutePath();

        btCancel = new Button("", new ImageView(new Image("file:///../image/NO2.png")));
        btCancel.setStyle("-fx-background-color:null");
        btCancel.setOnMouseEntered(e -> {
            btCancel.setStyle("-fx-background-color:lightblue");
        });
        btCancel.setOnMouseExited((e -> {
            btCancel.setStyle("-fx-background-color:null");
        }));
    }

    public void setButtonOnAction(File fileName, ArrayList<File> arrayList,
                                  ObservableList<RightTable> dataRight, ObservableList<MyFile> dataMiddle) {
        btCancel.setOnAction(e -> {
            for (int j = 0; j < dataMiddle.size(); j++){
                if(dataMiddle.get(j).getPath().equals(path)){
                    dataMiddle.get(j).getBtChoose().setDisable(false);
                    dataMiddle.get(j).getBtChoose().setText("点击选择");
                    dataMiddle.get(j).getBtChoose().setGraphic(new ImageView(new Image("file:///../image/YES3.png")));
                    break;
                }
            }
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getAbsolutePath().equals(fileName.getAbsolutePath())) {
                        arrayList.remove(i);
                    }
                }
            for (int i = 0; i < dataRight.size(); i++) {
                if (dataRight.get(i).path.equals(fileName.getAbsolutePath())) {
                    dataRight.remove(i);
                }
            }
//            for (int i = 0; i < arrayList.size(); i++) {
//                System.out.println(arrayList.get(i).getName());
//            }
//            System.out.println("finish!");
        });
    }

    public String getChoosedFile() {
        return choosedFile;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setChoosedFile(String choosedFile) {
        this.choosedFile = choosedFile;
    }

    public Button getBtCancel() {
        return btCancel;
    }

    public void setBtCancel(Button btCancel) {
        this.btCancel = btCancel;
    }
}
