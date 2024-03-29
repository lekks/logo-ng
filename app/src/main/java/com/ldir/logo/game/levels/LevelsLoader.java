package com.ldir.logo.game.levels;

import android.util.Log;

import com.ldir.logo.game.GameLevel;
import com.ldir.logo.game.GameMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ldir on 14.02.2015.
 */
public class LevelsLoader {
    private GameLevel[] levels;

    public LevelsLoader(String json) {
        loadLevels(json);
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
                JSONArray jmap = level.getJSONArray("map");
                GameMap map = levels[i].map;
                for (int j = 0; j < jmap.length(); j++) {
                    String row = jmap.getString(j);
                    for (int k = 0; k < row.length(); k++) {
                        map.set(j,k,Character.getNumericValue(row.charAt(k)));
                    }
                }
                levels[i].tag = level.optString("tag", null);
                levels[i].time = level.optInt("time",0);

                if(levels[i].time == 0 )
                    levels[i].time = map.count()*2; //fixme unify

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
