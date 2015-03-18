package com.ldir.logo.game;

import android.content.SharedPreferences;

import com.ldir.logo.activities.GameApp;

import java.io.IOException;
import java.io.InputStream;

public class Levels {
    private static LevelsLoader mLevels;
    private static LevelMaker mMaker = new LevelMaker();
    private static GameProgress mProgress;

    private static final String LevelsFile = "levels_test2.json";

    private static LevelsLoader levels()
    {
        if(mLevels == null) {
            String json = getAssetAsString(LevelsFile);
            mLevels = new LevelsLoader(json);
        }
        return mLevels;
    }

    public static int levelsCount(){
        return levels().length();
    }

    public static GameLevel getLevel(int n)
    {
//        return levels().get(n);
        return mMaker.getLevel(n);
    }


    public static void clearProgress() {
        progress().clearProgress();
    } // For tests

    public static void setCompleted(int level){
        progress().setComplete(level);
        saveProgress();
    }
    public static boolean isCompleted(int level){
        return progress().isComplete(level);
    }

    public static boolean isAllCompleted(){
        return progress().isAllComplete();
    }

    public static boolean isOpened(int level){
        return progress().isOpened(level);
    }

    public static int nextOpened(int current) {
        if(Levels.isAllCompleted())
            current+=1;
        else
            current = progress().nextOpened(current);

        if (current >= levelsCount())
            current = 0;
        return current;
    }
    public static int restoreCurrentLevel() {
        SharedPreferences sharedPrefs = GameApp.getGamePreferences();
        return sharedPrefs.getInt(GameApp.SHARED_SETTINGS_CURRENT_LEVEL, 0);
    }

    public static void saveCurrentLevel(int n) {
        SharedPreferences.Editor editor =  GameApp.getGamePreferences().edit();
        editor.putInt(GameApp.SHARED_SETTINGS_CURRENT_LEVEL, n);
        editor.apply();
    }

    private static GameProgress progress()
    {
        if (mProgress  == null) {
            mProgress = new GameProgress(levelsCount());
            SharedPreferences sharedPrefs = GameApp.getGamePreferences();
            String progressBundle =  sharedPrefs.getString(GameApp.SHARED_SETTINGS_LEVELS_PROGRESS, null);
            if (progressBundle != null)
                mProgress.restoreState(progressBundle);
        }
        return mProgress;
    }

    private static void saveProgress()
    {
        if (mProgress  != null) {
            SharedPreferences.Editor editor =  GameApp.getGamePreferences().edit();
            editor.putString(GameApp.SHARED_SETTINGS_LEVELS_PROGRESS, mProgress.bundleState());
            editor.apply();
        }
    }
    private static String getAssetAsString(String name) {
        try {
            InputStream is = GameApp.getAsset(name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }
    }



}


