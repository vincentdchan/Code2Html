package View;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.ArrayList;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private TextField currentPathTextField;
    private File srcPathFile;
    private BorderPane borderPane;
    private ArrayList<File> Code2HtmlFile;
    private ComboBox<String> showFileKindComboBox;
    private TableView<FileItem> middleTable;

    private ObservableList<FileItem> dataMiddle;
    private File searchingPath;

    @Override
    public void start(Stage primaryStage) throws Exception {
        srcPathFile = new File("file");
        Code2HtmlFile = new ArrayList<>();
        // ObservableList<RightTable> dataRight = FXCollections.observableArrayList();
//        Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
        primaryStage.setTitle("源代码自动转换程序");
        StackPane backpane = new StackPane();
//        backpane.setStyle("-fx-background-color:cyan");

        borderPane = new BorderPane();
        Pane topPane = generateTopPane(primaryStage);
        Pane bottomPane = generateBottomPane(primaryStage);

        // topPane.setLeft(labelCurrentPath);
        // topPane.setTop(menuBar);
        // topPane.setCenter(buttonsPaneBar);
        // topPane.setBottom(currentPath);
        // topPane.setRight(_null);

        middleTable = new TableView<>();
        TableColumn tableName = new TableColumn("FileName");
        tableName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        TableColumn tableDate = new TableColumn("LastModified");
        tableDate.setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        TableColumn tableLength = new TableColumn("Length");
        tableLength.setCellValueFactory(new PropertyValueFactory<>("length"));
        TableColumn tableType = new TableColumn("Type");
        tableType.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn tablePath = new TableColumn("Path");
        tablePath.setCellValueFactory(new PropertyValueFactory<>("path"));
        TableColumn tableBottom = new TableColumn("isChoose");
        tableBottom.setCellValueFactory(new PropertyValueFactory<>("btChoose"));
        tableName.prefWidthProperty().bind(borderPane.widthProperty().divide(8.5));
        tableDate.prefWidthProperty().bind(borderPane.widthProperty().divide(8.3));
        tableLength.prefWidthProperty().bind(borderPane.widthProperty().divide(8.5));
        tableType.prefWidthProperty().bind(borderPane.widthProperty().divide(8.5));
        tableBottom.prefWidthProperty().bind(borderPane.widthProperty().divide(8));

//        tableName.setPrefWidth(195);
//        tableDate.setPrefWidth(195);
//        tableLength.setPrefWidth(150);
//        tableType.setPrefWidth(150);
//        tableBottom.setPrefWidth(91);


        middleTable.getColumns().addAll(tableName,
                tableDate,
                tableLength,
                tablePath,
                tableType,
                tableBottom);

        StackPane rightPane = new StackPane();
        TableView<MyFile> rightTable = new TableView<>();
        TableColumn chooseFile = new TableColumn("ChoosedFile");
        chooseFile.setPrefWidth(195);
        chooseFile.setCellValueFactory(new PropertyValueFactory<>("choosedFile"));
        TableColumn isCancel = new TableColumn("isCancel");
        isCancel.setCellValueFactory(new PropertyValueFactory<>("btCancel"));
        rightTable.getColumns().addAll(chooseFile, isCancel);
        rightPane.getChildren().add(rightTable);

        /*
        Button computer = new Button(new File(System.getenv("COMPUTERNAME")).getName());
        computer.setStyle("-fx-background-color:null");
        computer.setOnMouseEntered(e -> {
            computer.setStyle("-fx-background-color:lightblue");
        });
        computer.setOnMouseExited(e -> {
            computer.setStyle("-fx-background-color:null");
        });
        // TreeItem<Button> rootItem = new TreeItem<>(computer, new ImageView(new Image("file:///../image/MyComputer.png")));
        computer.setOnAction(e -> {
            rootItem.setExpanded(true);
        });

        for (File file : File.listRoots()) {
            Button button = new Button(file.getPath() + "                              ", new ImageView(new Image("file:///../image/Disk.png")));
            button.setStyle("-fx-background-color:null");
            button.setOnMouseEntered(e -> {
                button.setStyle("-fx-background-color:lightblue");
            });
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-background-color:null");
            });
            TreeItem<Button> root = new TreeItem<>(button);
            rootItem.getChildren().add(root);
            Actions.clickAction(button, root, file, table, Code2HtmlFile, showFileKind, currentPath, rightTable, dataRight, dataMiddle);
        }
        rootItem.setExpanded(true);
        */

        // TreeView<Button> tree = new TreeView<>(rootItem);

//        WebView browser = new WebView();
//        WebEngine webEngine = browser.getEngine();
//        webEngine.load("http://www.baidu.com");

        // borderPane.setLeft(tree);
        borderPane.setTop(topPane);
        borderPane.setCenter(middleTable);
        borderPane.setRight(rightTable);
        borderPane.setBottom(bottomPane);

        backpane.getChildren().add(borderPane);

        Scene scene = new Scene(backpane, 800, 600);
        primaryStage.setScene(scene);
        scene.setFill(null);

        //窗口图标
        primaryStage.getIcons().add(new Image("file:///../image/2.jpg"));

        primaryStage.show();
    }

    /**
     * Top pane layout:
     *
     * Menubar
     * -------------------------
     * | Open button | Setting |
     * -------------------------
     * CurrentPath:_____________
     * -------------------------
     *
     * @return The result
     */
    private Pane generateTopPane(Stage primaryStage) {
        VBox result = new VBox();

        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem menuItemOpen = new MenuItem("Open");
        MenuItem menuItemAbout = new MenuItem("About");
        MenuItem menuItemExit = new MenuItem("Exit");

        menuItemOpen.setOnAction(e -> {
            handleOpenDirectory(primaryStage);
        });

        //关闭程序
        menuItemExit.setOnAction((ActionEvent t) -> {
            primaryStage.close();
        });

        menuFile.getItems().addAll(menuItemOpen, menuItemAbout, menuItemExit);

        Menu menuHelp = new Menu("Help");
        menuBar.getMenus().addAll(menuFile, menuHelp);

        HBox buttonBars = new HBox();
        Button openBtn = new Button("Open Folder");
        Button settingBtn = new Button("Setting");
        buttonBars.getChildren().addAll(openBtn, settingBtn);

        // Binding events
        openBtn.setOnAction(e -> {
            handleOpenDirectory(primaryStage);
        });

        HBox pathDisplayer = new HBox();
        Label labelCurrentPath = new Label("CurrentPath:");
        currentPathTextField = new TextField();
        currentPathTextField.setDisable(true);
        pathDisplayer.getChildren().addAll(labelCurrentPath, currentPathTextField);

        result.getChildren().addAll(menuBar, buttonBars, pathDisplayer);
        return result;
    }

    private void handleOpenDirectory(Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirecotry = directoryChooser.showDialog(primaryStage);
        if (selectedDirecotry != null) {
            searchingPath = selectedDirecotry;
            currentPathTextField.setText(selectedDirecotry.getAbsolutePath());

            // clear the data and search again
            dataMiddle = FXCollections.observableArrayList();
            searchDirectory(selectedDirecotry);
            middleTable.setItems(dataMiddle);
        }
    }

    private void searchDirectory(File directory) {
        for (File child: directory.listFiles()) {
            if (child.isDirectory()) {
                searchDirectory(child);
            } else {
                String ext = showFileKindComboBox.getValue();
                if (ext.equals("all")) {
                    String filename = child.getName();
                    if (filename.endsWith(".h") || filename.endsWith(".c") || filename.endsWith(".java")) {
                        dataMiddle.add(new FileItem(child, searchingPath));
                    }
                } else if (child.getName().endsWith(ext)) {
                    dataMiddle.add(new FileItem(child, searchingPath));
                }
            }
        }
    }

    private Pane generateBottomPane(Stage primaryStage) {
        GridPane bottomPane = new GridPane();
//        bottomPane.setGridLinesVisible(true);
        // bottomPane.setPadding(new Insets(5, 0, 5, 250));
        // bottomPane.setHgap(25);
        // bottomPane.setVgap(10);
        Label filePath = new Label("Path :");
        TextField showFilePath = new TextField();
        showFilePath.setText(srcPathFile.getAbsolutePath());
        showFilePath.setAlignment(Pos.BASELINE_LEFT);
        showFilePath.prefWidthProperty().bind(borderPane.widthProperty().divide(1.85));
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Button btChoosePath = new Button("Open");
        Button btStartCode = new Button("Start to Code");
        btChoosePath.setOnAction(e -> {
            File file = directoryChooser.showDialog(primaryStage);
            if (file != null) {
                showFilePath.setText(file.getAbsolutePath());
                Actions.codeAction(btStartCode, Code2HtmlFile, showFilePath.getText());
            }
        });

        Label fileKind = new Label("Kind :");
        Actions.codeAction(btStartCode, Code2HtmlFile, showFilePath.getText());
//        btStartCode.setDefaultButton(true);
        showFileKindComboBox = new ComboBox<>();
        showFileKindComboBox.prefWidthProperty().bind(borderPane.widthProperty().divide(1.85));
        showFileKindComboBox.getItems().addAll("all", ".h", ".c", ".java");
        showFileKindComboBox.setValue("all");
        bottomPane.add(filePath, 0, 0);
        bottomPane.add(showFilePath, 1, 0);
        bottomPane.add(btChoosePath, 2, 0);
        bottomPane.add(fileKind, 0, 1);
        bottomPane.add(showFileKindComboBox, 1, 1);
        bottomPane.add(btStartCode, 2, 1);

        return bottomPane;
    }

}
