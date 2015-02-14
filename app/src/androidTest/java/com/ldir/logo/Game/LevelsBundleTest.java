package com.ldir.logo.game;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;


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
        GameProgress progress = new GameProgress();
        progress.clearProgress();
        progress.setCompleted(0);
        progress.setCompleted(37);
        progress.setOpened(64);
        progress.setCompleted(64);
        progress.setOpened(13);
        String bundle = progress.bundleState();
        Log.i("Test", "BitSet is " + bundle);
        progress.clearProgress();
        Log.i("Test", "BitSet clear is " + progress.bundleState());
        progress.setOpened(14);
        progress.setCompleted(17);
        progress.restoreState(bundle);
        String bundle2 = progress.bundleState();
        Log.i("Test", "BitSet restored is " + progress.bundleState());
        assertEquals(bundle,bundle2);
        assertTrue(progress.isCompleted(37));
        assertTrue(progress.isCompleted(64));
        assertTrue(progress.isOpened(13));
        assertFalse(progress.isCompleted(14));
        assertFalse(progress.isOpened(16));
   }

}