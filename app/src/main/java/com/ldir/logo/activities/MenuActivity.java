package com.ldir.logo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ldir.logo.GameApp;
import com.ldir.logo.R;
import com.ldir.logo.game.GameProgress;
import com.ldir.logo.sound.Music;

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
        intent.putExtra("level", GameProgress.restoreCurrentLevel());
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


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("GameMenu", "Start");
        Music.setFile(GameApp.getAppResources().getString(R.string.menu_mus));
        Music.setMusicOn(Music.MENU_MUS,true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("GameMenu", "Stop");
        Music.setMusicOn(Music.MENU_MUS,false);
    }





}
