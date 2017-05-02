package View;

import Model.Configuration;
import Model.Java2Html;
import Model.TreeFileItem;
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
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static int TitleBarIconSize = 24;

    private TextField currentPathTextField;
    private File srcPathFile;
    private BorderPane borderPane;
    private ComboBox<String> showFileKindComboBox;
    // private TableView<FileItem> middleTable;
    private FileTreeControl middleTreeView;
    private TableView<FileItem> rightTable;
    private WebView previewWebView;

    private ObservableList<FileItem> dataMiddle;
    private ObservableList<FileItem> dataRight;
    private File searchingPath;

    private TreeFileItem previewFileItem;

    private Configuration config;

    @Override
    public void start(Stage primaryStage) throws Exception {
        config = new Configuration();

        srcPathFile = new File("file");
        // ObservableList<RightTable> dataRight = FXCollections.observableArrayList();
//        Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
        primaryStage.setTitle("源代码自动转换程序");
        StackPane backpane = new StackPane();
//        backpane.setStyle("-fx-background-color:cyan");

        borderPane = new BorderPane();
        Pane topPane = generateTopPane(primaryStage);
        Pane bottomPane = generateBottomPane(primaryStage);

        middleTreeView = new FileTreeControl();
        middleTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            FileTreeControl.MyTreeItem item = (FileTreeControl.MyTreeItem)newVal;
            if (!item.getValue().isDirectory()) {
                previewFileItem = item.getValue();
                refreshPreview();
            }
        });

        StackPane rightPane = new StackPane();
        rightTable = new TableView<>();
        TableColumn chooseFile = new TableColumn("File Name");
        chooseFile.setPrefWidth(150);
        chooseFile.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        TableColumn isCancel = new TableColumn("Path");
        isCancel.setCellValueFactory(new PropertyValueFactory<>("path"));
        rightTable.getColumns().addAll(chooseFile, isCancel);
        rightPane.getChildren().add(rightTable);

        VBox rightLayout = new VBox();
        previewWebView = new WebView();
        rightLayout.getChildren().addAll(generatePreviewToolbar(), previewWebView);

        SplitPane middlePane = new SplitPane(middleTreeView, rightLayout);
        middlePane.setDividerPosition(0, 0.3);
        borderPane.setTop(topPane);
        borderPane.setCenter(middlePane);
        borderPane.setBottom(bottomPane);

        backpane.getChildren().add(borderPane);

        Scene scene = new Scene(backpane, 800, 600);
        primaryStage.setScene(scene);
        scene.setFill(null);

        //窗口图标
        primaryStage.getIcons().add(new Image("file:///../image/2.jpg"));

        primaryStage.show();
    }

    private void refreshPreview() {
        if (previewFileItem == null) return;
        Java2Html j2h = new Java2Html();
        j2h.set_config(config);
        j2h.addGetter((String reseult) -> {
            previewWebView.getEngine().loadContent(reseult);
        });
        File file = previewFileItem.getBaseFile();
        try {
            j2h.convert(file.getName(), new String(
                    Files.readAllBytes(
                            Paths.get(file.getAbsolutePath()))));
        } catch (IOException ioexcp) {
            ioexcp.printStackTrace();
        } catch (Exception fuck) {
            fuck.printStackTrace();
        }
    }

    private void preview(TreeFileItem treeFileItem) {
        previewFileItem = treeFileItem;
        refreshPreview();
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

    private Pane generatePreviewToolbar() {
        HBox result = new HBox();

        Label themeSelectorLabel = new Label("Theme:");
        String[] stylesList = Java2Html.getStylesNameList();
        ComboBox<String> themeSelector = new ComboBox<String>(
                FXCollections.observableArrayList(stylesList)
        );
        themeSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            config.set_styleName(newVal);
            refreshPreview();
        });
        themeSelector.setValue(config.get_styleName());

        Label showLineNumberLabel = new Label("Show Line Number:");
        CheckBox showLineNumberCheckBox = new CheckBox();
        showLineNumberCheckBox.setSelected(true);
        showLineNumberCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            config.set_showLineNumber(newVal);
            refreshPreview();
        });

        Label fontSizeLabel = new Label("Font size:");
        TextField fontSizeTextField = new TextField(Integer.toString(config.get_fontSize()));
        fontSizeTextField.setPrefWidth(36);
        fontSizeTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() == 0) return;
            int value = Integer.parseInt(newVal);
            if (value >= 8 && value <= 48) {
                System.out.println(value);
                config.set_fontSize(value);
                refreshPreview();
            }
        });

        result.getChildren().addAll(themeSelectorLabel, themeSelector,
                showLineNumberLabel, showLineNumberCheckBox,
                fontSizeLabel, fontSizeTextField);

        return result;
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
        openImgView.setFitHeight(TitleBarIconSize);
        openImgView.setFitWidth(TitleBarIconSize);
        openBtn.setTooltip(new Tooltip("Open directory"));
        openBtn.setGraphic(openImgView);

        ImageView convertImgView = new ImageView(new Image("file:///../resources/icons/si-glyph-triangle-right.png"));
        convertImgView.setFitHeight(TitleBarIconSize);
        convertImgView.setFitWidth(TitleBarIconSize);
        Button convertBtn = new Button();
        convertBtn.setTooltip(new Tooltip("Begin Convert"));
        convertBtn.setGraphic(convertImgView);

        ImageView previewImgView = new ImageView(new Image("file:///../resources/icons/si-glyph-view.png"));
        previewImgView.setFitWidth(TitleBarIconSize);
        previewImgView.setFitHeight(TitleBarIconSize);
        Button previewBtn = new Button();
        previewBtn.setTooltip(new Tooltip("Preview"));
        previewBtn.setGraphic(previewImgView);

        ImageView settingImgView = new ImageView(new Image("file:///../resources/icons/si-glyph-gear.png"));
        settingImgView.setFitWidth(TitleBarIconSize);
        settingImgView.setFitHeight(TitleBarIconSize);
        Button settingBtn = new Button();
        settingBtn.setTooltip(new Tooltip("Setting"));
        settingBtn.setGraphic(settingImgView);

        buttonBars.getChildren().addAll(openBtn, convertBtn, previewBtn, settingBtn);

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
            // dataMiddle = FXCollections.observableArrayList();
            // searchDirectory(selectedDirecotry);
            // middleTable.setItems(dataMiddle);
            middleTreeView.setRootFileItem(new TreeFileItem(selectedDirecotry, new String[]{".c", ".java", ".h"}));
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
