package com.ldir.logo.game.levels;

import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.ldir.logo.game.GameLevel;
import com.ldir.logo.game.GameMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by Ldir on 07.03.2015.
 */
public class LevelMaker {
    Random rnd = new Random();
    SparseArray<GameLevel> mCache = new SparseArray<>();
    GameMap[] patterns;

    public LevelMaker(String json) {
        loadPatterns(json);
    }
    private GameLevel makeLevel(int id) {
        rnd.setSeed(id);
        GameLevel level = new GameLevel();
        int chips = rnd.nextInt(30) + 20;
        for (int i = 0; i < chips; ++i) {
            int row = rnd.nextInt(GameMap.ROWS);
            int col = rnd.nextInt(GameMap.COLS);
            level.map.gameMove(row, col);
        }
        level.time = 120;
        return level;
    }


    public GameLevel getLevel(int id){
        return makeLevel(id);
    }
//    GameLevel getLevel(int id){
//        GameLevel level = mCache.get(id);
//        if(level == null) {
//            level = makeLevel(id);
//            mCache.append(id,level);
//            return level;
//        } else {
//            return level;
//        }
//    }

    void loadPatterns(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray jlevels = obj.getJSONArray("patterns");
            patterns = new GameMap[jlevels.length()];
            Log.v("Patterns Loader", "Found " + jlevels.length() + " patterns");
            for (int i = 0; i < jlevels.length(); i++) {
                patterns[i]=new GameMap();
                JSONObject level = jlevels.getJSONObject(i);
                JSONArray jmap = level.getJSONArray("map");
                GameMap map = patterns[i];
                for (int j = 0; j < jmap.length(); j++) {
                    String row = jmap.getString(j);
                    for (int k = 0; k < row.length(); k++) {
                        char c = row.charAt(k);
                        if(c=='.')
                            map.set(j,k,0);
                        else
                            map.set(j,k,1);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }




    }
