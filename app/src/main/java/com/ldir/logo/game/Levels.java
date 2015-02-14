package com.ldir.logo.game;

import android.content.SharedPreferences;

import com.ldir.logo.activities.GameApp;

import java.io.IOException;
import java.io.InputStream;

public class Levels {
    private static LevelsLoader mLevels;
    private static GameProgress mProgress;

    private static LevelsLoader levels()
    {
        if(mLevels == null) {
            String json = getAssetAsString("levels.json");
            mLevels = new LevelsLoader(json);
        }
        return mLevels;
    }

    public static int levelsCount(){
        return levels().length();
    }

    public static GameLevel getLevel(int n)
    {
        return levels().get(n);
    }


    public static void clearProgress() {
        progress().clearProgress();
    }
    public static void setCompleted(int level){
        progress().setCompleted(level);
        saveProgress();
    }
    public static boolean isCompleted(int level){
        return progress().isCompleted(level);
    }
    public static void setOpened(int level){
        progress().setOpened(level);
        saveProgress();
    }

    public static boolean isOpened(int level){
        return progress().isOpened(level);
    }

    public static int restoreCurrentLevel() {
        SharedPreferences sharedPrefs = GameApp.getGamePreferences();
        return sharedPrefs.getInt(GameApp.SHARED_SETTINGS_CURRNT_LEVEL, 0);
    }

    public static void saveCurrentLevel(int n) {
        SharedPreferences.Editor editor =  GameApp.getGamePreferences().edit();
        editor.putInt(GameApp.SHARED_SETTINGS_CURRNT_LEVEL, n);
        editor.commit();
    }

    private static GameProgress progress()
    {
        if (mProgress  == null) {
            mProgress = new GameProgress();
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
            editor.commit();
        }
    }
    private static String getAssetAsString(String name) {
        String str = null;
        try {
            InputStream is = GameApp.getAsset(name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }
        return str;
    }



}


