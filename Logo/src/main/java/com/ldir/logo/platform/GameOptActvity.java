package com.ldir.logo.platform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.ldir.logo.R;
import com.ldir.logo.game.Game;

public class GameOptActvity extends Activity {

        public final static String CMD="cmd";
        public final static int CMD_RESET=1;
        public final static int CMD_RESTART=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_opt_actvity);
    }

    //FIXME надо тормознуть музыку при нажатии HOME

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        close();
        return true;
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
        Log.i("Options","Stop");
        Game.exitOptScreen();
    }

        @Override
    protected void onResume() {
        super.onResume();
        Log.i("Options", "Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Options", "Pause");
    }
}
