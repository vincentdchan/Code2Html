package View;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by cdzos on 2017/5/4.
 */
public class ConvertStage extends Stage {

    private Scene mainScene;

    public ConvertStage() {
        super();

        VBox myPane = new VBox();
        mainScene = new Scene(myPane, 100, 100);
        Button testBtn = new Button("test");
        testBtn.setOnAction(event -> {
            close();
        });
        myPane.getChildren().addAll(testBtn);

        initModality(Modality.APPLICATION_MODAL);
        setTitle("Convert");
        setScene(mainScene);
        setResizable(false);
    }

}
