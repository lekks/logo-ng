package com.ldir.logo.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.BitSet;

/**
 * Created by Ldir on 08.02.2015.
 */
class GameProgress {
    private final BitSet completed = new BitSet(32);
    public final static int GROUP_SIZE = 5; // для тестирования
    private int lastOpened = 0;

    void clearProgress() {
        completed.clear();
        lastOpened = 0;
    }
    void setCompleted(int level){
        completed.set(level);
        tryOpenGroup(level);
    }
    boolean isCompleted(int level){
        return completed.get(level);
    }

    boolean isCompletedTill(int level){
        for(int i = 0; i<level; ++i){
            if(!completed.get(i))
                return false;
        }
        return true;
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

    boolean isOpened(int level){
        return level/GROUP_SIZE <= lastOpened;
    }

    int nextOpened(int current) {
        if (isOpened(current + 1) && !isCompleted(current + 1)) {
            return current + 1;
        }
        for (int i = 0; i < (lastOpened + 1) * GROUP_SIZE; ++i) {
            if (isOpened(i) && !isCompleted(i))
                return i;
        }
        return 0;
    }

    String bundleState() {
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

    void restoreState(String bundle) {
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
