package com.ldir.logo.activities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class GameApp  extends Application{

    private static final String SHARED_SETTINGS = "com.ldir.logo";
    public static final String SHARED_SETTINGS_MUS_ENABLED = "MUS_ENABLED";
    public static final String SHARED_SETTINGS_SND_ENABLED = "SND_ENABLED";
    public static final String SHARED_SETTINGS_LEVELS_PROGRESS = "LEVELS_PROGRESS";
    public static final String SHARED_SETTINGS_CURRENT_LEVEL = "CURRENT_LEVEL";

    private static Context context;
    private static Resources resources;
//    private static Application app;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("GameApp", "onCreate");
        GameApp.context = getApplicationContext();
        GameApp.resources = getResources();
    }

    public static Context getAppContext() {
        return GameApp.context;
    }

    public static Resources getAppResources() {
        return GameApp.resources;
    }
    public static InputStream getAsset(String path) throws IOException {
        return GameApp.context.getAssets().open(path);
    }
    public static SharedPreferences getGamePreferences(){
        return GameApp.context.getSharedPreferences(SHARED_SETTINGS, MODE_PRIVATE);

    }

}
