package View;

import Model.Configuration;
import Model.Getter;
import Model.Java2Html;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by ZMYang on 2017/3/1.
 */
public class Actions {
    public static void clickAction(Button child, TreeItem<Button> parent, File file,
                                   TableView<MyFile> table, ArrayList<File> arrayList,
                                   ComboBox<String> showFileKind, TextField label,
                                   TableView tableView, ObservableList<RightTable> dataRight,
                                   ObservableList<MyFile> dataMiddle) {
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
            dataMiddle.clear();
            table.setItems(null);
            parent.setExpanded(true);
            if (parent.isLeaf() && subFile != null) {
                for (int i = 0; i < subFile.length; i++) {
                    if (subFile[i].isDirectory() && !subFile[i].isHidden()) {
                        Button button1 = new Button(subFile[i].getName(), new ImageView("file:///../image/File.png"));
                        TreeItem<Button> button = new TreeItem<>(button1);
                        parent.getChildren().add(button);
                        clickAction(button1, button, new File(subFile[i].getPath()), table, arrayList, showFileKind,
                                label, tableView, dataRight, dataMiddle);  //递归调用
                    }

                }
            }
            if (subFile != null) {
                for (int i = 0; i < subFile.length; i++) {
                    if (subFile[i].isFile()) {
                        MyFile myFile = new MyFile(subFile[i], arrayList, tableView, dataRight, dataMiddle);
                        if (myFile.getType() != null) {
                            if (showFileKind.getValue().equals("all") && (myFile.getType().matches("\\.java")
                                    || myFile.getType().matches("\\.h") || myFile.getType().matches("\\.c"))) {
                                data.add(myFile);
                                dataMiddle.add(myFile);
                                table.setItems(data);
                            } else if (showFileKind.getValue().equals(".java") && myFile.getType().matches("\\.java")) {
                                data.add((myFile));
                                dataMiddle.add(myFile);
                                table.setItems(data);
                            } else if (showFileKind.getValue().equals(".c") && myFile.getType().matches("\\.c")) {
                                data.add((myFile));
                                dataMiddle.add(myFile);
                                table.setItems(data);
                            } else if (showFileKind.getValue().equals(".h") && myFile.getType().matches("\\.h")) {
                                data.add((myFile));
                                dataMiddle.add(myFile);
                                table.setItems(data);
                            }
                        }
                    }
                }
            }
            showFileKind.setOnAction(E -> {
                data.clear();
                dataMiddle.clear();
                for (int i = 0; i < subFile.length; i++) {
                    if (subFile[i].isFile()) {
                        MyFile myFile = new MyFile(subFile[i], arrayList, tableView, dataRight, dataMiddle);
                        if (myFile.getType() != null) {
                            if (showFileKind.getValue().equals("all") && (myFile.getType().matches("\\.java")
                                    || myFile.getType().matches("\\.h") || myFile.getType().matches("\\.c"))) {
                                data.add(myFile);
                                dataMiddle.add(myFile);
                                table.setItems(data);
                            } else if (showFileKind.getValue().equals(".java") && myFile.getType().matches("\\.java")) {
                                data.add((myFile));
                                dataMiddle.add(myFile);
                                table.setItems(data);
                            } else if (showFileKind.getValue().equals(".c") && myFile.getType().matches("\\.c")) {
                                data.add((myFile));
                                dataMiddle.add(myFile);
                                table.setItems(data);
                            } else if (showFileKind.getValue().equals(".h") && myFile.getType().matches("\\.h")) {
                                data.add((myFile));
                                dataMiddle.add(myFile);
                                table.setItems(data);
                            }
                        }
                    }
                }
            });
        });
    }

    public static void codeAction(Button button, ArrayList<File> arrayList, String filePath) {
        button.setOnAction(e -> {
            if (arrayList.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.NONE, "请选定要转换的文件!", ButtonType.OK);
                alert.setTitle("通知");
                alert.showAndWait();
                return;
            }
            String[] Name = new String[100];
            for (int i = 0; i < arrayList.size(); i++) {
                Name[i] = arrayList.get(i).getName();
            }
            String[] add = new String[10];
            add[0] = "(1)";
            add[1] = "(2)";
            add[2] = "(3)";
            add[3] = "(4)";
            add[4] = "(5)";
            add[5] = "(6)";
            add[6] = "(7)";
            add[7] = "(8)";
            add[8] = "(9)";
            add[9] = "(10)";
            int t = 0;
            for (int i = 0; i < arrayList.size(); i++) {
                t = 0;
                for (int j = i + 1; j < arrayList.size(); j++) {
                    if (Name[i].equals(arrayList.get(j).getName())) {
                        Name[j] = Name[i] + add[t];
                        t++;
                    }
                }
            }
            for (int i = 0; i < arrayList.size(); i++) {
                File file = arrayList.get(i);
                try {
                    String content = new String(
                            Files.readAllBytes(Paths.get(file.getAbsolutePath()))
                    );
                    Configuration config = new Configuration();
                    Java2Html converter = new Java2Html();
                    converter.set_config(config);
                    converter.addGetter(new Getter(arrayList, Name[i], filePath));
                    converter.convert(file.getName(), content);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (Model.Generator.NotSupportedFiletypes e2) {
                    e2.printStackTrace();
                }
            }
            WebShow.happen(arrayList, filePath, Name);
        });
    }

    public static void sortArrayList(ArrayList<File> Code2Html, ArrayList<Button> buttonList, String[] strings, VBox leftPane, boolean choose) {
        File[] files = new File[Code2Html.size()];
        Button[] buttons = new Button[buttonList.size()];
        Node[] toolBars = new Node[leftPane.getChildren().size()];
//        System.out.println(Code2Html.size());
        for (int i = 0; i < Code2Html.size(); i++) {
            files[i] = Code2Html.get(i);
            buttons[i] = buttonList.get(i);
            toolBars[i] = leftPane.getChildren().get(2 * i + 1);
        }
        for (int i = files.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (files[j + 1].getName().compareTo(files[j].getName()) <= 0) {
                    File tf = files[j];
                    files[j] = files[j + 1];
                    files[j + 1] = tf;

                    Button bf = buttons[j];
                    buttons[j] = buttons[j + 1];
                    buttons[j + 1] = bf;

                    String ts = strings[j];
                    strings[j] = strings[j + 1];
                    strings[j + 1] = ts;

                    Node nt = toolBars[j];
                    toolBars[j] = toolBars[j + 1];
                    toolBars[j + 1] = nt;
                }
            }
        }
        Code2Html.clear();
        buttonList.clear();
        leftPane.getChildren().remove(1, leftPane.getChildren().size());
        if (choose) {
            for (int i = 0; i < files.length; i++) {
                Code2Html.add(files[i]);
                buttonList.add(buttons[i]);
                leftPane.getChildren().addAll(toolBars[i], new Separator());
//            System.out.println(toolBars[i]);
            }
        } else {
            for (int i = files.length - 1; i >= 0; i--) {
                Code2Html.add(files[i]);
                buttonList.add(buttons[i]);
                leftPane.getChildren().addAll(toolBars[i], new Separator());
            }
        }
    }

}
