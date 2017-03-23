package View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ZMYang on 2017/3/1.
 */
public class Actions {
    public static void clickAction(Button child, TreeItem<Button> parent, File file,
                                   TableView<MyFile> table, ArrayList<File> arrayList,
                                   ComboBox<String> showFileKind, TextField label) {
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
                        clickAction(button1, button, new File(subFile[i].getPath()), table, arrayList, showFileKind, label);  //递归调用
                    }

                }
            }
            if (subFile != null) {
                for (int i = 0; i < subFile.length; i++) {
                    if (subFile[i].isFile()) {
                        MyFile myFile = new MyFile(subFile[i], arrayList);
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
                        MyFile myFile = new MyFile(subFile[i], arrayList);
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

//    public static void comboBoxAction(ComboBox<String> showFileKind, File subFile,
//                                      ArrayList<File> arrayList, TableView<MyFile> table,
//                                      ObservableList<MyFile> data) {
//        showFileKind.setOnAction(e -> {
//
//        });
//    }
//
//    public static void showFileAction(File subFile, ArrayList<File> arrayList,
//                                      ComboBox<String> showFileKind, TableView<MyFile> table,
//                                      ObservableList<MyFile> data) {
//        MyFile myFile = new MyFile(subFile, arrayList);
//        data = FXCollections.observableArrayList();
//        if (myFile.getType() != null) {
//            if (showFileKind.getValue().equals("all") && (myFile.getType().matches("\\.java")
//                    || myFile.getType().matches("\\.h") || myFile.getType().matches("\\.c"))) {
//                data.add(myFile);
//                table.setItems(data);
//            } else if (showFileKind.getValue().equals(".java") && myFile.getType().matches("\\.java")) {
//                data.add((myFile));
//                table.setItems(data);
//            } else if (showFileKind.getValue().equals(".c") && myFile.getType().matches("\\.c")) {
//                data.add((myFile));
//                table.setItems(data);
//            } else if (showFileKind.getValue().equals(".h") && myFile.getType().matches("\\.h")) {
//                data.add((myFile));
//                table.setItems(data);
//            }
//        }
//    }
}
