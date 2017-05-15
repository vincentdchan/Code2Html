package View;

import Model.Configuration;
import Model.SyncGenerator;
import Model.TreeFileItem;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static int TitleBarIconSize = 24;
    public static int ToolbarIconSize = 12;
    public static String[] FileFilters = {".c", ".java", ".h", ".html", ".htm"};

    private TextField currentPathTextField;
    private TextField targetPathTextField;
    private File srcPathFile;
    private BorderPane borderPane;
    private FileTreeControl treeView;
    private TableView<FileItem> rightTable;
    private WebView previewWebView;

    private ObservableList<FileItem> dataRight;
    private File searchingPath;

    private TreeFileItem previewFileItem;

    private Configuration config;

    @Override
    public void start(Stage primaryStage) throws Exception {
        config = new Configuration();

        srcPathFile = new File("file");
//      Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
        primaryStage.setTitle("源代码自动转换程序");
        StackPane backpane = new StackPane();
//        backpane.setStyle("-fx-background-color:cyan");

        borderPane = new BorderPane();
        Pane topPane = generateTopPane(primaryStage);

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

        backpane.getChildren().add(borderPane);

        Scene scene = new Scene(backpane, 800, 600);
        primaryStage.setScene(scene);
        scene.setFill(null);
        scene.getStylesheets().add("/resources/ui.css");

        //窗口图标
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/image/c2html.png")));

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
        expandAllBtn.setTooltip(new Tooltip("全部展开"));

        ImageView collapseIM = new ImageView(new Image(
                getClass().getResourceAsStream("/resources/icons/si-glyph-arrow-resize-4.png")));
        collapseIM.setFitHeight(ToolbarIconSize);
        collapseIM.setFitWidth(ToolbarIconSize);
        Button collapseAllBtn = new Button();
        collapseAllBtn.setOnAction(event -> treeView.collapseAll());
        collapseAllBtn.setGraphic(collapseIM);
        collapseAllBtn.setTooltip(new Tooltip("全部合并"));

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
        refreshBtn.setTooltip(new Tooltip("刷新"));
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

        File file = previewFileItem.getBaseFile();
        Task<String> task = new Task<String>() {

            @Override
            protected String call() throws Exception {
                SyncGenerator generator = new SyncGenerator(config);
                return generator.convert(file.getName(), new String(
                        Files.readAllBytes(Paths.get(file.getAbsolutePath())), "utf8"
                ));
            }

        };

        task.setOnSucceeded(event -> {
            previewWebView.getEngine().loadContent((String)event.getSource().getValue());
        });

        task.exceptionProperty().addListener((slc, oldVal, newVal) ->  {
            if (newVal != null) {
                newVal.printStackTrace();
                alertException((Exception) newVal);
            }
        });

        new Thread(task).start();
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

    private List<String> findMonoFontFamilies(List<String> fonts) {
        List<String> result = new ArrayList<>();
        final Text thinTxt = new Text("1 l");
        final Text thickTxt = new Text("MWX");
        for (String fontFamilyName: fonts){
            Font font = Font.font(fontFamilyName, FontWeight.NORMAL, FontPosture.REGULAR, 14.0d);
            thinTxt.setFont(font);
            thickTxt.setFont(font);
            if (thinTxt.getLayoutBounds().getWidth() ==
                    thickTxt.getLayoutBounds().getWidth()) {
                result.add(fontFamilyName);
            }
        }
        return result;
    }

    private Pane generatePreviewToolbar() throws URISyntaxException {
        HBox result = new HBox();

        Label themeSelectorLabel = new Label("主题：");
        themeSelectorLabel.setPadding(new Insets(2, 3, 2, 3));
        ComboBox<String> themeSelector = new ComboBox<String>(
                FXCollections.observableArrayList(StyleFileList.names)
        );
        themeSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            config.set_styleName(newVal);
            refreshPreview();
        });
        themeSelector.setValue(config.get_styleName());

        Label showLineNumberLabel = new Label("显示行号：");
        showLineNumberLabel.setPadding(new Insets(2, 3, 2, 3));
        CheckBox showLineNumberCheckBox = new CheckBox();
        showLineNumberCheckBox.setSelected(true);
        showLineNumberCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            config.set_showLineNumber(newVal);
            refreshPreview();
        });

        Label fontFamilyLabel = new Label("字体：");
        fontFamilyLabel.setPadding(new Insets(2, 3, 2, 3));
        List<String> family = findMonoFontFamilies(Font.getFamilies());
        ComboBox<String> fontFamilySelector = new ComboBox<String>(
                FXCollections.observableArrayList(family)
        );
        fontFamilySelector.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                config.set_fontFamily(newVal);
                refreshPreview();
            }
        });


        Label fontSizeLabel = new Label("字号：");
        fontSizeLabel.setPadding(new Insets(2, 3, 2, 3));
        TextField fontSizeTextField = new TextField(Integer.toString(config.get_fontSize()));
        fontSizeTextField.setAlignment(Pos.TOP_RIGHT);
        fontSizeTextField.setPrefWidth(42);
        fontSizeTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() == 0) return;
            int value = Integer.parseInt(newVal);
            if (value >= 8 && value <= 48) {
                config.set_fontSize(value);
                refreshPreview();
            }
        });

        result.getChildren().addAll(themeSelectorLabel, themeSelector,
                showLineNumberLabel, showLineNumberCheckBox,
                fontFamilyLabel, fontFamilySelector,
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
        openBtn.setTooltip(new Tooltip("打开目录"));
        openBtn.setGraphic(openImgView);

        ImageView convertImgView = new ImageView(
                new Image(
                        getClass().getResourceAsStream("/resources/icons/si-glyph-triangle-right.png")));
        convertImgView.setFitHeight(TitleBarIconSize);
        convertImgView.setFitWidth(TitleBarIconSize);
        Button convertBtn = new Button();
        convertBtn.setTooltip(new Tooltip("开始转换"));
        convertBtn.setOnAction(event -> {
            ConvertStage stage = new ConvertStage();
            if (treeView.getRoot() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("哟，有一个错误出现了");
                alert.setHeaderText("错误");
                alert.setContentText("请先选择一个文件夹，然后再转换");

                alert.show();
                return;
            }
            stage.setRootFileItem(treeView.getRoot().getValue());
            String targetPathStr = targetPathTextField.getText();
            File targetFile = new File(targetPathStr);
            if (!targetFile.exists()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("哟，有一个错误出现了");
                alert.setHeaderText("错误");
                alert.setContentText("您设置的目标目录不存在");

                alert.show();
                return;
            }
            stage.setTargetPath(targetFile);
            stage.setConfig(config);
            stage.show();
            stage.beginConvert();
        });
        convertBtn.setGraphic(convertImgView);

        ImageView aboutImgView = new ImageView(
                new Image(
                        getClass().getResourceAsStream("/resources/icons/si-glyph-circle-info.png")));
        aboutImgView.setFitWidth(TitleBarIconSize);
        aboutImgView.setFitHeight(TitleBarIconSize);
        Button aboutBtn = new Button();
        aboutBtn.setTooltip(new Tooltip("关于"));
        aboutBtn.setOnAction(event -> {
            AboutStage stage = new AboutStage(this);
            stage.show();
        });
        aboutBtn.setGraphic(aboutImgView);

        buttonBars.getChildren().addAll(openBtn, convertBtn, aboutBtn);

        // Binding events
        openBtn.setOnAction(e -> {
            handleOpenDirectory(primaryStage);
        });

        final GridPane infoGrid = new GridPane();

        final Label labelCurrentPath = new Label("当前目录：");
        currentPathTextField = new TextField();
        currentPathTextField.setDisable(true);

        // BorderPane targetPathPane = new BorderPane();
        final Label targetPathLabel = new Label("目标目录：");
        targetPathTextField = new TextField();
        targetPathTextField.setEditable(true);
        Button openTargetPathBtn = new Button("打开...");
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
            // treeView.setRootFileItem(new TreeFileItem(selectedDirecotry, FileFilters));
            ScanStage scanStage = new ScanStage(this);
            scanStage.show();
            scanStage.beginReceive(new TreeFileItem(selectedDirecotry, FileFilters));
        }
    }

    public ObservableList<FileItem> getDataRight() {
        return dataRight;
    }

    public FileTreeControl getTreeView() {
        return treeView;
    }
}
