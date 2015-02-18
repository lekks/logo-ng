package com.ldir.logo.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.BitSet;

/**
 * Created by Ldir on 08.02.2015.
 */
class GameProgress {
    private BitSet completed = new BitSet(32);
//    private BitSet opened = new BitSet(32);
    public final static int GROUP_SIZE = 5; // для тестирования
    private int lastOpened = 0;

    public void clearProgress() {
        completed.clear();
        lastOpened = 0;
    }
    public void setCompleted(int level){
        completed.set(level);
        tryOpenGroup(level);
    }
    public boolean isCompleted(int level){
        return completed.get(level);
    }

    private void tryOpenGroup(int level) {
        int gr = level/GROUP_SIZE;
        if(lastOpened > gr)
            return;
        int cnt = 0;
        for (int i = gr*GROUP_SIZE; i < (gr+1)*GROUP_SIZE; ++i){
            if(isCompleted(i))
                ++cnt;
        }
        if(cnt == GROUP_SIZE){
            lastOpened=gr+1;
        }
    }

    public boolean isOpened(int level){
        return level/GROUP_SIZE <= lastOpened;
    }

    public String bundleState() {
        JSONObject json = new JSONObject();
        JSONArray jcompl = new JSONArray ();
        try {
            for (int i = completed.nextSetBit(0); i >= 0; i = completed.nextSetBit(i + 1))
                jcompl.put(i);
            json.put("completed",jcompl);
            json.put("opened_group",lastOpened);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json.toString();
    }

    public void restoreState(String bundle) {
        clearProgress();
        try {
            JSONObject obj = new JSONObject(bundle);
            lastOpened  = obj.optInt("opened_group",0);
            JSONArray jlevels = obj.getJSONArray("completed");
            for (int i = 0; i < jlevels.length(); i++)
                setCompleted(jlevels.getInt(i));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
