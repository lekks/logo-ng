package com.ldir.logo.game;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ldir on 14.02.2015.
 */
class LevelsLoader {
    private GameLevel[] levels;

    LevelsLoader(String filename) {
        loadLevels(filename);
    }

    private void loadLevels(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray jlevels = obj.getJSONArray("levels");
            levels = new GameLevel[jlevels.length()];
            Log.v("Levels Loader", "Found " + jlevels.length() + " levels");
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

    public int length(){
        return levels.length;
    }

    public GameLevel get(int n)
    {
        if(n < levels.length) {
            return levels[n];
        } else
            return null;
    }
}
