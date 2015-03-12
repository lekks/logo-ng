package com.ldir.logo.game;

/**
 * Created by Ldir on 07.03.2015.
 */
public class LevelMaker {
    static GameLevel makeLevel(){
        GameLevel level = new GameLevel();
        for(int i=0;i<GameMap.ROWS;i++){
            for(int j=0;j<GameMap.COLS;j++)
                level.map.gameMove(i,j);
        }
        level.time=120;
        return level;
    }




}
