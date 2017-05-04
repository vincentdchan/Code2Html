package View;

import Model.TreeFileItem;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.concurrent.BlockingQueue;

/**
 * Created by cdzos on 2017/5/4.
 */
public class ScanStage extends Stage {

    private Scene mainScene;
    private Label mainLabel;
    private FileTreeControl.MyTreeItem result;
    private Main mainStage;
    private boolean stopped = false;

    public ScanStage(Main mainStage) {
        super();

        this.mainStage = mainStage;
        VBox subPane = new VBox();
        subPane.setPadding(new Insets(15));
        BorderPane myPane = new BorderPane();

        mainScene = new Scene(myPane, 360, 60);
        mainLabel = new Label();
        myPane.setCenter(subPane);
        subPane.getChildren().add(new Label("Scan: "));
        subPane.getChildren().add(mainLabel);

        initModality(Modality.APPLICATION_MODAL);
        setTitle("Scanning...");
        setScene(mainScene);
        setResizable(false);
    }

    void beginReceive(TreeFileItem treeFileItem) {
        Task<String> tsk = new Task<String>() {
            @Override
            protected String call() throws Exception {
                treeFileItem.setGetter((String path) -> {
                    updateValue(path);
                });
                result = new FileTreeControl.MyTreeItem(treeFileItem);
                return result.getValue().getBaseFile().getAbsolutePath();
            }
        };
        tsk.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                close();
                if (!stopped) {
                    mainStage.getTreeView().setRoot(result);
                }
            }
        });
        mainLabel.textProperty().bind(tsk.valueProperty());
        new Thread(tsk).start();

        setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                stopped = true;
            }
        });
    }

}
