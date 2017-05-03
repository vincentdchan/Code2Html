package View;

import Model.Configuration;
import Model.Java2Html;
import Model.TreeFileItem;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.tools.Tool;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static int TitleBarIconSize = 24;
    public static int ToolbarIconSize = 12;
    public static String[] FileFilters = {".c", ".java", ".h"};

    private TextField currentPathTextField;
    private TextField targetPathTextField;
    private File srcPathFile;
    private BorderPane borderPane;
    private ComboBox<String> showFileKindComboBox;
    // private TableView<FileItem> middleTable;
    private FileTreeControl treeView;
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
        // Pane bottomPane = generateBottomPane(primaryStage);

        StackPane rightPane = new StackPane();
        rightTable = new TableView<>();
        TableColumn chooseFile = new TableColumn("File Name");
        chooseFile.setPrefWidth(150);
        chooseFile.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        TableColumn isCancel = new TableColumn("Path");
        isCancel.setCellValueFactory(new PropertyValueFactory<>("path"));
        rightTable.getColumns().addAll(chooseFile, isCancel);
        rightPane.getChildren().add(rightTable);

        BorderPane rightLayout = new BorderPane();
        previewWebView = new WebView();
        rightLayout.setTop(generatePreviewToolbar());
        rightLayout.setCenter(previewWebView);

        SplitPane middlePane = new SplitPane(generateLeftPane(primaryStage),
                rightLayout);
        middlePane.setDividerPosition(0, 0.3);
        borderPane.setTop(topPane);
        borderPane.setCenter(middlePane);
        // borderPane.setBottom(bottomPane);

        backpane.getChildren().add(borderPane);

        Scene scene = new Scene(backpane, 800, 600);
        primaryStage.setScene(scene);
        scene.setFill(null);
        scene.getStylesheets().add("/resources/ui.css");

        //窗口图标
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/image/2.jpg")));

        primaryStage.show();
    }

    private Pane generateLeftPane(Stage primaryStage) {
        BorderPane result = new BorderPane();

        HBox toolbar = new HBox();

        ImageView expandIM = new ImageView(new Image(
                getClass().getResourceAsStream("/resources/icons/si-glyph-arrow-resize-2.png")));
        expandIM.setFitWidth(ToolbarIconSize);
        expandIM.setFitHeight(ToolbarIconSize);
        Button expandAllBtn = new Button();
        expandAllBtn.setOnAction(event -> treeView.expandAll());
        expandAllBtn.setGraphic(expandIM);
        expandAllBtn.setTooltip(new Tooltip("Expand All"));

        ImageView collapseIM = new ImageView(new Image(
                getClass().getResourceAsStream("/resources/icons/si-glyph-arrow-resize-4.png")));
        collapseIM.setFitHeight(ToolbarIconSize);
        collapseIM.setFitWidth(ToolbarIconSize);
        Button collapseAllBtn = new Button();
        collapseAllBtn.setOnAction(event -> treeView.collapseAll());
        collapseAllBtn.setGraphic(collapseIM);
        collapseAllBtn.setTooltip(new Tooltip("Collapse All"));

        ImageView refreshIM = new ImageView(new Image(
                getClass().getResourceAsStream("/resources/icons/si-glyph-arrow-reload.png")));
        refreshIM.setFitHeight(ToolbarIconSize);
        refreshIM.setFitWidth(ToolbarIconSize);
        Button refreshBtn = new Button();
        refreshBtn.setOnAction(event -> {
            if (treeView.getRoot() != null) {
                TreeFileItem treeItem = treeView.getRoot().getValue();
                treeView.setRootFileItem(new TreeFileItem(treeItem.getBaseFile(), FileFilters));
            }
        });
        refreshBtn.setGraphic(refreshIM);
        refreshBtn.setTooltip(new Tooltip("Refresh"));
        toolbar.getChildren().addAll(expandAllBtn, collapseAllBtn, refreshBtn);

        treeView = new FileTreeControl();
        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                FileTreeControl.MyTreeItem item = (FileTreeControl.MyTreeItem)newVal;
                if (!item.getValue().isDirectory()) {
                    previewFileItem = item.getValue();
                    refreshPreview();
                }
            }
        });

        result.setTop(toolbar);
        result.setCenter(treeView);
        return result;
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
                            Paths.get(file.getAbsolutePath())), "utf8"));
        } catch (IOException ioexcp) {
            ioexcp.printStackTrace();
            alertException(ioexcp);
        } catch (Exception fuck) {
            fuck.printStackTrace();
            alertException(fuck);
        }
    }

    private void alertException(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("哟，有一个错误出现了");
        alert.setHeaderText(e.getClass().getName());
        alert.setContentText(e.getLocalizedMessage());

        alert.show();
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

    private Pane generatePreviewToolbar() throws URISyntaxException {
        HBox result = new HBox();

        Label themeSelectorLabel = new Label("Theme:");
        themeSelectorLabel.setPadding(new Insets(2, 3, 2, 3));
        ComboBox<String> themeSelector = new ComboBox<String>(
                FXCollections.observableArrayList(StyleFileList.names)
        );
        themeSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            config.set_styleName(newVal);
            refreshPreview();
        });
        themeSelector.setValue(config.get_styleName());

        Label showLineNumberLabel = new Label("Show Line Number:");
        showLineNumberLabel.setPadding(new Insets(2, 3, 2, 3));
        CheckBox showLineNumberCheckBox = new CheckBox();
        showLineNumberCheckBox.setSelected(true);
        showLineNumberCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            config.set_showLineNumber(newVal);
            refreshPreview();
        });

        Label fontSizeLabel = new Label("Font size:");
        fontSizeLabel.setPadding(new Insets(2, 3, 2, 3));
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
        final VBox result = new VBox();

        final HBox buttonBars = new HBox();
        ImageView openImgView = new ImageView(
                new Image(
                        getClass().getResourceAsStream("/resources/icons/si-glyph-folder-open.png")));
        final Button openBtn = new Button();
        openImgView.setFitHeight(TitleBarIconSize);
        openImgView.setFitWidth(TitleBarIconSize);
        openBtn.setTooltip(new Tooltip("Open directory"));
        openBtn.setGraphic(openImgView);

        ImageView convertImgView = new ImageView(
                new Image(
                        getClass().getResourceAsStream("/resources/icons/si-glyph-triangle-right.png")));
        convertImgView.setFitHeight(TitleBarIconSize);
        convertImgView.setFitWidth(TitleBarIconSize);
        Button convertBtn = new Button();
        convertBtn.setTooltip(new Tooltip("Begin Convert"));
        convertBtn.setGraphic(convertImgView);

        ImageView previewImgView = new ImageView(
                new Image(
                        getClass().getResourceAsStream("/resources/icons/si-glyph-view.png")));
        previewImgView.setFitWidth(TitleBarIconSize);
        previewImgView.setFitHeight(TitleBarIconSize);
        Button previewBtn = new Button();
        previewBtn.setTooltip(new Tooltip("Preview"));
        previewBtn.setGraphic(previewImgView);

        ImageView settingImgView = new ImageView(
                new Image(
                        getClass().getResourceAsStream("/resources/icons/si-glyph-gear.png")));
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

        final GridPane infoGrid = new GridPane();

        final Label labelCurrentPath = new Label("Current Path:");
        currentPathTextField = new TextField();
        currentPathTextField.setDisable(true);

        // BorderPane targetPathPane = new BorderPane();
        final Label targetPathLabel = new Label("Target Path:");
        targetPathTextField = new TextField();
        targetPathTextField.setEditable(true);
        Button openTargetPathBtn = new Button("Open");
        BorderPane targetBorderPane = new BorderPane();
        targetBorderPane.setCenter(targetPathTextField);
        targetBorderPane.setRight(openTargetPathBtn);
        openTargetPathBtn.setOnAction((event) -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File chooseDirectory = directoryChooser.showDialog(primaryStage);
            if (chooseDirectory != null) {
                targetPathTextField.setText(chooseDirectory.getAbsolutePath());
            }
        });

        final ColumnConstraints col1 = new ColumnConstraints();
        final ColumnConstraints col2 = new ColumnConstraints(200, Control.USE_COMPUTED_SIZE, Double.MAX_VALUE);
        col2.setHgrow(Priority.ALWAYS);
        infoGrid.getColumnConstraints().addAll(col1, col2);
        infoGrid.add(labelCurrentPath, 0, 0);
        infoGrid.add(currentPathTextField, 1, 0);
        infoGrid.add(targetPathLabel, 0, 1);
        infoGrid.add(targetBorderPane, 1, 1);

        File currentPath = new File("file");
        targetPathTextField.textProperty().setValue(currentPath.getAbsolutePath());

        result.getChildren().addAll(buttonBars, infoGrid);
        return result;
    }

    private void handleOpenDirectory(Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirecotry = directoryChooser.showDialog(primaryStage);
        if (selectedDirecotry != null) {
            searchingPath = selectedDirecotry;
            currentPathTextField.setText(selectedDirecotry.getAbsolutePath());

            // clear the data and search again
            treeView.setRootFileItem(new TreeFileItem(selectedDirecotry, FileFilters));
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
