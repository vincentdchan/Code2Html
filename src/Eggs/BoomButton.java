package Eggs;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Created by ZMYang on 2017/5/9.
 */
public class BoomButton extends Button {
    private Button button;
    private int surroundingBoom;
    private Boom boom;
    private int rowIndex;
    private int colIndex;
    private Color[] colorTable;
//    private int mode;//1 代表未按下， 2 代表右键一次出现小旗 ， 3 代表右键2次出现问号，0 代表已经按下。
//    int cnt = 0;

    public BoomButton(Boom boom, int i, int j) {
        this.boom = boom;
        rowIndex = i;
        colIndex = j;
        fillColorTable();
        button = new Button();
//        button.setText(null);
        button.setStyle("-fx-border-color:black");
//        mode = 1;
        button.setOnMouseClicked((MouseEvent e) -> {
            MouseButton mouseButton = e.getButton();
            if (e.getClickCount() == 1) {
                if (mouseButton == MouseButton.PRIMARY) {
                    setButtonPrimaryAction(button, this.rowIndex, this.colIndex, this.surroundingBoom);
                } else if (mouseButton == MouseButton.SECONDARY) {
                    setButtonSecondaryAction();
                }
            } else if (e.getClickCount() == 2) {
                if (setButtonDoubleClickAction()) {
                    extendSurrounding();
                }
            }
        });
//        setButtonStyle();
    }

    public void setButtonPrimaryAction(Button button, int rowIndex, int colIndex, int surroundingBoom) {
        if (boom.getTableMode()[rowIndex][colIndex] == 1) {
            boom.getTableMode()[rowIndex][colIndex] = 0;
            if (surroundingBoom == -1) {
                for (int i = 0; i < boom.getRow() + 1; i++) {
                    for (int j = 0; j < boom.getCol() + 1; j++) {
                        if (boom.getSurroundingBoom()[i][j] == -1) {
                            boom.getBoom()[i][j].setStyle("-fx-background-color:blue;-fx-border-color:black");
                            boom.getBoom()[i][j].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/image/sheepBoom.png"))));
                            FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(800), boom.getBoom()[i][j].getGraphic());
                            ft.setAutoReverse(true);
                            ft.setCycleCount(Timeline.INDEFINITE);
                            ft.setFromValue(1.0);
                            ft.setToValue(0.0);
                            ft.play();
                        }
                        if (boom.getTableMode()[i][j] == 2) {
                            boom.getBoom()[i][j].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/image/no.png"))));
                        }
                    }
                }
                button.setStyle("-fx-background-color:red;-fx-border-color:black");
//                button.setTextFill(colorTable[surroundingBoom - 1]);
                Rule.lose(boom, new Image(getClass().getResourceAsStream("/image/lose.png")));
            } else if (surroundingBoom == 0) {
                extendZero(boom, rowIndex, colIndex);
                Rule.showWin(boom, new Image(getClass().getResourceAsStream("/image/firework.png")));
            } else {
                button.setText("" + surroundingBoom);
                button.setTextFill(colorTable[surroundingBoom - 1]);
//                button.setStyle(colorTable[surroundingBoom - 1]);
//                button.setTextFill(Color.RED);
//                button.setStyle("-fx-text-color:red");
                boom.getTableIsWin()[rowIndex][colIndex] = 1;
//                String s = "" + surroundingBoom ;
//                Label l = new Label(s);
//                l.setFont(Font.font("Time" , FontWeight.BOLD ,12));
//                l.setTextFill(Color.RED);
//                button.setText(l.getText());
                button.setStyle("-fx-background-color:lightblue;-fx-border-color:black");
                Rule.showWin(boom, new Image(getClass().getResourceAsStream("/image/firework.png")));
            }
        }
    }

    private void setButtonSecondaryAction() {
        if (boom.getTableMode()[rowIndex][colIndex] != 0) {
            boom.getTableMode()[rowIndex][colIndex]++;
            if (boom.getTableMode()[rowIndex][colIndex] > 3) {
                boom.getTableMode()[rowIndex][colIndex] = 1;
                button.setGraphic(null);
            }
            if (boom.getTableMode()[rowIndex][colIndex] == 2) {
                Image image = new Image(getClass().getResourceAsStream("/image/flag.png"));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                button.setGraphic(imageView);
                int t = Game.count;
                Game.count -= 1;
                Game.boom_count.setText("" + (t - 1));
            }
            if (boom.getTableMode()[rowIndex][colIndex] == 3) {
                Image image = new Image(getClass().getResourceAsStream("/image/qm.png"));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                button.setGraphic(imageView);
                int t = Game.count;
                Game.count += 1;
                Game.boom_count.setText("" + (t + 1));
            }
        }
    }

    private boolean setButtonDoubleClickAction() {
        int mode_count = 0;
        if (rowIndex - 1 >= 0 && colIndex - 1 >= 0 && boom.getTableMode()[rowIndex - 1][colIndex - 1] == 2) {
            mode_count++;
        }
        if (rowIndex - 1 >= 0 && boom.getTableMode()[rowIndex - 1][colIndex] == 2) {
            mode_count++;
        }
        if (rowIndex - 1 >= 0 && colIndex + 1 <= boom.getCol() && boom.getTableMode()[rowIndex - 1][colIndex + 1] == 2) {
            mode_count++;
        }
        if (colIndex - 1 >= 0 && boom.getTableMode()[rowIndex][colIndex - 1] == 2) {
            mode_count++;
        }
        if (colIndex + 1 <= boom.getCol() && boom.getTableMode()[rowIndex][colIndex + 1] == 2) {
            mode_count++;
        }
        if (rowIndex + 1 <= boom.getRow() && colIndex - 1 >= 0 && boom.getTableMode()[rowIndex + 1][colIndex - 1] == 2) {
            mode_count++;
        }
        if (rowIndex + 1 <= boom.getRow() && boom.getTableMode()[rowIndex + 1][colIndex] == 2) {
            mode_count++;
        }
        if (rowIndex + 1 <= boom.getRow() && colIndex + 1 <= boom.getCol() && boom.getTableMode()[rowIndex + 1][colIndex + 1] == 2) {
            mode_count++;
        }
        return (mode_count == boom.getSurroundingBoom()[rowIndex][colIndex]);
    }

    private void extendSurrounding() {
        if (rowIndex - 1 >= 0) {
            if (colIndex - 1 >= 0) {
                setButtonPrimaryAction(boom.getBoom()[rowIndex - 1][colIndex - 1],
                        rowIndex - 1, colIndex - 1,
                        boom.getSurroundingBoom()[rowIndex - 1][colIndex - 1]);
            }
            setButtonPrimaryAction(boom.getBoom()[rowIndex - 1][colIndex],
                    rowIndex - 1, colIndex,
                    boom.getSurroundingBoom()[rowIndex - 1][colIndex]);
            if (colIndex + 1 <= boom.getCol()) {
                setButtonPrimaryAction(boom.getBoom()[rowIndex - 1][colIndex + 1],
                        rowIndex - 1, colIndex + 1,
                        boom.getSurroundingBoom()[rowIndex - 1][colIndex + 1]);
            }
        }
        if (colIndex - 1 >= 0) {
            setButtonPrimaryAction(boom.getBoom()[rowIndex][colIndex - 1],
                    rowIndex, colIndex - 1,
                    boom.getSurroundingBoom()[rowIndex][colIndex - 1]);
        }
        if (colIndex + 1 <= boom.getCol()) {
            setButtonPrimaryAction(boom.getBoom()[rowIndex][colIndex + 1],
                    rowIndex, colIndex + 1,
                    boom.getSurroundingBoom()[rowIndex][colIndex + 1]);
        }
        if (rowIndex + 1 <= boom.getRow()) {
            if (colIndex - 1 >= 0) {
                setButtonPrimaryAction(boom.getBoom()[rowIndex + 1][colIndex - 1],
                        rowIndex + 1, colIndex - 1,
                        boom.getSurroundingBoom()[rowIndex + 1][colIndex - 1]);
            }
            setButtonPrimaryAction(boom.getBoom()[rowIndex + 1][colIndex],
                    rowIndex + 1, colIndex,
                    boom.getSurroundingBoom()[rowIndex + 1][colIndex]);
            if (colIndex + 1 <= boom.getCol()) {
                setButtonPrimaryAction(boom.getBoom()[rowIndex + 1][colIndex + 1],
                        rowIndex + 1, colIndex + 1,
                        boom.getSurroundingBoom()[rowIndex + 1][colIndex + 1]);
            }
        }
    }

    private void extendZero(Boom boom, int row, int col) {
        boom.getBoom()[row][col].setStyle("-fx-background-color:grey;-fx-border-color:black");
        boom.getSurroundingBoom()[row][col] = -2;
        boom.getTableIsWin()[row][col] = 1;
        boom.getTableMode()[row][col] = 0;
//        boom.getBoom()[row][col].setText("" + boom.getSurroundingBoom()[row][col]);
//        boom.getBoom()[row][col].setDisable(true);
//        mode = 0;
//        System.out.println(row + " " + col);
        if (row - 1 >= 0 && col - 1 >= 0) {
            if (boom.getSurroundingBoom()[row - 1][col - 1] != 0
                    && boom.getSurroundingBoom()[row - 1][col - 1] != -2
                    && boom.getTableMode()[row - 1][col - 1] == 1) {
                boom.getBoom()[row - 1][col - 1].setText("" + boom.getSurroundingBoom()[row - 1][col - 1]);
                boom.getBoom()[row - 1][col - 1].setTextFill(colorTable[boom.getSurroundingBoom()[row - 1][col - 1] - 1]);
                boom.getBoom()[row - 1][col - 1].setStyle("-fx-background-color:lightblue;-fx-border-color:black");
//                boom.getBoom()[row - 1][col - 1].setStyle(colorTable[boom.getSurroundingBoom()[row - 1][col - 1]]);
                boom.getTableIsWin()[row - 1][col - 1] = 1;
            }
            if (boom.getSurroundingBoom()[row - 1][col - 1] == 0 && boom.getTableMode()[row - 1][col - 1] == 1) {
//                boom.getSurroundingBoom()[row - 1][col - 1] = -2;
//                boom.getBoom()[row - 1][col - 1].setStyle("-fx-background-color:null;-fx-border-color:black");
                extendZero(boom, row - 1, col - 1);
            }
            if (boom.getTableMode()[row - 1][col - 1] == 1) {
                boom.getTableMode()[row - 1][col - 1] = 0;
            }
        }
        if (row - 1 >= 0) {
            if (boom.getSurroundingBoom()[row - 1][col] != 0
                    && boom.getSurroundingBoom()[row - 1][col] != -2
                    && boom.getTableMode()[row - 1][col] == 1) {
                boom.getBoom()[row - 1][col].setText("" + boom.getSurroundingBoom()[row - 1][col]);
                boom.getBoom()[row - 1][col].setTextFill(colorTable[boom.getSurroundingBoom()[row - 1][col] - 1]);
                boom.getBoom()[row - 1][col].setStyle("-fx-background-color:lightblue;-fx-border-color:black");
                boom.getTableIsWin()[row - 1][col] = 1;
            }
            if (boom.getSurroundingBoom()[row - 1][col] == 0 && boom.getTableMode()[row - 1][col] == 1) {
//                boom.getSurroundingBoom()[row - 1][col] = -2;
//                boom.getBoom()[row - 1][col].setStyle("-fx-background-color:null;-fx-border-color:black");
                extendZero(boom, row - 1, col);
            }
            if (boom.getTableMode()[row - 1][col] == 1) {
                boom.getTableMode()[row - 1][col] = 0;
            }
        }
        if (row - 1 >= 0 && col + 1 <= boom.getCol()) {
            if (boom.getSurroundingBoom()[row - 1][col + 1] != 0
                    && boom.getSurroundingBoom()[row - 1][col + 1] != -2
                    && boom.getTableMode()[row - 1][col + 1] == 1) {
                boom.getBoom()[row - 1][col + 1].setText("" + boom.getSurroundingBoom()[row - 1][col + 1]);
                boom.getBoom()[row - 1][col + 1].setStyle("-fx-background-color:lightblue;-fx-border-color:black");
                boom.getBoom()[row - 1][col + 1].setTextFill(colorTable[boom.getSurroundingBoom()[row - 1][col + 1] - 1]);
                boom.getTableIsWin()[row - 1][col + 1] = 1;
            }
            if (boom.getSurroundingBoom()[row - 1][col + 1] == 0 && boom.getTableMode()[row - 1][col + 1] == 1) {
//                boom.getSurroundingBoom()[row - 1][col + 1] = -2;
//                boom.getBoom()[row - 1][col + 1].setStyle("-fx-background-color:null;-fx-border-color:black");
                extendZero(boom, row - 1, col + 1);
            }
            if (boom.getTableMode()[row - 1][col + 1] == 1) {
                boom.getTableMode()[row - 1][col + 1] = 0;
            }
        }
        if (col - 1 >= 0) {
            if (boom.getSurroundingBoom()[row][col - 1] != 0
                    && boom.getSurroundingBoom()[row][col - 1] != -2
                    && boom.getTableMode()[row][col - 1] == 1) {
                boom.getBoom()[row][col - 1].setText("" + boom.getSurroundingBoom()[row][col - 1]);
                boom.getBoom()[row][col - 1].setTextFill(colorTable[boom.getSurroundingBoom()[row][col - 1] - 1]);
                boom.getBoom()[row][col - 1].setStyle("-fx-background-color:lightblue;-fx-border-color:black");
                boom.getTableIsWin()[row][col - 1] = 1;
            }
            if (boom.getSurroundingBoom()[row][col - 1] == 0 && boom.getTableMode()[row][col - 1] == 1) {
//                boom.getSurroundingBoom()[row][col - 1] = -2;
//                boom.getBoom()[row][col - 1].setStyle("-fx-background-color:null;-fx-border-color:black");
                extendZero(boom, row, col - 1);
            }
            if (boom.getTableMode()[row][col - 1] == 1) {
                boom.getTableMode()[row][col - 1] = 0;
            }
        }
        if (col + 1 <= boom.getCol()) {
            if (boom.getSurroundingBoom()[row][col + 1] != 0
                    && boom.getSurroundingBoom()[row][col + 1] != -2
                    && boom.getTableMode()[row][col + 1] == 1) {
                boom.getBoom()[row][col + 1].setText("" + boom.getSurroundingBoom()[row][col + 1]);
                boom.getBoom()[row][col + 1].setTextFill(colorTable[boom.getSurroundingBoom()[row][col + 1] - 1]);
                boom.getBoom()[row][col + 1].setStyle("-fx-background-color:lightblue;-fx-border-color:black");
                boom.getTableIsWin()[row][col + 1] = 1;
            }
            if (boom.getSurroundingBoom()[row][col + 1] == 0 && boom.getTableMode()[row][col + 1] == 1) {
//                boom.getSurroundingBoom()[row][col + 1] = -2;
//                boom.getBoom()[row][col + 1].setStyle("-fx-background-color:null;-fx-border-color:black");
                extendZero(boom, row, col + 1);
            }
            if (boom.getTableMode()[row][col + 1] == 1) {
                boom.getTableMode()[row][col + 1] = 0;
            }
        }
        if (row + 1 <= boom.getRow() && col - 1 >= 0) {
            if (boom.getSurroundingBoom()[row + 1][col - 1] != 0
                    && boom.getSurroundingBoom()[row + 1][col - 1] != -2
                    && boom.getTableMode()[row + 1][col - 1] == 1) {
                boom.getBoom()[row + 1][col - 1].setText("" + boom.getSurroundingBoom()[row + 1][col - 1]);
                boom.getBoom()[row + 1][col - 1].setTextFill(colorTable[boom.getSurroundingBoom()[row + 1][col - 1] - 1]);
                boom.getBoom()[row + 1][col - 1].setStyle("-fx-background-color:lightblue;-fx-border-color:black");
                boom.getTableIsWin()[row + 1][col - 1] = 1;
            }
            if (boom.getSurroundingBoom()[row + 1][col - 1] == 0 && boom.getTableMode()[row + 1][col - 1] == 1) {
//                boom.getSurroundingBoom()[row + 1][col - 1] = -2;
//                boom.getBoom()[row + 1][col - 1].setStyle("-fx-background-color:null;-fx-border-color:black");
                extendZero(boom, row + 1, col - 1);
            }
            if (boom.getTableMode()[row + 1][col - 1] == 1) {
                boom.getTableMode()[row + 1][col - 1] = 0;
            }
        }
        if (row + 1 <= boom.getRow()) {
            if (boom.getSurroundingBoom()[row + 1][col] != 0
                    && boom.getSurroundingBoom()[row + 1][col] != -2
                    && boom.getTableMode()[row + 1][col] == 1) {
                boom.getBoom()[row + 1][col].setText("" + boom.getSurroundingBoom()[row + 1][col]);
                boom.getBoom()[row + 1][col].setStyle("-fx-background-color:lightblue;-fx-border-color:black");
                boom.getBoom()[row + 1][col].setTextFill(colorTable[boom.getSurroundingBoom()[row + 1][col] - 1]);
                boom.getTableIsWin()[row + 1][col] = 1;
            }
            if (boom.getSurroundingBoom()[row + 1][col] == 0 && boom.getTableMode()[row + 1][col] == 1) {
//                boom.getSurroundingBoom()[row + 1][col] = -2;
//                boom.getBoom()[row + 1][col].setStyle("-fx-background-color:null;-fx-border-color:black");
                extendZero(boom, row + 1, col);
            }
            if (boom.getTableMode()[row + 1][col] == 1) {
                boom.getTableMode()[row + 1][col] = 0;
            }
        }
        if (row + 1 <= boom.getRow() && col + 1 <= boom.getCol()) {
            if (boom.getSurroundingBoom()[row + 1][col + 1] != 0
                    && boom.getSurroundingBoom()[row + 1][col + 1] != -2
                    && boom.getTableMode()[row + 1][col + 1] == 1) {
                boom.getBoom()[row + 1][col + 1].setText("" + boom.getSurroundingBoom()[row + 1][col + 1]);
                boom.getBoom()[row + 1][col + 1].setStyle("-fx-background-color:lightblue;-fx-border-color:black");
                boom.getBoom()[row + 1][col + 1].setTextFill(colorTable[boom.getSurroundingBoom()[row + 1][col + 1] - 1]);
                boom.getTableIsWin()[row + 1][col + 1] = 1;
            }
            if (boom.getSurroundingBoom()[row + 1][col + 1] == 0 && boom.getTableMode()[row + 1][col + 1] == 1) {
//                boom.getSurroundingBoom()[row + 1][col + 1] = -2;
//                boom.getBoom()[row + 1][col + 1].setStyle("-fx-background-color:null;-fx-border-color:black");
                extendZero(boom, row + 1, col + 1);
            }
            if (boom.getTableMode()[row + 1][col + 1] == 1) {
                boom.getTableMode()[row + 1][col + 1] = 0;
            }
        }
    }

    private void fillColorTable() {
        colorTable = new Color[8];
        colorTable[0] = Color.BLUE;
        colorTable[1] = Color.CHOCOLATE;
        colorTable[2] = Color.FIREBRICK;
        colorTable[3] = Color.DARKGOLDENROD;
        colorTable[4] = Color.ORANGE;
        colorTable[5] = Color.RED;
        colorTable[6] = Color.HOTPINK;
        colorTable[7] = Color.BLACK;
    }

    public Boom getBoom() {
        return boom;
    }

    public void setBoom(Boom boom) {
        this.boom = boom;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public int getSurroundingBoom() {
        return surroundingBoom;
    }

    public void setSurroundingBoom(int surroundingBoom) {
        this.surroundingBoom = surroundingBoom;
    }

    public Color[] getColorTable() {
        return colorTable;
    }

    public void setColorTable(Color[] colorTable) {
        this.colorTable = colorTable;
    }
}
