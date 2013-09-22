package com.ldir.logo.platform;

import android.app.Application;
import android.content.Context;

import com.ldir.logo.game.Game;
import com.ldir.logo.graphics.Sprites;

public class GameApp  extends Application{
    private static Context context;
    
    public void onCreate(){
        super.onCreate();
        GameApp.context = getApplicationContext();
        Sprites.load(getResources());
        Game.startGame();
    }

    public static Context getAppContext() {
        return GameApp.context;
    }
}
