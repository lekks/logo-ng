package com.ldir.logo.sound;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.ldir.logo.R;
import com.ldir.logo.activities.GameApp;

import java.io.IOException;

/**
 * Created by Ldir on 30.12.2014.
 */
public class Music {
    private static IBXMPlayer music;
    private static String currentFile;
    private static int musicFlags=0;

    public static final int GAME_MUS=0;
    public static final int MENU_MUS=1;
    public static final int MENU_OPT_MUS=2;
    public static final int MENU_SEL_MUS=3;

    public static void setFile(String name) {
        if(currentFile!= null && !currentFile.equals(name))
            close();
        currentFile = name;
        updade();
    }


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
        if (music == null) {
            try {
                music = new IBXMPlayer(GameApp.getAsset(name));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }


    private static void updade() {
        if(getMusicEnabled() && currentFile != null && (musicFlags != 0) ) {
            open(currentFile);
        } else {
            close();
        }
    }

    public static void setMusicOn(int menu, boolean on) {
        if(on)
            musicFlags |= (1<<menu);
        else
            musicFlags &= ~(1<<menu);
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
