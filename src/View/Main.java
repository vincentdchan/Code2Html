package View;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private TextField currentPathTextField;
    private File srcPathFile;
    private BorderPane borderPane;
    private ComboBox<String> showFileKindComboBox;
    private TableView<FileItem> middleTable;
    private TableView<FileItem> rightTable;

    private ObservableList<FileItem> dataMiddle;
    private ObservableList<FileItem> dataRight;
    private File searchingPath;

    @Override
    public void start(Stage primaryStage) throws Exception {
        srcPathFile = new File("file");
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
        middleTable.setEditable(true);
        TableColumn<FileItem, Boolean> tableCheck = new TableColumn("Check");
        tableCheck.setCellValueFactory(new PropertyValueFactory<>("checked"));
        TableColumn<FileItem, String> tableName = new TableColumn("FileName");
        tableName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        TableColumn<FileItem, String> tableDate = new TableColumn("LastModified");
        tableDate.setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        TableColumn<FileItem, String> tableLength = new TableColumn("Length");
        tableLength.setCellValueFactory(new PropertyValueFactory<>("length"));
        TableColumn<FileItem, String> tableType = new TableColumn("Type");
        tableType.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<FileItem, String> tablePath = new TableColumn("Path");
        tablePath.setCellValueFactory(new PropertyValueFactory<>("path"));
        tableCheck.prefWidthProperty().bind(borderPane.widthProperty().divide(8.5));
        tableName.prefWidthProperty().bind(borderPane.widthProperty().divide(8.5));
        tableDate.prefWidthProperty().bind(borderPane.widthProperty().divide(8.3));
        tableLength.prefWidthProperty().bind(borderPane.widthProperty().divide(8.5));
        tableType.prefWidthProperty().bind(borderPane.widthProperty().divide(8.5));

        tableCheck.setCellFactory(p -> {
            CheckBox checkBox = new CheckBox();
            TableCell<FileItem, Boolean> tableCell = new TableCell<FileItem, Boolean>() {

                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null)
                        setGraphic(null);
                    else {
                        setGraphic(checkBox);
                        checkBox.setSelected(item);
                    }
                }

            };
            checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, (event) -> {
                handleFileItemChecked(checkBox, (FileItem)tableCell.getTableRow().getItem(), event);
            });

            checkBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                handleFileItemChecked(checkBox, (FileItem)tableCell.getTableRow().getItem(), event);
            });

            tableCell.setAlignment(Pos.CENTER);
            tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return tableCell;
        });
        tableCheck.setEditable(true);
        tableCheck.setMaxWidth(50);

//        tableName.setPrefWidth(195);
//        tableDate.setPrefWidth(195);
//        tableLength.setPrefWidth(150);
//        tableType.setPrefWidth(150);
//        tableBottom.setPrefWidth(91);


        middleTable.getColumns().addAll(tableCheck,
                tableName,
                tableDate,
                tableLength,
                tablePath,
                tableType);

        StackPane rightPane = new StackPane();
        rightTable = new TableView<>();
        TableColumn chooseFile = new TableColumn("File Name");
        chooseFile.setPrefWidth(150);
        chooseFile.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        TableColumn isCancel = new TableColumn("Path");
        isCancel.setCellValueFactory(new PropertyValueFactory<>("path"));
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

        SplitPane middlePane = new SplitPane(middleTable, rightTable);
        middlePane.setDividerPosition(0, 0.7);
        // borderPane.setLeft(tree);
        borderPane.setTop(topPane);
        borderPane.setCenter(middlePane);
        // borderPane.setCenter(middleTable);
        // borderPane.setRight(rightTable);
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
     * Check if the list contains value
     * clear it if it contains value
     * if it's null, then create one.
     * @param checkBox
     * @param fileItem
     * @param event
     */
    private void handleFileItemChecked(CheckBox checkBox, FileItem fileItem, Event event) {
        fileItem.setChecked(!checkBox.isSelected());
        if (dataRight == null) {
            dataRight = FXCollections.observableArrayList();
            rightTable.setItems(dataRight);
        } else {
            dataRight.clear();
        }
        for (FileItem fileItem1 : dataMiddle) {
            if (fileItem1.isChecked()) {
                dataRight.add(fileItem1);
            }
        }
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
        ImageView openImgView = new ImageView(new Image("file:///../resources/icons/si-glyph-folder-open.png"));
        Button openBtn = new Button();
        openImgView.setFitHeight(36);
        openImgView.setFitWidth(36);
        openBtn.setGraphic(openImgView);

        ImageView settingImgView = new ImageView(new Image("file:///../resources/icons/si-glyph-gear.png"));
        settingImgView.setFitWidth(36);
        settingImgView.setFitHeight(36);
        Button settingBtn = new Button();
        settingBtn.setGraphic(settingImgView);
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
        VBox result = new VBox();
        result.setAlignment(Pos.CENTER);
        result.setMaxHeight(Double.MAX_VALUE);
        result.setMaxWidth(Double.MAX_VALUE);
        result.setPadding(new Insets(5, 16,5, 16));

        GridPane bottomPane = new GridPane();
        bottomPane.setAlignment(Pos.CENTER);
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
                btStartCode.setOnAction(new ConvertActionHandler(this, showFilePath.getText()));
            }
        });

        Label fileKind = new Label("Kind :");
        btStartCode.setOnAction(new ConvertActionHandler(this, showFilePath.getText()));
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

        result.getChildren().add(bottomPane);
        return result;
    }

    public ObservableList<FileItem> getDataMiddle() {
        return dataMiddle;
    }

    public ObservableList<FileItem> getDataRight() {
        return dataRight;
    }

}
