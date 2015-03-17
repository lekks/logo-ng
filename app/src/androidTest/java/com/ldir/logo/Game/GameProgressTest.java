package com.ldir.logo.game;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;


/**
 * Created by Ldir on 15.03.2015.
 */
public class GameProgressTest extends ApplicationTestCase<Application> {


    public GameProgressTest() {
        super(Application.class);
    }


    private void assertOpened(GameProgress progress, int lastOpened)
    {
        for(int i=0;i<1000;++i) {
            if(i<lastOpened) {
                assertTrue("l="+i,progress.isOpened(i));
            } else
                assertFalse("l="+i,progress.isOpened(i));
        }

    }

    private void recreate(GameProgress progress, String stage) {
        String bundle = progress.bundleState();
//        Log.i("bundle ("+stage,progress.bundleState());
        progress.clearProgress();
        progress.restoreState(bundle);
//        Log.i("bundle )"+stage,progress.bundleState());
    }


    public void testGroupOpening() {
        GameProgress progress = new GameProgress(15);
        int i=0;
        assertOpened(progress,GameProgress.OPEN_GROUP);
        while(i<GameProgress.OPEN_GROUP-1)
            progress.setComplete(i++);

        assertOpened(progress, GameProgress.OPEN_GROUP);
        progress.setComplete(i++);
        assertOpened(progress,GameProgress.OPEN_GROUP*2);
        while(i<GameProgress.OPEN_GROUP*2-1)
            progress.setComplete(i++);
        assertOpened(progress,GameProgress.OPEN_GROUP*2);
        progress.setComplete(i++);
        assertOpened(progress,GameProgress.OPEN_GROUP*3);
        while(i<GameProgress.OPEN_GROUP*3-1)
            progress.setComplete(i++);
        assertFalse(progress.isAllComplete());
        progress.setComplete(i++);
        assertTrue(progress.isAllComplete());
        progress.clearProgress();
        assertOpened(progress,GameProgress.OPEN_GROUP);

    }

    public void testGroupSelect() {
        GameProgress progress = new GameProgress(1000);
        recreate(progress,"1");
        for(int i=0;i<GameProgress.OPEN_GROUP-1;i++) {
            assertEquals(i + 1, progress.nextOpened(i));
        }
        recreate(progress,"2");
        assertEquals(0, progress.nextOpened(GameProgress.OPEN_GROUP - 1));
        recreate(progress, "3");
        assertEquals(0, progress.nextOpened(GameProgress.OPEN_GROUP));
        recreate(progress, "4");

        progress.setComplete(1);
        recreate(progress, "5");
        assertEquals(0, progress.nextOpened(GameProgress.OPEN_GROUP - 1));
        progress.setComplete(0);
        recreate(progress, "6");
        assertEquals(2, progress.nextOpened(GameProgress.OPEN_GROUP - 1));
        progress.setComplete(3);
        recreate(progress, "7");
        assertEquals(2,progress.nextOpened(GameProgress.OPEN_GROUP-1));

        for(int i=3;i<GameProgress.OPEN_GROUP;i++) {
            progress.setComplete(i);
        }
        recreate(progress,"8");
        assertEquals(2, progress.nextOpened(GameProgress.OPEN_GROUP - 1));
        progress.setComplete(2);
        recreate(progress, "9");
        assertEquals(GameProgress.OPEN_GROUP,progress.nextOpened(GameProgress.OPEN_GROUP-1));
    }

}
