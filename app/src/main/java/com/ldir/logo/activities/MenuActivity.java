package com.ldir.logo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ldir.logo.R;
import com.ldir.logo.game.Levels;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);
    }

    public void onStartGame(View v){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("from", Levels.restoreCurrentLevel());
        startActivity(intent);

    }

    public void onSelectLevels(View v){
        Intent intent = new Intent(this, SelectLevelActivity.class);
        startActivity(intent);
    }

    public void onOptions(View v){
        Intent intent = new Intent(this, MenuOptActvity.class);
        startActivity(intent);
    }
}
