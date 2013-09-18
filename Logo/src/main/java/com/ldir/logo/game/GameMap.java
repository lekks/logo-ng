package com.ldir.logo.game;

/**
 * Created by Ldir on 18.09.13.
 */
public class GameMap {
    private byte map[][];
    GameMap(byte map[][]) {
        this.map = map;
    }
    GameMap(int rows, int cols) {
        this.map = new byte[rows][cols];
    }
    public byte get(int x,int y) {
        return map[x][y];
    }
    public void set(int x,int y, byte v ) {
        map[x][y] = v;
    }
}
