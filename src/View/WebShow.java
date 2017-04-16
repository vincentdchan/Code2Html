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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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

    public static void happen(ArrayList<File> arrayList, String filePath) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                new WebShow().start(new Stage());
            }
        });
        Code2Html = arrayList;
        FilePath = filePath;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane borderPane = new BorderPane();

        File file = new File(FilePath + "/" + Code2Html.get(0).getName() + ".html");
//        System.out.println(file.getAbsolutePath());
        VBox topPane = new VBox();

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");
        MenuItem openWithBrowse = new MenuItem("OpenWithBrowse");

        menu.getItems().addAll(openWithBrowse);
        menuBar.getMenus().addAll(menu);

        Button btAdd = new Button("Add");

        ToolBar toolBar = new ToolBar(
                btAdd,
                new Separator(),
                new Button("Very Small"),
                new Button("Small"),
                new Button("Middle"),
                new Button("Big"),
                new Button("Very Big")
        );


        topPane.getChildren().addAll(menuBar, toolBar);

        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load("file:///" + file.getAbsolutePath());
        primaryStage.setTitle(Code2Html.get(0).getName() + ".html");
//        System.out.println("file:///" + file.getAbsolutePath());


        VBox leftPane = new VBox();
//        leftPane.setPadding(new Insets(5, 2, 5, 2));
        leftPane.setSpacing(5);
        for (int i = 0; i < Code2Html.size(); i++) {
            int t = i;
            Button button = new Button(Code2Html.get(i).getName() + ".html");
            File anotherFile = new File(FilePath + "/" + Code2Html.get(i).getName() + ".html");
            button.setStyle("-fx-background-color:null");
            button.setOnMouseEntered(e -> {
                button.setStyle("-fx-background-color:lightblue");
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color:null");
            });
            button.setOnAction(e -> {
                webEngine.load("file:///" + anotherFile.getAbsolutePath());
                primaryStage.setTitle(Code2Html.get(t).getName() + ".html");
            });
            leftPane.getChildren().add(button);
            Separator separator = new Separator();

            leftPane.getChildren().add(separator);
        }

        btAdd.setOnAction(e -> {
            File addFile;
            FileChooser fileChooser = new FileChooser();
            addFile = fileChooser.showOpenDialog(primaryStage);
            if(addFile == null){
                return;
            }
            if (!addFile.getName().matches(".*\\.java") && !addFile.getName().matches(".*\\.c") && !addFile.getName().matches(".*\\.h")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "不是java文件或者C文件", ButtonType.OK);
                alert.showAndWait();
            }
            if (addFile.getName().matches(".*\\.java") || addFile.getName().matches(".*\\.c") || addFile.getName().matches(".*\\.h")) {
                final File FaddFile = addFile;
                Code2Html.add(addFile);
                try {
                    String content = new String(
                            Files.readAllBytes(Paths.get(addFile.getAbsolutePath()))
                    );
                    Configuration config = new Configuration();
                    Java2Html converter = new Java2Html();
                    converter.set_config(config);
                    converter.addGetter(new Getter(Code2Html, addFile.getName(), FilePath));
                    converter.convert(addFile.getName(), content);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Button button = new Button(addFile.getName() + ".html");
                File anotherFile = new File(FilePath + "/" + addFile.getName() + ".html");
                button.setStyle("-fx-background-color:null");
                button.setOnMouseEntered(E -> {
                    button.setStyle("-fx-background-color:lightblue");
                });
                button.setOnMouseExited(E -> {
                    button.setStyle("-fx-background-color:null");
                });
                button.setOnAction(E -> {
                    webEngine.load("file:///" + anotherFile.getAbsolutePath());
                    primaryStage.setTitle(FaddFile.getName() + ".html");
                });
                leftPane.getChildren().add(button);
                Separator separator = new Separator();

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


        borderPane.setCenter(browser);
        borderPane.setLeft(leftPane);
        borderPane.setTop(topPane);

        Scene scene = new Scene(borderPane, 1280, 960);
        primaryStage.setScene(scene);
        scene.setFill(null);

        primaryStage.show();
    }
}
