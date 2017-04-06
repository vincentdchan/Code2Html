package View;

import Model.Configuration;
import Model.Getter;
import Model.Java2Html;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by ZMYang on 2017/3/1.
 */
public class Actions {
    public static void clickAction(Button child, TreeItem<Button> parent, File file,
                                   TableView<MyFile> table, ArrayList<File> arrayList,
                                   ComboBox<String> showFileKind, TextField label,
                                   TableView tableView, ObservableList<RightTable> dataRight) {
        child.setStyle("-fx-background-color:null");
        child.setOnMouseEntered(e -> {
            child.setStyle("-fx-background-color:lightblue");
        });
        child.setOnMouseExited(e -> {
            child.setStyle("-fx-background-color:null");
        });
        ObservableList<MyFile> data = FXCollections.observableArrayList();
        child.setOnAction(e -> {
            label.setText(file.getAbsolutePath());
            File[] subFile = file.listFiles();
            data.clear();
            table.setItems(null);
            parent.setExpanded(true);
            if (parent.isLeaf() && subFile != null) {
                for (int i = 0; i < subFile.length; i++) {
                    if (subFile[i].isDirectory() && !subFile[i].isHidden()) {
                        Button button1 = new Button(subFile[i].getName(), new ImageView("file:///../image/File.png"));
                        TreeItem<Button> button = new TreeItem<>(button1);
                        parent.getChildren().add(button);
                        clickAction(button1, button, new File(subFile[i].getPath()), table, arrayList, showFileKind, label, tableView, dataRight);  //递归调用
                    }

                }
            }
            if (subFile != null) {
                for (int i = 0; i < subFile.length; i++) {
                    if (subFile[i].isFile()) {
                        MyFile myFile = new MyFile(subFile[i], arrayList, tableView, dataRight);
                        if (myFile.getType() != null) {
                            if (showFileKind.getValue().equals("all") && (myFile.getType().matches("\\.java")
                                    || myFile.getType().matches("\\.h") || myFile.getType().matches("\\.c"))) {
                                data.add(myFile);
                                table.setItems(data);
                            } else if (showFileKind.getValue().equals(".java") && myFile.getType().matches("\\.java")) {
                                data.add((myFile));
                                table.setItems(data);
                            } else if (showFileKind.getValue().equals(".c") && myFile.getType().matches("\\.c")) {
                                data.add((myFile));
                                table.setItems(data);
                            } else if (showFileKind.getValue().equals(".h") && myFile.getType().matches("\\.h")) {
                                data.add((myFile));
                                table.setItems(data);
                            }
                        }
                    }
                }
            }
            showFileKind.setOnAction(E -> {
                data.clear();
                for (int i = 0; i < subFile.length; i++) {
                    if (subFile[i].isFile()) {
                        MyFile myFile = new MyFile(subFile[i], arrayList, tableView, dataRight);
                        if (myFile.getType() != null) {
                            if (showFileKind.getValue().equals("all") && (myFile.getType().matches("\\.java")
                                    || myFile.getType().matches("\\.h") || myFile.getType().matches("\\.c"))) {
                                data.add(myFile);
                                table.setItems(data);
                            } else if (showFileKind.getValue().equals(".java") && myFile.getType().matches("\\.java")) {
                                data.add((myFile));
                                table.setItems(data);
                            } else if (showFileKind.getValue().equals(".c") && myFile.getType().matches("\\.c")) {
                                data.add((myFile));
                                table.setItems(data);
                            } else if (showFileKind.getValue().equals(".h") && myFile.getType().matches("\\.h")) {
                                data.add((myFile));
                                table.setItems(data);
                            }
                        }
                    }
                }
            });
        });
    }

    public static void codeAction(Button button , ArrayList<File> arrayList) {
        button.setOnAction(e -> {
            if(arrayList.size() == 0){
                Alert alert = new Alert(Alert.AlertType.NONE , "请选定要转换的文件!",ButtonType.OK);
                alert.setTitle("通知");
                alert.showAndWait();
            }
            for(int i = 0; i < arrayList.size() ; i++){
                File file = arrayList.get(i);
                String content = "";
                try {
                    Scanner in = new Scanner(file,"utf-8");
                    while(in.hasNextLine()){
                        content = content + in.nextLine();
                    }
                    Configuration config = new Configuration();
                    Java2Html converter = new Java2Html();
                    converter.set_config(config);
                    converter.addGetter(new Getter());
                    converter.convert(file.getName(),content);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
