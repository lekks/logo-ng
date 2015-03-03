package com.ldir.logo.sound;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.ldir.logo.R;
import com.ldir.logo.activities.GameApp;

import java.io.IOException;

/**
 * Created by Ldir on 30.12.2014.
 */
public class Music {
    private static IBXMPlayer music;

    private static boolean isON = false;

    private static void close() {
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

    private static void open(String name) {
        try {
            music = new IBXMPlayer(GameApp.getAsset(name));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    private static void updade() {
        if(isON && getMusicEnabled()) {
            if (music == null) {
                String[] songs = GameApp.getAppResources().getStringArray(R.array.game_mus);
                open(songs[0]);
            }
        } else {
            close();
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
