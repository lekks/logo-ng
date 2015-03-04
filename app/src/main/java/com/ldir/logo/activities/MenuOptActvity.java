package com.ldir.logo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ToggleButton;

import com.ldir.logo.R;
import com.ldir.logo.sound.GameSound;
import com.ldir.logo.sound.Music;

public class MenuOptActvity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_opt_actvity);
        ToggleButton toggle = (ToggleButton) findViewById(R.id.musToggleButton);
        toggle.setChecked(Music.getMusicEnabled());
        toggle = (ToggleButton) findViewById(R.id.sndToggleButton);
        toggle.setChecked(GameSound.getSoundEnabled());
    }

    private void close() {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        close();
        return true;
    }

    public void onToggleMusic(View v){
        ToggleButton toggle = (ToggleButton)v;
        if (toggle.isChecked()) {
            Music.setMusicEnabled(true);
        } else {
            Music.setMusicEnabled(false);
        }
    }

    public void onToggleSound(View v){
        ToggleButton toggle = (ToggleButton)v;
        if (toggle.isChecked()) {
            GameSound.setSoundEnabled(true);
        } else {
            GameSound.setSoundEnabled(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Music.setMusicOn(Music.MENU_OPT_MUS,true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Music.setMusicOn(Music.MENU_OPT_MUS,false);
    }


}
