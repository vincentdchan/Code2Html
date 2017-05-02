package View;

import Model.Configuration;
import Model.Getter;
import Model.Java2Html;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdzos on 2017/4/29.
 */
public class ConvertActionHandler implements EventHandler<ActionEvent> {

    private Main mainApp;
    private List<FileItem> fileItemList;
    private List<File> fileList;
    private String filePath;

    public ConvertActionHandler(Main mainApp, String filePath) {
        this.mainApp = mainApp;
        this.filePath = filePath;
    }

    @Override
    public void handle(ActionEvent event) {
        fileItemList = mainApp.getDataRight();
        if (fileItemList == null || fileItemList.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.NONE, "请选定要转换的文件!", ButtonType.OK);
            alert.setTitle("通知");
            alert.showAndWait();
            return;
        }
        fileList = new ArrayList<>();
        for (FileItem fileItem : fileItemList) {
            fileList.add(fileItem.getFile());
        }
        for (File file : fileList) {
            try {
                String content = new String(
                        Files.readAllBytes(Paths.get(file.getAbsolutePath()))
                );
                Configuration config = new Configuration();
                Java2Html converter = new Java2Html();
                converter.set_config(config);
                converter.addGetter(new Getter(fileList, file.getName(), filePath));
                converter.convert(file.getName(), content);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (Model.Generator.NotSupportedFiletypes e2) {
                e2.printStackTrace();
            }
        }
        // WebShow.happen(fileList, filePath);
    }

}
