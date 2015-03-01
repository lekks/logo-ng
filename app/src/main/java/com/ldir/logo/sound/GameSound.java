package com.ldir.logo.sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;

import com.ldir.logo.R;
import com.ldir.logo.activities.GameApp;

/**
 * Created by Ldir on 20.02.2015.
 */

public class GameSound {
    private static SoundPool mSoundPool;
    private static int mSound;

    public static void load() {
        if(mSoundPool == null) {
            mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            mSound = mSoundPool.load(GameApp.getAppContext(), R.raw.laser_s, 1);
        }
    }

    public static void release() {
        if(mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
    }

    public static void play(){
        if (getSoundEnabled() && mSoundPool != null) {
            mSoundPool.play(mSound, 0.3f, 0.3f, 0, 0, 1.0f);
        }
    }

    public static void setSoundEnabled(boolean musicEnabled) {
        SharedPreferences.Editor editor =  GameApp.getGamePreferences().edit();
        editor.putBoolean(GameApp.SHARED_SETTINGS_SND_ENABLED, musicEnabled);
        editor.commit();
        if (musicEnabled == false)
            release();
        else
            load();

    }

    public static boolean getSoundEnabled() {
        SharedPreferences sharedPrefs = GameApp.getGamePreferences();
        return sharedPrefs.getBoolean(GameApp.SHARED_SETTINGS_SND_ENABLED, true);
    }

}
