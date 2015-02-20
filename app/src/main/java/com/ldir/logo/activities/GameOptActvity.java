package com.ldir.logo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ToggleButton;

import com.ldir.logo.R;
import com.ldir.logo.sound.Music;

public class GameOptActvity extends Activity {
        public final static String CMD="cmd";
        public final static int CMD_RESET=1;
        public final static int CMD_EXIT=2;

    private ToggleButton mMusicTogglel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_opt_actvity);
        mMusicTogglel = (ToggleButton) findViewById(R.id.musToggleButton);
        mMusicTogglel.setChecked(Music.getMusicEnabled());

    }

    private void retCmd(int cmd) {
        Intent output = new Intent();
        output.putExtra(CMD,cmd);
        setResult(RESULT_OK, output);
        finish();
    }

    private void close() {
        finish();
    }


    public void onResetButton(View v){
        retCmd(CMD_RESET);
    }
    public void onExitButton(View v) {
        retCmd(CMD_EXIT);
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

}
