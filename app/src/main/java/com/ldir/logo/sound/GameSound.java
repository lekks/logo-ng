package com.ldir.logo.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.ldir.logo.R;

/**
 * Created by Ldir on 20.02.2015.
 */

public class GameSound {
    final SoundPool mSoundPool;
    final int mSound;

    public GameSound(Context context) {
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSound = mSoundPool.load(context, R.raw.laser_s, 1);
    }

    public void play(){
//        mSoundPool.play(mSound, 0.3f, 0.3f, 0, 0, 1.0f);
    }
}
