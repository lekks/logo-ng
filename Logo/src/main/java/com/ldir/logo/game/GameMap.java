package com.ldir.logo.game;

/**
 * Created by Ldir on 18.09.13.
 */
public class GameMap {
    private byte map[][];

    // TODO Переделать в методы
    public static int COLS =7;
    public static int ROWS =7;

    public static class Pos {
        public int row;
        public int col;
        public void set(int i,int j) {
            row = i;
            col = j;
        }
    }


    public GameMap() {
        this.map = new byte[ROWS][COLS];
    }

    GameMap(byte map[][]) {
        this.map = map;
    }

    GameMap(int rows, int cols) {
        this.map = new byte[rows][cols];
    }

    public void assign(GameMap other) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                map[i][j] = other.map[i][j]; // убрать внутренний цикл? System.arrayCopy
            }
        }
    }

    public GameMap clone() throws CloneNotSupportedException {
        super.clone();
        GameMap copy = new GameMap();
        copy.assign(this);
        return copy;
    }

    public byte get(int x,int y) {
        return map[x][y];
    }

    public void set(int x,int y, byte v ) {
        map[x][y] = v;
    }

    private void next(int i, int j) {
        if(++map[i][j]>4)map[i][j]=1;
    }

    public void reset() {
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++)
                map[i][j]=0;
        }
    }

    public boolean isEqual(GameMap other) {
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                if(map[i][j] != other.map[i][j])
                    return false;
            }
        }
        return true;
    }


    public void gameMove(int i,int j) {
        next(i,j);
        if (i+1<ROWS) next(i+1,j);
        if (j+1<COLS) next(i,j+1);
        if (i>0) next(i-1,j);
        if (j>0) next(i,j-1);
    }


}
