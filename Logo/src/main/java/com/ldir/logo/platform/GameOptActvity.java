package com.ldir.logo.platform;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ToggleButton;

import com.ldir.logo.R;
import com.ldir.logo.game.Game;

public class GameOptActvity extends Activity {
        public final static String CMD="cmd";
        public final static int CMD_RESET=1;
        public final static int CMD_RESTART=2;
        public final static int CMD_EXIT=3;

    private ToggleButton mMusicTogglel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_opt_actvity);
        SharedPreferences sharedPrefs = getSharedPreferences(GameApp.SHARED_SETTINGS, MODE_PRIVATE);
        mMusicTogglel = (ToggleButton) findViewById(R.id.musToggleButton);
        mMusicTogglel.setChecked(sharedPrefs.getBoolean(GameApp.SHARED_SETTINGS_MUS_ENABLED, true));

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


    public void onRestartButton(View v){
        retCmd(CMD_RESTART);
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
            Log.i("Options", "Music on");
            SharedPreferences.Editor editor = getSharedPreferences(GameApp.SHARED_SETTINGS, MODE_PRIVATE).edit();
            editor.putBoolean(GameApp.SHARED_SETTINGS_MUS_ENABLED, true);
            editor.commit();
        } else {
            Log.i("Options", "Music off");
            SharedPreferences.Editor editor = getSharedPreferences(GameApp.SHARED_SETTINGS, MODE_PRIVATE).edit();
            editor.putBoolean(GameApp.SHARED_SETTINGS_MUS_ENABLED, false);
            editor.commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Options", "Start");
        Game.enterOptScreen();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Options", "Stop");
        Game.exitOptScreen();
    }

}
