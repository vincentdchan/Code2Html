package View;
/**
 * Created by ZMYang on 2017/4/5.
 */

import Model.Configuration;
import Model.Getter;
import Model.Java2Html;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class WebShow extends Application {

    private static ArrayList<File> Code2Html;
    private static String FilePath;
    private static String[] codeName;

    public static void happen(ArrayList<File> arrayList, String filePath, String[] name) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                new WebShow().start(new Stage());
            }
        });
        Code2Html = arrayList;
        FilePath = filePath;
        codeName = name;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ArrayList<Button> buttonList = new ArrayList<>();

        BorderPane borderPane = new BorderPane();

//        File filePath = new File(FilePath);
//        File file = new File(FilePath + "/" + Code2Html.get(0).getName() + ".html");
        File file = new File(FilePath + "/" + codeName[0] + ".html");
        VBox topPane = new VBox();

//        MenuBar menuBar = new MenuBar();
//        Menu menu = new Menu("Menu");
        Button openWithBrowse = new Button("在浏览器打开");

//        menu.getItems().addAll(openWithBrowse);
//        menuBar.getMenus().addAll(menu);

        Button btAdd = new Button("添加", new ImageView(new Image("file:///../image/add.png")));
        btAdd.setStyle("-fx-background-color:null");
        btAdd.setOnMouseEntered(e -> {
            btAdd.setStyle("-fx-background-color:lightblue");
        });
        btAdd.setOnMouseExited(e -> {
            btAdd.setStyle("-fx-background-color:null");
        });

        Button btDeleteAll = new Button("全部删除", new ImageView(new Image("file:///../image/Delete.png")));
        btDeleteAll.setStyle("-fx-background-color:null");
        btDeleteAll.setOnMouseEntered(e -> {
            btDeleteAll.setStyle("-fx-background-color:lightblue");
        });
        btDeleteAll.setOnMouseExited(e -> {
            btDeleteAll.setStyle("-fx-background-color:null");
        });

        ComboBox<Integer> sizeOfFont = new ComboBox<>();
        for (int i = 10; i <= 48; i += 2) {
            sizeOfFont.getItems().add(i);
        }
        sizeOfFont.setValue(14);

        ComboBox<String> styleOfTheme = new ComboBox<>();
        styleOfTheme.getItems().addAll("8-Colour-Dark.tmTheme",
                "1337.tmTheme",
                "2112.tmTheme",
                "3024_Day.tmTheme",
                "3024_Night.tmTheme",
                "Abyss.tmTheme",
                "Acai.tmTheme",
                "Active4D.tmTheme",
                "Anarchist.tmTheme",
                "Angular-io-Code.tmTheme",
                "Array.tmTheme",
                "Codecademy.tmTheme",
                "dimmed-monokai.tmTheme",
                "Facebook.tmTheme",
                "Flatland_Dark.tmTheme",
                "Flatland_Monokai.tmTheme");
        styleOfTheme.setValue("1337.tmTheme");

        CheckBox chkShowLine = new CheckBox("显示行号");
        chkShowLine.setSelected(true);

        ToolBar toolBar = new ToolBar(
                btAdd,
                btDeleteAll,
                new Separator(),
                new Label("字号："),
                sizeOfFont,
                new Label("主题："),
                styleOfTheme,
                chkShowLine,
                new Separator(),
                openWithBrowse
        );


        topPane.getChildren().addAll(toolBar);

        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load("file:///" + file.getAbsolutePath());
        primaryStage.setTitle(codeName[0] + ".html");
//        primaryStage.setTitle(Code2Html.get(0).getName() + ".html");


        VBox leftPane = new VBox();
        Button ascending = new Button("升");
        Button descending = new Button("降");
        Button kind = new Button("类型");
        ToolBar leftToolBar = new ToolBar(
                ascending,
                descending,
                kind
        );
        leftPane.getChildren().add(leftToolBar);
        btDeleteAll.setOnAction(e -> {
            leftPane.getChildren().clear();
            leftPane.getChildren().add(leftToolBar);
            webEngine.load(null);
            Code2Html.clear();
            buttonList.clear();
        });
//        leftPane.setPadding(new Insets(5, 2, 5, 2));
        leftPane.setSpacing(5);
        for (int i = 0; i < Code2Html.size(); i++) {
            final int t = i;
            Separator separator = new Separator();
            Button button = new Button(codeName[i] + ".html");
//            Button button = new Button(Code2Html.get(i).getName() + ".html");
            Button btDelete = new Button("", new ImageView(new Image("file:///../image/NO2.png")));
            File anotherFile = new File(FilePath + "/" + codeName[i] + ".html");
//            File anotherFile = new File(FilePath + "/" + Code2Html.get(i).getName() + ".html");
            if (i == 0) {
                button.setStyle("-fx-background-color:lightblue");
            } else {
                button.setStyle("-fx-background-color:null");
            }
            btDelete.setStyle("-fx-background-color:null");
            btDelete.setOnMouseEntered(e -> {
                btDelete.setStyle("-fx-background-color:lightblue");
            });
            btDelete.setOnMouseExited(e -> {
                btDelete.setStyle("-fx-background-color:null");
            });
            ToolBar comboButton = new ToolBar(
                    button,
                    btDelete
            );
            btDelete.setOnAction(e -> {
//                final int p = t;
                leftPane.getChildren().remove(comboButton);
                leftPane.getChildren().remove(separator);
                webEngine.load(null);
                for (int j = 0; j < Code2Html.size(); j++) {
                    if (Code2Html.get(j).getName().equals(anotherFile.getName().substring(0, anotherFile.getName().indexOf(".html")))) {
                        Code2Html.remove(j);
                        buttonList.remove(j);
                        break;
                    }
                }
            });
            btDelete.setPrefHeight(comboButton.getPrefHeight());
            comboButton.setStyle("-fx-background-color:null");
            buttonList.add(button);
            button.setOnAction(e -> {
                for (int j = 0; j < buttonList.size(); j++) {
                    buttonList.get(j).setStyle("-fx-background-color:null");
                }
                button.setStyle("-fx-background-color:lightblue");
//                System.out.println(button.getTextFill());
                if (anotherFile.exists()) {
//                    try {
//                        String content = new String(
//                                Files.readAllBytes(Paths.get(Code2Html.get(t).getAbsolutePath()))
//                        );
//                        Configuration config = new Configuration();
//                        config.set_fontSize(sizeOfFont.getValue());
//                        config.set_styleName(styleOfTheme.getValue() + ".css");
//                        Java2Html converter = new Java2Html();
//                        converter.set_config(config);
//                        converter.addGetter(new Getter(Code2Html, Code2Html.get(t).getName(), FilePath));
//                        converter.convert(Code2Html.get(t).getName(), content);
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    } catch (Model.Generator.NotSupportedFiletypes e2) {
//                        e2.printStackTrace();
//                    }
                    webEngine.load("file:///" + anotherFile.getAbsolutePath());
//                    primaryStage.setTitle(Code2Html.get(t).getName() + ".html");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "找不到目标文件", ButtonType.OK);
                    alert.showAndWait();
                    for (int j = 0; j < Code2Html.size(); j++) {
                        if (codeName[j].equals(anotherFile.getName().substring(0, anotherFile.getName().indexOf(".html")))) {
                            Code2Html.remove(j);
                            buttonList.remove(j);
                            break;
                        }
                    }
                    leftPane.getChildren().remove(comboButton);
                    leftPane.getChildren().remove(separator);
                }
            });
            leftPane.getChildren().add(comboButton);
            leftPane.getChildren().add(separator);
        }

        sizeOfFont.setOnAction(e -> {
            for (int i = 0; i < buttonList.size(); i++) {
                try {
                    String content = new String(
                            Files.readAllBytes(Paths.get(Code2Html.get(i).getAbsolutePath()))
                    );
                    Configuration config = new Configuration();
                    if (chkShowLine.isSelected()) {
                        config.set_showLineNumber(true);
                    } else {
                        config.set_showLineNumber(false);
                    }
                    config.set_fontSize(sizeOfFont.getValue());
                    config.set_styleName(styleOfTheme.getValue());
                    Java2Html converter = new Java2Html();
                    converter.set_config(config);
                    converter.addGetter(new Getter(Code2Html, codeName[i], FilePath));
                    converter.convert(Code2Html.get(i).getName(), content);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (Model.Generator.NotSupportedFiletypes e2) {
                    e2.printStackTrace();
                }
            }
            webEngine.load(webEngine.getLocation());
        });
        styleOfTheme.setOnAction(e -> {
            for (int i = 0; i < buttonList.size(); i++) {
                try {
                    String content = new String(
                            Files.readAllBytes(Paths.get(Code2Html.get(i).getAbsolutePath()))
                    );
                    Configuration config = new Configuration();
                    if (chkShowLine.isSelected()) {
                        config.set_showLineNumber(true);
                    } else {
                        config.set_showLineNumber(false);
                    }
                    config.set_fontSize(sizeOfFont.getValue());
                    config.set_styleName(styleOfTheme.getValue());
                    Java2Html converter = new Java2Html();
                    converter.set_config(config);
                    converter.addGetter(new Getter(Code2Html, codeName[i], FilePath));
                    converter.convert(Code2Html.get(i).getName(), content);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (Model.Generator.NotSupportedFiletypes e2) {
                    e2.printStackTrace();
                }
            }
            webEngine.load(webEngine.getLocation());
        });

        chkShowLine.setOnAction(e -> {
            for (int i = 0; i < buttonList.size(); i++) {
                try {
                    String content = new String(
                            Files.readAllBytes(Paths.get(Code2Html.get(i).getAbsolutePath()))
                    );
                    Configuration config = new Configuration();
                    if (chkShowLine.isSelected()) {
                        config.set_showLineNumber(true);
                    } else {
                        config.set_showLineNumber(false);
                    }
                    config.set_fontSize(sizeOfFont.getValue());
                    config.set_styleName(styleOfTheme.getValue());
                    Java2Html converter = new Java2Html();
                    converter.set_config(config);
                    converter.addGetter(new Getter(Code2Html, codeName[i], FilePath));
                    converter.convert(Code2Html.get(i).getName(), content);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (Model.Generator.NotSupportedFiletypes e2) {
                    e2.printStackTrace();
                }
            }
            webEngine.load(webEngine.getLocation());
        });
        FileChooser fileChooser = new FileChooser();
        btAdd.setOnAction(e -> {
            Separator separator = new Separator();
            File addFile;
            fileChooser.setInitialDirectory(new File("file:///../test"));
            addFile = fileChooser.showOpenDialog(primaryStage);
//            File p = new File(addFile.getAbsolutePath());
//            fileChooser.setInitialDirectory(p);
            if (addFile == null) {
                return;
            }
            if (!addFile.getName().matches(".*\\.java") && !addFile.getName().matches(".*\\.c") && !addFile.getName().matches(".*\\.h")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "不是java文件或者C文件", ButtonType.OK);
                alert.showAndWait();
            }
            for (int i = 0; i < Code2Html.size(); i++) {
                if (addFile.getAbsolutePath().equals(Code2Html.get(i).getAbsolutePath())
                        || addFile.getName().equals(Code2Html.get(i).getName())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "该文件已存在或者文件名重复，若文件名重复，请在选择界面一次性选完", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
            }
            if (addFile.getName().matches(".*\\.java") || addFile.getName().matches(".*\\.c") || addFile.getName().matches(".*\\.h")) {
                final File FaddFile = addFile;
                Code2Html.add(addFile);
                codeName[Code2Html.size() - 1] = addFile.getName();
                try {
                    String content = new String(
                            Files.readAllBytes(Paths.get(addFile.getAbsolutePath()))
                    );
                    Configuration config = new Configuration();
                    if (chkShowLine.isSelected()) {
                        config.set_showLineNumber(true);
                    } else {
                        config.set_showLineNumber(false);
                    }
                    Java2Html converter = new Java2Html();
                    config.set_showLineNumber(chkShowLine.isSelected());
                    config.set_fontSize(sizeOfFont.getValue());
                    config.set_styleName(styleOfTheme.getValue() + ".css");
                    converter.set_config(config);
                    converter.addGetter(new Getter(Code2Html, addFile.getName(), FilePath));
                    converter.convert(addFile.getName(), content);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (Model.Generator.NotSupportedFiletypes e2) {
                    e2.printStackTrace();
                }
                Button button = new Button(addFile.getName() + ".html");
                Button btDelete = new Button("", new ImageView(new Image("file:///../image/NO2.png")));
                File anotherFile = new File(FilePath + "/" + addFile.getName() + ".html");
                for (int j = 0; j < buttonList.size(); j++) {
                    buttonList.get(j).setStyle("-fx-background-color:null");
                }
                btDelete.setStyle("-fx-background-color:null");
                button.setStyle("-fx-background-color:lightblue");
                btDelete.setOnMouseEntered(E -> {
                    btDelete.setStyle("-fx-background-color:lightblue");
                });
                btDelete.setOnMouseExited(E -> {
                    btDelete.setStyle("-fx-background-color:null");
                });
                ToolBar comboButton = new ToolBar(
                        button,
                        btDelete
                );
                btDelete.setOnAction(E -> {
//                    final int p = Code2Html.size() - 1;
                    leftPane.getChildren().remove(comboButton);
                    leftPane.getChildren().remove(separator);
                    webEngine.load(null);
                    for (int j = 0; j < Code2Html.size(); j++) {
                        if (Code2Html.get(j).getName().equals(anotherFile.getName().substring(0, anotherFile.getName().indexOf(".html")))) {
                            Code2Html.remove(j);
                            buttonList.remove(j);
                            break;
                        }
                    }
                });
                buttonList.add(button);
//                System.out.println(buttonList.get(buttonList.size() - 1));
//                final int t = Code2Html.size() - 1;
                button.setOnAction(E -> {
                    for (int j = 0; j < buttonList.size(); j++) {
                        buttonList.get(j).setStyle("-fx-background-color:null");
                    }
                    button.setStyle("-fx-background-color:lightblue");
                    if (anotherFile.exists()) {
//                        try {
//                            String content = new String(
//                                    Files.readAllBytes(Paths.get(Code2Html.get(t).getAbsolutePath()))
//                            );
//                            Configuration config = new Configuration();
//                            config.set_fontSize(sizeOfFont.getValue());
//                            config.set_styleName(styleOfTheme.getValue() + ".css");
//                            Java2Html converter = new Java2Html();
//                            converter.set_config(config);
//                            converter.addGetter(new Getter(Code2Html, Code2Html.get(t).getName(), FilePath));
//                            converter.convert(Code2Html.get(t).getName(), content);
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        } catch (Model.Generator.NotSupportedFiletypes e2) {
//                            e2.printStackTrace();
//                        }
                        webEngine.load("file:///" + anotherFile.getAbsolutePath());
//                        primaryStage.setTitle(FaddFile.getName() + ".html");
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "找不到目标文件", ButtonType.OK);
                        alert.showAndWait();
                        for (int i = 0; i < Code2Html.size(); i++) {
                            if (Code2Html.get(i).getName().equals(anotherFile.getName().substring(0, anotherFile.getName().indexOf(".html")))) {
                                Code2Html.remove(i);
                                buttonList.remove(i);
                                break;
                            }
                        }
                        leftPane.getChildren().remove(comboButton);
                        leftPane.getChildren().remove(separator);
                    }
                });
                comboButton.setStyle("-fx-background-color:null");
                leftPane.getChildren().add(comboButton);
                leftPane.getChildren().add(separator);
                webEngine.load("file:///" + anotherFile.getAbsolutePath());
                primaryStage.setTitle(FaddFile.getName() + ".html");
            }
        });

        openWithBrowse.setOnAction(e -> {
            try {
                String path = webEngine.getLocation();
                path = path.replaceAll("\\\\", "/");
                Desktop.getDesktop().browse(new URI(path));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        ascending.setOnAction(e -> {
//            File[] files = (File[])Code2Html.toArray();
            Actions.sortArrayList(Code2Html, buttonList, codeName, leftPane, true);

//            for (int i = 0; i < Code2Html.size(); i++) {
//                System.out.println(Code2Html.get(i).getName());
//                System.out.println(buttonList.get(i).getText());
//                System.out.println(codeName[i]);
//            }
//            System.out.println("finished!");
        });
        descending.setOnAction(e -> {
            Actions.sortArrayList(Code2Html, buttonList, codeName, leftPane, false);
//            for (int i = 0; i < Code2Html.size(); i++) {
//                System.out.println(Code2Html.get(i).getName());
//                System.out.println(buttonList.get(i).getText());
//                System.out.println(codeName[i]);
//            }
//            System.out.println("finished!");
        });

        borderPane.setCenter(browser);
        borderPane.setLeft(leftPane);
        borderPane.setTop(topPane);

        Scene scene = new Scene(borderPane, 1280, 960);
        primaryStage.setScene(scene);
        scene.setFill(null);

        primaryStage.setTitle("内置浏览器");
        primaryStage.show();
    }
}
