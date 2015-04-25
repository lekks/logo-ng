package com.ldir.logo.game.levels;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.ldir.logo.activities.GameApp;
import com.ldir.logo.game.GameMap;

/**
 * Created by Ldir on 20.03.2015.
 */
public class LevelGeneratorTest extends ApplicationTestCase<Application> {
    LevelMaker maker;
    String json;

    public LevelGeneratorTest() {
        super(Application.class);
    }

    public void testGenerator() {

        json = GameApp.getAssetAsString("patterns.json");
        maker = new LevelMaker(json);
        GameMap map = new GameMap();

        Log.d("Patterns",maker.patterns[0].map.dump());

        for(int i = 0; i<5; ++i){
            maker.generateLevel(map,i, maker.patterns[1].map, 80);
            Log.d("Level",map.dump());
        }
        maker.generateLevel(map, 0, maker.fullPatt.map, 100);
        Log.d("Level full",map.dump());

        long time = System.currentTimeMillis();
        int count = 0;
        while(System.currentTimeMillis()<time+1000) {
            maker.generateLevel(map, 0, maker.fullPatt.map, 100);
            count++;
        }
        Log.d("Level bench",Integer.toString(count));

    }

}
