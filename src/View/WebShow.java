package View;
/**
 * Created by ZMYang on 2017/4/5.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.net.URI;
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

        Button openWithBrowse = new Button("OpenWithBrowse");

        openWithBrowse.setOnAction(e -> {
            try {
                String path = webEngine.getLocation();
                path = path.replaceAll("\\\\","/");
                Desktop.getDesktop().browse(new URI(path));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(openWithBrowse,browser);

        borderPane.setCenter(vbox);
        borderPane.setLeft(leftPane);

        Scene scene = new Scene(borderPane, 1280, 960);
        primaryStage.setScene(scene);
        scene.setFill(null);

        primaryStage.show();
    }
}
