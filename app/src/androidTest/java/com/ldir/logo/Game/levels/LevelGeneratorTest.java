package com.ldir.logo.game.levels;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.ldir.logo.activities.GameApp;
import com.ldir.logo.game.levels.LevelMaker;

/**
 * Created by Ldir on 20.03.2015.
 */
public class LevelGeneratorTest extends ApplicationTestCase<Application> {


    public LevelGeneratorTest() {
        super(Application.class);
    }


    public void testGenerator() {
        String json = GameApp.getAssetAsString("patterns.json");

//        Log.d("Patterns",json);

        LevelMaker maker = new LevelMaker(json);
        Log.d("Patterns",maker.patterns[0].dump());

//        for(int i=0;i<maker.patterns.length;++i){
//
//        }
    }
}
