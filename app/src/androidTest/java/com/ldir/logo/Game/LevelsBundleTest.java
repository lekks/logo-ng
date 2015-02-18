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

    public void testBundleComplete() {
        GameProgress progress = new GameProgress();
        progress.clearProgress();
        progress.setCompleted(0);
        progress.setCompleted(37);
        progress.setCompleted(64);
        String bundle = progress.bundleState();
        Log.i("Test", "BitSet is " + bundle);
        progress.clearProgress();
        Log.i("Test", "BitSet clear is " + progress.bundleState());
        progress.setCompleted(17);
        progress.restoreState(bundle);
        String bundle2 = progress.bundleState();
        Log.i("Test", "BitSet restored is " + progress.bundleState());
        assertEquals(bundle,bundle2);
        assertTrue(progress.isCompleted(37));
        assertTrue(progress.isCompleted(64));
        assertFalse(progress.isCompleted(14));
   }

    private void assertOpened(GameProgress progress, int lastOpened)
    {
        for(int i=0;i<100;++i) {
            if(i<lastOpened) {
                assertTrue("l="+i,progress.isOpened(i));
            } else
                assertFalse("l="+i,progress.isOpened(i));
        }

    }

    public void testGroupOpening() {
        GameProgress progress = new GameProgress();
        progress.clearProgress();
        int i=0;
        assertOpened(progress,GameProgress.GROUP_SIZE);
        while(i<GameProgress.GROUP_SIZE-1)
            progress.setCompleted(i++);
        assertOpened(progress,GameProgress.GROUP_SIZE);
        progress.setCompleted(i++);
        assertOpened(progress,GameProgress.GROUP_SIZE*2);
        while(i<GameProgress.GROUP_SIZE*2-1)
            progress.setCompleted(i++);
        assertOpened(progress,GameProgress.GROUP_SIZE*2);
        progress.setCompleted(i++);
        assertOpened(progress,GameProgress.GROUP_SIZE*3);
//        progress.tryOpenGroup()

        }
    }