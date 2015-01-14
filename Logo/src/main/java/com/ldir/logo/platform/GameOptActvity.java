package com.ldir.logo.platform;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.ldir.logo.R;
import com.ldir.logo.game.Game;

public class GameOptActvity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_opt_actvity);
    }

    //FIXME надо тормознуть музыку при нажатии HOME

    private void close() {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        close();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Options","Start");
        Game.enterOptScreen();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Options","Stop");
        Game.exitOptScreen();
    }

        @Override
    protected void onResume() {
        super.onResume();
        Log.i("Options","Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Options","Pause");
    }
}
