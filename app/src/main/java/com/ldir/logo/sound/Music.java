package com.ldir.logo.sound;

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
                    music = new IBXMPlayer(GameApp.getAsset("mus/menu/1.xm"));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }
        } else {
            if (music != null) {
                try {
                    music.close();
                    music = null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }
        }
    }

    public static void setMusicOn(boolean on) {
        isON = on;
        updade();
    }

    public static void setMusicEnabled(boolean musicEnabled) {
        SharedPreferences.Editor editor =  GameApp.getGamePreferences().edit();
        editor.putBoolean(GameApp.SHARED_SETTINGS_MUS_ENABLED, musicEnabled);
        editor.commit();
        updade();
    }

    public static boolean getMusicEnabled() {
        SharedPreferences sharedPrefs = GameApp.getGamePreferences();
        return sharedPrefs.getBoolean(GameApp.SHARED_SETTINGS_MUS_ENABLED, true);
    }

}
