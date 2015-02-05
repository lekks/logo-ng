package com.ldir.logo;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.ldir.logo.game.MissionLoader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class LogoTest extends ApplicationTestCase<Application> {
    public LogoTest() {
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



    public void testSomething() {
        //Log.i("Test JSON", MissionLoader.loadJSONFromAsset());
        MissionLoader.loadLevels();
        assertEquals(true,true);
    }
    public void testOther() {
        Log.i("Test", "Sprites test 1");
//        assertEquals(false,true);
    }

}