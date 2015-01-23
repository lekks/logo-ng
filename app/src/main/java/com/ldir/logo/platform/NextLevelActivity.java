package com.ldir.logo.platform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.ldir.logo.R;
import com.ldir.logo.game.Game;

/**
 * Created by Ldir on 25.09.13.
 */

public class NextLevelActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Be sure to call the super class.
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.activity_next_level);
        setTitle("Level Complete!");

        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra("level");
//        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,android.R.drawable.ic_dialog_alert);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

}