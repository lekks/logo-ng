package com.ldir.logo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.ldir.logo.R;

/**
 * Created by Ldir on 25.09.13.
 */

public class GameWinActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.activity_game_win);
        setTitle("You win the game!");

//        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,android.R.drawable.ic_dialog_alert);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

}