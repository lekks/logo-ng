package com.ldir.logo.platform;

import android.app.Application;
import android.content.Context;

import com.ldir.logo.game.Game;
import com.ldir.logo.graphics.Sprites;
import com.ldir.logo.graphics.Underlayer;
import com.ldir.logo.music.Music;

public class GameApp  extends Application{

    public static final String SHARED_SETTINGS = "com.ldir.logo";
    public static final String SHARED_SETTINGS_MUS_ENABLED = "MUS_ENABLED";

    private static Context context;
    
    public void onCreate(){
        super.onCreate();
        GameApp.context = getApplicationContext();
        Sprites.load(getResources());
        Underlayer.load(getResources());

        Game.restartGame();
    }

    public static Context getAppContext() {
        return GameApp.context;
    }
}
