package com.ldir.logo;

import android.app.Application;
import android.graphics.Bitmap;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.ldir.logo.fieldviews.render.Sprite;


/**
 * Created by Ldir on 24.01.2015.
 */

public class SpritesTest extends ApplicationTestCase<Application> {
    public SpritesTest () {
        super(Application.class);
    }


    public void testSprite() {
        Log.i("Test", "Sprites test 1");
        //assertEquals(false,true);
        assertEquals(0,Sprite.countCacheForTest());
        Bitmap pic=Sprite.get(R.drawable.s1,10);
        assertEquals(10,pic.getWidth());
        assertNotNull(pic);
        assertEquals(1,Sprite.countCacheForTest());
        assertEquals(pic,Sprite.get(R.drawable.s1,10));
        assertEquals(1,Sprite.countCacheForTest());
        pic=Sprite.get(R.drawable.s1,16);
        assertEquals(16,pic.getWidth());
        assertEquals(2,Sprite.countCacheForTest());
        Sprite.get(R.drawable.s1,10);
        assertEquals(2,Sprite.countCacheForTest());
        Sprite.get(R.drawable.s2,10);
        assertEquals(3,Sprite.countCacheForTest());
    }

}