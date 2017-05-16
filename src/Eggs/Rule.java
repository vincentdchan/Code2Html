package Eggs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by ZMYang on 2017/5/13.
 */
public class Rule{

    public static void lose(Boom boom, Image imagePath) {
        Alert alert = new Alert(Alert.AlertType.NONE , "You lose!" , ButtonType.OK);
        alert.setGraphic(new ImageView(imagePath));
        alert.setTitle("What a pity!");
        alert.initOwner(null);
        for(int i = 0 ; i < boom.getRow() + 1 ; i++){
            for(int j = 0 ; j < boom.getCol() + 1; j++){
                boom.getTableMode()[i][j] = -2;
            }
        }
//        Media media = new Media("lose.mp3");
//        MediaPlayer mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.play();
//        MediaView mediaView = new MediaView(media);
        alert.showAndWait();
    }

    public static boolean isWin(Boom boom){
        boolean flag = true;
        for(int i = 0 ;i < boom.getRow() + 1 ; i++){
            for(int j = 0 ; j < boom.getCol() + 1 ; j++){
                if(boom.getTableIsWin()[i][j] == 0){
                    flag = false ;
                }
            }
        }
        return flag ;
    }
    public static void showWin(Boom boom, Image filePath){
        boolean iw = Rule.isWin(boom);
        if(iw){
            Alert alert = new Alert(Alert.AlertType.NONE , "You win!" , ButtonType.OK);
            alert.initOwner(null);
            alert.setGraphic(new ImageView(filePath));
            alert.setTitle("Congratulations!");
            for(int i = 0 ; i < boom.getRow() + 1 ; i++){
                for(int j = 0 ; j < boom.getCol() + 1; j++){
                    boom.getTableMode()[i][j] = -2;
                }
            }
            alert.showAndWait();
        }
    }
}
