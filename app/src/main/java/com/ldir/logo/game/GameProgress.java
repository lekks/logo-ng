package com.ldir.logo.game;

import android.content.SharedPreferences;

import com.ldir.logo.GameApp;

/**
 * Created by Ldir on 30.03.2015.
 */
public class GameProgress {
    private static Progress mProgress;
    private static Boolean allOpened;

    public static void clearProgress() {
        progress().clearProgress();
    } // For tests

    public final static boolean isLevelsDebug() {
        if(allOpened == null) {
            allOpened = GameApp.getMetaBundle().getBoolean("all_opened",false);
        }
        return allOpened;
    }

    public static void setCompleted(int level){
        if( !isLevelsDebug()) {
            progress().setComplete(level);
            saveProgress();
        }
    }
    public static boolean isCompleted(int level){
        return isLevelsDebug() || progress().isComplete(level);
    }

    public static boolean isAllCompleted(){
        return isLevelsDebug() || progress().isAllComplete();
    }

    public static boolean isOpened(int level){
        return isLevelsDebug() ||  progress().isOpened(level);
    }

    public static int nextOpened(int current) {
        if(isAllCompleted())
            current+=1;
        else
            current = progress().nextOpened(current);

        if (current >= Levels.levelsCount())
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

    private static Progress progress()
    {
        if (mProgress  == null) {
            mProgress = new Progress(Levels.levelsCount());
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

}
