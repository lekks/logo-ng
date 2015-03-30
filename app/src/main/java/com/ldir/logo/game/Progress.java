package com.ldir.logo.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ldir on 15.03.2015.
 */
public class Progress {
    public final static int OPEN_GROUP = 5;
    public int mLevelsCount;

    private final ArrayList<Integer> incomplete = new ArrayList<>();
    private int mOpened = 0;

    Progress(int levels){
        mLevelsCount = levels;
        clearProgress();
    }

    public void setLevelsCount(int levels){
        mLevelsCount = levels;
    }

    public void clearProgress() {
        incomplete.clear();
        mOpened = OPEN_GROUP;
        for (int i = 0; i < mOpened; ++i)
            incomplete.add(i);

    }

    public boolean isComplete(int level) {
        if (!isOpened( level))
            return false;
        if (incomplete.contains(level))
            return false;
        return true;

    }

    public boolean isOpened(int level) {
        if (level >= mOpened)
            return false;
        else
            return true;
    }

    public boolean isAllComplete(){
        return incomplete.isEmpty();
    }


    public void setComplete(int level){
        incomplete.remove((Object)level);
        int opened = mOpened;
        if(incomplete.isEmpty()) {
            mOpened += OPEN_GROUP;
            if (mOpened > mLevelsCount)
                mOpened = mLevelsCount;
            for (int i = opened; i < mOpened; ++i)
                incomplete.add(i);
        }
    }

    int nextOpened(int current) {
        if (isOpened(current + 1) && !isComplete(current + 1)) {
            return current + 1;
        }
        return Collections.min(incomplete);
    }


    String bundleState() {
        JSONObject json = new JSONObject();
        JSONArray jcompl = new JSONArray ();
        try {
            for (Integer i : incomplete) {
                jcompl.put(i);
            }
            json.put("incomplete",jcompl);
            json.put("opened",mOpened);
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
            if (obj.has("incomplete")) {
                JSONArray jlevels = obj.getJSONArray("incomplete");
                incomplete.clear();
                for (int i = 0; i < jlevels.length(); i++)
                    incomplete.add(jlevels.getInt(i));
                mOpened  = obj.optInt("opened_group",OPEN_GROUP);
            } else {
                clearProgress();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
