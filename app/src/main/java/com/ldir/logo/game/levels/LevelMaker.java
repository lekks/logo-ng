package com.ldir.logo.game.levels;

import android.util.Log;

import com.ldir.logo.game.GameLevel;
import com.ldir.logo.game.GameMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Ldir on 07.03.2015.
 */
public class LevelMaker {
    private class Pos {
        byte row;
        byte col;
    }


    Random rnd = new Random();
//    SparseArray<GameLevel> mCache = new SparseArray<>();
    Pattern[] patterns;
    final GameMap fullPatt = new GameMap();
    private static Pos cellsList[] = new Pos[GameMap.ROWS*GameMap.COLS];

    public LevelMaker(String json) {
        loadPatterns(json);
        for(int i=0;i<cellsList.length;++i) {
            cellsList[i] = new Pos();
        }
        fullPatt.fill(1);
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



    // Implementing Fisher–Yates shuffle
    static final void  shuffleArray(Pos[] ar, int length,Random rnd)
    {
        for (int i = length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Pos a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    void generateLevel(GameMap level, int seed, GameMap patt, int flood) {
        rnd.setSeed(seed);
        int pcnt=0;
        for(byte i=0;i<GameMap.ROWS;i++){
            for(byte j=0;j<GameMap.COLS;j++){
                if(patt.get(i,j)>0) {
                    cellsList[pcnt].row=i;
                    cellsList[pcnt].col=j;
                    pcnt++;
                }
            }
        }

        shuffleArray(cellsList, pcnt, rnd);

        level.fill(0);
        int chips = pcnt*flood/100;
        for (int i = 0; i < chips; ++i) {
            level.gameMove(cellsList[i].row, cellsList[i].col);
        }
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
            patterns = new Pattern[jlevels.length()];
            Log.v("Patterns Loader", "Found " + jlevels.length() + " patterns");
            for (int i = 0; i < jlevels.length(); i++) {
                JSONObject level = jlevels.getJSONObject(i);
                JSONArray jmap = level.getJSONArray("map");
                GameMap map = new GameMap();
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
                patterns[i] = new Pattern(map);
            }
            Arrays.sort(patterns);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}

class Pattern implements Comparable<Pattern>{
    GameMap map;
    int count;
    Pattern(GameMap map)
    {
        this.map = map;
        count = map.count();
    }

    @Override
    public int compareTo(Pattern another) {
        return this.count < another.count ? -1 : (this.count == another.count ? 0 : 1);
    }
}
