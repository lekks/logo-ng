package com.ldir.logo;

import android.app.Application;
import android.content.Context;

public class GameApp  extends Application{
    private static Context context;
    
    public void onCreate(){
        super.onCreate();
        GameApp.context = getApplicationContext();
        Sprites.load(getResources());
    }

    public static Context getAppContext() {
        return GameApp.context;
    }
}
