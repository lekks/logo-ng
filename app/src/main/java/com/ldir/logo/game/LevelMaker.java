package com.ldir.logo.game;

import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.Random;

/**
 * Created by Ldir on 07.03.2015.
 */
class LevelMaker {
    Random rnd = new Random();
    SparseArray<GameLevel> mCache = new SparseArray<>();

    private GameLevel makeLevel(int id) {
        rnd.setSeed(id);
        GameLevel level = new GameLevel();
        int chips = rnd.nextInt(30) + 20;
        for (int i = 0; i < chips; ++i) {
            int row = rnd.nextInt(GameMap.ROWS);
            int col = rnd.nextInt(GameMap.COLS);
            level.map.gameMove(row, col);
        }
        level.time = 120;
        return level;
    }


    GameLevel getLevel(int id){
        return makeLevel(id);
    }
//    GameLevel getLevel(int id){
//        GameLevel level = mCache.get(id);
//        if(level == null) {
//            level = makeLevel(id);
//            mCache.append(id,level);
//            return level;
//        } else {
//            return level;
//        }
//    }





    }
