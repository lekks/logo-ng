package com.ldir.logo.platform;

import android.app.Activity;
import android.content.Intent;
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

    private void close() {
        finish();
    }
    // TODO обработать кнопки
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        close();
        return true;
    }

}