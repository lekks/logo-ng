package com.ldir.logo.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.BitSet;

/**
 * Created by Ldir on 08.02.2015.
 */
public class GameProgress {
    private BitSet completed = new BitSet(32);
    private BitSet opened = new BitSet(32);

    public void clearProgress() {
        completed.clear();
        opened.clear();
    }
    public void setCompleted(int level){
        completed.set(level);
    }
    public boolean isCompleted(int level){
        return completed.get(level);
    }
    public void setOpened(int level){
        opened.set(level);
    }
    public boolean isOpened(int level){
        return opened.get(level);
    }

    public String bundleState() {
        JSONObject json = new JSONObject();
        JSONArray jcompl = new JSONArray ();
        JSONArray jopend = new JSONArray ();
        try {
            for (int i = completed.nextSetBit(0); i >= 0; i = completed.nextSetBit(i + 1))
                jcompl.put(i);
            for (int i = opened.nextSetBit(0); i >= 0; i = opened.nextSetBit(i + 1))
                jopend.put(i);
            json.put("completed",jcompl);
            json.put("opened",jopend);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json.toString();
    }

    public void restoreState(String bundle) {
        clearProgress();
    }
}
