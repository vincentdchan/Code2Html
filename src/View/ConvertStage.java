package View;

import Model.Configuration;
import Model.SyncGenerator;
import Model.TreeFileItem;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by cdzos on 2017/5/4.
 */
public class ConvertStage extends Stage {

    private Scene mainScene;
    private Label convertPathLabel;
    private ProgressBar progressBar;

    private TreeFileItem rootFileItem;
    private File targetPath;

    private Configuration config;

    private Lock _lock = new ReentrantLock();
    private int convertCount;

    public ConvertStage() {
        super();

        BorderPane mainPane = new BorderPane();
        mainScene = new Scene(mainPane, 480, 80);

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10, 0, 0, 0));
        pane.setLeft(new Label("Converting: "));
        convertPathLabel = new Label();
        pane.setCenter(convertPathLabel);

        progressBar = new ProgressBar(0.5);
        progressBar.prefWidthProperty().bind(mainPane.widthProperty());
        mainPane.setTop(pane);
        mainPane.setCenter(progressBar);

        initModality(Modality.APPLICATION_MODAL);
        setTitle("Convert");
        setScene(mainScene);
        setResizable(false);
    }

    public void beginConvert() {
        beginConvert(rootFileItem, targetPath);
    }

    public void setRootFileItem(TreeFileItem rootFileItem) {
        this.rootFileItem = rootFileItem;
    }

    private void beginConvert(TreeFileItem srcDirectory, File targetDirectory) {
        ConvertTask convertTask = new ConvertTask();
        convertTask.setSrcDirectory(srcDirectory);
        convertTask.setTargetDirectory(targetDirectory);

        convertPathLabel.textProperty().bind(convertTask.messageProperty());
        progressBar.progressProperty().bind(convertTask.progressProperty());

        long beginTime = System.currentTimeMillis();
        convertTask.setOnSucceeded(event -> {
            long finishedTime = System.currentTimeMillis();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("恭喜");
            alert.setHeaderText("恭喜！转换成功！");
            alert.setContentText("共转换 " + convertCount + " 个文件，耗时 " + (finishedTime - beginTime) * 1.0 / 1000 + "秒");
            alert.show();

            close();
        });

        convertTask.exceptionProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                newVal.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText("转换过程中出现了一个错误");

                alert.setContentText(newVal.getMessage());
                alert.show();

                close();
            }
        });

        new Thread(convertTask).start();
    }

    private class ConvertTask extends Task<Integer> {

        private TreeFileItem _srcDirectory;
        private File _targetDirectory;
        private int workedCount;

        @Override
        protected Integer call() throws Exception {
            _lock.lock();
            try {
                convertCount = _srcDirectory.countCheckItem();
            } finally {
                _lock.unlock();
            }
            workedCount = 0;
            uniqueConvert(_srcDirectory, _targetDirectory);
            return convertCount;
        }

        private void uniqueConvert(TreeFileItem srcDirectory, File targetDirectory) throws IOException, Model.SyncGenerator.NotSupportedFiletypes, InterruptedException {
            SyncGenerator generator = new SyncGenerator(config);

            for (TreeFileItem child : srcDirectory.children()) {
                if (child.isDirectory() && child.hasCheckedItem()) {
                    String name = child.getBaseFile().getName();
                    File targetDir = new File(targetDirectory, name);
                    if (targetDir.exists()) {   // do not make dir if it exists
                        uniqueConvert(child, targetDir);
                    } else {
                        if (targetDir.mkdir()) {
                            uniqueConvert(child, targetDir);
                        } else {
                            System.out.println("Make directory error: " + targetDir.getAbsolutePath());
                        }
                    }
                } else if (!child.isDirectory() && child.isChecked()) {    // is file
                    File baseFile = child.getBaseFile();
                    workedCount++;
                    updateMessage(baseFile.getAbsolutePath());
                    updateProgress(workedCount, convertCount);
                    File targetFile = new File(targetDirectory, baseFile.getName() + ".html");

                    String srcCode = new String(Files.readAllBytes(
                            Paths.get(baseFile.getAbsolutePath())), "utf8");

                    String result = generator.convert(baseFile.getName(), srcCode);

                    OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(targetFile), "utf8");
                    out.write(result);
                    out.close();
                }
            }
        }

        public int getConvertCount() {
            return convertCount;
        }

        public TreeFileItem getSrcDirectory() {
            return _srcDirectory;
        }

        public void setSrcDirectory(TreeFileItem srcDirectory) {
            this._srcDirectory = srcDirectory;
        }

        public File getTargetDirectory() {
            return _targetDirectory;
        }

        public void setTargetDirectory(File targetDirectory) {
            this._targetDirectory = targetDirectory;
        }

    }

    public File getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(File targetPath) {
        this.targetPath = targetPath;
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

}
