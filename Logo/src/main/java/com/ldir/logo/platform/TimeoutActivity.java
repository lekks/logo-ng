package com.ldir.logo.platform;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.ldir.logo.R;
import com.ldir.logo.game.Game;


public class TimeoutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout);
        setTitle("You lost!");
    }

    private void close() {
        finish();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        close();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Game.exitLostScreen();
    }
}
