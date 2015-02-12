package com.ldir.logo.game;

import android.util.Log;

import com.ldir.logo.activities.GameApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;

public class Levels {
    private static GameLevel[] levels;

    private static String getLevelsJSon(String name) {
        String json = null;
        try {
            InputStream is = GameApp.getAsset(name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }
        return json;
    }


    public static int length(){
        if(levels == null)
            loadLevels();
        return levels.length;
    }

    public static GameLevel get(int n)
    {
        if(levels == null)
            loadLevels();
        if(n < levels.length) {
            return levels[n];
        } else
            return null;
    }

    public static void loadLevels() {
        String json = getLevelsJSon("levels.json");
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray jlevels = obj.getJSONArray("levels");
            levels = new GameLevel[jlevels.length()];
            Log.v("Levels Loader","Found "+jlevels.length()+ " levels");
            for (int i = 0; i < jlevels.length(); i++) {
                levels[i]=new GameLevel();
                JSONObject level = jlevels.getJSONObject(i);
                levels[i].time = level.getInt("time");
                JSONArray jmap = level.getJSONArray("map");
                GameMap map = levels[i].map;
                for (int j = 0; j < jmap.length(); j++) {
                    String row = jmap.getString(j);
                    for (int k = 0; k < row.length(); k++) {
                        map.set(j,k,(byte)Character.getNumericValue(row.charAt(k)));
                    }
               }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }


    }


}