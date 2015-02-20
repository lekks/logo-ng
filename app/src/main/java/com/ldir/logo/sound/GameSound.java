package com.ldir.logo.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.ldir.logo.R;

/**
 * Created by Ldir on 20.02.2015.
 */

public class GameSound {
    final SoundPool soundPool;
    final int sound;

    public GameSound(Context context) {
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        sound = soundPool.load(context, R.raw.alert5, 1);
    }

    public void play(){
        soundPool.play(sound, 1.0f, 1.0f, 0, 0, 1.0f);
    }
}
