package com.ldir.logo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.ldir.logo.R;

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
//        setTitle("Level Complete!");

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

}