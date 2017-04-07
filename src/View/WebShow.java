package View;
/**
 * Created by ZMYang on 2017/4/5.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class WebShow extends Application {

    public static ArrayList<File> Code2Html ;

    public static void happen(ArrayList<File> arrayList){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                new WebShow().start(new Stage());
            }
        });
        Code2Html = arrayList;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane borderPane = new BorderPane();

        File file = new File("file:///D:/Code2Html/test/1.java.html");

        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load("file:///D:/Code2Html/test/1.java.html");

        borderPane.setCenter(browser);

        Scene scene = new Scene(borderPane, 1280, 960);
        primaryStage.setScene(scene);
        scene.setFill(null);

        primaryStage.show();
    }
}
