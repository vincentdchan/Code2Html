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
    private Button btCancel;

    public RightTable(File fileName, ArrayList<File> arrayList, ObservableList<RightTable> data) {
        choosedFile = fileName.getName();

        btCancel = new Button(" ", new ImageView(new Image("file:///../image/NO1.png")));
        btCancel.setStyle("-fx-background-color:null");
        btCancel.setOnMouseEntered(e -> {
            btCancel.setStyle("-fx-background-color:lightblue");
        });
        btCancel.setOnMouseExited((e -> {
            btCancel.setStyle("-fx-background-color:null");
        }));


        btCancel.setOnAction(e -> {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).equals(fileName)) {
                    arrayList.remove(i);
                }
            }
            for(int i = 0 ; i < data.size() ; i++){
                if(data.get(i).choosedFile.equals(fileName.getName())){
                    data.remove(i);
                }
            }
            for (int i = 0; i < arrayList.size(); i++) {
                System.out.println(arrayList.get(i).getName());
            }
            System.out.println("finish!");
        });
    }

    public String getChoosedFile() {
        return choosedFile;
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
