package com.ldir.logo;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.ldir.logo.game.GameProgress;
import com.ldir.logo.game.Levels;


/**
 * Created by Ldir on 24.01.2015.
 */

public class LevelsBundleTest extends ApplicationTestCase<Application> {
    public LevelsBundleTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception{
        super.tearDown();
    }

    public void testSprite() {
        Log.i("Test", "Levels test 1");
        GameProgress progress = new GameProgress();
        progress.clearProgress();
        progress.setCompleted(0);
        progress.setCompleted(37);
        progress.setOpened(64);
        progress.setCompleted(64);
        progress.setOpened(13);
        String bundle = progress.bundleState();
        Log.i("Test", "BitSet is " + bundle);
//        assertEquals("{\"completed\":[0,37,64]}",bundle);
//        for (int i )
   }

}