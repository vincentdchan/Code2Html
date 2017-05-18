package Eggs;
;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Created by ZMYang on 2017/5/18.
 */
public class Animation {
    private FadeTransition fadeTransition;

    public Animation(Node node , double seconds , double from , double to){
        fadeTransition = new FadeTransition(Duration.millis(seconds),node);
        fadeTransition.setFromValue(from);
        fadeTransition.setToValue(to);
        fadeTransition.setCycleCount(1);
    }
    public Animation(Node node){
        this(node , 500 , 0.0 , 1.0);
    }

    public void play(){
        fadeTransition.play();
    }

    public FadeTransition getFadeTransition() {
        return fadeTransition;
    }

    public void setFadeTransition(FadeTransition fadeTransition) {
        this.fadeTransition = fadeTransition;
    }
}
