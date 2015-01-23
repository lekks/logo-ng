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


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

}
