package View;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
        primaryStage.setTitle("源代码自动转换程序");
        StackPane backpane = new StackPane();
        backpane.setStyle("-fx-background-color:cyan");

//        FileChooser

        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem menuItemAbout = new MenuItem("About");
        MenuItem menuItemExit = new MenuItem("Exit");

        //关闭程序
        menuItemExit.setOnAction((ActionEvent t) -> {
            primaryStage.close();
        });

        menuFile.getItems().addAll(menuItemAbout, menuItemExit);
        Menu menuHelp = new Menu("Help");

        menuBar.getMenus().addAll(menuFile, menuHelp);
//        ImageView background = new ImageView("Image/1.jpg");
//        background.setFitHeight(500);
//        background.setFitWidth(500);
//        background.setSmooth(true);
//        backpane.getChildren().add(background);

        BorderPane borderPane = new BorderPane();

        TableView<MyFile> table = new TableView<>();
        TableColumn tableName = new TableColumn("FileName");
        tableName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        TableColumn tableDate = new TableColumn("LastModified");
        tableDate.setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        TableColumn tableLength = new TableColumn("Length");
        tableLength.setCellValueFactory(new PropertyValueFactory<>("length"));
        TableColumn tableType = new TableColumn("Type");
        tableType.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn tableBottom = new TableColumn("isChoose");
        tableBottom.setCellValueFactory(new PropertyValueFactory<>("btChoose"));
        tableName.setPrefWidth(195);
        tableDate.setPrefWidth(195);
        tableLength.setPrefWidth(150);
        tableType.setPrefWidth(150);
        tableBottom.setPrefWidth(87);



        table.getColumns().addAll(tableName , tableDate, tableLength,tableType,tableBottom);

        Label labelIsChosen = new Label("已选文件");
        labelIsChosen.setFont(Font.font("Times New Roman", FontWeight.BOLD,27) );
        ListView list = new ListView();
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        BorderPane rightBorderPane = new BorderPane();
        rightBorderPane.setTop(labelIsChosen);
        rightBorderPane.setLeft(list);



        HBox hBox = new HBox();
        Button btOK = new Button("       确认       ");
        btOK.setStyle("-fx-padding:8");
        hBox.getChildren().add(btOK);

        Button computer = new Button(new File(System.getenv("COMPUTERNAME")).getName());
        computer.setStyle("-fx-background-color:null");
        computer.setOnMouseEntered(e->{
            computer.setStyle("-fx-background-color:lightblue");
        });
        computer.setOnMouseExited(e->{
            computer.setStyle("-fx-background-color:null");
        });
        TreeItem<Button> rootItem = new TreeItem<>(computer, new ImageView(new Image("file:///../image/MyComputer.png")));
        computer.setOnAction(e->{
            rootItem.setExpanded(true);
        });

        for (File file : File.listRoots()) {
            Button button = new Button(file.getPath()+"                              ", new ImageView(new Image("file:///../image/Disk.png")));
            button.setStyle("-fx-background-color:null");
            button.setOnMouseEntered(e->{
                button.setStyle("-fx-background-color:lightblue");
            });
            button.setOnMouseExited(e->{
                button.setStyle("-fx-background-color:null");
            });
            TreeItem<Button> root = new TreeItem<>(button);
            rootItem.getChildren().add(root);
            tools.clickAction(button , root ,file , table);
        }

        TreeView<Button> tree = new TreeView<>(rootItem);

        borderPane.setLeft(tree);
        borderPane.setTop(menuBar);
        borderPane.setCenter(table);
        borderPane.setRight(rightBorderPane);
        borderPane.setBottom(hBox);

        backpane.getChildren().add(borderPane);

        Scene scene = new Scene(backpane, 1280, 960);
        primaryStage.setScene(scene);
        scene.setFill(null);

        //窗口图标
        primaryStage.getIcons().add(new Image("file:///../image/2.jpg"));

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
