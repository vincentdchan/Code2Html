package Eggs;

import javafx.scene.control.Button;

public class Boom extends Game {

    private int row;
    private int col;
    private int boom_count;
    private Button[][] boom;
    private int[][] surroundingBoom;
    private int[][] tableIsWin;
    private int[][] tableMode;
//    private TextField textBoomCount;
//    private int count;
    public Boom(String level) throws Exception{
        generateBoomAndArea(level);
        fillNumber();
//        textBoomCount = super.boom_count;
//        count = super.count;
    }//for self


    private void generateBoomAndArea(String level) {
        switch (level) {
            case "Easy":
                boom_count = 10;
                row = 8;
                col = 8;
                break;
            case "Medium":
                boom_count = 40;
                row = 15;
                col = 15;
                break;
            case "Difficult":
                boom_count = 99;
                row = 15;
                col = 29;
                break;
            default:
                return;
        }
        boom = new Button[row + 1][col + 1];
        surroundingBoom = new int[row + 1][col + 1];
        tableIsWin = new int[row + 1][col + 1];
        tableMode = new int[row + 1][col + 1];
        for (int i = 0; i < boom_count; i++) {
            int a = ((int) (Math.random() * (row + 1)));
            int b = ((int) (Math.random() * (col + 1)));
            if (surroundingBoom[a][b] != -1) {
                surroundingBoom[a][b] = -1;
                tableIsWin[a][b] = 1;
            } else {
                i--;
            }
        }
    }

    private void fillNumber() {
        for (int i = 0; i < row + 1; i++) {
            for (int j = 0; j < col + 1; j++) {
                tableMode[i][j] = 1;
                if (surroundingBoom[i][j] != -1) {
                    surroundingBoom[i][j] = statisticSurroundingBoom(i, j);
                }
//                BoomButton boomButton = new BoomButton();
//                boom[i][j] = boomButton;
            }
        }

    }

    private int statisticSurroundingBoom(int row, int col) {
        int boom = 0;
        if (row - 1 >= 0 && col - 1 >= 0 && surroundingBoom[row - 1][col - 1] == -1) {
            boom++;
        }
        if (row - 1 >= 0 && surroundingBoom[row - 1][col] == -1) {
            boom++;
        }
        if (row - 1 >= 0 && col + 1 <= this.col && surroundingBoom[row - 1][col + 1] == -1) {
            boom++;
        }
        if (col - 1 >= 0 && surroundingBoom[row][col - 1] == -1) {
            boom++;
        }
        if (col + 1 <= this.col && surroundingBoom[row][col + 1] == -1) {
            boom++;
        }
        if (row + 1 <= this.row && col - 1 >= 0 && surroundingBoom[row + 1][col - 1] == -1) {
            boom++;
        }
        if (row + 1 <= this.row && surroundingBoom[row + 1][col] == -1) {
            boom++;
        }
        if (row + 1 <= this.row && col + 1 <= this.col && surroundingBoom[row + 1][col + 1] == -1) {
            boom++;
        }
        return boom;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getBoom_count() {
        return boom_count;
    }

    public void setBoom_count(int boom_count) {
        this.boom_count = boom_count;
    }

    public Button[][] getBoom() {
        return boom;
    }

    public void setBoom(Button[][] boom) {
        this.boom = boom;
    }

    public int[][] getSurroundingBoom() {
        return surroundingBoom;
    }

    public void setSurroundingBoom(int[][] surroundingBoom) {
        this.surroundingBoom = surroundingBoom;
    }

    public int[][] getTableIsWin() {
        return tableIsWin;
    }

    public void setTableIsWin(int[][] tableIsWin) {
        this.tableIsWin = tableIsWin;
    }

    public int[][] getTableMode() {
        return tableMode;
    }

    public void setTableMode(int[][] tableMode) {
        this.tableMode = tableMode;
    }

//    public TextField getTextBoomCount() {
//        return textBoomCount;
//    }
//
//    public void setTextBoomCount(TextField textBoomCount) {
//        this.textBoomCount = textBoomCount;
//    }
//
//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }
}
