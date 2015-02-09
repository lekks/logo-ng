package com.ldir.logo.game;

import java.util.BitSet;

/**
 * Created by Ldir on 08.02.2015.
 */
public class GameProgress {
    private BitSet completed;

    public GameProgress(int size) {
        completed = new BitSet(size);
    }

    public void clear() {
        completed.clear();
    }

    public void setCompleted(int level){
        completed.set(level);
    }

    public boolean isCompleted(int level){
        return completed.get(level);
    }

    public String getBundle() {
        return completed.toString();
    }

}
