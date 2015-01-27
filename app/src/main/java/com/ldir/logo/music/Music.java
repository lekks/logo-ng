package com.ldir.logo.music;

import android.content.SharedPreferences;

import com.ldir.logo.activities.GameApp;

import java.io.IOException;

/**
 * Created by Ldir on 30.12.2014.
 */
public class Music {
    private static IBXMPlayer music;

    private static boolean isON = false;

    private static void updade() {
        if(isON && getMusicEnabled()) {
            if (music == null) {
                try {
                    music = new IBXMPlayer(GameApp.getAppContext().getAssets().open("mus/menu/1.xm"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (music != null) {
                try {
                    music.close();
                    music = null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setMusicOn(boolean on) {
        isON = on;
        updade();
    }

    public static void setMusicEnabled(boolean musicEnabled) {
        SharedPreferences.Editor editor =  GameApp.getAppContext().getSharedPreferences(GameApp.SHARED_SETTINGS, GameApp.MODE_PRIVATE).edit();
        editor.putBoolean(GameApp.SHARED_SETTINGS_MUS_ENABLED, musicEnabled);
        editor.commit();
        updade();
    }

    public static boolean getMusicEnabled() {
        SharedPreferences sharedPrefs = GameApp.getAppContext().getSharedPreferences(GameApp.SHARED_SETTINGS, GameApp.MODE_PRIVATE);
        return sharedPrefs.getBoolean(GameApp.SHARED_SETTINGS_MUS_ENABLED, true);
    }

}
