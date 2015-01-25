package com.ldir.logo.platform;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.ldir.logo.game.Game;

public class GameApp  extends Application{

    public static final String SHARED_SETTINGS = "com.ldir.logo";
    public static final String SHARED_SETTINGS_MUS_ENABLED = "MUS_ENABLED";

    private static Context context;
    private static Resources resources;
//    private static Application app;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("GameApp", "onCreate");
        GameApp.context = getApplicationContext();
        GameApp.resources = getResources();

        Game.restartGame();
    }

    public static Context getAppContext() {
        return GameApp.context;
    }
    public static Resources getAppResources() {
        return GameApp.resources;
    }

}
