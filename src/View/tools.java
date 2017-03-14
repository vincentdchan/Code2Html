package View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

import java.io.File;

/**
 * Created by ZMYang on 2017/3/1.
 */
public class tools {
    public static void clickAction(Button child, TreeItem<Button> parent, File file, TableView<MyFile> table) {
        child.setStyle("-fx-background-color:null");
        child.setOnMouseEntered(e -> {
            child.setStyle("-fx-background-color:lightblue");
        });
        child.setOnMouseExited(e -> {
            child.setStyle("-fx-background-color:null");
        });
        child.setOnAction(e -> {
            File[] subFile = file.listFiles();
            ObservableList<MyFile> data = FXCollections.observableArrayList();
//            data.clear();
            parent.setExpanded(true);
            if (parent.isLeaf() && subFile != null) {
                for (int i = 0; i < subFile.length; i++) {
                    if (subFile[i].isDirectory() && !subFile[i].isHidden()) {
                        Button button1 = new Button(subFile[i].getName(), new ImageView("file:///../image/File.png"));
                        TreeItem<Button> button = new TreeItem<>(button1);
                        parent.getChildren().add(button);
                        clickAction(button1, button, new File(subFile[i].getPath()), table);  //递归调用
                    } else if (subFile[i].isFile() && subFile != null) {
                        MyFile myFile = new MyFile(subFile[i]);
                        if (myFile.getType() != null) {
                            if (myFile.getType().matches("\\.java") || myFile.getType().matches("\\.h") || myFile.getType().matches("\\.c")) {
                                data.add(myFile);
                                table.setItems(data);
                            }
                        }
                    }
                }
            }
        });
    }

}
