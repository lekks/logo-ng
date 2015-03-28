package com.ldir.logo.game;

/**
 * Created by Ldir on 18.09.13.
 */
public class GameMap {
    private final byte map[][];

    public static final int COLS =7;
    public static final int ROWS =7;

    public static class Pos {
        public int row;
        public int col;
        public void set(int i,int j) {
            row = i;
            col = j;
        }
    }


    static public float calcCellSize(int fieldWidth, int fieldHeight) {
        return Math.min((float)fieldWidth/(float)COLS, (float)fieldHeight/(float)ROWS);
    }

    static public void fitWidhHeight(int[] size)
    {
        int t = size[0] * GameMap.ROWS / GameMap.COLS;
        if (size[1] > t) size[1] = t;
        t = size[1] * GameMap.COLS / GameMap.ROWS;
        if (size[0] > t) size[0] = t;
    }


    public GameMap() {
        this.map = new byte[ROWS][COLS];
    }

//    GameMap(int rows, int cols) {
//        this.map = new byte[rows][cols];
//    }

    public void assign(GameMap other) {
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(other.map[i], 0, map[i], 0, COLS);
        }
    }

    @Override
    public GameMap clone() throws CloneNotSupportedException {
        super.clone();
        GameMap copy = new GameMap();
        copy.assign(this);
        return copy;
    }

    public byte get(int x,int y) {
        return map[x][y];
    }

    public void set(int x,int y, int v ) {
        map[x][y] = (byte)v;
    }

    public void set(int x,int y, byte v ) {
        map[x][y] = (byte)v;
    }


    public void resetField() {
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++)
                map[i][j]=0;
        }
    }

    public boolean isEqual(GameMap other) { //Typed compare
        if (this == other) return true;
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                if(map[i][j] != other.map[i][j])
                    return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                result = 31 * result + map[i][j];
            }
        }
        return result;
    }

    @Override public boolean equals(Object other) {
        if (!(other instanceof GameMap)) return false;
        return isEqual((GameMap) other);
    }

    private void nextPresssed(int i, int j) {
        if(++map[i][j]>4)map[i][j]=1;
    }

    private void nextSide(int i, int j) {
        if(map[i][j]>0)
            nextPresssed(i, j);
    }

    public boolean gameMove(int i,int j) {
        if(map[i][j] == 0) {
            nextPresssed(i, j);
            if (i+1<ROWS) nextSide(i + 1, j);
            if (j+1<COLS) nextSide(i, j + 1);
            if (i>0) nextSide(i - 1, j);
            if (j>0) nextSide(i, j - 1);
            return true;
        } else
            return false;
    }

    public String dump(){
        StringBuilder str = new StringBuilder(64);
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                str.append(map[i][j]);
            }
            str.append("\n");
        }
        return str.toString();
    }

}
