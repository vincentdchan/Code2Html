package Eggs;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Game extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    //    private BorderPane borderPane;
    private BorderPane topPane;
    //    private VBox centerPane;
    private TextField counter;
    public static TextField boom_count;
    public static int count;
    //    private Button reStart;
    private ImageView reStart;
    private Scene scene;
    private String[] levels;
    private MenuBar menuBar;
    private Menu menuGame;
    private String curLevel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        levels = new String[4];
        setLevels();
        curLevel = levels[0];
        BorderPane borderPane = new BorderPane();
        generateTopPane(borderPane, primaryStage);
        generateCenterPane(borderPane, curLevel);
        primaryStage.setTitle("AlphaSheep Sweeper");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/image/logo.png")));
//        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/image/logo.png")));
//        show();
    }

    private void generateTopPane(BorderPane borderPane, Stage primaryStage) throws Exception {
        topPane = new BorderPane();

        generateMenu(primaryStage);

        HBox leftHBox = new HBox();
        counter = new TextField();
//        counter.setStyle("-fx-background-color:black");
        counter.setDisable(true);
        counter.setPrefWidth(50);
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/image/counter.png")));
        imageView.setFitHeight(32);
        imageView.setFitWidth(32);
        leftHBox.getChildren().addAll(imageView, counter);
        Image image = new Image(getClass().getResourceAsStream("/image/sheep.png"));
        reStart = new ImageView(image);
//        reStart.setOnMouseEntered(e -> {
//            reStart.setStyle("-fx-background-color:lightblue");
//        });
//        reStart.setOnMouseExited(e -> {
//            reStart.setStyle("-fx-background-color:null");
//        });
        reStart.setOnMouseClicked(E -> {
            try {
                setReStartAction(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        HBox rightHbox = new HBox();
        boom_count = new TextField();
//        boom_count.setStyle("-fx-background-color:black");
        boom_count.setDisable(true);
        boom_count.setPrefWidth(50);
        rightHbox.getChildren().addAll(new ImageView(new Image(getClass().getResourceAsStream("/image/sheepBoom.png"))), boom_count);

        topPane.setTop(menuBar);
        topPane.setLeft(leftHBox);
        topPane.setRight(rightHbox);
        topPane.setCenter(reStart);
        topPane.setBottom(new Separator());
        borderPane.setTop(topPane);
    }

    private void generateMenu(Stage primaryStage) throws Exception {
        menuBar = new MenuBar();
        menuGame = new Menu("Game");
        MenuItem menuItemRestart = new MenuItem("Restart");
        menuItemRestart.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/image/sheep.png"))));
        menuItemRestart.setOnAction(E -> {
            try {
                setReStartAction(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        MenuItem menuItemEasy = new MenuItem("Easy");
//        menuItemEasy.setGraphic(new ImageView(new Image("file:///../image/menuSheep.png")));
        MenuItem menuItemMedium = new MenuItem("Medium");
        MenuItem menuItemDifficult = new MenuItem("Difficult");
        MenuItem menuItemSelf = new MenuItem("Custom");
        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/image/ExitSheep.png"))));
        menuItemExit.setOnAction(e -> {
            primaryStage.close();
        });
        menuGame.getItems().addAll(menuItemRestart, new SeparatorMenuItem(), menuItemEasy,
                menuItemMedium, menuItemDifficult, menuItemSelf, new SeparatorMenuItem(), menuItemExit);
        menuBar.getMenus().add(menuGame);
        for (int i = 2; i <= 4; i++) {
            if (menuGame.getItems().get(i).getText().equals(curLevel)) {
                menuGame.getItems().get(i).setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/image/menuSheep.png"))));
            }
        }
        setMenuItemAction(primaryStage);
    }

    private void setLevels() {
        levels[0] = "Easy";
        levels[1] = "Medium";
        levels[2] = "Difficult";
        levels[3] = "Custom";
    }

    private void generateCenterPane(BorderPane borderPane, String level) throws Exception {
        VBox centerPane = new VBox();
        Boom boom = new Boom(level);
        count = boom.getBoom_count();
        boom_count.setText("" + count);
        scene = new Scene(borderPane, 55 * (boom.getCol() + 1), 55 * (boom.getRow() + 2));
        for (int i = 0; i <= boom.getRow(); i++) {
            HBox hBox = new HBox();
            for (int j = 0; j <= boom.getCol(); j++) {
                BoomButton boomButton = new BoomButton(boom, i, j);
                boomButton.setSurroundingBoom(boom.getSurroundingBoom()[i][j]);
                boomButton.getButton().setPrefWidth(55);
                boomButton.getButton().setPrefHeight(55);
//                boomButton.getButton().setText("" + boom.getSurroundingBoom()[i][j]);
//                boomButton.getButton().prefWidthProperty().bind(scene.widthProperty().divide(boom.getRow() + 1));
//                boomButton.getButton().prefHeightProperty().bind(scene.heightProperty().divide(boom.getCol() + 1));
                boom.getBoom()[i][j] = boomButton.getButton();
                hBox.getChildren().add(boomButton.getButton());
            }
            centerPane.getChildren().add(hBox);
            borderPane.setCenter(centerPane);
        }
    }

    private void setMenuItemAction(Stage primaryStage) throws Exception {
        for (int i = 2; i <= 4; i++) {
            final int t = i;
            menuGame.getItems().get(i).setOnAction(e -> {
                curLevel = levels[t - 2];
                BorderPane borderPane = new BorderPane();
                try {
                    generateTopPane(borderPane, primaryStage);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                try {
                    generateCenterPane(borderPane, levels[t - 2]);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                for (int j = 2; j <= 4; j++) {
                    menuGame.getItems().get(j).setGraphic(null);
                }
                menuGame.getItems().get(t).setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/image/menuSheep.png"))));
                primaryStage.setScene(scene);
                menuGame.getItems().get(0).setOnAction(E -> {
                    try {
                        setReStartAction(primaryStage);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });
            });
        }
    }

    private void setReStartAction(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        generateTopPane(borderPane, primaryStage);
        generateCenterPane(borderPane, curLevel);
        primaryStage.setScene(scene);
    }
}
