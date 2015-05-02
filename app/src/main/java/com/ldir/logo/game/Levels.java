package com.ldir.logo.game;

import com.ldir.logo.GameApp;
import com.ldir.logo.game.levels.LevelMaker;
import com.ldir.logo.game.levels.LevelsLoader;

public class Levels {
    private static LevelsLoader mLevels;
    private static LevelMaker mMaker;

    private static final String LevelsFile = "levels.json";
    private static final String PatternsFile = "patterns.json";

    private static LevelsLoader levels()
    {
        if(mLevels == null) {
            String json = GameApp.getAssetAsString(LevelsFile);
            mLevels = new LevelsLoader(json);
        }
        return mLevels;
    }

    private static LevelMaker generator()
    {
        if(mMaker == null) {
            String json = GameApp.getAssetAsString(PatternsFile);
            mMaker = new LevelMaker(json);
        }
        return mMaker;
    }

    public static int levelsCount(){
//        return 10000;
        return levels().length();
    }

    public static GameLevel getLevel(int n)
    {
        GameLevel level = levels().get(n);
        if(GameProgress.isAllOpened())
            level.time = 1000;
        return level;
//        return generator().getLevel(n);
    }





}


