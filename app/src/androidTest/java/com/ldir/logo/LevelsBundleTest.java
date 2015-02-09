package com.ldir.logo;

import android.app.Application;
import android.graphics.Bitmap;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.ldir.logo.fieldviews.render.Sprite;
import com.ldir.logo.game.GameProgress;

import java.util.BitSet;


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


    //http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
    //http://stackoverflow.com/questions/2473597/bitset-to-and-from-integer-long/2473719#2473719

    public void testSprite() {
        Log.i("Test", "Levels test 1");
        GameProgress progr=new GameProgress(100);
        progr.setCompleted(4);
        progr.setCompleted(64);
        Log.i("Test", "BitSet is " + progr.getBundle());
   }

}